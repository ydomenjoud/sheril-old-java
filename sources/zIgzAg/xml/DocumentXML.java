// v 1.10 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.


package zIgzAg.xml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;


public class DocumentXML{

 private static final int TAILLE_BUFFER_PAR_DEFAUT=100000;

 private BaliseXML document;

 private StringBuffer texte;

 private File fichier;

 private int tailleDuBuffer;

 public DocumentXML(String nomFichier,BaliseXML baliseRacine){
  this(nomFichier,baliseRacine,TAILLE_BUFFER_PAR_DEFAUT);
  }

 public DocumentXML(String nomFichier,BaliseXML baliseRacine,int tailleBuffer){
  tailleDuBuffer=tailleBuffer;
  fichier=new File(nomFichier);
  document=baliseRacine;
  }

 public static StringBuffer traduire(StringBuffer entree){
  return entree;
  }

 public void ecrire(){
  texte=new StringBuffer(tailleDuBuffer);
  BaliseXML.affiche(texte,document);
  traduire(texte);

  if(fichier.getParentFile()!=null) fichier.getParentFile().mkdirs();
  try{BufferedWriter fluxE=new BufferedWriter(new FileWriter(fichier));
      String doc=texte.toString();
      fluxE.write(doc,0,doc.length());
      fluxE.close();
      }
  catch(IOException e){e.printStackTrace();}
  }

 public static BaliseXML creerTable(Object[][] tableau){
  BaliseXML table=new BaliseXML(BaliseXML.TABLE);
  BaliseXML ligne;
  BaliseXML colonne;
  for(int i=0;i<tableau.length;i++){
   ligne=new BaliseXML(BaliseXML.TR);
   for(int j=0;j<tableau[0].length;j++){
    colonne=new BaliseXML(BaliseXML.TD,String.valueOf(tableau[i][j]));
    ligne.ajout(colonne);
    }
   table.ajout(ligne);
   }
  return table;
  }

 public static BaliseXML creerTable(BaliseXML[][] tableau,BaliseXML[] lignes){
  BaliseXML table=new BaliseXML(BaliseXML.TABLE);
  for(int i=0;i<tableau.length;i++){
   for(int j=0;j<tableau[0].length;j++)
    lignes[i].ajout(tableau[i][j]);
   table.ajout(lignes[i]);
   }
  return table;
  }

 public static BaliseXML creerTable(BaliseXML table,BaliseXML[][] tableau){
  BaliseXML ligne;
  boolean ligneValide;
  for(int i=0;i<tableau.length;i++){
   ligne=new BaliseXML(BaliseXML.TR);
   ligneValide=false;
   for(int j=0;j<tableau[0].length;j++)
    if(tableau[i][j]!=null){
     ligne.ajout(tableau[i][j]);
     ligneValide=true;
     }
   if(ligneValide) table.ajout(ligne);
   }
  return table;
  }

 }