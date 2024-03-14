INSERT INTO tabou_consommation_espace (libelle, code) VALUES ('Coup parti en extension urbaine', 'COUP_PARTI_EXTENSION');
INSERT INTO tabou_consommation_espace (libelle, code) VALUES ('Coup parti en renouvellement urbain', 'COUP_PARTI_RENOUVLMT');

UPDATE tabou_maitrise_ouvrage SET libelle = 'inter', code = 'inter' WHERE id = 3;
UPDATE tabou_operation SET id_maitrise_ouvrage = 2 WHERE id_maitrise_ouvrage = 3;
UPDATE tabou_maitrise_ouvrage SET libelle = 'Intercommunale', code = 'INTERCOMMUNALE' WHERE id = 2;

UPDATE tabou_maitrise_ouvrage SET libelle = 'com', code = 'com' WHERE id = 1;
UPDATE tabou_operation SET id_maitrise_ouvrage = 3 WHERE id_maitrise_ouvrage = 1;
UPDATE tabou_maitrise_ouvrage SET libelle = 'Communale', code = 'COMMUNALE' WHERE id = 3;

UPDATE tabou_maitrise_ouvrage SET libelle = 'metro', code = 'metro' WHERE id = 4;
UPDATE tabou_operation SET id_maitrise_ouvrage = 1 WHERE id_maitrise_ouvrage = 4;
UPDATE tabou_maitrise_ouvrage SET libelle = 'Métropolitaine', code = 'METROPOLITAINE' WHERE id = 1;

UPDATE tabou_maitrise_ouvrage SET libelle = 'Privé', code = 'PRIVE' WHERE id = 4;