#!/bin/bash
# Banc d'essai du combat spatial. À lancer à la racine du projet.
#   ./scripts/bench-combat.sh benchmarks/ --n 1000 --regles actuel,mono,fiab --csv resultats.csv
set -e
LIBS_PATH=$(find ./libs -name "*.jar" | tr '\n' ':')
mkdir -p ./classes
find ./sources -name "*.java" -print | xargs javac -nowarn -Xlint:none -cp "${LIBS_PATH}" -d ./classes
java -cp "./classes:${LIBS_PATH}" zIgzAg.jeu.oceane.BenchCombat "$@"
