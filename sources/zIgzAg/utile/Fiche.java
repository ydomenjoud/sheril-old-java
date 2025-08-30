// v 1.00 01/01/01
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


public class Fiche{


 public static void initialisation(String fichier){
   File fiche=new File(fichier);
   if (fiche.exists()) fiche.delete();
  }


 public static void ecriture(String fichier,String ajout){
  try{BufferedWriter fluxE=new BufferedWriter(new FileWriter(fichier,true));
      fluxE.write(ajout,0,ajout.length());
      fluxE.newLine();
      fluxE.close();
      }
  catch(IOException e){e.printStackTrace();}
  }


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