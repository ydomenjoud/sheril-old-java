// v2.01 03/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.01, 03/02/01
 */

import java.io.Serializable;
import java.util.Locale;
import zIgzAg.utile.Mdt;

public class Produit extends Technologie implements Serializable{

 private int mineraiNecessaire;
 private float prix;
 private int[][] marchandisesNecessaires;

 //les méthodes d'accès

 public int getMineraiNecessaire(){return mineraiNecessaire;}
 public float getPrix(){return prix;}
 public int[][] getMarchandises(){return marchandisesNecessaires;}

 public int getQuantiteMarchandise(int marchandise){
  return Mdt.valeurCorrespondante(marchandisesNecessaires,marchandise);
  }

 public String getListeMarchandises(Locale l){
  if(marchandisesNecessaires==null) return "&nbsp;";
  String retour=new String();
  for(int i=0;i<marchandisesNecessaires.length;i++){
   retour=retour+Utile.maj(Univers.getMessage("MARCHANDISES",marchandisesNecessaires[i][0],l))+" : "+
       Integer.toString(marchandisesNecessaires[i][1]);
   if(i!=marchandisesNecessaires.length-1) retour=retour+"<BR>";
   }
  return retour;
  }

 public int getNombreMarchandises(){
  if (marchandisesNecessaires==null) return 0;
   else return marchandisesNecessaires.length;
  }

 //Le constructeur

 protected Produit(){}

 public Produit(String code,int niv,String[] parent,int recherche,int[][] caracS,int minerai,float pri,int[][] mar){
  super(code,niv,parent,recherche,caracS);
  mineraiNecessaire=minerai;
  prix=pri;
  marchandisesNecessaires=mar;
  }


 //les autres méthodes






 }
