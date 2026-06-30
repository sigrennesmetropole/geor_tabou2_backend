-- =============================================================================
-- Update post-migration 2.2.0 -> 2.3.0 : init des logements spécifiques.
-- Opérations/secteurs : un seul groupe global (sans accession) pour éviter les
-- doublons dans la popup Vocation qui aplatit les groupes.
-- Programmes : un groupe par type d'accession (portée PROGRAMME) via la
-- programmation habitat. Idempotent, sans doublon.
-- Pré-requis : migration 2.2.0 -> 2.3.0 déjà jouée.
-- =============================================================================

SET search_path TO tabou2, public;

DO
$$
    DECLARE
        op    RECORD;
        prg   RECORD;
        acc   RECORD;
        tl    RECORD;
        dup   RECORD;
        ls_id bigint;
        ph_id bigint;
    BEGIN
        -- Opérations et secteurs : un seul groupe global (sans type d'accession).
        -- La popup Vocation aplatit les groupes : plusieurs accessions => doublons.
        FOR op IN SELECT id_operation FROM tabou_operation
            LOOP
                -- Groupe à conserver = le plus ancien déjà rattaché à l'opération
                ls_id := NULL;
                SELECT ls.id_logements_specifiques
                INTO ls_id
                FROM tabou_operation_logements_sp ols
                         JOIN tabou_logements_specifiques ls
                              ON ls.id_logements_specifiques = ols.id_logements_specifiques
                WHERE ols.id_operation = op.id_operation
                ORDER BY ls.id_logements_specifiques
                LIMIT 1;

                -- Création du groupe + rattachement si aucun
                IF ls_id IS NULL THEN
                    INSERT INTO tabou_logements_specifiques (id_type_accession_logement, valeur)
                    VALUES (NULL, NULL)
                    RETURNING id_logements_specifiques INTO ls_id;

                    INSERT INTO tabou_operation_logements_sp (id_operation, id_logements_specifiques)
                    VALUES (op.id_operation, ls_id)
                    ON CONFLICT (id_operation, id_logements_specifiques) DO NOTHING;
                END IF;

                -- Suppression des groupes en double de l'opération (tout sauf ls_id)
                FOR dup IN
                    SELECT ols.id_logements_specifiques AS id
                    FROM tabou_operation_logements_sp ols
                    WHERE ols.id_operation = op.id_operation
                      AND ols.id_logements_specifiques <> ls_id
                    LOOP
                        DELETE FROM tabou_operation_logements_sp
                        WHERE id_operation = op.id_operation
                          AND id_logements_specifiques = dup.id;
                        DELETE FROM tabou_logement_specifique WHERE id_logements_specifiques = dup.id;
                        DELETE FROM tabou_logements_specifiques WHERE id_logements_specifiques = dup.id;
                    END LOOP;

                -- Groupe global : pas de type d'accession
                UPDATE tabou_logements_specifiques
                SET id_type_accession_logement = NULL
                WHERE id_logements_specifiques = ls_id;

                -- 1 détail par type de logement actif (si absent)
                FOR tl IN
                    SELECT tlg.id_type_logement AS id
                    FROM tabou_type_logement tlg
                    WHERE tlg.date_debut <= now()
                      AND (tlg.date_fin IS NULL OR tlg.date_fin > now())
                    LOOP
                        INSERT INTO tabou_logement_specifique
                            (id_logements_specifiques, id_type_logement, valeur_prevue, valeur_realisee)
                        SELECT ls_id, tl.id, NULL, NULL
                        WHERE NOT EXISTS (SELECT 1
                                          FROM tabou_logement_specifique d
                                          WHERE d.id_logements_specifiques = ls_id
                                            AND d.id_type_logement = tl.id);
                    END LOOP;
            END LOOP;

        -- Programmes (portée PROGRAMME via la programmation habitat)
        FOR prg IN SELECT id_programme, id_programmation_habitat FROM tabou_programme
            LOOP
                -- Crée la programmation habitat si absente
                ph_id := prg.id_programmation_habitat;
                IF ph_id IS NULL THEN
                    INSERT INTO tabou_programmation_habitat (nb_logements, nb_logements_hfv, surface_shab)
                    VALUES (NULL, NULL, NULL)
                    RETURNING id_programmation_habitat INTO ph_id;
                    UPDATE tabou_programme SET id_programmation_habitat = ph_id WHERE id_programme = prg.id_programme;
                END IF;

                FOR acc IN
                    SELECT t.id_type_accession_logement AS id
                    FROM tabou_type_accession_logement t
                             JOIN tabou_type_accession_logement_portee p
                                  ON p.id_type_accession_logement = t.id_type_accession_logement
                                      AND p.portee = 'PROGRAMME'
                    WHERE t.date_debut <= now()
                      AND (t.date_fin IS NULL OR t.date_fin > now())
                    LOOP
                        -- Groupe déjà rattaché à la programmation habitat pour ce type d'accession ?
                        ls_id := NULL;
                        SELECT ls.id_logements_specifiques
                        INTO ls_id
                        FROM tabou_prog_habitat_logements_sp phls
                                 JOIN tabou_logements_specifiques ls
                                      ON ls.id_logements_specifiques = phls.id_logements_specifiques
                        WHERE phls.id_programmation_habitat = ph_id
                          AND ls.id_type_accession_logement = acc.id
                        LIMIT 1;

                        -- Création du groupe + rattachement si absent
                        IF ls_id IS NULL THEN
                            INSERT INTO tabou_logements_specifiques (id_type_accession_logement, valeur)
                            VALUES (acc.id, NULL)
                            RETURNING id_logements_specifiques INTO ls_id;

                            INSERT INTO tabou_prog_habitat_logements_sp (id_programmation_habitat, id_logements_specifiques)
                            VALUES (ph_id, ls_id)
                            ON CONFLICT (id_programmation_habitat, id_logements_specifiques) DO NOTHING;
                        END IF;

                        -- 1 détail par type de logement actif (si absent)
                        FOR tl IN
                            SELECT tlg.id_type_logement AS id
                            FROM tabou_type_logement tlg
                            WHERE tlg.date_debut <= now()
                              AND (tlg.date_fin IS NULL OR tlg.date_fin > now())
                            LOOP
                                INSERT INTO tabou_logement_specifique
                                    (id_logements_specifiques, id_type_logement, valeur_prevue, valeur_realisee)
                                SELECT ls_id, tl.id, NULL, NULL
                                WHERE NOT EXISTS (SELECT 1
                                                  FROM tabou_logement_specifique d
                                                  WHERE d.id_logements_specifiques = ls_id
                                                    AND d.id_type_logement = tl.id);
                            END LOOP;
                    END LOOP;
            END LOOP;
    END
$$;
