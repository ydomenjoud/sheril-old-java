// v 1.00 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.sql;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
//import zIgzAg.collection.IntMap;
//import zIgzAg.collection.IntHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;


public class InputSQLWriter{
/*
 private Connection connection;
 private IntMap registreChargement;
 private Map registreClasses;

 public InputSQLWriter(Connection connection){
  this.connection=connection;
  this.registreChargement=new IntHashMap();
  registreClasses=new HashMap();
  }

// accéder et remplir le registre des ObjetSQL -->

 public void putObjetSQL(int valeur,Object o){
  registreChargement.put(valeur,o);
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

//méthode de départ --->

 public Object chargerObjet(Object o,int index){




  }


// méthode principale --->

 public Object charger(Object o,int index){
  if(index==-1) return null;
  else
   if(o.getClass().isArray()) return chargerTable(index);
    else if(o instanceof List) {
     ((List)o).addAll(Arrays.asList((Object[])chargerTable(index)));
     return o;
     }
   else if(o instanceof Map) return chargerMap((Map)o,index);

  else{
   Class c=o.getClass();
   Field[] f=null;
   if(champsClasseEnregistree(c)) f=retourneChampsClasse(c);
    else{
     f=listeColonnes(o);
     ajouterChampsClasse(c,f);
     }

   String table=getTraductionChamp(c.getName());
   String[] listeChamps=(String[])descriptionTables.get(table);

   Integer cle=new Integer(index);
   String[] r=getResultat(table,cle);
   if(o instanceof String) o=(r[0]==null ? null : r[0].replace(STRING_REMPLACEMENT_CHAR,'\''));
    else
     for(int i=0;i<f.length;i++){
      int pos=Mdt.position(listeChamps,getTraductionChamp(f[i].getName()));
      if(pos!=-1)
       if(getType(f[i].getType())!=TYPE_AUTRE) setTypeSimple(o,f[i],r[pos-1]);
         else try{
               if(r[pos-1]!=null)
                f[i].set(o,chargerObjet(traductionChargementClasse(f[i].getType()),Integer.parseInt(r[pos-1])));
              }catch(Exception e){e.printStackTrace();}
      }

  System.out.println("Chargement de "+o.toString());
  supprimerResultat(table,cle);
  return o;
  }
 }


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
    oSQL.put(new Integer(MARQUEUR++),1,ObjetSQL.TYPE_INT);
    for(int i=0;i<f.length;i++)
     oSQL.put(valeurs.get(i),i+2,b[i]);
    oSQL.executerInsertion();
    }
   catch(SQLException e){
    System.out.println("erreur2 avec la classe : "+o.getClass().getName());
    e.printStackTrace();
    }

   return MARQUEUR;
   }
  }







public class InputSQLWriter{

 private HashMap descriptionTables;
 private HashMap resultat;
 private boolean chargerInteger=false;

 public ChargerObjetMysql(String host,String nomBase){
  super(host,nomBase);
  chargerDescriptionTables();
  chargerResultats();
  System.out.println("Phase accès fichier du chargement terminée");
  }

 public void chargerDescriptionTables(){
  String[] liste=SessionMysql.listeTables(connection);
  descriptionTables=new HashMap(liste.length);
  for(int i=0;i<liste.length;i++)
   descriptionTables.put(liste[i],SessionMysql.getNomsColonnes(connection,liste[i]));
  }

 public void chargerResultats(){
  resultat=new HashMap();
  String[] liste=SessionMysql.listeTables(connection);
  System.out.println("Chargement en cours...");
  for(int i=0;i<liste.length;i++){
   boolean ok=true;
   if(!chargerInteger) if(liste[i].equals("java_lang_integer")) ok=false;
   if (ok)
   try{
    String[] listeColonnes=(String[])descriptionTables.get(liste[i]);
    HashMap h=new HashMap(SessionMysql.nombreDeLignesTable(connection,liste[i]));
    Statement s=connection.createStatement();
    ResultSet r=SessionMysql.selectionnerTout(s,liste[i]);
    while (r.next()){
     String[] inter=new String[listeColonnes.length-1];
     for(int j=0;j<listeColonnes.length-1;j++)
      if(r.getObject(j+2)!=null) inter[j]=r.getString(j+2);
     h.put(new Integer(r.getInt(1)),inter);
     }
    r.close();
    s.close();
    resultat.put(liste[i],h);
    System.out.println("Table "+Integer.toString(i+1)+" sur "+Integer.toString(liste.length)+" chargée");
    }
   catch(SQLException e){
    System.out.println("SQLException: " + e.getMessage());
    System.out.println("SQLState:     " + e.getSQLState());
    System.out.println("VendorError:  " + e.getErrorCode());
    }
   }
  }

 public String[] getResultat(String nomTable,Integer cle){
  if((chargerInteger)||(!nomTable.equals("java_lang_integer")))
   return (String[])((HashMap)resultat.get(nomTable)).get(cle);

  try{
   Statement s=connection.createStatement();
   String[] i1=new String[1];
   i1[0]=CLE;
   String[] i2=new String[1];
   i2[0]=cle.toString();
   ResultSet r=SessionMysql.selectionner(s,nomTable,i1,i2);
   String[] o=new String[1];
   r.next();
   o[0]=r.getString(1);
   return o;
   }
   catch(SQLException e){
    System.out.println("SQLException: " + e.getMessage());
    System.out.println("SQLState:     " + e.getSQLState());
    System.out.println("VendorError:  " + e.getErrorCode());
    System.exit(0);
    return null;
    }
  }

 public void supprimerResultat(String nomTable,Integer cle){
  if((chargerInteger)||(!nomTable.equals("java_lang_integer")))
  ((HashMap)resultat.get(nomTable)).remove(cle);
  }

 public void setTypeSimple(Object o,Field f,String r){
  try{
  int type=getType(f.getType());
  if(type==TYPE_STRING) f.set(o,(r==null ? null : r.replace(STRING_REMPLACEMENT_CHAR,'\'')));
  else if(type==TYPE_INT) f.setInt(o,Integer.parseInt(r));
  else if(type==TYPE_FLOAT) f.setFloat(o,Float.parseFloat(r));
  else if(type==TYPE_DOUBLE) f.setDouble(o,Double.parseDouble(r));
  else if(type==TYPE_BOOLEAN) f.setBoolean(o,(r.equals("1") ? true : false));
  else if(type==TYPE_SHORT) f.setShort(o,Short.parseShort(r));
  else if(type==TYPE_LONG) f.setLong(o,Long.parseLong(r));
  else if(type==TYPE_CHAR) f.setChar(o,r.charAt(0));
  else System.out.println("erreur setTypeSimple");
  }
  catch(Exception e){e.printStackTrace();}
  }

 public Map chargerMap(Map h,int index){
  Integer cle=new Integer(index);
  String[] r=getResultat(MAP,cle);
  if(r[0]!=null){
   Object cleO=chargerTable(Integer.parseInt(r[0]));
   Object valeurO=chargerTable(Integer.parseInt(r[1]));
   for(int i=0;i<Array.getLength(cleO);i++)
    h.put(Array.get(cleO,i),Array.get(valeurO,i));
   }

  supprimerResultat(MAP,cle);
  return h;
  }

 public Object chargerTable(int index){
  Integer cle=new Integer(index);
  String[] r=getResultat(TABLEAU,cle);

  String tableauDimension=r[1];
  String tableauType=r[0];
  int tableauIndex=Integer.parseInt(r[2]);

  Class c=null;
  if(tableauType.equals(Integer.TYPE.getName())) c=Integer.TYPE;
  else if(tableauType.equals(Float.TYPE.getName())) c=Float.TYPE;
  else if(tableauType.equals(Boolean.TYPE.getName())) c=Boolean.TYPE;
  else if(tableauType.equals(Double.TYPE.getName())) c=Double.TYPE;
  else if(tableauType.equals(Short.TYPE.getName())) c=Short.TYPE;
  else if(tableauType.equals(Long.TYPE.getName())) c=Long.TYPE;
  else if(tableauType.equals(Character.TYPE.getName())) c=Character.TYPE;
  else try{c=Class.forName(tableauType);}catch(Exception e){e.printStackTrace();}

  int[] d=dimensionTraduction(tableauDimension);

  Object o=Array.newInstance(c,d);
  int typeC=getType(c);

 //pas plus de deux dimensions pour l'instant.

  if(d.length==1)
   for(int i=0;i<d[0];i++)
    remplirTableau(o,i,c,typeC,tableauIndex++);

  else

  if(d.length==2)
   for(int i=0;i<d[0];i++){
   Object o2=Array.get(o,i);
   for(int j=0;j<d[1];j++)
    remplirTableau(o2,j,c,typeC,tableauIndex++);
   }
  else{System.out.println("Taille tableau non supportée");System.exit(0);}

 supprimerResultat(TABLEAU,cle);
 return o;
 }

 public void remplirTableau(Object tab,int i,Class c,int typeC,int index){
  if((typeC==TYPE_AUTRE)||(typeC==TYPE_STRING))
   Array.set(tab,i,chargerObjet(traductionChargementClasse(c),index));
  else if(typeC==TYPE_INT)
   Array.setInt(tab,i,((Integer)chargerObjet(traductionChargementClasse(c),index)).intValue());
  else if(typeC==TYPE_DOUBLE)
   Array.setDouble(tab,i,((Double)chargerObjet(traductionChargementClasse(c),index)).doubleValue());
  else if(typeC==TYPE_FLOAT)
   Array.setFloat(tab,i,((Float)chargerObjet(traductionChargementClasse(c),index)).floatValue());
  else if(typeC==TYPE_LONG)
   Array.setLong(tab,i,((Long)chargerObjet(traductionChargementClasse(c),index)).longValue());
  else if(typeC==TYPE_SHORT)
   Array.setShort(tab,i,((Short)chargerObjet(traductionChargementClasse(c),index)).shortValue());
  else if(typeC==TYPE_BOOLEAN)
   Array.setBoolean(tab,i,((Boolean)chargerObjet(traductionChargementClasse(c),index)).booleanValue());
  else if(typeC==TYPE_CHAR)
   Array.setChar(tab,i,((Character)chargerObjet(traductionChargementClasse(c),index)).charValue());
  }

 public Object chargerObjet(Object o,int index){
  if(index==Integer.MAX_VALUE) return null;
  else
   if(o.getClass().isArray()) return chargerTable(index);
    else if(o instanceof List) {
     ((List)o).addAll(Arrays.asList((Object[])chargerTable(index)));
     return o;
     }
   else if(o instanceof Map) return chargerMap((Map)o,index);

  else{
   Class c=o.getClass();
   Field[] f=null;
   if(champsClasseEnregistree(c)) f=retourneChampsClasse(c);
    else{
     f=listeColonnes(o);
     ajouterChampsClasse(c,f);
     }

   String table=getTraductionChamp(c.getName());
   String[] listeChamps=(String[])descriptionTables.get(table);

   Integer cle=new Integer(index);
   String[] r=getResultat(table,cle);
   if(o instanceof String) o=(r[0]==null ? null : r[0].replace(STRING_REMPLACEMENT_CHAR,'\''));
    else
     for(int i=0;i<f.length;i++){
      int pos=Mdt.position(listeChamps,getTraductionChamp(f[i].getName()));
      if(pos!=-1)
       if(getType(f[i].getType())!=TYPE_AUTRE) setTypeSimple(o,f[i],r[pos-1]);
         else try{
               if(r[pos-1]!=null)
                f[i].set(o,chargerObjet(traductionChargementClasse(f[i].getType()),Integer.parseInt(r[pos-1])));
              }catch(Exception e){e.printStackTrace();}
      }

  System.out.println("Chargement de "+o.toString());
  supprimerResultat(table,cle);
  return o;
  }
 }

*/


 }
