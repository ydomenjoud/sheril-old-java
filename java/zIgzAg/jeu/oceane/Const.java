// v2.02 24/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.02, 24/02/01
 */

public interface Const{

 public static final String ADRESSE_MJ="oceane-correspondance@altern.org";
 public static final String SMTP_ENVOI="smtp.free.fr";
 //diverses caractéristiques internet.

 public static final int NB_GALAXIES=Messages.NOMS_GALAXIES.length;

 public static final int BORNE_MAX=100;
  // Les bornes pour chaque galaxie(multiple de 20 de preference).Les coordonnées vont de 1 à BORNE_MAX.

 public static final int NB_SECTEURS=(BORNE_MAX)/4;
  // Le nombre de secteurs par galaxie.

 public static final int NB_PORTES=5;
  // nombre de portes galactiques par galaxie.

 public static final int NB_PLANETES_PAR_SYSTEMES=20;
 //Le nombre maximal de planètes par système.

 public static final int[][] PASSAGES_GALACTIQUES={{3,3},{50,50},{3,98},{98,98},{98,3}};
  // localisation des portes galactiques.

 public static final int[][] REPARTITION_DES_RACES={{0,60,40,100},{0,40,0,40},{40,100,0,40},{60,100,40,60},{60,100,60,100},
                                                    {0,20,20,100},{20,80,20,80},{80,100,0,80},{0,80,0,20},{20,100,80,100}};
  // Les répartitions initiales des races(miny,maxy,minx,maxx).
  // La répartition se fait en surfaces rectangulaires homogènes.

 public static final int NB_RACES_PAR_GALAXIE=5;
  // Il y a obligatoirement 5 races différentes par galaxie.

 public static final int NB_RACES=Messages.RACES.length;
  // Le nombre de races différentes.

 public static final int TAILLE_MAX_DE_PLANETE=5;
  // La taille d'une planète va de 1 au max.

 public static final int TYPE_MAX_DE_PLANETE=19;
  // Le type d'une planète va de 0 au max.

 public static final int PRODUCTION_MINERAI_MAX=5;
  // La production "de base" d'une planète va de 0 au max.

 public static final int NB_SORTES_ATMOSPHERES=Messages.SORTES_ATMOSPHERES.length;
  // Le nombre d'atmospheres différentes.

 public static final int RAD_MAX=200;
 public static final int RAD_MIN=0;
 public static final int TEMP_MAX=200;
 public static final int TEMP_MIN=-150;
 public static final int GRA_MAX=100;
 public static final int GRA_MIN=0;
 // Les max et min de radiations(mR),temperatures(°C) et gravites(g) possibles.

 public static final int NB_POLITIQUES=Messages.POLITIQUES.length;
 //le nombre de formes de politique.

 public static final int NB_MISSIONS=Messages.MISSIONS.length;
 //le nombre de sortes de missions possibles pour les services spéciaux.

 public static final int MISSION_ESPIONNAGE=0;
 public static final int MISSION_SABOTAGE=1;
 public static final int MISSION_VOL_TECHNOLOGIE=2;
 public static final int MISSION_PROPAGANDE=3;
 //les codes pour les différentes sortes de missions.

 public static final int NB_MARCHANDISES=Messages.MARCHANDISES.length;
 //le nombre de marchandises différentes.

 public static final int NB_ETOILES=Messages.ETOILES.length;
 //le nombre de types d'étoile.

 public static final int NB_CARACTERISTIQUES_ARMES=Messages.CARACTERISTIQUES_ARMES.length;
 //le nombre de caracteristiques pour décrire les composants de vaisseau de type arme.

 public static final int DOMAINES_BUDGET_TECHNOLOGIQUE=0;
 public static final int DOMAINES_BUDGET_SERVICES_SPECIAUX=1;
 public static final int DOMAINES_BUDGET_CONTRE_ESPIONNAGE=2;
 //Les différents champs de budget de système.

 public static final int MODIFICATEUR_DIFFICULTE_RECHERCHE=3;
 //plus cette variable est grande, plus il est difficile de trouver des technologies(c'est un coefficient multiplicateur)

 public static final int NB_DOMAINES_BUDGET=Messages.DOMAINES_BUDGET.length;
 //le nombre de domaines différents dans l'affectation du budget.

 public static final int TAXATION_MAXIMALE=5;
 //le taux d'impôts le plus élevé possible.

 public static final float COUT_TERRAFORMATION=10F;
 //Le cout de terraformation par niveau pour une planète.

 public static final String CV_ARME="arme";
 public static final String CV_MOTEUR="moteur";
 public static final String CV_AUTRE="autre";
 //Les caractérisations des différents types de composant de vaisseau.

 public static final String CV_ARME_CS="combat spatial";
 public static final String CV_ARME_M="mixte";
 public static final String CV_ARME_CP="combat planétaire";
 //Les caractérisations des différents types d'armes.

 public static final int NB_BOUCLIERS_MAX=10;
 //Le nombre maximum de boucliers par vaisseau.

 public static final int NB_NAVIRE_USINE_MAX=1;
 //Le nombre maximal de modules de construction par vaisseau.

