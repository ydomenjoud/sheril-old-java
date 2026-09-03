# Combat : un bâtiment déjà détruit mais pas encore balayé apparaît comme "détruit à 0 dégât" au combat suivant

- **Fichiers en cause** : `sources/zIgzAg/jeu/oceane/Combat.java`
  (seul appelant de `Planete.eliminerPertesBatiments()`),
  `sources/zIgzAg/jeu/oceane/Planete.java` (`eliminerPertesBatiments`,
  `listeEquipementsNombresDommages`), `sources/zIgzAg/jeu/oceane/
  RapportCombatData.java` (`fromCombatPlanete`, reconstruction du delta
  de dégâts par tour).
- **Nature** : bug logique (suppression différée d'un bâtiment détruit),
  confirmé par un test isolé exécutant le vrai code de production
  (`Planete`, `ConstructionPlanetaire`, `Univers` mocké statiquement pour
  la seule résolution de technologie) — voir §3.

## 1. Comportement observé (signalé par l'utilisateur)

Sur `https://sheril.pbem-france.net/yann/test2/1tour17/`, dans le combat
entre la flotte **Juqav(11)** (commandant Salardon) et la planète
**Wiryxi 10 (10)** (commandant mabeur), l'affichage des dégâts subis par
les bâtiments planétaires est incohérent.

Extrait de `combat.htm` (export JSON embarqué, tour 1 de ce combat) :

```json
"batiments": [
  {
    "nom": "Usine d'optimisation planétaire de type V",
    "nombre": 0,
    "variationNombre": -1,
    "degatsEncaisses": 50,
    "variationDegats": 0,
    "degatsInfliges": 0
  },
  {
    "nom": "Mine",
    "nombre": 0,
    "variationNombre": -4,
    "degatsEncaisses": 80,
    "variationDegats": 40,
    "degatsInfliges": 0
  }
]
```

L'Usine d'optimisation planétaire de type V passe de 1 à 0 exemplaire ce
tour (`variationNombre: -1`, elle vient donc d'être détruite), mais
affiche **0 dégât supplémentaire encaissé ce tour** (`variationDegats:
0`) — contradictoire en apparence : comment un bâtiment peut-il être
détruit "ce tour-ci" sans qu'aucun dégât n'y ait été enregistré ce
tour-ci ? À titre de comparaison, la Mine, détruite le même tour
(`variationNombre: -4`), affiche bien un delta de dégâts cohérent
(`variationDegats: 40`).

## 2. Cause racine

`ConstructionPlanetaire.ajouterDommages(nb)` :

```java
public void ajouterDommages(int nb) {
    determinerBatiment();
    dommages = Math.min(dommages + nb, batiment.getPointsDeStructure());
    if (dommages >= batiment.getPointsDeStructure())
        detruit = true;
}
```

Un bâtiment atteignant `dommages == structure` est marqué `detruit =
true` — **mais rien ne le retire de `Planete.batiments` à cet instant.**
Le retrait effectif n'a lieu que via :

```java
public void eliminerPertesBatiments() {
    ArrayList elimine = new ArrayList();
    for (int i = 0; i < batiments.size(); i++)
        if (((ConstructionPlanetaire) batiments.get(i)).estDetruit())
            elimine.add(batiments.get(i));
    for (int i = 0; i < elimine.size(); i++)
        eliminerBatiment((ConstructionPlanetaire) elimine.get(i));
}
```

Or `eliminerPertesBatiments()` **n'est appelée que depuis
`Combat.combatFlottePlanete`** — confirmé par recherche exhaustive
(`grep -rn "eliminerPertesBatiments()" sources/`) : aucun autre site
d'appel dans tout le code source, pas de balayage de fin de tour, pas de
nettoyage générique ailleurs.

Conséquence : un bâtiment qui atteint `dommages == structure` (donc
`estDetruit() == true`) reste néanmoins compté comme vivant — dans
`Planete.getBatiments()`, dans `Planete.listeEquipementsNombresDommages()`
(donc dans le nombre de bâtiments et le total de dégâts agrégés) — tant
qu'aucun combat ne se produit sur cette planète pour déclencher le
balayage. Si le coup fatal a lieu à un moment où plus aucun combat ne
suit dans l'immédiat (fin du combat en cours après le dernier tir utile,
ou bâtiment achevé par une source hors de la boucle de combat qui suit),
le bâtiment "zombie" survit jusqu'au **prochain** combat sur cette
planète — potentiellement un tour de jeu plus tard, avec un tout autre
attaquant.

C'est exactly ce qui explique l'incohérence rapportée : au moment où le
combat Juqav-vs-Wiryxi 10 commence, l'Usine V est déjà à `dommages ==
structure` (probablement depuis un combat antérieur ou une action
antérieure ce même tour) mais toujours listée comme 1 exemplaire vivant.
Le premier tour du combat de Juqav appelle
`p.eliminerPertesBatiments()` (comme toute résolution de combat) et la
fait disparaître — mais comme son état "avant ce combat" (utilisé par
`RapportCombatData.fromCombatPlanete` pour calculer le delta) affichait
déjà `dommages == structure`, le calcul du delta ressort à 0 : le combat
de Juqav n'a *lui-même* rien eu à faire pour l'achever, il a seulement
"découvert" une destruction déjà consommée. La Mine, elle, n'était pas
encore à son seuil de destruction avant ce combat — Juqav lui inflige
réellement les dégâts qui la détruisent ce tour, d'où un
`variationDegats` non nul et cohérent pour elle.

`RapportCombatData.fromCombatPlanete` reconstruit le total cumulé de
dégâts ainsi (calcul en lui-même correct, il ne fait que refléter fidèlement
l'état qu'on lui donne) :

```java
int dom  = nT[1] + (dT[0] - nT[0]) * nbCases;   // total cumulé "après ce tour"
int domA = mT[1] + (dT[0] - mT[0]) * nbCases;   // total cumulé "avant ce tour"
bat.degatsEncaisses  = dom;
bat.variationDegats  = dom - domA;
```

(`mT`/`nT` = état agrégé avant/après ce tour de combat, `dT[0]` = nombre
initial de bâtiments de ce type au tout début du combat, `nbCases` = leur
structure unitaire.) Le défaut n'est donc pas dans cette formule, mais
dans les données qu'elle reçoit : `mT` (l'état "avant ce combat") reflète
déjà un bâtiment fantôme resté artificiellement "vivant" avec des dégâts
au maximum.

## 3. Vérification effectuée

Test isolé (`BatimentDetruitLazySweepTest`, non commité sur cette
branche — exécuté puis supprimé, conformément au principe de ne garder
sur cette branche que le rapport), appelant directement le vrai code de
production (`Planete`, `ConstructionPlanetaire`), `Univers` mocké
statiquement (Mockito) pour la seule résolution de `Univers.getTechnologie`
:

```java
Batiment batiment = fabriquerBatiment("usineV", /*structure=*/ 50);
Planete planete = new Planete();
planete.initialiserBatiments();
ConstructionPlanetaire cp = new ConstructionPlanetaire("usineV");
planete.ajouterBatiment(cp);

cp.ajouterDommages(50);                    // dommages == structure
assertTrue(cp.estDetruit());               // -> vrai

assertEquals(1, planete.getBatiments().length);   // -> toujours listé comme vivant !

Map avant = planete.listeEquipementsNombresDommages();
int[] avantT = (int[]) avant.get("usineV");
assertEquals(1, avantT[0]);                // nombre agrégé : 1 (alors que détruit)
assertEquals(50, avantT[1]);               // dégâts agrégés : déjà au maximum

planete.eliminerPertesBatiments();         // seul ce balayage explicite le retire
assertEquals(0, planete.getBatiments().length);
```

Exécuté avec succès (`mvn test`, via un `pom.xml` temporaire pointant
`sources/`, supprimé après vérification) :

```
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Confirme empiriquement, avec le vrai code : un bâtiment `estDetruit() ==
true` reste compté comme vivant (nombre et dégâts agrégés inclus) tant
qu'aucun `eliminerPertesBatiments()` n'est appelé — et ce dernier n'est
jamais appelé hors d'un combat actif (recherche exhaustive, §2).

## 4. Correctifs envisageables — non implémentés

Deux angles possibles, non tranchés ici (à discuter avant implémentation) :

**Option A — balayer immédiatement.** Appeler
`eliminerPertesBatiments()` dès qu'un bâtiment devient détruit, plutôt
que de compter sur le prochain combat pour le faire :
- dans `ConstructionPlanetaire.ajouterDommages`, on ne peut pas appeler
  `eliminerPertesBatiments()` directement (la classe n'a pas de référence
  à la `Planete` qui la contient) ;
- il faudrait soit passer la `Planete` (ou un callback) à
  `ajouterDommages`, soit ajouter un balayage systématique en fin de
  résolution de tour (`DeroulementDuTour` ou `Commandant.
  resolutionGestionSystemes`), garantissant qu'aucun bâtiment "zombie"
  ne survit au-delà du tour où il a été détruit.

**Option B — corriger l'attribution, pas la suppression.** Conserver le
balayage tardif (peu coûteux, déjà cohérent pour l'état du jeu — un
bâtiment détruit ne défend plus, ne produit plus, qu'il soit ou non
encore listé), mais faire en sorte que `RapportCombatData.
fromCombatPlanete` (ou une étape antérieure) ne compte pas comme
"destruction de ce combat" un bâtiment qui était déjà à `dommages ==
structure` **avant** que ce combat ne commence — par exemple en
excluant du delta les bâtiments déjà `estDetruit()` dans l'état "avant
ce combat", ou en réattribuant leur destruction au combat/tour où le
seuil a réellement été franchi (nécessiterait de conserver un horodatage
de la destruction sur `ConstructionPlanetaire`, pas seulement un booléen).

**Recommandation** : l'option A (balayage immédiat, ou a minima
systématique en fin de tour) est la plus simple et corrige le problème à
la racine — un bâtiment détruit ne devrait jamais être compté comme
vivant, dans aucun rapport, quelle qu'en soit la raison. L'option B est
plus chirurgicale mais laisse subsister le bâtiment zombie dans toutes
les autres statistiques (nombre de bâtiments, défense planétaire...)
entre le tour de sa destruction réelle et le prochain combat.

