// v2.02 24/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.02, 24/02/01
 */

public class MessagesSystemes extends MessagesAbstraits{

 public static final String ER_FLOTTE_0000=
  "Erreur lors de la création d'une flotte de départ : valeur trop élevé.";

 public static final String ER_ORDRE_0000=
  "Entier négatif lors de réception ordre, méthode {0}, entier {1}.";
 public static final String ER_ORDRE_0001=
  "String non transformable en entier lors de réception ordre, méthode {0}, string {1}.";
  public static final String ER_ORDRE_0002=
  "Nombre d'ordres trop grand, méthode {0}.";
 public static final String ER_POSITION_0000=
  "String non transformable en position : {0}.";

 public static final String ER_COMMANDANT_ALLIANCE_0000=
  "Impossible de voter pour élire un dirigeant de l'alliance numéro {0}. Non-appartenance à l'alliance.";
 public static final String ER_COMMANDANT_ALLIANCE_0001=
  "Impossible de voter pour élire un dirigeant de l'alliance numéro {0}. Alliance de type non conforme.";
 public static final String ER_COMMANDANT_ALLIANCE_0002=
  "Impossible d'adhérer à l'alliance numéro {0} : elle n'existe pas.";
 public static final String ER_COMMANDANT_ALLIANCE_0003=
  "Impossible de voter pour élire le commandant numéro {0} à la tête d'une alliance. Commandant non-existant.";
 public static final String ER_COMMANDANT_ALLIANCE_0004=
  "Impossible de voter pour élire le commandant numéro {0} à la tête de l'alliance numéro {1}. Ce commandant n'est pas adhérent de cette alliance.";
 public static final String ER_COMMANDANT_ALLIANCE_0005=
  "Impossible d'exclure un commandant de l'alliance numéro {0}. Non-appartenance à l'alliance.";
 public static final String ER_COMMANDANT_ALLIANCE_0006=
  "Impossible de quitter l'alliance numéro {0} : elle n'existe pas.";
 public static final String ER_COMMANDANT_ALLIANCE_0008=
  "Impossible d'exclure le commandant numéro {0} d'une alliance. Commandant non-existant.";
 public static final String ER_COMMANDANT_ALLIANCE_0009=
  "Impossible d'exclure le commandant numéro {0} de l'alliance numéro {1}. Ce commandant n'est pas adhérent de cette alliance.";
 public static final String ER_COMMANDANT_ALLIANCE_0010=
  "Impossible d'exclure un commandant de l'alliance numéro {0}. Alliance de type non conforme.";
 public static final String ER_COMMANDANT_ALLIANCE_0011=
  "Impossible de créer une alliance : technologie nécessaire non connue.";
 public static final String ER_COMMANDANT_ALLIANCE_0012=
  "Impossible de renommer l'alliance {0} : elle n'existe pas.";
 public static final String ER_COMMANDANT_ALLIANCE_0013=
  "Impossible de changer le site de l'alliance {0} : elle n'existe pas.";


 public static final String ER_COMMANDANT_PACTE_0000=
   "Pacte non-existant. Impossible de rompre le pacte de non-agression avec le commandant numéro {0}.";
 public static final String ER_COMMANDANT_PACTE_0001=
   "Pacte déjà existant. Impossible de passer un pacte de non-agression avec le commandant numéro {0}.";

 public static final String ER_COMMANDANT_DON_CENTAURES_0000=
   "Impossible de donner des centaures au commandant numéro {0} : il n'existe pas.";

 public static final String ER_COMMANDANT_DON_TECHNOLOGIE_0000=
   "Impossible de donner la technologie {0} au commandant numéro {1} : ce dernier n'existe pas.";
 public static final String ER_COMMANDANT_DON_TECHNOLOGIE_0001=
   "Impossible de donner la technologie {0} au commandant numéro {1} : la technologie n'est pas connue du donneur.";

 public static final String ER_COMMANDANT_ELIMINER_TECHNOLOGIE_0000=
   "Impossible d'éliminer la technologie {0} : elle n'est pas connue du commandant.";

 public static final String ER_COMMANDANT_DON_FLOTTE_0000=
   "Impossible de donner la flotte numéro {0} au commandant numéro {1} : ce dernier n'existe pas.";
 public static final String ER_COMMANDANT_DON_FLOTTE_0001=
   "Impossible de donner la flotte numéro {0} au commandant numéro {1} : vous n'avez pas de flottes de ce numéro.";
 public static final String ER_COMMANDANT_DON_FLOTTE_0002=
   "Impossible de donner la flotte numéro {0} au commandant numéro {1} : le commandant n'est pas joué par un humain.";


