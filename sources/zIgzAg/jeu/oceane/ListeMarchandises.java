// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;



public interface ListeMarchandises{
	 
 public static final int[][] laserI={{8,1}}; 
 public static final int[][] laserII={{8,1}};
 public static final int[][] laserIII={{8,1}};
 public static final int[][] laserIV={{8,1}};
 public static final int[][] laserV={{8,1}};
 public static final int[][] laserVI={{8,1}};
 public static final int[][] laserVII={{8,1}};
 public static final int[][] laserVIII={{8,1}};
 public static final int[][] laserIX={{8,1}};
 public static final int[][] laserX={{8,1}};

 public static final int[][] plasmaI={{10,1}};
 public static final int[][] plasmaII={{10,1}};
 public static final int[][] plasmaIII={{10,1}};
 public static final int[][] plasmaIV={{10,1}};
 public static final int[][] plasmaV={{10,1}};
 public static final int[][] plasmaVI={{10,1}};
 public static final int[][] plasmaVII={{10,1}};
 public static final int[][] plasmaVIII={{10,1}};
 public static final int[][] plasmaIX={{10,1}};
 public static final int[][] plasmaX={{10,1}};

 public static final int[][] torpI={{4,1}};
 public static final int[][] torpII={{4,1}};
 public static final int[][] torpIII={{4,1}};
 public static final int[][] torpIV={{4,1}};
 public static final int[][] torpV={{4,1}};
 public static final int[][] torpVI={{4,1}};
 public static final int[][] torpVII={{4,1}};
 public static final int[][] torpVIII={{4,1}};
 public static final int[][] torpIX={{4,1}};
 public static final int[][] torpX={{4,1}};

 public static final int[][] missI={{6,1}};
 public static final int[][] missII={{6,1}};
 public static final int[][] missIII={{6,1}};
 public static final int[][] missIV={{6,1}};
 public static final int[][] missV={{6,1}};
 public static final int[][] missVI={{6,1}};
 public static final int[][] missVII={{6,1}};
 public static final int[][] missVIII={{6,1}};
 public static final int[][] missIX={{6,1}};
 public static final int[][] missX={{6,1}};
 
 public static final int[][] absorbI={{8,1}};
 public static final int[][] absorbII={{8,1}};
 public static final int[][] absorbIII={{8,1}};
 public static final int[][] absorbIV={{8,2}};
 public static final int[][] absorbV={{8,2}};
 public static final int[][] absorbVI={{8,2},{14,1}};
 public static final int[][] absorbVII={{8,3},{14,1}};
 public static final int[][] absorbVIII={{8,3},{14,2}};
 public static final int[][] absorbIX={{8,4},{14,2}};
 public static final int[][] absorbX={{8,5},{14,3}};
 

// fin armes de def

 public static final int[][] bombeI={{9,1}};
 public static final int[][] bombeII={{9,1}};
 public static final int[][] bombeIV={{9,1}};
 public static final int[][] bombeIII={{9,1}};
 public static final int[][] bombeV={{9,1}};
 public static final int[][] bombeVI={{9,1}};
 public static final int[][] bombeVII={{9,1}};
 public static final int[][] bombeVIII={{9,1}};
 public static final int[][] bombeIX={{9,1}};
 public static final int[][] bombeX={{9,1}};
 
 public static final int[][] moteurI={{10,1}};
 public static final int[][] moteurII={{10,1}};
 public static final int[][] moteurIII={{10,1}};
 public static final int[][] moteurIV={{10,2}};
 public static final int[][] moteurV={{10,3}};
 public static final int[][] moteurVI={{10,3},{12,1}};
 public static final int[][] moteurVII={{10,3},{12,2}};
 public static final int[][] moteurVIII={{10,4},{12,2},{14,1}};
 public static final int[][] moteurIX={{10,5},{12,3},{14,2}};
 public static final int[][] moteurX = {{10,10},{12,10},{14,10}};

 public static final int[][] intraI={{10,2},{14,1}};

 public static final int[][] interI={{10,5},{14,2}};

 public static final int[][] scanII={{8,1}};
 public static final int[][] scanIII={{8,2}};
 public static final int[][] scanIV={{8,3},{12,1}};
 public static final int[][] scanV={{8,4},{12,2}};
 public static final int[][] scanVI={{8,5},{11,3}};
 public static final int[][] scanVII={{8,6},{12,4}};
 public static final int[][] scanVIII={{8,7},{12,5}};
 public static final int[][] scanIX={{8,8},{12,6}};
 public static final int[][] scanX={{8,10},{12,8}};

