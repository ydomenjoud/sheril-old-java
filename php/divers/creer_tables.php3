
<?php

$result = mysql($base,"CREATE TABLE aa_clone(NUMERO VARCHAR(50),LISTE VARCHAR(200),DA int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE aa_registre(NUMERO int,LOGIN VARCHAR(10),MOT_DE_PASSE VARCHAR(20),NOM VARCHAR(100),ADRESSE VARCHAR(100),RACE int,TOUR_ARRIVEE int,ORDRE_LOCAL int,ENVOI_STATS int,BG_RAPPORT int,DETAIL_ORDRES int,RECEPTION_RAPPORTS int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE aa_inscription(NOM VARCHAR(100),ADRESSE VARCHAR(100),RACE int,FLOTTE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE aa_vaisseaux(ADRESSE VARCHAR(100),VAISSEAU int,NOMBRE int)")
or die ("Creation table : table déjà existente!<br>");



$result = mysql($base,"CREATE TABLE z_ordres(NUMERO int PRIMARY KEY,CODE TEXT)")
or die ("Creation table : table déjà existente!<br>");


$result = mysql($base,"CREATE TABLE z_alliances_adhesion(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_commandants_adhesion(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_alliance_dirigeant(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");


$result = mysql($base,"CREATE TABLE z_appartient_alliance_a_1(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_appartient_alliance_c_1(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_appartient_alliance_a_2(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_appartient_alliance_c_2(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_appartient_alliance_a_3(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_signer_pacte(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_rompre_pacte(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_heros(NUMERO int PRIMARY KEY, CODE TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_gouverneurs(NUMERO int PRIMARY KEY, CODE TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_leaders(CODE TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_flottes(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_systemes(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_technologies_cherchees(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_technologies_connues(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_missions(CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_systemes_detectes(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_systemes_construction(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_constructions_possibles(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_programmation_construction(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_politiques(CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_budgets(CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_taxation(CODE TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_rebus_systemes(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_rebus_materiel(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_flottes_cargo(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_cargaison_chargement(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_vaisseaux_cargo(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_proprios_poste(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_cargaison_dechargement(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_galaxies(CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_directives(CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_strategies(NUMERO int PRIMARY KEY, CODE TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_flottes_galactiques(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_flottes_intragalactiques(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_flottes_detectees(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_flottes_colonisateur(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_flottes_mines(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_flottes_usines(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_plans_constructibles(NUMERO int PRIMARY KEY, CODE TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_commandants_transfert(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_mode_transfert(CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_techno_transfert(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_plans_transfert(NUMERO int PRIMARY KEY, CODE TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_domaine_plan(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_strategie_cible(CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_strategie_agressivite(CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_alliance_secrete(CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_alliance_type(CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_systeme_renommer(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_lieutenant_renommer(NUMERO int PRIMARY KEY, CODE TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_vaisseaux(NUMERO int PRIMARY KEY, CODE TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_composants(NUMERO int PRIMARY KEY, CODE TEXT, PARAM TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE z_plans_cibles(NUMERO int PRIMARY KEY, CODE TEXT)")
or die ("Creation table : table déjà existente!<br>");







$result = mysql($base,"CREATE TABLE quitter_alliance(NUMERO int, ALLIANCE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE deplacer_flotte(NUMERO int, NUMFLOTTE int, POSX int, POSY int,GALAXIE int, DIRECTIVE int, STRATEGIE varchar(100))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE rompre_pacte(NUMERO int, PACTE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE signer_pacte(NUMERO int, PACTE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE adherer_alliance(NUMERO int, ALLIANCE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE valider_adhesion_alliance(NUMERO int, POSTULANT int,ALLIANCE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE nommer_dirigeant(NUMERO int, ALLIANCE int, DIRIGEANT int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE creer_alliance(NUMERO int, ALLIANCE varchar(100), SECRET int, TYPE int, DROITS int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE exclure_alliance(NUMERO int, ALLIANCE int, VOTE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE transferer_centaures(NUMERO int, BENEFICIAIRE int, MONTANT int, MODE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE transferer_technologie(NUMERO int, BENEFICIAIRE int, TECHNOLOGIE varchar(20), MODE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE transferer_systeme(NUMERO int, BENEFICIAIRE int, SYSTEME varchar(20), MODE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE transferer_planete(NUMERO int, BENEFICIAIRE int, SYSTEME varchar(20), PLANETE int, MODE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE transferer_flotte(NUMERO int, BENEFICIAIRE int, FLOTTE int, DUREE int, MODE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE vendre_flotte(NUMERO int, BENEFICIAIRE int, FLOTTE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE construire(NUMERO int, SYSTEME varchar(20), CONSTRUCTION varchar(100), NOMBRE int, PLANETE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE modifier_politique(NUMERO int, SYSTEME varchar(20), POLITIQUE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE modifier_budget(NUMERO int, SYSTEME varchar(20), BUDGET int, POURCENTAGE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE modifier_taxation_systeme(NUMERO int, SYSTEME varchar(20), TAXE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE modifier_taxation_planete(NUMERO int, SYSTEME varchar(20), TAXE int, PLANETE int)")
or die ("Creation table : table déjà existente!<br>");


