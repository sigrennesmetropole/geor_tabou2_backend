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

* `default.properties` qui est la configuration georchestra par défaut
* `tabou2-common.properties` pour les éléments communs
* `tabou2/tabou2.properties` qui est surchargé sur les environnements lors du déploiements 

Ces fichiers permettent notamment de configurer les accès aux bases de données, les paramètres de connexion au serveur LDAP.

#### II.3 - Construction de l'application

L'application est construite à partir de la commande maven
`mvn -DskipTest package`

Le résultat de cette construction est :
* Un fichier WAR `[projet]/tabou2-facade/target/tabou2.war` déployable directement dans Tomcat ou Jetty
* Un fichier SpringBoot JAR `[projet]/tabou2-facade/target/tabou2.jar`

#### II.4 - Lancement de l'application

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

Pour tester le bon fonctionnement de l'application, il faut accéder à l'URL swagger : http://localhost:8080/v1/swagger-ui.html

Afin de pouvoir appeler les URLs d'authentification, installer une extension au navigateur permettant de modifier les headers (ModHeader pour chrome)
Et ajouter les header suivants :  

* sec-username 
* sec-roles
* sec-firstname
* sec-lastname
* sec-email
* sec-org
* sec-orgname


