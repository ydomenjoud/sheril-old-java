# Combat : un même bâtiment partagé entre plusieurs planètes apparaît "détruit à 0 dégât" sur celles qui n'ont jamais combattu

- **Fichiers en cause** :
  - `sources/zIgzAg/jeu/oceane/Systeme.java` — `ajouterRichesses`
    (branche `TRANSPORT_BATIMENT`) : **cause racine confirmée**, voir §2.
  - `sources/zIgzAg/jeu/oceane/Combat.java` (seul appelant de
    `Planete.eliminerPertesBatiments()`), `sources/zIgzAg/jeu/oceane/
    Planete.java` (`eliminerPertesBatiments`,
    `listeEquipementsNombresDommages`), `sources/zIgzAg/jeu/oceane/
    RapportCombatData.java` (`fromCombatPlanete`) : mécanisme secondaire
    qui détermine *comment* le défaut se manifeste dans le rapport, voir
    §3.
- **Nature** : bug logique — partage d'une instance Java entre plusieurs
  planètes distinctes (pas un problème de calcul de dégâts). Confirmé par
  deux tests isolés exécutant le vrai code de production (`Systeme`,
  `Planete`, `ConstructionPlanetaire`, `Univers` mocké statiquement pour
  la seule résolution de technologie) — voir §2 et §3.

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
tour (`variationNombre: -1`), mais affiche **0 dégât supplémentaire
encaissé ce tour** (`variationDegats: 0`). La Mine, détruite le même
tour, affiche un delta cohérent (`variationDegats: 40`).

Élément décisif communiqué ensuite par l'utilisateur : **selon ses
informations, ce système n'a subi aucun combat depuis la construction
des bâtiments en question.** Ceci écarte totalement l'hypothèse
initialement documentée ici (un combat antérieur, hors de la fenêtre de
données disponible, aurait endommagé le bâtiment sans le balayer) — si
aucun combat n'a jamais eu lieu, aucun dégât réel ne peut avoir été
infligé par la voie normale. Cette contrainte a permis d'identifier la
véritable cause, ci-dessous.

## 2. Cause racine confirmée : une même instance `ConstructionPlanetaire` partagée entre plusieurs planètes

`Systeme.ajouterRichesses`, branche `TRANSPORT_BATIMENT` (appelée quand
une construction de bâtiment planétaire aboutit sans planète précisée,
cf. `doc/fix/construction-planetaire-espace-insuffisant-silencieux.md`
pour le même paramètre `PLANETE_NON_PRECISE`) :

```java
} else if (ObjetTransporte.typeDeCodeChargement(o.getCode()) == Const.TRANSPORT_BATIMENT) {
    Batiment b = (Batiment) Univers.getTechnologie(o.getCode());
    Planete p = trouverPlaneteSurLaquelleAjouterBatimentDeType(numero, b);
    ...
    int nbAjouter = 0;
    ConstructionPlanetaire batiment = new ConstructionPlanetaire(o.getCode());   // <- UNE SEULE instance créée ici
    while ((nbAjouter < o.getNombreObjets()) && (p != null)) {
        p.ajouterBatiment(batiment);        // <- LA MÊME instance, ajoutée à p qui change à chaque itération
        p = trouverPlaneteSurLaquelleAjouterBatimentDeType(numero, b);
        nbAjouter++;
    }
}
```

Quand `o.getNombreObjets() > 1` (plusieurs exemplaires construits d'un
coup, sans planète précisée), la boucle appelle
`trouverPlaneteSurLaquelleAjouterBatimentDeType` à **chaque itération**
pour répartir les exemplaires — et cette fonction choisit fréquemment
une planète **différente** à chaque appel (équilibrage de charge : elle
privilégie la planète qui a le moins de bâtiments de ce type). Mais
`ConstructionPlanetaire batiment = new ConstructionPlanetaire(o.getCode())`
n'est instancié **qu'une seule fois, avant la boucle** — c'est donc la
**même référence d'objet Java** qui est ajoutée à `p.ajouterBatiment(...)`
sur chacune des planètes choisies, pas une copie.

Fait aggravant : `resolutionConstructions` (`Possession.java`) avait
pourtant construit N objets **distincts** :

