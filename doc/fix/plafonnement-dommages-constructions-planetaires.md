# Correctif : le compteur de dégâts d'une construction planétaire n'était jamais plafonné à sa structure

## 1. Comportement observé

Sur un serveur de test du dépôt amont exécutant une variante de rapport plus
détaillée (colonnes "Dommages persistants avant/après", non présentes dans ce
dépôt), la colonne "Dommages encaissés" d'une ligne "Mine" affichait un
delta de la forme `(+-263)` au lieu de `(-263)`, avec une valeur "Dommages
persistants avant" de **283** pour seulement 3 mines encore vivantes (20
points de structure chacune, soit un maximum théorique de 60) — jusqu'à
**4,7 fois** la structure totale que ces mines pouvaient physiquement
encaisser. Le même schéma se répète sur 5 autres lignes indépendantes issues
de 3 rapports réels distincts (détail complet et calcul algébrique dans
`doc/bugs/dommages-persistants-mines-plafond-manquant.md`).

Ce dépôt ne dispose pas de ces colonnes de rapport détaillées, mais le
mécanisme sous-jacent — le compteur `dommages` d'un bâtiment planétaire qui
dépasse sa structure — existe ici aussi et a été reproduit directement sur
le code de ce dépôt (§3).

## 2. Cause racine

### 2.1 `ConstructionPlanetaire.ajouterDommages` n'a jamais de plafond

```java
// sources/zIgzAg/jeu/oceane/ConstructionPlanetaire.java (avant correctif)
public void ajouterDommages(int nb) {
    determinerBatiment();
    dommages = dommages + nb;
    if (dommages > batiment.getPointsDeStructure())
        detruit = true;
}
```

Le champ `dommages` cumule chaque coup reçu sans jamais être borné à la
structure du bâtiment. Le drapeau `detruit` est positionné dès que ce cumul
dépasse la structure, mais **rien n'empêche `dommages` de continuer à
grossir après ce point**.

### 2.2 Une construction déjà détruite reste une cible valide jusqu'à la fin du round

`Combat.tirAirSol` construit la liste des bâtiments ciblables **une seule
fois en tête de round** :

```java
// Combat.java (comportement actuel, NON modifié par ce correctif)
ConstructionPlanetaire[] listeC = p.getBatiments();
...
ArrayList strato = f.forceAttaqueStratospherique(strategie.getAgressivite());
...
tirDefensesPlanetaires(listeC, strato, sol, g, h, true, c2);
nbPopDefensive = tirAirSol(c1, strato, listeC, nbPopDefensive, true, g, h);
...
p.eliminerPertesBatiments();          // <-- retrait des bâtiments détruits, UNE SEULE FOIS,
listeC = p.getBatiments();            //     après que tous les tirs "strato" du round aient eu lieu
```

Dans `tirAirSol`, chaque vaisseau attaquant choisit sa cible au hasard dans
cette même liste figée (`Univers.getInt(cibles.length)`), **sans jamais
vérifier `cibles[index].estDetruit()`** avant de tirer. Tant que
`eliminerPertesBatiments()` n'a pas été appelé (c'est-à-dire pendant toute la
première salve "stratosphérique" du round), un bâtiment déjà détruit peut
donc continuer à être visé par tous les tirs suivants du même round — et
chacun de ces tirs incrémente son `dommages` sans limite via §2.1.

**Cette spécificité (liste de cibles calculée une seule fois par round,
purge différée en fin de salve) n'est pas modifiée par ce correctif** : elle
fait partie du fonctionnement du moteur de combat (répartition des tirs
d'une salve sur l'état des cibles en début de manche) et son évolution
éventuelle relève d'une décision de conception distincte, hors du périmètre
de ce correctif ponctuel. Le correctif proposé ici agit uniquement en aval,
sur le compteur `dommages` lui-même : quel que soit le nombre de tirs qu'un
bâtiment déjà détruit continue d'encaisser dans le round, son compteur ne
doit jamais dépasser ce que sa structure permet physiquement.

## 3. Vérification effectuée (résultats numériques réels)

Test exécuté sur le vrai code de production (`Vaisseau.tirSurConstruction`,
`ConstructionPlanetaire.ajouterDommages`), sans réflexion pour forcer un état
initial : une flotte de 26 bombardiers (8 dégâts au sol chacun) tirant en un
seul round sur 3 mines survivantes (20 points de structure chacune, plafond
théorique 60).

**Avant correctif :**
```
dommages() par mine :
  208 (detruite=true)
  0 (detruite=false)
  0 (detruite=false)
Total dommages = 208 ; plafond théorique = 60 ; dépassement = 148
```

**Après correctif** (même scénario, seul `ajouterDommages` modifié) :
```
dommages() par mine :
  20 (detruite=true)
  0 (detruite=false)
  0 (detruite=false)
Total dommages = 20 ; plafond théorique = 60 ; dépassement = 0
```

Le dépassement de 148 points disparaît intégralement ; le compteur reste
désormais toujours dans les bornes physiques du bâtiment.

## 4. Correctif proposé

```diff
 	public void ajouterDommages(int nb) {
 		determinerBatiment();
-		dommages = dommages + nb;
-		if (dommages > batiment.getPointsDeStructure())
+		dommages = Math.min(dommages + nb, batiment.getPointsDeStructure());
+		if (dommages >= batiment.getPointsDeStructure())
 			detruit = true;
 	}
```

Aucun correctif compagnon n'a été nécessaire : `getPointsDeStructureRestants()`
(`Math.max(0, structure - dommages)`) et `estDetruit()` continuent de
fonctionner correctement avec un `dommages` désormais borné — ils étaient
déjà cohérents avec cette invariante, seule la source qui pouvait la violer
est corrigée.

## 5. Portée et limites du correctif

**Corrige :**
- L'accumulation illimitée du compteur `dommages` d'un bâtiment planétaire
  au-delà de sa structure, dans un round où plus de vaisseaux que de
  bâtiments cibles continuent de tirer après qu'une cible soit détruite.
- Par extension, empêche toute future divergence entre un total de dégâts
  "réel" (borné par la structure) et un total "affiché" recalculé à partir
  d'un ancien snapshot — la classe de défaut à l'origine du symptôme
  "(+-N)" observé sur le serveur de test amont (voir
  `doc/bugs/dommages-persistants-mines-plafond-manquant.md`).

**Ne corrige PAS :**
- Le bug d'affichage séparé dans `Combat.ecrireDetailCombatPlanete`, où le
  préfixe `"(+"` est concaténé sans condition de signe — ce dépôt n'a pas
  cette méthode dans sa forme "5 colonnes / dommages persistants" observée
  sur le serveur amont, mais si une évolution future de rapport venait à
  exposer un calcul de delta similaire, ce bug d'affichage resterait entier
  et indépendant de ce correctif.
- Les valeurs déjà stockées en base pour des parties en cours qui auraient
  accumulé un `dommages` excessif avant l'application de ce correctif : le
  plafond n'agit que sur les appels futurs à `ajouterDommages`, pas
  rétroactivement sur les données existantes.
- Le mécanisme de ciblage lui-même (liste de cibles figée en début de round,
  purge différée des bâtiments détruits) : volontairement non modifié, voir
  §2.2.
