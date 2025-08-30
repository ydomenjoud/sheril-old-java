// v 1.00 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.




package zIgzAg.utile;

import java.io.*;
import java.util.zip.*;

public class Copie{

 public static void copieRepertoire(String source,String destination){
  File rep=new File(source);
  String[] liste=rep.list();
  File dest=new File(destination);
  dest.mkdirs();
  for(int i=0;i<liste.length;i++)
   copieFichier(source+"/"+liste[i],destination+"/"+liste[i]);
  }

 public static void copieFichier(String source,String destination){
  File f=new File(destination);
  //f.mkdirs();

   try{FileOutputStream fos=new FileOutputStream(destination);
       FileInputStream fis = new FileInputStream(source);

       int N = (int) fis.available();
       byte[] buf = new byte[N];
       fis.read(buf, 0, buf.length);
       fos.write(buf, 0, buf.length);

       fos.close();
       fis.close();

     }catch(Exception e){e.printStackTrace();}

  }

 public static void copie(String source,String destination){
  File f=new File(source);
  if(f.isDirectory()) copieRepertoire(source,destination);
   else copieFichier(source,destination);
  }

 public static void zipperRepertoire(ZipOutputStream zos,File rep,String depart){
  try{
   File[] liste=rep.listFiles();
   for(int i=0;i<liste.length;i++)
    zipperFichier(zos,liste[i],depart);
   }
  catch(Exception e){e.printStackTrace();}

  }

 public static void zipperFichier(ZipOutputStream zos,File fichier,String depart){
  try{
   if(fichier.isDirectory()) zipperRepertoire(zos,fichier,depart+fichier.getName()+"/");
     else{
      ZipEntry ze=new ZipEntry(depart+fichier.getName());
      zos.putNextEntry(ze);
      FileInputStream fis = new FileInputStream(fichier);
      int N =(int)fichier.length();
      byte[] buf = new byte[N];
      fis.read(buf, 0, buf.length);
      zos.write(buf, 0, buf.length);
      zos.closeEntry();
      fis.close();
      }
   }
  catch(Exception e){e.printStackTrace();}
  }

 public static void zipper(String[] source,String repertoireArchive,String nomArchive){
  File dest=new File(repertoireArchive);
  dest.mkdirs();

  try{
   ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(repertoireArchive+"/"+nomArchive));
   for(int i=0;i<source.length;i++){
    File f=new File(source[i]);
    zipperFichier(zos,f,"");
    }
   zos.close();
   }
  catch(Exception e){e.printStackTrace();}
  }


 }