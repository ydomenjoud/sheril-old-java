# 2. Population

La population représente le nombre d'habitants établis sur chaque planète d'un système.

## <a id="2.1"></a>2.1 Habitabilité des planètes

Dans Sheril, une seule et unique espèce peut être présente par planète. Par défaut, il s’agit de la population optimale, mais il est possible de la changer au moyen de vaisseaux colonisateurs.

Chaque espèce a une tolérance minimum et maximum aux caractéristiques d’une planète (radiation, température, gravité).

|           |     |     |      |     |     |      |
|-----------|-----|-----|------|-----|-----|------|
| Fremens   | 40  | 200 | 0    | 200 | 0.0 | 8.0  |
| Atalantes | 0   | 120 | 50   | 180 | 4.0 | 10.0 |
| Zwaïas    | 10  | 165 | -150 | 70  | 2.0 | 8.0  |
| Yorksors  | 50  | 150 | -100 | 160 | 3.0 | 9.0  |
| Fergoks   | 50  | 200 | -140 | 190 | 0.0 | 8.0  |

La population maximum d'une espèce résulte du rapport entre ces caractéristiques et celles de la planète, ainsi que de sa taille et de son niveau de terraformation.

Par ailleurs, le type et l’atmosphère d'une planète déterminent le taux d'augmentation de la population par tour.

**Formule pour calculer la population maximale**

Si une ou plusieurs des trois caractéristiques (radiation, température ou gravité) est en dehors des intervalles de tolérance, la population maximale is nulle. Sinon elle résulte de la formule suivante :

R = 1 - \[ 1000 x ( RadiationPlanète - ( -2 x NiveauTerraformation+ RaceRadiationMin ) )x ( RadiationPlanète - ( 2 x NiveauTerraformation+ RaceRadiationMax) ) \] / \[ (4 x NiveauTerraformation ²) + ( RaceRadiationMin-RaceRadiationMax) ² \] T = 1 - \[ 1000 x ( TempératurePlanète - (-2 x NiveauTerraformation+ RaceTempératureMin) )x (TempératurePlanète - ( 2 x NiveauTerraformation+ RaceTempératureMax) ) \] / \[ (4 x NiveauTerraformation ²) + ( RaceTempératureMin-RaceTempératureMax) ²\] G = 1 - \[ 1000 x ( GravitéPlanète - RaceGravitéMin ) x (GravitéPlanète - RaceGravitéMax) \] / \[ ( RaceGravitéMin-RaceGravitéMax) ²\] PopMax = Taille x (R + T + G) + ModificateurRaceAtmosphère x Taille x 100

### <a id="2.1.1"></a>2.1.1 Terraformation

La terraformation est une opération d'ingénierie planétaire qui augmente de +1 le niveau de terraformation d'une planète (toutes les planètes débutent au niveau 0).

- Mécanique : Chaque niveau de terraformation élargit la tolérance biologique des espèces de 2 unités : il diminue de 2 le seuil minimum et augmente de 2 le seuil maximum pour les radiations et la température. En revanche, elle n'a aucun effet sur la gravité.

- Application : Elle peut être ordonnée pour tout un système ou planète par planète avec la technologie *Maîtrise de la gestion*, à raison d'une seule terraformation par planète et par tour.



Objectifs de la terraformation :

1.  **Rendre des mondes viables :** Permet d'abaisser les contraintes environnementales pour rendre colonisables certaines planètes dont les niveaux de radiation ou de température excluaient initialement l'installation d’une espèce (les seuils hors tolérance rendant la population maximale nulle).

2.  **Augmenter la population maximale :** En rapprochant les conditions de la planète des conditions optimales de l'espèce, elle augmente directement le plafond d'habitants admissibles.

3.  **Accélérer la croissance démographique :** Elle optimise le taux d'augmentation de la population par tour sur la planète concernée.

4.  **Développer l'économie globale :** Une population maximale plus élevée combinée à une croissance accrue maximise mécaniquement la base taxable en Centaures et le réservoir de miliciens mobilisables en cas de défense planétaire.

*Exemple : La planète Désert Froid a un niveau de terraformation de 2, une température moyenne de -83°C, une radiation de 20 mR et une gravité de 2 g. Pour les Fremens, les radiations (minimum de base : 40 mR) et la température (minimum de base : 0°C) posaient initialement problème. Même avec le niveau de terraformation de 2 (qui abaisse le seuil de tolérance à 36 mR pour les radiations et à -4°C pour la température), la planète reste hors de leurs limites physiologiques. Les Fremens ne peuvent donc pas s'y établir (la population maximale reste nulle). *

Chaque terraformation de planète a un coût selon la formule : 50c + (2 x niv supérieur)

*Exemples : *

*Terraformer du niveau 0 à 1 = 50 + (2 x 1) = 52c sur une planète*

*Terraformer du niveau 4 à 5 = 50 + (2 x 5) = 60c sur une planète*

### <a id="2.1.2"></a>2.1.2 Colonisation

La colonisation est l'action d'implanter ou de remplacer une population d'une espèce donnée sur une planète.

Dès le départ, chaque planète est déjà peuplée par l'espèce la plus adaptée. Il reste toutefois possible de modifier cette situation par la colonisation, que ce soit par choix de rôle play ou parce que des terraformations ont rendu la planète plus propice à une autre espèce.

Pour coloniser une planète, vous devez utiliser un vaisseau équipé d’un robot colonisateur.

L'ordre de coloniser une planète du système survolé par une flotte s'effectue depuis le formulaire d'ordres. La flotte doit déjà être en position en début de tour.

Une fois déployé, le vaisseau contenant le robot colonisateur est détruit. Le vaisseau colonise la planète au profit de l'espèce composant son équipage. **Si la planète est déjà colonisée par cette même espèce, l'action n'a aucun effet, mais le colonisateur est tout de même détruit.**

Dans le cas où une population différente de celle de l’équipage est déjà présente, elle est violemment exterminée et remplacée. La stabilité de la planète diminue alors de 10 points et votre réputation de 300 points.

En présence de plusieurs colonisateurs au sein d’une même flotte, l'un d'eux est sélectionné au hasard.

Une colonisation peut également survenir de manière automatique lorsqu'une planète atteint son seuil maximal de population pour une espèce donnée.