 public static final int ROYALTIES_MAXIMALES=100;
  //Le taux maximal de royalties permises sur un plan de vaisseau.

 public static final int MODIFICATEUR_MULTIPLICATEUR_CREATION=5;
  //Le modificateur multiplicateur du prix nécessaire pour créer un plan de vaisseau.

 //public static final int NB_MAX_ALLIANCES=2;
 //Le nombre maximal d'alliances possibles par joueur.

 public static final float COUT_CREATION_ALLIANCE_SECRETE=2000F;
 public static final float COUT_CREATION_ALLIANCE_NON_SECRETE=1000F;
 //Les différents coûts de création d'alliance.

 public static final int ALLIANCE_TYPE_DEMOCRATIQUE=0;
 public static final int ALLIANCE_TYPE_AUTOCRATIQUE=1;
 public static final int ALLIANCE_TYPE_ANARCHIQUE=2;
 //Les différents types d'alliance.

 public static final int NB_TYPE_ALLIANCES=Messages.TYPE_ALLIANCE.length;
  //Le nombre de type d'alliance.

 public static final float REVENU_ALLIANCE_ANARCHIQUE=100F;
  //Le revenu fixe d'une alliance anarchique.

 public static final int DON_MODE_NORMAL=0;
 public static final int DON_MODE_CACHE=1;
 public static final int DON_MODE_ANONYME=2;
 //Les différents types de don.

 public static final int NB_MODES_TRANSFERTS=Messages.MODE_TRANSFERT.length;
 //le nombre de mode de transferts

 public static final int SURCOUT_DON_CENTAURES_CACHE=50;
 public static final int SURCOUT_DON_CENTAURES_ANONYME=100;
 //le surcout (en pourcentage) suivant le mode de transmission.
 public static final int DON_MAXIMUM_SECRET=300;
 //la barrière au-delà de laquelle un don classique devient publique.

 public static final float SURCOUT_DON_TECHNO_CACHE=100F;
 public static final float SURCOUT_DON_TECHNO_ANONYME=200F;
 //le surcout (en centaures) suivant le mode de transmission
 public static final int CHANCE_DON_TECHNO_PUBLIC=20;
 //Le pourcentage de chance que le don de technologie en mode normal devienne public.

 public static final float SURCOUT_DON_SYSTEME_CACHE=300F;
 public static final float SURCOUT_DON_SYSTEME_ANONYME=500F;
 //le surcout (en centaures) suivant le mode de transmission
 public static final int CHANCE_DON_SYSTEME_PUBLIC=80;
 //Le pourcentage de chance que le don de systeme en mode normal devienne public.

 public static final float SURCOUT_DON_PLANETE_CACHE=50F;
 public static final float SURCOUT_DON_PLANETE_ANONYME=80F;
 //le surcout (en centaures) suivant le mode de transmission
 public static final int CHANCE_DON_PLANETE_PUBLIC=10;
 //Le pourcentage de chance que le don de planete en mode normal devienne public.

 public static final float SURCOUT_DON_FLOTTE_CACHE=50F;
 public static final float SURCOUT_DON_FLOTTE_ANONYME=200F;
 //le surcout (en centaures) suivant le mode de transmission
 public static final int CHANCE_DON_FLOTTE_PUBLIC=10;
 //Le pourcentage de chance que le don de flotte en mode normal devienne public.

 public static final float SURCOUT_DON_STRATEGIE_CACHE=20F;
 public static final float SURCOUT_DON_STRATEGIE_ANONYME=100F;
 //le surcout (en centaures) suivant le mode de transmission
 public static final int CHANCE_DON_STRATEGIE_PUBLIC=5;
 //Le pourcentage de chance que le don de stratégie en mode normal devienne public.

 public static final float SURCOUT_DON_PLAN_CACHE=20F;
 public static final float SURCOUT_DON_PLAN_ANONYME=20F;
 //le surcout (en centaures) suivant le mode de transmission
 public static final int CHANCE_DON_PLAN_PUBLIC=5;
 //Le pourcentage de chance que le don de plan en mode normal devienne public.



 //public static final int NB_MAX_RECHERCHES=3;
 //Le nombre maximal de technologies que le joueur peut rechercher en même temps.

 public static final int MORAL_DEPART_EQUIPAGE=4500;
 //la caractéristique moral de l'équipage d'un vaisseau lors de sa création.

 public static final int REPUTATION_RUPTURE_DE_PACTE=-100;
 public static final int REPUTATION_SIGNATURE_DE_PACTE=25;
 public static final int REPUTATION_COLONISER_PLANETE=50;
 public static final int REPUTATION_ATTAQUER_PLANETE=-100;
 //les différents éléments agissant sur la réputation.

 public static final int RELATION_ATTAQUE_ERADICATION=-200;
 public static final int RELATION_ATTAQUE_PILLAGE=-50;
 public static final int RELATION_ATTAQUE_PLANETE=-20;
 public static final int RELATION_TRANSFERT_TECHNOLOGIE=50;
 public static final int RELATION_TRANSFERT_SYSTEME=10;
 //les différents éléments agissant sur la relation entre les races.

