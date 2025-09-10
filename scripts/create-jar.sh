#!/bin/bash

# a lancer à la racine
export LIBS_PATH=$(find ./libs -name "*.jar" | tr '\n' ':')
mkdir -p ./classes
find ./sources -name "*.java" -print | xargs javac -cp "${LIBS_PATH}" -d ./classes
jar -cvf ./sheril.jar -C ./classes .
for jar_file in libs/*.jar; do jar -uf ./sheril.jar "$jar_file"; done