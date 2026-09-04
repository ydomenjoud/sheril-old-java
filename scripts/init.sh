#!/bin/bash
rm -rf data/tour* data/stats php/rapports/* php/stats/*
mkdir -p data/commun
chmod -R 0777 php/live/
# Copie du modÃ¨le de configuration si config.properties n'existe pas encore
[ -f config.properties ] || cp config.properties.sample config.properties
cp php/secure/connect.txt.sample php/secure/connect.txt
cp php/live/a.php.sample php/live/a.php
echo "0" > php/tour.txt
echo "0" > data/commun/tour.txt
# Si un nombre de joueurs est passÃ© en paramÃ¨tre, il surcharge la valeur de config.properties (entre 30 et 42)
if [ -n "$1" ]; then
	if [ "$1" -lt 30 ] || [ "$1" -gt 42 ]; then
		echo "Le nombre de joueurs doit Ãªtre compris entre 30 et 42."
		exit 1
	fi
	java -cp sheril.jar Start init "$1"
else
	java -cp sheril.jar Start init
fi
