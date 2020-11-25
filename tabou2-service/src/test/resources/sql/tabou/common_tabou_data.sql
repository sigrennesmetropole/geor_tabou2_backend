-----------------------------------
-- Insertion des données statiques
-----------------------------------

-- Insertion des étapes d'un programme

INSERT INTO tabou_etape_programme (libelle, code, mode, type, remove_restriction) VALUES ('En projet', 'EN_PROJET_OFF', 'OFF', 'START', false);
INSERT INTO tabou_etape_programme (libelle, code, mode, type, remove_restriction) VALUES ('En étude', 'EN_ETUDE_OFF', 'OFF', 'NORMAL', false);
INSERT INTO tabou_etape_programme (libelle, code, mode, type, remove_restriction) VALUES ('Annulé', 'ANNULE_OFF', 'OFF', 'END', false);

INSERT INTO tabou_etape_programme (libelle, code, mode, type, remove_restriction) VALUES ('En projet', 'EN_PROJET_PUBLIC', 'PUBLIC', 'START', true);
INSERT INTO tabou_etape_programme (libelle, code, mode, type, remove_restriction) VALUES ('En étude', 'EN_ETUDE_PUBLIC', 'PUBLIC', 'NORMAL', true);
INSERT INTO tabou_etape_programme (libelle, code, mode, type, remove_restriction) VALUES ('En chantier', 'EN_CHANTIER_PUBLIC', 'PUBLIC', 'NORMAL', false);
INSERT INTO tabou_etape_programme (libelle, code, mode, type, remove_restriction) VALUES ('Achevé', 'ACHEVE_PUBLIC', 'PUBLIC', 'NORMAL', false);
INSERT INTO tabou_etape_programme (libelle, code, mode, type, remove_restriction) VALUES ('Clôturé', 'CLOTURE_PUBLIC', 'PUBLIC', 'END', false);
INSERT INTO tabou_etape_programme (libelle, code, mode, type, remove_restriction) VALUES ('Annulé', 'ANNULE_PUBLIC', 'PUBLIC', 'NORMAL', false);

-- Insertion des étapes suivants pour une étape programme

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'EN_PROJET_OFF' and tep2.code = 'EN_ETUDE_OFF';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'EN_PROJET_OFF' and tep2.code = 'ANNULE_OFF';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'EN_PROJET_OFF' and tep2.code = 'EN_PROJET_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'EN_ETUDE_OFF' and tep2.code = 'ANNULE_OFF';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'EN_ETUDE_OFF' and tep2.code = 'EN_ETUDE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'EN_PROJET_PUBLIC' and tep2.code = 'EN_ETUDE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'EN_PROJET_PUBLIC' and tep2.code = 'ANNULE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'EN_ETUDE_PUBLIC' and tep2.code = 'EN_CHANTIER_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'EN_ETUDE_PUBLIC' and tep2.code = 'ANNULE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'EN_CHANTIER_PUBLIC' and tep2.code = 'ACHEVE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'EN_CHANTIER_PUBLIC' and tep2.code = 'ANNULE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'ACHEVE_PUBLIC' and tep2.code = 'CLOTURE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'ACHEVE_PUBLIC' and tep2.code = 'ANNULE_PUBLIC';

INSERT INTO tabou_etape_programme_workflow (id_etape_programme, id_etape_programme_next)
select tep1.id_etape_programme as id_etape_programme, tep2.id_etape_programme as id_etape_programme_next
from tabou_etape_programme tep1, tabou_etape_programme tep2
where tep1.code = 'ANNULE_PUBLIC' and tep2.code = 'CLOTURE_PUBLIC';


-- Insertion des étapes d'une opération

INSERT INTO tabou_etape_operation (libelle, code, mode, type, remove_restriction) VALUES ('En projet', 'EN_PROJET_OFF', 'OFF', 'START', false);
INSERT INTO tabou_etape_operation (libelle, code, mode, type, remove_restriction) VALUES ('En étude', 'EN_ETUDE_OFF', 'OFF', 'NORMAL', false);
INSERT INTO tabou_etape_operation (libelle, code, mode, type, remove_restriction) VALUES ('Annulé', 'ANNULE_OFF', 'OFF', 'END', false);

INSERT INTO tabou_etape_operation (libelle, code, mode, type, remove_restriction) VALUES ('En projet', 'EN_PROJET_PUBLIC', 'PUBLIC', 'START', true);
INSERT INTO tabou_etape_operation (libelle, code, mode, type, remove_restriction) VALUES ('En étude', 'EN_ETUDE_PUBLIC', 'PUBLIC', 'NORMAL', true);
INSERT INTO tabou_etape_operation (libelle, code, mode, type, remove_restriction) VALUES ('Opérationnel', 'OPERATIONNEL_PUBLIC', 'PUBLIC', 'NORMAL', false);
INSERT INTO tabou_etape_operation (libelle, code, mode, type, remove_restriction) VALUES ('Clôturé', 'CLOTURE_PUBLIC', 'PUBLIC', 'END', false);
INSERT INTO tabou_etape_operation (libelle, code, mode, type, remove_restriction) VALUES ('Annulé', 'ANNULE_PUBLIC', 'PUBLIC', 'NORMAL', false);

