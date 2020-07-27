#!/bin/bash
read -p "Entrer le nom du projet: " projet
patternProjet='appbl'
clientName='rm'
rm -rf .git
find . -name "*.*" -print0 | while read -d $'\0' file
do
	if [[ -f $file ]]; then #teste si c'est un fichier
		filename="$(basename -- $file)" #donne le nom du fichier et son extension sans le chemin
		if [[ $filename != "rename.sh" ]]; then
			sed -i "s/$patternProjet/$projet/g" $file #remplace toutes les occurences de $patternProjet dans le fichier par $projet
			echo "$filename a été parcouru"
			if [[ $filename == *"$patternProjet"* ]]; then #teste si $filename contient $patternProjet
				newfilename="${filename//$patternProjet/$projet}" #remplace $patternProjet dans le nom du fichier par $projet
				newfile="${file//$filename/$newfilename}" #donne le chemin complet avec le nouveau nom de fichier
				mv $file $newfile #renommage du fichier
				echo "$filename renommé en $newfilename"
			fi
		fi
	fi
done

mv appbl-storage/src/main/java/${clientName}/appbl "appbl-storage/src/main/java/${clientName}/${projet}"
mv appbl-facade/src/main/java/${clientName}/appbl "appbl-facade/src/main/java/${clientName}/${projet}"
mv appbl-service/src/main/java/${clientName}/appbl "appbl-service/src/main/java/${clientName}/${projet}"
mv appbl-service/src/test/java/${clientName}/appbl "appbl-service/src/test/java/${clientName}/${projet}"

mv appbl-storage "${projet}-storage"
mv appbl-facade "${projet}-facade"
mv appbl-front "${projet}-front"
mv appbl-service "${projet}-service"
echo "L'exécution du script est terminé..."
echo "Vous pouvez fermer la fenêtre"
sleep 60
