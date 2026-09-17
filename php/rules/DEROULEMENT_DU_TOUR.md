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

Tous les ordres de tous les joueurs sont exécutés ici, dans l'ordre où ils
apparaissent dans le formulaire. C'est la phase la plus longue puisqu'elle
regroupe tous les types d'ordres possibles.

| Ordre (tel que vu en jeu)                                                  | Pris en compte                                                       |
|----------------------------------------------------------------------------|----------------------------------------------------------------------|
| Adhérer à une alliance                                                     | Immédiatement                                                        |
| Valider l'adhésion à une alliance                                          | Immédiatement                                                        |
| Voter pour élire le dirigeant d'une alliance                               | Immédiatement                                                        |
| Voter pour exclure un membre d'une alliance                                | Immédiatement                                                        |
| Quitter une alliance                                                       | Immédiatement                                                        |
| Signer un pacte                                                            | Immédiatement                                                        |
| Rompre un pacte                                                            | Immédiatement                                                        |
| Affecter un héros                                                          | Immédiatement                                                        |
| Affecter un gouverneur                                                     | Immédiatement                                                        |
| Licencier un lieutenant                                                    | Immédiatement                                                        |
| Enroler un lieutenant                                                      | Immédiatement                                                        |
| Changer la capitale                                                        | Immédiatement                                                        |
| Orienter les recherches                                                    | Plus tard, phase [**Gestion Domaine**](#7-gestion-domaine)           |
| Services spéciaux                                                          | Immédiatement                                                        |
| Annuler construction                                                       | Immédiatement                                                        |
| Construction                                                               | Plus tard, phase [**Gestion des systèmes**](#5-gestion-des-systèmes) |
| Programmer des constructions                                               | Plus tard, phase [**Gestion des systèmes**](#5-gestion-des-systèmes) |
| Déprogrammer des constructions                                             | Immédiatement                                                        |
| Modifier une [politique](1_galaxie_systemes_planetes.md#1.2.6)             | Immédiatement                                                        |
| Modifier un budget                                                         | Plus tard, phase [**Gestion Domaine**](#7-gestion-domaine)           |
| Modifier la [taxation](1_galaxie_systemes_planetes.md#1.3.2) d'un système  | Immédiatement                                                        |
| Modifier la [taxation](1_galaxie_systemes_planetes.md#1.3.2) d'une planète | Immédiatement                                                        |
| [Terraformer](2_population.md#2.1.1) un système                            | Immédiatement                                                        |
| Terraformer une planète                                                    | Immédiatement                                                        |
| Détruire des bâtiments                                                     | Immédiatement                                                        |
| Transfert inter-systèmes                                                   | Plus tard, phase [**Gestion des systèmes**](#5-gestion-des-systèmes) |
| Coloniser                                                                  | Immédiatement                                                        |
| Larguer des mines                                                          | Immédiatement                                                        |
| Construire à partir d'une flotte                                           | Plus tard, phase [**Gestion des flottes**](#6-gestion-des-flottes)   |
| Diviser une flotte                                                         | Immédiatement                                                        |
| Déplacer une flotte                                                        | Immédiatement, combats gérés phase [**Militaire**](#4-militaire)     |
| Pister une flotte                                                          | Immédiatement, combats gérés phase [**Militaire**](#4-militaire)     |
| Fusionner des flottes                                                      | Immédiatement                                                        |
| Donner des centaures                                                       | Immédiatement                                                        |
| Donner une technologie                                                     | Immédiatement                                                        |
| Donner un système                                                          | Immédiatement                                                        |
| Donner une planète                                                         | Immédiatement                                                        |
| Prêter une flotte                                                          | Immédiatement                                                        |
| Vendre une flotte                                                          | Immédiatement                                                        |
| Donner une stratégie de combat                                             | Immédiatement                                                        |
| Donner un plan de vaisseau                                                 | Immédiatement                                                        |
| Créer un plan de vaisseau                                                  | Immédiatement                                                        |
| Créer une alliance                                                         | Immédiatement                                                        |
| Créer une stratégie de combat                                              | Immédiatement                                                        |
| Abandonner une technologie                                                 | Immédiatement                                                        |
| Modifier le taux de taxation de vos postes commerciaux                     | Immédiatement                                                        |
| Renommer un système                                                        | Immédiatement                                                        |
| Renommer une planète                                                       | Immédiatement                                                        |
| Renommer une flotte                                                        | Immédiatement                                                        |
| Renommer un lieutenant                                                     | Immédiatement                                                        |
| Renommer une alliance                                                      | Immédiatement                                                        |
| Vendre sur le marché galactique                                            | Immédiatement                                                        |
| Acheter sur le marché galactique                                           | Immédiatement                                                        |

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