 public static final int POLITIQUE_IMPOT=0;
 public static final int POLITIQUE_COMMERCE=1;
 public static final int POLITIQUE_DEFENSE=2;
 public static final int POLITIQUE_CONSTRUCTION=3;
 public static final int POLITIQUE_EXPANSION=4;
 public static final int POLITIQUE_INTEGRISME=5;
 public static final int POLITIQUE_TOTALITAIRE=6;
 public static final int POLITIQUE_ESCLAVAGISTE=7;
 public static final int POLITIQUE_ANTI_HUMAIN=8;
 public static final int POLITIQUE_ANTI_ZORGLUB=9;
 public static final int POLITIQUE_ANTI_GOLO=10;
 public static final int POLITIQUE_ANTI_YOZDA=11;
 public static final int POLITIQUE_ANTI_JONDOISHI=12;
 public static final int POLITIQUE_ANTI_NOMADE=13;
 public static final int POLITIQUE_ANTI_DREWIN=14;
 public static final int POLITIQUE_ANTI_TONK=15;
 public static final int POLITIQUE_ANTI_GOLUB=16;
 public static final int POLITIQUE_ANTI_ZOOUSH=17;
 //Les différentes politiques.

 public static final int PRODUIT_NOURRITURE=0;
 public static final int PRODUIT_MATERIEL_AGRICOLE=1;
 public static final int PRODUIT_LUXE=2;
 public static final int PRODUIT_HOLOFILM=3;
 public static final int PRODUIT_ALCOOLS=4;
 public static final int PRODUIT_MEDICAMENT=5;
 public static final int PRODUIT_LOGICIEL=6;
 public static final int PRODUIT_ROBOT=7;
 public static final int PRODUIT_COMPOSANT_ELECTRONIQUE=8;
 public static final int PRODUIT_ARMEMENT=9;
 public static final int PRODUIT_CARBURANT=10;
 public static final int PRODUIT_PIECE_INDUSTRIELLE=11;
 public static final int PRODUIT_METAUX_PRECIEUX=12;
 public static final int PRODUIT_TIXIUM=13;
 public static final int PRODUIT_LIXIAM=14;
 public static final int PRODUIT_OXOLE=15;
 //Les différents produits commerciaux.


 public static final int BUDGET_COMMANDANT_TOUR_PRECEDENT=0;
 public static final int BUDGET_COMMANDANT_REVENUS_SYSTEMES=1;
 public static final int BUDGET_COMMANDANT_REVENUS_ALLIANCE=2;
 public static final int BUDGET_COMMANDANT_TAXE_SUR_DROITS_ENTREE_ALLIANCE=3;
 public static final int BUDGET_COMMANDANT_RECEPTION_CENTAURES=4;
 public static final int BUDGET_COMMANDANT_VENTE_MARCHANDISE=5;
 public static final int BUDGET_COMMANDANT_LICENCIER_LIEUTENANT=6;
 public static final int BUDGET_COMMANDANT_POLITIQUE_EXTERMINATION=7;
 public static final int BUDGET_COMMANDANT_PERCEPTION_ROYALTIES=8;
 public static final int BUDGET_REVENUS_VILLE_SPATIALE=9;
 public static final int BUDGET_COMMANDANT_PILLAGE_PLANETE=10;
 public static final int BUDGET_COMMANDANT_REVENUS_DIVERS=11;
 public static final int BUDGET_COMMANDANT_VENTE_FLOTTE=12;
 public static final int BUDGET_COMMANDANT_TOTAL_RECETTE=19;
 public static final int BUDGET_COMMANDANT_CREATION_ALLIANCE=20;
 public static final int BUDGET_COMMANDANT_ADHESION_ALLIANCE=21;
 public static final int BUDGET_COMMANDANT_DON_CENTAURES=22;
 public static final int BUDGET_COMMANDANT_DON_TECHNOLOGIE=23;
 public static final int BUDGET_COMMANDANT_DON_SYSTEME=24;
 public static final int BUDGET_COMMANDANT_DON_PLANETE=25;
 public static final int BUDGET_COMMANDANT_TERRAFORMATION=26;
 public static final int BUDGET_COMMANDANT_ACHAT_MARCHANDISE=27;
 public static final int BUDGET_COMMANDANT_ACHAT_LIEUTENANT=28;
 public static final int BUDGET_COMMANDANT_CHANGER_CAPITALE=29;
 public static final int BUDGET_COMMANDANT_CHANGER_POLITIQUE=30;
 public static final int BUDGET_COMMANDANT_PRET_FLOTTE=31;
 public static final int BUDGET_COMMANDANT_DON_PLAN=32;
 public static final int BUDGET_COMMANDANT_CREATION_PLAN=33;
 public static final int BUDGET_COMMANDANT_DON_STRATEGIE=34;
 public static final int BUDGET_COMMANDANT_REALISATION_CONSTRUCTION=35;
 public static final int BUDGET_COMMANDANT_ENTRETIEN_FLOTTE=36;
 public static final int BUDGET_COMMANDANT_ENTRETIEN_SYSTEME=37;
 public static final int BUDGET_COMMANDANT_RECHERCHE=38;
 public static final int BUDGET_COMMANDANT_SERVICES_SPECIAUX=39;
 public static final int BUDGET_COMMANDANT_CONTRE_ESPIONNAGE=40;
 public static final int BUDGET_COMMANDANT_ENTRETIEN_LIEUTENANTS=41;
 public static final int BUDGET_COMMANDANT_ENTRETIEN_TECHNOLOGIES=42;
 public static final int BUDGET_COMMANDANT_REPARATION_FLOTTE=43;
 public static final int BUDGET_COMMANDANT_ACHAT_FLOTTE=44;
 public static final int BUDGET_COMMANDANT_DEPENSES_DIVERSES=45;
 public static final int BUDGET_COMMANDANT_FRANCHISSEMENT_PORTE_INTRAGALACTIQUE=46;
 public static final int BUDGET_COMMANDANT_TOTAL_DEPENSE=47;
 public static final int BUDGET_COMMANDANT_TOTAL_DISPONIBLE=48;
 //Les différents champs du budget.

