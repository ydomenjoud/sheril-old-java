# 4. Les flottes

Tout commandant, quel que soit son grade, son espèce ou sa puissance, peut posséder un nombre illimité de flottes composées chacune de un ou plusieurs vaisseaux.

Le coût d'entretien d’une flotte correspond au coût de construction total de l’ensemble de ses vaisseaux divisé par 10. L'entretien des flottes stationnées en orbite de vos propres systèmes est réduit de moitié.

Il n'y a pas de limite stricte au nombre de vaisseaux pouvant être regroupés dans une même flotte, mais dépasser un certain volume expose la flotte à des dégâts de collision à chaque tour.

Cette limite maximale de risques de collisions peut être augmentée grâce à la technologie *Maîtrise militaire*.

## <a id="4.1"></a>4.1 Fusionner des flottes

Il est possible de fusionner un nombre illimité de flottes situées au même emplacement en une ou plusieurs flottes distinctes. Lors d’une fusion, seule la flotte portant le plus petit numéro d'identification est conservée ; l’autre disparaît.

Si vous regroupez plus de deux flottes, toutes sont automatiquement fusionnées avec celle dont le numéro est le plus bas. De même, la flotte résultante conserve la directive de la flotte ayant le numéro le plus bas.

Les fusions de flottes ont lieu avant les attaques (spatiales et planétaires).

## <a id="4.2"></a>4.2 Diviser une flotte

Une flotte doit contenir au minimum un vaisseau. Il n’y a pas de nombre maximal de vaisseaux au sein d'une flotte.

Une flotte peut être scindée en autant de flottes distinctes qu’il y a de vaisseaux. À chaque division, une nouvelle flotte est créée. Une flotte peut être divisée autant de fois que vous le souhaitez au cours d’un même tour (tant qu'il reste au moins un vaisseau dans la flotte originale).

Si certains vaisseaux sont endommagés, ce sont ceux-là qui seront automatiquement transférés vers la nouvelle flotte issue de la division.

En cas de division d’une flotte avec un héro, ce dernier demeure au sein de la flotte initiale.

Les divisions de flottes ont lieu avant les fusions et les déplacements..

## <a id="4.3"></a>4.3 Vitesse de déplacement

Une flotte se déplace d’un nombre de cases (parsecs) égal à la vitesse de son vaisseau le plus lent. Les déplacements s'effectuent dans toutes les directions, y compris en diagonale.

Tout déplacement de flotte nécessite de lui assigner une directive.

Un vaisseau dont le réacteur est détruit a toujours la capacité de se déplacer.

## <a id="4.4"></a>4.4 Directives de rencontre

Une directive de rencontre est une instruction assignée à une flotte (notamment lors de son ordre de déplacement) qui détermine son comportement opérationnel et son engagement au combat sur le parsec où elle termine son tour.

**La directive s’active à l’endroit où la flotte termine son tour**, même si elle n’a pas atteint sa destination finale (par exemple en raison d'une vitesse insuffisante pour s'y rendre).

Les directives sont résolues successivement selon l’ordre présenté dans le tableau suivant. Cet ordre est déterminant, notamment lorsque vous attaquez au même endroit une flotte et un système à l’aide de deux flottes distinctes, et que la flotte ennemie possède la directive *Attaque préventive*.

|                                                                                                                 |                                                                                                                                                                                                                                            |
|-----------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Attaque les flottes d’un commandant particulier. (Le neutre est considéré par le jeu comme étant un commandant) | Attaque toutes les flottes d'un commandant en particulier (le neutre est considéré comme un commandant), et qui sont présentes sur la même case que votre flotte.                                                                          |
| Attaque préventive                                                                                              | Attaque toutes les flottes qui comptent attaquer le système (or ses planètes) que la flotte survole.                                                                                                                                       |
| Attaquer toute flotte rencontrée                                                                                | Votre flotte attaque toute flotte présente sur la même case qu'elle, et uniquement les flottes.                                                                                                                                            |
| Attaque de système                                                                                              | Votre flotte tente de prendre toutes les planètes du système. S’il n’y a plus de milice, la planète vous appartient et la flotte attaque une autre planète si elle le peut.                                                                |
| Pillage de système                                                                                              | Votre flotte tente de piller toutes les planètes du système. Le pillage rapporte autant de centaures que de population détruite. Une planète partiellement pillée ne rapporte rien.                                                        |
| Attaque de planète                                                                                              | Votre flotte tente de prendre une planète du système. S’il n’y a plus de milice, la planète vous appartient.                                                                                                                               |
| Pillage de planète                                                                                              | Votre flotte tente de piller une planète du système. S’il n’y a plus de milice, la planète devient neutre. Le pillage rapporte autant de centaures que de population détruite. Une planète partiellement pillée ne rapporte rien.          |
| Eradication de planète                                                                                          | Votre flotte tente d'éradiquer une planète du système. S’il n’y a plus de milice, la planète devient neutre et n’a plus aucune population. Il faut coloniser à nouveau. L’éradication n’est possible que pour une seule planète à la fois. |
| Attitude neutre                                                                                                 | La flotte ne fait rien, mais se défend en cas d'agression.                                                                                                                                                                                 |

**Il est à noter qu'une flotte conservera sa directive si elle n'a pas participé à un combat, ou si elle a participé à un combat spatial contre une flotte avec une puissance en deçà du cinquième de sa propre puissance.**

*Par exemple, la flotte “Chasse” constituée de 7 Intercepteurs standards (puissance de 7\*(3+½)=24) s'arrêtant en 10-10 with la directive “Attaque de toutes flottes rencontrées” pourra initier un combat spatial avec toutes les flottes - les unes après les autres - constituées d’un seul Intercepteur standard (puissance de 3+½=3), car elle conservera sa directive après chaque combat. Si elle avait affronté une flotte constituée de 2 Intercepteurs standards (puissance de 2\*(3+½)=7), elle n'aurait pas pu engager d'autres flottes, car son adversaire avait une puissance supérieure à 24/5=4. On dit alors que la flotte “Chasse” a été neutralisée.*

## <a id="4.5"></a>4.5 Puissance d'une flotte

La puissance d'une flotte est égale à la somme de la puissance spatiale de tous ses vaisseaux, plus la somme de la puissance planétaire de tous ses vaisseaux divisée par deux (arrondi à l'entier inférieur).

Formule : Puissance = AS + (AP/2)

|     |               |               |
|-----|---------------|---------------|
| 0   | \< 25         | Insignifiante |
| 1   | 25 – 49       | Ridicule      |
| 2   | 50 – 99       | Très petite   |
| 3   | 100 – 199     | Petite        |
| 4   | 200 – 499     | Moyenne       |
| 5   | 500 – 999     | Assez grande  |
| 6   | 1000 – 1999   | Grande        |
| 7   | 2000 – 4999   | Très grande   |
| 8   | 5000 – 9999   | Gigantesque   |
| 9   | 10000 – 17999 | Titanesque    |
| 10  | ≥ 18000       | Inimaginable  |

Remarque : il faut au minimum une flotte avec une puissance de 50 pour pouvoir participer à un combat planétaire.
