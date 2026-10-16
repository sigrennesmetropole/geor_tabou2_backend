-- =============================================================================
-- Update post-migration 2.4.0 -> 2.5.0 (OPTIONNEL) :
-- initialise la vocation principale des programmes qui n'en ont pas en recopiant
-- celle de leur opération ou secteur parent. Idempotent : seuls les programmes
-- sans vocation sont mis à jour.
-- Pré-requis : migration 2.4.0 -> 2.5.0 déjà jouée.
-- =============================================================================

SET search_path TO tabou2, public;

UPDATE tabou_programme p
SET id_vocation = o.id_vocation
FROM tabou_operation o
WHERE p.id_operation = o.id_operation
  AND p.id_vocation IS NULL
  AND o.id_vocation IS NOT NULL;
