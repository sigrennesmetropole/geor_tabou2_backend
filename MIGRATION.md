# Guide de migration

Opérations à réaliser sur la base de données, GeoServer et le backend lors de la montée de version de Tabou2. Les
opérations côté front-end sont décrites dans le fichier `MIGRATION.md` du dépôt `tabou2_front`.

## 2.5.0 → 2.6.0 — Opérations Off visibles par commune (US-323)

Les utilisateurs sans accès global (ni référent ni administrateur) peuvent désormais lire les opérations Off non
prospectives de leur propre commune. La commune est déterminée à partir de l'organisation geOrchestra de l'utilisateur
(en-tête `sec-orgname`), rapprochée par le nom (sans tenir compte de la casse ni des accents) de
`limite_admin.commune_emprise.nom` avec `commune_agglo = 1`. Les utilisateurs dont l'organisation ne correspond à aucune
commune ne voient que les opérations publiques.

La couche cartographique des opérations n'est plus lue directement depuis GeoServer : le proxy du backend `/carto/ows`
transmet les requêtes WMS/WFS avec un compte technique et applique la règle de visibilité dans le filtre de chaque
requête.

### Ordre de déploiement

1. Migration de la base de données (le backend valide le schéma au démarrage et échoue si les nouvelles vues sont
   absentes).
2. GeoServer : recharger la couche, créer le compte technique, restreindre l'accès direct à la couche.
3. Configuration du backend, puis déploiement du backend 2.6.0.
4. Déploiement du front-end (voir `tabou2_front/MIGRATION.md`).

### 1. Base de données

Le script `resources/bdd/migration2.5.0Vers2.6.0.sql` est idempotent et peut être rejoué. Il crée ou remplace, dans le
schéma `tabou2` :

| Vue                      | Contenu                                                                                                  |
|--------------------------|----------------------------------------------------------------------------------------------------------|
| `v_oa_emprise_commune`   | Emprises des opérations (ZAC, diffus, ZA) avec leur commune principale (plus grande intersection)        |
| `v_oa_operation_commune` | Vue légère `(id_tabou, code_insee)` utilisée par la règle de visibilité de l'API REST                    |
| `v_oa_operation`         | Colonnes inchangées, plus `code_insee` et `mode_etape` ajoutées à la fin                                 |

Les géométries invalides (emprises ou communes) sont réparées à la volée avec `st_makevalid` pour les seuls calculs
spatiaux ; la colonne exposée `shape` est inchangée.

**Pré-vérifications**

- `v_oa_operation` est mise à jour avec `CREATE OR REPLACE VIEW`, et non supprimée, car `v_oa_secteur` et
  `v_oa_programme` en dépendent. PostgreSQL refuse le remplacement si le nom, le type ou la position d'une colonne
  existante diffère de celui attendu. Comparer les colonnes actuelles avec la liste ci-dessous (positions 1 à 34) :

  ```sql
  SELECT attnum, attname, format_type(atttypid, atttypmod)
  FROM pg_attribute
  WHERE attrelid = 'tabou2.v_oa_operation'::regclass AND attnum > 0
  ORDER BY attnum;
  ```

  Attendu (tel que retourné par `format_type` ; les longueurs des colonnes recopiées telles quelles proviennent des
  tables sources et peuvent différer, les colonnes calculées `nature`, `commune`, `nom`, `etape`, `vocation` et `shape`
  doivent correspondre exactement) :

  ```text
  objectid integer, id_emprise integer, id_tabou integer, diffusion_restreinte boolean, nature character varying,
  commune character varying(50), code character varying(255), nom character varying, amenageur character varying(255),
  etape character varying, aire_geo_ha double precision, operation character varying(255),
  description character varying(255), vocation character varying, decision character varying(255),
  maitriseouvrage character varying(255), modeamenagement character varying(255),
  consommationespace character varying(255), dateautorisation timestamp without time zone,
  dateoperationnel timestamp without time zone, datecloture timestamp without time zone,
  num_ads character varying(255), logementsprevus integer, plhlogementsprevus integer, plhlogementslivres integer,
  territoire character varying(255), scott boolean, ql3 character varying(255), create_user character varying(255),
  create_date timestamp without time zone, modif_user character varying(255),
  modif_date timestamp without time zone, shape geometry(MultiPolygon,3948), prospectif boolean
  ```