 public static final int[][] bouclierVIII={{13,1}};
 public static final int[][] bouclierIX={{13,2}};
 public static final int[][] bouclierX={{13,3}};

 public static final int[][] lmineI={{9,1}};
 public static final int[][] lmineII={{9,2}};
 public static final int[][] lmineIII={{9,3},{7,1}};
 public static final int[][] lmineIV={{9,4},{7,2}};
 public static final int[][] lmineV={{9,5},{7,3}};
 public static final int[][] lmineVI={{9,8},{7,4}};
 public static final int[][] lmineVII={{9,9},{7,5}};
 public static final int[][] lmineVIII={{9,9},{7,6}};
 public static final int[][] lmineIX={{9,9},{7,7}};
 public static final int[][] lmineX={{9,10},{7,8}};
 
 public static final int[][] dmineIII={{8,1}};
 public static final int[][] dmineIV={{8,2}};
 public static final int[][] dmineV={{8,3}};
 public static final int[][] dmineVI={{8,4},{7,1}};
 public static final int[][] dmineVII={{8,5},{7,2}};
 public static final int[][] dmineVIII={{8,6},{7,3}};
 public static final int[][] dmineIX={{8,7},{7,4}};
 public static final int[][] dmineX={{8,10},{7,10}};

 public static final int[][] mconstruI={{11,1}};
 public static final int[][] mconstruII={{11,2}};
 public static final int[][] mconstruIII={{11,3},{13,1}};
 public static final int[][] mconstruIV={{11,4},{7,1},{13,2}};
 public static final int[][] mconstruV={{11,5},{7,3},{13,3}};
 public static final int[][] mconstruVI={{11,7},{7,5},{13,4}};
 public static final int[][] mconstruVII={{11,10},{7,8},{13,6}};

 public static final int[][] bscanII={{8,1}};
 public static final int[][] bscanIII={{8,2}};
 public static final int[][] bscanIV={{8,3}};
 public static final int[][] bscanV={{8,4}};
 public static final int[][] bscanVI={{8,5},{11,3}};
 public static final int[][] bscanVII={{8,6},{12,4}};
 public static final int[][] bscanVIII={{8,7},{12,5}};
 public static final int[][] bscanIX={{8,8},{12,6}};
 public static final int[][] bscanX={{8,10},{12,8}};

 public static final int[][] balastV={{7,1}};
 public static final int[][] balastVI={{7,2}};
 public static final int[][] balastVII={{7,3}};
 public static final int[][] balastVIII={{7,4}};
 public static final int[][] balastIX={{7,5}};
 public static final int[][] balastX={{7,6}};

 public static final int[][] cargoV={{7,1}};
 public static final int[][] cargoVI={{7,2}};
 public static final int[][] cargoVII={{7,3}};
 public static final int[][] cargoVIII={{7,4}};
 public static final int[][] cargoIX={{7,5}};
 public static final int[][] cargoX={{7,6}};

 public static final int[][] villeI    = { {15,1}  , {0,1} };
 public static final int[][] villeII   = { {15,2}  , {0,2}  , {10,1} };
 public static final int[][] villeIII  = { {15,3}  , {0,3}  , {10,2} , {7,1} };
 public static final int[][] villeIV   = { {15,4}  , {0,4}  , {10,3} , {7,2} , {8,1} };
 public static final int[][] villeV    = { {15,5}  , {0,5}  , {10,4} , {7,3} , {8,2} , {5,1} };
 public static final int[][] villeVI   = { {15,6}  , {0,6}  , {10,5} , {7,4} , {8,3} , {5,2} };
 public static final int[][] villeVII  = { {15,7}  , {0,7}  , {10,6} , {7,5} , {8,4} , {5,3} };
 public static final int[][] villeVIII = { {15,8}  , {0,8}  , {10,7} , {7,6} , {8,5} , {5,4} };
 public static final int[][] villeIX   = { {15,9} , {0,9}  , {10,8} , {7,7} , {8,6} , {5,5} };
 public static final int[][] villeX    = { {15,10} , {0,10} , {10,9} , {7,8} , {8,7} , {5,6} };



