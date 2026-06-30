-- Migration 2.2.0 -> 2.3.0 (idempotent, rejouable sans doublon)

SET search_path TO tabou2, public;

-- 1. Nouvelles colonnes prix / hébergement

-- Hébergement résidence sénior sur opération et programme
ALTER TABLE tabou_operation
    ADD COLUMN IF NOT EXISTS hebergement_resid_senior_prevu integer;
ALTER TABLE tabou_operation
    ADD COLUMN IF NOT EXISTS hebergement_resid_senior_realise integer;

-- Prix logements libres / terrain à bâtir sur programme
ALTER TABLE tabou_programme
    ADD COLUMN IF NOT EXISTS prix_logts_libres_m2_shab_prevu double precision;
ALTER TABLE tabou_programme
    ADD COLUMN IF NOT EXISTS prix_logts_libres_m2_shab_realise double precision;
ALTER TABLE tabou_programme
    ADD COLUMN IF NOT EXISTS prix_terrain_batir_m2_prevu double precision;
ALTER TABLE tabou_programme
    ADD COLUMN IF NOT EXISTS prix_terrain_batir_m2_realise double precision;
ALTER TABLE tabou_programme
    ADD COLUMN IF NOT EXISTS hebergement_resid_senior_prevu integer;
ALTER TABLE tabou_programme
    ADD COLUMN IF NOT EXISTS hebergement_resid_senior_realise integer;

-- 2. Logements spécifiques & programmation habitat

-- Référentiel des types d'accession
CREATE TABLE IF NOT EXISTS tabou_type_accession_logement
(
    id_type_accession_logement bigserial,
    code                       varchar(30)  NOT NULL,
    libelle                    varchar(100) NOT NULL,
    date_debut                 timestamp    NOT NULL,
    date_fin                   timestamp,
    ordre                      integer      NOT NULL,
    PRIMARY KEY (id_type_accession_logement)
);

-- Portées applicables à chaque type d'accession
CREATE TABLE IF NOT EXISTS tabou_type_accession_logement_portee
(
    id_type_accession_logement bigint      NOT NULL,
    portee                     varchar(20) NOT NULL,
    CONSTRAINT chk_portee_values CHECK (portee IN ('OPERATION', 'PROGRAMME', 'SECTEUR')),
    CONSTRAINT fk_portee_type_accession
        FOREIGN KEY (id_type_accession_logement)
            REFERENCES tabou_type_accession_logement (id_type_accession_logement)
            ON DELETE CASCADE
);

-- Référentiel des types de logement spécifique
CREATE TABLE IF NOT EXISTS tabou_type_logement
(
    id_type_logement bigserial,
    code             varchar(30)  NOT NULL,
    libelle          varchar(100) NOT NULL,
    date_debut       timestamp    NOT NULL,
    date_fin         timestamp,
    ordre            integer      NOT NULL,
    PRIMARY KEY (id_type_logement)
);

-- Groupe de logements spécifiques rattaché à une accession
CREATE TABLE IF NOT EXISTS tabou_logements_specifiques
(
    id_logements_specifiques   bigserial,
    id_type_accession_logement bigint,
    valeur                     integer,
    PRIMARY KEY (id_logements_specifiques),
    CONSTRAINT fk_logements_sp_type_accession
        FOREIGN KEY (id_type_accession_logement)
            REFERENCES tabou_type_accession_logement (id_type_accession_logement)
);

-- Détail par type de logement d'un groupe
CREATE TABLE IF NOT EXISTS tabou_logement_specifique
(
    id_logement_specifique   bigserial,
    id_logements_specifiques bigint NOT NULL,
    id_type_logement         bigint NOT NULL,
    valeur_prevue            integer,
    valeur_realisee          integer,
    PRIMARY KEY (id_logement_specifique),
    CONSTRAINT fk_logement_sp_logements_specifiques
        FOREIGN KEY (id_logements_specifiques)
            REFERENCES tabou_logements_specifiques (id_logements_specifiques),
    CONSTRAINT fk_logement_sp_type_logement
        FOREIGN KEY (id_type_logement)
            REFERENCES tabou_type_logement (id_type_logement)
);

