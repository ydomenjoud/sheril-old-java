#!/bin/bash
cp config.properties.sample config.properties
cp php/secure/connect.txt.sample php/secure/connect.txt
cp php/live/a.php.sample php/live/a.php
mkdir -p data/commun
chmod -R 0777 php/live/