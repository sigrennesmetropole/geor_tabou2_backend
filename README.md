# tabou2

URL swagger : http://localhost:8080/v1/swagger-ui.html

Afin de pouvoir appeler les URLs d'authentification, installer une extension au navigateur permettant de modifier les headers (ModHeader pour chrome)
Et ajouter les header suivants :  

* sec-username 
* sec-roles
* sec-firstname
* sec-lastname
* sec-email
* sec-org
* sec-orgname

L'application doit être démarrée avec l'option 
<pre>-Dgeochestra.datadir=[projet]/tabou2-facade/src/main/resources</pre>

Afin de trouver 

* default.properties qui est la configuration georchestra par défaut
* tabou2-common.properties pour les éléments communs
* tabou2/tabou2.properties qui est surchargé sur les environnements lors du déploiements 

