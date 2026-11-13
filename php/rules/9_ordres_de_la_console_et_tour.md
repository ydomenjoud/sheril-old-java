# Déroulement d'un tour — guide joueur

Ce document liste, phase par phase et dans l'ordre exact où elles se
produisent, toutes les étapes d'un tour. Pour chaque ordre que vous pouvez
donner (avec le nom que vous voyez dans le jeu), il indique **à quel moment
il est réellement pris en compte** : soit tout de suite pendant la phase
« Console de commandes », soit plus tard dans le même tour, pendant la phase
automatique qui lui correspond.

Règle générale : **rien n'est jamais reporté au tour suivant**. Un ordre
« différé » se réalise toujours avant la fin du tour où vous l'avez donné,
simplement un peu plus tard que les ordres à effet immédiat.

## <a id="9.1"></a>1. Gestion des collisions

Les débris et dégâts laissés sur les positions de vos flottes depuis le tour
précédent sont réglés.

## <a id="9.2"></a>2. Console de commandes

Tous les ordres de tous les joueurs sont enregistrés ici, dans l'ordre où ils apparaissent dans le formulaire. 
Ils sont soit exécutés immédiatement, soit pris en compte pour une exécution plus tard dans le tour. 
Le détail pour chaque ordre est indiqué dans le tableau ci-dessous

