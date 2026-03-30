-- Ajout du champ prospectif sur les étapes des opérations uniquement
ALTER TABLE IF EXISTS tabou_etape_operation ADD COLUMN IF NOT EXISTS prospectif boolean NOT NULL DEFAULT false;
ALTER TABLE IF EXISTS tabou_etape_operation ADD COLUMN IF NOT EXISTS secteur boolean NOT NULL DEFAULT true;

-- Ajout de la nouvelle étape "En réflexion" pour les opérations (prospective, mode OFF, type START)
INSERT INTO tabou_etape_operation (libelle, code, mode, type, remove_restriction, secteur, prospectif, order_)
VALUES ('En réflexion (Off)', 'EN_REFLEXION_OFF', 'OFF', 'START', false, false, true, -10);

-- Marquage de l'étape "En projet (Off)" comme prospective pour les opérations
UPDATE tabou_etape_operation SET prospectif = true WHERE code = 'EN_PROJET_OFF';
UPDATE tabou_etape_operation SET secteur = false WHERE code = 'EN_PROJET_OFF';

-- L'ancien START "EN_PROJET_OFF" n'est plus le démarrage, c'est "EN_REFLEXION_OFF"
-- On change le type de EN_PROJET_OFF en NORMAL et on le renomme juste En Projet
UPDATE tabou_etape_operation SET type = 'NORMAL', libelle = 'En Projet (Off)' WHERE code = 'EN_PROJET_OFF';

-- Mise à jour de EN_ETUDE_OFF en START (nouveau point d'entrée)
UPDATE tabou_etape_operation SET type = 'START' WHERE code = 'EN_ETUDE_OFF';

-- Mise à jour de EN_ETUDE_PUBLIC en START (nouveau point d'entrée)
UPDATE tabou_etape_operation SET type = 'START' WHERE code = 'EN_ETUDE_PUBLIC';

-- Renommage de l'étape "Opérationnel" en "Réalisé"
UPDATE tabou_etape_operation SET libelle = 'Réalisé', code = 'REALISE_PUBLIC' WHERE code = 'OPERATIONNEL_PUBLIC';

TRUNCATE tabou_etape_operation_workflow;

INSERT INTO tabou_etape_operation_workflow(id_etape_operation, id_etape_operation_next)
VALUES
-- En réflexion <-> En projet
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_REFLEXION_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_REFLEXION_OFF')),
-- En projet <-> En étude (OFF)
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF')),
-- En étude (OFF) <-> En étude
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF')),
-- En projet <-> En étude
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF')),
-- En étude <-> En chantier
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC')),
-- En chantier <-> Réalisé
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC')),
-- Réalisé <-> Cloturé
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'CLOTURE_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'CLOTURE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC')),
-- XXX <-> Annulé (OFF)
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_REFLEXION_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_OFF')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_REFLEXION_OFF')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_OFF')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_OFF')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_OFF')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_OFF'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_OFF')),
-- XXX <-> Annulé
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_CHANTIER_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'OPERATIONNEL_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'CLOTURE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC')),
((SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'ANNULE_PUBLIC'), (SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'CLOTURE_PUBLIC'));

UPDATE tabou_operation SET id_etape_operation=(SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_ETUDE_PUBLIC') WHERE id_etape_operation=(SELECT id_etape_operation FROM tabou_etape_operation WHERE code = 'EN_PROJET_PUBLIC');
