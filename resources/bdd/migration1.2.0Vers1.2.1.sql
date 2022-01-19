-- Création tables annexes

create table if not exists tabou_plh(
    id_plh bigserial,
    logement_prevu integer,
    logement_livre integer,
    date timestamp,
    description text,
    primary key (id_plh)
);

create table if not exists tabou_entite_referente(
    id_entite_referente bigserial,
    code varchar(20) unique not null,
    libelle varchar(50) not null,
    date_inactif timestamp,
    create_date timestamp,
    create_user varchar(20),
    primary key(id_entite_referente)
);

create table if not exists tabou_vocation_za(
    id_vocation_za bigserial,
    code varchar(20) unique not null,
    libelle varchar(50) not null,
    date_inactif timestamp,
    create_date timestamp,
    create_user varchar(20),
    primary key(id_vocation_za)
);

create table if not exists tabou_type_occupation(
    id_type_occupation bigserial,
    code varchar(20) unique not null,
    libelle varchar(50) not null,
    date_inactif timestamp,
    create_date timestamp,
    create_user varchar(20),
    primary key(id_type_occupation)
);

create table if not exists tabou_type_programmation(
    id_type_programmation bigserial,
    code varchar(20) unique not null,
    libelle varchar(50) not null,
    create_date timestamp,
    create_user varchar(50),
    date_inactif timestamp,
    primary key (id_type_programmation)
);

create table if not exists tabou_information_programmation(
    id_information_programmation bigserial,
    id_operation bigserial,
    id_type_programmation bigserial,
    description text,
    primary key (id_information_programmation)
);

create table if not exists tabou_type_contribution(
    id_type_contribution bigserial,
    code varchar(20) unique not null,
    libelle varchar(50) not null,
    create_date timestamp,
    create_user varchar(50),
    date_inactif timestamp,
    primary key (id_type_contribution)
);

create table if not exists tabou_contribution(
    id_contribution bigserial,
    id_operation bigserial,
    id_type_contribution bigserial,
    description text,
    primary key (id_contribution)
);

create table if not exists tabou_type_foncier(
    id_type_foncier bigserial,
    code varchar(20) unique not null,
    libelle varchar(50) not null,
    create_date timestamp,
    create_user varchar(50),
    date_inactif timestamp,
    primary key (id_type_foncier)
);

create table if not exists tabou_description_foncier(
    id_description_foncier bigserial,
    id_operation bigserial,
    id_type_foncier bigserial,
    description text,
    taux double precision,
    primary key (id_description_foncier)
);

create table if not exists tabou_type_amenageur(
    id_type_amenageur bigserial,
    code varchar(20) unique not null,
    libelle varchar(50) not null,
    create_date timestamp,
    create_user varchar(50),
    date_inactif timestamp,
    primary key(id_type_amenageur)
    );

create table if not exists tabou_amenageur(
    id_amenageur bigserial,
    id_operation bigserial,
    id_type_amenageur bigserial,
    nom varchar(255),
    primary key (id_amenageur)
);

create table if not exists tabou_description_concertation(
    id_description_concertation bigserial,
    date_debut timestamp,
    date_fin timestamp,
    primary key(id_description_concertation)
);

create table if not exists tabou_type_financement_operation(
    id_type_financement_operation bigserial,
    code varchar(20) unique not null,
    libelle varchar(50) not null,
    create_date timestamp,
    create_user varchar(50),
    date_inactif timestamp,
    primary key(id_type_financement_operation)
);

create table if not exists tabou_description_financement_operation(
    id_description_financement_operation bigserial,
    id_operation bigserial,
    id_type_financement_operation bigserial,
    description text,
    primary key(id_description_financement_operation)
);

create table if not exists tabou_type_action_operation(
    id_type_action_operation bigserial,
    code varchar(20) unique not null,
    libelle varchar(50) not null,
    create_date timestamp,
    create_user varchar(50),
    date_inactif timestamp,
    primary key(id_type_action_operation)
);

create table if not exists tabou_action_operation(
    id_action_operation bigserial,
    id_operation bigserial,
    id_type_action_operation bigserial,
    description text,
    primary key (id_action_operation)
);

create table if not exists tabou_outil_foncier(
    id_outil_foncier bigserial,
    code varchar(20) unique not null,
    libelle varchar(50) not null,
    date_inactif timestamp,
    create_date timestamp,
    create_user varchar(20),
    primary key(id_outil_foncier)
);