-- Programmation habitat
CREATE TABLE IF NOT EXISTS tabou_programmation_habitat
(
    id_programmation_habitat bigserial,
    nb_logements             integer,
    nb_logements_hfv         integer,
    surface_shab             double precision,
    PRIMARY KEY (id_programmation_habitat)
);

-- Jointure programmation habitat <-> logements spécifiques
CREATE TABLE IF NOT EXISTS tabou_prog_habitat_logements_sp
(
    id                       bigserial,
    id_programmation_habitat bigint NOT NULL,
    id_logements_specifiques bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_prog_habitat_logements_sp UNIQUE (id_programmation_habitat, id_logements_specifiques),
    CONSTRAINT fk_prog_hab_ls_programmation
        FOREIGN KEY (id_programmation_habitat)
            REFERENCES tabou_programmation_habitat (id_programmation_habitat),
    CONSTRAINT fk_prog_hab_ls_logements_sp
        FOREIGN KEY (id_logements_specifiques)
            REFERENCES tabou_logements_specifiques (id_logements_specifiques)
);

-- Jointure opération <-> logements spécifiques
CREATE TABLE IF NOT EXISTS tabou_operation_logements_sp
(
    id_operation             bigint NOT NULL,
    id_logements_specifiques bigint NOT NULL,
    PRIMARY KEY (id_operation, id_logements_specifiques),
    CONSTRAINT fk_op_ls_operation
        FOREIGN KEY (id_operation) REFERENCES tabou_operation (id_operation),
    CONSTRAINT fk_op_ls_logements_sp
        FOREIGN KEY (id_logements_specifiques) REFERENCES tabou_logements_specifiques (id_logements_specifiques)
);

-- Colonne de lien programme -> programmation habitat
ALTER TABLE tabou_programme
    ADD COLUMN IF NOT EXISTS id_programmation_habitat bigint;

-- 3. Données référentielles

-- Types d'accession
INSERT INTO tabou_type_accession_logement (code, libelle, ordre, date_debut, date_fin)
SELECT v.code, v.libelle, v.ordre, TIMESTAMP '2026-01-01 00:00:00', NULL
FROM (VALUES ('ACCESS_AIDE', 'Accession aidée', 1),
             ('ACCESS_LIBRE', 'Accession libre', 2),
             ('ACCESS_MAITRISE', 'Accession maîtrisée', 3),
             ('LOCATIF_AIDE', 'Locatif aidé', 4),
             ('LOCATIF_REG_HLM', 'Locatif régulé HLM', 5),
             ('LOCATIF_REG_PRIVE', 'Locatif régulé privé', 6)) AS v(code, libelle, ordre)
WHERE NOT EXISTS (SELECT 1 FROM tabou_type_accession_logement t WHERE t.code = v.code);

-- Portées PROGRAMME et OPERATION pour chaque accession
INSERT INTO tabou_type_accession_logement_portee (id_type_accession_logement, portee)
SELECT t.id_type_accession_logement, 'PROGRAMME'
FROM tabou_type_accession_logement t
WHERE NOT EXISTS (SELECT 1
                  FROM tabou_type_accession_logement_portee p
                  WHERE p.id_type_accession_logement = t.id_type_accession_logement
                    AND p.portee = 'PROGRAMME');
INSERT INTO tabou_type_accession_logement_portee (id_type_accession_logement, portee)
SELECT t.id_type_accession_logement, 'OPERATION'
FROM tabou_type_accession_logement t
WHERE NOT EXISTS (SELECT 1
                  FROM tabou_type_accession_logement_portee p
                  WHERE p.id_type_accession_logement = t.id_type_accession_logement
                    AND p.portee = 'OPERATION');

-- Types de logement spécifique
INSERT INTO tabou_type_logement (code, libelle, ordre, date_debut, date_fin)
SELECT v.code, v.libelle, v.ordre, TIMESTAMP '2026-01-01 00:00:00', NULL
FROM (VALUES ('TOTAL', 'Logts spécifiques (total)', 1),
             ('RESID_JEUNES_ACTIFS', 'Résid. jeunes actifs', 2),
             ('RESID_ETUDIANTE', 'Résid. étudiante', 3),
             ('RESID_SENIOR', 'Résid. sénior', 4),
             ('HABITAT_PARTICIPATIF', 'Habitat participatif', 5),
             ('RESID_CO_LIVING', 'Résid. co-living', 6),
             ('ADAPTES_GDV', 'Adaptés GDV', 7),
             ('BEGUINAGE', 'Béguinage', 8),
             ('ADAPTES_INSERTION', 'Adaptés d''insertion', 9)) AS v(code, libelle, ordre)
