
# Moteur de jeu basé sur celui d'océane https://github.com/ydomenjoud/oceane 


## tools

### créer un jar du projet 
```shell
./scripts/create-jar.sh
```

### utilise le jar

* ajouter un config.properties a côté du jar
* initialiser l'univers : ```java -cp sheril.jar Start init```
* initialiser l'univers : ```java -cp sheril.jar Start newRound```

## DOCKER

```shell
cp config.properties.sample config.properties
cp php/secure/connect.txt.sample php/secure/connect.txt
cp php/live/a.php.dist php/live/a.php
chmod -R 0777 php/live/
docker compose up -d
docker compose exec engine ./scripts/create-jar.sh
docker compose exec engine java -cp sheril.jar Start init
docker compose exec engine java -cp sheril.jar Start addNewGalaxy 0
docker compose exec engine java -cp sheril.jar Start newRound
```