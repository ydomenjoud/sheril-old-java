# 1. La galaxie, les systèmes et les planètes

## 1.1. La galaxie

Shéril est le nom de la galaxie. La galaxie est composée d’un ensemble de systèmes stellaires eux-mêmes composés de planètes habitables. La galaxie est découpée en 4 secteurs. Il y a 40 systèmes par secteur. Chaque système compte 10 à 20 planètes.

La galaxie est un quadrillage de 40x40 cases. Les bords opposés sont contigus. On passe donc de la case 40-40 à la case 40-1 comme si elles étaient adjacentes. Une case ne peut contenir qu’un seul système.

![](./img/Quadrillage%20galactique.PNG)

## 1.2. Les systèmes

### 1.2.1. Généralités

Un système possède :

* Un nom
* Des coordonnées
* Un type d’étoile
* Un nombre de planètes

Pour renommer un système, le commandant doit posséder toutes les planètes du système.

Les coordonnées du système de 1 à 40. 1 étant l’axe vertical et 40 l’axe horizontal.

_Exemple : Le système ayant pour nom Terre d'asile a pour coordonnées 1-33 : 1 selon l'axe vertical et 33 selon l'axe horizontal._

Chaque système possède une étoile, autour de laquelle tourne ses planètes. On distingue 6 étoiles différentes :

* Etoile bleue
* Nova
* Etoile blanche
* Naine orange
* Naine bleue
* Naine rouge

Les étoiles bleues possèdent en moyenne plus de planètes que les novas, qui en possèdent plus que les étoiles blanches et ainsi de suite.
Le nombre de planètes de chaque système est compris entre 10 et 20. Un système peut être partagé entre autant de commandants qu'il a de planètes.

_Exemple : Le système ayant pour nom Terre d'asile possède 7 planètes. Le commandant X en possède deux, le commandant Y une, le commandant Z trois. La planète restante est neutre. Ce système est donc partagé entre 4 propriétaires._

### 1.2.2. Les bonus liés à la population majoritaire

La population du système est la somme des populations de ses planètes.

La race qui a le plus de population est la race majoritaire. Conséquences :

* L’équipage des vaisseaux construit sur le système sera de la race majoritaire.
* Les bonus correspondants s’appliquent **(ce qui n’est pas le cas à ce jour**)
* Les colonisateurs produits pourront coloniser une planète avec cette race.

**Exemple**
![](./img/tableau%20population.PNG)

Dans cet exemple, la race majoritaire est Fergok.

|     |**Impact sur l’évolution de la population** | **Bonus technologique*** | **Combat spatial**** |**Combat planétaire****|
|--|:--:|:--:|:--:|:--:|
| Fremens | 0% | -5% | 0%| 5% |
| Atalantes| 10% | 0% | 0% | 0% |
| Zwaïas| 10% | 0% | 5% | 0% |
| Yorksors| -5% | 20% | 5% | -5% |
| Fergoks| 10% | -10% | 10% | 10% |
| Cyborgs| 0% | -5% | 5% | 5% |

*Le modificateur appliqué est celui de la population majoritaire sur le système. Par exemple un système avec une population majoritairement Yorksorienne bénéficiera d’un bonus de 20% sur ses revenus engagés dans la recherche technologie.


** Les modificateurs appliqués sont ceux de l’équipage du vaisseau.
