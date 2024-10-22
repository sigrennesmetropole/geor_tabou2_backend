set -e
set -o pipefail

# Opérations à effectuer sur chaque hôte qui hébergera un environnement de déploiement du webservice.
# À exécuter en tant que super-utilisateur.

# Ce script suppose qu'une VM n'héberge qu'un seul environnement de déploiement pour une même application.
# Si cette prémisse venait à changer, éditer l'arborescence des applications (par exemple /opt/env-recette/${appname}),
# le nom du service systemd, etc.

# à éditer
appname=tabou2-springboot
appdescription="Application blanche, modèle de webservice développé avec Spring Boot."

###################################

# utilisateur et groupe applicatifs
useradd ${appname}
# groupadd ${appname} <- inutile car useradd crée le groupe

# ajoute l'user admin dans le groupe applicatif
usermod -a -G ${appname} admin

# crée les répertoires
mkdir -p /opt/${appname}/{properties,logs}
chown -R ${appname}:${appname} /opt/${appname}/
chmod -R ug+rxw /opt/${appname}/

# déclare l'application en tant que service
cat <<EOF > /etc/systemd/system/${appname}.service
[Unit]
Description=${appdescription}
After=syslog.target

[Service]
User=${appname}
Group=${appname}

ExecStart=/usr/bin/java -jar /opt/${appname}/${appname}.jar --${appname}.properties=/opt/${appname}/properties/${appname}.properties --logging.config=/opt/${appname}/properties/logBack.xml --jakarta.net.ssl.trustStore=/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts --jakarta.net.ssl.trustStorePassword=changeit
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=${appname}

[Install]
WantedBy=multi-user.target
EOF

# configure le démarrage automatique
systemctl enable ${appname}
