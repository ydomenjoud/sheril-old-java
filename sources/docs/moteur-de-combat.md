# Moteur de combat : adressage des cibles et résolution des dommages

Analyse du code tel qu'il est à la révision `1de56a1` (branche `develop`).
Les chemins sont relatifs à `sources/`.

Fichiers concernés :

| Fichier | Rôle |
|---|---|
| `zIgzAg/jeu/oceane/Combat.java` | Orchestration : qui attaque qui, boucle des rounds, ciblage, mouvement, ordre de tir |
| `zIgzAg/jeu/oceane/Vaisseau.java` | Tir d'un vaisseau, jet pour toucher, boucliers, absorbeur, dommages aux composants |
| `zIgzAg/jeu/oceane/Flotte.java` | Listes de cibles par type/taille, répartition strato / air-sol, combativité |
| `zIgzAg/jeu/oceane/Arme.java` | Chance de toucher selon taille et distance, caractéristiques par niveau |
| `zIgzAg/jeu/oceane/ConstructionPlanetaire.java` | Tir des batteries planétaires, dommages aux bâtiments |
| `zIgzAg/jeu/oceane/Position3D.java` | Géométrie du champ de bataille (distance, déplacement, fuite) |
| `zIgzAg/jeu/oceane/StrategieDeCombatSpatial.java` | Placement, priorités de taille, agressivité, type de cible |
| `ListeCaracArmes.java`, `ListeChancesDeToucherArmes.java`, `Const.java` | Données chiffrées |

Le combat est déclenché une fois par tour par `Combat.resolutionCombats()` (`DeroulementDuTour.java:90`).

---

## 1. Adressage au niveau des flottes : qui attaque qui

### 1.1 Regroupement

`Univers.listeFlottesDirectives()` (`Univers.java:854`) construit une table
`Position → (directive complète → liste de [n° commandant, n° flotte])`.
La directive complète vaut `directive + 100 × précision` ; la précision est le n° du joueur visé ou le n° de la planète visée (`Flotte.java:375`).

Chaque case de la carte est résolue indépendamment par `resolutionCombatsSurUneCase` (`Combat.java:98`).

### 1.2 Ordre de traitement des directives

Sur une case, les directives sont traitées dans cet ordre fixe. À l'intérieur d'une même directive, les flottes passent dans un ordre aléatoire (`Utile.ordreAuHasard`).

| Ordre | Directive | Cibles |
|---|---|---|
| 1 | `ATTAQUE_JOUEUR` (8) | Toutes les flottes du commandant désigné, parcourues dans un ordre aléatoire commun à la case |
| 2 | `ATTAQUE_PREVENTIVE` (2) | Les flottes ayant une directive d'attaque, de pillage ou d'éradication de système ou de planète |
| 3 | `ATTAQUE_TOUTE_FLOTTES` (3) | Toutes les flottes présentes, même ordre aléatoire |
| 4 | `ATTAQUE_SYSTEME` (1) | Toutes les planètes attaquables du système, dans un ordre aléatoire |
| 5 | `PILLAGE_SYSTEME` (4) | Idem |
| 6 | `ATTAQUE_PLANETE` (5) | La planète désignée par la précision |
| 7 | `PILLAGE_PLANETE` (6) | Idem |
| 8 | `ERADICATION_PLANETE` (7) | Idem |

Les combats spatiaux précèdent donc toujours les combats planétaires, et une flotte en attaque préventive intercepte les assaillants avant qu'ils ne touchent une planète.

### 1.3 Filtre diplomatique

`peutCombattre` (`Combat.java:77`) interdit le combat contre soi-même, contre un commandant lié par un pacte de non-agression, et contre un commandant partageant au moins une alliance.

### 1.4 Enchaînement des combats d'une même flotte

`attaqueFlotte` (`Combat.java:242`) refuse le combat si l'attaquant n'existe plus ou est repassé en `ATTITUDE_NEUTRE`.