 public static final int COMPOSANT_CAPACITE_PROPULSION=0;
 public static final int COMPOSANT_PORTEE_SCANNER_SYSTEME=1;
 public static final int COMPOSANT_CAPACITE_PROPULSION_INTRAGALACTIQUE=2;
 public static final int COMPOSANT_CAPACITE_PROPULSION_INTERGALACTIQUE=3;
 public static final int COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE=4;
 public static final int COMPOSANT_CAPACITE_LANCEUR_MINES_CLASSIQUES=5;
 public static final int COMPOSANT_CAPACITE_DETECTION_MINES=6;
 public static final int COMPOSANT_CAPACITE_NAVIRE_USINE=7;
 public static final int COMPOSANT_BROUILLAGE_RADAR=8;
 public static final int COMPOSANT_CAPACITE_RAYON_TRACTEUR=9;
 public static final int COMPOSANT_CAPACITE_CONTENANCE_CARGO=10;
 public static final int COMPOSANT_CAPACITE_VILLE_SPATIALE=11;
 public static final int COMPOSANT_CAPACITE_COLONISATEUR=12;
 public static final int COMPOSANT_PORTEE_SCANNER_FLOTTE=13;
 public static final int COMPOSANT_DRAGUEUR_MINES=14;
 public static final int BATIMENT_CAPACITE_PRODUCTION_VAISSEAU=0;
 public static final int BATIMENT_CAPACITE_EXTRACTION_MINERAI=1;
 public static final int BATIMENT_CAPACITE_RECYCLAGE_MINERAI=2;
 public static final int BATIMENT_GAIN_POINTS_CONSTRUCTION=3;
 public static final int BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE=4;
 public static final int BATIMENT_CAPACITE_REPARATION_VAISSEAU=5;
 public static final int BATIMENT_CAPACITE_BOUCLIER=6;
 public static final int BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE=7;
 public static final int BATIMENT_PORTEE_RADAR=8;
 public static final int BATIMENT_CAPACITE_EXTRACTION_AVANCE=9;
 //les différentes caractéristiques spéciales.

 public static final int DIRECTIVE_FLOTTE_ATTITUDE_NEUTRE=0;
 public static final int DIRECTIVE_FLOTTE_ATTAQUE_JOUEUR=8;
 public static final int DIRECTIVE_FLOTTE_ATTAQUE_PREVENTIVE=2;
 public static final int DIRECTIVE_FLOTTE_ATTAQUE_TOUTE_FLOTTES=3;
 public static final int DIRECTIVE_FLOTTE_ATTAQUE_SYSTEME=1;
 public static final int DIRECTIVE_FLOTTE_ATTAQUE_PLANETE=5;
 public static final int DIRECTIVE_FLOTTE_PILLAGE_SYSTEME=4;
 public static final int DIRECTIVE_FLOTTE_PILLAGE_PLANETE=6;
 public static final int DIRECTIVE_FLOTTE_ERADICATION_PLANETE=7;
 //Les différents types de directive

 public static final int NB_DIRECTIVES=Messages.DIRECTIVES.length;
 //Le nombre de types de directive différents.

 public static final float COUT_REPARATION_VAISSEAU=0.5F;
  //Le côut de réparation d'un point de dommage pour un vaisseau.
 public static final int POINTS_REPARATION_BATIMENT=5;
  //Le nombre de points de réparation d'un bâtiment par tour.

 public static final int ARME_VITESSE_DE_BASE=0;
 public static final int ARME_DOMMAGES_BOUCLIER=1;
 public static final int ARME_DOMMAGES_COQUE=2;
 public static final int ARME_DOMMAGES_SOL=3;
 public static final int ARME_PORTEE=4;
 public static final int ARME_FIABILITE=5;
 //les différentes caractéristiques des composants armes.

 public static final int TRANSPORT_MINERAI=0;
 public static final int TRANSPORT_AUCUN=-1;
 public static final int TRANSPORT_MARCHANDISE=1;
 public static final int TRANSPORT_BATIMENT=2;
 public static final int TRANSPORT_POPULATION=3;
 //Les codes pour les différents types de transport.

