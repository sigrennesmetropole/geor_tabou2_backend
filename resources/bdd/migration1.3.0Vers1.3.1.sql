-- Ajout d'attributs dans tabou_consommation_espace
INSERT INTO tabou_consommation_espace (libelle, code) VALUES ('Coup parti en extension urbaine', 'COUP_PARTI_EXTENSION');
INSERT INTO tabou_consommation_espace (libelle, code) VALUES ('Coup parti en renouvellement urbain', 'COUP_PARTI_RENOUVLMT');

-- Mise à jour libelle de tabou_maitrise_ouvrage
UPDATE tabou_operation SET id_maitrise_ouvrage = 3 WHERE id_maitrise_ouvrage = 2;
DELETE FROM tabou_maitrise_ouvrage WHERE id = 2;
INSERT INTO tabou_maitrise_ouvrage (libelle, code) VALUES ('Privé', 'PRIVE');

-- Mise en place d'un ordre pour l'affichage
ALTER TABLE tabou_maitrise_ouvrage ADD order_ integer;
UPDATE tabou_maitrise_ouvrage SET order_ = 1 WHERE code = 'METROPOLITAINE';
UPDATE tabou_maitrise_ouvrage SET order_ = 2 WHERE code = 'INTERCOMMUNALE';
UPDATE tabou_maitrise_ouvrage SET order_ = 3 WHERE code = 'COMMUNALE';
UPDATE tabou_maitrise_ouvrage SET order_ = 4 WHERE code = 'PRIVE';
