// v 1.10 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.


package zIgzAg.html;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;


/** Cette classe permet de créer et d'écrire un document au format HTML.<br>
  * Elle contient diverses méthodes utiles pour manier les balises HTML.
  * @author Julien Buret
  * @version 1.1
  * @see BaliseHTML
  */
public class DocumentHTML{

 /** Une liste de caractères spéciaux à coder dans un document HTML.
  */
 private static final char[] caracteresSpeciaux={'À','Á','Á','Ç','È','É','Ê','Î','Ô','Ö','Ù','Ú','à','á','â','ç','è','é',
         'ê','î','ô','ö','ù','ú','û','ï'};

/** La traduction des caractères spéciaux à coder dans un document HTML.
  */
 private static final String[] traduction={"&#192;","&#193;","&#194;","&#199;","&#200;","&#201;","&#202;","&#206;","&#212;",
         "&#214;","&#217;","&#218;","&#224;","&#225;","&#226;","&#231;","&#232;","&#233;","&#234;","&#238;","&#244;",
         "&#246;","&#249;","&#250;","&#251;","&#239;"};

/** La taille par défaut du <code>StringBuffer</code> utilisé. Dans le cas de documents gigantesques ou petits,
  * il peut être utile pour des raisons de rapidité d'exécution de modifier cette taille en utilisant le constructeur
  * nécessaire.
  * @see DocumentHTML#DocumentHTML(String,BaliseHTML,int)
  */
 private static final int TAILLE_BUFFER_PAR_DEFAUT=100000;

/** La balise de départ du document.
  */
 private BaliseHTML document;

/** Le <code>StringBuffer</code> où sera stocké les caractères du document HTML avant son écriture.
  */
 private StringBuffer texte;

/** Le fichier ou sera écrit le document HTML.
  */
 private File fichier;

/** La taille du <code>Stringbuffer</code>.
  */
 private int tailleDuBuffer;

/** Un constructeur avec comme nom de fichier <tt>fichier</tt>, comme balise racine <tt>baliseRacine</tt> et
  * comme taille de buffer la taille de buffer par défaut.
  * @param fichier le nom du fichier.
  * @param baliseRacine la balise racine du document HTML.
  */
 public DocumentHTML(String nomFichier,BaliseHTML baliseRacine){
  this(nomFichier,baliseRacine,TAILLE_BUFFER_PAR_DEFAUT);
  }

/** Un constructeur avec comme nom de fichier <tt>fichier</tt>,comme balise racine <tt>baliseRacine</tt> et
  * comme taille de buffer <tt>tailleBuffer</tt>.
  * @param fichier le nom du fichier.
  * @param baliseRacine la balise racine du document HTML.
  * @param tailleBuffer la taille du <code>Stringbuffer</code>.
  */
 public DocumentHTML(String nomFichier,BaliseHTML baliseRacine,int tailleBuffer){
  tailleDuBuffer=tailleBuffer;
  fichier=new File(nomFichier);
  document=baliseRacine;
  }

 /** Une méthode pour traduire les caractères spéciaux dans un format lisible en mode HTML.
  */
 public static StringBuffer traduire(StringBuffer entree){
  int pos=0;
  char test;
  while(pos<entree.length()){
   test=entree.charAt(pos);
   if(Character.getNumericValue(test)==-1)
    for(int i=0;i<caracteresSpeciaux.length;i++)
     if(test==caracteresSpeciaux[i]){
      entree.deleteCharAt(pos);
      entree.insert(pos,traduction[i]);
      pos+=traduction[i].length()-1;
      break;
      }
   pos++;
   }
  return entree;
  }

/** Ecrit le document HTML dans le fichier spécifié lors de la création du document HTML.
  */
 public void ecrire(){
  texte=new StringBuffer(tailleDuBuffer);
  BaliseHTML.affiche(texte,document);
  traduire(texte);

  if(fichier.getParentFile()!=null) fichier.getParentFile().mkdirs();
  try{BufferedWriter fluxE=new BufferedWriter(new FileWriter(fichier));
      String doc=texte.toString();
      fluxE.write(doc,0,doc.length());
      fluxE.close();
      }
  catch(IOException e){e.printStackTrace();}
  }

  /** Renvoit une TABLE HTML en utilisant la méthode <code>toString()</code> des objets en paramètres.
   * Cette méthode n'est pas à utiliser <i>à priori</i>, car plus lente que les autres méthodes <i>creerTable()</i>.
   * @param tableau le tableau d'objects.
   * @return la balise contenant le tableau.
   */
 public static BaliseHTML creerTable(Object[][] tableau){
  BaliseHTML table=new BaliseHTML(BaliseHTML.TABLE);
  BaliseHTML ligne;
  BaliseHTML colonne;
  for(int i=0;i<tableau.length;i++){
   ligne=new BaliseHTML(BaliseHTML.TR);
   for(int j=0;j<tableau[0].length;j++){
    colonne=new BaliseHTML(BaliseHTML.TD,String.valueOf(tableau[i][j]));
    ligne.ajout(colonne);
    }
   table.ajout(ligne);
   }
  return table;
  }

 /** Renvoit une TABLE HTML en utilisant un <code>tableau</code> de Balises en paramètre qui représentent
   * les cellules de la TABLE.
   * De plus <code>lignes</code> permet de définir les paramètres des lignes.
   * @param tableau les Balises des cellules.
   * @param lignes les Balises des lignes.
   * @return la balise contenant le tableau.
   */
 public static BaliseHTML creerTable(BaliseHTML[][] tableau,BaliseHTML[] lignes){
  BaliseHTML table=new BaliseHTML(BaliseHTML.TABLE);
  for(int i=0;i<tableau.length;i++){
   for(int j=0;j<tableau[0].length;j++)
    lignes[i].ajout(tableau[i][j]);
   table.ajout(lignes[i]);
   }
  return table;
  }

 /** Renvoit une TABLE HTML en utilisant un <code>tableau</code> de Balises en paramètre qui représentent
   * les cellules de la TABLE.<br>
   * C'est la balise <tt>table</tt> qui est renvoyée.
   * @param table la Balise tableau où seront stockées les différentes lignes(cette balise doit être de type <tt>TABLE</tt>
   *               de préférence!).
   * @param tableau les Balises des cellules.
   * @return la balise <tt>table</tt> à laquelle on a rajouté le tableau.
   */
 public static BaliseHTML creerTable(BaliseHTML table,BaliseHTML[][] tableau){
  BaliseHTML ligne;
  boolean ligneValide;
  for(int i=0;i<tableau.length;i++){
   ligne=new BaliseHTML(BaliseHTML.TR);
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