-- Migration 2.5.0 -> 2.6.0 (idempotent, rejouable sans doublon)
-- US-323 : visibilité des opérations Off par commune
--  - tabou2.v_oa_emprise_commune : emprises des opérations (ZAC, en diffus, ZA) et leur commune majoritaire, calculée
--    par une seule jointure latérale, triée par surface d'intersection décroissante puis code INSEE ; une emprise qui ne
--    fait que toucher la limite d'une commune (surface d'intersection nulle) n'est pas rattachée à cette commune
--  - tabou2.v_oa_operation_commune : vue légère (id_tabou, code_insee) du filtre de visibilité des services REST ; sans
--    fonction de fenêtre ni jointure métier, PostgreSQL y pousse le filtre id_tabou jusqu'aux tables d'emprises
--  - tabou2.v_oa_operation expose code_insee (commune majoritaire) et mode_etape (PUBLIC / OFF), utilisés par le filtre
--    de visibilité de la couche cartographique
--  - géométries invalides : les calculs spatiaux portent sur des géométries réparées (st_makevalid) pour qu'une emprise
--    ou une commune invalide ne fasse pas échouer les vues ; la géométrie exposée (shape) reste celle d'origine
--  - performance : OFFSET 0 empêche le planificateur d'aplatir les sous-requêtes (géométries réparées et aire calculées
--    une seule fois par couple emprise/commune) et st_covers évite l'intersection coûteuse quand l'emprise est
--    entièrement dans une commune
--  - pas de DROP VIEW : v_oa_secteur et v_oa_programme dépendent de v_oa_operation. CREATE OR REPLACE impose de
--    conserver le nom, le type et l'ordre des colonnes existantes de v_oa_operation ; les nouvelles colonnes sont
--    ajoutées à la fin

SET search_path TO tabou2, public;

CREATE OR REPLACE VIEW tabou2.v_oa_emprise_commune
AS SELECT oa_emprise.id_emprise,
    oa_emprise.id_tabou,
    oa_emprise.nature,
    oa_emprise.nom,
    oa_emprise.vocation,
    oa_emprise.archive,
    oa_emprise.shape,
    com.nom AS commune,
    com.code_insee
   FROM ( SELECT zac.id_zac AS id_emprise,
            zac.id_tabou,
            'ZAC'::character varying AS nature,
            zac.nomzac AS nom,
                CASE zac.vocation
                    WHEN 'Activités'::text THEN 'Activités'::character varying
                    ELSE zac.vocation
                END AS vocation,
            zac.archive,
            zac.shape
           FROM urba_foncier.zac
          GROUP BY zac.id_zac, zac.id_tabou, zac.nomzac, zac.shape
        UNION ALL
         SELECT oa_1.objectid AS id_emprise,
            oa_1.id_tabou,
            'En diffus'::character varying AS nature,
            oa_1.nomopa AS nom,
                CASE oa_1.nature
                    WHEN 'Mobilité espace public'::text THEN 'Mobilité'::character varying
                    WHEN 'Espace naturel'::text THEN 'Espace vert'::character varying
                    WHEN 'Equipement'::text THEN 'Equipement public'::character varying
                    ELSE oa_1.nature
                END AS vocation,
            oa_1.archive,
            oa_1.shape
           FROM urba_foncier.oa_limite_intervention oa_1
          GROUP BY oa_1.objectid, oa_1.id_tabou, oa_1.nomopa, oa_1.shape, oa_1.nature
         HAVING oa_1.nature IS NOT NULL
        UNION ALL
         SELECT za.objectid AS id_emprise,
            za.id_tabou,
            'ZA'::character varying AS nature,
            za.nomza AS nom,
            za.territoire AS vocation,
            za.archive,
            za.shape
           FROM economie.za za
          GROUP BY za.objectid, za.id_tabou, za.nomza, za.shape, za.territoire
         HAVING za.territoire IS NOT NULL) oa_emprise
     LEFT JOIN LATERAL ( SELECT c.nom, c.code_insee
           FROM ( SELECT g.nom, g.code_insee,
                        CASE
                            WHEN st_covers(g.commune_shape, g.emprise_shape) THEN st_area(g.emprise_shape)
                            WHEN st_intersects(g.commune_shape, g.emprise_shape)
                                THEN st_area(st_intersection(g.emprise_shape, g.commune_shape))
                            ELSE 0::double precision
                        END AS aire_intersection
                   FROM ( SELECT ce.nom, ce.code_insee,
                                st_collectionextract(st_makevalid(ce.shape), 3) AS commune_shape,
                                st_collectionextract(st_makevalid(oa_emprise.shape), 3) AS emprise_shape
                           FROM limite_admin.commune_emprise ce
                          WHERE ce.commune_agglo = 1 AND ce.shape && oa_emprise.shape
                         OFFSET 0) g
                 OFFSET 0) c
          WHERE c.aire_intersection > 0::double precision
          ORDER BY c.aire_intersection DESC, c.code_insee
         LIMIT 1) com ON true;

