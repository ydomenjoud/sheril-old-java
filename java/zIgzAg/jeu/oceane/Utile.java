// v2.00 13/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */

import java.util.GregorianCalendar;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Map;
import zIgzAg.utile.Mdt;

 public class Utile{

  public static final String[] ROMAINS={"I","II","III","IV","V","VI","VII","VIII","IX","X"};
  //Les chiffres romains de 1 à 10

  public static String getNom(){
   char[] alpha=
      {'A','E','I','O','U','Y','B','C','D','F','G','H','J','K','L','M','N','P','Q','R','S','T','V','W','X','Z',' '};
   int t=Univers.getInt(3)+1;
   int u=Univers.getInt(2);
   char[] tab=new char[2*t+u];
   for(int i=0;i<2*t;i+=2){
    tab[i]=alpha[Univers.getInt(27)];
    tab[i+1]=alpha[Univers.getInt(7)];
    }
   if (u==1) tab[tab.length-1]=alpha[Univers.getInt(26)];
   for(int i=1;i<tab.length;i++)
    if(tab[i-1]!=' ') tab[i]=Character.toLowerCase(tab[i]);
   return ((String)new String(tab)).trim();
   }

  // Une fonction qui retourne un nom au hasard.

 public static String maj(String entree){
  if(entree.length()==0) return entree;
  if(entree.length()==1) return entree.toUpperCase();
  return entree.substring(0,1).toUpperCase()+entree.substring(1,entree.length());
  }
  //met la première lettre du mot en majuscule.

 public static float aD(float valeur,int nbDecimales){
  float ar=nbDecimales*10F;
  Integer a=new Integer(Math.round(valeur*ar));
  return a.floatValue()/ar;
  }
 //arrondit à nbDecimales.

 public static float a1D(float valeur){return aD(valeur,1);}
 public static float a2D(float valeur){return aD(valeur,2);}
 //arrondit à une ou deux décimales.


  public static int getRaceDeDepart(Position pos){
   for(int k=pos.getNumeroGalaxie()*Const.NB_RACES_PAR_GALAXIE;k<(Const.NB_RACES_PAR_GALAXIE*(pos.getNumeroGalaxie()+1));
                                                                                                         k++)
    if ((pos.getY()>Const.REPARTITION_DES_RACES[k][0])&&
        (pos.getY()<=Const.REPARTITION_DES_RACES[k][1])&&
        (pos.getX()>Const.REPARTITION_DES_RACES[k][2])&&
        (pos.getX()<=Const.REPARTITION_DES_RACES[k][3]))
         return k;

   return -1;
   }

  // Une fonction qui retourne la race de départ pour une position donnée.

  public static String getDate(){
   GregorianCalendar gc=new GregorianCalendar();
   String retour=changeInt(gc.get(GregorianCalendar.DAY_OF_MONTH));
   retour=retour+"-"+changeInt(gc.get(GregorianCalendar.MONTH)+1);
   retour=retour+"-"+Integer.toString(gc.get(GregorianCalendar.YEAR));
   retour=retour+" "+changeInt(gc.get(GregorianCalendar.HOUR_OF_DAY));
   retour=retour+":"+changeInt(gc.get(GregorianCalendar.MINUTE));
   retour=retour+":"+changeInt(gc.get(GregorianCalendar.SECOND));
   return retour;
   }

  // Une fonction qui retourne une version de la date en chaine de caractères.

  private static String changeInt(int entree){
   String retour=Integer.toString(entree);
   if (retour.length()==1) retour="0"+retour;
   return retour;
   }

  //Une fonction qui retourne un nombre à un ou deux chiffres comme String de deux chiffres.

/*  public static int numMarchandise(String entree){
   for(int i=0;i<Messages.MARCHANDISES.length;i++)
    if (Messages.MARCHANDISES[i].equals(entree)) return i;
   return -1;
   }
*/
  // Une fonction qui retourne le numéro correspondant au nom de marchandise dans la langue par défaut,
  // et -1 si aucun ne correspond.

  public static int courtChemin(int depart,int arrivee,int deplacement){
   if(Math.abs(depart-arrivee)<=deplacement) return arrivee;
    else if(depart<arrivee) return (depart+deplacement);
            else return (depart-deplacement);
   }

  //une fonction qui indique le plus court chemin entre un point et un autre.

  public static int[] transformer(Integer[] t){
   int[] retour=new int[t.length];
   for(int i=0;i<t.length;i++)
    retour[i]=t[i].intValue();
   return retour;
   }

  public static int[] transformer3(Object[] t){
   int[] retour=new int[t.length];
   for(int i=0;i<t.length;i++)
    retour[i]=Integer.parseInt(t[i].toString());
   return retour;
   }

  public static Integer[] transformer2(int[] t){
   Integer[] retour=new Integer[t.length];
   for(int i=0;i<t.length;i++)
    retour[i]=new Integer(t[i]);
   return retour;
   }

  //une fonction qui transforme un tableau d'Integer en tableau d'entier et son inverse.

  public static String[] tableauToString(Object[] t){
   String[] retour=new String[t.length];
   for(int i=0;i<t.length;i++)
    retour[i]=t[i].toString();
   return retour;
   }

  //une fonction qui transforme un tableau d'Objet en un tableau des résultats des fonctions toString() coorespondants.

  public static Integer[] retournerTableauEntiers(int borne){
   Integer[] retour=new Integer[borne+1];
   for(int i=0;i<=borne;i++)
    retour[i]=new Integer(i);
   return retour;
   }
   //retourne un tableau d'Integer progressifs, utile pour la production des ordres(! borne comprise).

  public static int numeroMarchandise(String code){
   return Mdt.position(Messages.MARCHANDISES,code);
   }

  public static int numeroRace(String code){
   return Mdt.position(Messages.RACES,code);
   }

   //fonctions pour trouver le numéro correspondant au code.

  public static String choisirLogin(){
   char[] alpha={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
   char[] retour=new char[Const.TAILLE_LOGIN];
   for(int i=0;i<Const.TAILLE_LOGIN;i++) retour[i]=alpha[Univers.getInt(alpha.length)];
   String l=new String(retour);
   if(!Univers.existenceLogin(l)) return l;
    else return choisirLogin();
   }

  //fonction qui retourne un login.

  public static int[] ordreAuHasard(int t){
   int[] r=new int[t];
   for(int i=0;i<t;i++){
    boolean b=true;
    r[i]=Univers.getInt(t);
    while(b){
     boolean c=true;
     for(int j=0;j<i;j++)
      if(r[j]==r[i]) c=false;
     if(c) b=false;
      else r[i]=Univers.getInt(t);
     }
    }
   return r;
   }

  //renvoit un tableau d'entier de taille t remplis avec t entiers distincts de 0 à t-1 dans un ordre au hasard.


  }