```java
ObjetComplexeTransporte objet = new ObjetComplexeTransporte(code);
for (int j = 0; j < nbbis; j++)
    objet.ajouterObjet(new ConstructionPlanetaire(code));   // N instances distinctes, réellement créées
s.ajouterRichesses(com.getNumero(), objet, c[i].getPlanete());
```

Mais `ajouterRichesses` ne les récupère **jamais** individuellement
(`objet.getObjet(i)` n'est appelé nulle part dans cette branche) — seul
`o.getNombreObjets()` est lu, comme simple compteur de boucle. Les N
objets réellement construits sont donc silencieusement jetés, remplacés
par une unique instance fabriquée à la volée et dupliquée **par
référence** sur toutes les planètes choisies.

Conséquence : une fois cette instance partagée détruite au combat sur
**une seule** des planètes qui la référencent, elle apparaît
instantanément détruite sur **toutes les autres** — sans qu'aucun combat
n'ait jamais eu lieu sur elles, expliquant précisément l'observation de
l'utilisateur.

### Vérification effectuée

Test isolé (`ConstructionPartageeEntrePlanetesTest`, non commité sur
cette branche, exécuté puis supprimé), appelant directement le vrai code
de production (`Systeme.ajouterRichesses`, `Planete`,
`ConstructionPlanetaire`), `Univers` mocké statiquement (Mockito) pour
la seule résolution de `Univers.getTechnologie`/`existenceTechnologieBatiment` :

```java
// 3 planètes distinctes, toutes possédées par le joueur 10
Systeme systeme = new Systeme();
systeme.setPlanetes(new Planete[]{p1, p2, p3});

// Équivalent de resolutionConstructions : 3 exemplaires réellement construits
ObjetComplexeTransporte objet = new ObjetComplexeTransporte("usineV");
objet.ajouterObjet(new ConstructionPlanetaire("usineV"));
objet.ajouterObjet(new ConstructionPlanetaire("usineV"));
objet.ajouterObjet(new ConstructionPlanetaire("usineV"));

// Équivalent de mettreEnChantier(..., 3, "usineV", planète non précisée)
systeme.ajouterRichesses(10, objet, Integer.MIN_VALUE);
```

Résultat (`mvn test`, via un `pom.xml` temporaire pointant `sources/`,
supprimé après vérification) :

```
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Confirmé : `p1.getBatiments()[0]`, `p2.getBatiments()[0]` et
`p3.getBatiments()[0]` sont **le même objet** (`assertSame` passe).
Endommager l'exemplaire via `p1` (`ajouterDommages(50)`, atteignant sa
structure) le marque `estDetruit() == true` **also visible depuis p2 et
p3** sans qu'aucun dégât n'y ait jamais été appliqué. Balayer via
`p1.eliminerPertesBatiments()` le retire de `p1` — `p2` et `p3` gardent
encore leur propre référence à l'objet (déjà détruit, en attente de leur
propre balayage) tant qu'aucun combat n'a lieu chez elles.

## 3. Mécanisme secondaire : pourquoi ça se traduit par "0 dégât" plutôt qu'une erreur visible

Une fois l'instance partagée détruite via une planète, les autres
planètes qui la référencent ne signalent rien d'anormal dans l'immédiat
— elles continuent de la compter comme un bâtiment vivant. C'est
seulement leur **propre** futur combat qui la fera disparaître, via le
même mécanisme documenté initialement dans ce rapport :

`ConstructionPlanetaire.ajouterDommages(nb)` :

```java
public void ajouterDommages(int nb) {
    determinerBatiment();
    dommages = Math.min(dommages + nb, batiment.getPointsDeStructure());
    if (dommages >= batiment.getPointsDeStructure())
        detruit = true;
}
```

`detruit = true` ne retire **pas** le bâtiment de `Planete.batiments` —
seul `Planete.eliminerPertesBatiments()` le fait :

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

Et `eliminerPertesBatiments()` **n'est appelée que depuis
`Combat.combatFlottePlanete`** (confirmé par recherche exhaustive,
`grep -rn "eliminerPertesBatiments()" sources/`) — aucun autre site
d'appel, pas de balayage de fin de tour.

`RapportCombatData.fromCombatPlanete` reconstruit le total cumulé de
dégâts par bâtiment sur la base de l'état "avant ce combat" / "après ce
combat" (calcul en lui-même correct) :

```java
int dom  = nT[1] + (dT[0] - nT[0]) * nbCases;   // total cumulé "après ce tour"
int domA = mT[1] + (dT[0] - mT[0]) * nbCases;   // total cumulé "avant ce tour"
bat.degatsEncaisses  = dom;
bat.variationDegats  = dom - domA;
```

Le défaut n'est pas dans cette formule : c'est que **l'état "avant ce
combat" montre déjà** l'objet partagé comme détruit (`dommages ==
structure`, hérité d'un combat qui a eu lieu ailleurs, sur une planète
sœur), donc la variation calculée pour *ce* combat-ci ressort à 0 — il
n'a, lui, rigoureusement rien eu à faire pour "l'achever".

Second effet du même défaut d'agrégation : `Planete.
listeEquipementsNombresDommages()` **somme** les dégâts de tous les
exemplaires d'un même code sans jamais distinguer un exemplaire réel
d'un exemplaire partagé/fantôme :

```java
public Map listeEquipementsNombresDommages() {
    ConstructionPlanetaire[] c = getBatiments();
    HashMap h = new HashMap(c.length);
    for (int i = 0; i < c.length; i++) {
        ... inter[0]++;                          // +1 exemplaire (même si c'est une référence partagée !)
        ... inter[1] += c[i].getDommages();       // + ses dégâts (partagés eux aussi)
    }
    return h;
}
```

C'est cette somme brute, recopiée telle quelle par
`RapportCombatData.fromInitialPlanete` (`bat.degatsEncaisses = mT[1];`,
sans aucun calcul), qui expose au rapport un total incluant des dégâts
qui ne proviennent d'aucun combat propre à cette planète.

## 4. Les missions spéciales (espionnage, sabotage) sont-elles en cause ?

Non, ni l'une ni l'autre — vérifié par lecture de
`Commandant.effectuerMissionSpeciale` :

- **Espionnage** (`MISSION_ESPIONNAGE`) : ajoute uniquement la position à
  la liste des positions espionnées du joueur. **Aucun effet sur les
  bâtiments.**
- **Sabotage** (`MISSION_SABOTAGE`) :
  ```java
  sys.detruireToutBatimentDePlanete(nPlanete);
  ```
  et `Systeme.detruireToutBatimentDePlanete` :
  ```java
  public void detruireToutBatimentDePlanete(int planete) {
      getPlanete(planete).initialiserBatiments();
  }
  ```
  Vide **intégralement et instantanément** la liste des bâtiments de la
  planète visée — sans passer par `ajouterDommages`/`estDetruit`/
  `eliminerPertesBatiments`, et sans jamais toucher une AUTRE planète.
  Ne peut ni produire l'état "partagé et détruit ailleurs" (§2), ni
  laisser un seul type de bâtiment affecté en épargnant les autres sur
  la même planète (un sabotage réussi les emporterait tous en même
  temps).

Ces deux missions sont hors de cause. Elles confirment, par l'exemple,
que la construction sans planète précisée (§2) reste le seul chemin
identifié vers cet état.

## 5. Confirmation multi-planètes : ce n'est pas un cas isolé

Le combat.htm du tour 17 contient les combats de la flotte **Juqav**
contre **6 planètes** du système Hadalis ce même tour (Wiryxi 2, 6, 8,
10, 11, 13) :

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

Sur **5 des 6 planètes**, les **Usines** (types IV et V) sont
systématiquement détruites avec `variationDegats: 0`, jamais les Mines
détruites dans les mêmes combats. Ce motif se lit maintenant sans
ambiguïté à la lumière du §2 : un ordre de construction groupée d'Usines
V (et IV) **sans planète précisée** a très probablement réparti quelques
instances *partagées* sur Wiryxi 2, 6, 8, 10, 11 et 13 (six planètes du
même système, même propriétaire) ; Juqav, en attaquant Wiryxi 2 en
premier, a réellement détruit l'exemplaire partagé (delta cohérent,
5/41) — ce qui l'a fait apparaître instantanément détruit sur les cinq
autres planètes, où le combat de Juqav n'a plus eu qu'à "découvrir" et
balayer une destruction déjà consommée ailleurs (`variationDegats: 0`).
Les Mines, elles, n'ont manifestement pas été construites via un ordre
groupé sans planète précisée (ou l'ont été en quantité telle
qu'aucun partage n'a eu lieu), d'où leur comportement systématiquement
correct.

Ceci répond aussi à la question de l'utilisateur : le système n'a
effectivement subi **aucun combat** avant celui de Juqav — la
destruction "prématurée" vue sur 5 planètes ne vient pas d'un combat
antérieur sur *ces* planètes-là, mais du combat **réel** que Juqav livre
**ce tour-ci sur Wiryxi 2**, dont l'effet se propage instantanément aux
cinq autres via l'instance partagée.

## 6. Correctifs envisageables — non implémentés

**Correctif de la cause racine (§2, prioritaire)** — dans
`Systeme.ajouterRichesses`, créer une instance distincte à chaque
itération au lieu d'une seule réutilisée :

```diff
 int nbAjouter = 0;
-ConstructionPlanetaire batiment = new ConstructionPlanetaire(o.getCode());
 while ((nbAjouter < o.getNombreObjets()) && (p != null)) {
-    p.ajouterBatiment(batiment);
+    p.ajouterBatiment(new ConstructionPlanetaire(o.getCode()));
     p = trouverPlaneteSurLaquelleAjouterBatimentDeType(numero, b);
     nbAjouter++;
 }
```

Un simple déplacement de l'instanciation à l'intérieur de la boucle.
Alternative plus fidèle à l'intention d'origine (réutiliser les objets
réellement construits par `resolutionConstructions` plutôt que d'en
fabriquer un nouveau) : itérer sur `o.getObjet(i)` au lieu de fabriquer
un objet générique — mais cela suppose que tous les éléments de
`ObjetComplexeTransporte` sont bien du même type, à vérifier.

**Correctifs du mécanisme secondaire (§3)**, toujours utiles
indépendamment du §2 (un bâtiment peut légitimement rester "détruit non
balayé" plusieurs tours même sans partage d'instance, cf. l'hypothèse
initiale de ce rapport) :
- **Option A — balayer immédiatement** : appeler
  `eliminerPertesBatiments()` dès qu'un bâtiment devient détruit
  (nécessite de donner à `ConstructionPlanetaire` une référence à sa
  `Planete`, ou d'ajouter un balayage systématique en fin de tour).
- **Option B — corriger l'attribution** : exclure du delta de
  `RapportCombatData.fromCombatPlanete` les bâtiments déjà `estDetruit()`
  dans l'état "avant ce combat", plutôt que de changer le moment du
  balayage.

**Non implémenté à ce stade**, conformément à la demande : bug analysé
et documenté, correctifs proposés, aucun appliqué.

## 7. Portée

- Le défaut du §2 affecte potentiellement **tout ordre de construction
  groupée d'un bâtiment planétaire sans planète précisée** (dès que
  `nombre > 1` et que la répartition choisit plus d'une planète) — pas
  seulement les Usines, ni seulement cette partie. Son impact dépend de
  la fréquence à laquelle les joueurs utilisent ce mode de construction
  pour plusieurs exemplaires à la fois.
- Conséquences au-delà de l'affichage : les planètes "victimes" du
  partage croient posséder un bâtiment qui, en réalité, n'est qu'un
  reflet d'un bâtiment détruit ailleurs — tant qu'aucun combat ne les
  balaie, ces planètes bénéficient à tort des effets du bâtiment
  (bonus de production, points de construction, défense si c'est un
  bâtiment armé...) alors qu'il n'existe plus réellement de leur point
  de vue économique propre.
- Le mécanisme secondaire (§3) reste un défaut à part entière, qui peut
  se manifester même sans partage d'instance (bâtiment authentiquement
  détruit sur sa propre planète, laissé "zombie" faute de combat suivant
  immédiat) — les deux correctifs (§6) sont complémentaires, pas
  redondants.
