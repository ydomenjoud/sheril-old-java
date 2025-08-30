// v2.00 13/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */


 public class Position3D implements Cloneable{

  private int[] pos;

  public Position3D(){
   pos=new int[3];
   }

  public Position3D(int[] position){
   pos=(int[])position.clone();
   }

  public int[] getPos(){return pos;}

  public void setPos(int[] e){pos=e;}

  //méthodes pour tester l'égalité.

  public boolean equals(Object objet){
   if(objet==null) return false;
   Position3D entree=(Position3D)objet;
   if ((entree.getPos()[0]==pos[0])&&(entree.getPos()[1]==pos[1])&&(entree.getPos()[2]==pos[2]))
    return true;
    else return false;
   }


  //pour cloner une position.

  public Object clone(){
   return new Position3D(pos);
   }

  //représentation "de base".

  public String toString(){
   return "("+"x:"+Integer.toString(pos[0])+"|y:"+Integer.toString(pos[1])+"|z:"+Integer.toString(pos[2])+")";
   }

  //fonction spéciale "au hasard"

  public static Position3D auHasard(int[] dep,int mod){
   int[] inter=new int[3];
   inter[0]=dep[0]+Univers.getInd()*Univers.getInt(6-mod);
   inter[1]=dep[1]+Univers.getInd()*Univers.getInt(6-mod);
   inter[2]=dep[2]+Univers.getInd()*Univers.getInt(10);
   return new Position3D(inter);
   }

  //nouvelle position après un déplacement de x case et autres fonctions de déplacement.

  public static Position3D positionAtteinte(Position3D depart,Position3D arrivee,int x){
   if(arrivee==null) return depart;
   Position3D retour=(Position3D)depart.clone();
   int[] inter=retour.getPos();
   inter[0]=Utile.courtChemin(depart.getPos()[0],arrivee.getPos()[0],x);
   inter[1]=Utile.courtChemin(depart.getPos()[1],arrivee.getPos()[1],x);
   inter[2]=Utile.courtChemin(depart.getPos()[2],arrivee.getPos()[2],x);
   return retour;
   }

  public static int distance(Position3D a,Position3D b){
   return Math.max(Math.max(Math.abs(a.getPos()[0]-b.getPos()[0]),Math.abs(a.getPos()[1]-b.getPos()[1])),
                   Math.abs(a.getPos()[2]-b.getPos()[2]));
   }

  public static Position3D positionLaPlusProche(Position3D depart,Position3D[] arrivees){
   int min=Integer.MAX_VALUE;
   int index=-1;
   for(int i=0;i<arrivees.length;i++)
    if(distance(depart,arrivees[i])<min){
     min=distance(depart,arrivees[i]);
     index=i;
     }
   if(index!=-1) return (Position3D)arrivees[index].clone();
    else return (Position3D)depart.clone();
   }

  public static Position3D positionDeFuite(boolean att){
   Position3D retour=new Position3D();
   int[] inter=new int[3];
   if(att) inter[1]=Const.COMBAT_Y_FIN1;
    else inter[1]=Const.COMBAT_Y_FIN2;
   inter[0]=Univers.getInd()*Univers.getInt(100);
   inter[2]=Univers.getInd()*Univers.getInt(100);
   retour.setPos(inter);
   return retour;
   }


 }
