-- https://github.com/sigrennesmetropole/geor_tabou2_front/issues/297 : Mise à jour des workflows

TRUNCATE TABLE tabou_etape_operation_workflow;
INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
VALUES
    -- Off 1 - Off 2
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF')),
    -- Off 2 - Public 1
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF')),
    -- Off - Annulé off
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_OFF')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_OFF')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF')),
    -- Public 1 - Public 2
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_PUBLIC')),
    -- Public 2 - Public 3
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC')),
    -- Public 3 - Public 4
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC')),
    -- Public 4 - Public 5
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'CLOTURE_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'CLOTURE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC')),
    -- Public - Annulé
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'CLOTURE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC')),
    ((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'CLOTURE_PUBLIC'));

TRUNCATE TABLE tabou_etape_programme_workflow;
INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
VALUES
    -- Off 1 - Off 2
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_PROJET_OFF'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_OFF')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_OFF'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_PROJET_OFF')),
    -- Off 2 - Public 1
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_OFF'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_PROJET_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_PROJET_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_OFF')),
    -- Off - Annulé off
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_PROJET_OFF'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_OFF')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_OFF'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_OFF')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_OFF'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_PROJET_OFF')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_OFF'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_OFF')),

    -- Public 1 - Public 2
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_PROJET_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_PROJET_PUBLIC')),
    -- Public 2 - Public 3
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_CHANTIER_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_CHANTIER_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_PUBLIC')),
    -- Public 3 - Public 4
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_CHANTIER_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ACHEVE_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ACHEVE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_CHANTIER_PUBLIC')),
    -- Public 4 - Public 5
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ACHEVE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'CLOTURE_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'CLOTURE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ACHEVE_PUBLIC')),
    -- Public - Annulé
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_PROJET_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_PROJET_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_ETUDE_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_CHANTIER_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'EN_CHANTIER_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ACHEVE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ACHEVE_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'CLOTURE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_PUBLIC')),
    ((SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_programme FROM tabou_etape_programme WHERE code = 'CLOTURE_PUBLIC'));

UPDATE tabou_etape_operation SET libelle = concat(libelle, ' (Off)') WHERE code like '%OFF' and libelle not like '%(Off)';
UPDATE tabou_etape_programme SET libelle = concat(libelle, ' (Off)') WHERE code like '%OFF' and libelle not like '%(Off)';

-- On souhaite laisser la possibilité de ne pas saisir les logements habitats favorables vieillissement pour les programmes. En attendant la valeur était à -1.
UPDATE tabou_programme SET logements_habitat_favorable_vieillissement = null WHERE logements_habitat_favorable_vieillissement = -1;

-- https://github.com/sigrennesmetropole/geor_tabou2_front/issues/299 : Ajout de nouveaux champs de programmation
CREATE TABLE IF NOT EXISTS tabou_programmation(id_programmation bigserial PRIMARY KEY, surface_bureaux double precision,
                                               surface_commerces double precision, surface_industrie double precision,
                                               surface_equipements double precision, surface_autres double precision);
ALTER TABLE IF EXISTS tabou_programme ADD COLUMN IF NOT EXISTS id_programmation bigint;

ALTER TABLE tabou_programme ADD CONSTRAINT fk_programmation FOREIGN KEY (id_programmation)
    REFERENCES tabou_programmation(id_programmation);

-- On ajoute un ordre sur les etapes de workflow
ALTER TABLE IF EXISTS tabou_etape_operation ADD COLUMN IF NOT EXISTS order_ integer;
ALTER TABLE IF EXISTS tabou_etape_programme ADD COLUMN IF NOT EXISTS order_ integer;

-- On crée un ordre
UPDATE tabou_etape_operation SET order_ = 0 WHERE code = 'EN_PROJET_OFF';
UPDATE tabou_etape_operation SET order_ = 5 WHERE code = 'EN_ETUDE_OFF';
UPDATE tabou_etape_operation SET order_ = 10 WHERE code = 'EN_PROJET_PUBLIC';
UPDATE tabou_etape_operation SET order_ = 15 WHERE code = 'EN_ETUDE_PUBLIC';
UPDATE tabou_etape_operation SET order_ = 20 WHERE code = 'EN_CHANTIER_PUBLIC';
UPDATE tabou_etape_operation SET order_ = 25 WHERE code = 'OPERATIONNEL_PUBLIC';
UPDATE tabou_etape_operation SET order_ = 30 WHERE code = 'CLOTURE_PUBLIC';
UPDATE tabou_etape_operation SET order_ = 35 WHERE code = 'ANNULE_OFF';
UPDATE tabou_etape_operation SET order_ = 40 WHERE code = 'ANNULE_PUBLIC';

UPDATE tabou_etape_programme SET order_ = 0 WHERE code = 'EN_PROJET_OFF';
UPDATE tabou_etape_programme SET order_ = 5 WHERE code = 'EN_ETUDE_OFF';
UPDATE tabou_etape_programme SET order_ = 10 WHERE code = 'EN_PROJET_PUBLIC';
UPDATE tabou_etape_programme SET order_ = 15 WHERE code = 'EN_ETUDE_PUBLIC';
UPDATE tabou_etape_programme SET order_ = 20 WHERE code = 'EN_CHANTIER_PUBLIC';
UPDATE tabou_etape_programme SET order_ = 25 WHERE code = 'ACHEVE_PUBLIC';
UPDATE tabou_etape_programme SET order_ = 30 WHERE code = 'CLOTURE_PUBLIC';
UPDATE tabou_etape_programme SET order_ = 35 WHERE code = 'ANNULE_OFF';
UPDATE tabou_etape_programme SET order_ = 40 WHERE code = 'ANNULE_PUBLIC';

-- On rend l'ordre obligatoire :
ALTER TABLE IF EXISTS tabou_etape_operation ALTER COLUMN order_ SET NOT NULL;
ALTER TABLE IF EXISTS tabou_etape_programme ALTER COLUMN order_ SET NOT NULL;

-- On renomme l'étape achevé en réalisé
UPDATE tabou_etape_programme SET libelle = 'Réalisé' WHERE code = 'ACHEVE_PUBLIC';
