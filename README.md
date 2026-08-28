
# Moteur de jeu basé sur celui d'océane https://github.com/ydomenjoud/oceane 

## tl;dr
```shell
git clone https://github.com/ydomenjoud/sheril-old-java.git sheril
# ou par ssh: git clone git@github.com:ydomenjoud/sheril-old-java.git sheril
cd sheril
docker compose up -d
docker compose exec engine bash ./scripts/init.sh
```
Aller sur http://localhost:666 pour créer votre premier commandant
Puis passer le tour avec :
```shell
docker compose exec engine java -cp sheril.jar Start newRound
```

## Configuration des secrets

Les fichiers contenant des identifiants sensibles ne sont pas versionnés. Avant de démarrer, copier chaque `.sample` vers son fichier réel et renseigner les valeurs :

```shell
cp php/secure/connect.txt.sample php/secure/connect.txt
cp php/secure/discord.php.sample php/secure/discord.php
```

- `php/secure/connect.txt` : identifiants de connexion à la base de données
- `php/secure/discord.php` : `client_id` / `client_secret` / `redirect_uri` de l'application OAuth2 Discord (portail développeur Discord → onglet OAuth2)

## Pour lancer le jeu sous DOCKER

### démarrage de la base de données, la console et le moteur
```shell
docker compose up -d
```

### initialisation environnement + Univers
```shell
docker compose exec engine bash ./scripts/init.sh
```

### Faire tourner un tour
```shell
docker compose exec engine java -cp sheril.jar Start newRound
```

### lister les commandants et leur mot de passe
```shell
docker compose exec db mysql -u user -p"password" sheril -e "SELECT NOM,NUMERO,LOGIN,MOT_DE_PASSE FROM aa_registre;"
```

### recréer le jar suite à une modification du code source
```shell
docker compose exec engine bash ./scripts/create-jar.sh
```

### Nettoyer le jeu pour recommencer
```shell
docker compose exec engine bash ./scripts/clean.sh
docker compose down -v
```