`combatFlotteFlotte` renvoie `true` seulement si l'attaquant a survécu **et** avait une puissance au moins 5 fois supérieure à celle du défenseur au début du combat (`Combat.java:941`, `1079-1086`). Sinon sa directive est remise à neutre : il ne livrera plus d'autre combat ce tour, ni spatial ni planétaire. La même règle s'applique au défenseur. Une flotte écrasante peut donc enchaîner plusieurs cibles, une flotte « à égalité » ne livre qu'un combat.

Après une attaque de système ou de planète, la directive est toujours remise à neutre (`Combat.java:376`, `411`).

---

## 2. Combat spatial (`combatFlotteFlotte`, `Combat.java:892`)

### 2.1 Initialisation

1. **Combativité** de chaque vaisseau : `5 + niveau de moral + moral modifié du héros` (`Vaisseau.java:231`). C'est le nombre de rounds pendant lesquels le vaisseau accepte de se battre.
2. **Positionnement** (`positionnement`, `Combat.java:1271`). Le champ est un espace 3D entier :
   - attaquant : `x ∈ [0, 30[`, `y ∈ [0, 10[` ;
   - défenseur : `x ∈ [0, 30[`, `y ∈ [20, 30[` (bande neutre de 10 entre les deux) ;
   - si la stratégie définit une position pour ce plan de vaisseau, elle est utilisée (symétrisée pour le défenseur : `x → 30 − x`, `y → 20 − y`) ; sinon la position est tirée au hasard dans la zone du camp ;
   - une dispersion est ajoutée (`Position3D.auHasard`) : `± rand(6 − niveau « maîtrise du savoir » du héros)` en x et y, `± rand(10)` en z.
3. **Table des cibles** : un tableau d'entiers par vaisseau (`initialisationCibles`), dont seule la case 0 est utilisée aujourd'hui (voir 2.7).

### 2.2 Déroulement d'un round

```
preparerAuCombat(true)      → liste des armes spatiales valides, reset des dommages infligés
determinationTempo          → initiative de chaque vaisseau
determinationCible (×2)     → chaque vaisseau choisit UNE cible
mouvement                   → déplacements, du plus lent au plus rapide
combat                      → tirs, du plus rapide au plus lent
eliminerPertesVaisseaux     → retrait des épaves
diminuerCombativite         → −1 pour tous les survivants
```

Le combat s'arrête quand une flotte est anéantie, ou quand **aucune** des deux flottes n'a plus de vaisseau combatif (`Combat.java:1027-1034`). Tant qu'un seul camp reste combatif, les rounds continuent.

### 2.3 Initiative (tempo)

`determinationTempo` (`Combat.java:1382`) :

```
tempo = vitesse modifiée du héros × rand(100)
      + Vaisseau.getTempo()
      + rand(500)

Vaisseau.getTempo() = capacité de mouvement × 100
                    + rand(100) × niveau d'expérience
                    + (somme des vitesses des armes valides × 100) / (nb d'armes valides + 1)
```

Les tempos sont rendus uniques (incrément en cas de collision) et triés. Le mouvement se fait par tempo croissant, les tirs par tempo décroissant, les deux flottes étant entrelacées (`mouvement`, `combat`). Les vaisseaux rapides bougent donc en dernier et tirent en premier.

### 2.4 Choix de la cible (`determinationCible`, `Combat.java:1313`)

La cible est rechoisie à chaque round, pour chaque vaisseau :

1. **Filtre par type** : `Flotte.listeNumerosVaisseauxDeType(typeCible de la stratégie)` (`Flotte.java:1154`) renvoie les vaisseaux ennemis non détruits qui sont :
   - `CIBLE_PROCHE` (0) : tous ;
   - `CIBLE_CHASSEUR` (1) : ceux qui ont au moins une arme spatiale valide ;
   - `CIBLE_BOMBARDIER` (2) : ceux qui ont au moins une arme planétaire valide ;
   - `CIBLE_CARGO` (3) : les transporteurs.
