-- On ajoute les nouvelles tables

CREATE TABLE IF NOT EXISTS tabou_type_plh(id_type_plh bigserial, libelle varchar, date_debut timestamp, type_attribut varchar(8), id_type_plh_parent bigint, primary key (id_type_plh));
CREATE TABLE IF NOT EXISTS tabou_attribut_plh(id_attribut_plh bigserial, value_ text, id_type_plh bigint, id_programme bigint, primary key (id_attribut_plh));
CREATE TABLE IF NOT EXISTS tabou_programme_type_plh(id_programme bigint, id_type_plh bigint, primary key (id_programme, id_type_plh));

-- On crée les foreign keys

ALTER TABLE tabou_type_plh ADD CONSTRAINT fk_tabou_type_plh_parent FOREIGN KEY (id_type_plh_parent) REFERENCES tabou_type_plh;
ALTER TABLE tabou_attribut_plh ADD CONSTRAINT fk_tabou_attribut_plh_type FOREIGN KEY (id_type_plh) REFERENCES tabou_type_plh;
ALTER TABLE tabou_attribut_plh ADD CONSTRAINT fk_tabou_attribut_plh_programme FOREIGN KEY (id_programme) REFERENCES tabou_programme;
ALTER TABLE tabou_programme_type_plh ADD CONSTRAINT fk_tabou_programme_type_plh_plh_type FOREIGN KEY (id_type_plh) REFERENCES tabou_type_plh;
ALTER TABLE tabou_programme_type_plh ADD CONSTRAINT fk_tabou_programme_type_plh_programme FOREIGN KEY (id_programme) REFERENCES tabou_programme;

ALTER TABLE tabou_programme ADD COLUMN IF NOT EXISTS surface_totale double precision;