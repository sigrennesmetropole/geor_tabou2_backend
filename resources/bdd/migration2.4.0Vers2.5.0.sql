-- Migration 2.4.0 -> 2.5.0 (idempotent, rejouable sans doublon)
-- Migration 2.4.0 -> 2.5.0 (idempotent, rejouable sans doublon)
-- Issue #318 : indicateur "Consommation d'espace"
--  - alignement sur le modèle date_debut / date_fin (comme tabou_type_accession_logement)
--  - retrait des valeurs "Coup parti ..." : réaffectation des opérations puis clôture (date_fin)

SET search_path TO tabou2, public;

-- 1. Colonnes de validité date_debut / date_fin
ALTER TABLE tabou_consommation_espace
    ADD COLUMN IF NOT EXISTS date_debut timestamp;
ALTER TABLE tabou_consommation_espace
    ADD COLUMN IF NOT EXISTS date_fin timestamp;

UPDATE tabou_consommation_espace SET date_debut = now() WHERE date_debut IS NULL;
ALTER TABLE tabou_consommation_espace ALTER COLUMN date_debut SET NOT NULL;

-- 2. Valeur "Mixte" (conservée dans le menu, ajoutée si absente)
INSERT INTO tabou_consommation_espace (libelle, code, date_debut)
SELECT 'Mixte', 'MIXTE', now()
WHERE NOT EXISTS (SELECT 1 FROM tabou_consommation_espace WHERE libelle = 'Mixte');

-- 3. Réaffectation des opérations utilisant les valeurs supprimées
--    (MIN(id) => valeur scalaire garantie ; IN => robuste aux doublons éventuels de code)
UPDATE tabou_operation
SET id_consommation_espace = (SELECT MIN(id) FROM tabou_consommation_espace WHERE code = 'EXTENSION')
WHERE id_consommation_espace IN (SELECT id FROM tabou_consommation_espace WHERE code = 'COUP_PARTI_EXTENSION');

UPDATE tabou_operation
SET id_consommation_espace = (SELECT MIN(id) FROM tabou_consommation_espace WHERE code = 'RENOUVELLEMENT')
WHERE id_consommation_espace IN (SELECT id FROM tabou_consommation_espace WHERE code = 'COUP_PARTI_RENOUVLMT');

