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
| `--regles` | `actuel` | Le moteur tel qu'il tourne en partie (commit `82b6849`) : tir continu (à son tour, un vaisseau enchaîne ses cibles tant qu'il lui reste des armes, jusqu'à `Const.NB_CIBLES` selon sa taille), cibles vivantes, enrayement de 20 % − niveau de l'arme pour toutes les armes |
| `ancien` / `multi` | Moteur d'avant `82b6849` : cibles multiples par passes partagées (une cible par vaisseau et par passe), épaves redésignables, sans enrayement. Les anciens résultats nommés `multi…` ont été calculés avec lui |
| `mono` | Moteur d'origine : une seule cible par vaisseau et par round, sans enrayement |
| `fiab` | Active l'enrayement (`Arme.getTauxEnrayement`, 20 % − niveau de l'arme), après un `ancien` ou un `mono`. Avant `82b6849`, il lisait la colonne « fiabilité » de `ListeCaracArmes` (laser 20, torpille 25, missile 30…) : les anciens résultats `…+fiab` ne sont plus reproductibles à l'identique |
| `fiabNN` | Enrayement identique de 100 − NN % pour toutes les armes (`fiab80` : 20 % d'enrayement, quel que soit le niveau) |
| `fiabtA-B` | Enrayement selon la taille du vaisseau qui tire : A % pour une coque T1, B % pour une T10, linéaire entre les deux (`fiabt40-0` : T3 31 %, T6 18 %, T9 4 %) |
| `fiabtailleN` | Taux de l'arme (20 % − niveau), moins N points par cran de taille du vaisseau qui tire, T1 = 0 (`fiabtaille1`, arme V : T3 14 %, T9 8 %) |
| `continu` | Tir continu, après un `ancien` ou un `mono` |
| `vivantes` | Cibles vivantes, après un `ancien` ou un `mono` : la liste de secours du choix de cible (toute la flotte adverse) écarte les vaisseaux déjà détruits pendant le round |

Les règles s'appliquent de gauche à droite et se combinent avec `+` : `ancien` et `mono` remettent
tir continu, cibles vivantes et enrayement à zéro, les suivantes les réactivent une à une
(`--regles actuel,ancien,ancien+continu+vivantes,mono+fiab`).

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
