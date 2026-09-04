#!/bin/bash
mkdir -p data/commun
chmod -R 0777 php/live/
# Copie du modèle de configuration si config.properties n'existe pas encore
[ -f config.properties ] || cp config.properties.sample config.properties
cp php/secure/connect.txt.sample php/secure/connect.txt
cp php/live/a.php.sample php/live/a.php
echo "0" > php/tour.txt
echo "0" > data/commun/tour.txt
<<<<<<< Updated upstream
java -cp sheril.jar Start init
=======
# Si un nombre de joueurs est passé en paramètre, il surcharge la valeur de config.properties (entre 30 et 42)
if [ -n "$1" ]; then
	if [ "$1" -lt 30 ] || [ "$1" -gt 42 ]; then
		echo "Le nombre de joueurs doit être compris entre 30 et 42."
		exit 1
	fi
	java -cp sheril.jar Start init "$1"
else
	java -cp sheril.jar Start init
fi
>>>>>>> Stashed changes
