# Pour les utilisateurs dockers :

Il est possible de générer une image docker en utilisant la commande suivante :

```
$ mvn clean package -DskipTests -Pdocker package
```

Si vous utilisez l'image docker vaut devez prendre en compte 2 éléments :

# Servlet container

L'image docker est basée sur Jetty pas sur Tomcat.

# Configuration

Il faut configurer l'application correctement. Voici un extrait du fichier de configuration a modifier en fonction de l'installation :

```
 <TODO>


```