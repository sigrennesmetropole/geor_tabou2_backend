-- Création du user sig_user
CREATE USER sig_user WITH
    LOGIN
    NOSUPERUSER
    INHERIT
    NOCREATEDB
    NOCREATEROLE
    NOREPLICATION
    ENCRYPTED PASSWORD 'sig_user';

-- Création du user ddc_user
CREATE USER ddc_user WITH
    LOGIN
    NOSUPERUSER
    INHERIT
    NOCREATEDB
    NOCREATEROLE
    NOREPLICATION
    ENCRYPTED PASSWORD 'ddc_user';

ALTER USER sig_user SET search_path TO limite_admin, urba_foncier, economie, demographie, public;
ALTER USER ddc_user SET search_path TO ddc, public;

-- Création des schémas
CREATE SCHEMA if not exists ddc AUTHORIZATION postgres;
CREATE SCHEMA if not exists limite_admin AUTHORIZATION postgres;
CREATE SCHEMA if not exists urba_foncier AUTHORIZATION postgres;
CREATE SCHEMA if not exists economie AUTHORIZATION postgres;
CREATE SCHEMA if not exists demographie AUTHORIZATION postgres;

-- Ajout des extensions dans le schéma
CREATE EXTENSION postgis;
CREATE EXTENSION postgis_topology;
CREATE EXTENSION fuzzystrmatch;
CREATE EXTENSION postgis_tiger_geocoder;


-- Création de la table des permis de construire : nécessaire seulement pour environnement de dev et intégration continue

create table if not exists ddc.pc_ddc (
    id bigserial,
    num_ads varchar(255),
    date_depot_dossier timestamp,
    date_completude_dossier timestamp,
    ads_date timestamp,
    doc_date timestamp,
    dat_date timestamp,
    surf_autre float,
    surf_bureaux float,
    surf_commerces float,
    surf_equip_pub float,
    surf_industries float,
    primary key (id)
);