CREATE OR REPLACE VIEW tabou2.v_oa_operation_commune
AS SELECT DISTINCT e.id_tabou,
    e.code_insee
   FROM tabou2.v_oa_emprise_commune e
  WHERE e.id_tabou IS NOT NULL AND e.code_insee IS NOT NULL;

CREATE OR REPLACE VIEW tabou2.v_oa_operation
AS SELECT row_number() OVER ()::integer AS objectid,
    oa_emprise.id_emprise,
    oa_emprise.id_tabou,
    oa.diffusion_restreinte,
        CASE
            WHEN oa_emprise.id_tabou IS NULL THEN oa_emprise.nature
            ELSE nat.libelle
        END AS nature,
    oa_emprise.commune,
    oa.code,
        CASE
            WHEN oa_emprise.id_tabou IS NULL THEN oa_emprise.nom
            ELSE oa.nom
        END AS nom,
    amen.nom AS amenageur,
        CASE
            WHEN oa_emprise.archive AND oa_emprise.id_tabou IS NULL THEN 'Clôturé'::character varying
            ELSE etaop.libelle
        END AS etape,
    st_area(st_multi(oa_emprise.shape)::geometry(MultiPolygon,3948)) / 10000::double precision AS aire_geo_ha,
    oa.operation,
    oa.description,
        CASE
            WHEN oa_emprise.id_tabou IS NULL THEN oa_emprise.vocation
            ELSE voc.libelle
        END AS vocation,
    "dec".libelle AS decision,
    moa.libelle AS maitriseouvrage,
    modame.libelle AS modeamenagement,
    conso.libelle AS consommationespace,
    oa.autorisation_date AS dateautorisation,
    oa.operationnel_date AS dateoperationnel,
    oa.cloture_date AS datecloture,
    oa.num_ads,
    oa.nb_logement_prevu AS logementsprevus,
    plh.logement_prevu AS plhlogementsprevus,
    plh.logement_livre AS plhlogementslivres,
    oa.ql1 AS territoire,
    oa.scot AS scott,
    oa.ql3,
    oa.create_user,
    oa.create_date,
    oa.modif_user,
    oa.modif_date,
    st_multi(oa_emprise.shape)::geometry(MultiPolygon,3948) AS shape,
    etaop.prospectif,
    oa_emprise.code_insee,
    etaop.mode AS mode_etape
   FROM tabou2.v_oa_emprise_commune oa_emprise
     LEFT JOIN tabou2.tabou_operation oa ON oa.id_operation = oa_emprise.id_tabou
     LEFT JOIN tabou2.tabou_nature nat ON oa.id_nature = nat.id_nature
     LEFT JOIN tabou2.tabou_plh plh ON oa.id_plh = plh.id_plh
     LEFT JOIN tabou2.tabou_amenageur amen ON oa.id_operation = amen.id_operation
     LEFT JOIN tabou2.tabou_etape_operation etaop ON oa.id_etape_operation = etaop.id_etape_operation
     LEFT JOIN tabou2.tabou_vocation voc ON oa.id_vocation = voc.id
     LEFT JOIN tabou2.tabou_decision "dec" ON oa.id_decision = "dec".id
     LEFT JOIN tabou2.tabou_maitrise_ouvrage moa ON moa.id = oa.id_maitrise_ouvrage
     LEFT JOIN tabou2.tabou_mode_amenagement modame ON modame.id = oa.id_mode_amenagement
     LEFT JOIN tabou2.tabou_consommation_espace conso ON conso.id = oa.id_consommation_espace;