| Enchaînement des Ordres des joueurs                                                   | Pris en compte                                           |
|---------------------------------------------------------------------------------------|----------------------------------------------------------|
| Adhérer à une [alliance](7_relations_entre_les_commandants#7.2)                       | Immédiatement                                            |
| Valider l'adhésion à une [alliance](7_relations_entre_les_commandants#7.2)            | Immédiatement                                            |
| Voter pour élire le dirigeant d'une [alliance](7_relations_entre_les_commandants#7.2) | Immédiatement                                            |
| Voter pour exclure un membre d'une [alliance](7_relations_entre_les_commandants#7.2)  | Immédiatement                                            |
| Quitter une [alliance](7_relations_entre_les_commandants#7.2)                         | Immédiatement                                            |
| Signer un [pacte](7_relations_entre_les_commandants#7.1)                              | Immédiatement                                            |
| Rompre un [pacte](7_relations_entre_les_commandants#7.1)                              | Immédiatement                                            |
| Affecter un [héros](8_lieutenants#8.1)                                                | Immédiatement                                            |
| Affecter un [gouverneur](8_lieutenants#8.1)                                           | Immédiatement                                            |
| Licencier un [lieutenant](8_lieutenants#8.1)                                          | Immédiatement                                            |
| Enroler un [lieutenant](8_lieutenants#8.1)                                            | Immédiatement                                            |
| Changer la [capitale](1_galaxie_systemes_planetes#1.2)                                | Immédiatement                                            |
| [Orienter les recherches](6_recherches_technologiques#6.2)                            | Plus tard, phase [**Gestion Domaine**](#9.7)             |
| [Services spéciaux](7_relations_entre_les_commandants#7.3)                            | Immédiatement                                            |
| Annuler [construction](3_constructions)                                               | Immédiatement                                            |
| [Construction](3_constructions)                                                       | Plus tard, phase [**Gestion des systèmes**](#9.5)        |
| Programmer des [constructions](3_constructions)                                       | Plus tard, phase [**Gestion des systèmes**](#9.5)        |
| Déprogrammer des [constructions](3_constructions)                                     | Immédiatement                                            |
| Modifier une [politique](1_galaxie_systemes_planetes#1.2.6)                           | Immédiatement                                            |
| Modifier un budget                                                                    | Plus tard, phase [**Gestion Domaine**](#9.7)             |
| Modifier la [taxation](1_galaxie_systemes_planetes#1.3.2) d'un système                | Immédiatement                                            |
| Modifier la [taxation](1_galaxie_systemes_planetes#1.3.2) d'une planète               | Immédiatement                                            |
| [Terraformer](2_population#2.1.1) un système                                          | Immédiatement                                            |
| [Terraformer](2_population#2.1.1) une planète                                         | Immédiatement                                            |
| Détruire des bâtiments                                                                | Immédiatement                                            |
| [Transfert inter-systèmes](3_constructions#3.3)                                       | Plus tard, phase [**Gestion des systèmes**](#9.5)        |
| [Coloniser](2_population#2.1.2)                                                       | Immédiatement                                            |
| Larguer des mines                                                                     | Immédiatement                                            |
| [Construire à partir d'une flotte](3_constructions#3.5)                               | Plus tard, phase [**Gestion des flottes**](#9.6)         |
| [Diviser une flotte](4_flottes#4.2)                                                   | Immédiatement                                            |
| [Déplacer une flotte](4_flottes#4.3)                                                  | Immédiatement, combats gérés phase [**Militaire**](#9.4) |
| Pister une flotte                                                                     | Immédiatement, combats gérés phase [**Militaire**](#9.4) |
| [Fusionner des flottes](4_flottes#4.1)                                                | Immédiatement                                            |
| [Donner des centaures](7_relations_entre_les_commandants#7.4)                         | Immédiatement                                            |
| [Donner une technologie](7_relations_entre_les_commandants#7.4)                       | Immédiatement                                            |
| [Donner un système](7_relations_entre_les_commandants#7.4)                            | Immédiatement                                            |
| [Donner une planète](7_relations_entre_les_commandants#7.4)                           | Immédiatement                                            |
| [Prêter une flotte](7_relations_entre_les_commandants#7.4)                            | Immédiatement                                            |
| [Vendre une flotte](7_relations_entre_les_commandants#7.4)                            | Immédiatement                                            |
| Donner une [stratégie de combat](5_combats#5.4)                                       | Immédiatement                                            |
| Donner un [plan de vaisseau](3_constructions#3.5)                                     | Immédiatement                                            |
| Créer un [plan de vaisseau](3_constructions#3.5)                                      | Immédiatement                                            |
| [Créer une alliance](7_relations_entre_les_commandants#7.2)                           | Immédiatement                                            |
| Créer une [stratégie de combat](5_combats#5.4)                                        | Immédiatement                                            |
| Abandonner une [technologie](6_recherches_technologiques#6.2)                         | Immédiatement                                            |
| Modifier le taux de taxation de vos [postes commerciaux](3_constructions#3.2)         | Immédiatement                                            |
| Renommer un système                                                                   | Immédiatement                                            |
| Renommer une planète                                                                  | Immédiatement                                            |
| Renommer une flotte                                                                   | Immédiatement                                            |
| Renommer un lieutenant                                                                | Immédiatement                                            |
| Renommer une alliance                                                                 | Immédiatement                                            |
| Vendre sur le marché galactique                                                       | Immédiatement                                            |
| Acheter sur le marché galactique                                                      | Immédiatement                                            |

## <a id="9.3"></a>3. Événements galactiques

Des événements aléatoires propres à la galaxie peuvent survenir (bonus de
ressources, etc.).

## <a id="9.4"></a>4. Militaire

Tous les combats du tour sont résolus (flotte contre flotte, flotte contre
planète). Le **butin** (pillage d'un système, récupération de marchandises
sur une flotte vaincue) est déterminé au même moment

## <a id="9.5"></a>5. Gestion des systèmes

1. Révoltes éventuelles.
2. Ajustement de réputation lié à une éventuelle politique d'extermination.
3. Réalisation des « Transfert inter-systèmes ».
4. Évolution de la stabilité.
5. Évolution de la population.
6. Production de minerai.
7. Production de marchandises.
8. Réalisation des constructions  (« Construction », « Programmer des constructions »).
9. Réparations du système.

## <a id="9.6"></a>6. Gestion des flottes

1. Résolution « Construire à partir d'une flotte ».
2. Réparations.
3. Collisions internes à la flotte.
4. Calcul des frais d'entretien.
5. Revenus et croissance de population des Villes Spatiales.
6. Retours de location de vaisseaux.

## <a id="9.7"></a>7. Gestion Domaine

1. Vos héros et gouverneurs gagnent de l'expérience et peuvent monter de niveau.
2. Résolution des recherches.
3. Calcul des budgets définitifs (recettes, revenus systèmes, dépenses, solde disponible).
4. Ajustement réputation en fonction des politiques en vigueur
   (loisir, esclavagisme, intégrisme, totalitarisme...).

## <a id="9.8"></a>8. Gestion fin de tour

1. Test si une technologie doit devenir publique.
2. Suppression des enchères "Marché galactique" qui traînent depuis plus de 5 tours.
3. Traitement des alliances.
4. Registre des commandants: départs, nouvelles inscriptions.
5. Génération des enchères de lieutenants pour le tour suivant.
6. Calcul des points de victoires.
7. Génération des rapports
