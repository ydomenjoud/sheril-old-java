# Matrice des combats par taille

Toutes les tailles T3 à T9 l'une contre l'autre (attaquant × défenseur, égalités comprises),
4 armes de chaque côté (laser, plasma, torpille, missile, niveau V), boucliers V oui/non pour chaque camp :
3 136 scénarios, à minerai égal. Le camp dont le vaisseau coûte le plus aligne 20 vaisseaux,
l'autre autant que le même minerai le permet (jusqu'à 1 146 T3 contre 20 T9).
Agressivité « rage » des deux côtés, placement par défaut, sans héros.

Chaque coque est remplie jusqu'au nombre maximal de cases de sa taille (`Const.TAILLE_VAISSEAUX`),
pour que la taille ne change jamais : le plus petit moteur qui donne une vitesse de 8, les
boucliers éventuels, puis des armes dans le reste. Un bouclier (4 cases) remplace 4 armes.

| Taille | Cases | Moteur (cases) | Armes | Boucliers | Armes avec boucliers |
|---|---|---|---|---|---|
| T3 | 17 | I (1) | 16 | 1 | 12 |
| T4 | 33 | II (4) | 29 | 2 | 21 |
| T5 | 65 | III (8) | 57 | 3 | 45 |
| T6 | 129 | IV (15) | 114 | 5 | 94 |
| T7 | 257 | V (30) | 227 | 10 | 187 |
| T8 | 512 | VI (60) | 452 | 10 | 412 |
| T9 | 1 025 | VII (120) | 905 | 10 | 865 |

- `matrice.csv` : résultats, 50 combats par scénario, règles `actuel` (moteur du commit `82b6849` : tir continu,
  cibles vivantes, enrayement 20 % − niveau) et `multi` (moteur précédent, par passes, sans enrayement).
- `matrice_combats.html` : page autonome (données intégrées, hors ligne) ; choix de l'armement et des
  boucliers de chaque camp et de la règle, tableau croisé des tailles avec le % de minerai encore vivant
  en fin de combat ; mode comparaison de deux états (écart en points). L'adresse (`#…`) garde la vue.
- `gen_matrice.py` : génère les scénarios en lots, du plus coûteux au plus léger.
- `lancer_matrice.sh` : lance les lots en parallèle puis fusionne dans `matrice.csv` ; reprend là où il
  s'est arrêté (les lots déjà calculés sont sautés).
- `generer_html.py` + `matrice_modele.html` : produisent la page à partir d'un `matrice.csv`.

Minerai vivant = 1 − pertes moyennes ÷ vaisseaux engagés (un vaisseau endommagé compte comme entier).

## Rejouer

Depuis la racine du dépôt, après compilation dans `classes/` (environ 7 min par règle sur 20 cœurs) :

```bash
benchmarks/matrice/lancer_matrice.sh /chemin/travail 50 20 actuel,multi
python3 benchmarks/matrice/generer_html.py /chemin/travail/matrice.csv matrice_combats.html
```
