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
    `tirAirSol`, dans la boucle de `Combat.combatFlottePlanete`. Le
    combat flotte-planète reste donc le seul moyen d'endommager
    *progressivement* un bâtiment planétaire (voir §6 pour la piste
    "missions spéciales", qui suit un chemin différent et est écartée
    pour une autre raison).
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

## 6. Confirmation multi-planètes : ce n'est pas un cas isolé

Le combat.htm du tour 17 contient les combats de la flotte **Juqav**
contre **6 planètes** du système Hadalis ce même tour (Wiryxi 2, 6, 8,
10, 11, 13). En comparant, pour chaque planète, les bâtiments détruits
et leur `variationDegats` :

| Planète | Bâtiment détruit | `degatsEncaisses` | `variationDegats` |
|---|---|---|---|
| Wiryxi 2  | Usine IV        | 50  | **5** (cohérent) |
| Wiryxi 2  | Usine II        | 50  | **41** (cohérent) |
| Wiryxi 2  | Mine            | 120 | **120** (cohérent) |
| Wiryxi 6  | Usine IV        | 50  | **0** ⚠️ |
| Wiryxi 6  | Usine V ×2      | 100 | **0** ⚠️ |
| Wiryxi 6  | Mine            | 100 | **60** (cohérent) |
| Wiryxi 8  | Usine V ×2      | 100 | **0** ⚠️ |
| Wiryxi 8  | Mine            | 100 | **60** (cohérent) |
| Wiryxi 10 | Usine V         | 50  | **0** ⚠️ |
| Wiryxi 10 | Mine            | 80  | **40** (cohérent) |
| Wiryxi 11 | Usine V         | 50  | **0** ⚠️ |
| Wiryxi 11 | Mine            | 140 | **100** (cohérent) |
| Wiryxi 13 | Usine V         | 50  | **0** ⚠️ |
| Wiryxi 13 | Mine            | 80  | **20** (cohérent) |

