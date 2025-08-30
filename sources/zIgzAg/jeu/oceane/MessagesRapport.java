// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;




public class MessagesRapport extends MessagesAbstraits{
 public static int tour=Univers.getTour()+1;

 public static final String[] INFO_GENERALES={"Informations générales <font size=2 color=\"#FFB400\">("+Utile.getDateRapport()+")</font>","Numéro du tour","Numéro du Commandant","Nom",
   "Race","Puissance","Nombre de planètes contrôlées","Grade","Réputation","Statut","Nombre de transferts"};

 public static final String[] VOTRE_PEUPLE={"Votre Peuple","Type","Nombre","Max","Colons"};

 public static final String[] RESUME_SYSTEMES={"Résumé de vos systémes","&nbsp;","Position","Nom","Pop","Pop Max",
                                               "Planètes",
                                               "Taxe","Stabilité","PDC","R.M.","Budget T.","Budget S.","Budget C.",
                                               "Propriétaire(s)","Gouverneur","Politique","colons"};

 public static final String[] SYSTEMES_GENERAL={"Rapport des Systèmes","Capitale : ","Aucune"};

 public static final String[] SYSTEME={"Nombre de planètes : ","(voir planètes)","Populations","Population max",
   "Augmentation","Population totale","Population totale max","Augmentation moyenne","Ressources","Mines",
   "Stock minerai","Points de construction","Caractéristiques","Taux d'impôts","Stabilité","Politique","Revenu",
   "Révoltes","Terraformation","Budget","Technologie","Services spéciaux","Contre-espionnage","Constructions en cours",
   "Type","Nombre","Points constru. nécessaires","Pour la planète","Non précisé","équipement","Postes commerciaux",
   "Gouverneur","Programmation construction"};

 public static final String[] POSTES_COMMERCIAUX={"Rapport de vos postes commerciaux","Production","Stock","Prix",
                                                "Rapport des postes commerciaux étrangers"};

 public static final String[] DETAIL_SYSTEME={"Détail des planètes du système ","Propriétaire : ",
   "Caractéristiques de la planète","Atmosphére","Marchandise Produite",
   "Radiations","Température","Gravité","Caractéristiques d'exploitation",
   "Révolte","NON DISPONIBLES","Production de minerai"};

 public static final String[] FLOTTES={"Rapport des flottes"," (Pour voir le détail des flottes)","Nom de la flotte",
  "Numéro de la flotte","Position","Direction","Héros présent","Directive","Puissance","Attaque spatiale",
  "Attaque planétaire","Vitesse","état","Expérience","Moral","Postes commerciaux survolés",
  "Transport n°","Type transport","Contenu","état de la soute",
  "Capacité maximale ville spatiale","Population présente","Nombre", "Production","Capacité de soute totale"};


 public static final String[] RESUME_FLOTTES={"Résumé de vos flottes","Nom-Numéro","Position","Direction","Directive",
  "Vitesse","Force spatiale","Force planétaire","état","Cargaison","capa","Héros","Vso"};

 public static final String[] DETAIL_FLOTTES={"Description de vos flottes","Nom de la flotte","Numéro de la flotte","Nom",
  "Type de vaisseau","Dommages","Expérience","Moral","Composants détruits","équipage"};

 public static final String[] RESUME_TECHNOLOGIES={"Rapport des technologies","Technologies connues",
  "Technologies pouvant être cherchées","Bâtiments",
  "Nom","Prix","Points de construction","Minerai","Produits nécessaires","Encombrement transport",
  "Trouvée à partir de",
  "Composants de vaisseaux","Nom","Prix","Minerai","Nombre de cases","Produits nécessaires","Trouvée à partir de",
  "Autres","Nom","Description","Trouvée à partir de",
  "Les technologies publiques sont en <i><FONT color=\"#00F1AF\">vert</FONT></i>.",
  "Recherches actuelles","Technologie","Pourcentage du budget technologique affecté","Points de recherche"};

 public static final String[] TECHNOLOGIES={"Détail des technologies","Technologies connues",
  "Technologies pouvant être cherchées",
  "Nom","Prix","Minerai","Points de construction","Produits nécessaires","Points de structure",
  "Prix","Minerai","Nombre de cases",
  "Produits nécessaires","Vitesse","Dommages bouclier","Dommages coque","Dommages au sol","Portée","Fiabilité",
  "Pourcentage de chance de toucher les différents types de coque","Caractéristique(s) spéciale(s)"};

 public static final String[] PLANS_DE_VAISSEAUX={"Plans de vaisseaux","Nom","Concepteur","Marque","T.C.","Taille","Vitesse",
   "Points de construction nécessaires","Coût en centaures","Côut en minerai","Matériaux spéciaux nécessaires",
   "Capacité d'attaque spatiale","Capacité d'attaque planétaire","Soutes à cargo",
   "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Composants&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
   "Royalties","Domaine"};

 public static final String[] ALLIANCES={"Alliances","Nom","Type","Concepteur","Dirigeant","Droits d'entrée","Secrête",
   "Membres de l'Alliance"};

 public static final String[] ALLIANCES_DETAIL={"Membres de l'alliance ","Nom","Adresse Electronique","Race","Puissance",
   "Nombre de planètes","Grade","Réputation","Statut"};

 public static final String[] LEADERS={"Nom","Position","Niveau","Race","Vitesse","Attaque","Défense","Moral",
  "Marchandage","Compétences","Expérience","Valeur de base"};

