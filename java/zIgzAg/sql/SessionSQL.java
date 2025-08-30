// v 1.00 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.sql;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SessionSQL{

 public SessionSQL(){};

 // méthode abstraite persistante --->

 public abstract Connection getConnection(String host,String base,
                                                 String login,String motDePasse);

 // méthode plus simple -->

 public Connection getConnection(String host,String base){
  return getConnection(host,base,null,null);
  }


 // méthodes abstraites à implémenter dans cette classe plus tard --->

 public abstract int nombreDeLignesTable(Connection c,String nomTable);

 public abstract String[] getNomsColonnes(Connection c,String nomTable);

 public abstract String[] getTypesColonnes(Connection c,String nomTable);

 public abstract String[] listeTables(Connection c);

 //autres méthodes --->

 public void fermerConnection(Connection c){
  try{
   c.close();
   }
  catch(SQLException e){
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }
  }

 public Statement getStatement(Connection c){
  Statement s=null;
  try{s=c.createStatement();
     }
  catch(SQLException e){
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }
  return s;
  }

 public void fermerStatement(Statement s){
  try{
   s.close();
   }
  catch(SQLException e){
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }
  }

 public void update(Statement s,String commande){
  try{
   s.executeUpdate(commande);
   }
  catch (SQLException e) {
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }
  }

 public void execute(Statement s,String commande){
  try{
   s.execute(commande);
   }
  catch (SQLException e) {
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }
  }

 public ResultSet query(Statement s,String commande){
  try{
   return s.executeQuery(commande);
   }
  catch(SQLException E){
   System.out.println("SQLException: " + E.getMessage());
   System.out.println("SQLState:     " + E.getSQLState());
   System.out.println("VendorError:  " + E.getErrorCode());
   }
  return null;
  }

 public void creerBase(Statement s,String base){
  execute(s,"CREATE DATABASE "+base);
  }

 public void effacerTable(Statement s,String table){
  execute(s,"DROP TABLE "+table);
  }

 public void effacerToutesTables(Connection c){
  String[] liste=listeTables(c);
  Statement s=getStatement(c);
  for(int i=0;i<liste.length;i++)
   effacerTable(s,liste[i]);
  fermerStatement(s);
  }

 public void viderTable(Statement s,String table){
  execute(s,"DELETE * FROM "+table);
  }

 public void viderToutesTables(Connection c){
  String[] liste=listeTables(c);
  Statement s=getStatement(c);
  for(int i=0;i<liste.length;i++)
   viderTable(s,liste[i]);
  fermerStatement(s);
  }

 public void creerTable(Statement s,String nomTable,String[] champs){
  execute(s,"CREATE TABLE "+nomTable+"("+champsTraduction1(champs)+")");
  }

 public void creerTable(Statement s,String nomTable,String[] nomsC,String[] typesC){
  execute(s,"CREATE TABLE "+nomTable+"("+champsTraduction4(nomsC,typesC)+")");
  }


  // sépare chaque champs par des virgules --->
 public String champsTraduction1(String[] t){
  StringBuffer retour=new StringBuffer(50);
  for(int i=0;i<t.length;i++){
   retour.append(t[i]);
   if(i!=t.length-1) retour.append(",");
   }
  return retour.toString();
  }

 //sépare chaque valeur par des virgules  --->
 public String champsTraduction2(String[] t){
  StringBuffer retour=new StringBuffer(50);
  for(int i=0;i<t.length;i++){
   retour.append('\'');
   retour.append(t[i]);
   retour.append('\'');
   if(i!=t.length-1) retour.append(",");
   }
  return retour.toString();
  }

 //pour les conditions  --->
 public String champsTraduction3(String[] c,String[] v){
  StringBuffer retour=new StringBuffer(100);
  for(int i=0;i<c.length;i++){
   retour.append(c[i]);
   retour.append("='");
   retour.append(v[i]);
   retour.append('\'');
   if(i!=c.length-1) retour.append(" AND ");
   }
  return retour.toString();
  }

 //pour avoir des string du type "num INT,welldone BIGINT" etc.
 public String champsTraduction4(String[] c,String[] v){
  StringBuffer retour=new StringBuffer(100);
  for(int i=0;i<c.length;i++){
   retour.append(c[i]);
   retour.append(" ");
   retour.append(v[i]);
   if(i!=c.length-1) retour.append(',');
   }
  return retour.toString();
  }

 public void insererLigne(Statement s,String nomTable,String[] champs,String[] valeurs){
  update(s,"INSERT INTO "+nomTable+"("+champsTraduction1(champs)+") values ("+champsTraduction2(valeurs)+")");
  }

 public ResultSet selectionnerTout(Statement s,String nomTable){
  return query(s,"SELECT * FROM "+nomTable);
  }

 public ResultSet selectionner(Statement s,String nomTable,String[] champs,String[] valeurs){
  return query(s,"SELECT * FROM "+nomTable+" WHERE "+champsTraduction3(champs,valeurs));
  }

 public int nombreLignesResultSet(ResultSet r){
  int nb=0;
  try{while(r.next()) nb++;}
  catch(SQLException e){
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }
  return nb;
  }


 }