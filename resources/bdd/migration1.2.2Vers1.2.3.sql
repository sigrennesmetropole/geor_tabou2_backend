-- Ajout de la colonne id_description_concertation à la table tabou_operation
ALTER TABLE tabou_operation ADD COLUMN IF NOT EXISTS id_description_concertation BIGINT;
ALTER TABLE tabou_operation ADD CONSTRAINT fk_tabou_description_concertation FOREIGN KEY (id_description_concertation) REFERENCES tabou_description_concertation(id_description_concertation);
