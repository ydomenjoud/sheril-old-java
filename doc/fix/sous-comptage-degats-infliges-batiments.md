# Sous-comptage des dégâts infligés lors de la destruction d'un bâtiment planétaire

- **Fichiers modifiés** : `sources/zIgzAg/jeu/oceane/Vaisseau.java`,
  `sources/zIgzAg/jeu/oceane/ConstructionPlanetaire.java`
- **Méthode en cause** : `Vaisseau.tirSurConstruction`
- **Nature** : bug logique (ordre des opérations), pas un problème de
  données — atteignable en jeu normal, sans configuration particulière.

## 1. Comportement observé

Rapporté par un utilisateur à partir d'un rapport de combat synthétique
(sans détail tour par tour) : attaque d'une flotte de 26 Bombardiers Zwaia
+ 10 Grands Bombardiers Standard contre une planète défendue par 2169
milices et 6 mines.

Le rapport indique, pour la même bataille :
- côté planète : **"6 mines détruites ayant encaissé 120 dégâts"**
- côté flotte assaillante : **"78 dégâts infligés"** par les Bombardiers
  Zwaia (seul type de vaisseau crédité de dégâts dans le rapport)

Écart de 42 points entre les dégâts que les bâtiments ont réellement
encaissés (120) et ce que la flotte assaillante déclare avoir infligé (78).
Ce n'est pas un cas de dégâts négatifs (une hypothèse explorée et écartée
au préalable, voir `doc/combat-comportements-non-documentes.md` sur la
branche `feature/combat-tests`) mais un sous-comptage systématique.

## 2. Cause racine

`Vaisseau.tirSurConstruction(ConstructionPlanetaire[] cibles, Heros h, Gouverneur g, boolean bombe)`
résout un tir de bombardement sur un bâtiment planétaire (mine, batterie,
bouclier...). Extrait du code original :

```java
cibles[index].ajouterDommages(arme.getDommagesSol());
int dommagesActuel = Math.min(arme.getDommagesSol(), cibles[index].getPointsDeStructureRestants());
dommagesEffectues += dommagesActuel;
```

`ConstructionPlanetaire.ajouterDommages(nb)` **applique** immédiatement le
dégât à la cible (`dommages += nb`, et marque `detruit = true` si le total
dépasse les points de structure du bâtiment). La ligne suivante appelle
`getPointsDeStructureRestants()` (= `Math.max(0, pointsDeStructure -
dommages)`) — mais **après** que `dommages` a déjà été incrémenté par le
coup en cours. Le "restant" mesuré ne reflète donc pas la structure
disponible *avant* le tir, mais ce qu'il en reste *après*.

Conséquence : dès qu'un tir détruit sa cible, ou "l'overkill" (dégâts du
tir supérieurs à la structure qu'il lui restait avant le coup), le
"restant après coup" vaut 0 (ou un reste très réduit). `dommagesActuel` —
et donc le compteur `dommagesEffectues` de l'attaquant, celui qui
alimente le "X dégâts infligés" du rapport — est tronqué, potentiellement
jusqu'à 0, **alors même que le bâtiment a bien reçu et enregistré la
totalité du coup** dans son propre champ `dommages`.

Le rapport, lui, calcule séparément le "dégâts encaissés" côté planète à
partir d'une estimation différente (nombre de bâtiments détruits × leur
structure totale, voir `Combat.ecrireDetailCombatPlanete`), qui n'a pas ce
défaut. Les deux moitiés du rapport sont donc calculées par deux
mécanismes non réconciliés, d'où l'écart visible dans tout combat où des
bâtiments sont détruits — l'écart grandit avec le nombre de coups fatals
ou surpuissants (typiquement : beaucoup de petites structures visées par
une grosse flotte, exactement le scénario rapporté : 6 mines contre 36
bombardiers).

## 3. Correctif proposé

Mesurer la structure restante **avant** d'appliquer le dégât, pas après —
un simple réordonnancement de deux lignes dans `Vaisseau.tirSurConstruction` :

```diff
-					cibles[index].ajouterDommages(arme.getDommagesSol());
-					int dommagesActuel = Math.min(arme.getDommagesSol(), cibles[index].getPointsDeStructureRestants());
+					int structureRestanteAvantLeCoup = cibles[index].getPointsDeStructureRestants();
+					cibles[index].ajouterDommages(arme.getDommagesSol());
+					int dommagesActuel = Math.min(arme.getDommagesSol(), structureRestanteAvantLeCoup);
 					dommagesEffectues += dommagesActuel ;
```

`dommagesActuel` devient alors : le minimum entre la puissance de l'arme et
ce qu'il restait effectivement à détruire — la quantité de dégâts qu'un
tir peut réellement "consommer" sur une structure ne peut pas dépasser ce
qu'il en restait avant le tir. Un éventuel surplus (overkill) n'est
légitimement pas compté, mais tout ce qui a été réellement détruit l'est.

