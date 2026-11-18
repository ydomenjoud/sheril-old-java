# Matrice des combats par taille

Toutes les tailles T3 à T9 l'une contre l'autre (attaquant × défenseur, égalités comprises),
4 armes de chaque côté (laser, plasma, torpille, missile, niveau V), boucliers V oui/non pour chaque camp :
3 136 scénarios, à minerai égal. Le camp dont le vaisseau coûte le plus aligne 20 vaisseaux,
l'autre autant que le même minerai le permet (jusqu'à 1 266 T3 contre 20 T9).
Agressivité « rage » des deux côtés, placement par défaut, sans héros. Un bouclier remplace 4 armes
(T3 : 1, T4 : 2, T5 : 3, T6 : 5, T7 à T9 : 10).

- `matrice.csv` : résultats, 50 combats par scénario, règles `multi`, `multi+fiab`, `multi+fiab80`.
- `matrice_combats.html` : page autonome (données intégrées, hors ligne) ; choix de l'armement et des
  boucliers de chaque camp et de la règle, tableau croisé des tailles avec le % de minerai encore vivant
  en fin de combat ; mode comparaison de deux états (écart en points). L'adresse (`#…`) garde la vue.
- `gen_matrice.py` : génère les scénarios en lots, du plus coûteux au plus léger.
- `lancer_matrice.sh` : lance les lots en parallèle puis fusionne dans `matrice.csv` ; reprend là où il
  s'est arrêté (les lots déjà calculés sont sautés).
- `generer_html.py` + `matrice_modele.html` : produisent la page à partir d'un `matrice.csv`.

Minerai vivant = 1 − pertes moyennes ÷ vaisseaux engagés (un vaisseau endommagé compte comme entier).

## Rejouer

Depuis la racine du dépôt, après compilation dans `classes/` (environ 20 min sur 20 cœurs) :

```bash
benchmarks/matrice/lancer_matrice.sh /chemin/travail 50 20 multi,multi+fiab,multi+fiab80
python3 benchmarks/matrice/generer_html.py /chemin/travail/matrice.csv matrice_combats.html
```
