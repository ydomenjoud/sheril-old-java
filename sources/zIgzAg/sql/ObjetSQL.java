// v 1.00 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.sql.SQLException;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;


public class ObjetSQL{

 private static final String CLE="c_";

 public static final byte TYPE_STRING=0;
 public static final byte TYPE_INT=1;
 public static final byte TYPE_FLOAT=2;
 public static final byte TYPE_BOOLEAN=3;
 public static final byte TYPE_LONG=4;
 public static final byte TYPE_DOUBLE=5;
 public static final byte TYPE_SHORT=6;
 public static final byte TYPE_BYTE=7;
 public static final byte TYPE_CHAR=8;
 public static final byte TYPE_OBJET=9;

 public static final String SQL_STRING="TEXT";
 public static final String SQL_INT="INT";
 public static final String SQL_FLOAT="FLOAT";
 public static final String SQL_BOOLEAN="TINYINT";
 public static final String SQL_LONG="BIGINT";
 public static final String SQL_DOUBLE="DOUBLE";
 public static final String SQL_SHORT="INT";
 public static final String SQL_BYTE="TINYINT";
 public static final String SQL_CHAR="CHAR";
 public static final String SQL_OBJET="INT";

 private PreparedStatement insertion;
 private Field[] champs;
 private byte[] types;

//Le constructeur "input"
 public ObjetSQL(Class c){
  champs=listeChampsObjet(c);

  }

//Le constructeur "output"
 public ObjetSQL(Connection connection,Class c) throws SQLException{
  champs=listeChampsObjet(c);
  determinerTypes();
  insertion=preparerInsertion(connection,c);
  creerTable(connection,c);
  }

//méthodes d'accès aux champs -->

 public Field[] getChamps(){return champs;}

 public byte[] getTypes(){return types;}

 public void determinerTypes(){
  types=new byte[champs.length];
  for(int i=0;i<types.length;i++)
   types[i]=getTypeSQL(champs[i].getType());
  }


//méthodes statiques utiles --->
 public static byte getTypeSQL(Class c){
  if(c.isPrimitive()){
   if(c==Integer.TYPE) return TYPE_INT;
    else if(c==Float.TYPE) return TYPE_FLOAT;
    else if(c==Boolean.TYPE) return TYPE_BOOLEAN;
    else if(c==Long.TYPE) return TYPE_LONG;
    else if(c==Double.TYPE) return TYPE_DOUBLE;
    else if(c==Short.TYPE) return TYPE_SHORT;
    else if(c==Byte.TYPE) return TYPE_BYTE;
    else if(c==Character.TYPE) return TYPE_CHAR;
    else throw new InternalError("type primitif non connu");
   }
  else{
    if(c==String.class) return TYPE_STRING;
    else if(c==Integer.class) return TYPE_INT;
    else if(c==Float.class) return TYPE_FLOAT;
    else if(c==Boolean.class) return TYPE_BOOLEAN;
    else if(c==Long.class) return TYPE_LONG;
    else if(c==Double.class) return TYPE_DOUBLE;
    else if(c==Short.class) return TYPE_SHORT;
    else if(c==Byte.class) return TYPE_BYTE;
    else if(c==Character.class) return TYPE_CHAR;
    else return TYPE_OBJET;
    }
  }

 public static String getTypeSQL(byte t){
  switch(t){
   case TYPE_INT:return SQL_INT;
   case TYPE_OBJET:return SQL_INT;
   case TYPE_STRING:return SQL_STRING;
   case TYPE_FLOAT:return SQL_FLOAT;
   case TYPE_BOOLEAN:return SQL_BOOLEAN;
   case TYPE_LONG:return SQL_LONG;
   case TYPE_SHORT:return SQL_SHORT;
   case TYPE_BYTE:return SQL_BYTE;
   case TYPE_CHAR:return SQL_CHAR;
   default:return null;
   }
  }


 public static String getNomTable(Class c){
  String retour=c.getName();
  //if(retour.indexOf('£')!=-1) throw new IllegalArgumentException();
  return retour.replace('.','_');
  }

 public static String getNomClasseTable(String nomTable){
  return nomTable.replace('_','.');
  }

 public static String getNomChamp(Field f){
  String retour=f.getName();
  if(retour.equals("type")) return "_type_";
  if(retour.equals("table")) return "_table_";
  //if(retour.indexOf('£')!=-1) throw new IllegalArgumentException();
  return retour.replace('.','_');
  }

 public static String getNomClasseChamp(String nomChamp){
  if(nomChamp.equals("_type_")) return "type";
  if(nomChamp.equals("_table_")) return "table";
  return nomChamp.replace('_','.');
  }

