// v2.00 13/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */

import java.io.Serializable;
import java.util.Locale;
import zIgzAg.utile.Mdt;

public abstract class ObjetTransporte implements Serializable,Cloneable{

 private String code;

 public String getCode(){return code;}
 public boolean estDeType(String c){return code.equals(c);}

 public static int typeDeCodeChargement(String code){
   if(Messages.MINERAI.equals(code)) return Const.TRANSPORT_MINERAI;
   if(Mdt.estPresent(Messages.MARCHANDISES,code)) return Const.TRANSPORT_MARCHANDISE;
   if(Mdt.estPresent(Messages.RACES,code)) return Const.TRANSPORT_POPULATION;
   if(Univers.existenceTechnologieBatiment(code)) return Const.TRANSPORT_BATIMENT;
   return Const.TRANSPORT_AUCUN;
   }

 public static boolean existenceCodeChargement(String code){
   if(typeDeCodeChargement(code)!=Const.TRANSPORT_AUCUN) return true;
    else return false;
   }

 public static int getEncombrementChargement(String code){
  if(typeDeCodeChargement(code)!=Const.TRANSPORT_BATIMENT) return 1;
     else return ((Batiment)Univers.getTechnologie(code)).getPointsDeStructure();
  }

 public static String traductionChargement(String code,int nb,Locale loc){
  int t=typeDeCodeChargement(code);
  if(t==Const.TRANSPORT_MINERAI) return Univers.getMessage("MINERAI",loc);
  if(t==Const.TRANSPORT_MARCHANDISE) return Univers.getMessage("MARCHANDISES",Utile.numeroMarchandise(code),loc);
  if(t==Const.TRANSPORT_POPULATION) return Univers.getMessage("RACES",Utile.numeroRace(code),loc);
  if(t==Const.TRANSPORT_BATIMENT) if(nb>1) return Univers.getTechnologie(code).getNomPlurielComplet(loc);
                                     else return Univers.getTechnologie(code).getNomComplet(loc);
  return "erreur!";
  }

 public static String getDescriptionListeChargement(ObjetTransporte[] o,Locale loc){
  String retour=new String();
  for(int i=0;i<o.length;i++){
   retour=retour+Utile.maj(traductionChargement(o[i].getCode(),o[i].getNombreObjets(),loc))+
                  " : "+Integer.toString(o[i].getNombreObjets());
   if(i!=(o.length-1)) retour=retour+"<BR>";
   }
  return retour;
  }

 public abstract Object ajout(Object o);
 public abstract Object suppression(int nb);
 public abstract boolean estValide();
 public abstract int getNombreObjets();
 public abstract Object clone();

 protected ObjetTransporte(){};

 public ObjetTransporte(String type){
  code=type;
  }



 }








