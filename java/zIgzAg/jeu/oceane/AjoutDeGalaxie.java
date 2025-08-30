// v2.00 13/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.


package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */

 public class AjoutDeGalaxie{

 /* Classes à modifier :

  Const : REPARTITION_DES_RACES, POLITIQUE_..,CHANCE_AUGMENTER_CARACTERISTIQUE_LEADER,CHANCE_TROUVER_COMPETENCE_HEROS,
          CHANCE_TROUVER_COMPETENCE_GOUVERNEUR,HABITAT_RADIATION,HABITAT_TEMPERATURE,HABITAT_GRAVITE,RACES_ATMOSPHERES,
          RACES_CARACTERISTIQUES,RACE_TECHNOLOGIES

  Messages : RACES, POLITIQUES,

  Possession : racePolitiqueAnti(int)

  Rapport : COULEURS_RACES, COULEURS_GALAXIES

*/


  public static void main(String args[]){
   if(args.length!=1) System.exit(0);
   int numero=Integer.parseInt(args[0]);

   Univers univers=new Univers(true,Const.MESSAGE_U_00001);

   Commandant commandantNeutre=Univers.getCommandant(0);
   Position pos=null;

   boolean[][] presenceSys=new boolean[Const.BORNE_MAX][Const.BORNE_MAX];
   int nbSys=1000+Univers.getInt(200);
   int nbFlottes=500+Univers.getInt(100);
   int nbSecteursVides=4;
   int[] secteursVides=new int[nbSecteursVides];

   for(int i=0;i<nbSecteursVides;i++){
    boolean dejaVu=false;
    int choix=Univers.getInt(Const.NB_SECTEURS)+1;
    for(int j=0;j<i;j++) if (secteursVides[j]==choix) dejaVu=true;
    if(!dejaVu) secteursVides[i]=choix;
     else i--;
    }

   for (int i=0;i<Const.NB_PORTES;i++)
    presenceSys[Const.PASSAGES_GALACTIQUES[i][0]-1][Const.PASSAGES_GALACTIQUES[i][1]-1]=true;

   for (int i=0;i<nbSys;i++){
    System.out.print("S"+i+"-");
    pos=Position.auHasard(numero);
    if (!presenceSys[pos.getY()-1][pos.getX()-1]){
     boolean sV=false;
     for(int j=0;j<nbSecteursVides;j++) if (pos.getNumeroSecteur()==secteursVides[j]) sV=true;
     if ((!sV)||(Univers.getTest(10))){
      Univers.setSysteme(Systeme.creerAuHasard(pos));
      commandantNeutre.ajouterPossession(pos,Possession.creerAuHasard());
      presenceSys[pos.getY()-1][pos.getX()-1]=true;
      }
      else i--;
      }
     else i--;
    }

   for(int i=0;i<nbFlottes;i++){
    pos=Position.auHasard(numero);
    if(presenceSys[pos.getY()-1][pos.getX()-1]){
     System.out.print("F"+i+"-");
     Flotte f=Flotte.creerAuHasard(pos,"Flotte neutre",Utile.getRaceDeDepart(pos),Univers.getInt(500));
     f.setDirective(Const.DIRECTIVE_FLOTTE_ATTAQUE_PREVENTIVE);
     commandantNeutre.ajouterFlotte(f);
     }
     else i--;
    }

   Univers.setCommandant(commandantNeutre);
   univers.sauvegarder();


   }



 }
