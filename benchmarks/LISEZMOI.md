# Banc d'essai du combat spatial

Rejoue N fois des combats flotte contre flotte avec le vrai moteur
(`Combat.resoudreCombatSpatial`), sous plusieurs jeux de règles, et compare les résultats.
Aucune partie n'est chargée et rien n'est écrit dans `data/`.

## Lancer

À la racine du projet :

```bash
./scripts/bench-combat.sh benchmarks/ --n 1000 --regles actuel,mono,fiab --csv resultats.csv
```

| Option | Défaut | Rôle |
|---|---|---|
| `<fichier.bench>` ou `<dossier>` | — | Scénarios à jouer (tous les `.bench` d'un dossier) |
| `--n` | 1000 | Nombre de combats par scénario et par jeu de règles |
| `--graine` | 1 | Graine du premier combat ; le combat i utilise `graine + i` |
| `--regles` | `actuel` | Jeux de règles séparés par des virgules |
| `--csv` | — | Écrit aussi les résultats dans un fichier CSV |

Chaque jeu de règles rejoue les mêmes graines : à graine égale, seule la règle change.

## Règles

| Nom | Effet |
|---|---|
| `actuel` | Le moteur tel qu'il tourne en partie : tirs sur cibles multiples (quand sa cible est détruite ou hors de portée, un vaisseau en désigne une autre pour les armes qui n'ont pas encore tiré, jusqu'à `Const.NB_CIBLES` fois selon sa taille) |
| `multi` | Identique à `actuel` (nom gardé pour les anciens résultats) |
| `mono` | Ancien moteur : une seule cible par vaisseau et par round |
| `fiab` | Chaque tir peut s'enrayer : la caractéristique « fiabilité » de l'arme est lue comme un pourcentage d'enrayement |

Elles se combinent avec `+` : `--regles actuel,mono+fiab`.

## Format d'un scénario

```
[Nom du scénario]
varier NIV = 1,3,5                       # un scénario par valeur ; {NIV} est remplacé partout
A: agressivite=rage cible=proche         # options de la flotte attaquante
A: heros=att:2,def:0,vit:0,moral:0
A: 10 Chasseur = moteur@1 laser@{NIV}x3  # <nombre> <nom> = <composants>
D: agressivite=normal
D: 5 Croiseur = moteur@3 plasma@2x10 bouclier@2x2 ; xp=2 moral=6 pos=15,5 cibles=petits
```

- `A:` est l'attaquant, `D:` le défenseur. `#` commence un commentaire.
- Plusieurs lignes `varier` donnent toutes les combinaisons. Une valeur peut contenir des
  espaces (une liste de composants) et une autre variable : `varier GROS = moteur@2 {ARME}@1x10, moteur@3 {ARME}@1x24`.
- **Composants** : `code@niveau` (niveau 1 à 10) ou le code exact de `ListeTechnologique`
  (`laserIII`), suivi de `xN` pour la quantité. Tous les composants de vaisseau du jeu sont acceptés.
- **Options de flotte** :
  - `agressivite` : `pillage`, `fuyard`, `prudent`, `normal`, `combatif`, `rage` ;
  - `cible` : `proche`, `chasseur`, `bombardier`, `cargo` ;
  - `heros` : `att`, `def`, `vit`, `moral`, `race`, `savoir` (maîtrise du savoir), `fanatique` (inspiration fanatique).
  - `minerai=N` : budget de minerai de la flotte. Un type dont le nombre est `*`
    (`A: * Chasseur = …`) reçoit alors autant de vaisseaux que le budget le permet, une fois
    payés les types à nombre fixe. Un seul `*` par camp.
- **Options de vaisseau**, après `;` :
  - `xp` : niveau d'expérience, 0 à 4 ;
  - `moral` : niveau de moral, 0 à 11 (4 par défaut, comme un équipage neuf) ;
  - `race` : race de l'équipage ;
  - `pos=x,y` : position imposée par la stratégie ;
  - `cibles` : `petits`, `gros`, ou une liste de tailles de 1 à 10 par ordre de préférence.

## Colonnes

Avant chaque tableau, le banc affiche pour chaque camp sa composition, son nombre de
vaisseaux, son minerai, son prix en centaures et son nombre de cases. Le CSV reprend ces
valeurs (`minerai_a`, `prix_a`, `cases_a`, `composition_a`, et de même pour `d`).

| Colonne | Sens |
|---|---|
| `vict.A%`, `vict.D%` | Combats où l'autre flotte est anéantie |
| `mutuel%` | Les deux flottes anéanties au même round |
| `aucun%` | Combat arrêté faute de combativité, sans flotte anéantie |
| `rounds` | Nombre moyen de rounds |
| `pertes A`, `pertes D` | Nombre moyen de vaisseaux perdus |
| `dom. A`, `dom. D` | Points de dommages moyens subis par la flotte |

Avec 1000 combats, un pourcentage est précis à environ ±3 points.

## Limites

- Combat spatial seulement, un combat isolé : les flottes, stratégies et héros sont recréés
  à chaque combat. L'usure des boucliers d'un combat à l'autre dans le même tour n'est pas reproduite.