2. **Filtre par taille** : la stratégie fournit, par plan de vaisseau tireur, un ordre de préférence des 10 classes de taille (`getCibles`). À défaut, l'ordre va de la plus grande taille (9) à la plus petite (0) (`choixPossibleCible`).
3. **Parcours probabiliste** : les 10 priorités sont parcourues dans l'ordre, mais chacune n'est examinée qu'avec **50 % de chance** (`Univers.getTest(50)`). Le parcours s'arrête à la première classe examinée qui contient au moins un candidat. L'ordre de priorité est donc une tendance, pas une règle stricte : la priorité n° 1 est sautée une fois sur deux.
4. **Repli** : si rien n'est trouvé, le filtre par type est abandonné (toute la flotte ennemie) et le parcours est retenté jusqu'à 5 fois.
5. **Échec** : si les 5 replis échouent, la cible vaut `−1` et le vaisseau ne tire pas ce round.
6. **Départage** : parmi les candidats de la classe retenue, la cible est le vaisseau le plus proche (`Position3D.positionLaPlusProche`), le premier trouvé en cas d'égalité.

La distance est une distance de Tchebychev : `max(|Δx|, |Δy|, |Δz|)` (`Position3D.java:90`).

### 2.5 Mouvement (`mouvementVaisseau`, `Combat.java:1467`)

- Cas normal : le vaisseau se rapproche de sa cible d'au plus `capacité de mouvement` cases **sur chaque axe** (`Position3D.positionAtteinte`). La capacité vaut `propulsion + bonus de taille` (9 pour la taille 0, 0 pour la taille 9 — `Const.TAILLE_VAISSEAUX`), et 0 sans propulsion valide.
- Fuite : le vaisseau s'éloigne vers `y = −1000` (attaquant) ou `y = +1020` (défenseur), à vitesse `mouvement − 2`, si l'une de ces conditions est vraie :
  - le vaisseau n'est plus combatif ;
  - il n'a plus d'arme spatiale valide ;
  - la flotte est en fuite tactique (`fuiteTactique`, `Combat.java:1402`) :

| Agressivité | Fuite tactique si… |
|---|---|
| `FUYARD` (1) | toujours |
| `PRUDENT` (2) | puissance < ½ de l'adversaire |
| `NORMAL` (3) | puissance < ¼ |
| `COMBATIF` (4) | puissance < ⅛ |
| `RAGE` (5) | jamais |
| `PILLAGE` (0) | l'adversaire n'a plus de vaisseau du type ciblé (cargo, bombardier ou chasseur) |

La fuite n'est pas un désengagement : le vaisseau reste dans le combat et reste ciblable, il s'éloigne simplement hors de portée. Un vaisseau en fuite tactique mais encore combatif continue de tirer s'il a une cible à portée.

### 2.6 Tir d'un vaisseau (`tirVaisseau` → `Vaisseau.tir`)

Conditions pour tirer (`Combat.java:1531-1535`) : le tireur est combatif et non détruit, il a une cible (≠ −1), et cette cible n'a pas déjà été détruite plus tôt dans le round. Il n'y a **pas de reciblage** : si la cible est morte, le tir du round est perdu.

`Vaisseau.tir` (`Vaisseau.java:421`) parcourt les armes spatiales valides :

- arme hors de portée (chance = 0) : ignorée pour ce round ;
- sinon l'arme tire une fois et est retirée de la liste jusqu'au round suivant ;
- la salve s'arrête dès que la cible est détruite ; les armes restantes ne sont pas réaffectées.

#### Chance de toucher

Base (`Arme.getChanceDeToucher`, `Arme.java:59`) :

```
si distance ≥ portée : 0
sinon : chanceToucher[taille de la cible] × (100 − 90 × distance / portée) / 100
```

La chance décroît donc linéairement de 100 % de la base (distance 0) à environ 10 % en limite de portée. La base dépend de l'arme et de la taille de la cible (`ListeChancesDeToucherArmes`) :

| Arme | Taille 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | Portée | Dom. bouclier | Dom. coque | Dom. sol |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| laser | 50 | 45 | 35 | 25 | 20 | 15 | 10 | 5 | 3 | 1 | 8 | 2 | 1 | 1 |
| plasma | 20 | 35 | 40 | 45 | 50 | 45 | 35 | 25 | 15 | 5 | 12 | 1 | 1 | 1 |
| torpille | 1 | 2 | 3 | 10 | 15 | 20 | 30 | 40 | 45 | 50 | 20 | 1 | 2 | 2 |
| missile | 30 | 35 | 40 | 40 | 40 | 40 | 40 | 40 | 40 | 40 | 15 | 0 | 1 | 1 |
| bombe | — | — | — | — | — | — | — | — | — | — | 0 | 0 | 0 | 5 |