create table if not exists tabou_type_acteur(
    id_type_acteur bigserial,
    code varchar(20) unique not null,
    libelle varchar(50) not null,
    create_date timestamp,
    create_user varchar(50),
    date_inactif timestamp,
    primary key(id_type_acteur)
);

create table if not exists tabou_acteur(
    id_acteur bigserial,
    id_operation bigserial,
    id_type_acteur bigserial,
    description text,
    primary key (id_acteur)
);

create table if not exists tabou_fonction_contact (
    id_fonction_contact bigserial,
    code varchar(255) NOT NULL UNIQUE,
    libelle varchar(255) NOT NULL,
    PRIMARY KEY (id_fonction_contact)
);


create table if not exists tabou_contact_tiers
(
    id_contact_tiers    bigserial,
    id_tiers            bigserial NOT NULL,
    id_fonction_contact bigserial NOT NULL,
    nom                 varchar(50) NOT NULL,
    prenom              varchar(50),
    service             varchar(50),
    adresse             varchar(100),
    adresse_cp          varchar(20),
    adresse_ville       varchar(50),
    telecopie           varchar(50),
    telephone           varchar(50),
    email               varchar(100),
    date_inactif        timestamp,
    create_date         timestamp,
    create_user         varchar(50),
    modif_date          timestamp,
    modif_user          varchar(50),
    primary key (id_contact_tiers),
    CONSTRAINT fk_tabou_contact_tiers_tabou_tiers FOREIGN KEY (id_tiers) REFERENCES tabou_tiers(id_tiers),
    CONSTRAINT fk_tabou_contact_tiers_tabou_fonction_contact FOREIGN KEY (id_fonction_contact) REFERENCES tabou_fonction_contact(id_fonction_contact)
);

-- Modification table operation
DO $$
BEGIN
if exists (select *
    from information_schema.columns
    where table_name = 'tabou_operation' and column_name = 'ql2')
then
    alter table tabou_operation
    rename ql2 to scot;
end if;
END $$;

alter table tabou_operation
add if not exists densite_scot double precision,
add if not exists id_plh bigserial,
add if not exists id_entite_referente bigserial,
add if not exists objectifs text,
add if not exists id_vocation_za bigserial,
add if not exists paf_taux double precision,
add if not exists id_type_occupation bigserial,
add if not exists id_outil_foncier bigserial,
add if not exists densite_oap double precision,
add if not exists plui_disposition text,
add if not exists plui_adaptation text,
add if not exists outil_amenagement varchar(255),
add if not exists concertation_existe boolean,
add if not exists concertation_date_debut timestamp,
add if not exists concertation_date_fin timestamp,
add if not exists etude text,
add if not exists localisation text,
add if not exists usage_actuel varchar(255),
add if not exists avancement_administratif text,
add if not exists environnement text,
add if not exists surface_realisee double precision;

-- Modification table tiers
DO $$
BEGIN
if not exists (select *
    from information_schema.columns
    where table_name = 'tabou_tiers' and column_name = 'adresse')
then
    alter table tabou_tiers add adresse varchar(255);
    update tabou_tiers set adresse = adresse_num where adresse_num is not null and adresse_rue is null;
    update tabou_tiers set adresse = adresse_rue where adresse_rue is not null and adresse_num is null;
    update tabou_tiers set adresse = concat(adresse_num, ' ', adresse_rue) where adresse_num is not null and adresse_rue is not null;
    alter table tabou_tiers drop adresse_rue, drop adresse_num;
end if;
END $$;

-- Ajout foreign keys
DO $$
BEGIN
alter table if exists tabou_operation
    drop constraint if exists fk_tabou_operation_tabou_plh;
alter table if exists tabou_operation
    add constraint fk_tabou_operation_tabou_plh
    foreign key (id_plh)
    references tabou_plh;

alter table if exists tabou_operation
    drop constraint if exists fk_tabou_operation_tabou_entite_referente;
alter table if exists tabou_operation
    add constraint fk_tabou_operation_tabou_entite_referente
    foreign key (id_entite_referente)
    references tabou_entite_referente;

alter table if exists tabou_operation
    drop constraint if exists fk_tabou_operation_tabou_vocation_za;
alter table if exists tabou_operation
    add constraint fk_tabou_operation_tabou_vocation_za
    foreign key (id_vocation_za)
    references tabou_vocation_za;

