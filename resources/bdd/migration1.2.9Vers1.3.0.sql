-- Permet de mettre à jour nos bases locales, en prod la table a été créée par le SIG
ALTER TABLE tabou_pc_ddc ADD COLUMN IF NOT EXISTS version_ads VARCHAR(3);