# 3. Constructions

Une construction désigne l'opération industrielle de conversion de ressources en un actif matériel, qu'il soit implanté au sol (construction planétaire) ou déployé en orbite (flotte de vaisseaux).

Une construction nécessite :

- Des points de construction

- Du minerai

- Des centaures

- Des marchandises éventuelles

- Un chantier naval (pour les constructions spatiale uniquement)

Si une construction ne peut être achevée en un seul tour, elle est consignée dans le rapport du système, au sein du tableau des constructions en cours. Les points de construction du système lui sont alors automatiquement alloués au tour suivant jusqu’à son achèvement.

Chaque planète rapporte un point de construction à son système. Certains bâtiments et marchandises permettent d’augmenter ce total.

Le minerai et les marchandises peuvent être consommés le tour même de leur production.

La distribution des points de construction se fait à raison d’un point à la fois.

Par exemple, si vous souhaitez construire 1 mine (1 point de construction), 1 chantier naval (10 points de construction) et 1 bouclier planétaire de type I (10 points de construction) sur un système de 19 planètes, les points de construction seront attribués ainsi :

- 1 point de construction pour la mine (sera construite)

- 1 point de construction pour le chantier naval

- 1 point de construction pour le bouclier planétaire de type I

- 1 point de construction pour le chantier naval

- 1 point de construction pour le bouclier planétaire de type I

- …et ainsi de suite jusqu'à l'attribution des 19 points de construction du système

Au final, il manquera 1 point de construction pour le chantier naval et 1 point de construction pour le bouclier planétaire de type I. Aucun de ces deux bâtiments ne sera construit.

## <a id="3.1"></a>3.1 Production minière

La production de minerai d’une planète est calculée selon deux critères : le nombre de mines construites et sa valeur de production minière.

1 mine produit une quantité de minerai égale à la valeur de production.

2 mines produisent la valeur de production, plus cette même valeur diminuée de 1.

3 mines produisent la valeur de production, plus cette valeur diminuée de 1, plus cette valeur diminuée de 2.

Et ainsi de suite selon cette suite dégressive.

*Exemple : *

*La planète Eden 1 possède une valeur de production minière de 3.*

*Sans mine : elle ne produit aucun minerai.*

*Avec 1 mine : elle produit 3 minerais.*

*Avec 2 mines : elle produit 3 + (3 - 1) = 5 minerais.*

*Avec 3 mines : elle produit 3 + (3 - 1) + (3 - 2) = 6 minerais.*

*Avec 4 mines et plus : elle ne produira que 6 minerais, rendant les mines supplémentaires inutiles*

## <a id="3.2"></a>3.2 Marchandises

Les marchandises sont des ressources économiques produites à l'échelle des systèmes. Elles interviennent principalement dans les coûts de construction pour des bâtiments ou des vaisseaux, et peuvent conférer divers modificateurs passifs au système qui en détient un stock suffisant.

Les différentes marchandises sont :

- Armement et explosifs

- Articles de luxe

- Composants électroniques

- Déchets

- Holofilms et hololivres

- Logiciels

- Matériel agricole

- Médicaments

- Métaux précieux

- Pièces industrielles

- Produits alimentaires

- Robots

- Systèmes de guidage

- Unités énergétiques

- Lixiam

- Oxole

- Tixium

La production de marchandises de chaque système est présentée dans un tableau intitulé "Poste commercial". Les marchandises sont produites naturellement et/ou grâce à des bâtiments, à l'exception du Lixiam, de l’Oxole, du Tixium et des Déchets, qui sont issus exclusivement d'une production naturelle (entre autres).

Les marchandises sont requises pour certaines constructions.

**Un stock minimal de 100 unités d'une marchandise confère des bonus ou des malus**, à l'exception du Lixiam, Oxole, Tixium et des Systèmes de guidage.

|                          |                                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------------|
| **Marchandises**         | **Bonus ou malus pour le système**                                                                           |
| Armement et explosifs    | -1% de stabilité et +50% de miliciens (sans toutefois dépasser la population)                                |
| Articles de luxe         | +10% sur les revenus.                                                                                        |
| Composants électroniques | \+ 25% de budgets services spéciaux et contre-espionnage. (Chaque centaure attribué au budget en vaut 1,25.) |
| Déchets                  | \- 1% de stabilité                                                                                           |
| Holofilms et hololivres  | \+ 1% de stabilité.                                                                                          |
| Logiciels                | \+ 25% en recherche technologique.                                                                           |
| Matériel agricole        | \+ 1 produit alimentaire par planète habitée.                                                                |
| Médicaments              | \+ 10% sur le taux d'augmentation de la population.                                                          |
| Métaux précieux          | +5% sur les revenus.                                                                                         |
| Pièces industrielles     | \- 10% de frais d'entretien des bâtiments planétaires du système.                                            |
| Produits alimentaires    | \+ 5% sur le taux d'augmentation de la population.                                                           |
| Robots                   | \+ 5 points de construction.                                                                                 |
| Systèmes de Guidage      | Ni bonus ni malus                                                                                            |
| Unités énergétiques      | \- 50% de frais d'entretien pour les flottes au-dessus du système.                                           |
| Lixiam, Oxole et Tixium  | Ni bonus ni malus                                                                                            |

## <a id="3.3"></a>3.3 Transferts inter systèmes

