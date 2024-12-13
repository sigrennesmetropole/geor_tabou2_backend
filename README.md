# tabou2

## I - Description de l'application

Le projet _git_ est construit comme suit :

- `docker` : ce répertoire contient des propositions de fichiers Dockerfile pour la construction/modification des images dockers ainsi  qu'une proposition pour le fichier _docker-compose.yml_
- `tabou2-facade` : il s'agit du sous-projet maven contenant l'application et les controleurs
- `tabou2-service` : il s'agit du sous-projet maven contenant les entités et les DAO
- `tabou2-storage` : il s'agit du sous-projet maven contenant les services métiers, les services techniques
- `readme` : les données nécessaires au présent document
- `resources` :  les resources avec notamment :
    - `bdd` qui contient les fichiers SQL d'initialisation
    - `openapi`qui contient le fichier openapi permettant de générer l'ensemble des services REST du back-office

## II - Installation

#### II.1 - Base de données

L'installation peut être réalisée soit :
* Dans une base de données dédiée
* Dans un schéma d'une base de données existantes

Dans tous les cas, il faut exécuter les scripts sql dans l'ordre suivant:
1. `[projet]/resources/bdd/create.sql` pour la création des tables tabou
2. `[projet]/resources/bdd/create_rm_database.sql` pour la création des tables Rennes Métropole
3. `[projet]/resources/bdd/common_data.sql` pour l'insertion des données par défaut

#### II.2 - Configuration de l'application

La configuration de l'application de trouve dans le répertoire `[projet]/tabou2-facade/src/main/resources`.

* `default.properties` qui est la configuration georchestra par défaut (notamment le nom de domaine georchestra, les paramètres de connexion au serveur LDAP)
* `tabou2-common.properties` pour les éléments communs (par exemple le répertoire temporaire utilisé par l'application). Ce fichier est inclu dans le WAR construit
* `tabou2/tabou2.properties` qui est surchargé sur les environnements lors du déploiements. Il contient notamment la configuration aux base de données Tabou, Droits des cités et Rennes Métropole. 

Le fichier `tabou2/tabou2.properties` fichier est détaillé comme suit:

```properties

# Path du serveur
server.servlet.context-path=/tabou2

# Base de données
spring.jpa.properties.javax.persistence.schema-generation.create-source=metadata
spring.jpa.properties.javax.persistence.schema-generation.scripts.action=create
spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target=create.sql   

# Hibernate
spring.jpa.properties.hibernate.current_session_context_class=org.springframework.orm.hibernate5.SpringSessionContext

# Base de données Droit des Cités
spring.ddc.datasource.jdbc-url=jdbc:postgresql://localhost:5432/tabou2
spring.ddc.datasource.username=root
spring.ddc.datasource.password=postgres
spring.ddc.datasource.driver-class-name=org.postgresql.Driver
spring.ddc.datasource.hibernate.show_sql=true
spring.ddc.datasource.hibernate.format_sql=true
spring.ddc.datasource.hibernate.hbm2ddl.auto=validate

# Base de données Tabou2
spring.tabou2.datasource.jdbc-url=jdbc:postgresql://localhost:5432/tabou2
spring.tabou2.datasource.username=root
spring.tabou2.datasource.password=postgres
spring.tabou2.datasource.driver-class-name=org.postgresql.Driver
spring.tabou2.datasource.hibernate.show_sql=true
spring.tabou2.datasource.hibernate.format_sql=true
spring.tabou2.datasource.hibernate.hbm2ddl.auto=validate

# Base de données SIG de Rennes Metropole
spring.sig.datasource.jdbc-url=jdbc:postgresql://localhost:5432/tabou2
spring.sig.datasource.username=sig_user
spring.sig.datasource.password=sig_user
spring.sig.datasource.driver-class-name=org.postgresql.Driver
spring.sig.datasource.hibernate.show_sql=true
spring.sig.datasource.hibernate.format_sql=true
spring.sig.datasource.hibernate.hbm2ddl.auto=validate

```

Description de quelques paramètres du fichier `tabou2/tabou2.properties`:
* `spring.tabou2.datasource.jdbc-url`: url de connexion à la base de données Tabou
* `spring.tabou2.datasource.username`: le nom d'utilisateur pour la base de données
* `spring.tabou2.datasource.password`: le mot de passe

Les propriétés préfixées par `spring.ddc` concernent la base de donnés Droit des cités, tandis que les propriétés préfixées par `spring.sig` concernent la base de données SIG de Rennes Métropole

#### II.3 Configuration du certificat

Un script est lancé au déploiement de l'image docker de l'application qui ajoute un certificat donné au keystore.  
Afin d'ajouter le bon certificat au bon keystore, il est nécessaire de remplir les informations adéquates dans le fichier `properties` de l'application :

```yaml
# dossier contenant le certificat
server.trustcert.keystore.path=
# filename du certificat
server.trustcert.keystore.cert=
# nom de l'alias du certificat à insérer dans le keystore
server.trustcert.keystore.alias=
# chemin absolu du keystore dans le container docker
server.trustcert.keystore.store=
# mot de passe du keystore
server.trustcert.keystore.password=
```
Par exemple :
```
server.trustcert.keystore.path=/etc/georchestra/
server.trustcert.keystore.cert=tabou2.crt
server.trustcert.keystore.alias=certificat-tabou2
server.trustcert.keystore.store=/usr/local/openjdk-11/lib/security/cacerts
server.trustcert.keystore.password=changeit
```
Si les variables ne sont pas remplies, le certificat n'est pas ajouté au keystore et l'application démarre normalement.

#### II.4 - Construction de l'application

L'application est construite à partir de la commande maven
`mvn -DskipTest package`

Le résultat de cette construction est :
* Un fichier WAR `[projet]/tabou2-facade/target/tabou2.war` déployable directement dans Tomcat ou Jetty
* Un fichier SpringBoot JAR `[projet]/tabou2-facade/target/tabou2.jar`

#### II.5 - Lancement de l'application

L'application peut être lancée :
* Soit dans un container Tomcat 9.

Il suffit alors de déposer le fichier WAR produit dans le répertoire webapps de Tomcat.

* Soit dans un container Jetty

Il suffit alors de copier le fichier WAR produit dans le répertoire webapps de Jetty puis de lancer Jetty

```sh
cp signalement.war /var/lib/jetty/webapps/tabou2.war
java -Djava.io.tmpdir=/tmp/jetty \
      -Dgeorchestra.datadir=[projet]/tabou2-facade/src/main/resources 	\
      -Xmx${XMX:-1G} -Xms${XMX:-1G}           \
      -jar /usr/local/jetty/start.jar"
```

* Soit en lançant l'application SpringBoot à partir du JAR 

```
java -jar signalement.jar \
      -Dgeochestra.datadir=[projet]/tabou2-facade/src/main/resources
```

Il est à noter que l'application doit être démarrée avec l'option suivante afin de définir le répertoire où se trouvent ses ressources.
<pre>-Dgeochestra.datadir=[projet]/tabou2-facade/src/main/resources</pre>

## II - Test de l'application

Pour tester le bon fonctionnement de l'application, il faut accéder à l'URL swagger : http://localhost:8080/swagger-ui.html

Afin de pouvoir appeler les URLs d'authentification, installer une extension au navigateur permettant de modifier les headers (ModHeader pour chrome)
Et ajouter les header suivants :  

* sec-username 
* sec-roles
* sec-firstname
* sec-lastname
* sec-email
* sec-org
* sec-orgname


