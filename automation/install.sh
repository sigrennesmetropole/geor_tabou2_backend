#!/bin/bash

# ajout module
helm upgrade --install -f back.yaml -f config/${TARGET_ENV}/back.${TARGET_ENV}.yaml tabou2-back-${TARGET_ENV} boost-stable/boost-deploy

# création du déploiement
kubectl apply -f config/${TARGET_ENV}/secret.${TARGET_ENV}.yaml

# ajout du répertoire partagé
sed -i "s/TARGET_ENV/${TARGET_ENV}/g" back-config.yaml

kubectl patch deployment tabou2-back-${TARGET_ENV}-rm-tabou2-back --patch-file back-config.yaml

kubectl apply -f config/${TARGET_ENV}/back.ingress.${TARGET_ENV}.yaml

# on attend que tout soit démarré
sleep 15

# copie des fichiers
POD_NAME=`kubectl get pods -o name | grep "tabou2-${TARGET_ENV}" | sed -e 's:pod\/::g'`
echo "Target pod $POD_NAME"
if [ ! -z "$POD_NAME" ]; then
	kubectl cp "config/${TARGET_ENV}/log4j2.xml" $POD_NAME:"/etc/georchestra/tabou2/log4j2.xml"
	kubectl cp "config/${TARGET_ENV}/tabou2.properties" $POD_NAME:"/etc/georchestra/tabou2/tabou2.properties"
fi

# forcer le rédémarrage
if [ ! -z "${FORCE_PODS}" ]; then 
	kubectl delete pod -l app=tabou2-back-${TARGET_ENV} ; 
fi