Un transfert inter-systèmes est un déplacement logistique de marchandises ou de bâtiments d'une planète à une autre, qu'il s'agisse de planètes situées dans des systèmes différents ou au sein d'un même système

Chaque commandant dispose à chaque tour d'un quota de transferts défini par le nombre de systèmes sous son contrôle. Ce total est indiqué en tête de son rapport. Chaque opération permet d'acheminer un maximum de 9999 unités.

Les points de structure d'un bâtiment n'influent pas sur cette limite : il est ainsi possible de transférer 9999 bâtiments d'un même type, quels que soient leurs points de structure respectifs.

Les marchandises à déplacer doivent obligatoirement être déjà présentes dans les stocks de la planète d’origine.

Il est impossible de transférer une marchandise produite le tour même de son déplacement. En revanche, les transferts s'effectuant avant la phase de construction, une marchandise acheminée peut être consommée au cours de ce même tour.

 

Formule du nombre de transferts maximum : nombre de systèmes possédés (à + de 75% de planètes) + 2x niveau de Maîtrise des étoiles

## <a id="3.4"></a>3.4 Constructions planétaires

Les constructions planétaires désignent l'ensemble des bâtiments et infrastructures fixes construits à la surface d'une planète.

Vous pouvez ordonner la construction d’un bâtiment au sein d’un système. Si vous ne précisez pas la planète ciblée, le bâtiment est érigé sur l’une d’elles, choisie aléatoirement.

Vous pouvez mettre en chantier simultanément plusieurs constructions différentes sur un même système, à condition que la planète concernée soit sous votre contrôle. Il est également possible d'étaler une construction sur plusieurs tours si vous manquez de points de construction, de minerai ou de marchandises.

## <a id="3.5"></a>3.5 Constructions spatiales

Une construction spatiale désigne la mise en production et l'assemblage d'un vaisseau spatial.

Il s'agit de la conversion de ressources (points de construction, minerai, centaures et marchandises) en puissance militaire, logistique ou démographique projetable sur la carte galactique sous forme de flotte.

Les principales caractéristiques d’un vaisseau sont :

- Sa taille : elle va de 1 à 10.

- Sa vitesse : Il s’agit du nombre de cases qu’elle peut parcourir au maximum en un tour.

- Ses points de construction nécessaires (= le nombre de cases occupées divisé par 2 arrondi à l'entier inférieur).

- Son coût en centaures, en minerai et marchandises. Ces coûts sont débités le tour où la construction se termine.

- Sa capacité d’attaque spatiale et planétaire.

- Ses composants.

La taille d’un vaisseau dépend du nombre de ses composants. Chaque composant occupe un certain espace, mesuré en « cases ». La taille de votre vaisseau correspond donc au nombre total de cases occupées.

|     |      |         |     |
|-----|------|---------|-----|
| 1   | 1    | 3       | 9   |
| 2   | 4    | 9       | 8   |
| 3   | 10   | 17      | 7   |
| 4   | 18   | 33      | 6   |
| 5   | 34   | 65      | 5   |
| 6   | 66   | 129     | 4   |
| 7   | 130  | 257     | 3   |
| 8   | 258  | 512     | 2   |
| 9   | 513  | 1025    | 1   |
| 10  | 1026 | 1000000 | 0   |

La vitesse d'un vaisseau dépend de sa taille globale ainsi que du type de réacteur installé.

Un réacteur de type I octroie une vitesse de 1, un réacteur de type II une vitesse de 2, et ainsi de suite. Il est possible de concevoir un vaisseau dépourvu de réacteur ; sa vitesse est alors nulle.

Chaque composant requiert un coût en Centaures, en minerai et en marchandises.

Le coût de construction d'un vaisseau correspond à la somme des coûts en Centaures (auxquels sont appliqués des royalties, le cas échéant), en minerais et en marchandises de chacun de ses composants.

La technologie *Maîtrise de l’espace* vous permet de créer de nouveaux plans de vaisseaux. La conception d’un plan s'effectue en un seul tour et nécessite un coût en Centaures équivalant à 10 fois le prix en Centaures du vaisseau.

Il n’est pas possible d'équiper un vaisseau de plus de 10 boucliers ou de plus d'un module de construction.

Lors de la conception de plan de vaisseau, vous aurez à prévoir le nom et la marque du vaisseau.

Le plan du vaisseau peut être public. Dans ce cas, il est visible et utilisable par tous les commandants sans distinction.

Le plan du vaisseau peut être privé. Dans ce cas, il n'est visible et utilisable que par vous et par ceux à qui vous le transmettez.

Le plan du vaisseau peut être versé dans le domaine de l'espèce du commandant. Dans ce cas, il n'est visible et utilisable que par les commandants de la même espèce.

Le plan du vaisseau peut être uniquement réservé pour les membres d’une alliance du concepteur. Dans ce cas, il n'est visible et utilisable que par les membres de l’alliance.

Vous aurez également la possibilité d’appliquer des royalties sur vos conceptions. Il s’agit d’un pourcentage du prix du vaisseau reversé à son concepteur lors de chaque construction effectuée par un autre commandant.

## <a id="3.6"></a>3.6 Entretien des systèmes

Les systèmes ont un coût d’entretien proportionnel à l’ensemble de leurs bâtiments.

Le coût d’entretien d’un système correspond à la somme des coûts de ses bâtiments divisée par 10.