 public static final int[][] hscanII={{8,1}};
 public static final int[][] hscanIII={{8,2}};
 public static final int[][] hscanIV={{8,3},{12,1}};
 public static final int[][] hscanV={{8,4},{12,2}};
 public static final int[][] hscanVI={{8,5},{11,3}};
 public static final int[][] hscanVII={{8,6},{12,4}};
 public static final int[][] hscanVIII={{8,7},{12,5}};
 public static final int[][] hscanIX={{8,8},{12,6}};
 public static final int[][] hscanX={{8,10},{12,8}};

 public static final int[][] draminI={{7,1}};
 public static final int[][] draminII={{7,2}};
 public static final int[][] draminIII={{7,3}};
 public static final int[][] draminIV={{7,4}};
 public static final int[][] draminV={{7,5}};
 public static final int[][] draminVI={{7,6}};
 public static final int[][] draminVII={{7,7}};
 public static final int[][] draminVIII={{7,8}};
 public static final int[][] draminIX={{7,9}};
 public static final int[][] draminX={{7,10}};

 public static final int[][] robocoI={{0,1}};

  //les batiments -->

 public static final int[][] retraiteI={{11,2}};  

 public static final int[][] optpI    = { {7,1} };  
 public static final int[][] optpII   = { {7,2}, {10,1} };  
 public static final int[][] optpIII  = { {7,3}, {10,2}, {6,1} };
 public static final int[][] optpIV   = { {7,4}, {10,3}, {6,2}, {8,1} };  
 public static final int[][] optpV    = { {7,5}, {10,4}, {6,3}, {8,2}, {11,1} };  
 public static final int[][] optpVI   = { {7,6}, {10,5}, {6,4}, {8,3}, {11,2} };  
 public static final int[][] optpVII  = { {7,7}, {10,6}, {6,5}, {8,4}, {11,3} };  
 public static final int[][] optpVIII = { {7,8}, {10,7}, {6,6}, {8,5}, {11,4} };  
 public static final int[][] optpIX   = { {7,9}, {10,8}, {6,7}, {8,6}, {11,5} };  
 public static final int[][] optpX    = {{7,10}, {10,9}, {6,8}, {8,7}, {11,6} };  

 public static final int[][] optsI={{7,2}};  

 public static final int[][] chantierI= {{7,2}};  

    // Les mines 

 public static final int[][] mineII  = {{11,1}};  
 public static final int[][] mineIII = {{11,2},{8,1}};  
 public static final int[][] mineIV  = {{11,3},{8,2},{6,1}};  
 public static final int[][] mineV   = {{11,4},{8,3},{6,2},{10,1}};  




 public static final int[][] repareI  ={{11,1}};
 public static final int[][] repareII ={{11,2}};
 public static final int[][] repareIII={{11,3}};
 public static final int[][] repareIV ={{11,4}};
 public static final int[][] repareV  ={{11,5}};

 public static final int[][] boucplaII={{7,1}};
 public static final int[][] boucplaIII={{7,2},{6,1}};
 public static final int[][] boucplaIV={{7,3},{6,2}};
 public static final int[][] boucplaV={{7,4},{6,3}};
 public static final int[][] boucplaVI={{7,5},{6,4}};
 public static final int[][] boucplaVII={{7,6},{6,5}};
 public static final int[][] boucplaVIII={{7,7},{6,6},{13,1}};
 public static final int[][] boucplaIX={{7,8},{6,7},{13,2}};
 public static final int[][] boucplaX={{7,9},{6,8},{13,3}};

 public static final int[][] radarII={{6,1}};
 public static final int[][] radarIII={{6,2}};
 public static final int[][] radarIV={{6,2}};
 public static final int[][] radarV={{6,3}};
 public static final int[][] radarVI={{6,4}};
 public static final int[][] radarVII={{6,5}};
 public static final int[][] radarVIII={{6,6},{13,1}};
 public static final int[][] radarIX={{6,7},{13,2}};
 public static final int[][] radarX={{6,8},{13,3}};

 public static final int[][] extraI={{7,1}};
 public static final int[][] extraII={{7,2}};
 public static final int[][] extraIII={{7,3}};
 public static final int[][] extraIV={{7,4}};
 public static final int[][] extraV={{7,5},{13,1}};

