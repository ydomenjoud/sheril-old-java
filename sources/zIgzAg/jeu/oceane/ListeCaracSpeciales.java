// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;



public interface ListeCaracSpeciales{

 public static final int[][] mystII={{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,100},{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,100}};
 public static final int[][] mystIII={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,100},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,100}};
 public static final int[][] mystIV={{Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE,1000}};
 public static final int[][] mystV={{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,2004}};
 public static final int[][] mystVI={{Const.COMPOSANT_CAPACITE_PROPULSION,40},{Const.COMPOSANT_CAPACITE_PROPULSION_INTRAGALACTIQUE,1},{Const.COMPOSANT_CAPACITE_PROPULSION_INTERGALACTIQUE,1}};
 public static final int[][] mystVII={{Const.COMPOSANT_CAPACITE_DETECTION_MINES,100}};
 public static final int[][] mystVIII={{Const.COMPOSANT_BROUILLAGE_RADAR,100}};
 public static final int[][] mystIX={{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE,100000}};
 public static final int[][] mystX={{Const.BATIMENT_CAPACITE_BOUCLIER,1},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};


 public static final int[][] moteurI={{Const.COMPOSANT_CAPACITE_PROPULSION,1}};
 public static final int[][] moteurII={{Const.COMPOSANT_CAPACITE_PROPULSION,2}};
 public static final int[][] moteurIII={{Const.COMPOSANT_CAPACITE_PROPULSION,3}};
 public static final int[][] moteurIV={{Const.COMPOSANT_CAPACITE_PROPULSION,4}};
 public static final int[][] moteurV={{Const.COMPOSANT_CAPACITE_PROPULSION,5}};
 public static final int[][] moteurVI={{Const.COMPOSANT_CAPACITE_PROPULSION,7}};
 public static final int[][] moteurVII={{Const.COMPOSANT_CAPACITE_PROPULSION,9}};
 public static final int[][] moteurVIII={{Const.COMPOSANT_CAPACITE_PROPULSION,12}};
 public static final int[][] moteurIX={{Const.COMPOSANT_CAPACITE_PROPULSION,14}};
 public static final int[][] moteurX={{Const.COMPOSANT_CAPACITE_PROPULSION,16}};

 public static final int[][] intraI={{Const.COMPOSANT_CAPACITE_PROPULSION_INTRAGALACTIQUE,1}};

 public static final int[][] interI={{Const.COMPOSANT_CAPACITE_PROPULSION_INTERGALACTIQUE,1}};


 public static final int[][] bouclierI={{Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE,2}};
 public static final int[][] bouclierII={{Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE,4}};
 public static final int[][] bouclierIII={{Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE,6}};
 public static final int[][] bouclierIV={{Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE,8}};
 public static final int[][] bouclierV={{Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE,10}};
 public static final int[][] bouclierVI={{Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE,12}};
 public static final int[][] bouclierVII={{Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE,16}};
 public static final int[][] bouclierVIII={{Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE,20}};
 public static final int[][] bouclierIX={{Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE,26}};
 public static final int[][] bouclierX={{Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE,40}};

// dÃ©but armes dÃ©fense

 public static final int[][] absorbI={{Const.COMPOSANT_CAPACITE_ABSORBTION,1}};
 public static final int[][] absorbII={{Const.COMPOSANT_CAPACITE_ABSORBTION,2}};
 public static final int[][] absorbIII={{Const.COMPOSANT_CAPACITE_ABSORBTION,3}};
 public static final int[][] absorbIV={{Const.COMPOSANT_CAPACITE_ABSORBTION,5}};
 public static final int[][] absorbV={{Const.COMPOSANT_CAPACITE_ABSORBTION,7}};
 public static final int[][] absorbVI={{Const.COMPOSANT_CAPACITE_ABSORBTION,9}};
 public static final int[][] absorbVII={{Const.COMPOSANT_CAPACITE_ABSORBTION,12}};
 public static final int[][] absorbVIII={{Const.COMPOSANT_CAPACITE_ABSORBTION,15}};
 public static final int[][] absorbIX={{Const.COMPOSANT_CAPACITE_ABSORBTION,19}};
 public static final int[][] absorbX={{Const.COMPOSANT_CAPACITE_ABSORBTION,25}};