alter table if exists tabou_operation
    drop constraint if exists fk_tabou_operation_tabou_type_occupation;
alter table if exists tabou_operation
    add constraint fk_tabou_operation_tabou_type_occupation
    foreign key (id_type_occupation)
    references tabou_type_occupation;

alter table if exists tabou_operation
    drop constraint if exists fk_tabou_operation_tabou_outil_foncier;
alter table if exists tabou_operation
    add constraint fk_tabou_operation_tabou_outil_foncier
    foreign key (id_outil_foncier)
    references tabou_outil_foncier;

alter table if exists tabou_information_programmation
    drop constraint if exists fk_information_programmation_type_programmation;
alter table if exists tabou_information_programmation
    add constraint fk_information_programmation_type_programmation
    foreign key (id_type_programmation) references tabou_type_programmation;

alter table if exists tabou_information_programmation
    drop constraint if exists fk_information_programmation_tabou_operation;
alter table if exists tabou_information_programmation
    add constraint fk_information_programmation_tabou_operation
    foreign key (id_operation) references tabou_operation;

alter table if exists tabou_contribution
    drop constraint if exists fk_contribution_type_contribution;
alter table if exists tabou_contribution
    add constraint fk_contribution_type_contribution
    foreign key (id_type_contribution) references tabou_type_contribution;

alter table if exists tabou_contribution
    drop constraint if exists fk_contribution_tabou_operation;
alter table if exists tabou_contribution
    add constraint fk_contribution_tabou_operation
    foreign key (id_operation) references tabou_operation;

alter table if exists tabou_description_foncier
    drop constraint if exists fk_description_foncier_type_foncier;
alter table if exists tabou_description_foncier
    add constraint fk_description_foncier_type_foncier
    foreign key (id_type_foncier) references tabou_type_foncier;

alter table if exists tabou_description_foncier
    drop constraint if exists fk_description_foncier_tabou_operation;
alter table if exists tabou_description_foncier
    add constraint fk_description_foncier_tabou_operation
    foreign key (id_operation) references tabou_operation;

alter table if exists tabou_amenageur
    drop constraint if exists fk_tabou_amenageur_tabou_type_amenageur;
alter table if exists tabou_amenageur
    add constraint fk_tabou_amenageur_tabou_type_amenageur
    foreign key (id_type_amenageur)
    references tabou_type_amenageur;

alter table if exists tabou_amenageur
    drop constraint if exists fk_tabou_amenageur_tabou_operation;
alter table if exists tabou_amenageur
    add constraint fk_tabou_amenageur_tabou_operation
    foreign key (id_operation)
    references tabou_operation;

alter table if exists tabou_description_financement_operation
    drop constraint if exists fk_tabou_description_financement_operation_tabou_type_financement_operation;
alter table if exists tabou_description_financement_operation
    add constraint fk_tabou_description_financement_operation_tabou_type_financement_operation
    foreign key (id_type_financement_operation)
    references tabou_type_financement_operation;

alter table if exists tabou_description_financement_operation
    drop constraint if exists fk_tabou_description_financement_operation_tabou_operation;
alter table if exists tabou_description_financement_operation
    add constraint fk_tabou_description_financement_operation_tabou_operation
    foreign key (id_operation)
    references tabou_type_financement_operation;

alter table if exists tabou_action_operation
    drop constraint if exists fk_tabou_action_operation_tabou_type_action_operation;
alter table if exists tabou_action_operation
    add constraint fk_tabou_action_operation_tabou_type_action_operation
    foreign key (id_type_action_operation)
    references tabou_type_action_operation;

alter table if exists tabou_action_operation
    drop constraint if exists fk_tabou_action_operation_tabou_operation;
alter table if exists tabou_action_operation
    add constraint fk_tabou_action_operation_tabou_operation
    foreign key (id_operation)
    references tabou_operation;

alter table if exists tabou_acteur
    drop constraint if exists fk_tabou_acteur_tabou_type_acteur;
alter table if exists tabou_acteur
    add constraint fk_tabou_acteur_tabou_type_acteur
    foreign key (id_type_acteur)
    references tabou_type_acteur;

alter table if exists tabou_acteur
    drop constraint if exists fk_tabou_acteur_tabou_operation;
alter table if exists tabou_acteur
    add constraint fk_tabou_acteur_tabou_operation
    foreign key (id_operation)
    references tabou_operation;

END $$;