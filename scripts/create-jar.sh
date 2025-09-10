#!/bin/bash

# a lancer à la racine

export LIBS_PATH=$(find ./libs -name "*.jar" | tr '\n' ':')
mkdir -p ./classes
find ./sources -name "*.java" -print | xargs javac -cp "${LIBS_PATH}" -d ./classes
jar -cvf ./sheril.jar -C ./classes .
cd ./libs
for jar_file in *.jar; do jar -uf ./sheril.jar "$jar_file"; done