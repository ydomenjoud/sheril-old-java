
# Moteur de jeu basé sur celui d'océane https://github.com/ydomenjoud/oceane 

## tl;dr
```shell
git clone --single-branch -b main https://github.com/ydomenjoud/sheril-old-java.git sheril
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

## Pour lancer le jeu sous DOCKER

### démarrage de la base de données, la console et le moteur
```shell
docker compose up -d
```

### initialisation environnement + Univers
```shell
docker compose exec engine bash ./scripts/init.sh
```

<<<<<<< Updated upstream
=======
L'argument est le nombre de joueurs prévu, entre `30` et `42`. Si cet argument est omis (ex: `init.sh`), la valeur du paramètre `NB_JOUEURS` définie dans `config.properties` est utilisée.

La galaxie est fixée à `50 x 50` (pour 30 à 35 joueurs) ou `60 x 60` (pour 36 à 42 joueurs) et contient huit systèmes neutres proches par départ : quatre à une ou deux cases et quatre à trois ou quatre cases de la capitale. La capitale et ces huit neutres totalisent entre 155 et 165 planètes. Des systèmes neutres supplémentaires sont répartis dans chaque région de Voronoï selon le paramètre `SYSTEMES_REGIONAUX_PAR_PAQUET`.

## Configuration (`config.properties`)

Le fichier `config.properties` permet de personnaliser le comportement du moteur de jeu. Il est créé automatiquement lors du premier lancement du script d'initialisation par copie du modèle `config.properties.sample`.

### Section `# GENERATION`

Les paramètres de génération d'univers suivants peuvent être configurés :

- **`NB_JOUEURS`** (par défaut : `40`) : Nombre de joueurs prévus pour la génération initiale de la galaxie (valeur autorisée entre `30` et `42`). Cette valeur est utilisée lorsque `init.sh` est appelé sans argument.
- **`SYSTEMES_REGIONAUX_PAR_PAQUET`** (par défaut : `4`) : Nombre de systèmes neutres régionaux additionnels répartis dans la zone de Voronoï de chaque joueur au tour 0.

>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
### Nettoyer le jeu pour recommencer
=======
### nettoyer les données de jeu
>>>>>>> Stashed changes
```shell
docker compose exec engine bash ./scripts/clean.sh
docker compose down -v
```

