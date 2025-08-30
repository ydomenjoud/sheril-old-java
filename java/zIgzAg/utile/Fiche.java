// v 1.01 24/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.


package zIgzAg.utile;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/** Cette classe contient des méthodes statiques permettant de créer et d'accéder à une "fiche" de carnet de bord système.
  * Chaque ligne correspond à un nouvel ajout.
  * @author Julien Buret
  * @version 1.01 24/02/01
  */
public class Fiche{

/** cette méthode efface le fichier spécifié si il existe.
  * @param fichier la localisation du fichier.
  */
 public static void initialisation(String fichier){
   File fiche=new File(fichier);
   if (fiche.exists()) fiche.delete();
  }

/** cette méthode ajoute au fichier contenant les informations l'information <code>ajout</code>.
  * Si le fichier n'existait pas précédemment, il est créé.<br><br>
  * <b>ATTENTION</b> : ne pas mettre de caractère &quot; retour à la ligne &quot; dans la <code>String</code> ajout :
  * cela fausserait les méthodes &quot; lecture &quot; de cette classe.
  * @param fichier la localisation du fichier.
  * @param ajout l'information à rajouter.
  */
 public static void ecriture(String fichier,String ajout){
  try{BufferedWriter fluxE=new BufferedWriter(new FileWriter(fichier,true));
      fluxE.write(ajout,0,ajout.length());
      fluxE.newLine();
      fluxE.close();
      }
  catch(IOException e){e.printStackTrace();}
  }

/** cette méthode retourne l'information contenue dans le fichier à l'index <tt>index</tt>.
  * @param fichier la localisation du fichier.
  * @param index le numéro de l'information.
  * @return une <code>String</code> contenant les informations, <tt>null</tt> si le fichier ou l'index n'existent pas.
  */
 public static String lecture(String fichier,int index){
  File fiche=new File(fichier);
  if (!fiche.exists()) return null;
  String retour=null;

  try{
      BufferedReader fluxLu=new BufferedReader(new FileReader(fiche));
      try{for(int i=0;i<index;i++) retour=fluxLu.readLine();
         }
      catch(IOException e){e.printStackTrace();}
      finally {fluxLu.close();}
     }
  catch(IOException e){e.printStackTrace();}

  return retour;
  }

/** cette méthode retourne l'ensemble des informations contenues dans le fichier sous forme de tableau de
  * <code>String</code>.
  * @param fichier la localisation du fichier.
  * @return une <code>String</code> contenant les informations, ou <tt>null</tt> si le fichier n'existe pas.
  */
 public static String[] lecture(String fichier){
  File fiche=new File(fichier);
  if (!fiche.exists()) return null;
  String inter=new String();
  Vector stock=new Vector();

  try{
      BufferedReader fluxLu=new BufferedReader(new FileReader(fiche));
      try{while(inter!=null){
           inter=fluxLu.readLine();
           if (inter!=null) stock.add(inter);
           }
         }
      catch(IOException e){e.printStackTrace();}
      finally {fluxLu.close();}
     }
  catch(IOException e){e.printStackTrace();}

  return (String[])stock.toArray(new String[0]);
  }

 /** cette méthode retourne l'ensemble des informations contenues dans le fichier sous forme d'un
  * <code>StringBuffer</code>.
  * @param fichier la localisation du fichier.
  * @return une <code>StringBuffer</code> contenant les informations, ou <tt>null</tt> si le fichier n'existe pas.
  */
 public static StringBuffer lectureBuffer(String fichier){
  File fiche=new File(fichier);
  if (!fiche.exists()) return null;
  StringBuffer retour=new StringBuffer(1000);

  try{BufferedReader fluxLu=new BufferedReader(new FileReader(fiche));
      try{String inter=fluxLu.readLine();
          while(inter!=null){
           retour.append(inter);retour.append('\n');
           inter=fluxLu.readLine();
           }
         }
      catch(IOException e){e.printStackTrace();}
      finally {fluxLu.close();}
     }
  catch(IOException e){e.printStackTrace();}

  return retour;
  }

}