-- Insertion des étapes suivants pour une étape programme

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'EN_PROJET_OFF' and teo2.code = 'EN_ETUDE_OFF';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'EN_PROJET_OFF' and teo2.code = 'ANNULE_OFF';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'EN_PROJET_OFF' and teo2.code = 'EN_PROJET_PUBLIC';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'EN_ETUDE_OFF' and teo2.code = 'ANNULE_OFF';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'EN_ETUDE_OFF' and teo2.code = 'EN_ETUDE_PUBLIC';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'EN_PROJET_PUBLIC' and teo2.code = 'EN_ETUDE_PUBLIC';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'EN_PROJET_PUBLIC' and teo2.code = 'ANNULE_PUBLIC';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'EN_ETUDE_PUBLIC' and teo2.code = 'OPERATIONNEL_PUBLIC';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'EN_ETUDE_PUBLIC' and teo2.code = 'ANNULE_PUBLIC';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'OPERATIONNEL_PUBLIC' and teo2.code = 'CLOTURE_PUBLIC';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'OPERATIONNEL_PUBLIC' and teo2.code = 'ANNULE_PUBLIC';

INSERT INTO tabou_etape_operation_workflow (id_etape_operation, id_etape_operation_next)
select teo1.id_etape_operation as id_etape_operation, teo2.id_etape_operation as id_etape_operation_next
from tabou_etape_operation teo1, tabou_etape_operation teo2
where teo1.code = 'ANNULE_PUBLIC' and teo2.code = 'CLOTURE_PUBLIC';


-- Insertion des natures d'une opération

INSERT INTO tabou_nature (libelle) VALUES ('ZAC');
INSERT INTO tabou_nature (libelle) VALUES ('ZA');
INSERT INTO tabou_nature (libelle) VALUES ('En diffus');


-- Insertion des types d'évènements

INSERT INTO tabou_type_evenement (libelle, code, systeme) VALUES ('Reunion', 'REUNION', false);
INSERT INTO tabou_type_evenement (libelle, code, systeme) VALUES ('Ouverture chantier', 'OUVERTURE_CHANTIER', false);
INSERT INTO tabou_type_evenement (libelle, code, systeme) VALUES ('Instruction', 'INSTRUCTION', false);
INSERT INTO tabou_type_evenement (libelle, code, systeme) VALUES ('Recours', 'RECOURS', false);
INSERT INTO tabou_type_evenement (libelle, code, systeme) VALUES ('Modification etape', 'ETAPE_UPDATED', true);


-- Insertion des vocations

INSERT INTO tabou_vocation (libelle, code) VALUES ('Habitat', 'HABITAT');
INSERT INTO tabou_vocation (libelle, code) VALUES ('Mixte', 'MIXTE');
INSERT INTO tabou_vocation (libelle, code) VALUES ('Activité, Mobilité', 'ACTIVITE');
INSERT INTO tabou_vocation (libelle, code) VALUES ('Mobilité', 'MOBILITE');
INSERT INTO tabou_vocation (libelle, code) VALUES ('Équipement public', 'EQUIPEMENT_PUBLIC');
INSERT INTO tabou_vocation (libelle, code) VALUES ('Espace vert', 'ESPACE_VERT');
INSERT INTO tabou_vocation (libelle, code) VALUES ('Autre', 'AUTRE');


-- Insertion des décisions

INSERT INTO tabou_decision (libelle, code) VALUES ('Arrêté préfectoral', 'ARRET_PREF');
INSERT INTO tabou_decision (libelle, code) VALUES ('Délibération du Conseil de Rennes Métropole', 'DELIBERATION_CONSEIL_RM');
INSERT INTO tabou_decision (libelle, code) VALUES ('Délibération du Conseil Municipal', 'DELIBERATION_CONSEIL_M');
INSERT INTO tabou_decision (libelle, code) VALUES ('Non précisé', 'NON_PRECISE');


-- Insertion des maitrises d'ouvrage

INSERT INTO tabou_maitrise_ouvrage (libelle, code) VALUES ('Communale', 'COMMUNALE');
INSERT INTO tabou_maitrise_ouvrage (libelle, code) VALUES ('Communautaire', 'COMMUNAUTAIRE');
INSERT INTO tabou_maitrise_ouvrage (libelle, code) VALUES ('Intercommunale', 'INTERCOMMUNALE');
INSERT INTO tabou_maitrise_ouvrage (libelle, code) VALUES ('Métropolitaine', 'METROPOLITAINE');


-- Insertion des modes d'aménagement

INSERT INTO tabou_mode_amenagement (libelle, code) VALUES ('Concession', 'CONCESSION');
INSERT INTO tabou_mode_amenagement (libelle, code) VALUES ('CPA', 'CPA');
INSERT INTO tabou_mode_amenagement (libelle, code) VALUES ('Régie directe', 'REGIE');


-- Insertion des consommations d'expace

INSERT INTO tabou_consommation_espace (libelle, code) VALUES ('Extension urbaine', 'EXTENSION');
INSERT INTO tabou_consommation_espace (libelle, code) VALUES ('Renouvellement urbain', 'RENOUVELLEMENT');
INSERT INTO tabou_consommation_espace (libelle, code) VALUES ('Non précisé', 'NON_PRECISE');
