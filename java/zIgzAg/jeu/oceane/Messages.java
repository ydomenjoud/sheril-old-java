// v2.01 24/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.01, 24/02/01
 */

public class Messages extends MessagesAbstraits{

 public static final String[] NOMS_GALAXIES={"Voie Lactée","Orion"};

 public static final String[] SORTES_ATMOSPHERES={"idéale","vivifiante","classique","toxique","trés toxique"};
   // les différentes atmosphères.

 public static final String[] MARCHANDISES={"produits alimentaires","matériel agricole","articles de luxe",
               "holofilms et hololivres","alcools et drogues","médicaments","logiciels","robots","composants électroniques",
               "armement et explosifs","carburants","pièces industrielles","métaux précieux","tixium","lixiam","oxole"};
   //les noms des marchandises.

 public static final String[] POLITIQUES={"impôts","commerce","défense","construction","expansion","intégriste",
   "totalitaire","esclavagiste","anti-Humains","anti-ZorgluBs","anti-Golos","anti-Yozdas","anti-Jondoïshis","anti-Nomades",
   "anti-Drewins","anti-Tonks","anti-Golubs","anti-Zooush"};
   //Les différentes formes de politique.

 public static final String[] MISSIONS={"d'espionnage","de sabotage","de vol de technologie","de propagande"};
  //Les différentes sortes de mission pour les services spéciaux

 public static final String[] ETOILES={"étoile bleue","nova","étoile blanche","naine orange","naine bleue","naine rouge"};
   //le nom des différents types d'étoiles.

 public static final String[] CARACTERISTIQUES_ARMES={"vitesse de base","dommage bouclier","dommage coque","dommage au sol",
    "portée","fiabilité"};
   //Le nom des différentes caractéristiques des armes.

 public static final String[] CARAC_SPECIALES_COMPOSANTS={"propulsion","portée scanner de systèmes",
    "propulsion intragalactique","propulsion intergalactique","capacité bouclier magnétique","lanceur de mines",
    "détection de mines","potentiel navire usine","brouillage radar","capacité rayon tracteur","capacité cargo",
    "capacité ville spatiale","capacité colonisation","portée scanner de flottes","capacité dragueur mines"};
   //Le nom des différentes caractéristiques spéciales des composants de vaisseaux.

 public static final String[] CARAC_SPECIALES_BATIMENTS={"construction de vaisseaux","extraction de minerai",
  "retraitement de minerai","facilités de construction","présence population non nécessaire",
  "capacité réparation vaisseaux","bouclier magnétique","capacité production marchandises","portée radar",
  "capacité extraction avancée"};
   //Le nom des différentes caractéristiques spéciales des batiments.

 public static final String[] DOMAINES_BUDGET={"recherche","services spéciaux","contre-espionnage"};
   //Les différentes possibilités pour le choix du budget d'un système.

 public static final String[] RACES={"humain","zorglub","golo","yozda","jondoïshi",
                                     "nomade","drewin","tonk","golub","zooush",};
   //Les différentes races.

 public static final String[] COMPETENCES_LEADER={"maîtrise de la vitesse","maîtrise de l'attaque",
  "maîtrise de la défense","maîtrise du moral","maîtrise du marchandage","inspiration fanatique","entretien flotte",
  "entretien héros","immortalité","voyageur","voyage intragalactique","voyage intergalactique","maîtrise du savoir",
  "entretien système","maîtrise de la finance"};
   //Description des différentes compétences spéciales possibles des leaders(héros ou gouverneurs).

 public static final String[] CARACTERISTIQUES_LEADER={"vitesse","attaque","défense","moral","marchandage"};
   //Description des différentes caractéristques des leaders(héros ou gouverneurs).

 public static final String[] EXPERIENCE={"conscrit","réserviste","soldat","vétéran","élite"};
   //Les différents niveaux d'expérience pour les équipages de vaisseaux.

 public static final String[] MORAL={"suicidaire","défaitiste","calamiteux","mauvais","médiocre","moyen","assez bon",
         "bon","trés bon","excellent","extraordinaire","suprême"};
   //Les différents types de moral pour l'équipage d'un vaisseau

 public static final String[] PUISSANCE={"insignifiante","ridicule","trés petite","petite","moyenne","assez grande",
         "grande","trés grande","gigantesque","titanesque","inimaginable"};
   //Les différents types de grandeur pour une flotte

 public static final String[] DOMMAGES={"neuf","quasi-neuf","moyennement endommagé","sérieusement endommagé",
         "catastrophique","en perdition"};
  //Les différents niveaux de dommages pour une flotte

 public static final String[] REPUTATION={"sanguinaire","pirate","hors-la-loi","neutre","honnête","respecté"};
  //Les différents status possibles en fonction de la réputation.

 public static final String[] GRADE={"chef de bande","hobereau","chevalier","baron","comte","duc","roi","empereur"};
  //Les différents grades possibles en fonction du nombre de planètes possédées.

 public static final String[] RELATIONS={"guerre ouverte","haineuse","catastrophique","détestable","mauvaise",
         "neutre","loyale","bonne","amicale","alliance","amour fou"};
  //Les différents types de relations entre races.

 public static final String[] DIRECTIVES={"attitude neutre","attaque du système","attaque préventive",
   "attaquer toute flotte rencontrée","pillage du système","attaque de la planète n°","pillage de planète n°",
   "éradication de la planète n°","attaque des flottes du commandant "};
  //différentes directives.

 public static final String[] DOMAINES_PLAN_DE_VAISSEAU={"public","privé"};
  //domaine public ou privé...

