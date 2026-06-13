#!/bin/bash
echo "0" > php/tour.txt
echo "0" > data/commun/tour.txt
java -cp sheril.jar Start init
java -cp sheril.jar Start addNewGalaxy 0