 public static final int[][] battlaII={{9,1}};
 public static final int[][] battlaIII={{9,2}};
 public static final int[][] battlaIV={{9,2}};
 public static final int[][] battlaV={{9,3}};
 public static final int[][] battlaVI={{9,4}};
 public static final int[][] battlaVII={{9,5}};
 public static final int[][] battlaVIII={{9,6},{13,1}};
 public static final int[][] battlaIX={{9,7},{13,2}};
 public static final int[][] battlaX={{9,8},{13,3}};

 public static final int[][] battplaII={{9,1}};
 public static final int[][] battplaIII={{9,2}};
 public static final int[][] battplaIV={{9,2}};
 public static final int[][] battplaV={{9,3}};
 public static final int[][] battplaVI={{9,4}};
 public static final int[][] battplaVII={{9,5}};
 public static final int[][] battplaVIII={{9,6},{13,1}};
 public static final int[][] battplaIX={{9,7},{13,2}};
 public static final int[][] battplaX={{9,8},{13,3}};

 public static final int[][] rampemisII={{9,1},{6,1}};
 public static final int[][] rampemisIII={{9,2},{6,2}};
 public static final int[][] rampemisIV={{9,2},{6,3}};
 public static final int[][] rampemisV={{9,3},{6,4}};
 public static final int[][] rampemisVI={{9,4},{6,5}};
 public static final int[][] rampemisVII={{9,5},{6,6}};
 public static final int[][] rampemisVIII={{9,6},{13,1},{6,7}};
 public static final int[][] rampemisIX={{9,7},{13,2},{6,8}};
 public static final int[][] rampemisX={{9,8},{13,3},{6,9}};

 public static final int[][] lancetorII={{9,2}};
 public static final int[][] lancetorIII={{9,3}};
 public static final int[][] lancetorIV={{9,4}};
 public static final int[][] lancetorV={{9,5}};
 public static final int[][] lancetorVI={{9,6}};
 public static final int[][] lancetorVII={{9,7}};
 public static final int[][] lancetorVIII={{9,8},{13,2}};
 public static final int[][] lancetorIX={{9,9},{13,3}};
 public static final int[][] lancetorX={{9,10},{13,4}};