 public static final String[] CHAMPS_BUDGET={"centaures du tour précedent","revenus des systèmes","revenus des alliances",
  "taxe d'entrée dans alliance","dons centaures","vente de marchandises","licenciement lieutenant",
  "politiques d'extermination","perception de royalties","villes spatiales","pillage de planète","revenus divers",
  "vente flotte","","","","","","",
  "total des recettes","création alliance",
  "adhésion alliance","dons centaures","dons technologie","dons système","don planète","terraformation",
  "achat de marchandises","achat de lieutenant","changement de capitale","changement de politique",
  "prêt flotte","don plan de vaisseau","création plan de vaisseau","don stratégie de combat","réalisation construction",
  "entretien flotte","entretien système","budget technologique","budget services spéciaux",
  "budget contre-espionnage","entretien des lieutenants","entretien des technologies","réparation des flottes",
  "achat flotte","divers","franchissement porte intra-galactique",
  "total des dépenses","total disponible"};
  //les différents champs de budget.

 public static final String[] TYPE_ALLIANCE={"démocratique","autocratique","anarchique"};
  //les différents types d'alliance possible.

 public static final String[] ALLIANCE_SECRET={"secrète","non-secrète"};
  //les adjectifs secret et non-secret pour les alliances.

 public static final String[] MODE_TRANSFERT={"normal","discret","anonyme"};
  //les différents modes pour les transferts.

 public static final String[] STRATEGIE_CIBLE={"aucune","chasseur","bombardier","cargo"};
  //les différentes cible préférentielles dans les stratégies.

 public static final String[] STRATEGIE_AGRESSIVITE={"pillage","fuyard","prudent","normal","combatif","rage"};
  //les différents types d'agressivité dans les stratégies.

 public static final String[] ORDRES={"Adhérer à une alliance","Valider l'adhésion à une alliance",
  "Voter pour élire le dirigeant d'une alliance","Voter pour exclure un membre d'une alliance","Quitter une alliance",
  "Signer un pacte","Rompre un pacte","Affecter un héros","Affecter un gouverneur","Licencier un lieutenant",
  "Enroler un lieutenant","Changer la capitale","Orienter les recherches","Services spéciaux",
  "Annuler construction","Construction","Programmer des constructions","Déprogrammer des constructions",
  "Modifier une politique","Modifier un budget","Modifier la taxation d'un système","Modifier la taxation d'une planète",
  "Terraformer un système","Terraformer une planète","Détruire des bâtiments","Décharger des cargos","Charger des cargos",
  "Coloniser","Larguer des mines","Construire à partir d'une flotte","Utiliser une porte galactique","Diviser une flotte",
  "Déplacer une flotte","Pister une flotte","Fusionner une flotte","Donner des centaures",
  "Donner une technologie","Donner un système","Donner une planète","Préter une flotte",
  "Vendre une flotte","Donner une stratégie de combat",
  "Donner un plan de vaisseau","Créer un plan de vaisseau","Créer une alliance","Créer une stratégie de combat",
  "Abandonner une technologie","Modifier le taux de taxation de vos postes commerciaux",
  "Renommer un système","Renommer une planète","Renommer une flotte","Renommer un lieutenant","Renommer une alliance",
  "Site d'un commandant","Site d'une alliance","Ecrire un article"};
  //les différents ordres possibles.

 public static final String[] OUI_NON={"oui","non"};
  //oui ou non ?!

 public static final String ALLIANCE_CONCEPTEUR_INCONNU="inconnu";
  //le nom pour les concepteurs d'alliances inconnus.

 public static final String PLANETE_MARCHANDISE_INCULTE="aucune";
  //Si la planète ne produit naturellement aucune marchandise.

 public static final String NON_EXISTENCE_DIRIGEANT_ALLIANCE="aucun";
  //le nom pour les dirigeants des alliances non dirigées..

 public static final String PLAN_DE_VAISSEAU_CONCEPTEUR_INCONNU="inconnu";
  //le nom pour les concepteurs de plans de vaisseaux inconnus.

 public static final String PLAN_DE_VAISSEAU_MARQUE_INCONNUE="inconnue";
  //le nom pour les marques de plans de vaisseaux inconnues.

 public static final String MINERAI="minerai";
  //le nom pour decrire le minerai.

 public static final String DE_TYPE=" de type ";
  //Sert à faite la liaison pour le nom comple de la technologie. Exemple: mine de type I.

 public static final String POSTE_COMMERCIAL="poste commercial du commandant ";
  //le nom pour décrire un poste commercial;

 public static final String RESERVE_LEADER="en réserve";
  //la description de la position d'un leader "en réserve";

 public static final String POSITION_GOUVERNEUR="gouverneur du système ";
  //la description de la position d'un gouverneur.

 public static final String POSITION_HEROS="commandant de la flotte numéro ";
  //la description de la position d'un héros.

 public static final String DESCRIPTION_FLOTTE1="flotte numéro ";
 public static final String DESCRIPTION_FLOTTE2=" du commandant ";
  //les phrases nécessaires pour la description d'une flotte.

 public static final String TECHNOLOGIE_AUCUN_PARENT="aucune technologie nécessaire";
  //Pour les technologies "de base".

 public static final String DENOMINATION_FLOTTE_DE_DEPART="Flotte de départ";
  //Le nom de la flotte de départ.

 public static final String RETOUR_DE_PRET="Retour de prêt";
  //Le nom de la flotte de retour de prêt.

 public static final String SECTEUR="secteur ";
  //Le nom pour les secteurs de galaxie.

 public static final String RETOUR_SITE="Retour vers le site d'Océane";
  //La phrase clé du programme !

 }



