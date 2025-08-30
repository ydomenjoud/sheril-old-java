
import zIgzAg.sql.*;
import java.sql.Connection;
import zIgzAg.jeu.oceane.*;

public class Essai{

//Il est nécessaire de créer une base "base_oceane" avant de lancer ce programme ;o)

 public static void main(String[] arg){

  Univers univers=new Univers(true,"sql session");
  SessionUnivers su=new SessionUnivers(univers);



  zIgzAg.sql.SessionMysql s=new zIgzAg.sql.SessionMysql();

  Connection c=s.getConnection("localhost","base_oceane");
  OutputSQLWriter o=new OutputSQLWriter(c);
  o.ajouterObjet(su);



  }


 }