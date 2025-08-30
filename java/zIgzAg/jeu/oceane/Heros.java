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

public class Heros extends Leader implements Serializable{

 public static transient Heros HEROS_NON_PRESENT=new Heros("",new int[0][0],0,0,0,0,0,-1,0);

 private int position;

 public int getPosition(){return position;}
 public void setPosition(int entree){position=entree;}

 public boolean estEnReserve(){
  if(position==Const.POSITION_HEROS_RESERVE) return true; else return false;}

 public void mettreEnReserve(){position=Const.POSITION_HEROS_RESERVE;}

 public String descriptionPosition(Locale l){
  if(estEnReserve()) return Utile.maj(Univers.getMessage("RESERVE_LEADER",l));
   else return Utile.maj(Univers.getMessage("POSITION_HEROS",l))+Integer.toString(position+1);
  }


 public int nombreDeNiveauxDeCompetence(int comp){
  if(comp<10) return 5;
  if((comp==10)||(comp==11)) return 1;
  if(comp==12) return 5;
  return 0;
  }

 public int competenceNouvelleAuHasard(int r){
  while(true){
   int hasard=Univers.getInt(101);
   int index=0;
   for(int i=0;i<Const.NOMBRE_COMPETENCES;i++)
    if(hasard>0){
     hasard=hasard-Const.CHANCE_TROUVER_COMPETENCE_HEROS[r][i];
     index=i;
     }
   boolean reussite=false;
   if(getNiveauCompetence(index)<nombreDeNiveauxDeCompetence(index)) reussite=true;
   if((index==Const.COMPETENCE_LEADER_VOYAGE_INTRAGALACTIQUE)||(index==Const.COMPETENCE_LEADER_VOYAGE_INTERGALACTIQUE))
    if(getNiveau()<8) reussite=false;
   if(reussite) return index;
   }
  }

 private Heros(){}

 public Heros(String n,int[][] comp,int vit,int att,int def,int mor,int march,int rac,int tour){
  super(n,comp,vit,att,def,mor,march,rac,tour);
  position=Const.POSITION_HEROS_RESERVE;
  }
}
