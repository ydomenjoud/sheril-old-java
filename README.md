
# Moteur de jeu basé sur celui d'océane https://github.com/ydomenjoud/oceane 

## DOCKER

### démarrage de la base de données, la console et le moteur
```shell
docker compose up -d
```

### initialisation de l'environnement
```shell
docker compose exec engine bash ./scripts/prepare.sh
```

### initialisater un Univers avec une galaxie
```shell
docker compose exec engine bash ./scripts/init.sh
```

### Faire tourner un tour
```shell
docker compose exec engine java -cp sheril.jar Start newRound
```

### recréer le jar suite à une modification du code source
```shell
docker compose exec engine bash ./scripts/create-jar.sh
```

### recréer le jar suite à une modification du code source
```shell
docker compose exec engine bash ./scripts/clean.sh
```