// fin armes de dÃ©fense


// fin batiments gestion

 public static final int[][] lmineI={{Const.COMPOSANT_CAPACITE_LANCEUR_MINES_CLASSIQUES,10}};
 public static final int[][] lmineII={{Const.COMPOSANT_CAPACITE_LANCEUR_MINES_CLASSIQUES,25}};
 public static final int[][] lmineIII={{Const.COMPOSANT_CAPACITE_LANCEUR_MINES_CLASSIQUES,40}};
 public static final int[][] lmineIV={{Const.COMPOSANT_CAPACITE_LANCEUR_MINES_CLASSIQUES,80}};
 public static final int[][] lmineV={{Const.COMPOSANT_CAPACITE_LANCEUR_MINES_CLASSIQUES,150}};
 public static final int[][] lmineVI={{Const.COMPOSANT_CAPACITE_LANCEUR_MINES_CLASSIQUES,300}};
 public static final int[][] lmineVII={{Const.COMPOSANT_CAPACITE_LANCEUR_MINES_CLASSIQUES,600}};
 public static final int[][] lmineVIII={{Const.COMPOSANT_CAPACITE_LANCEUR_MINES_CLASSIQUES,1200}};
 public static final int[][] lmineIX={{Const.COMPOSANT_CAPACITE_LANCEUR_MINES_CLASSIQUES,2500}};
 public static final int[][] lmineX={{Const.COMPOSANT_CAPACITE_LANCEUR_MINES_CLASSIQUES,5000}};

 public static final int[][] dmineI={{Const.COMPOSANT_CAPACITE_DETECTION_MINES,10}};
 public static final int[][] dmineII={{Const.COMPOSANT_CAPACITE_DETECTION_MINES,20}};
 public static final int[][] dmineIII={{Const.COMPOSANT_CAPACITE_DETECTION_MINES,30}};
 public static final int[][] dmineIV={{Const.COMPOSANT_CAPACITE_DETECTION_MINES,40}};
 public static final int[][] dmineV={{Const.COMPOSANT_CAPACITE_DETECTION_MINES,50}};
 public static final int[][] dmineVI={{Const.COMPOSANT_CAPACITE_DETECTION_MINES,60}};
 public static final int[][] dmineVII={{Const.COMPOSANT_CAPACITE_DETECTION_MINES,70}};
 public static final int[][] dmineVIII={{Const.COMPOSANT_CAPACITE_DETECTION_MINES,80}};
 public static final int[][] dmineIX={{Const.COMPOSANT_CAPACITE_DETECTION_MINES,90}};
 public static final int[][] dmineX={{Const.COMPOSANT_CAPACITE_DETECTION_MINES,100}};

 public static final int[][] mconstruI={{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,2}};
 public static final int[][] mconstruII={{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,4}};
 public static final int[][] mconstruIII={{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,8}};
 public static final int[][] mconstruIV={{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,16}};
 public static final int[][] mconstruV={{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,32}};
 public static final int[][] mconstruVI={{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,64}};
 public static final int[][] mconstruVII={{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,128}};

 public static final int[][] bscanI={{Const.COMPOSANT_BROUILLAGE_RADAR,10}};
 public static final int[][] bscanII={{Const.COMPOSANT_BROUILLAGE_RADAR,20}};
 public static final int[][] bscanIII={{Const.COMPOSANT_BROUILLAGE_RADAR,30}};
 public static final int[][] bscanIV={{Const.COMPOSANT_BROUILLAGE_RADAR,40}};
 public static final int[][] bscanV={{Const.COMPOSANT_BROUILLAGE_RADAR,50}};
 public static final int[][] bscanVI={{Const.COMPOSANT_BROUILLAGE_RADAR,58}};
 public static final int[][] bscanVII={{Const.COMPOSANT_BROUILLAGE_RADAR,65}};
 public static final int[][] bscanVIII={{Const.COMPOSANT_BROUILLAGE_RADAR,70}};
 public static final int[][] bscanIX={{Const.COMPOSANT_BROUILLAGE_RADAR,75}};
 public static final int[][] bscanX={{Const.COMPOSANT_BROUILLAGE_RADAR,85}};

 public static final int[][] tractI={{Const.COMPOSANT_CAPACITE_RAYON_TRACTEUR,1}};
    
    public static final int[][] cargoI  = null;
 public static final int[][] cargoII = null;
 public static final int[][] cargoIII= null;
 public static final int[][] cargoIV = null;
 public static final int[][] cargoV  = null;
 public static final int[][] cargoVI = null;
 public static final int[][] cargoVII= null;
 public static final int[][] cargoVIII= null;
 public static final int[][] cargoIX  = null;
 public static final int[][] cargoX   = null;   

 public static final int[][] villeI={{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE,    2000}};
 public static final int[][] villeII={{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE,   3500}};
 public static final int[][] villeIII={{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE,  4800}};
 public static final int[][] villeIV={{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE,   6000}};
 public static final int[][] villeV={{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE,    7200}};
 public static final int[][] villeVI={{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE,  8300}};
 public static final int[][] villeVII={{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE, 9400}};

 /*
 public static final int[][] villeVIII={{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE,10500}};
 public static final int[][] villeIX={{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE,12000}};
 public static final int[][] villeX={{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE,15000}};
    */

 public static final int[][] robocoI={{Const.COMPOSANT_CAPACITE_COLONISATEUR,1}};
  
 public static final int[][] scanI={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,1},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,1}};
 public static final int[][] scanII={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,2},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,2}};
 public static final int[][] scanIII={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,3},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,3}};
 public static final int[][] scanIV={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,4},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,4}};
 public static final int[][] scanV={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,5},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,5}};
 public static final int[][] scanVI={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,6},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,6}};
 public static final int[][] scanVII={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,7},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,7}};
 public static final int[][] scanVIII={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,8},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,8}};
 public static final int[][] scanIX={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,9},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,9}};
 public static final int[][] scanX={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,10},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,10}};
 
    /*
 public static final int[][] hscanI={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,2},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,2}};
 public static final int[][] hscanII={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,2},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,2}};
 public static final int[][] hscanIII={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,3},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,3}};
 public static final int[][] hscanIV={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,4},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,4}};
 public static final int[][] hscanV={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,5},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,5}};
 public static final int[][] hscanVI={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,6},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,6}};
 public static final int[][] hscanVII={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,7},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,7}};
 public static final int[][] hscanVIII={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,8},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,8}};
 public static final int[][] hscanIX={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,9},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,9}};
 public static final int[][] hscanX={{Const.COMPOSANT_PORTEE_SCANNER_FLOTTE,10},{Const.COMPOSANT_PORTEE_SCANNER_SYSTEME,10}};
    */

 public static final int[][] draminI={{Const.COMPOSANT_DRAGUEUR_MINES,1}};
 public static final int[][] draminII={{Const.COMPOSANT_DRAGUEUR_MINES,2}};
 public static final int[][] draminIII={{Const.COMPOSANT_DRAGUEUR_MINES,4}};
 public static final int[][] draminIV={{Const.COMPOSANT_DRAGUEUR_MINES,8}};
 public static final int[][] draminV={{Const.COMPOSANT_DRAGUEUR_MINES,16}};
 public static final int[][] draminVI={{Const.COMPOSANT_DRAGUEUR_MINES,40}};
 public static final int[][] draminVII={{Const.COMPOSANT_DRAGUEUR_MINES,80}};
 public static final int[][] draminVIII={{Const.COMPOSANT_DRAGUEUR_MINES,160}};
 public static final int[][] draminIX={{Const.COMPOSANT_DRAGUEUR_MINES,320}};
 public static final int[][] draminX={{Const.COMPOSANT_DRAGUEUR_MINES,650}};

  //les batiments -->

 public static final int[][] chantierI={
     {Const.BATIMENT_CAPACITE_PRODUCTION_VAISSEAU,1},
     {Const.BATIMENT_CAPACITE_REPARATION_VAISSEAU,20},
     {Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};

 public static final int[][] mineI   = {  {Const.BATIMENT_CAPACITE_EXTRACTION_MINERAI,1}, {Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}  };
 public static final int[][] mineII  = {  {Const.BATIMENT_CAPACITE_EXTRACTION_MINERAI,2}, {Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}  };
 public static final int[][] mineIII = {  {Const.BATIMENT_CAPACITE_EXTRACTION_MINERAI,3}, {Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}  };
 public static final int[][] mineIV  = {  {Const.BATIMENT_CAPACITE_EXTRACTION_MINERAI,4}, {Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}  };
 public static final int[][] mineV   = {  {Const.BATIMENT_CAPACITE_EXTRACTION_MINERAI,5}, {Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}  };

 public static final int[][] retraiteI={{Const.BATIMENT_CAPACITE_RECYCLAGE_MINERAI,1}};

 public static final int[][] optpI    ={{Const.BATIMENT_GAIN_POINTS_CONSTRUCTION,1}};
 public static final int[][] optpII   ={{Const.BATIMENT_GAIN_POINTS_CONSTRUCTION,2}};
 public static final int[][] optpIII  ={{Const.BATIMENT_GAIN_POINTS_CONSTRUCTION,3}};
 public static final int[][] optpIV   ={{Const.BATIMENT_GAIN_POINTS_CONSTRUCTION,4}};
 public static final int[][] optpV    ={{Const.BATIMENT_GAIN_POINTS_CONSTRUCTION,5}};
 public static final int[][] optpVI   ={{Const.BATIMENT_GAIN_POINTS_CONSTRUCTION,6}};
 public static final int[][] optpVII  ={{Const.BATIMENT_GAIN_POINTS_CONSTRUCTION,7}};
 public static final int[][] optpVIII ={{Const.BATIMENT_GAIN_POINTS_CONSTRUCTION,8}};
 public static final int[][] optpIX   ={{Const.BATIMENT_GAIN_POINTS_CONSTRUCTION,9}};
 public static final int[][] optpX    ={{Const.BATIMENT_GAIN_POINTS_CONSTRUCTION,10}};

 public static final int[][] optsI={{Const.BATIMENT_GAIN_POINTS_CONSTRUCTION,2},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};

 public static final int[][] repareI   = { {Const.BATIMENT_CAPACITE_REPARATION_VAISSEAU,  100} };
 public static final int[][] repareII  = { {Const.BATIMENT_CAPACITE_REPARATION_VAISSEAU,  400} };
 public static final int[][] repareIII = { {Const.BATIMENT_CAPACITE_REPARATION_VAISSEAU, 1350} };
 public static final int[][] repareIV  = { {Const.BATIMENT_CAPACITE_REPARATION_VAISSEAU, 3200} };
 public static final int[][] repareV   = { {Const.BATIMENT_CAPACITE_REPARATION_VAISSEAU, 6250} };

 public static final int[][] boucplaI={{Const.BATIMENT_CAPACITE_BOUCLIER,1},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] boucplaII={{Const.BATIMENT_CAPACITE_BOUCLIER,2},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] boucplaIII={{Const.BATIMENT_CAPACITE_BOUCLIER,3},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] boucplaIV={{Const.BATIMENT_CAPACITE_BOUCLIER,4},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] boucplaV={{Const.BATIMENT_CAPACITE_BOUCLIER,5},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] boucplaVI={{Const.BATIMENT_CAPACITE_BOUCLIER,6},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] boucplaVII={{Const.BATIMENT_CAPACITE_BOUCLIER,7},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] boucplaVIII={{Const.BATIMENT_CAPACITE_BOUCLIER,8},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] boucplaIX={{Const.BATIMENT_CAPACITE_BOUCLIER,9},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] boucplaX={{Const.BATIMENT_CAPACITE_BOUCLIER,10},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};

 public static final int[][] radarI={{Const.BATIMENT_PORTEE_RADAR,1},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] radarII={{Const.BATIMENT_PORTEE_RADAR,2},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] radarIII={{Const.BATIMENT_PORTEE_RADAR,3},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] radarIV={{Const.BATIMENT_PORTEE_RADAR,4},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] radarV={{Const.BATIMENT_PORTEE_RADAR,5},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] radarVI={{Const.BATIMENT_PORTEE_RADAR,6},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] radarVII={{Const.BATIMENT_PORTEE_RADAR,7},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] radarVIII={{Const.BATIMENT_PORTEE_RADAR,8},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] radarIX={{Const.BATIMENT_PORTEE_RADAR,9},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};
 public static final int[][] radarX={{Const.BATIMENT_PORTEE_RADAR,10},{Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE,1}};

 public static final int[][] extraI={{Const.BATIMENT_CAPACITE_EXTRACTION_AVANCE,1}};
 public static final int[][] extraII={{Const.BATIMENT_CAPACITE_EXTRACTION_AVANCE,2}};
 public static final int[][] extraIII={{Const.BATIMENT_CAPACITE_EXTRACTION_AVANCE,3}};
 public static final int[][] extraIV={{Const.BATIMENT_CAPACITE_EXTRACTION_AVANCE,4}};
 public static final int[][] extraV={{Const.BATIMENT_CAPACITE_EXTRACTION_AVANCE,5}};

    // Usines
 public static final int[][] agroI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,1}};
 public static final int[][] agroII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,1}};
 public static final int[][] agroIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,1}};
 public static final int[][] agroIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,1}};
 public static final int[][] agroV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,1}};
 public static final int[][] agroVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,1}};
 public static final int[][] agroVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,1}};
 public static final int[][] agroVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,1}};
 public static final int[][] agroIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,1}};
 public static final int[][] agroX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,1}};

 public static final int[][] agricoleI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,2}}; 
 public static final int[][] agricoleII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,2}};
 public static final int[][] agricoleIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,2}};
 public static final int[][] agricoleIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,2}};
 public static final int[][] agricoleV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,2}};
 public static final int[][] agricoleVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,2}};
 public static final int[][] agricoleVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,2}};
 public static final int[][] agricoleVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,2}};
 public static final int[][] agricoleIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,2}};
 public static final int[][] agricoleX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,2}};

 public static final int[][] modeI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,3}};
 public static final int[][] modeII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,3}};
 public static final int[][] modeIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,3}};
 public static final int[][] modeIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,3}};
 public static final int[][] modeV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,3}};
 public static final int[][] modeVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,3}};
 public static final int[][] modeVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,3}};
 public static final int[][] modeVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,3}};
 public static final int[][] modeIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,3}};
 public static final int[][] modeX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,3}};
 
 public static final int[][] culturelI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,4}};
 public static final int[][] culturelII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,4}};
 public static final int[][] culturelIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,4}};
 public static final int[][] culturelIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,4}};
 public static final int[][] culturelV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,4}};
 public static final int[][] culturelVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,4}};
 public static final int[][] culturelVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,4}};
 public static final int[][] culturelVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,4}};
 public static final int[][] culturelIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,4}};
 public static final int[][] culturelX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,4}};

 public static final int[][] transfoI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,5}};
 public static final int[][] transfoII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,5}};
 public static final int[][] transfoIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,5}};
 public static final int[][] transfoIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,5}};
 public static final int[][] transfoV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,5}};
 public static final int[][] transfoVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,5}};
 public static final int[][] transfoVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,5}};
 public static final int[][] transfoVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,5}};
 public static final int[][] transfoIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,5}};
 public static final int[][] transfoX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,5}};

 public static final int[][] pharmaI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,6}};
 public static final int[][] pharmaII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,6}};
 public static final int[][] pharmaIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,6}};
 public static final int[][] pharmaIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,6}};
 public static final int[][] pharmaV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,6}};
 public static final int[][] pharmaVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,6}};
 public static final int[][] pharmaVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,6}};
 public static final int[][] pharmaVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,6}};
 public static final int[][] pharmaIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,6}};
 public static final int[][] pharmaX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,6}};

 public static final int[][] infoI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,7}};
 public static final int[][] infoII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,7}};
 public static final int[][] infoIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,7}};
 public static final int[][] infoIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,7}};
 public static final int[][] infoV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,7}};
 public static final int[][] infoVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,7}};
 public static final int[][] infoVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,7}};
 public static final int[][] infoVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,7}};
 public static final int[][] infoIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,7}};
 public static final int[][] infoX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,7}};

 public static final int[][] robotI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,8}};
 public static final int[][] robotII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,8}};
 public static final int[][] robotIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,8}};
 public static final int[][] robotIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,8}};
 public static final int[][] robotV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,8}};
 public static final int[][] robotVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,8}};
 public static final int[][] robotVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,8}};
 public static final int[][] robotVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,8}};
 public static final int[][] robotIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,8}};
 public static final int[][] robotX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,8}};

 public static final int[][] technoI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,9}};
 public static final int[][] technoII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,9}};
 public static final int[][] technoIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,9}};
 public static final int[][] technoIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,9}};
 public static final int[][] technoV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,9}};
 public static final int[][] technoVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,9}};
 public static final int[][] technoVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,9}};
 public static final int[][] technoVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,9}};
 public static final int[][] technoIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,9}};
 public static final int[][] technoX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,9}};

 public static final int[][] armeI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,10}};
 public static final int[][] armeII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,10}};
 public static final int[][] armeIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,10}};
 public static final int[][] armeIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,10}};
 public static final int[][] armeV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,10}};
 public static final int[][] armeVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,10}};
 public static final int[][] armeVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,10}};
 public static final int[][] armeVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,10}};
 public static final int[][] armeIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,10}};
 public static final int[][] armeX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,10}};

 public static final int[][] raffineI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,11}};
 public static final int[][] raffineII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,11}};
 public static final int[][] raffineIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,11}};
 public static final int[][] raffineIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,11}};
 public static final int[][] raffineV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,11}};
 public static final int[][] raffineVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,11}};
 public static final int[][] raffineVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,11}};
 public static final int[][] raffineVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,11}};
 public static final int[][] raffineIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,11}};
 public static final int[][] raffineX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,11}};

 public static final int[][] lourdeI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,12}};
 public static final int[][] lourdeII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,12}};
 public static final int[][] lourdeIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,12}};
 public static final int[][] lourdeIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,12}};
 public static final int[][] lourdeV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,12}};
 public static final int[][] lourdeVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,12}};
 public static final int[][] lourdeVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,12}};
 public static final int[][] lourdeVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,12}};
 public static final int[][] lourdeIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,12}};
 public static final int[][] lourdeX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,12}};

 public static final int[][] metauxI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,13}};
 public static final int[][] metauxII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,13}};
 public static final int[][] metauxIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,13}};
 public static final int[][] metauxIV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,13}};
 public static final int[][] metauxV={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,13}};
 public static final int[][] metauxVI={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,13}};
 public static final int[][] metauxVII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,13}};
 public static final int[][] metauxVIII={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,13}};
 public static final int[][] metauxIX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,13}};
 public static final int[][] metauxX={{Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,13}};



    // Cyborg

 public static final int[][] cyb_mdc_t1_I   = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,   2}};
 public static final int[][] cyb_mdc_t1_II  = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,   4}};
 public static final int[][] cyb_mdc_t1_III = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,   8}};
 public static final int[][] cyb_mdc_t1_IV  = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,  16}};
 public static final int[][] cyb_mdc_t1_V   = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,  32}};
 public static final int[][] cyb_mdc_t1_VI  = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,  64}};
 public static final int[][] cyb_mdc_t1_VII = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE, 128}};

 public static final int[][] cyb_mdc_t2_I   = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,   3}};
 public static final int[][] cyb_mdc_t2_II  = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,   9}};
 public static final int[][] cyb_mdc_t2_III = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,  27}};
 public static final int[][] cyb_mdc_t2_IV  = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,  81}};
 public static final int[][] cyb_mdc_t2_V   = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE, 243}};
 public static final int[][] cyb_mdc_t2_VI  = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE, 729}};

 public static final int[][] cyb_mdc_t3_I   = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,    4}};
 public static final int[][] cyb_mdc_t3_II  = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,   16}};
 public static final int[][] cyb_mdc_t3_III = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,   64}};
 public static final int[][] cyb_mdc_t3_IV  = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE,  256}};
 public static final int[][] cyb_mdc_t3_V   = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE, 1024}};
 public static final int[][] cyb_mdc_t3_VI  = {{Const.COMPOSANT_CAPACITE_NAVIRE_USINE, 4096}};

 public static final int[][] cyb_vs_tt_I    = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_TECHNO,   2000}};
 public static final int[][] cyb_vs_tt_II   = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_TECHNO,   2500}};
 public static final int[][] cyb_vs_tt_III  = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_TECHNO,   3000}};
 public static final int[][] cyb_vs_tt_IV   = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_TECHNO,   3500}};
 public static final int[][] cyb_vs_tt_V    = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_TECHNO,   5500}};

 public static final int[][] cyb_vs_te_I    = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_ESPIONNAGE,   2000}};
 public static final int[][] cyb_vs_te_II   = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_ESPIONNAGE,   2500}};
 public static final int[][] cyb_vs_te_III  = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_ESPIONNAGE,   3000}};
 public static final int[][] cyb_vs_te_IV   = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_ESPIONNAGE,   3500}};
 public static final int[][] cyb_vs_te_V    = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_ESPIONNAGE,   5500}};

 public static final int[][] cyb_vs_tc_I    = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_CONTRE,   2000}};
 public static final int[][] cyb_vs_tc_II   = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_CONTRE,   2500}};
 public static final int[][] cyb_vs_tc_III  = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_CONTRE,   3000}};
 public static final int[][] cyb_vs_tc_IV   = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_CONTRE,   3500}};
 public static final int[][] cyb_vs_tc_V    = {{Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_CONTRE,   5500}};


 public static final int[][] maitstargateI    = {{Const.MAITRISE_CAPACITE_TRANSFERT, 100}};
 public static final int[][] maitstargateII   = {{Const.MAITRISE_CAPACITE_TRANSFERT, 400}};
 public static final int[][] maitstargateIII  = {{Const.MAITRISE_CAPACITE_TRANSFERT, 900}};
 public static final int[][] maitstargateIV   = {{Const.MAITRISE_CAPACITE_TRANSFERT, 1600}};
 public static final int[][] maitstargateV    = {{Const.MAITRISE_CAPACITE_TRANSFERT, 2500}};
 public static final int[][] maitstargateVI   = {{Const.MAITRISE_CAPACITE_TRANSFERT, 3600}};
 public static final int[][] maitstargateVII  = {{Const.MAITRISE_CAPACITE_TRANSFERT, 4900}};
 public static final int[][] maitstargateVIII = {{Const.MAITRISE_CAPACITE_TRANSFERT, 6400}};
 public static final int[][] maitstargateIX   = {{Const.MAITRISE_CAPACITE_TRANSFERT, 8100}};
 public static final int[][] maitstargateX    = {{Const.MAITRISE_CAPACITE_TRANSFERT, 10000}};
 

    // Zwaias

 public static final int[][] poly_constI = {
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,   8},
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,   9},
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,  10},
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,  12},
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,   4},
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,  13}
 };

    public static final int[][] poly_constII  = poly_constI;
    public static final int[][] poly_constIII = poly_constI;
    public static final int[][] poly_constIV  = poly_constI;
    public static final int[][] poly_constV   = poly_constI;

 public static final int[][] poly_popI = {
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,  11},
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,  1},
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,  3},
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,  5},
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,  6},
     {Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE,  7}
 };

    public static final int[][] poly_popII  = poly_popI;
    public static final int[][] poly_popIII = poly_popI;
    public static final int[][] poly_popIV  = poly_popI;
    public static final int[][] poly_popV   = poly_popI;


}
       	 
       	 
       	 
       	 
       	 
       	 	
       	