Sur **5 des 6 planètes** attaquées par la même flotte ce même tour, les
**Usines** (types IV et V) sont systématiquement détruites avec
`variationDegats: 0`, alors que les **Mines détruites dans les mêmes
combats** affichent systématiquement un delta cohérent et non nul. Seule
Wiryxi 2 (première planète attaquée dans l'ordre du rapport) montre des
Usines avec un delta correct.

Ce n'est donc pas une anomalie isolée sur une seule planète : le même
mécanisme (§2) s'est très probablement produit **à l'échelle de tout un
système** avant `tour15` — un bombardement antérieur, hors de la fenêtre
de données disponible, a laissé les Usines de plusieurs planètes à
`dommages == structure` sans qu'aucun combat ultérieur ne vienne les
balayer, jusqu'à ce que Juqav attaque ce tour-ci, planète par planète.
La récurrence du motif sur un type de bâtiment précis (Usine) et jamais
sur un autre (Mine) dans les mêmes combats est cohérente avec un
bombardement qui aurait ciblé les Usines en priorité (bâtiment à forte
valeur économique) sur l'ensemble du système, laissant les Mines
relativement épargnées à ce moment-là.

## 7. Les missions spéciales (espionnage, sabotage) peuvent-elles produire ces dégâts ?

Non, ni l'une ni l'autre, mais pour des raisons différentes.

`Commandant.effectuerMissionSpeciale` (`services_speciaux`) gère 4 types
de mission (`Const.MISSION_ESPIONNAGE`, `MISSION_SABOTAGE`,
`MISSION_VOL_TECHNOLOGIE`, `MISSION_PROPAGANDE`) :

- **Espionnage** (`MISSION_ESPIONNAGE`) : ajoute uniquement la position à
  la liste des positions espionnées du joueur
  (`ajouterPositionEspionnee`) pour lui donner accès au rapport détaillé
  du système visé. **Aucun effet sur les bâtiments.**
- **Sabotage** (`MISSION_SABOTAGE`) :
  ```java
  if (typeMission == Const.MISSION_SABOTAGE) {
      sys.detruireToutBatimentDePlanete(nPlanete);
      ...
  }
  ```
  et `Systeme.detruireToutBatimentDePlanete` :
  ```java
  public void detruireToutBatimentDePlanete(int planete) {
      getPlanete(planete).initialiserBatiments();
  }
  ```
  Le sabotage **vide intégralement et instantanément** la liste des
  bâtiments de la planète visée (`batiments = new ArrayList(0)`) — tous
  les bâtiments disparaissent d'un coup, sans passer par
  `ajouterDommages`/`estDetruit`/`eliminerPertesBatiments`. Il ne peut
  donc **ni** produire un bâtiment à `dommages == structure` non balayé
  (il n'y a pas d'étape intermédiaire : c'est un retrait total, immédiat,
  propre) **ni**, a fortiori, expliquer qu'un seul type de bâtiment
  (Usine) soit touché en laissant les autres (Mine) intacts sur la même
  planète — un sabotage réussi les emporterait tous en même temps.

Ces deux missions sont donc hors de cause. Elles confirment par
l'exemple que `ConstructionPlanetaire.ajouterDommages` reste bien le
*seul* chemin vers un état "endommagé mais pas retiré" — le sabotage,
qui est le mécanisme le plus proche en intention ("détruire des
bâtiments hors combat"), emprunte délibérément un chemin totalement
différent et ne laisse aucun état intermédiaire.

## 8. Comment `degatsEncaisses` est calculé à l'initialisation

"À l'initialisation" signifie ici : la valeur affichée pour l'état
"avant le premier tour" d'un combat (utilisée par
`RapportCombatData.fromInitialPlanete`, la factory de l'état de départ) :

```java
// 3. Bâtiments au départ (champs issus de mm)
for (Object key : mm.keySet()) {
    String codeBatiment = (String) key;
    Batiment b = (Batiment) Univers.getTechnologie(codeBatiment);
    int[] mT = (int[]) mm.get(codeBatiment);

    EntiteCombatData bat = new EntiteCombatData();
    bat.nom = Utile.maj(b.getNomComplet(c1.getLocale()));
    bat.nombre = mT[0];
    bat.degatsEncaisses = mT[1];   // <- valeur directement recopiée, aucun calcul

    data.batiments.add(bat);
}
```

`degatsEncaisses` à l'initialisation n'est **pas calculé** — c'est une
simple recopie de `mT[1]`, lui-même produit par
`Planete.listeEquipementsNombresDommages()` :

```java
public Map listeEquipementsNombresDommages() {
    ConstructionPlanetaire[] c = getBatiments();
    HashMap h = new HashMap(c.length);
    for (int i = 0; i < c.length; i++) {
        Object o = h.get(c[i].getCode());
        if (o == null) {
            int[] inter = {1, c[i].getDommages()};
            h.put(c[i].getCode(), inter);
        } else {
            int[] inter = (int[]) o;
            inter[0]++;                          // +1 exemplaire
            inter[1] += c[i].getDommages();       // + ses dégâts individuels
        }
    }
    return h;
}
```

Autrement dit : `degatsEncaisses` à l'initialisation = **la somme brute
de `ConstructionPlanetaire.getDommages()` sur tous les exemplaires
actuellement présents dans `Planete.getBatiments()` pour ce type de
bâtiment**, prise telle quelle au moment de l'appel — sans aucune
distinction entre :
- des dégâts réellement infligés récemment (même tour, tour précédent...) ;
- des dégâts anciens, accumulés puis jamais réparés ;
- et, précisément le cas de ce rapport, des dégâts d'un bâtiment déjà
  `estDetruit() == true` mais jamais balayé (§2), dont les dégâts
  "au maximum" sont comptés exactement comme ceux d'un bâtiment
  légitimement encore actif.

C'est cette absence de distinction, à la source même de l'agrégat, qui
permet à un bâtiment zombie de se fondre invisiblement dans le total
"initial" d'un combat qui n'a, lui, rigoureusement rien à voir avec son
état — expliquant à la fois le cas de Wiryxi 10 et sa récurrence sur les
autres planètes du §6.
