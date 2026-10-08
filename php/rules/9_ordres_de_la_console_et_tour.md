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

## 1. Gestion des collisions

Les débris et dégâts laissés sur les positions de vos flottes depuis le tour
précédent sont réglés.

## 2. Console de commandes

Tous les ordres de tous les joueurs sont enregistrés ici, dans l'ordre où ils apparaissent dans le formulaire. 
Ils sont soit exécutés immédiatement, soit pris en compte pour une exécution plus tard dans le tour. 
Le détail pour chaque ordre est indiqué dans le tableau ci-dessous

| Enchaînement des Ordres des joueurs                                                             | Pris en compte                                                                                               |
|-------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| Adhérer à une [alliance](/rules/7_relations_entre_les_commandants#7.2)                       | Immédiatement                                                                                                |
| Valider l'adhésion à une [alliance](/rules/7_relations_entre_les_commandants#7.2)            | Immédiatement                                                                                                |
| Voter pour élire le dirigeant d'une [alliance](/rules/7_relations_entre_les_commandants#7.2) | Immédiatement                                                                                                |
| Voter pour exclure un membre d'une [alliance](/rules/7_relations_entre_les_commandants#7.2)  | Immédiatement                                                                                                |
| Quitter une [alliance](/rules/7_relations_entre_les_commandants#7.2)                         | Immédiatement                                                                                                |
| Signer un [pacte](/rules/7_relations_entre_les_commandants#7.1)                              | Immédiatement                                                                                                |
| Rompre un [pacte](/rules/7_relations_entre_les_commandants#7.1)                              | Immédiatement                                                                                                |
| Affecter un [héros](/rules/8_lieutenants#8.1)                                                | Immédiatement                                                                                                |
| Affecter un [gouverneur](/rules/8_lieutenants#8.1)                                           | Immédiatement                                                                                                |
| Licencier un [lieutenant](/rules/8_lieutenants#8.1)                                          | Immédiatement                                                                                                |
| Enroler un [lieutenant](/rules/8_lieutenants#8.1)                                            | Immédiatement                                                                                                |
| Changer la [capitale](/rules/1_galaxie_systemes_planetes#1.2)                                | Immédiatement                                                                                                |
| [Orienter les recherches](/rules/6_recherches_technologiques#6.2)                            | Plus tard, phase [**Gestion Domaine**](/rules/9_ordres_de_la_console_et_tour#7-gestion-domaine)           |
| [Services spéciaux](/rules/7_relations_entre_les_commandants#7.3)                            | Immédiatement                                                                                                |
| Annuler [construction](/rules/3_constructions)                                               | Immédiatement                                                                                                |
| [Construction](/rules/3_constructions)                                                       | Plus tard, phase [**Gestion des systèmes**](/rules/9_ordres_de_la_console_et_tour#5-gestion-des-systèmes) |
| Programmer des [constructions](/rules/3_constructions)                                       | Plus tard, phase [**Gestion des systèmes**](/rules/9_ordres_de_la_console_et_tour#5-gestion-des-systèmes) |
| Déprogrammer des [constructions](/rules/3_constructions)                                     | Immédiatement                                                                                                |
| Modifier une [politique](/rules/1_galaxie_systemes_planetes#1.2.6)                           | Immédiatement                                                                                                |
| Modifier un budget                                                                              | Plus tard, phase [**Gestion Domaine**](/rules/9_ordres_de_la_console_et_tour#7-gestion-domaine)           |
| Modifier la [taxation](/rules/1_galaxie_systemes_planetes#1.3.2) d'un système                | Immédiatement                                                                                                |
| Modifier la [taxation](/rules/1_galaxie_systemes_planetes#1.3.2) d'une planète               | Immédiatement                                                                                                |
| [Terraformer](/rules/2_population#2.1.1) un système                                          | Immédiatement                                                                                                |
| [Terraformer](/rules/2_population#2.1.1) une planète                                         | Immédiatement                                                                                                |
| Détruire des bâtiments                                                                          | Immédiatement                                                                                                |
| [Transfert inter-systèmes](/rules/3_constructions#3.3)                                       | Plus tard, phase [**Gestion des systèmes**](/rules/9_ordres_de_la_console_et_tour#5-gestion-des-systèmes) |
| [Coloniser](/rules/2_population#2.1.2)                                                       | Immédiatement                                                                                                |
| Larguer des mines                                                                               | Immédiatement                                                                                                |
| [Construire à partir d'une flotte](/rules/3_constructions#3.5)                               | Plus tard, phase [**Gestion des flottes**](/rules/9_ordres_de_la_console_et_tour#6-gestion-des-flottes)   |
| [Diviser une flotte](/rules/4_flottes#4.2)                                                   | Immédiatement                                                                                                |
| [Déplacer une flotte](/rules/4_flottes#4.3)                                                  | Immédiatement, combats gérés phase [**Militaire**](/rules/9_ordres_de_la_console_et_tour#4-militaire)     |
| Pister une flotte                                                                               | Immédiatement, combats gérés phase [**Militaire**](/rules/9_ordres_de_la_console_et_tour#4-militaire)     |
| [Fusionner des flottes](/rules/4_flottes#4.1)                                                | Immédiatement                                                                                                |
| [Donner des centaures](/rules/7_relations_entre_les_commandants#7.4)                         | Immédiatement                                                                                                |
| [Donner une technologie](/rules/7_relations_entre_les_commandants#7.4)                       | Immédiatement                                                                                                |
| [Donner un système](/rules/7_relations_entre_les_commandants#7.4)                            | Immédiatement                                                                                                |
| [Donner une planète](/rules/7_relations_entre_les_commandants#7.4)                           | Immédiatement                                                                                                |
| [Prêter une flotte](/rules/7_relations_entre_les_commandants#7.4)                            | Immédiatement                                                                                                |
| [Vendre une flotte](/rules/7_relations_entre_les_commandants#7.4)                            | Immédiatement                                                                                                |
| Donner une [stratégie de combat](/rules/5_combats#5.4)                                       | Immédiatement                                                                                                |
| Donner un [plan de vaisseau](/rules/3_constructions#3.5)                                     | Immédiatement                                                                                                |
| Créer un [plan de vaisseau](/rules/3_constructions#3.5)                                      | Immédiatement                                                                                                |
| [Créer une alliance](/rules/7_relations_entre_les_commandants#7.2)                           | Immédiatement                                                                                                |
| Créer une [stratégie de combat](/rules/5_combats#5.4)                                        | Immédiatement                                                                                                |
| Abandonner une [technologie](/rules/6_recherches_technologiques#6.2)                         | Immédiatement                                                                                                |
| Modifier le taux de taxation de vos [postes commerciaux](/rules/3_constructions#3.2)         | Immédiatement                                                                                                |
| Renommer un système                                                                             | Immédiatement                                                                                                |
| Renommer une planète                                                                            | Immédiatement                                                                                                |
| Renommer une flotte                                                                             | Immédiatement                                                                                                |
| Renommer un lieutenant                                                                          | Immédiatement                                                                                                |
| Renommer une alliance                                                                           | Immédiatement                                                                                                |
| Vendre sur le marché galactique                                                                 | Immédiatement                                                                                                |
| Acheter sur le marché galactique                                                                | Immédiatement                                                                                                |

## 3. Événements galactiques

Des événements aléatoires propres à la galaxie peuvent survenir (bonus de
ressources, etc.).

## 4. Militaire

Tous les combats du tour sont résolus (flotte contre flotte, flotte contre
planète). Le **butin** (pillage d'un système, récupération de marchandises
sur une flotte vaincue) est déterminé au même moment

## 5. Gestion des systèmes

1. Révoltes éventuelles.
2. Ajustement de réputation lié à une éventuelle politique d'extermination.
3. Réalisation des « Transfert inter-systèmes ».
4. Évolution de la stabilité.
5. Évolution de la population.
6. Production de minerai.
7. Production de marchandises.
8. Réalisation des constructions  (« Construction », « Programmer des constructions »).
9. Réparations du système.

## 6. Gestion des flottes

1. Résolution « Construire à partir d'une flotte ».
2. Réparations.
3. Collisions internes à la flotte.
4. Calcul des frais d'entretien.
5. Revenus et croissance de population des Villes Spatiales.
6. Retours de location de vaisseaux.

## 7. Gestion Domaine

1. Vos héros et gouverneurs gagnent de l'expérience et peuvent monter de niveau.
2. Résolution des recherches.
3. Calcul des budgets définitifs (recettes, dépenses, solde disponible).
4. Ajustement réputation en fonction des politiques en vigueur
   (loisir, esclavagisme, intégrisme, totalitarisme...).

## 8. Gestion fin de tour

1. Test si une technologie doit devenir publique.
2. Suppression des enchères "Marché galactique" qui traînent depuis plus de 5 tours.
3. Traitement des alliances.
4. Registre des commandants: départs, nouvelles inscriptions.
5. Génération des enchères de lieutenants pour le tour suivant.
6. Calcul des points de victoires.
7. Génération des rapports
