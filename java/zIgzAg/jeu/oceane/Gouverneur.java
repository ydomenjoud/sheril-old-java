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

public class Gouverneur extends Leader implements Serializable{

 public static transient Gouverneur GOUVERNEUR_NON_PRESENT=new Gouverneur("",new int[0][0],0,0,0,0,0,-1,0);

 private Position position;

 public Position getPosition(){return position;}
 public void setPosition(Position entree){position=entree;}

 public boolean estEnReserve(){
  if(position==Const.POSITION_GOUVERNEUR_RESERVE) return true; else return false;}
 public void mettreEnReserve(){position=Const.POSITION_GOUVERNEUR_RESERVE;}

 public String descriptionPosition(Locale l){
  if(estEnReserve()) return Utile.maj(Univers.getMessage("RESERVE_LEADER",l));
   else return Utile.maj(Univers.getMessage("POSITION_GOUVERNEUR",l))+Univers.getSysteme(position).getNomPosition(l);
  }



 public int nombreDeNiveauxDeCompetence(int comp){
  if((comp==6)||(comp==9)||(comp==10)||(comp==11)) return 0;
  return 5;
  }

 public int competenceNouvelleAuHasard(int r){
  while(true){
   int hasard=Univers.getInt(101);
   int index=0;
   for(int i=0;i<Const.NOMBRE_COMPETENCES;i++)
    if(hasard>0){
     hasard=hasard-Const.CHANCE_TROUVER_COMPETENCE_GOUVERNEUR[r][i];
     index=i;
     }
   if(getNiveauCompetence(index)<nombreDeNiveauxDeCompetence(index)) return index;
   }
  }


 private Gouverneur(){}

 public Gouverneur(String n,int[][] comp,int vit,int att,int def,int mor,int march,int rac,int tour){
  super(n,comp,vit,att,def,mor,march,rac,tour);
  position=Const.POSITION_GOUVERNEUR_RESERVE;
  }

}