-- 4. Clôture des valeurs "Coup parti ..." (plus sélectionnables, conservées pour l'historique)
UPDATE tabou_consommation_espace
SET date_fin = now()
WHERE code IN ('COUP_PARTI_EXTENSION', 'COUP_PARTI_RENOUVLMT')
  AND date_fin IS NULL;
-- Migration 2.4.0 -> 2.5.0 (issue #317) : ajout des services référents DJB, DEEI, DV.
-- Issue #317 : alignement des entités référentes sur le modèle date_debut / date_fin
-- (comme tabou_type_accession_logement) et ajout des services DJB, DEEI, DV.
-- Issue #317 : ajout du modèle de validité date_debut / date_fin sur les entités
-- référentes (comme tabou_type_accession_logement) et ajout des services DJB, DEEI, DV.
-- create_date / create_user (audit) sont conservées.

SET search_path TO tabou2, public;

-- Nouvelle colonne date_debut (début de validité). date_inactif -> date_fin (fin de validité).
ALTER TABLE tabou_entite_referente
    ADD COLUMN IF NOT EXISTS date_debut timestamp;

DO $$
    BEGIN
        IF EXISTS (SELECT 1 FROM information_schema.columns
                   WHERE table_schema = 'tabou2' AND table_name = 'tabou_entite_referente'
                     AND column_name = 'date_inactif') THEN
            ALTER TABLE tabou_entite_referente RENAME COLUMN date_inactif TO date_fin;
        END IF;
    END $$;

-- Les entités déjà présentes : date de début = leur date de création (create_date)
UPDATE tabou_entite_referente
SET date_debut = COALESCE(create_date, TIMESTAMP '2026-01-01 00:00:00')
WHERE date_debut IS NULL;
ALTER TABLE tabou_entite_referente ALTER COLUMN date_debut SET NOT NULL;

-- Nouveaux services : create_date = date_debut = maintenant
INSERT INTO tabou_entite_referente (code, libelle, date_debut, create_date)
SELECT v.code, v.libelle, now(), now()
FROM (VALUES ('DJB', 'DJB'),
             ('DEEI', 'DEEI'),
             ('DV', 'DV')) AS v(code, libelle)
WHERE NOT EXISTS (SELECT 1
                  FROM tabou_entite_referente e
                  WHERE e.code = v.code);

SET search_path TO tabou2, public;

-- Vocation principale propre au programme (nullable : tolérée sur les programmes existants)
ALTER TABLE tabou_programme
    ADD COLUMN IF NOT EXISTS id_vocation bigint;

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_programme_tabou_vocation') THEN
            ALTER TABLE tabou_programme
                ADD CONSTRAINT fk_tabou_programme_tabou_vocation
                    FOREIGN KEY (id_vocation) REFERENCES tabou_vocation (id);
        END IF;
    END
$$;

CREATE INDEX IF NOT EXISTS "idx_tabou_programme_id_vocation_fk" ON tabou_programme (id_vocation);

-- L'initialisation des vocations des programmes existants est faite par le script
-- optionnel « update2.5.0_programme_vocation.sql » (à jouer après validation client).

-- Issue #321 : indicateurs « Vocation (PLAE) » et « Renouvellement économique (PLAE) » des opérations / secteurs
CREATE TABLE IF NOT EXISTS tabou_vocation_plae (
    id         bigserial,
    libelle    varchar(255) NOT NULL,
    code       varchar(255) UNIQUE NOT NULL,
    date_debut timestamp NOT NULL DEFAULT now(),
    date_fin   timestamp,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tabou_renouvellement_eco_plae (
    id         bigserial,
    libelle    varchar(255) NOT NULL,
    code       varchar(255) UNIQUE NOT NULL,
    date_debut timestamp NOT NULL DEFAULT now(),
    date_fin   timestamp,
    PRIMARY KEY (id)
);

INSERT INTO tabou_vocation_plae (libelle, code)
SELECT v.libelle, v.code
FROM (VALUES (1, 'Productive', 'PRODUCTIVE'),
             (2, 'Commerciale', 'COMMERCIALE'),
             (3, 'Tertiaire', 'TERTIAIRE'),
             (4, 'Mixte', 'MIXTE')) AS v(ordre, libelle, code)
WHERE NOT EXISTS (SELECT 1 FROM tabou_vocation_plae p WHERE p.code = v.code)
ORDER BY v.ordre;

INSERT INTO tabou_renouvellement_eco_plae (libelle, code)
SELECT v.libelle, v.code
FROM (VALUES (1, 'En réserve', 'EN_RESERVE'),
             (2, 'En étude', 'EN_ETUDE'),
             (3, 'Opérationnelle', 'OPERATIONNELLE'),
             (4, 'Terminée', 'TERMINEE')) AS v(ordre, libelle, code)
WHERE NOT EXISTS (SELECT 1 FROM tabou_renouvellement_eco_plae r WHERE r.code = v.code)
ORDER BY v.ordre;

ALTER TABLE tabou_operation
    ADD COLUMN IF NOT EXISTS id_vocation_plae bigint;
ALTER TABLE tabou_operation
    ADD COLUMN IF NOT EXISTS id_renouvellement_eco_plae bigint;

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_operation_tabou_vocation_plae') THEN
            ALTER TABLE tabou_operation
                ADD CONSTRAINT fk_tabou_operation_tabou_vocation_plae
                    FOREIGN KEY (id_vocation_plae) REFERENCES tabou_vocation_plae (id);
        END IF;
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_operation_tabou_renouvellement_eco_plae') THEN
            ALTER TABLE tabou_operation
                ADD CONSTRAINT fk_tabou_operation_tabou_renouvellement_eco_plae
                    FOREIGN KEY (id_renouvellement_eco_plae) REFERENCES tabou_renouvellement_eco_plae (id);
        END IF;
    END
$$;

CREATE INDEX IF NOT EXISTS "idx_tabou_operation_id_vocation_plae_fk" ON tabou_operation (id_vocation_plae);
CREATE INDEX IF NOT EXISTS "idx_tabou_operation_id_renouvellement_eco_plae_fk" ON tabou_operation (id_renouvellement_eco_plae);

ALTER TABLE tabou_programme
    ADD COLUMN IF NOT EXISTS id_vocation_plae bigint,
    ADD COLUMN IF NOT EXISTS id_renouvellement_eco_plae bigint,
    ADD COLUMN IF NOT EXISTS id_vocation_za_suivi bigint,
    ADD COLUMN IF NOT EXISTS id_renouvellement_eco_za_suivi bigint,
    ADD COLUMN IF NOT EXISTS entreprises_deveco text,
    ADD COLUMN IF NOT EXISTS statut_propriete varchar(20),
    ADD COLUMN IF NOT EXISTS prix_vente integer,
    ADD COLUMN IF NOT EXISTS prix_redevance_annuelle integer,
    ADD COLUMN IF NOT EXISTS surface_previsionnelle integer,
    ADD COLUMN IF NOT EXISTS surface_construite double precision,
    ADD COLUMN IF NOT EXISTS date_option_reservation_prevue timestamp,
    ADD COLUMN IF NOT EXISTS date_option_reservation_reelle timestamp,
    ADD COLUMN IF NOT EXISTS date_option_cession_prevue timestamp,
    ADD COLUMN IF NOT EXISTS date_option_cession_reelle timestamp,
    ADD COLUMN IF NOT EXISTS date_signature_acte_cession_prevue timestamp,
    ADD COLUMN IF NOT EXISTS date_signature_acte_cession_reelle timestamp,
    ADD COLUMN IF NOT EXISTS duree_bail_prevue integer,
    ADD COLUMN IF NOT EXISTS duree_bail_reelle integer;

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_programme_tabou_vocation_plae') THEN
            ALTER TABLE tabou_programme
                ADD CONSTRAINT fk_tabou_programme_tabou_vocation_plae
                    FOREIGN KEY (id_vocation_plae) REFERENCES tabou_vocation_plae (id);
        END IF;
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_programme_tabou_renouvellement_eco_plae') THEN
            ALTER TABLE tabou_programme
                ADD CONSTRAINT fk_tabou_programme_tabou_renouvellement_eco_plae
                    FOREIGN KEY (id_renouvellement_eco_plae) REFERENCES tabou_renouvellement_eco_plae (id);
        END IF;
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_programme_vocation_za_suivi') THEN
            ALTER TABLE tabou_programme
                ADD CONSTRAINT fk_tabou_programme_vocation_za_suivi
                    FOREIGN KEY (id_vocation_za_suivi) REFERENCES tabou_vocation_plae (id);
        END IF;
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_programme_renouvellement_eco_za_suivi') THEN
            ALTER TABLE tabou_programme
                ADD CONSTRAINT fk_tabou_programme_renouvellement_eco_za_suivi
                    FOREIGN KEY (id_renouvellement_eco_za_suivi) REFERENCES tabou_renouvellement_eco_plae (id);
        END IF;
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'enum_statut_propriete_programme') THEN
            ALTER TABLE tabou_programme
                ADD CONSTRAINT enum_statut_propriete_programme
                    CHECK (statut_propriete IN ('CESSION', 'BAC'));
        END IF;
    END
$$;

CREATE INDEX IF NOT EXISTS "idx_tabou_programme_id_vocation_plae_fk" ON tabou_programme (id_vocation_plae);
CREATE INDEX IF NOT EXISTS "idx_tabou_programme_id_renouvellement_eco_plae_fk" ON tabou_programme (id_renouvellement_eco_plae);
CREATE INDEX IF NOT EXISTS "idx_tabou_programme_id_vocation_za_suivi_fk" ON tabou_programme (id_vocation_za_suivi);
CREATE INDEX IF NOT EXISTS "idx_tabou_programme_id_renouvellement_eco_za_suivi_fk" ON tabou_programme (id_renouvellement_eco_za_suivi);