- Essai à blanc sur la base cible, dans une transaction annulée :

  ```sql
  BEGIN;
  \i resources/bdd/migration2.5.0Vers2.6.0.sql
  ROLLBACK;
  ```

- Sauvegarder la définition actuelle, nécessaire pour un retour arrière :
  `SELECT pg_get_viewdef('tabou2.v_oa_operation'::regclass, true);`

**Exécution**

Exécuter le script avec le propriétaire des vues du schéma `tabou2` :

```shell
psql -v ON_ERROR_STOP=1 -h <hôte> -U <propriétaire> -d <base> -f resources/bdd/migration2.5.0Vers2.6.0.sql
```

Si le backend ou GeoServer se connecte avec un autre rôle que le propriétaire, lui accorder l'accès en lecture :

```sql
GRANT SELECT ON tabou2.v_oa_emprise_commune, tabou2.v_oa_operation_commune TO <rôle>;
```

**Contrôles**

```sql
-- emprises liées à une opération mais situées hors des communes de l'agglomération
SELECT nature, id_emprise, id_tabou FROM tabou2.v_oa_emprise_commune WHERE id_tabou IS NOT NULL AND code_insee IS NULL;
-- géométries invalides (n'est plus bloquant, à corriger dans les données sources)
SELECT 'zac', id_zac FROM urba_foncier.zac WHERE NOT st_isvalid(shape)
UNION ALL SELECT 'oa', objectid FROM urba_foncier.oa_limite_intervention WHERE NOT st_isvalid(shape)
UNION ALL SELECT 'za', objectid FROM economie.za WHERE NOT st_isvalid(shape)
UNION ALL SELECT 'commune', objectid FROM limite_admin.commune_emprise WHERE NOT st_isvalid(shape);
```

### 2. GeoServer

- **Recharger la couche** `tabou:v_oa_operation` (*Couches* > `v_oa_operation` > *Recharger le type d'entité*, ou
  `POST /geoserver/rest/reset`). GeoServer met en cache les attributs d'une couche : tant qu'elle n'est pas rechargée,
  `code_insee` et `mode_etape` sont inconnus et toutes les requêtes filtrées par le proxy échouent pour les
  utilisateurs sans accès global.

### 3. Georchestra
- **Compte technique** : créer un utilisateur Georchestra (par exemple `tabou2`) avec un rôle autorisé à lire
  `tabou:v_oa_operation`. Le proxy du backend s'authentifie avec ce compte (HTTP Basic).

### 4. Backend

Ajouter les propriétés du proxy dans `tabou2.properties` (à ajouter également aux configurations de déploiement, voir
`tabou2-facade/src/main/resources/tabou2/exemple.properties`) :

```properties
## Proxy cartographique /carto/ows (US-323)
tabou2.geoserver.url=http://<hôte-geoserver>:8080/geoserver/ows
tabou2.geoserver.username=<compte technique>
tabou2.geoserver.password=<mot de passe du compte technique>
# Couches servies par le proxy, séparées par des virgules ; chacune doit exposer mode_etape, prospectif et code_insee
tabou2.geoserver.authorized-layers=tabou:v_oa_operation
# Paramètres de connexion facultatifs (valeurs par défaut indiquées ; délais en millisecondes)
tabou2.geoserver.pool.max-connections=50
tabou2.geoserver.pool.acquire-timeout-ms=5000
tabou2.geoserver.connect-timeout-ms=5000
tabou2.geoserver.read-timeout-ms=60000
```

- `tabou2.geoserver.authorized-layers` doit contenir le nom de la couche utilisé par le front-end
  (`layersCfg.layerOA.nom`) ; toute autre couche est refusée par le proxy.
- Le proxy est servi à l'adresse `<backend>/carto/ows`, soit `/tabou2/carto/ows` derrière le security-proxy
  geOrchestra : aucune nouvelle route n'est nécessaire. Le security-proxy doit transmettre l'en-tête `sec-orgname`
  (comportement par défaut de geOrchestra).

### Retour arrière

Le backend 2.5.0 fonctionne avec les nouvelles vues (les colonnes et vues supplémentaires sont ignorées) ; la base de
données peut donc être laissée en l'état. Supprimer les nouvelles colonnes de `v_oa_operation` nécessite de supprimer
puis recréer `v_oa_secteur` et `v_oa_programme` avec les définitions sauvegardées avant la migration.