Valeurs au niveau 0. Par niveau d'arme (`Arme.java:102-146`) : chance `+ niveau × max(base/10, 1)`, portée `+ niveau`, fiabilité `− niveau`, vitesse de +1 à +5, dommages bouclier +1 à +3 à partir du niveau 3, dommages coque +1 à +3 à partir du niveau 4, dommages sol `+ niveau` pour les armes planétaires.

Modificateurs (`reussiteTir`, `Vaisseau.java:453`) :

```
test = base
     + niveau d'expérience du tireur (0 à 4)
     + attaque modifiée du héros attaquant
     + bonus racial de combat spatial (tous à 0 actuellement)
     + (1 + « inspiration fanatique ») si le héros est de la race de l'équipage
     − niveau d'expérience de la cible
     − défense modifiée du héros adverse
     − (1 + « inspiration fanatique ») si le héros adverse est de la race de l'équipage cible
touché si rand(100) < max(1, test)
```

#### Résolution des dommages (`effectuerDommages`, `Vaisseau.java:391`)

1. **Bouclier.** Si la cible possède un composant bouclier valide non saturé (`getNumeroBouclierValide`), le coup est **entièrement absorbé** : le compteur de ce bouclier augmente des `dommages bouclier` de l'arme, et rien ne passe à la coque, quel que soit le débordement. Un bouclier de niveau N encaisse donc `⌈N / dommages bouclier⌉` coups. Les boucliers sont consommés l'un après l'autre.
2. **Absorbeur.** Sans bouclier valide, les `dommages coque` passent par l'absorbeur (`dommagesApresAbsorbe`, `Vaisseau.java:754`). Sa réserve est remise à sa capacité au début de chaque salve d'un vaisseau attaquant (`cible.initialiserAbsorbeur()` en tête de `tir`) : il absorbe les N premiers points infligés par chaque tireur à chaque round.
3. **Coque.** Chaque point restant est appliqué à un composant valide tiré au hasard (`ajouterDommageAuHasard`). Un composant devient inutilisable quand ses points de dommage égalent son nombre de cases. Conséquences immédiates :
   - une arme détruite est retirée des armes valides ;
   - un moteur détruit a 1 % de chance de faire exploser le vaisseau (`CHANCE_EXPLOSION_MOTEUR`) ;
   - puissance, mouvement, boucliers et absorbeur sont recalculés sans les composants détruits.
4. **Destruction.** Le vaisseau est détruit quand il n'a plus aucun composant valide. Ses « points de vie » sont donc la somme des cases de ses composants.

Effets annexes : le tireur gagne du moral et la cible en perd sur chaque coup efficace ; un coup entièrement absorbé par l'absorbeur fait perdre du moral au tireur ; tireur, cible et héros gagnent de l'expérience.

### 2.7 Une seule cible par vaisseau et par round

La boucle prévue pour les tirs sur cibles multiples est désactivée (`Combat.java:1001`) :

```java
for (int i = 1; i <= 0 /* Const.NB_CIBLES[tailleMax] */; i++) {
```

`Const.NB_CIBLES` ne sert plus qu'à dimensionner la table des cibles et à borner le nombre d'armes hors de portée tolérées dans `Vaisseau.tir` (`compteur <= NB_CIBLES[taille]`). En pratique chaque vaisseau concentre toutes ses armes à portée sur une cible unique, et l'excédent de puissance de feu sur une cible détruite est perdu.

### 2.8 Fin du combat

- Flotte anéantie : son héros « meurt » (retour en réserve avec `1 + 20 × immortalité` % de chance) et la flotte est supprimée.
- Les cargaisons perdues par l'adversaire sont récupérées par une flotte équipée d'un rayon tracteur, dans la limite de sa capacité (`Flotte.recupererMarchandises`).
- Au-delà de 200 points de dommages cumulés, un champ de débris est créé sur la case.
- `degatsDernierCombat` (somme des dommages de coque des deux camps) alimente l'intensité du combat dans les statistiques.