WHERE NOT EXISTS (SELECT 1 FROM tabou_type_logement t WHERE t.code = v.code);

-- 4. Programmation habitat : DDL uniquement.
-- L'association des logements spécifiques (op/secteur/programme) est faite par
-- le script idempotent « update2.3.0_logements_specifiques.sql » joué après.

-- Contrainte FK programme -> programmation habitat
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_programme_programmation_habitat') THEN
            ALTER TABLE tabou_programme
                ADD CONSTRAINT fk_tabou_programme_programmation_habitat
                    FOREIGN KEY (id_programmation_habitat) REFERENCES tabou_programmation_habitat (id_programmation_habitat);
        END IF;
    END
$$;

-- Anciennes colonnes de tabou_programme conservées (la vue v_oa_programme en dépend).
-- À supprimer ultérieurement après adaptation de la vue :
-- ALTER TABLE tabou_programme DROP COLUMN IF EXISTS logements_locatif_aide_prevu;
-- ALTER TABLE tabou_programme DROP COLUMN IF EXISTS logements_access_aide_prevu;
-- ALTER TABLE tabou_programme DROP COLUMN IF EXISTS logements_locatif_regule_prive_prevu;
-- ALTER TABLE tabou_programme DROP COLUMN IF EXISTS logements_locatif_regule_hlm_prevu;
-- ALTER TABLE tabou_programme DROP COLUMN IF EXISTS logements_access_maitrise_prevu;
-- ALTER TABLE tabou_programme DROP COLUMN IF EXISTS logements_access_libre_prevu;
-- ALTER TABLE tabou_programme DROP COLUMN IF EXISTS logements_habitat_favorable_vieillissement;
-- ALTER TABLE tabou_programme DROP COLUMN IF EXISTS surface_shab;

-- 5. Index

CREATE INDEX IF NOT EXISTS idx_logements_sp_type_accession ON tabou_logements_specifiques (id_type_accession_logement);
CREATE INDEX IF NOT EXISTS idx_logement_sp_logements_specifiques ON tabou_logement_specifique (id_logements_specifiques);
CREATE INDEX IF NOT EXISTS idx_logement_sp_type_logement ON tabou_logement_specifique (id_type_logement);
CREATE INDEX IF NOT EXISTS idx_programme_programmation_habitat ON tabou_programme (id_programmation_habitat);
CREATE INDEX IF NOT EXISTS idx_prog_hab_ls_programmation_habitat ON tabou_prog_habitat_logements_sp (id_programmation_habitat);
CREATE INDEX IF NOT EXISTS idx_op_ls_operation ON tabou_operation_logements_sp (id_operation);
CREATE INDEX IF NOT EXISTS idx_op_ls_logements_sp ON tabou_operation_logements_sp (id_logements_specifiques);
CREATE INDEX IF NOT EXISTS idx_portee_type_accession ON tabou_type_accession_logement_portee (id_type_accession_logement);

-- 6. Jointures PLH & report PLH

-- Jointure opération <-> type PLH
CREATE TABLE IF NOT EXISTS tabou_operation_type_plh
(
    id_operation BIGINT NOT NULL,
    id_type_plh  BIGINT NOT NULL,
    PRIMARY KEY (id_operation, id_type_plh)
);
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_operation_type_plh_operation') THEN
            ALTER TABLE tabou_operation_type_plh
                ADD CONSTRAINT fk_tabou_operation_type_plh_operation FOREIGN KEY (id_operation) REFERENCES tabou_operation;
        END IF;
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_operation_type_plh_type') THEN
            ALTER TABLE tabou_operation_type_plh
                ADD CONSTRAINT fk_tabou_operation_type_plh_type FOREIGN KEY (id_type_plh) REFERENCES tabou_type_plh;
        END IF;
    END
$$;

