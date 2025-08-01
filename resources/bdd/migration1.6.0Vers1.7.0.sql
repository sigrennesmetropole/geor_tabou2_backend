-- Modification du workflow, ajout de l'étape "En chantier"
INSERT INTO tabou_etape_operation (code, libelle, mode, remove_restriction, type) VALUES ('EN_CHANTIER_PUBLIC','En chantier', 'PUBLIC',false,'NORMAL');

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select tep1.id_etape_operation as id_etape_operation, tep2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation tep1, tabou_etape_operation tep2
where tep1.code = 'EN_CHANTIER_PUBLIC' and tep2.code = 'OPERATIONNEL_PUBLIC';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select tep1.id_etape_operation as id_etape_operation, tep2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation tep1, tabou_etape_operation tep2
where tep1.code = 'EN_CHANTIER_PUBLIC' and tep2.code = 'ANNULE_PUBLIC';

DELETE FROM tabou_etape_operation_workflow
WHERE id_etape_operation = (select tep1.id_etape_operation as id_etape_operation
        from tabou_etape_operation tep1
        where tep1.code = 'EN_ETUDE_PUBLIC')
AND id_etape_operation_next = (select tep2.id_etape_operation as id_etape_operation_next
    from tabou_etape_operation tep2
    where tep2.code = 'OPERATIONNEL_PUBLIC');

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select tep1.id_etape_operation as id_etape_operation, tep2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation tep1, tabou_etape_operation tep2
where tep1.code = 'EN_ETUDE_PUBLIC' and tep2.code = 'EN_CHANTIER_PUBLIC';

-- Ajout indicateur Habitat favorable au vieillissement
ALTER TABLE tabou_programme ADD COLUMN IF NOT EXISTS logements_habitat_favorable_vieillissement integer;
ALTER TABLE tabou_operation ADD COLUMN IF NOT EXISTS logements_habitat_favorable_vieillissement integer;

-- Correction de certaines valeurs des listes et du worflow opération
UPDATE tabou_mode_amenagement SET libelle = 'Autre' WHERE code = 'CPA';
UPDATE tabou_mode_amenagement SET libelle = 'Régie' WHERE code = 'REGIE';
UPDATE tabou_mode_amenagement SET libelle = 'Concession d''aménagement' WHERE code = 'CONCESSION';
UPDATE tabou_vocation SET libelle = 'Activités' WHERE code = 'ACTIVITE';
UPDATE tabou_etape_operation SET libelle = 'Réalisé' WHERE code = 'OPERATIONNEL_PUBLIC';

-- Mise en place d'une colonne ordre dans des listes
ALTER TABLE tabou_mode_amenagement ADD COLUMN IF NOT EXISTS order_ INTEGER DEFAULT 99 NOT NULL;
UPDATE tabou_mode_amenagement SET order_ = '5' WHERE code = 'REGIE';
UPDATE tabou_mode_amenagement SET order_ = '10' WHERE code = 'CONCESSION';
UPDATE tabou_mode_amenagement SET order_ = '15' WHERE code = 'CPA';

ALTER TABLE tabou_outil_amenagement ADD COLUMN IF NOT EXISTS order_ INTEGER DEFAULT 99 NOT NULL;
UPDATE tabou_outil_amenagement SET order_ = '5' WHERE code = 'ZAC';
UPDATE tabou_outil_amenagement SET order_ = '10' WHERE code = 'PA';
UPDATE tabou_outil_amenagement SET order_ = '15' WHERE code = 'DP';
UPDATE tabou_outil_amenagement SET order_ = '20' WHERE code = 'PC';
UPDATE tabou_outil_amenagement SET order_ = '25' WHERE code = 'PC_DIVISION';

ALTER TABLE tabou_vocation ADD COLUMN IF NOT EXISTS order_ INTEGER DEFAULT 99 NOT NULL;
UPDATE tabou_vocation SET order_ = '5' WHERE code = 'HABITAT';
UPDATE tabou_vocation SET order_ = '10' WHERE code = 'ACTIVITE';
UPDATE tabou_vocation SET order_ = '15' WHERE code = 'MOBILITE';
UPDATE tabou_vocation SET order_ = '20' WHERE code = 'EQUIPEMENT_PUBLIC';
UPDATE tabou_vocation SET order_ = '25' WHERE code = 'ESPACE_VERT';
UPDATE tabou_vocation SET order_ = '30' WHERE code = 'MIXTE';
UPDATE tabou_vocation SET order_ = '35' WHERE code = 'AUTRE';

ALTER TABLE tabou_operation ADD COLUMN IF NOT EXISTS id_parent bigint;