 public static final int TECHNOLOGIE_TYPE_BATIMENT=0;
 public static final int TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU=1;
 public static final int TECHNOLOGIE_TYPE_SIMPLE=-1;
 //le type de technologie recherché(pour la recherche des capacités spéciales).

 public static final int PRIX_MARCHANDISE_PAR_DEFAUT=50;
 //Le prix des marchandises par défaut.

 public static final int POSITION_HEROS_RESERVE=-1;
 public static final Position POSITION_GOUVERNEUR_RESERVE=null;
 //Les positions de "réserve" pour les leaders.

 public static final int COMPETENCE_LEADER_MAITRISE_VITESSE=0;
 public static final int COMPETENCE_LEADER_MAITRISE_ATTAQUE=1;
 public static final int COMPETENCE_LEADER_MAITRISE_DEFENSE=2;
 public static final int COMPETENCE_LEADER_MAITRISE_MORAL=3;
 public static final int COMPETENCE_LEADER_MAITRISE_MARCHANDAGE=4;
 public static final int COMPETENCE_LEADER_INSPIRATION_FANATIQUE=5;
 public static final int COMPETENCE_LEADER_ENTRETIEN_FLOTTE=6;
 public static final int COMPETENCE_LEADER_ENTRETIEN_LEADER=7;
 public static final int COMPETENCE_LEADER_IMMORTALITE=8;
 public static final int COMPETENCE_LEADER_VOYAGEUR=9;
 public static final int COMPETENCE_LEADER_VOYAGE_INTRAGALACTIQUE=10;
 public static final int COMPETENCE_LEADER_VOYAGE_INTERGALACTIQUE=11;
 public static final int COMPETENCE_LEADER_MAITRISE_SAVOIR=12;
 public static final int COMPETENCE_LEADER_ENTRETIEN_SYSTEME=13;
 public static final int COMPETENCE_LEADER_MAITRISE_FINANCE=14;
 //les différentes compétences de leader.

 public static final int NOMBRE_COMPETENCES=Messages.COMPETENCES_LEADER.length;
 //Le nombre de compétences différentes pour un leader.

 public static final float VALEUR_UNITE=100F;
 public static final float VALEUR_COMPETENCE_SPECIALE=2000F;
 public static final float VALEUR_COMPETENCE_NORMAL=1000F;
 //la valeur d'un point de caractéristique,d'une compétence spéciale,ou d'une compétence normale d'un leader.

 public static final int[][] CHANCE_AUGMENTER_CARACTERISTIQUE_LEADER=
  { {20,20,20,20,20},{30,15,15,10,30},{25,30,30,10,5},{15,10,10,30,35},{15,15,15,35,20},
    {20,20,20,20,20},{30,10,30,10,20},{35,25,10,20,10},{30,25,25,10,10},{30,20,10,20,20}
  };
 //les différentes possibilités d'augmentation de caractéristique pour un leader par rapport à sa race.

 public static final int[][] CHANCE_TROUVER_COMPETENCE_HEROS=
  { {9,9,9,9,9,9,9,9,9,9,0,0,10,0,0},{15,5,5,7,8,8,7,10,10,15,0,0,10,0,0},{15,15,15,5,5,12,5,5,5,5,0,0,13,0,0},
    {8,5,5,15,15,12,8,5,15,5,0,0,7,0,0},{7,7,7,13,7,5,10,8,14,7,0,0,15,0,0},
    {9,9,9,9,9,9,9,9,9,9,0,0,10,0,0},{15,5,12,5,8,8,7,5,15,10,0,0,10,0,0},{15,15,5,10,5,15,13,5,5,5,0,0,7,0,0},
    {5,15,5,5,10,15,8,10,10,10,0,0,7,0,0},{15,7,7,5,8,5,15,8,7,8,0,0,15,0,0}
  };
 //les différentes possibilités de répartition de compétence pour un héros par rapport à sa race.

 public static final int[][] CHANCE_TROUVER_COMPETENCE_GOUVERNEUR=
  { {9,9,9,9,9,9,0,9,9,0,0,0,8,9,11},{15,5,5,7,10,10,0,10,10,0,0,0,8,10,10},{15,15,15,5,5,12,0,5,5,0,0,0,11,5,7},
    {8,5,5,15,15,12,0,5,15,0,0,0,5,8,7},{7,7,7,13,7,5,0,8,12,0,0,0,15,10,9},
    {9,9,9,9,9,9,0,9,9,0,0,0,8,9,11},{15,5,12,7,10,5,0,5,5,0,0,0,8,10,8},{15,15,5,5,5,12,0,5,5,0,0,0,11,15,7},
    {8,15,5,15,5,12,0,5,15,0,0,0,5,8,7},{15,10,7,9,7,5,0,8,5,0,0,0,15,10,9}
  };
 //les différentes possibilités de répartition de compétence pour un gouverneur par rapport à sa race.

 public static final int DEBRIS_METEORITE=-1;
 public static final int DEBRIS_TROU_NOIR=-2;
 public static final int DEBRIS_RESTE_VAISSEAUX=-3;
 public static final int DEBRIS_MINES_CLASSIQUES=-4;
 //les types de débris(les mines intelligentes sont désignées par le numéro de leur joueur).