-- Jointure programme <-> attribut PLH
CREATE TABLE IF NOT EXISTS tabou_programme_attribut_plh
(
    id_programme    BIGINT NOT NULL,
    id_attribut_plh BIGINT NOT NULL,
    PRIMARY KEY (id_programme, id_attribut_plh)
);
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_programme_attribut_plh_programme') THEN
            ALTER TABLE tabou_programme_attribut_plh
                ADD CONSTRAINT fk_tabou_programme_attribut_plh_programme FOREIGN KEY (id_programme) REFERENCES tabou_programme;
        END IF;
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_programme_attribut_plh_attribut') THEN
            ALTER TABLE tabou_programme_attribut_plh
                ADD CONSTRAINT fk_tabou_programme_attribut_plh_attribut FOREIGN KEY (id_attribut_plh) REFERENCES tabou_attribut_plh;
        END IF;
    END
$$;

-- Jointure opération <-> attribut PLH
CREATE TABLE IF NOT EXISTS tabou_operation_attribut_plh
(
    id_operation    BIGINT NOT NULL,
    id_attribut_plh BIGINT NOT NULL,
    PRIMARY KEY (id_operation, id_attribut_plh)
);
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_operation_attribut_plh_operation') THEN
            ALTER TABLE tabou_operation_attribut_plh
                ADD CONSTRAINT fk_tabou_operation_attribut_plh_operation FOREIGN KEY (id_operation) REFERENCES tabou_operation;
        END IF;
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tabou_operation_attribut_plh_attribut') THEN
            ALTER TABLE tabou_operation_attribut_plh
                ADD CONSTRAINT fk_tabou_operation_attribut_plh_attribut FOREIGN KEY (id_attribut_plh) REFERENCES tabou_attribut_plh;
        END IF;
    END
$$;

-- Reprise du lien programme -> attribut PLH avant suppression de la colonne id_programme
DO
$$
    BEGIN
        IF EXISTS (SELECT 1
                   FROM information_schema.columns
                   WHERE table_schema = 'tabou2'
                     AND table_name = 'tabou_attribut_plh'
                     AND column_name = 'id_programme') THEN
            INSERT INTO tabou_programme_attribut_plh (id_programme, id_attribut_plh)
            SELECT id_programme, id_attribut_plh
            FROM tabou_attribut_plh
            WHERE id_programme IS NOT NULL
            ON CONFLICT (id_programme, id_attribut_plh) DO NOTHING;
        END IF;
    END
$$;
ALTER TABLE tabou_attribut_plh
    DROP CONSTRAINT IF EXISTS fk_tabou_attribut_plh_programme;
ALTER TABLE tabou_attribut_plh
    DROP COLUMN IF EXISTS id_programme;

-- Champ de report PLH
ALTER TABLE tabou_type_plh
    ADD COLUMN IF NOT EXISTS sync_field VARCHAR(255);

-- 7. Droits applicatifs

GRANT ALL ON TABLE tabou_programmation_habitat TO tabou2;
GRANT ALL ON TABLE tabou_prog_habitat_logements_sp TO tabou2;
GRANT ALL ON TABLE tabou_logements_specifiques TO tabou2;
GRANT ALL ON TABLE tabou_logement_specifique TO tabou2;
GRANT ALL ON TABLE tabou_type_accession_logement TO tabou2;
GRANT ALL ON TABLE tabou_type_accession_logement_portee TO tabou2;
GRANT ALL ON TABLE tabou_type_logement TO tabou2;
GRANT ALL ON TABLE tabou_operation_logements_sp TO tabou2;
GRANT ALL ON TABLE tabou_operation_type_plh TO tabou2;
GRANT ALL ON TABLE tabou_programme_attribut_plh TO tabou2;
GRANT ALL ON TABLE tabou_operation_attribut_plh TO tabou2;

GRANT USAGE, SELECT ON SEQUENCE tabou_programmation_habitat_id_programmation_habitat_seq TO tabou2;
GRANT USAGE, SELECT ON SEQUENCE tabou_prog_habitat_logements_sp_id_seq TO tabou2;
GRANT USAGE, SELECT ON SEQUENCE tabou_logements_specifiques_id_logements_specifiques_seq TO tabou2;
GRANT USAGE, SELECT ON SEQUENCE tabou_logement_specifique_id_logement_specifique_seq TO tabou2;
GRANT USAGE, SELECT ON SEQUENCE tabou_type_accession_logement_id_type_accession_logement_seq TO tabou2;
GRANT USAGE, SELECT ON SEQUENCE tabou_type_logement_id_type_logement_seq TO tabou2;