$result = mysql($base,"CREATE TABLE terraformer_systeme(NUMERO int, SYSTEME varchar(20))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE terraformer_planete(NUMERO int, SYSTEME varchar(20), PLANETE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE mettre_au_rebus(NUMERO int, SYSTEME varchar(20), CONSTRUCTION varchar(20), NOMBRE int, PLANETE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE programmer_construction(NUMERO int, SYSTEME varchar(20), CONSTRUCTION varchar(100))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE deprogrammer_construction(NUMERO int, SYSTEME varchar(20))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE annuler_construction(NUMERO int, SYSTEME varchar(20))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE services_speciaux(NUMERO int, SYSTEME varchar(20), TYPE int, PLANETE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE affecter_recherche(NUMERO int, TECHNOLOGIE varchar(20),AFFECTATION int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE changer_capitale(NUMERO int, SYSTEME varchar(20))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE enroler_lieutenant(NUMERO int, OFFRE int, NUM int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE liberer_lieutenant(NUMERO int, NOM varchar(100))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE affecter_heros(NUMERO int, NOM varchar(100), POSITION int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE affecter_gouverneur(NUMERO int, NOM varchar(100), POSITION varchar(20))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE renommer_lieutenant(NUMERO int, ANCIEN_NOM varchar(100), NOUVEAU_NOM varchar(100))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE renommer_systeme(NUMERO int, SYSTEME varchar(20), NOM varchar(100))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE renommer_planete(NUMERO int, SYSTEME varchar(20), NOM varchar(100), PLANETE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE renommer_flotte(NUMERO int, FLOTTE int, NOM varchar(100))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE ecrire_adresse_commandant(NUMERO int, ADRESSE varchar(100))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE ecrire_adresse_alliance(NUMERO int, ADRESSE varchar(100), ALLIANCE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE ecrire_article(NUMERO int, ARTICLE TEXT)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE charger_cargo(NUMERO int, FLOTTE int, CHARGEMENT varchar(100), NOMBRE int, CARGO int, POSTE int, PLANETE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE decharger_cargo(NUMERO int, FLOTTE int, CHARGEMENT varchar(100), NOMBRE int, CARGO int, POSTE int, PLANETE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE utiliser_porte_galactique(NUMERO int, FLOTTE int, GALAXIE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE utiliser_porte_intragalactique(NUMERO int, FLOTTE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE pister(NUMERO int, FLOTTE int, CIBLE varchar(20))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE fusionner_flotte(NUMERO int, FLOTTE_1 int, FLOTTE_2 int, DIRECTIVE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE utiliser_colonisateur(NUMERO int, FLOTTE int, PLANETE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE larguer_mines(NUMERO int, FLOTTE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE renommer_alliance(NUMERO int, NOM varchar(100), ALLIANCE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE construire_flotte(NUMERO int, FLOTTE int, CONSTRUCTION varchar(100))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE donner_plan(NUMERO int, PLAN varchar(100), BENEFICIAIRE int, MODE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE diviser_flotte(NUMERO int, FLOTTE int, NOM varchar(100), NB_DIVISION int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE diviser_flotte_ajouter(NUMERO int, FLOTTE int, NB_DIVISION int,TYPE varchar(100), NOMBRE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE creer_plan(NUMERO int, NOM varchar(100), MARQUE varchar(100), DOMAINE varchar(100), ROYALTIES int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE creer_plan_ajouter(NUMERO int, NOM varchar(100), NOMBRE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE fixer_taux_poste(NUMERO int, TAUX int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE abandonner_technologie(NUMERO int, NOM varchar(20))")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE creer_strategie(NUMERO int, NOM varchar(100), AGRESSIVITE int, CIBLE int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE creer_strategie_ajouter(NUMERO int, NOM varchar(100), POS_X int, POS_Y int, CIBLE_A int, CIBLE_B int, CIBLE_C int, CIBLE_D int, CIBLE_E int, CIBLE_F int, CIBLE_G int, CIBLE_K int, CIBLE_I int, CIBLE_J int)")
or die ("Creation table : table déjà existente!<br>");

$result = mysql($base,"CREATE TABLE transferer_strategie(NUMERO int, BENEFICIAIRE int, STRATEGIE varchar(100), MODE int)")
or die ("Creation table : table déjà existente!<br>");



?>