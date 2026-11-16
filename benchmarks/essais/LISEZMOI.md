# Essais d'échelles du nombre de cibles

Scénarios et résultats des essais sur `Const.NB_CIBLES` (multi-cible), 100 combats par cas.

- `egal2.bench` : 100 T6 contre l'équivalent en minerai de T3, plasma V, avec et sans boucliers.
- `tailles.bench` : T1 à T9, chaque taille contre les trois suivantes, même arme des deux côtés (84 scénarios).
- `croise.bench` : mêmes tailles, chaque arme des petits contre chaque arme des gros (336 scénarios).
- `t_*.csv` : résultats de `tailles.bench` ; `c_*.csv` : résultats de `croise.bench`.

Échelles (valeurs de `NB_CIBLES`, cibles par round = valeur + 1) :

| Nom | NB_CIBLES |
|---|---|
| actuelle | 0,3,7,15,31,63,127,255,511,2000 |
| E | 0,1,3,7,31,63,127,255,511,2000 |
| I | 0,0,1,2,5,15,63,191,511,2000 |
| J | 0,2,4,9,23,63,127,255,511,2000 |
| K | 0,2,5,11,31,63,127,255,511,2000 |

Le fichier `c_actuelle.csv` contient aussi la règle `actuel` (cible unique).

Pour rejouer un essai avec une autre échelle (`BenchEchelle` remplace `Const.NB_CIBLES` puis lance le banc) :

```bash
java -cp "classes:libs/*" zIgzAg.jeu.oceane.BenchEchelle 0,1,3,7,31,63,127,255,511,2000 benchmarks/essais/croise.bench --n 100 --regles multi --csv sortie.csv
```

Conclusion : l'activation du multi-cible fait l'essentiel (l'essaim l'emporte dans 196 cas sur 336 au lieu de 314) ;
les échelles testées ne font pas significativement mieux que l'échelle actuelle.