---

## 3. Combat planétaire (`combatFlottePlanete`, `Combat.java:414`)

Il n'y a ici ni position, ni distance, ni ciblage individuel : les cibles sont tirées au hasard dans des groupes.

### 3.1 Conditions et forces en présence

- La flotte doit avoir une puissance d'au moins 50 (`PUISSANCE_ATTAQUE_PLANETAIRE_MINIMALE`).
- Nombre maximal de rounds : la combativité moyenne de la flotte.
- **Milices** : `population totale × stabilité / 100`, augmentées de 50 % si la politique du système est `DEFENSE`, puis de 50 % si le système possède un stock important d'armement, sans dépasser la population totale.
- **Bâtiments** : chaque `ConstructionPlanetaire` a des points de structure ; ceux dotés d'une arme sont des batteries, ceux dotés de la capacité bouclier sont des boucliers planétaires.

### 3.2 Répartition de la flotte selon l'agressivité

`Flotte.forceAttaqueStratospherique` et `forceAttaqueAirSol` (`Flotte.java:1181`, `1199`). Un « bombardier » a au moins une arme planétaire valide, un « chasseur » au moins une arme spatiale valide.

| Agressivité | Groupe stratosphérique (bombardement) | Groupe air-sol (assaut) |
|---|---|---|
| `FUYARD`, `PRUDENT` | toute la flotte | aucun |
| `NORMAL`, `PILLAGE` | les bombardiers | les non-bombardiers |
| `COMBATIF` | les non-chasseurs | les chasseurs |
| `RAGE` | aucun | toute la flotte |

### 3.3 Les quatre phases d'un round

`preparerAuCombat(false)` rend toutes les armes disponibles (spatiales et planétaires). Contrairement au combat spatial, les armes ne sont pas consommées : chaque arme éligible tire à chaque phase où son vaisseau intervient.

**Phase 1 — Batteries planétaires** (`tirDefensesPlanetaires`, `Combat.java:793`)
- Groupe visé : le groupe stratosphérique s'il n'est pas vide, sinon le groupe air-sol.
- Chaque bâtiment tente 50 salves (`NOMBRE_SALVE_BATTERIE`) ; seules les batteries tirent réellement.
- Cible de chaque salve : un vaisseau tiré uniformément dans le groupe, épaves comprises (une salve sur une épave est perdue).
- Tir à bout portant : distance 0, donc chance de base pleine selon la taille du vaisseau, avec les modificateurs du gouverneur (attaque, inspiration fanatique) contre ceux du héros (défense).
- Dommages : bouclier du vaisseau d'abord (même règle qu'en 2.6), sinon points de coque répartis au hasard. L'absorbeur n'intervient pas contre les batteries.

**Phase 2 — Bombardement** (`tirAirSol(strato, …, construCible = true)`, `Combat.java:842`)
- Cibles : les boucliers planétaires s'il en existe, sinon tous les bâtiments.
- S'il reste au moins un bâtiment : chaque arme **planétaire** des vaisseaux du groupe choisit un bâtiment au hasard dans cette liste et lui inflige ses `dommages sol` (`Vaisseau.tirSurConstruction`).
- S'il ne reste aucun bâtiment : chaque arme planétaire tue `dommages sol` miliciens (`Vaisseau.tirSurMilices`).
- Chance : `50 + expérience + attaque du héros (+ bonus de race) − expérience du bâtiment − défense du gouverneur − inspiration fanatique du gouverneur`.

**Phase 3 — Milices** (`tirMilicesPlanetaires`, `Combat.java:827`)
- Ne tirent que s'il y a plus de 50 miliciens et que le groupe air-sol n'est pas vide.
- `1 + milices / 100` batteries virtuelles de type `battlaI` (laser niveau 0), 50 salves chacune, sur le groupe air-sol uniquement.
- Tir à distance `portée − 1` : la chance tombe à 22 % de la base, soit 11 % contre une taille 0 et 0 % contre les tailles 8 et 9, que les milices ne peuvent donc pas toucher.

Les bâtiments détruits sont retirés avant la phase 4.

