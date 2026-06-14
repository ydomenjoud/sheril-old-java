#!/bin/bash
mkdir -p data/commun
chmod -R 0777 php/live/
cp config.properties.sample config.properties
cp php/secure/connect.txt.sample php/secure/connect.txt
cp php/live/a.php.sample php/live/a.php
echo "0" > php/tour.txt
echo "0" > data/commun/tour.txt
java -cp sheril.jar Start init
java -cp sheril.jar Start addNewGalaxy 0