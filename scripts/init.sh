#!/bin/bash
java -cp sheril.jar Start init
java -cp sheril.jar Start addNewGalaxy 0
echo "0" > php/tour.txt
echo "0" > data/commun/tour.txt