 public static final String ER_COMMANDANT_VENTE_FLOTTE_0000=
   "Impossible de vendre la flotte numéro {0} au commandant numéro {1} : ce dernier n'existe pas.";
 public static final String ER_COMMANDANT_VENTE_FLOTTE_0001=
   "Impossible de vendre la flotte numéro {0} au commandant numéro {1} : vous n'avez pas de flottes de ce numéro.";
 public static final String ER_COMMANDANT_VENTE_FLOTTE_0002=
   "Impossible de vendre la flotte numéro {0} au commandant numéro {1} : le commandant n'est pas joué par un humain.";


 public static final String ER_COMMANDANT_DON_SYSTEME_0000=
   "Impossible de donner un système au commandant numéro {0} : ce dernier n'existe pas.";
 public static final String ER_COMMANDANT_DON_SYSTEME_0001=
   "Impossible de donner le système {0} : il n'existe pas.";

 public static final String ER_COMMANDANT_DON_PLANETE_0000=
   "Impossible de donner une planète au commandant numéro {0} : ce dernier n'existe pas.";
 public static final String ER_COMMANDANT_DON_PLANETE_0001=
   "Impossible de donner une planète du système {0} : le système n'existe pas.";

 public static final String ER_COMMANDANT_ANNULER_CONSTRUCTION_0000=
  "Impossible d'annuler les constructions sur le système {0} : le commandant ne le possède pas.";

 public static final String ER_COMMANDANT_MISE_EN_CHANTIER_0000=
   "Impossible de mettre en chantier une construction sur le système {0} : le commandant ne le possède pas.";
 public static final String ER_COMMANDANT_MISE_EN_CHANTIER_0001=
   "Impossible de mettre en chantier la construction {0} : le commandant ne peut pas le faire.";
 public static final String ER_COMMANDANT_MISE_EN_CHANTIER_0002=
   "Impossible de mettre en chantier une construction  sur le système {0} : votre gouverneur qui le dirige y verrait une atteinte à ses prérogatives.";

 public static final String ER_COMMANDANT_PROGRAMMER_CONSTRUCTION_0000=
   "Impossible de programmer une construction sur le système {0} : le commandant ne le possède pas.";
 public static final String ER_COMMANDANT_PROGRAMMER_CONSTRUCTION_0001=
   "Impossible de programmer la construction {0} : le commandant ne peut la construire.";
 public static final String ER_COMMANDANT_PROGRAMMER_CONSTRUCTION_0002=
   "Impossible de programmer la construction {0} : la technologie nécessaire n'est pas connue.";

 public static final String ER_COMMANDANT_ANNULER_PROGRAMME_CONSTRUCTION_0000=
   "Impossible d'annuler un programme de construction sur le système {0} : le commandant ne le possède pas.";

 public static final String ER_COMMANDANT_MODIFIER_POLITIQUE_0000=
   "Impossible de modifier la politique du système {0} : le commandant ne le possède pas.";
 public static final String ER_COMMANDANT_MODIFIER_POLITIQUE_0001=
   "Impossible de modifier la politique du système {0} : la politique demandée n'existe pas.";

 public static final String ER_COMMANDANT_MODIFIER_BUDGET_0000=
   "Impossible de modifier le budget du système {0} : le commandant ne le possède pas.";
 public static final String ER_COMMANDANT_MODIFIER_BUDGET_0001=
   "Impossible de modifier la budget du système {0} : le domaine de budget demandé n'existe pas.";

 public static final String ER_COMMANDANT_MODIFIER_TAXATION_0000=
   "Impossible de modifier la taxation du système {0} : le commandant ne le possède pas.";
 public static final String ER_COMMANDANT_MODIFIER_TAXATION_0001=
   "Impossible de modifier la taxation du système {0} : elle est trop élevée.";
 public static final String ER_COMMANDANT_MODIFIER_TAXATION_0002=
   "Impossible de modifier la taxation d'une planète du système {0} : le commandant ne possède pas ce système.";
 public static final String ER_COMMANDANT_MODIFIER_TAXATION_0003=
   "Impossible de modifier la taxation d'une planète du système {0} : la taxation est trop élevée.";
 public static final String ER_COMMANDANT_MODIFIER_TAXATION_0004=
   "Impossible de modifier la taxation d'une planète d'un système : technologie nécessaire non connue.";