### Correctif compagnon (nécessaire, pas optionnel)

En appliquant le correctif ci-dessus, `getPointsDeStructureRestants()` se
retrouve appelé **avant** `ajouterDommages()` — et donc avant que
`ConstructionPlanetaire` ait jamais résolu son `Batiment` sous-jacent
(résolution paresseuse via `determinerBatiment()`, jusque-là déclenchée
comme effet de bord par `ajouterDommages()`, qui l'appelle en premier).
Sans correctif compagnon, ceci produit un `NullPointerException` sur
`batiment.getPointsDeStructure()` (repéré lors de la vérification, voir
§4) dès le premier tir sur un bâtiment neuf.

`ConstructionPlanetaire.getPointsDeStructure()` (une méthode différente,
juste deux lignes plus bas dans le fichier) gère déjà ce cas correctement :

```java
public int getPointsDeStructure() {
    determinerBatiment();
    return (batiment != null) ? batiment.getPointsDeStructure() : 0;
}
```

`getPointsDeStructureRestants()` ne le faisait pas :

```diff
 public int getPointsDeStructureRestants() {
+    determinerBatiment();
     return Math.max(0, batiment.getPointsDeStructure() - dommages);
 }
```

Ce correctif compagnon aligne `getPointsDeStructureRestants()` sur le même
motif défensif que `getPointsDeStructure()` juste à côté — il corrige un
défaut préexistant dans `ConstructionPlanetaire` (l'appel non protégé à
`batiment.getPointsDeStructure()`) qui restait invisible tant que l'ordre
des appels dans `tirSurConstruction` le masquait.

## 4. Vérification effectuée

Deux scénarios ad hoc (repris de `CombatDegatsNegatifsTest.java`, écrit
lors de l'investigation sur la branche `feature/combat-tests` — **non
commités sur cette branche**, conformément à la consigne de n'y garder que
le correctif) :

1. **Coup unique overkill** : mine à 20 points de structure, déjà endommagée
   à 15/20, visée par un tir de 20 dégâts.
   - Avant le correctif : `dommagesActuel = 0` (le coup fatal n'est pas
     comptabilisé), alors que `mine.getDommages() = 35` (le coup est bien
     appliqué) et `mine.estDetruit() = true`.
   - Après le correctif : `dommagesActuel = 5` (= structure restante avant
     le coup, 20-15), `mine.getDommages() = 35`, `mine.estDetruit() = true`
     — inchangés pour la cible, corrigés pour le compteur de l'attaquant.

2. **Série de 3 tirs de 7** sur une mine à 20 de structure (7, 14, 21 cumulés
   → détruite au 3ᵉ tir avec 1 point de surplus).
   - Avant le correctif : total infligé déclaré = 14 (le 3ᵉ tir, fatal,
     compte 0), alors que `mine.getDommages() = 21`.
   - Après le correctif : total infligé déclaré = 20 (7 + 7 + 6, le 3ᵉ tir
     plafonné aux 6 points qu'il restait à détruire, pas les 7 bruts),
     `mine.getDommages() = 21` inchangé.

Exécution confirmée (via un `pom.xml` Maven temporaire, non versionné,
pointant sur `sources/` et un dossier de test temporaire, tous deux
supprimés après vérification) :

```
Test 2 - totalInflige=20 (attendu 20) ; mine.getDommages()=21 (attendu 21) ; estDetruit=true
Test 1 - degatsInfliges=5 (attendu 5) ; mine.getDommages()=35 (attendu 35) ; estDetruit=true
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

L'ensemble de `sources/` a également été recompilé sans erreur après les
deux changements (`javac` sur tous les fichiers de `sources/`, classpath
`libs/*.jar`).

## 5. Portée et limites du correctif

- S'applique à tout combat flotte-planète où des vaisseaux bombardent des
  bâtiments planétaires (mines, batteries, boucliers...) via
  `Vaisseau.tirSurConstruction` — c'est-à-dire tout combat où la flotte
  assaillante détruit au moins un bâtiment.
- Ne modifie ni la quantité de dégâts réellement appliquée aux bâtiments
  (`ConstructionPlanetaire.dommages`, donc ni leur destruction ni le
  déroulement du combat), ni aucune autre mécanique de combat — seul le
  compteur de statistiques `Vaisseau.dommagesEffectues` (et donc le
  "dégâts infligés" affiché côté attaquant) est concerné.
- Ne fait pas disparaître tout écart entre "infligé" et "encaissé" : les
  deux valeurs restent calculées par deux mécanismes différents (delta par
  tir côté attaquant vs estimation "nombre détruit × structure totale"
  côté défenseur, voir `Combat.ecrireDetailCombatPlanete`). Le correctif
  élimine la troncature systématique des coups fatals/surpuissants, qui
  est la source dominante de l'écart observé, sans garantir une
  correspondance exacte au point près dans tous les cas.