    // Usine

public static final int[][] agroI=null;
public static final int[][] agroII={{10,1}};
public static final int[][] agroIII={{10,2}};
public static final int[][] agroIV={{10,3}};
public static final int[][] agroV={{10,4}};
public static final int[][] agroVI={{10,5}};
public static final int[][] agroVII={{10,6}};
public static final int[][] agroVIII={{10,7}};
public static final int[][] agroIX={{10,8}};
public static final int[][] agroX={{10,9}};

public static final int[][] agricoleI=null;
public static final int[][] agricoleII=null;
public static final int[][] agricoleIII=null;
public static final int[][] agricoleIV=null;
public static final int[][] agricoleV=null;
public static final int[][] agricoleVI={{13,1}};
public static final int[][] agricoleVII={{13,2}};
public static final int[][] agricoleVIII={{13,3}};
public static final int[][] agricoleIX={{13,4}};
public static final int[][] agricoleX={{13,5}};

public static final int[][] modeI=null;
public static final int[][] modeII={{10,1}};
public static final int[][] modeIII={{10,2}};
public static final int[][] modeIV={{10,3}};
public static final int[][] modeV={{10,4}};
public static final int[][] modeVI={{10,5}};
public static final int[][] modeVII={{10,6}};
public static final int[][] modeVIII={{10,7}};
public static final int[][] modeIX={{10,8}};
public static final int[][] modeX={{10,9}};

public static final int[][] culturelI=null;
public static final int[][] culturelII={{10,1}};
public static final int[][] culturelIII={{10,2}};
public static final int[][] culturelIV={{10,3}};
public static final int[][] culturelV={{10,4}};
public static final int[][] culturelVI={{10,5}};
public static final int[][] culturelVII={{10,6}};
public static final int[][] culturelVIII={{10,7}};
public static final int[][] culturelIX={{10,8}};
public static final int[][] culturelX={{10,9}};

public static final int[][] transfoI=null;
public static final int[][] transfoII={{10,1}};
public static final int[][] transfoIII={{10,2}};
public static final int[][] transfoIV={{10,3}};
public static final int[][] transfoV={{10,4}};
public static final int[][] transfoVI={{10,5}};
public static final int[][] transfoVII={{10,6}};
public static final int[][] transfoVIII={{10,7}};
public static final int[][] transfoIX={{10,8}};
public static final int[][] transfoX={{10,9}};

public static final int[][] pharmaI=null;
public static final int[][] pharmaII={{10,1}};
public static final int[][] pharmaIII={{10,2}};
public static final int[][] pharmaIV={{10,3}};
public static final int[][] pharmaV={{10,4}};
public static final int[][] pharmaVI={{10,5}};
public static final int[][] pharmaVII={{10,6}};
public static final int[][] pharmaVIII={{10,7}};
public static final int[][] pharmaIX={{10,8}};
public static final int[][] pharmaX={{10,9}};

public static final int[][] infoI=null;
public static final int[][] infoII={{10,1}};
public static final int[][] infoIII={{10,2}};
public static final int[][] infoIV={{10,3}};
public static final int[][] infoV={{10,4}};
public static final int[][] infoVI={{10,5}};
public static final int[][] infoVII={{10,6}};
public static final int[][] infoVIII={{10,7}};
public static final int[][] infoIX={{10,8}};
public static final int[][] infoX={{10,9}};

public static final int[][] robotI=null;
public static final int[][] robotII={{10,1}};
public static final int[][] robotIII={{10,2}};
public static final int[][] robotIV={{10,3}};
public static final int[][] robotV={{10,4}};
public static final int[][] robotVI={{10,5}};
public static final int[][] robotVII={{10,6}};
public static final int[][] robotVIII={{10,7}};
public static final int[][] robotIX={{10,8}};
public static final int[][] robotX={{10,9}};

public static final int[][] technoI=null;
public static final int[][] technoII={{10,1}};
public static final int[][] technoIII={{10,2}};
public static final int[][] technoIV={{10,3}};
public static final int[][] technoV={{10,4}};
public static final int[][] technoVI={{10,5}};
public static final int[][] technoVII={{10,6}};
public static final int[][] technoVIII={{10,7}};
public static final int[][] technoIX={{10,8}};
public static final int[][] technoX={{10,9}};

public static final int[][] armeI=null;
public static final int[][] armeII={{10,1}};
public static final int[][] armeIII={{10,2}};
public static final int[][] armeIV={{10,3}};
public static final int[][] armeV={{10,4}};
public static final int[][] armeVI={{10,5}};
public static final int[][] armeVII={{10,6}};
public static final int[][] armeVIII={{10,7}};
public static final int[][] armeIX={{10,8}};
public static final int[][] armeX={{10,9}};

public static final int[][] raffineI=null;
public static final int[][] raffineII={{10,1}};
public static final int[][] raffineIII={{10,2}};
public static final int[][] raffineIV={{10,3}};
public static final int[][] raffineV={{10,4}};
public static final int[][] raffineVI={{10,5}};
public static final int[][] raffineVII={{10,6}};
public static final int[][] raffineVIII={{10,7}};
public static final int[][] raffineIX={{10,8}};
public static final int[][] raffineX={{10,9}};

public static final int[][] lourdeI=null;
public static final int[][] lourdeII={{10,1}};
public static final int[][] lourdeIII={{10,2}};
public static final int[][] lourdeIV={{10,3}};
public static final int[][] lourdeV={{10,4}};
public static final int[][] lourdeVI={{10,5}};
public static final int[][] lourdeVII={{10,6}};
public static final int[][] lourdeVIII={{10,7}};
public static final int[][] lourdeIX={{10,8}};
public static final int[][] lourdeX={{10,9}};

public static final int[][] metauxI=null;
public static final int[][] metauxII={{10,1}};
public static final int[][] metauxIII={{10,2}};
public static final int[][] metauxIV={{10,3}};
public static final int[][] metauxV={{10,4}};
public static final int[][] metauxVI={{10,5}};
public static final int[][] metauxVII={{10,6}};
public static final int[][] metauxVIII={{10,7}};
public static final int[][] metauxIX={{10,8}};
public static final int[][] metauxX={{10,9}};


    // Techno Cyborg