 public static final String ER_COMMANDANT_TERRAFORMER_0000=
   "Impossible de terraformer le système {0} : le commandant ne le possède pas.";
 public static final String ER_COMMANDANT_TERRAFORMER_0001=
   "Impossible de terraformer une planète du système {0} : le commandant ne possède pas ce système.";
 public static final String ER_COMMANDANT_TERRAFORMER_0002=
   "Impossible de terraformer une planète : technologie nécessaire non connue.";

 public static final String ER_COMMANDANT_DETRUIRE_BATIMENT_0000=
   "Impossible de détruire des bâtiments sur le système {0} : le commandant ne le possède pas.";
 public static final String ER_COMMANDANT_DETRUIRE_BATIMENT_0001=
   "Impossible de détruire des bâtiments de code {1} sur le système {0} : ce type de bâtiment n'existe pas.";
 public static final String ER_COMMANDANT_DETRUIRE_BATIMENT_0002=
   "Impossible de détruire des bâtiments : technologie nécessaire non connue.";

 public static final String ER_COMMANDANT_AFFECTER_RECHERCHE_0000=
   "Impossible de chercher la technologie {0} : le commandant ne peut chercher cette technologie.";

 public static final String ER_COMMANDANT_MISSION_SPECIALE_0000=
   "Impossible pour les services secrets d'effectuer une mission spéciale d'un type qui n'existe pas.";

 public static final String ER_COMMANDANT_DEPLACER_FLOTTE_0000=
   "Erreur dans le déplacement d'une flotte. Directive non-existante : {0}-{1}.";
 public static final String ER_COMMANDANT_DEPLACER_FLOTTE_0001=
   "Impossible de déplacer la flotte numéro {0} : elle n'existe pas.";

 public static final String ER_COMMANDANT_CHARGER_CARGO_0000=
   "Impossible de charger un cargo de la flotte numéro {0} : elle n'existe pas.";
 public static final String ER_COMMANDANT_CHARGER_CARGO_0001=
   "Impossible de charger un cargo à partir d'une planète appartenant au joueur numéro {0} : ce dernier n'existe pas.";
 public static final String ER_COMMANDANT_CHARGER_CARGO_0002=
   "Impossible de charger un cargo à partir d'un système sur la case {0} : il n'y a pas de système ici.";
 public static final String ER_COMMANDANT_CHARGER_CARGO_0004=
   "Impossible de charger un cargo avec quelque chose de code {0} : ce code n'existe pas.";

 public static final String ER_COMMANDANT_DECHARGER_CARGO_0000=
   "Impossible de décharger un cargo de la flotte numéro {0} : elle n'existe pas.";
 public static final String ER_COMMANDANT_DECHARGER_CARGO_0001=
   "Impossible de décharger un cargo sur une planète appartenant au joueur numéro {0} : ce dernier n'existe pas.";
 public static final String ER_COMMANDANT_DECHARGER_CARGO_0002=
   "Impossible de décharger un cargo sur un système de la case {0} : il n'y a pas de système ici.";
 public static final String ER_COMMANDANT_DECHARGER_CARGO_0004=
   "Impossible de décharger un cargo de quelque chose de code {0} : ce code n'existe pas.";

 public static final String ER_COMMANDANT_FUSIONNER_FLOTTE_0000=
   "Erreur dans la fusion d'une flotte. Directive non-existante : {0}-{1}.";

 public static final String ER_COMMANDANT_DIVISER_FLOTTE_0000=
   "Impossible de diviser une flotte : le vaisseau {0} n'existe pas, ou le nombre {1} est négatif.";

 public static final String ER_COMMANDANT_PISTER_FLOTTE_0000=
   "Impossible de pister une flotte avec la flotte numéro {0} : elle n'existe pas.";
 public static final String ER_COMMANDANT_PISTER_FLOTTE_0001=
   "Impossible de pister une flotte du commandant numéro {0} : celui-ci n'existe pas.";

 public static final String ER_COMMANDANT_LANCER_MINES_0000=
   "Impossible de lancer des mines avec la flotte numéro {0} : elle n'existe pas.";
 public static final String ER_COMMANDANT_LANCER_MINES_0001=
   "Impossible de lancer des mines avec la flotte numéro {0} : elle ne possède pas de vaisseaux lanceurs de mines.";

 public static final String ER_COMMANDANT_COLONISER_PLANETE_0000=
   "Impossible de coloniser une planète avec la flotte numéro {0} : elle n'existe pas.";
 public static final String ER_COMMANDANT_COLONISER_PLANETE_0001=
   "Impossible de coloniser une planète avec la flotte numéro {0} : cette flotte ne survole pas de système.";
 public static final String ER_COMMANDANT_COLONISER_PLANETE_0002=
   "Impossible de coloniser la planète {2} du système {0} avec la flotte numéro {1} : cette planète n'existe pas.";
 public static final String ER_COMMANDANT_COLONISER_PLANETE_0003=
   "Impossible de coloniser une planète avec la flotte numéro {0} : cette flotte ne contient pas de colonisateur.";

