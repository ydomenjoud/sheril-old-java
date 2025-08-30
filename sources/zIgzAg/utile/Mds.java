// v 1.00 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.


package zIgzAg.utile;


/**
  *
  * @author Julien Buret
  * @version 1.0
  */
public class Mds{

 public static String remplacer(String sujet,String cherche,String remplace){
  String retour=null;
  String inter=sujet;
  int index=0;
  while((index=inter.indexOf(cherche))!=-1){
   retour=(retour==null ? "" : retour)+inter.substring(0,index)+remplace;
   inter=inter.substring(index+cherche.length(),inter.length());
   }
  if (retour!=null) retour=retour+inter;

  if(retour==null) return sujet;
   else return retour;
  }

}