 public static boolean existenceMethode(Class c,String nom,Class[] param){
  try{
   c.getDeclaredMethod(nom,param);
   }
  catch(NoSuchMethodException e){
   return false;
   }
  catch(SecurityException e){
   e.printStackTrace();
   System.exit(0);
   }
  return true;
  }

 public static Field[] listeChampsClasse(Class c){
  if(!Serializable.class.isAssignableFrom(c)) return new Field[0];
  if(existenceMethode(c,"writeObject",new Class[]{ObjectOutputStream.class}))
   System.out.println("Attention,la classe "+c.getName()+" a une méthode writeObject"+
                       "qui ne peut bien sûr pas être prise en compte.");
  if(existenceMethode(c,"readObject",new Class[]{ObjectInputStream.class}))
   System.out.println("Attention,la classe "+c.getName()+" a une méthode readObject"+
                       "qui ne peut bien sûr pas être prise en compte.");


  Field[] f=c.getDeclaredFields();
  List a=new ArrayList(f.length);
  for(int i=0;i<f.length;i++)
   if((!Modifier.isTransient(f[i].getModifiers()))&&(!Modifier.isStatic(f[i].getModifiers())))
    a.add(f[i]);
  return (Field[])a.toArray(new Field[0]);
  }

 public static Field[] listeChampsObjet(Class o){
  List a=new ArrayList(Arrays.asList(listeChampsClasse(o)));
  Class c=o.getSuperclass();
  while((c!=null)&&(c!=Object.class)){
   a.addAll(Arrays.asList(listeChampsClasse(c)));
   c=c.getSuperclass();
   }
  Field[] retour=(Field[])a.toArray(new Field[0]);
  AccessibleObject.setAccessible(retour,true);
  return retour;
  }

//méthodes "output" --->
 public void executerInsertion() throws SQLException{
  insertion.execute();
  }

 protected PreparedStatement preparerInsertion(Connection connection,Class c)
                                  throws SQLException{

  StringBuffer sb=new StringBuffer(500);
  sb.append("INSERT INTO ");
  sb.append(getNomTable(c));
  sb.append(" (");
  sb.append(CLE);

  if(champs.length!=0){
   sb.append(',');
   for(int i=0;i<champs.length;i++){
    sb.append(getNomChamp(champs[i]));
    if(i!=champs.length-1) sb.append(',');
    }
   }

  sb.append(") VALUES (");
  for(int i=0;i<champs.length+1;i++){
   sb.append('?');
   if(i!=champs.length) sb.append(',');
   }
  sb.append(");");

  return connection.prepareStatement(sb.toString());
  }

 protected void creerTable(Connection connection,Class c) throws SQLException {
  StringBuffer sb=new StringBuffer(500);
  sb.append("CREATE TABLE ");
  sb.append(getNomTable(c));
  sb.append(" (");
  sb.append(CLE);
  sb.append(" INT PRIMARY KEY");

  if(champs.length!=0){
   sb.append(',');
   for(int i=0;i<champs.length;i++){
    sb.append(getNomChamp(champs[i]));
    sb.append(' ');
    sb.append(getTypeSQL(types[i]));
    if(i!=champs.length-1) sb.append(',');
    }
   }

  sb.append(");");

  Statement s=connection.createStatement();
  s.execute(sb.toString());
  s.close();
  }

 public void put(Object o,int num,byte type) throws SQLException{
  if(o==null)
   insertion.setNull(num,Types.NULL);

  switch(type){
   case TYPE_OBJET:  insertion.setInt(num,((Integer)o).intValue());break;
   case TYPE_INT:    insertion.setInt(num,((Integer)o).intValue());break;
   case TYPE_STRING: insertion.setString(num,(String)o);break;
   case TYPE_FLOAT:  insertion.setFloat(num,((Float)o).floatValue());break;
   case TYPE_BOOLEAN:insertion.setBoolean(num,((Boolean)o).booleanValue());break;
   case TYPE_LONG:   insertion.setLong(num,((Long)o).longValue());break;
   case TYPE_SHORT:  insertion.setShort(num,((Short)o).shortValue());;break;
   case TYPE_BYTE:   insertion.setByte(num,((Byte)o).byteValue());break;
   case TYPE_CHAR:   insertion.setString(num,o.toString());break;
   default:          break;
   }
  }

//méthodes "input" --->
/*
 public static ObjetSQL getObjectSQL(String nomTable){
  try{
      Class c=Class.forName(getNomClasseTable(nomTable));
     }
  catch(Exception e){
   e.printStackTrace();
   return null;
   }

  }

*/

 }
