-- Fichier de test permettant de générer la bdd embarquée - A supprimer

-- la table autorité : t_authority
DROP TABLE IF EXISTS t_authority;
CREATE TABLE t_authority
(
    id_authority serial PRIMARY KEY,
    code         varchar(250)                                                    -- Code d'autorité
);

-- la table refresh Token : t_refresh_token
DROP TABLE IF EXISTS t_refresh_token;
CREATE TABLE t_refresh_token (
  id_refresh_token serial PRIMARY KEY,
  token varchar(2048) unique NOT NULL
);

-- la table configuration : t_configuration
DROP TABLE IF EXISTS t_configuration;
CREATE TABLE t_configuration (
  id_configuration serial PRIMARY KEY,
  comment varchar(2048)
);