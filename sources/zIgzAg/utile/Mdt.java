// v 1.40 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.


package zIgzAg.utile;

import java.lang.reflect.Array;
import java.util.Arrays;


public class Mdt{

 private Mdt(){}



 public static int[] ajout(int[] tab,int val){
  int[] retour;
  if(tab==null) retour=new int[1];
   else{
    retour=new int[tab.length+1];
    System.arraycopy(tab,0,retour,0,tab.length);
    }
  retour[retour.length-1]=val;
  return retour;
  }


 public static int position(int[] tab,int val){
  if(tab!=null)
   for(int i=0;i<tab.length;i++)
    if(tab[i]==val) return i;
  return -1;
  }


 public static boolean estPresent(int[] tab,int val){
  if(position(tab,val)==-1) return false;
   else return true;
  }



 public static int valeurCorrespondante(int[][] tab,int index){
  if((tab!=null)&&(tab[0].length==2))
   for(int i=0;i<tab.length;i++)
    if(tab[i][0]==index) return tab[i][1];
  return 0;
  }


 public static int indexCorrespondant(int[][] tab,int index){
  if((tab!=null)&&(tab[0].length==2))
   for(int i=0;i<tab.length;i++)
    if(tab[i][0]==index) return i;
  return -1;
  }

 public static int[][] modifierIndex(int[][] tab,int index,int modification){
  if((tab!=null)&&(tab[0].length==2)){
   int i=indexCorrespondant(tab,index);
   if(i!=-1) tab[i][1]=tab[i][1]+modification;
   }
  return tab;
  }


 public static int[][] ajoutCoupleIndex(int[][] tab,int[] ajout){
  if(ajout==null) return tab;
  int[][] retour;
  if(tab==null) retour=new int[1][2];
   else{
    retour=new int[tab.length+1][2];
    for(int i=0;i<tab.length;i++) retour[i]=tab[i];
    }
  retour[retour.length-1]=(int[])ajout.clone();
  return retour;
  }


 public static int[][] ajoutIndex(int[][] tab1,int[][] tab2){
  if(tab2==null) return tab1;
  if(tab1==null) return (int[][])cloneTableau(tab2);
  if((tab1[0].length!=2)||(tab2[0].length!=2))
   throw new IllegalArgumentException("Index non conforme : la deuxième dimension du tableau n'a pas la bonne dimension");
  int[][] retour=(int[][])cloneTableau(tab1);
  for(int i=0;i<tab2.length;i++){
   int inter=indexCorrespondant(retour,tab2[i][0]);
   if(inter!=-1) retour[inter][1]=retour[inter][1]+tab2[i][1];
    else retour=ajoutCoupleIndex(retour,tab2[i]);
   }
  return retour;
  }



 public static <T> T[] fusion(T[] tab1, T[] tab2) {
    if (tab1 == null) return tab2;
    if (tab2 == null) return tab1;
    T[] result = Arrays.copyOf(tab1, tab1.length + tab2.length);
    System.arraycopy(tab2, 0, result, tab1.length, tab2.length);
    return result;
}


 public static int position(Object[] tab,Object val){
  if((tab!=null)&&(val!=null))
   for(int i=0;i<tab.length;i++)
    if(tab[i].equals(val)) return i;
  return -1;
  }


 public static boolean estPresent(Object[] tab,Object val){
  if(position(tab,val)==-1) return false;
   else return true;
  }


 public static Object cloneTableau(Object[] o){
  if(o==null) return null;
  Object o2=o.clone();
  Class c=o.getClass().getComponentType();

  if(c.isArray())
   if(!c.getComponentType().isPrimitive())
    for(int i=0;i<Array.getLength(o);i++)
     Array.set(o2,i,cloneTableau((Object[])Array.get(o,i)));
   else
    if(c.getComponentType()==Integer.TYPE)
     for(int i=0;i<Array.getLength(o);i++)
      Array.set(o2,i,((int[])Array.get(o,i)).clone());
    else
     if(c.getComponentType()==Float.TYPE)
     for(int i=0;i<Array.getLength(o);i++)
      Array.set(o2,i,((float[])Array.get(o,i)).clone());
    else
     if(c.getComponentType()==Double.TYPE)
     for(int i=0;i<Array.getLength(o);i++)
      Array.set(o2,i,((double[])Array.get(o,i)).clone());
    else
     if(c.getComponentType()==Boolean.TYPE)
     for(int i=0;i<Array.getLength(o);i++)
      Array.set(o2,i,((boolean[])Array.get(o,i)).clone());
    else
     if(c.getComponentType()==Character.TYPE)
     for(int i=0;i<Array.getLength(o);i++)
      Array.set(o2,i,((char[])Array.get(o,i)).clone());
    else
     if(c.getComponentType()==Long.TYPE)
     for(int i=0;i<Array.getLength(o);i++)
      Array.set(o2,i,((long[])Array.get(o,i)).clone());
    else
     if(c.getComponentType()==Short.TYPE)
     for(int i=0;i<Array.getLength(o);i++)
      Array.set(o2,i,((short[])Array.get(o,i)).clone());

  return o2;
  }


 public static int[] dimensionsTableau(Object o){
  int d=nombreDimensionsTableau(o);
  int[] retour=new int[d];
  Object o2=o;
  for(int i=0;i<d;i++){
   retour[i]=Array.getLength(o2);
   if(retour[i]==0) break;
    else o2=Array.get(o2,0);
   }
  return retour;
  }

 public static int nombreDimensionsTableau(Object o){
  int retour=0;
  Class c=o.getClass().getComponentType();
  while(c!=null){
   retour++;
   c=c.getComponentType();
   }
  return retour;
  }


 public static int[] listePosition(Object[] tab,Object val){
  int[] retour=null;
  if((tab!=null)&&(val!=null))
   for(int i=0;i<tab.length;i++)
    if(tab[i].equals(val)) ajout(retour,i);
  return retour;
  }



 }