 public static final int[][] HABITAT_RADIATION={ {1,85},{0,150},{5,75},{0,200},{10,70},
                                                 {1,110},{50,200},{0,200},{50,150},{0,100}
                                                 };
 public static final int[][] HABITAT_TEMPERATURE={ {-80,70},{0,200},{-150,10},{-150,200},{-150,200},
                                                   {-80,70},{-50,100},{-150,200},{-60,80},{-100,120}
                                                    };
 public static final int[][] HABITAT_GRAVITE={ {1,40},{1,40},{1,100},{0,100},{10,35},
                                               {1,35},{1,40},{30,100},{1,60},{0,25}
                                                };
  //Les conditions supportées par les différentes races(min et max).

 public static final int[][] RACES_ATMOSPHERES=
  { {2,1,0,-1,-2},{0,0,1,2,3},{-1,0,3,0,-1},{-3,-2,-1,0,2},{2,1,0,1,2},
    {2,1,0,-1,-2},{3,2,0,-2,-3},{0,0,0,0,0},{0,0,2,1,1},{1,1,-3,1,1}
  };
  // Le modificateur de progression de population suivant la race et le type d'atmosphère.

 public static final int[][] RACES_CARACTERISTIQUES={
  {0,0,0,0},{5,5,-10,-5},{-2,-10,5,15},{10,0,-5,-15},{0,15,-5,-5},
  {0,0,0,0},{-2,-5,0,10},{0,-10,15,0},{10,0,-10,10},{0,15,0,-10}
  };
 public static final int RACE_CARACTERISTIQUE_AUGMENTATION_POPULATION=0;
 public static final int RACE_CARACTERISTIQUE_BONUS_TECHNOLOGIQUE=1;
 public static final int RACE_CARACTERISTIQUE_COMBAT_SPATIAL=2;
 public static final int RACE_CARACTERISTIQUE_COMBAT_PLANETAIRE=3;
 public static final String[] RACE_TECHNOLOGIES={"battlaII","moteurIII","radarV","missIII",null,
                                                 null,null,null,null,null};
 // les modificateurs propres à chaque race.

 public static final int STRATEGIE_AGRESSIVITE_RAGE=5;
 public static final int STRATEGIE_AGRESSIVITE_COMBATIF=4;
 public static final int STRATEGIE_AGRESSIVITE_NORMAL=3;
 public static final int STRATEGIE_AGRESSIVITE_PRUDENT=2;
 public static final int STRATEGIE_AGRESSIVITE_FUYARD=1;
 public static final int STRATEGIE_AGRESSIVITE_PILLAGE=0;
 //Les différents types d'agressivite.
 public static final int STRATEGIE_AGRESSIVITE_NB_MAXIMAL=STRATEGIE_AGRESSIVITE_RAGE;
 //le numéro le plus élevé d'agressivité(pour vérification des ordres);

 public static final int STRATEGIE_CIBLE_CARGO=3;
 public static final int STRATEGIE_CIBLE_BOMBARDIER=2;
 public static final int STRATEGIE_CIBLE_CHASSEUR=1;
 public static final int STRATEGIE_CIBLE_PROCHE=0;
 //Les différents types de cibles possibles.
 public static final int STRATEGIE_CIBLE_NB_MAXIMAL=STRATEGIE_CIBLE_CARGO;
 //le numéro le plus élevé de type de cible(pour vérification des ordres);

 public static final StrategieDeCombatSpatial STRATEGIE_DEFAUT=new StrategieDeCombatSpatial(
      "{0}",STRATEGIE_AGRESSIVITE_NORMAL,STRATEGIE_CIBLE_PROCHE,new String[0],new int[0][0],new int[0][0]);
 //Les stratégies possibles au départ pour une flotte.

 public static final int COMBAT_X_MAX=30;
 public static final int COMBAT_X_MIN=0;
 public static final int COMBAT_Y_MAX=10;
 public static final int COMBAT_Y_MIN=0;
 public static final int COMBAT_Y_ESPACE=10;
 public static final int COMBAT_Y_FIN1=-1000;
 public static final int COMBAT_Y_FIN2=-COMBAT_Y_FIN1+COMBAT_Y_MAX+COMBAT_Y_ESPACE;

 //Les dimensions des deux camps lors d'un combat au départ.

 public static final int[][] TAILLE_VAISSEAUX={{1,2,9},{3,4,8},{5,8,7},{9,20,6},{21,50,5},
         {51,100,4},{101,200,3},{201,500,2},{501,1000,1},{1001,1000000,0}};
 //Les paramétrages de la taille des vaisseaux(min nb cases,max nb cases,vitesse de base)

 public static final int TAILLE_MAXIMAL_VAISSEAU=10;
 //La taille maximale d'un vaisseau.

 public static final int BASE_NIVEAU_EXPERIENCE=2000;
 public static final int BASE_NIVEAU_MORAL=1000;
 public static final int BASE_NIVEAU_PUISSANCE=10;
 //la base pour calculer le niveau d'expérience.
 //la base pour calculer le niveau de moral.
 //la base pour calculer le niveau de puissance.