**Phase 4 — Assaut** (`tirAirSol(sol, …, construCible = false)`)
- S'il reste un bouclier planétaire : toutes les armes du groupe air-sol tirent sur les boucliers avec leurs `dommages sol`.
- Sinon : toutes les armes du groupe air-sol (spatiales comprises) tirent sur les milices, `dommages sol` miliciens tués par coup réussi.

Un bouclier planétaire protège donc intégralement les milices tant qu'il tient, et les bombardiers ne touchent les milices que lorsque plus aucun bâtiment n'existe.

### 3.4 Fin et issue

Le combat s'arrête quand les milices tombent à 0, quand la flotte est anéantie, ou au nombre maximal de rounds.

| Issue (milices à 0) | Effet |
|---|---|
| Attaque (système ou planète) | La planète est transférée à l'attaquant ; réputation et relations raciales ajustées |
| Pillage | La planète passe au neutre, sa population est supprimée, l'attaquant reçoit un budget égal aux milices tuées |
| Éradication | La planète passe au neutre, population réinitialisée |

Dans tous les cas la population baisse de `min(milices tuées, population / 10)`. Si le défenseur perd le système, son gouverneur « meurt ».

**Attaque de système** (`resolutionAttaqueSysteme`, `Combat.java:268`) : les planètes sont attaquées l'une après l'autre dans un ordre aléatoire, et le compteur de rounds se poursuit d'une planète à la suivante. La série s'arrête dès que la flotte est détruite ou que le nombre maximal de rounds est atteint ; une planète qui résiste jusqu'au bout interrompt donc la conquête du système.

---

## 4. Anomalies et points d'attention

Classés par impact estimé sur le résultat des combats.

1. **La symétrie du défenseur modifie la stratégie elle-même** (`Combat.java:1290-1295`). `positionnement` écrit dans le tableau renvoyé par `StrategieDeCombatSpatial.getPositionnement`, qui est le tableau stocké, pas une copie. Chaque vaisseau défenseur d'un même plan réapplique la symétrie : le 1er est placé côté défenseur, le 2e revient côté attaquant, le 3e côté défenseur, etc. La stratégie du commandant reste modifiée pour les combats suivants et est sérialisée ainsi. Correctif simple : cloner le tableau avant de le transformer.
2. **Multi-cibles désactivé** (2.7). Les gros vaisseaux ne répartissent plus leur feu : ils n'engagent qu'une cible par round, et le surplus est perdu dès qu'elle est détruite. Si c'est voulu, `NB_CIBLES` et la table des cibles sont du code mort ; à noter que `NB_CIBLES[0] = 0` fait abandonner sa salve à un vaisseau de taille 0 dès qu'il rencontre une arme hors de portée, les armes suivantes n'étant plus examinées.
3. **Pas de reciblage dans le round** (2.6). Combiné à l'ordre d'initiative, plusieurs vaisseaux visant la même cible gaspillent leurs tirs dès qu'elle tombe.
4. **Boucliers jamais rechargés pendant le traitement du tour.** Le tableau `boucliers` est `transient` et n'est créé que s'il est nul (`Vaisseau.java:479`). Un bouclier entamé le reste pour tous les rounds et tous les combats du même tour ; il n'est « rechargé » que parce qu'il n'est pas sauvegardé. Le tableau est aussi dimensionné une seule fois, et l'index utilisé se décale si un composant bouclier est détruit entre-temps.
5. **Une arme à 0 dommage bouclier ne perce jamais un bouclier.** Le coup est absorbé sans entamer le compteur. C'est le cas du missile aux niveaux 0 à 2 : une flotte de missiles bas niveau ne peut rien contre un vaisseau portant un bouclier valide.
6. **Priorités de taille probabilistes et « pas de cible » possible** (2.4). Chaque priorité est sautée une fois sur deux. Face à une flotte d'une seule classe de taille, un vaisseau a environ 1,6 % de chance (0,5⁶) de ne trouver aucune cible pendant un round.
7. **Les salves des batteries visent aussi les épaves** (3.3, phase 1) : la défense perd de l'efficacité à mesure qu'elle détruit des vaisseaux dans le round.
8. **Réputation du pillage** (`Combat.java:583`) : `REPUTATION − memoirePop − max(0, nbPopDefensive)` alors que le budget, deux lignes plus haut, utilise `memoirePop − max(0, nbPopDefensive)`. Il manque vraisemblablement des parenthèses.
9. **Statistiques de dégâts hétérogènes.** Les vaisseaux ne comptent que les dommages de coque réellement infligés ; les batteries comptent aussi les dommages aux boucliers et le dépassement sur une cible détruite (`ConstructionPlanetaire.java:122-128`).

