-- =============================================================================
-- Migration 2.1.0 vers 2.2.0
-- Ajout du champ "Plus d'informations" sur l'onglet Projet Urbain
-- Ajout de l'onglet Projet Urbain sur les Programmes
-- =============================================================================

-- Ajout du champ "Plus d'informations" à la table tabou_projet_urbain
ALTER TABLE tabou_projet_urbain ADD COLUMN IF NOT EXISTS plus_informations text;

-- Ajout de la FK vers tabou_projet_urbain dans tabou_programme
ALTER TABLE tabou_programme ADD COLUMN IF NOT EXISTS fk_projet_urbain bigint;
ALTER TABLE tabou_programme ADD CONSTRAINT fk_tabou_programme_tabou_projet_urbain
    FOREIGN KEY (fk_projet_urbain) REFERENCES tabou_projet_urbain(id_projet_urbain);