 public static final int CHANCE_EXPLOSION_MOTEUR=1;
  //le pourcentage de chance pour qu'un moteur explose quand il est touché.

 public static final int NOMBRE_SALVE_BATTERIE=50;
  //le nombre de tirs par batterie de défense planétaire.

 public static float COUT_CHANGER_CAPITALE=100F;
  //Le coût d'un changement de capitale.

 public static int[][] MODIFICATEUR_STABILITE_CAPITALE={{0,3},{1,2},{2,1},{6,0},{8,-1},{11,-3},{Integer.MAX_VALUE,-8}};
  //Les modificateurs de stabilité en fonction de la distance par rapport à la capitale.
 public static int[] MODIFICATEUR_STABILITE_TAXATION={6,3,0,-3,-7,-12};
  //Les modificateurs de stabilité en fonction de la taxation de la planète.


 public static float COUT_CHANGER_POLITIQUE=10F;
  //Le coût d'un changement de politique.


 public static final String[] NOMS_TABLES_ORDRES={
  "adherer_alliance","valider_adhesion_alliance","nommer_dirigeant","exclure_alliance","quitter_alliance",
  "signer_pacte","rompre_pacte","affecter_heros","affecter_gouverneur","liberer_lieutenant","enroler_lieutenant",
  "changer_capitale","affecter_recherche","services_speciaux",
  "annuler_construction","construire","programmer_construction","deprogrammer_construction",
  "modifier_politique","modifier_budget","modifier_taxation_systeme","modifier_taxation_planete",
  "terraformer_systeme","terraformer_planete","mettre_au_rebus",
  "decharger_cargo","charger_cargo",
  "utiliser_colonisateur","larguer_mines","construire_flotte",
  "utiliser_porte_galactique","utiliser_porte_intragalactique","diviser_flotte","deplacer_flotte","pister",
  "fusionner_flotte",
  "transferer_centaures","transferer_technologie","transferer_systeme","transferer_planete","transferer_flotte",
  "vendre_flotte","transferer_strategie","donner_plan",
  "creer_plan","creer_alliance","creer_strategie","abandonner_technologie","fixer_taux_poste",
  "renommer_systeme","renommer_planete","renommer_flotte","renommer_lieutenant","renommer_alliance",
  "ecrire_adresse_commandant","ecrire_adresse_alliance","ecrire_article",
  "diviser_flotte_ajouter","creer_plan_ajouter","creer_strategie_ajouter"};
  //le nom des tables des différents ordres.Attention! elles servent également à définir les méthodes de réception d'ordre,
  //ainsi que les productions d'ordres! Données sensibles :o) !!!

 public static final int BORNE_ORDRES_VISIBLES=NOMS_TABLES_ORDRES.length-3;
 //la borne des ordres visibles.

 public static final int ORDRE_VALIDER_ADHESION_ALLIANCE=1;
 public static final int ORDRE_EXCLURE_ALLIANCE=3;
 public static final int ORDRE_SIGNER_PACTE=5;
 public static final int ORDRE_ENROLER_LIEUTENANT=10;
 public static final int ORDRE_AFFECTER_RECHERCHE=12;
 public static final int ORDRE_SERVICES_SPECIAUX=13;
 public static final int ORDRE_MODIFIER_BUDGET=19;
 public static final int ORDRE_DIVISER_FLOTTE=32;
 public static final int ORDRE_DEPLACEMENT_FLOTTE=33;
 public static final int ORDRE_DON_TECHNOLOGIE=37;
 public static final int ORDRE_CREER_PLAN=44;
 public static final int ORDRE_CREER_STRATEGIE=46;
 public static final int ORDRE_DIVISER_FLOTTE_DETAIL=57;
 public static final int ORDRE_CREER_PLAN_DETAIL=58;
 public static final int ORDRE_CREER_STRATEGIE_DETAIL=59;
 //certains ordres particuliers.

 public static final int NOMBRE_LIMITE_ENROLER_LIEUTENANT=1;
 public static final int NOMBRE_LIMITE_SERVICES_SPECIAUX=3;
 public static final int NOMBRE_LIMITE_DON_TECHNOLOGIE=1;
 public static final int NOMBRE_LIMITE_CREATION_PLAN=1;
 public static final int NOMBRE_LIMITE_CREATION_STRATEGIE=1;
 //limitation d'un certain nombre d'ordres.