---

## 5. Simulateur

`docs/simulateur-combat.html` (à ouvrir directement dans un navigateur) rejoue le combat spatial décrit en section 2, round par round, avec des flottes composées à la main. Trois règles y sont commutables : le multi-cible (anomalie 2), la fiabilité des armes et la correction de la symétrie du défenseur (anomalie 1).

Écarts connus du simulateur par rapport au moteur Java : les compétences de héros (inspiration fanatique, maîtrise du savoir) et les bonus raciaux ne sont pas modélisés, et la taille d'un vaisseau est déduite de la somme des cases de ses composants.

---

## 6. Collisions après le combat

Deux mécanismes distincts frappent les survivants ; le simulateur les chiffre tous les deux.

**Collisions internes, en fin de tour** (`Commandant.resolutionGestionFlottes`, `Commandant.java:1661`, puis `Flotte.gererCollisionVaisseaux`). Elles ne dépendent pas du combat mais de la taille de la flotte :

```
risque = ⌈nombre de vaisseaux / (45 + 5 × rand(5))⌉ − 2 × niveau de maîtrise militaire
si risque > 0 :
    taille   = risque²
    réserve  = taille × (5 + rand(3)) + rand(10)
    pour chaque vaisseau, dans l'ordre de la flotte :
        dégâts = rand(max(1, taille / (taille du vaisseau + 1)))
        réserve −= dégâts ; arrêt quand la réserve est épuisée
```

Un risque de 1 ne cause jamais de dégât (`rand(1)` vaut 0) : il faut plus de 45 à 65 vaisseaux, et un seul niveau de maîtrise militaire annule un risque de 2. Les petits vaisseaux, placés en tête de flotte, encaissent l'essentiel.

**Correction appliquée après la révision `1de56a1`** : `gererCollisionVaisseaux` ne compte plus que les dommages réellement subis par chaque vaisseau, pour le chiffre du rapport comme pour la réserve. Toute la réserve se transforme donc en pertes : environ un quart de la structure d'une flotte de 500 chasseurs de 5 cases par tour, près de la moitié à 1000. Le simulateur affiche les deux règles côte à côte.

Avant cette correction, le chiffre de dommages renvoyé, celui que le joueur lisait dans son rapport, additionnait les dégâts tirés et non les dégâts réellement subis : un vaisseau de 5 cases qui tirait 800 points était détruit, comptait pour 800 dans le rapport et retirait 800 à la réserve. Sur une grande flotte de petits vaisseaux la réserve était donc épuisée après quelques dizaines de destructions. Exemple simulé : 3000 chasseurs de 5 cases, risque 60, 18 293 dommages annoncés pour 18 vaisseaux détruits et 90 points de structure réellement perdus.

**Collisions avec les débris, au début du tour suivant** (`Commandant.resolutionCollisions`, `DeroulementDuTour.java:70`, puis `Debris.gererCollisions` et `Flotte.gererCollision`). Un combat spatial qui inflige plus de 200 points de dommages laisse sur la case un champ de débris de cette taille, qui s'ajoute aux débris existants et ne se résorbe jamais seul (seul le dragage le réduit). La phase de collision passe avant l'exécution des ordres : une flotte restée sur la case ne peut pas l'éviter. Pour chaque vaisseau :

```
dégâts = rand(max(1, (débris / (rand(500) + 1)) / (11 − taille du vaisseau))) / 10
```

Les gros vaisseaux sont ici les plus exposés. Les composants de détection et de dragage de mines protègent de ces collisions ; ils ne sont pas modélisés dans le simulateur.