 public static final String[] STRATEGIES={"Stratégies de combat","Nom","Agressivité","Cible prioritaire",
 "Type de vaisseau","Position","Taille visée 1","TV 2","TV 3","TV 4","TV 5","TV 6","TV 7","TV 8","TV 9","TV 10"};

 public static final String[] DETECTION_FLOTTES={"Flottes étrangères détectées par vos radars",
  "Position","Nom","Nombre de vaisseaux","Description","Puissance estimée"};
 public static final String[] DETECTION_SYSTEMES={"Systèmes étrangers détectés par vos radars",
  "Position","Nom","Population","Nombre de planètes","Propriétaire(s)"};

 public static final String[] MESSAGES={"Erreurs détectées lors de la réception des ordres",
  "Résultats des actions importantes","Déroulement des combats","Ordres passés"};

 public static final String[] RAPPORT_COMBAT={"Attaquant","Attaqué","Type de combat","Flotte attaquante",
  "Flotte ou système attaquée (éventuellement planètes prises ou pillées)"};

 public static final String RAPPORT_FINANCIER="Rapport financier";
 public static final String HEROS="Heros sous vos ordres";
 public static final String GOUVERNEURS="Gouverneurs sous vos ordres";
 public static final String PACTE_NON_AGRESSION="Pactes de non-agression avec les commandants suivants";

 public static final String TITRE_RAPPORT="Rapport du commandant ";
 public static final String TITRE_DETAIL_SYSTEME="Détail du système ";
 public static final String TITRE_DETAIL_FLOTTES="Détail des flottes";
 public static final String TITRE_DETAIL_TECHNOLOGIES="Détail des technologies";
 public static final String TITRE_DETAIL_COMBAT="Détail des combats";
 public static final String RETOUR_PRINCIPAL="Pour revenir au rapport principal";

 //Viennent ensuite les messages pour la production des ordres.

 public static final String[] CHAPITRES_ORDRES={"Diplomatie et recherche","Gestion des systèmes","Déplacement",
  "Dons et prêts","Divers"};

 public static final String[] DEROULEMENT_EVENEMENTS={
  "Résolution des collisions entre les astéroïdes, les mines anti-matières,etc. et les flottes",
  "Résolution des votes au sein des alliances","Résolution des enchères sur les lieutenants",
  "- Résolution des combats<BR>- Perception des revenus<BR>- Gestion des systêmes et des constructions"+
  "<BR>- Finalisation du budget"};

 public static final String ORDRE_PRINCIPAL="Page principale des ordres";

 public static final String TITRE_ORDRES="Passage des ordres du commandant ";

 //Et pour le combat.

 public static final String[] COMBAT_FLOTTE={"Tour {0} du combat","Flotte {0} du commandant {1}","Type de vaisseau",
  "Nombre","Dommages encaissés","Dommages infligés"};
 public static final String[] COMBAT_PLANETE={"Planete {0} du commandant {1}","Milices",
  "Nombre","Dommages encaissés","Bâtiments planétaires"};

 //Et pour les stats.

 public static final String[] STATS_PUISSANCE={"Statistiques de puissance des commandants du tour "+tour,"Place","Nom","Numéro",
   "Race","Puissance"};
 public static final String[] STATS_REPUTATION={"Réputation des commandants du tour "+tour,"Place","Nom","Numéro",
   "Race","Réputation","Statut"};
 public static final String[] STATS_PLANETES={"Possessions des commandants du tour "+tour,"Place","Nom","Numéro",
   "Race","Nombre de planètes","Grade"};
 public static final String[] STATS_TAUX_POSTE={"Taxation des postes commerciaux du tour "+tour,
  "Place","Nom","Numéro","Race","Taxation"};
 public static final String[] STATS_CENTAURES={"Etats des comptes à la Banque Galactique du tour "+tour,
  "Place","Nom","Numéro","Race","Centaures"};
 public static final String[] STATS_POPULATION={"Recensement (en millions d'habitants) du tour "+tour,
  "Place","Nom","Numéro","Race","Population totale"};
 public static final String[] STATS_FLOTTES={"Liste des plus grosses flottes du tour "+tour,
  "Place","Nom","Numéro","Race","Puissance"};
 public static final String[] STATS_VAISSEAUX={"Nombre d'exemplaires en circulation du tour "+tour,
  "Place","Nom","Nombre"};
 public static final String[] STATS_ALLIANCES={"Alliances non-secrêtes du tour "+tour,"Place","Nom","Concepteur","Adhérents","Membres","Type",
  "Site web"};
 public static final String[] STATS_ENCHERES={"Lieutenants disponibles du tour "+tour,"Heros en vente "+tour,"Gouverneurs en vente "+tour};
 public static final String[] STATS_HEROs={"Classement des héros du tour "+tour};
 public static final String[] STATS_VAISSEAUX_PUBLICS={"Vaisseaux publics du tour "+tour};
 public static final String[] STATS_UNIVERS={"Statistiques générales sur l'univers connu du tour "+tour,"Population","Marchandises",
  "Relations entre races","Détail"};
 public static final String[] STATS_UNIVERS_POPULATION={"Population (en millions) du tour "+tour,"Type","Nombre","Total"};
 public static final String[] STATS_UNIVERS_POSTE={"Marchandises du tour "+tour,"Type","Nombre en circulation","Prix moyen constaté"};
 public static final String[] STATS_UNIVERS_RELATIONS={"Relations entres les différentes races du tour "+tour};
 public static final String[] STATS_UNIVERS_SITES={"Sites des commandants du tour "+tour,"Sites des alliances","Sites d'Océane"};




 }