**Non implémenté à ce stade**, conformément à la demande : bug analysé
et documenté, correctifs proposés, aucun appliqué.

## 5. Portée et question ouverte

- N'affecte que l'affichage/la comptabilité par tour de combat — l'état
  final du jeu (bâtiment bel et bien détruit) n'est jamais faux, seul le
  moment/l'attribution de sa destruction dans le rapport peut être
  trompeur.
- Question ouverte, hors accès aux données réelles de cette partie (pas
  de `comm.txt`/`sys.txt` disponible pour `test2`, contrairement aux cas
  précédents où `analyse/` fournissait l'état réel) : quel événement
  antérieur a porté l'Usine V à `dommages == structure` avant le combat
  de Juqav ? Cela ne change rien au correctif proposé, mais confirmerait
  le scénario exact.

  Éléments de réponse rassemblés (sans trancher définitivement, faute de
  données antérieures à `tour15`) :
  - **`ConstructionPlanetaire.ajouterDommages` n'a qu'un seul site
    d'appel dans tout le code** (`Vaisseau.tirSurConstruction`,
    `Vaisseau.java` ~ligne 522), lui-même appelé uniquement depuis
    `tirAirSol`, dans la boucle de `Combat.combatFlottePlanete`. Aucun
    autre mécanisme (sabotage, espionnage, bombardement hors combat...)
    ne peut endommager un bâtiment planétaire — la piste "autre source
    de dégâts" est donc écartée.
  - Le rapport de combat (`combat.htm`) des tours **15 et 16** de cette
    même partie (`1tour15/`, `1tour16/`) ne mentionne **aucun combat**
    impliquant "Wiryxi 10", et le combat de Juqav (tour 17) est le seul
    de tout le rapport tour17 à cibler cette planète.
  - Conclusion : les dégâts préexistants ne proviennent ni d'un autre
    combat ce même tour, ni des deux tours précédents accessibles — ils
    remontent nécessairement à un tour antérieur à `tour15` (hors de la
    fenêtre de données disponible sur ce serveur), pendant lequel un
    combat a porté l'Usine V à `dommages == structure` sans qu'aucun
    combat ultérieur ne se reproduise sur cette planète avant celui de
    Juqav — illustrant concrètement qu'un bâtiment "zombie" peut
    survivre ainsi un nombre de tours arbitrairement long avant d'être
    enfin balayé.