-- Scripts temporaire. A revoir pour intégrer les scripts fournis par denis
CREATE TABLE limite_admin.commune_emprise
(
    objectid integer NOT NULL,
    code_insee numeric(15,0) NOT NULL,
    nom character varying(50),
    commune_agglo smallint,
    x_centrbrg numeric(38,8),
    y_centrbrg numeric(38,8),
    st_area_geom_poly_ numeric(38,8) NOT NULL,
    st_length_geom_poly_ numeric(38,8) NOT NULL,
    shape geometry,
    CONSTRAINT enforce_geotype_shape CHECK (geometrytype(shape) = 'POLYGON'::text OR shape IS NULL),
    CONSTRAINT enforce_srid_shape CHECK (st_srid(shape) = 3948)
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE if not exists demographie.iris
(
    objectid integer NOT NULL,
    nom_groupe character varying(41),
    ccom character varying(6),
    code_iris character varying(10),
    axe_eo integer,
    val integer,
    nusectmorp character varying(5),
    nmsectmorp character varying(60),
    nmiris character varying(50),
    niris character varying(50),
    st_area_shape_ numeric(38,8) NOT NULL,
    st_length_shape_ numeric(38,8) NOT NULL,
    st_area_shape1 numeric(38,8) NOT NULL,
    st_length_shape1 numeric(38,8) NOT NULL,
    shape geometry,
    CONSTRAINT enforce_geotype_shape CHECK (geometrytype(shape) = 'POLYGON'::text),
    CONSTRAINT enforce_srid_shape CHECK (st_srid(shape) = 3948)
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE if not exists urba_foncier.plui_zone_urba
(
    objectid integer,
    anc_objectid integer,
    id_rm integer,
    id_docurba character varying(17),
    code_insee character varying(5),
    semio character varying(255),
    commentaire character varying(255),
    libelle character varying(254),
    libelong character varying(254),
    typezone character varying(3),
    destdomi character varying(2),
    nomfic character varying(80),
    urlfic character varying(254),
    date_proc_creat character varying(8),
    date_proc_modif character varying(8),
    date_creat timestamp without time zone,
    date_modif timestamp without time zone,
    shape geometry(MultiPolygon,3948)
)
    WITH (
        OIDS=FALSE
    );

CREATE TABLE urba_foncier.zac
(
    id_zac serial NOT NULL,
    code_insee character varying(5),
    nomzac character varying(50),
    decis character varying(59),
    datedecis character varying(8),
    datedecism character varying(8),
    datereal character varying(8),
    daterealm character varying(8),
    dateclot character varying(8),
    vocation character varying(9),
    maouvr character varying(16),
    modame character varying(13),
    etape character varying(24),
    urbcrea character varying(50),
    amecrea character varying(50),
    urbreal character varying(50),
    amereal character varying(50),
    nature character varying(21),
    obs character varying(254),
    datesig character varying(15),
    taxes character varying(13),
    shape geometry,
    date_modif timestamp without time zone,
    archive boolean DEFAULT false,
    aire_geo numeric(15,2),
    perimetre_geo numeric(15,2),
    id_tabou integer,
    CONSTRAINT pk_zac_id_zac PRIMARY KEY (id_zac),
    CONSTRAINT decis_dom CHECK (decis::text = ANY (ARRAY['arrêté préfectoral'::text, 'délibération du Comité Syndical intercommunal'::text, 'délibération du Conseil d''agglomération de Rennes Métropole'::text, 'délibération du Conseil de Rennes Métropole'::text, 'délibération du Conseil Municipal'::text])),
    CONSTRAINT enforce_geotype_shape CHECK (geometrytype(shape) = 'MULTIPOLYGON'::text OR geometrytype(shape) = 'POLYGON'::text),
    CONSTRAINT enforce_srid_shape CHECK (st_srid(shape) = 3948),
    CONSTRAINT etape_dom CHECK (etape::text = ANY (ARRAY['Clôturé'::text, 'En cours d''aménagement'::text, 'En études de réalisation'::text, 'Réalisé'::text, 'Supprimé'::text])),
    CONSTRAINT maouvr_dom CHECK (maouvr::text = ANY (ARRAY['Communale'::text, 'Communautaire'::text, 'Intercommunale'::text, 'Métropolitaine'::text])),
    CONSTRAINT modame_dom CHECK (modame::text = ANY (ARRAY['Concession'::text, 'CPA'::text, 'Régie directe'::text])),
    CONSTRAINT nature_dom CHECK (nature::text = ANY (ARRAY['Extension urbaine'::text, 'Renouvellement urbain'::text])),
    CONSTRAINT taxes_dom CHECK (taxes::text = ANY (ARRAY['PARTICIPATION'::text, 'TA'::text])),
    CONSTRAINT vocation_dom CHECK (vocation::text = ANY (ARRAY['Activités'::text, 'Habitat'::text, 'Mixte'::text]))
)
    WITH (
        OIDS=FALSE
    );

CREATE TABLE urba_foncier.oa_secteur
(
    objectid integer NOT NULL,
    secteur character varying(200),
    shape geometry,
    id_tabou integer,
    CONSTRAINT enforce_dims_shape CHECK (st_ndims(shape) = 2),
    CONSTRAINT enforce_geotype_shape CHECK (geometrytype(shape) = 'MULTIPOLYGON'::text OR geometrytype(shape) = 'POLYGON'::text),
    CONSTRAINT enforce_srid_shape CHECK (st_srid(shape) = 3948)
)
    WITH (
        OIDS=FALSE
    );

CREATE TABLE economie.za
(
    objectid integer NOT NULL,
    etape character varying(50) NOT NULL,
    etat character varying(50),
    scot character varying(50),
    territoire character varying(50),
    nomza character varying(50),
    idza character varying(8),
    code_insee character varying(6) NOT NULL,
    saisie character varying(50),
    observation character varying(254),
    shape geometry,
    archive boolean DEFAULT false, -- False = non / True = oui
    id_tabou integer,
    date_modif timestamp without time zone DEFAULT now(),
    datecrea timestamp without time zone,
    datefin timestamp without time zone,
    CONSTRAINT za_pkey PRIMARY KEY (objectid),
    CONSTRAINT enforce_dims_shape CHECK (st_ndims(shape) = 2),
    CONSTRAINT enforce_geotype_shape CHECK (geometrytype(shape) = 'MULTIPOLYGON'::text OR geometrytype(shape) = 'POLYGON'::text),
    CONSTRAINT enforce_srid_shape CHECK (st_srid(shape) = 3948),
    CONSTRAINT etape_dom CHECK (etape::text = ANY (ARRAY['créée'::text, 'en cours'::text, 'potentielle'::text, 'terminée'::text])),
    CONSTRAINT etat_dom CHECK (etat::text = ANY (ARRAY['extension'::text, 'renouvellement'::text])),
    CONSTRAINT scot_dom CHECK (scot::text = ANY (ARRAY['compatible avec le SCoT'::text, 'non compatible avec le SCoT'::text])),
    CONSTRAINT territoire_dom CHECK (territoire::text = ANY (ARRAY['Pôle tertiaire'::text, 'Proximité'::text, 'Structurante'::text]))
)
    WITH (
        OIDS=FALSE
    );

CREATE TABLE if not exists limite_admin.quartier
(
    objectid integer NOT NULL,
    matricule character varying(15),
    nuquart smallint,
    nmquart character varying(60),
    numnom character varying(60),
    nom character varying(50),
    st_area_shape_ numeric(38,8) NOT NULL,
    st_length_shape_ numeric(38,8) NOT NULL,
    shape geometry,
    code_insee integer,
    CONSTRAINT enforce_geotype_shape CHECK (geometrytype(shape) = 'POLYGON'::text),
    CONSTRAINT enforce_srid_shape CHECK (st_srid(shape) = 3948)
) WITH (
    OIDS=FALSE
);

CREATE TABLE urba_foncier.instructeur_secteur
(
    id integer NOT NULL,
    nom_com character varying(250),
    code_insee integer,
    nuquart integer,
    instructeur character varying(250),
    assistant character varying(250),
    secteur character varying(50),
    CONSTRAINT pk_instructeur_secteur_id PRIMARY KEY (id)
) WITH (
     OIDS=FALSE
    );

CREATE TABLE urba_foncier.chargedoperation_secteur
(
    id integer NOT NULL,
    geom geometry(MultiPolygon,3948),
    code_insee double precision,
    nom_secteur character varying(100),
    referent character varying(100),
    CONSTRAINT chargedoperation_secteur_pkey PRIMARY KEY (id)
) WITH (
      OIDS=FALSE
    );


CREATE TABLE limite_admin.comite_sect_tab
(
    num_secteur integer NOT NULL,
    nom_secteur character varying(50),
    CONSTRAINT pk_comite_sect PRIMARY KEY (num_secteur)
) WITH (
      OIDS=FALSE
    );


CREATE TABLE urba_foncier.negociateurfoncier_secteur
(
    objectid bigint NOT NULL,
    geom geometry(MultiPolygon,3948),
    code_insee double precision,
    nom character varying(50),
    negociateur character varying(100),
    CONSTRAINT negociateurfoncier_secteur_pkey PRIMARY KEY (objectid)
)  WITH (
       OIDS=FALSE
    );

ALTER TABLE ddc.pc_ddc  OWNER TO ddc_user;
ALTER TABLE limite_admin.quartier  OWNER TO sig_user;
ALTER TABLE limite_admin.comite_sect_tab  OWNER TO sig_user;
ALTER TABLE urba_foncier.plui_zone_urba  OWNER TO sig_user;
ALTER TABLE urba_foncier.zac  OWNER TO sig_user;
ALTER TABLE urba_foncier.oa_secteur  OWNER TO sig_user;
ALTER TABLE urba_foncier.instructeur_secteur  OWNER TO sig_user;
ALTER TABLE urba_foncier.chargedoperation_secteur  OWNER TO sig_user;
ALTER TABLE urba_foncier.negociateurfoncier_secteur  OWNER TO sig_user;
ALTER TABLE economie.za  OWNER TO sig_user;
ALTER TABLE demographie.iris  OWNER TO sig_user;
ALTER TABLE limite_admin.commune_emprise  OWNER TO sig_user;

-- Ajout des droits
GRANT ALL ON SCHEMA ddc TO ddc_user;
GRANT ALL ON SCHEMA limite_admin TO sig_user;
GRANT ALL ON SCHEMA urba_foncier TO sig_user;
GRANT ALL ON SCHEMA economie TO sig_user;
GRANT ALL ON SCHEMA demographie TO sig_user;