 public static final String ER_COMMANDANT_AFFECTER_HEROS_0000=
   "Impossible d'affecter un héros sur la flotte numéro {0} : elle n'existe pas.";
 public static final String ER_COMMANDANT_AFFECTER_HEROS_0001=
   "Impossible d'affecter le héros {0} sur une flotte : il n'appartient pas au commandant.";

 public static final String ER_COMMANDANT_AFFECTER_GOUVERNEUR_0000=
   "Impossible d'affecter un gouverneur sur le système {0} : il n'appartient pas au commandant.";
 public static final String ER_COMMANDANT_AFFECTER_GOUVERNEUR_0001=
   "Impossible d'affecter le gouverneur {0} sur un système : ce gouverneur n'appartient pas au commandant.";

 public static final String ER_COMMANDANT_LICENCIER_LIEUTENANT_0000=
   "Impossible de licencier le lieutenant {0} : il n'appartient pas au commandant.";

 public static final String ER_COMMANDANT_RENOMMER_LIEUTENANT_0000=
   "Impossible de renommer le lieutenant {0} : il n'appartient pas au commandant.";
 public static final String ER_COMMANDANT_RENOMMER_LIEUTENANT_0001=
   "Impossible de renommer le lieutenant {0} : il a déjà été nommé.";

 public static final String ER_COMMANDANT_CHANGER_CAPITALE_0000=
   "Impossible de changer la capitale en {0} : ce système n'appartient pas au commandant.";

 public static final String ER_COMMANDANT_DON_PLAN_0000=
   "Impossible de donner le plan {0} : ce commandant ne le possède pas.";
 public static final String ER_COMMANDANT_DON_PLAN_0001=
   "Impossible de donner un plan au commandant {0} : ce commandant n'existe pas.";

 public static final String ER_COMMANDANT_CREER_PLAN_0000=
   "Impossible de creer un plan : le montant des royalties, {0}%, n'est pas dans les bornes admissibles.";

 public static final String ER_COMMANDANT_CONSTRUCTION_FLOTTE_0000=
   "Impossible de construire des vaisseaux {0} avec une flotte : le commandant ne possède pas ce plan.";
 public static final String ER_COMMANDANT_CONSTRUCTION_FLOTTE_0001=
   "Impossible de construire quelque chose avec la flotte {0} : cette flotte ne possède pas la capacité nécessaire.";

 public static final String ER_PLAN_DE_VAISSEAU_0001=
   "Composant non-existant. Impossible de produire le plan de vaisseau {0} avec le composant de code {1}.";

 public static final String ER_COMMANDANT_DON_STRATEGIE_0000=
   "Impossible de donner la stratégie {0} : ce commandant ne la possède pas.";
 public static final String ER_COMMANDANT_DON_STRATEGIE_0001=
   "Impossible de donner une stratégie au commandant {0} : ce commandant n'existe pas.";

 public static final String ER_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0000=
   "Impossible de changer de galaxie : flotte non-existante {0}.";
 public static final String ER_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0001=
   "Impossible de changer de galaxie : galaxie non-existante {0}.";

 public static final String ER_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0000=
   "Impossible d'utiliser une porte intragalactique : flotte {0} non-existante.";
 public static final String ER_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0001=
   "Impossible d'utiliser une porte intragalactique : flotte {0} non présente sur une porte.";

 public static final String ER_COMMANDANT_TAUX_POSTE_0000=
   "Impossible fixer les taux des postes : taux trop élevé : {0}.";

 public static final String ER_STRATEGIE_COMBAT_0000=
   "Impossible de créer une stratégie. Taux d'agressivité non dans les bornes admises : {0}.";
 public static final String ER_STRATEGIE_COMBAT_0001=
   "Impossible de créer une stratégie.  Cible principale non dans les bornes admises : {0}.";
 public static final String ER_STRATEGIE_COMBAT_0002=
   "Impossible de spécifier un vaisseau lors d'une création de stratégie.  Vaisseau non connu par le commandant : {0}.";
 public static final String ER_STRATEGIE_COMBAT_0003=
   "Impossible de spécifier un vaisseau lors d'une création de stratégie.  Pour le vaisseau {0}, position non dans les bornes admises : {1}-{2}.";



 }