 public static final int[][] cyb_mdc_t1_I   = {{11,1}};
 public static final int[][] cyb_mdc_t1_II  = {{11,2},{7,1}};
 public static final int[][] cyb_mdc_t1_III = {{11,3},{7,2},{10,1}};
 public static final int[][] cyb_mdc_t1_IV  = {{11,4},{7,3},{10,2},{8,1}};
 public static final int[][] cyb_mdc_t1_V   = {{11,5},{7,4},{10,3},{8,2},{13,1}};
 public static final int[][] cyb_mdc_t1_VI  = {{11,6},{7,6},{10,4},{8,3},{13,1}};
 public static final int[][] cyb_mdc_t1_VII = {{11,8},{7,8},{10,6},{8,4},{13,1},{14,1}};

 public static final int[][] cyb_mdc_t2_I   = {{11,1}};
 public static final int[][] cyb_mdc_t2_II  = {{11,2}};
 public static final int[][] cyb_mdc_t2_III = {{11,3}};
 public static final int[][] cyb_mdc_t2_IV  = {{11,4}};
 public static final int[][] cyb_mdc_t2_V   = {{11,5}};
 public static final int[][] cyb_mdc_t2_VI  = {{11,7}};

 public static final int[][] cyb_mdc_t3_I   = {{11,2}};
 public static final int[][] cyb_mdc_t3_II  = {{11,3}};
 public static final int[][] cyb_mdc_t3_III = {{11,4}};
 public static final int[][] cyb_mdc_t3_IV  = {{11,6}};
 public static final int[][] cyb_mdc_t3_V   = {{11,10}};
 public static final int[][] cyb_mdc_t3_VI  = {{11,16}};

    public static final int[][] cyb_vs_tt_I    = {{0,1}};
    public static final int[][] cyb_vs_tt_II   = {{0,2},{8,1}};
    public static final int[][] cyb_vs_tt_III  = {{0,3},{8,2},{5,1}};
    public static final int[][] cyb_vs_tt_IV   = {{0,4},{8,3},{5,2},{3,1}};
    public static final int[][] cyb_vs_tt_V    = {{0,6},{8,5},{5,3},{3,3},{11,2}};

    public static final int[][] cyb_vs_te_I    = {{0,1}};
    public static final int[][] cyb_vs_te_II   = {{0,2},{8,1}};
    public static final int[][] cyb_vs_te_III  = {{0,3},{8,2},{5,1}};
    public static final int[][] cyb_vs_te_IV   = {{0,4},{8,3},{5,2},{3,1}};
    public static final int[][] cyb_vs_te_V    = {{0,6},{8,5},{5,3},{3,3},{11,2}};

    public static final int[][] cyb_vs_tc_I    = {{0,1}};
    public static final int[][] cyb_vs_tc_II   = {{0,2},{8,1}};
    public static final int[][] cyb_vs_tc_III  = {{0,3},{8,2},{5,1}};
    public static final int[][] cyb_vs_tc_IV   = {{0,4},{8,3},{5,2},{3,1}};
    public static final int[][] cyb_vs_tc_V    = {{0,6},{8,5},{5,3},{3,3},{11,2}};


    // techno Zwaias
public static final int[][] poly_popI    = null;
public static final int[][] poly_popII   = { {10,1} };
public static final int[][] poly_popIII  = { {10,2} };
public static final int[][] poly_popIV   = { {10,3} };
public static final int[][] poly_popV    = { {10,4} };
public static final int[][] poly_popVI   = { {10,5} , {13,1} };
public static final int[][] poly_popVII  = { {10,6} , {13,2} };
public static final int[][] poly_popVIII = { {10,7} , {13,3} };
public static final int[][] poly_popIX   = { {10,8} , {13,4} };
public static final int[][] poly_popX    = { {10,9} , {13,5} };


public static final int[][] poly_constI    = null;
public static final int[][] poly_constII   = { {10,1} };
public static final int[][] poly_constIII  = { {10,2} };
public static final int[][] poly_constIV   = { {10,3} };
public static final int[][] poly_constV    = { {10,4} };
public static final int[][] poly_constVI   = { {10,5} , {13,1} };
public static final int[][] poly_constVII  = { {10,6} , {13,2} };
public static final int[][] poly_constVIII = { {10,7} , {13,3} };
public static final int[][] poly_constIX   = { {10,8} , {13,4} };
public static final int[][] poly_constX    = { {10,9} , {13,5} };

}
       	 
       	 
       	 
       	 
       	 
       	 	
       	
