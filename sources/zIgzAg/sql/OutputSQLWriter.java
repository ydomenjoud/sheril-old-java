// v 1.00 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.sql;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.lang.reflect.Field;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;

public class OutputSQLWriter{

 public static int MARQUEUR;

 private Connection connection;
 private Map registre;


 public OutputSQLWriter(Connection connection){
  this.connection=connection;
  this.registre=new HashMap();
  MARQUEUR=0;
  }

// accéder et remplir le registre des Objets SQL -->

 public ObjetSQL getObjetSQL(Class c){
  Object o=registre.get(c);
  if(o==null){
   ObjetSQL oSQL=null;
   try{oSQL=new ObjetSQL(connection,c);
      }
   catch(SQLException e){e.printStackTrace();System.out.println("---"+c+"---");System.exit(0);}
   registre.put(c,oSQL);
   return oSQL;
   }
   else return (ObjetSQL)o;
  }

// méthodes pour type de classes spécial --->

 public int ajouterMap(Map m){
  int cles=ajouterTable(m.keySet().toArray());
  int valeurs=ajouterTable(m.values().toArray());
  MapSQL mSQL=new MapSQL(m,cles,valeurs);
  return ajouterObjet(mSQL);
  }

 public int ajouterTable(Object o){
  int taille=Array.getLength(o);
  int[] tab=new int[taille];
  for(int i=0;i<taille;i++)
   tab[i]=ajouterObjet(Array.get(o,i));
  TableSQL t=new TableSQL(o,tab);
  return ajouterObjet(t);
  }

 public int ajouterCollection(Collection o){
  int table=ajouterTable(o.toArray());
  CollectionSQL c=new CollectionSQL(o,table);
  return ajouterObjet(c);
  }

// méthode principale --->

 public int ajouterObjet(Object o){
  if(o==null) return -1;
  else
 //cas particuliers
   //tableau
   if(o.getClass().isArray()) return ajouterTable(o);
   //Collection
    else if(o instanceof Collection) return ajouterCollection((Collection)o);
   //Map
    else if(o instanceof Map) return ajouterMap((Map)o);
 //Cas général
  else{
   Class c=o.getClass();

   //cas particulier si l'objet est un String
   if(c==String.class){
    o=new StringSQL((String)o);
    c=o.getClass();
    }

   ObjetSQL oSQL=getObjetSQL(c);
   Field[] f=oSQL.getChamps();
   byte[] b=oSQL.getTypes();

   List valeurs=new ArrayList();

   try{
    for(int i=0;i<f.length;i++)
     if(b[i]==ObjetSQL.TYPE_OBJET) valeurs.add(new Integer(ajouterObjet(f[i].get(o))));
      else valeurs.add(f[i].get(o));
    }
   catch(Exception e){
    System.out.println("erreur1 avec la classe : "+o.getClass().getName());
    e.printStackTrace();
    }

   try{
    oSQL.put(new Integer(MARQUEUR),1,ObjetSQL.TYPE_INT);
    for(int i=0;i<f.length;i++)
     oSQL.put(valeurs.get(i),i+2,b[i]);
    oSQL.executerInsertion();
    }
   catch(SQLException e){
    System.out.println("erreur2 avec la classe : "+o.getClass().getName());
    e.printStackTrace();
    }

   return MARQUEUR++;
   }
  }


 }
