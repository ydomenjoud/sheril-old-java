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

public class SessionMysql extends SessionSQL{

 public SessionMysql(){};

 public Connection getConnection(String host,String base,String login,String motDePasse){
  Connection c=null;
  try{Class.forName("org.gjt.mm.mysql.Driver").newInstance();
     }
  catch(Exception e){
   System.err.println("Impossible de charger le driver.");
   e.printStackTrace();
   System.exit(0);
   }

  String inter="jdbc:mysql://"+host+"/"+base;
  if(login!=null) inter=inter+"?user="+login+"&password="+motDePasse;

  try{c=DriverManager.getConnection(inter);
     }
  catch(SQLException e){
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }

  return c;
  }


 // méthodes temporaires "spécial mysql" --->

  public String[] listeTables(Connection c){
  ArrayList a=new ArrayList();
  try{
   Statement s=c.createStatement();
   ResultSet r=query(s,"SHOW TABLES");
   while (r.next()) a.add(r.getString(1));
   r.close();
   s.close();
   }
  catch(SQLException e){
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }
  return (String[])a.toArray(new String[0]);
  }


 public int nombreDeLignesTable(Connection c,String nomTable){
  int retour=0;
  try{
   Statement s=c.createStatement();
   ResultSet r=query(s,"SHOW TABLE STATUS");
   while (r.next()) if(r.getString(1).equals(nomTable)) retour=r.getInt("Rows");
   r.close();
   s.close();
   }
  catch(SQLException e){
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }
  return retour;
  }



 public String[] getNomsColonnes(Connection c,String nomTable){
  ArrayList a=new ArrayList();
  try{
   Statement s=c.createStatement();
   ResultSet r=query(s,"SHOW COLUMNS FROM "+nomTable);
   while (r.next()) a.add(r.getString(1));
   r.close();
   s.close();
   }
  catch(SQLException e){
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }
  return (String[])a.toArray(new String[0]);
  }

 public String[] getTypesColonnes(Connection c,String nomTable){
  ArrayList a=new ArrayList();
  try{
   Statement s=c.createStatement();
   ResultSet r=query(s,"SHOW COLUMNS FROM "+nomTable);
   while (r.next()) a.add(r.getString(2));
   r.close();
   s.close();
   }
  catch(SQLException e){
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }
  return (String[])a.toArray(new String[0]);
  }

 }