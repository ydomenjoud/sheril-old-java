#!/bin/bash

# a lancer à la racine
export LIBS_PATH=$(find ./libs -name "*.jar" | tr '\n' ':')
mkdir -p ./classes

echo "Compilation en cours..."

set -o pipefail
find ./sources -name "*.java" -print | xargs javac -nowarn -Xlint:none -cp "${LIBS_PATH}" -d ./classes 2>&1


# On crée un dossier temporaire pour extraire les dépendances
mkdir -p ./tmp_libs
cd ./tmp_libs
for jar_file in ../libs/*.jar; do
    jar -xf "$jar_file" # On extrait le contenu de chaque lib
done
# On nettoie les manifestes des dépendances pour ne pas écraser le nôtre
rm -rf META-INF
cd ..

# On fusionne tes classes ET les classes extraites dans le JAR final
jar -cf ./sheril.jar -C ./classes . -C ./tmp_libs . > /dev/null

# Nettoyage du dossier temporaire
rm -rf ./tmp_libs

echo "✅ sheril.jar JAR créé avec succès"