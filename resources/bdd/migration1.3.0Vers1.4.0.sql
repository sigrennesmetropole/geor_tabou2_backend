-- Ajout d'attributs dans tabou_consommation_espace
INSERT INTO tabou_consommation_espace (libelle, code) VALUES ('Coup parti en extension urbaine', 'COUP_PARTI_EXTENSION');
INSERT INTO tabou_consommation_espace (libelle, code) VALUES ('Coup parti en renouvellement urbain', 'COUP_PARTI_RENOUVLMT');

-- Mise à jour libelle de tabou_maitrise_ouvrage
UPDATE tabou_operation SET id_maitrise_ouvrage = (SELECT id FROM tabou_maitrise_ouvrage WHERE code = 'INTERCOMMUNALE')
                       WHERE id_maitrise_ouvrage = (SELECT id FROM tabou_maitrise_ouvrage WHERE code = 'COMMUNAUTAIRE');
DELETE FROM tabou_maitrise_ouvrage WHERE code = 'COMMUNAUTAIRE';
INSERT INTO tabou_maitrise_ouvrage (libelle, code) VALUES ('Privé', 'PRIVE');

-- Mise en place d'un ordre pour l'affichage
ALTER TABLE tabou_maitrise_ouvrage ADD order_ integer;
UPDATE tabou_maitrise_ouvrage SET order_ = 1 WHERE code = 'METROPOLITAINE';
UPDATE tabou_maitrise_ouvrage SET order_ = 2 WHERE code = 'INTERCOMMUNALE';
UPDATE tabou_maitrise_ouvrage SET order_ = 3 WHERE code = 'COMMUNALE';
UPDATE tabou_maitrise_ouvrage SET order_ = 4 WHERE code = 'PRIVE';

-- Changement de la contrainte modame_dom
ALTER TABLE urba_foncier.zac DROP CONSTRAINT IF EXISTS modame_dom;
ALTER TABLE urba_foncier.zac ADD CONSTRAINT modame_dom CHECK (modame::text = ANY (ARRAY['Régie directe'::text, 'Concession'::text, 'Autre'::text]));

ALTER TABLE tabou_programme ADD date_annulation timestamp;
ALTER TABLE tabou_operation ADD annulation_date timestamp;
ALTER TABLE tabou_evenement_operation ALTER COLUMN description TYPE varchar(500);
ALTER TABLE tabou_evenement_programme ALTER COLUMN description TYPE varchar(500);

CREATE TABLE IF NOT EXISTS tabou_projet_urbain(id_projet_urbain bigserial, title varchar, chapeau text, projet text, actualites text, savoir text, primary key (id_projet_urbain));
ALTER TABLE tabou_operation ADD fk_projet_urbain bigint;
ALTER TABLE tabou_operation ADD CONSTRAINT fk_tabou_operation_tabou_projet_urbain FOREIGN KEY (fk_projet_urbain) REFERENCES tabou_projet_urbain;