 public static final String TABLE_REGISTRE="aa_registre";
 public static final String TABLE_INSCRIPTION="aa_inscription";
 public static final String TABLE_INSCRIPTION_VAISSEAUX="aa_vaisseaux";
 public static final String TABLE_ORDRES="z_ordres";
 public static final String TABLE_ALLIANCE_ADHESION="z_alliances_adhesion";
 public static final String TABLE_COMMANDANT_ADHESION="z_commandants_adhesion";
 public static final String TABLE_DIRIGEANT="z_alliance_dirigeant";
 public static final String TABLE_ALLIANCE_APPARTENANCE_1="z_appartient_alliance_a_1";
 public static final String TABLE_COMMANDANT_ALLIANCE_APPARTENANCE_1="z_appartient_alliance_c_1";
 public static final String TABLE_ALLIANCE_APPARTENANCE_2="z_appartient_alliance_a_2";
 public static final String TABLE_COMMANDANT_ALLIANCE_APPARTENANCE_2="z_appartient_alliance_c_2";
 public static final String TABLE_ALLIANCE_APPARTENANCE_3="z_appartient_alliance_a_3";
 public static final String TABLE_COMMANDANT_SIGNER_PACTE="z_signer_pacte";
 public static final String TABLE_COMMANDANT_ROMPRE_PACTE="z_rompre_pacte";
 public static final String TABLE_HEROS="z_heros";
 public static final String TABLE_GOUVERNEURS="z_gouverneurs";
 public static final String TABLE_LEADER_EN_VENTE="z_leaders";
 public static final String TABLE_FLOTTES="z_flottes";
 public static final String TABLE_SYSTEMES="z_systemes";
 public static final String TABLE_TECHNOLOGIES_CHERCHEES="z_technologies_cherchees";
 public static final String TABLE_TECHNOLOGIES_CONNUES="z_technologies_connues";
 public static final String TABLE_MISSIONS="z_missions";
 public static final String TABLE_SYSTEMES_DETECTES="z_systemes_detectes";
 public static final String TABLE_SYSTEMES_CONSTRUCTION_EN_COURS="z_systemes_construction";
 public static final String TABLE_CONSTRUCTIONS_POSSIBLES="z_constructions_possibles";
 public static final String TABLE_PROGRAMMATION_CONSTRUCTION="z_programmation_construction";
 public static final String TABLE_POLITIQUES="z_politiques";
 public static final String TABLE_BUDGETS="z_budgets";
 public static final String TABLE_TAXATION="z_taxation";
 public static final String TABLE_REBUS_SYSTMES="z_rebus_systemes";
 public static final String TABLE_REBUS_MATERIEL="z_rebus_materiel";
 public static final String TABLE_FLOTTE_CARGO="z_flottes_cargo";
 public static final String TABLE_CARGAISON_CHARGEMENT="z_cargaison_chargement";
 public static final String TABLE_VAISSEAUX_CARGOS="z_vaisseaux_cargo";
 public static final String TABLE_PROPRIOS_POSTES="z_proprios_poste";
 public static final String TABLE_CARGAISON_DECHARGEMENT="z_cargaison_dechargement";
 public static final String TABLE_GALAXIES="z_galaxies";
 public static final String TABLE_DIRECTIVES="z_directives";
 public static final String TABLE_STRATEGIES="z_strategies";
 public static final String TABLE_FLOTTES_DETECTEES="z_flottes_detectees";
 public static final String TABLE_FLOTTE_GALACTIQUE="z_flottes_galactiques";
 public static final String TABLE_FLOTTE_INTRAGALACTIQUE="z_flottes_intragalactiques";
 public static final String TABLE_FLOTTE_COLONISATEUR="z_flottes_colonisateur";
 public static final String TABLE_FLOTTE_MINES="z_flottes_mines";
 public static final String TABLE_FLOTTE_USINES="z_flottes_usines";
 public static final String TABLE_PLANS_CONSTRUCTIBLES="z_plans_constructibles";
 public static final String TABLE_COMMANDANTS_TRANSFERT="z_commandants_transfert";
 public static final String TABLE_MODE_TRANSFERT="z_mode_transfert";
 public static final String TABLE_TECHNOLOGIES_TRANSFERABLES="z_techno_transfert";
 public static final String TABLE_PLANS_TRANSFERABLES="z_plans_transfert";
 public static final String TABLE_DOMAINE_PLAN="z_domaine_plan";
 public static final String TABLE_STRATEGIE_CIBLE="z_strategie_cible";
 public static final String TABLE_STRATEGIE_AGRESSIVITE="z_strategie_agressivite";
 public static final String TABLE_ALLIANCE_SECRETE="z_alliance_secrete";
 public static final String TABLE_ALLIANCE_TYPE="z_alliance_type";
 public static final String TABLE_RENOMMER_SYSTEME="z_systeme_renommer";
 public static final String TABLE_RENOMMER_LIEUTENANT="z_lieutenant_renommer";
 public static final String TABLE_VAISSEAUX="z_vaisseaux";
 public static final String TABLE_COMPOSANTS="z_composants";
 public static final String TABLE_PLANS_CIBLES="z_plans_cibles";
 //Les différentes tables où sont stockées les données nécessaires aux ordres.

 public static final int TAILLE_LOGIN=10;
  //le nombre de lettres du login.

 public static final int MESSAGE_TYPE_COMMANDANT=0;
 public static final int MESSAGE_TYPE_SYSTEME=1;

 //Les différents types de message.

 //Les messages systèmes lors de la gestion d'un univers.
 public static final String MESSAGE_U_00000="Initialisation d'un univers";
 public static final String MESSAGE_U_00001="Création d'une nouvelle galaxie";

 public static final String TEMP="c:/julien/Univers/temp.txt";
 public static final String TEMP2="c:/julien/Univers/temp2.txt";
 //fichier temporaire




}
