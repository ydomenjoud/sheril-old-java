// v2.02 24/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.02, 24/02/01
 */

import zIgzAg.html.DocumentHTML;
import zIgzAg.html.BaliseHTML;
import zIgzAg.utile.Fiche;
import zIgzAg.utile.Mdt;
import zIgzAg.utile.Mds;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Locale;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.*;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import zIgzAg.sql.SessionMysql;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;

public class ProductionOrdres{

 private static final int TAILLE_BUFFER_ECRITURE=1000;

 private static final String NOM_PAGE_PRINCIPALE_ORDRES="ordres.htm";
 private static final String NOM_PAGE_MENU="listeC.htm";
 private static final String NOM_PAGE_ACCUEIL="../depart.htm";
 private static final String NOM_INDEX="index.php3";
 private static final String NOM_FRAME_MENU="menu";
 private static final String NOM_FRAME_PRINCIPALE="principal";
 private static final String FOND_MENU="../images/blkbck31.jpg";

 public static final Integer VALEUR_DEFAUT=new Integer(100000);
 public static final Position POSITION_DEFAUT=new Position();

 private static final Class[] PARAMETRE_METHODE=new Class[0];

 private String chemin;
 private static Commandant c;
 private ArrayList listeMenu;
 public int numeroOrdre;

 public void productionPagePrincipaleOrdres(){
  BaliseHTML fs=new BaliseHTML(BaliseHTML.FRAMESET,BaliseHTML.COLS,"20%,*");
  fs.ajout(BaliseHTML.FRAMESPACING,"0").ajout(BaliseHTML.FRAMEBORDER,"0").ajout(BaliseHTML.BORDER,"false");
  BaliseHTML fm=new BaliseHTML(BaliseHTML.FRAME,BaliseHTML.NAME,NOM_FRAME_MENU);
  fm.ajout(BaliseHTML.SRC,NOM_PAGE_MENU);
  BaliseHTML fp=new BaliseHTML(BaliseHTML.FRAME,BaliseHTML.NAME,NOM_FRAME_PRINCIPALE);
  fp.ajout(BaliseHTML.SRC,NOM_PAGE_ACCUEIL);
  fs.ajout(fm).ajout(fp);
  Rapport.getDocument(chemin+NOM_PAGE_PRINCIPALE_ORDRES,
    (String)Univers.getMessageRapport("TITRE_ORDRES",c.getLocale())+c.getNomNumero(),fs).ecrire();
  }

 public void productionIndex(){
  String s="<?php $base=\"jeu.oceane\"; $commandant="+Integer.toString(c.getNumero())+
           "; $langue=\""+c.getLocale().getLanguage()+"\"; "+"include \"../principal.txt\"; ?>";
  try{BufferedWriter fluxE=new BufferedWriter(new FileWriter(chemin+NOM_INDEX));
      fluxE.write(s,0,s.length());
      fluxE.close();
      }
  catch(IOException e){e.printStackTrace();}
  }

 public void productionMenu(){
  Integer[] l=(Integer[])listeMenu.toArray(new Integer[0]);
  ecrire(afficherT(Const.TABLE_ORDRES,l));
  }

 public static String afficherA_PHP(String o,Object[] k,Object[] v){
  StringBuffer s=new StringBuffer(TAILLE_BUFFER_ECRITURE);
  s.append("<?php ");
  s.append('$');
  s.append(o);
  s.append("=array(");
  for(int i=0;i<k.length;i++){
   s.append('\"');s.append(k[i].toString());s.append('\"');
   s.append(" => ");
   s.append('\"');s.append(v[i].toString());s.append('\"');
   if(i!=k.length-1) s.append(',');
   }
  s.append("); ?>");
  return s.toString();
  }

 public static void productionDonneeRaces(Locale l){
  Fiche.initialisation(Chemin.DONNEES_RACES);
  String[] m=new String[Const.NB_RACES];
  for(int i=0;i<m.length;i++)
   m[i]=Utile.maj(Univers.getMessage("RACES",i,l));
  Fiche.ecriture(Chemin.DONNEES_RACES,afficherA_PHP("race",Utile.retournerTableauEntiers(Const.NB_RACES-1),m));
  Fiche.ecriture(Chemin.DONNEES_RACES,afficherA_PHP("couleur",Utile.retournerTableauEntiers(Const.NB_RACES-1),
                   Rapport.COULEURS_RACES));
  Integer[] listeR=new Integer[Const.NB_RACES];
  for(int i=0;i<listeR.length;i++)
   listeR[i]=new Integer(Univers.listePositionsSystemesDisponibles(i).length/4);
  Fiche.ecriture(Chemin.DONNEES_RACES,afficherA_PHP("choix_race",Utile.retournerTableauEntiers(Const.NB_RACES-1),listeR));
  }


/*
 public void productionMenu(){
  BaliseHTML body=Rapport.getBody().ajout(BaliseHTML.BACKGROUND,FOND_MENU);
  BaliseHTML b=new BaliseHTML(BaliseHTML.B);
  BaliseHTML f=new BaliseHTML(BaliseHTML.FONT,BaliseHTML.SIZE,"2");
  BaliseHTML ul=new BaliseHTML(BaliseHTML.UL);
  BaliseHTML li=new BaliseHTML(BaliseHTML.LI);
  ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getALienE(Chemin.SITE).ajout(Rapport.getFont("#00FFFF",null).
    setTexteContenu(Univers.getMessage("RETOUR_SITE",c.getLocale())))));
  ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getALienE("./").ajout(BaliseHTML.TARGET,NOM_FRAME_PRINCIPALE).
   ajout(Rapport.getFont("#00FFFF",null).setTexteContenu(
    (String)Univers.getMessageRapport("ORDRE_PRINCIPAL",c.getLocale())))));
  ul.ajout((Rapport.sautP()));

  String[] chap=(String[])Univers.getMessageRapport("CHAPITRES_ORDRES",c.getLocale());
  String[] tab=Univers.getTableauMessage("ORDRES",c.getLocale());
  String[] comm=(String[])Univers.getMessageRapport("DEROULEMENT_EVENEMENTS",c.getLocale());
  int chapitre=0;
  int indexComm=0;
  for(int i=0;i<listeMenu.size();i++){
   int index=((Integer)listeMenu.get(i)).intValue();
   if(i==0)
    ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getFont("#80FF80",null).ajout(Rapport.getTextI(comm[indexComm++]))));
   if(((indexComm==1)&&(index>Const.ORDRE_EXCLURE_ALLIANCE))||((indexComm==2)&&(index>Const.ORDRE_ENROLER_LIEUTENANT)))
    ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getFont("#80FF80",null).ajout(Rapport.getTextI(comm[indexComm++]))));
   if((chapitre==0)||((index>13)&&(chapitre==1))||((index>24)&&(chapitre==2))||((index>34)&&(chapitre==3))||
      ((index>42)&&(chapitre==4)))
     ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getFont("#FF0000",null).setTexteContenu(chap[chapitre++])));
   if(index<Const.BORNE_ORDRES_VISIBLES)
    ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getALienE("./?table="+Const.NOMS_TABLES_ORDRES[index]).
      ajout(BaliseHTML.TARGET,NOM_FRAME_PRINCIPALE).setTexteContenu(tab[index])));
   if(i==listeMenu.size()-1)
    ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getFont("#80FF80",null).ajout(Rapport.getTextI(comm[3]))));
   }
  f.ajout(ul);
  b.ajout(f);
  body.ajout(b);
  Rapport.getDocument(chemin+NOM_PAGE_MENU," ",body).ecrire();
  }
*/

 public static void productionMenuComplet(Locale l){
  BaliseHTML body=Rapport.getBody().ajout(BaliseHTML.BACKGROUND,FOND_MENU);
  BaliseHTML b=new BaliseHTML(BaliseHTML.B);
  BaliseHTML f=new BaliseHTML(BaliseHTML.FONT,BaliseHTML.SIZE,"2");
  BaliseHTML ul=new BaliseHTML(BaliseHTML.UL);
  BaliseHTML li=new BaliseHTML(BaliseHTML.LI);
 // ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getALienE("../").ajout(Rapport.getFont("#00FFFF",null).
 //   setTexteContenu(Univers.getMessage("RETOUR_SITE",l)))));
 // ul.ajout((Rapport.sautP()));

  String[] chap=(String[])Univers.getMessageRapport("CHAPITRES_ORDRES",l);
  String[] comm=(String[])Univers.getMessageRapport("DEROULEMENT_EVENEMENTS",l);
  int chapitre=0;
  String[] tab=Univers.getTableauMessage("ORDRES",l);
  for(int i=0;i<Const.BORNE_ORDRES_VISIBLES;i++){
   if(i==0) ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getFont("#80FF80",null).ajout(Rapport.getTextI(comm[0]))));
   if(i==Const.ORDRE_EXCLURE_ALLIANCE+1)
    ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getFont("#80FF80",null).ajout(Rapport.getTextI(comm[1]))));
   if(i==Const.ORDRE_ENROLER_LIEUTENANT+1)
    ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getFont("#80FF80",null).ajout(Rapport.getTextI(comm[2]))));
   if((chapitre==0)||((i>13)&&(chapitre==1))||((i>24)&&(chapitre==2))||((i>34)&&(chapitre==3))||((i>42)&&(chapitre==4)))
     ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getFont("#FF0000",null).setTexteContenu(chap[chapitre++])));
   ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getALienE("./"+l.toString()+"/"+Const.NOMS_TABLES_ORDRES[i]+".txt").
      ajout(BaliseHTML.TARGET,NOM_FRAME_PRINCIPALE).setTexteContenu(tab[i])));
   if(i==Const.BORNE_ORDRES_VISIBLES-1)
    ul.ajout(((BaliseHTML)li.clone()).ajout(Rapport.getFont("#80FF80",null).ajout(Rapport.getTextI(comm[3]))));
   }
  f.ajout(ul);
  b.ajout(f);
  body.ajout(b);
  Rapport.getDocument(Chemin.ORDRES+NOM_PAGE_MENU," ",body).ecrire();
  }

 public static void ecrireSecurite(String chemin,Commandant co){
  String securite1="PerlSetVar AuthFile ./secure2/"+
                   Integer.toString(co.getNumero())+".txt\nAuthName \"Acces Restreint\"\n"+
                   "AuthType Basic\n\n<Limit GET POST>\nrequire valid-user\n</Limit>";
  try{BufferedWriter fluxE=new BufferedWriter(new FileWriter(chemin+".htaccess"));
      fluxE.write(securite1,0,securite1.length());
      fluxE.close();
      }
  catch(IOException e){e.printStackTrace();}
  }

 public static void ecrirePasseport(String chemin,Commandant co){
  String securite1=co.getLogin()+":"+co.getMotDePasse()+"\n"+"psyko:hilarium";
  try{BufferedWriter fluxE=new BufferedWriter(new FileWriter(chemin+Integer.toString(co.getNumero())+".txt"));
      fluxE.write(securite1,0,securite1.length());
      fluxE.close();
      }
  catch(IOException e){e.printStackTrace();}
  }


 public static void ecrire(String entree){
  Integer num=new Integer( (c==null ? 0 : c.getNumero()/100) );
  try{BufferedWriter fluxE=new BufferedWriter(new FileWriter(Chemin.DONNEES_ORDRES+num.toString()+".txt",true));
      fluxE.write(entree,0,entree.length());
      fluxE.close();
      }
  catch(IOException e){e.printStackTrace();}
  }



 public static String afficherTab(Object[] o){
  StringBuffer s=new StringBuffer(TAILLE_BUFFER_ECRITURE);
  s.append('\'');
  for(int i=0;i<o.length;i++){
   s.append(Mds.remplacer(o[i].toString(),"'","\\'"));
   if(o.length-1!=i) s.append(',');
   }
  s.append('\'');
  return s.toString();
  }

 public static String afficherTab2(Object[] o){
  StringBuffer s=new StringBuffer(50);
  for(int i=0;i<o.length;i++){
   s.append('\'');
   s.append(Mds.remplacer(o[i].toString(),"'","\\'"));
   s.append('\'');
   if(o.length-1!=i) s.append(',');
   }
  return s.toString();
  }

 public String afficherA(String table,Object[] k,Object[] v){
  StringBuffer s=new StringBuffer(2*TAILLE_BUFFER_ECRITURE);
  s.append("INSERT INTO ");
  s.append(table);
  s.append(" (NUMERO,CODE,PARAM) values (");
  s.append(Integer.toString(c.getNumero()));
  s.append(',');
  s.append(afficherTab(k));
  s.append(',');
  s.append(afficherTab(v));
  s.append(");\r\n");
  return s.toString();
  }

 public static String afficherSA(String table,Object[] k,Object[] v){
  StringBuffer s=new StringBuffer(2*TAILLE_BUFFER_ECRITURE);
  s.append("INSERT INTO ");
  s.append(table);
  s.append(" (CODE,PARAM) values (");
  s.append(afficherTab(k));
  s.append(',');
  s.append(afficherTab(v));
  s.append(");\r\n");
  return s.toString();
  }

 public String afficherT(String table,Object[] o){
  StringBuffer s=new StringBuffer(TAILLE_BUFFER_ECRITURE);
  s.append("INSERT INTO ");
  s.append(table);
  s.append(" (NUMERO,CODE) values (");
  s.append(Integer.toString(c.getNumero()));
  s.append(',');
  s.append(afficherTab(o));
  s.append(");\r\n");
  return s.toString();
  }

 public static String afficherST(String table,Object[] o){
  StringBuffer s=new StringBuffer(TAILLE_BUFFER_ECRITURE);
  s.append("INSERT INTO ");
  s.append(table);
  s.append(" (CODE) values (");
  s.append(afficherTab(o));
  s.append(");\r\n");
  return s.toString();
  }

 public static String afficherCommandant(String table,Object[] o){
  StringBuffer s=new StringBuffer(TAILLE_BUFFER_ECRITURE/4);
  s.append("INSERT INTO ");
  s.append(table);
  s.append(" (NUMERO,LOGIN,MOT_DE_PASSE,NOM,ADRESSE,RACE,TOUR_ARRIVEE) values (");
  s.append(afficherTab2(o));
  s.append(");\r\n");
  return s.toString();
  }

 public static String supprimerCommandant(String table,int numero){
  StringBuffer s=new StringBuffer(100);
  s.append("DELETE FROM ");
  s.append(table);
  s.append(" WHERE NUMERO='");
   s.append(Integer.toString(numero));
  s.append("';\r\n");
  return s.toString();
  }


 public static void produireOrdresGeneraux(){
  Locale l=Locale.FRENCH;
  ecrire(afficherST(Const.TABLE_LEADER_EN_VENTE,Univers.listeLeadersEnVente()));
  ecrire(afficherST(Const.TABLE_TAXATION,Utile.retournerTableauEntiers(Const.TAXATION_MAXIMALE)));

  ecrire(afficherSA(Const.TABLE_MISSIONS,
    Utile.retournerTableauEntiers(Const.NB_MISSIONS-1),Univers.getTableauMessage("MISSIONS",l)));
  ecrire(afficherSA(Const.TABLE_POLITIQUES,
    Utile.retournerTableauEntiers(Const.NB_POLITIQUES-1),Univers.getTableauMessage("POLITIQUES",l)));
  ecrire(afficherSA(Const.TABLE_BUDGETS,
    Utile.retournerTableauEntiers(Const.NB_DOMAINES_BUDGET-1),Univers.getTableauMessage("DOMAINES_BUDGET",l)));
  ecrire(afficherSA(Const.TABLE_GALAXIES,
    Utile.retournerTableauEntiers(Const.NB_GALAXIES-1),Univers.getTableauMessage("NOMS_GALAXIES",l)));
  ecrire(afficherSA(Const.TABLE_DIRECTIVES,
    Utile.transformer2(Flotte.retourneNumerosDirectivePossibles()),Flotte.retourneDirectivesPossibles(l)));
  ecrire(afficherSA(Const.TABLE_MODE_TRANSFERT,
    Utile.retournerTableauEntiers(Const.NB_MODES_TRANSFERTS-1),Univers.getTableauMessage("MODE_TRANSFERT",l)));
  ecrire(afficherSA(Const.TABLE_STRATEGIE_CIBLE,
    Utile.retournerTableauEntiers(Messages.STRATEGIE_CIBLE.length-1),Univers.getTableauMessage("STRATEGIE_CIBLE",l)));
  ecrire(afficherSA(Const.TABLE_STRATEGIE_AGRESSIVITE,
    Utile.retournerTableauEntiers(Messages.STRATEGIE_AGRESSIVITE.length-1),
    Univers.getTableauMessage("STRATEGIE_AGRESSIVITE",l)));
  ecrire(afficherSA(Const.TABLE_ALLIANCE_SECRETE,
   Utile.retournerTableauEntiers(1),Univers.getTableauMessage("ALLIANCE_SECRET",l)));
  ecrire(afficherSA(Const.TABLE_ALLIANCE_TYPE,
   Utile.retournerTableauEntiers(Const.NB_TYPE_ALLIANCES-1),Univers.getTableauMessage("TYPE_ALLIANCE",l)));
  }

 public static void produireRegistre(int[] listeC){
  SessionMysql mySQL=new SessionMysql();
  Univers.phaseSuivante();
  Commandant[] c=Univers.getListeCommandantsHumains();
  for(int i=0;i<c.length;i++){
   if(Mdt.estPresent(listeC,c[i].getNumero())) c[i].setDernierTourRendu(Univers.getTour());
   boolean elimine=false;
   if(c[i].getDernierTourRendu()<Univers.getTour()-3) elimine=true;
    else
     try{
      Connection connection=mySQL.getConnection("localhost","jeu.oceane");
      Statement s=connection.createStatement();
      String[] i1=new String[1];
      i1[0]="NUMERO";
      String[] i2=new String[1];
      i2[0]=Integer.toString(c[i].getNumero());
      ResultSet r=mySQL.selectionner(s,Const.TABLE_REGISTRE,i1,i2);
      elimine=true;
      while(r.next()){
       elimine=false;
       c[i].setNom(r.getString("NOM").replace('#','\'').replace(',',' '));
       c[i].setAdresseElectronique(r.getString("ADRESSE"));
       c[i].setMotDePasse(r.getString("MOT_DE_PASSE"));
       c[i].setModePassationOrdres((r.getString("ORDRE_LOCAL").equals("0") ? false : true));
       c[i].setEnvoiStats((r.getString("ENVOI_STATS").equals("0") ? false : true));
       c[i].setRapportSansBgNoir((r.getString("BG_RAPPORT").equals("0") ? false : true));
       c[i].setModeDetailOrdresPasses((r.getString("DETAIL_ORDRES").equals("0") ? false : true));
       c[i].setTypeReceptionRapport(r.getInt("RECEPTION_RAPPORTS"));
       }
      }
     catch(SQLException e){
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState:     " + e.getSQLState());
      System.out.println("VendorError:  " + e.getErrorCode());
      }
   if( (c[i].getNumero()==2)||(c[i].getNumero()==3)||(c[i].getNumero()==4)||(c[i].getNumero()==5)||
       (c[i].getNumero()==6)||(c[i].getNumero()==228)) elimine=false;
   if(elimine){
    System.out.println("Supression du commandant "+c[i].getNomNumero());
    Joueur.supprimerCommandant(c[i]);
    ecrire(supprimerCommandant(Const.TABLE_REGISTRE,c[i].getNumero()));
    }
   }
  Univers.phaseSuivante();
  try{
   Connection connection=mySQL.getConnection("localhost","jeu.oceane");
   Statement s=connection.createStatement();
   ResultSet r=mySQL.selectionnerTout(s,Const.TABLE_INSCRIPTION);
   Statement s2=connection.createStatement();
   ResultSet r2=mySQL.selectionnerTout(s2,Const.TABLE_INSCRIPTION_VAISSEAUX);

   while(r.next()){
    String adresse=r.getString("ADRESSE");
    try{
     InternetAddress.parse(adresse);
     //pour vérifier que l'adresse est correcte.
     String nom=r.getString("NOM").replace('#','\'').replace(',',' ');
     int race=r.getInt("RACE");
     r2.first();
     HashMap h=new HashMap();
     while(r2.next())
      if(r2.getString("ADRESSE").equals(adresse))
       h.put(r2.getString("VAISSEAU"),r2.getString("NOMBRE"));
     System.out.println("Creation du commandant "+nom);
     Commandant nouveau=Joueur.creerCommandant(nom,adresse,race,h);
     String[] o=new String[7];
     o[0]=Integer.toString(nouveau.getNumero());
     o[1]=nouveau.getLogin();
     o[2]=nouveau.getMotDePasse();
     o[3]=nouveau.getNom();
     o[4]=nouveau.getAdresseElectronique();
     o[5]=Integer.toString(nouveau.getRace());
     o[6]=Integer.toString(nouveau.getTourArrivee());
     ecrire(afficherCommandant(Const.TABLE_REGISTRE,o));
     }
     catch(AddressException e){System.out.println("Adresse "+adresse+" non valide.");}
    }
   }
   catch(SQLException e){
    System.out.println("SQLException: " + e.getMessage());
    System.out.println("SQLState:     " + e.getSQLState());
    System.out.println("VendorError:  " + e.getErrorCode());
    }
  }



 public ProductionOrdres(Commandant commandant){
  c=commandant;
  chemin=Chemin.ORDRES+Integer.toString(c.getNumero())+"/";
  listeMenu=new ArrayList(Const.NOMS_TABLES_ORDRES.length);
  numeroOrdre=0;
  }

 public void resoudreMethode(){
  try{
   Method m=getClass().getDeclaredMethod(Const.NOMS_TABLES_ORDRES[numeroOrdre],PARAMETRE_METHODE);
   if((((Boolean)m.invoke(this,new Object[0])).booleanValue())&&(numeroOrdre<Const.BORNE_ORDRES_VISIBLES))
    listeMenu.add(new Integer(numeroOrdre));
   }
  catch(Exception e){
   System.out.println("erreur méthode inconnue : "+Const.NOMS_TABLES_ORDRES[numeroOrdre]);
   e.printStackTrace();
   }
  }

 public void creation(){
  // productionPagePrincipaleOrdres();
  // productionIndex();
  // ecrireSecurite();

  for(numeroOrdre=0;numeroOrdre<Const.NOMS_TABLES_ORDRES.length;numeroOrdre++)
   resoudreMethode();

  productionMenu();
  }

 public boolean adherer_alliance(){
  if((!c.estRuine())&&(!c.nombreAlliancesTropGrand())){
   Map m=Univers.clonerListeAlliance();
   int[] inter=c.numerosAlliances();
   for(int i=0;i<inter.length;i++)
    m.remove(new Integer(inter[i]));
   ecrire(afficherA(Const.TABLE_ALLIANCE_ADHESION,m.keySet().toArray(new Integer[0]),m.values().toArray(new Alliance[0])));
   return true;
   }
  return false;
  }

 public boolean valider_adhesion_alliance(){
  if(c.estDirigeantAlliance()){
   int[] numA=c.getAllianceDirigee();
   Commandant[] co=Univers.getListeCommandantsHumains();
   TreeMap m=new TreeMap();
   for(int i=0;i<co.length;i++)
     if(!co[i].estRuine()){
      boolean ok=false;
      for(int j=0;j<numA.length;j++)
       if(!co[i].appartientAAlliance(numA[j]))
        ok=true;
      if(ok)
       m.put(new Integer(co[i].getNumero()),co[i]);
      }

   ecrire(afficherA(
    Const.TABLE_COMMANDANT_ADHESION,m.keySet().toArray(new Integer[0]),m.values().toArray(new Commandant[0])));

   TreeMap m2=new TreeMap();
   for(int i=0;i<numA.length;i++)
    m2.put(new Integer(numA[0]),Univers.getAlliance(numA[i]).getNom());

   ecrire(afficherA(
    Const.TABLE_DIRIGEANT,m2.keySet().toArray(new Integer[0]),m2.values().toArray(new String[0])));

   return true;
   }
  return false;
  }

 public boolean nommer_dirigeant(){
  int[] a=c.numerosAlliances();
  HashMap m=new HashMap(a.length);
  for(int i=0;i<a.length;i++){
   Alliance alliance=Univers.getAlliance(a[i]);
   if((alliance.estDemocratique())||((alliance.estAnarchique())&&(!alliance.estSecrete())))
    m.put(new Integer(a[i]),alliance);
   }
  if(m.size()!=0){
   TreeMap m2=new TreeMap();
   for(int i=0;i<m.size();i++){
    Integer[] k=(Integer[])m.keySet().toArray(new Integer[0]);
    Alliance alliance=Univers.getAlliance(k[i].intValue());
    m2.putAll(alliance.getListeAdherents());
    }
   ecrire(afficherA(
    Const.TABLE_ALLIANCE_APPARTENANCE_1,m.keySet().toArray(new Integer[0]),m.values().toArray(new Alliance[0])));
   ecrire(afficherA(Const.TABLE_COMMANDANT_ALLIANCE_APPARTENANCE_1,
    m2.keySet().toArray(new Integer[0]),m2.values().toArray(new Commandant[0])));
   return true;
   }
  return false;
  }

 public boolean exclure_alliance(){
  int[] a=c.numerosAlliances();
  HashMap m=new HashMap(a.length);
  for(int i=0;i<a.length;i++){
   Alliance alliance=Univers.getAlliance(a[i]);
   if((alliance.estDemocratique())||((alliance.estAutocratique())&&(alliance.estDirigeePar(c.getNumero()))))
    m.put(new Integer(a[i]),alliance);
   }
  if(m.size()!=0){
   TreeMap m2=new TreeMap();
   Integer[] k=(Integer[])m.keySet().toArray(new Integer[0]);
   for(int i=0;i<k.length;i++){
    Alliance alliance=Univers.getAlliance(k[i].intValue());
    m2.putAll(alliance.getListeAdherents());
    }
   ecrire(afficherA(Const.TABLE_ALLIANCE_APPARTENANCE_2,
     m.keySet().toArray(new Integer[0]),m.values().toArray(new Alliance[0])));
   ecrire(afficherA(Const.TABLE_COMMANDANT_ALLIANCE_APPARTENANCE_2,
     m2.keySet().toArray(new Integer[0]),m2.values().toArray(new Commandant[0])));
   return true;
   }
  return false;
  }

 public boolean quitter_alliance(){
  int[] a=c.numerosAlliances();
  if(a.length!=0){
   HashMap m=new HashMap(a.length);
   for(int i=0;i<a.length;i++)
    m.put(new Integer(a[i]),Univers.getAlliance(a[i]));
   ecrire(afficherA(Const.TABLE_ALLIANCE_APPARTENANCE_3,
    m.keySet().toArray(new Integer[0]),m.values().toArray(new Alliance[0])));
   return true;
   }
  return false;
  }

 public boolean signer_pacte(){
  Map m=Univers.clonerListeCommandants();
  int[] p=c.listePactesDeNonAgression();
  for(int i=0;i<p.length;i++) m.remove(new Integer(p[i]));
  m.remove(new Integer(0));
  m.remove(new Integer(c.getNumero()));
  ecrire(afficherA(Const.TABLE_COMMANDANT_SIGNER_PACTE,
   m.keySet().toArray(new Integer[0]),m.values().toArray(new Commandant[0])));
  return true;
  }

 public boolean rompre_pacte(){
  int[] p=c.listePactesDeNonAgression();
  if(p.length!=0){
   Integer[] k=Utile.transformer2(p);
   Commandant[] v=new Commandant[p.length];
   for(int i=0;i<p.length;i++) v[i]=Univers.getCommandant(p[i]);
   ecrire(afficherA(Const.TABLE_COMMANDANT_ROMPRE_PACTE,k,v));
   return true;
   }
  return false;
  }

 public boolean affecter_heros(){
  Heros[] h=c.listeHeros();
  if(h.length!=0){
   Map.Entry[] m=c.listeFlottesEtNumeros();
   if(m.length>0){
    Integer[] k1=new Integer[m.length];
    String[] v1=new String[m.length];
    for(int i=0;i<m.length;i++){
     k1[i]=(Integer)m[i].getKey();
     v1[i]=((Flotte)m[i].getValue()).getNomNumero(((Integer)m[i].getKey()).intValue());
     }

    ecrire(afficherT(Const.TABLE_HEROS,h));
    return true;
    }
   }
  return false;
  }

 public boolean affecter_gouverneur(){
  Gouverneur[] h=c.listeGouverneur();
  if(h.length!=0){
   Position[] p=c.listePossession();
   String[] s=Systeme.getListeDescription(Systeme.getListeSystemes(p),c.getLocale());


   ecrire(afficherT(Const.TABLE_GOUVERNEURS,h));
   return true;
   }
  return false;
  }

 public boolean liberer_lieutenant(){
  Leader[] h=c.listeHeros();
  Leader[] g=c.listeGouverneur();
  if(h.length+g.length!=0) return true;
  return false;
  }

 public boolean enroler_lieutenant(){
  if(!c.estRuine()) return true;
  return false;
  }

 public boolean changer_capitale(){
  if(!c.estRuine()){
   Position[] p=c.listePossession();
   if(p.length!=0) return true;
   }
  return false;
  }

 public boolean affecter_recherche(){
  String[] k=c.listeTechnologiesPouvantEtreCherchees();
  /*List l=Arrays.asList(k);
  String[] r=c.recherchesActuelles();
  for(int i=0;i<r.length;i++)
   if(!l.contains(r[i])) l.add(r[i]);
  k=(String[])l.toArray(new String[0]);
  */
  Technologie[] t=Technologie.transformationCode(k);
  String[] v=new String[t.length];
  for(int i=0;i<v.length;i++) v[i]=t[i].getNomComplet(c.getLocale());
  ecrire(afficherA(Const.TABLE_TECHNOLOGIES_CHERCHEES,k,v));
  return true;
  }

 public boolean abandonner_technologie(){
  String[] k=c.listeTechnologiesNonPubliquesConnues();
  if(k.length!=0){
   Technologie[] t=Technologie.transformationCode(k);
   String[] v=new String[t.length];
   for(int i=0;i<v.length;i++) v[i]=t[i].getNomComplet(c.getLocale());
   ecrire(afficherA(Const.TABLE_TECHNOLOGIES_CONNUES,k,v));
   return true;
   }
  return false;
  }

 public boolean services_speciaux(){
  Position[] p=c.listePossession();
  Position[] p2=c.getSystemesDetectes();
  Position[] k=new Position[p.length+p2.length];
  if(k.length!=0){
   for(int i=0;i<p.length;i++) k[i]=p[i];
   for(int i=p.length;i<p2.length+p.length;i++) k[i]=p2[i-p.length];
   String[] v=Systeme.getListeDescription(Systeme.getListeSystemes(k),c.getLocale());
   ecrire(afficherA(Const.TABLE_SYSTEMES_DETECTES,k,v));
   return true;
   }
  return false;
  }

 public boolean annuler_construction(){
  Position[] p=c.listePossession();
  ArrayList a=new ArrayList(p.length);
  for(int i=0;i<p.length;i++)
   if(c.getPossession(p[i]).constructionEnCours()) a.add(p[i]);
  if(a.size()!=0){
   p=(Position[])a.toArray(new Position[0]);
   String[] v=Systeme.getListeDescription(Systeme.getListeSystemes(p),c.getLocale());
   ecrire(afficherA(Const.TABLE_SYSTEMES_CONSTRUCTION_EN_COURS,p,v));
   return true;
   }
  return false;
  }

 public boolean construire(){
  Position[] k1=c.listePossession();
  if(k1.length!=0){
   String[] v1=Systeme.getListeDescription(Systeme.getListeSystemes(k1),c.getLocale());
   String[] k2=c.listeDesMisesEnChantierPossibles();
   String[] v2=c.listeNomsDesMisesEnChantierPossibles();
   ecrire(afficherA(Const.TABLE_CONSTRUCTIONS_POSSIBLES,k2,v2));
   ecrire(afficherA(Const.TABLE_SYSTEMES,k1,v1));
   return true;
   }
  return false;
  }

 public boolean programmer_construction(){
  Position[] k1=c.listePossession();
  if((k1.length!=0)&&(c.estTechnologieConnue("progcoI")))
   return true;
  return false;
  }

 public boolean deprogrammer_construction(){
  Position[] k1=c.listePossession();
  if(k1.length!=0){
   ArrayList a=new ArrayList(k1.length);
   for(int i=0;i<k1.length;i++)
    if(c.getPossession(k1[i]).possedeProgrammationConstruction()) a.add(k1[i]);
   if(a.size()!=0){
    k1=(Position[])a.toArray(new Position[0]);
    String[] v1=Systeme.getListeDescription(Systeme.getListeSystemes(k1),c.getLocale());
    ecrire(afficherA(Const.TABLE_PROGRAMMATION_CONSTRUCTION,k1,v1));
    return true;
    }
   }
  return false;
  }

 public boolean modifier_politique(){
  Position[] k1=c.listePossession();
  if((k1.length!=0)&&(!c.estRuine())) return true;
  return false;
  }

 public boolean modifier_budget(){
  Position[] k1=c.listePossession();
  if(k1.length!=0) return true;
  return false;
  }

 public boolean modifier_taxation_systeme(){
  Position[] k1=c.listePossession();
  if(k1.length!=0) return true;
  return false;
  }

 public boolean modifier_taxation_planete(){
  if((c.estTechnologieConnue("gestplaI"))&&(c.listePossession().length!=0)) return true;
  return false;
  }

 public boolean terraformer_systeme(){
  Position[] k1=c.listePossession();
  if((k1.length!=0)&&(!c.estRuine())) return true;
  return false;
  }

 public boolean terraformer_planete(){
  if((c.estTechnologieConnue("gestplaI"))&&(c.listePossession().length!=0)) return true;
  return false;
  }

 public boolean mettre_au_rebus(){
  Position[] k1=c.listePossession();
  if((k1.length!=0)&&(c.estTechnologieConnue("gestplaI"))){
   String[] v1=Systeme.getListeDescription(Systeme.getListeSystemes(k1),c.getLocale());
   Map m=c.listeEquipement();
   String[] k2=(String[])m.keySet().toArray(new String[0]);
   Technologie[] t=Technologie.transformationCode(k2);
   String[] v2=new String[t.length];
   for(int i=0;i<v2.length;i++) v2[i]=t[i].getNomComplet(c.getLocale());
   ecrire(afficherA(Const.TABLE_REBUS_SYSTMES,k1,v1));
   ecrire(afficherA(Const.TABLE_REBUS_MATERIEL,k2,v2));
   return true;
   }
  return false;
  }

 public boolean charger_cargo(){
  Map.Entry[] m=c.listeFlottesEtNumerosCargosSurPoste();
  if(m.length>0){
   Integer[] k1=new Integer[m.length];
   String[] v1=new String[m.length];
   for(int i=0;i<m.length;i++){
    k1[i]=(Integer)m[i].getKey();
    v1[i]=((Flotte)m[i].getValue()).getNomNumero(((Integer)m[i].getKey()).intValue());
    }

   ArrayList a1=new ArrayList(50);
   ArrayList a2=new ArrayList(50);
   a1.add(Univers.getMessage("MINERAI",c.getLocale()));
   a2.add(Messages.MINERAI);
   a1.addAll(Arrays.asList(Utile.tableauToString(Utile.retournerTableauEntiers(Const.NB_MARCHANDISES-1))));
   a2.addAll(Arrays.asList(Univers.getTableauMessage("MARCHANDISES",c.getLocale())));
   a1.addAll(Arrays.asList(Messages.RACES));
   a2.addAll(Arrays.asList(Univers.getTableauMessage("RACES",c.getLocale())));
   Map map=c.listeEquipement();
   a1.addAll(map.keySet());
   Technologie[] t=Technologie.transformationCode((String[])map.keySet().toArray(new String[0]));
   for(int i=0;i<t.length;i++) a2.add(t[i].getNomComplet(c.getLocale()));
   ObjetTransporte[] o=c.listeCargaisonTransporteeTriee();
   for(int i=0;i<o.length;i++)
    if(ObjetTransporte.typeDeCodeChargement(o[i].getCode())==Const.TRANSPORT_BATIMENT)
     if(!a1.contains(o[i].getCode())){
      a1.add(o[i].getCode());
      a2.add(Univers.getTechnologie(o[i].getCode()).getNomComplet(c.getLocale()));
      }
   String[] k2=(String[])a1.toArray(new String[0]);
   String[] v2=(String[])a2.toArray(new String[0]);

   int max=0;
   for(int i=0;i<m.length;i++) max=Math.max(max,((Flotte)m[i].getValue()).nbVaisseauxCargos());
   ArrayList b1=new ArrayList(max+1);
   ArrayList b2=new ArrayList(max+1);
   b1.add(VALEUR_DEFAUT.toString());
   b2.add(" ");
   for(int i=0;i<max;i++){
    b1.add(Integer.toString(i));
    b2.add(Integer.toString(i+1));
    }
   String[] k3=(String[])b1.toArray(new String[0]);
   String[] v3=(String[])b2.toArray(new String[0]);

   ArrayList c1=new ArrayList(10);
   ArrayList c2=new ArrayList(10);
   c1.add(VALEUR_DEFAUT.toString());
   c2.add(" ");
   for(int i=0;i<m.length;i++){
    int[] inter=Univers.getSysteme(((Flotte)m[i].getValue()).getPosition()).getProprios();
    for(int j=0;j<inter.length;j++){
     Integer cle=new Integer(inter[j]);
     if(!c1.contains(cle.toString())){
      c1.add(cle.toString());
      c2.add(Univers.getCommandant(cle.intValue()).toString());
      }
     }
    }
   String[] k4=(String[])c1.toArray(new String[0]);
   String[] v4=(String[])c2.toArray(new String[0]);

   ecrire(afficherA(Const.TABLE_FLOTTE_CARGO,k1,v1));
   ecrire(afficherA(Const.TABLE_CARGAISON_CHARGEMENT,k2,v2));
   ecrire(afficherA(Const.TABLE_VAISSEAUX_CARGOS,k3,v3));
   ecrire(afficherA(Const.TABLE_PROPRIOS_POSTES,k4,v4));

   return true;
   }
  return false;
  }

 public boolean decharger_cargo(){
  Map.Entry[] m=c.listeFlottesEtNumerosCargosSurPoste();
  if(m.length>0){
   ArrayList a1=new ArrayList(50);
   ArrayList a2=new ArrayList(50);
   a1.add(Univers.getMessage("MINERAI",c.getLocale()));
   a2.add(Messages.MINERAI);
   a1.addAll(Arrays.asList(Utile.tableauToString(Utile.retournerTableauEntiers(Const.NB_MARCHANDISES-1))));
   a2.addAll(Arrays.asList(Univers.getTableauMessage("MARCHANDISES",c.getLocale())));
   a1.addAll(Arrays.asList(Messages.RACES));
   a2.addAll(Arrays.asList(Univers.getTableauMessage("RACES",c.getLocale())));
   ObjetTransporte[] o=c.listeCargaisonTransporteeTriee();
   for(int i=0;i<o.length;i++)
    if(ObjetTransporte.typeDeCodeChargement(o[i].getCode())==Const.TRANSPORT_BATIMENT){
     a1.add(o[i].getCode());
     a2.add(Univers.getTechnologie(o[i].getCode()).getNomComplet(c.getLocale()));
     }
   String[] k2=(String[])a1.toArray(new String[0]);
   String[] v2=(String[])a2.toArray(new String[0]);

   ecrire(afficherA(Const.TABLE_CARGAISON_DECHARGEMENT,k2,v2));
   return true;
   }
  return false;
  }

 public boolean deplacer_flotte(){
  Map.Entry[] m=c.listeFlottesEtNumeros();
  if(m.length>0){
   Integer[] k1=new Integer[m.length];
   String[] v1=new String[m.length];
   for(int i=0;i<m.length;i++){
    k1[i]=(Integer)m[i].getKey();
    v1[i]=((Flotte)m[i].getValue()).getNomNumero(((Integer)m[i].getKey()).intValue());
    }

   ecrire(afficherA(Const.TABLE_FLOTTES,k1,v1));

   return true;
   }
  return false;
  }

 public boolean pister(){
  Map.Entry[] m=c.listeFlottesEtNumeros();
  int[][] b=c.getFlottesDetectees();
  if((m.length>0)&&(b.length>0)){

   String[] v2=new String[b.length];
   String[] k2=new String[b.length];
   for(int i=0;i<b.length;i++){
    Commandant commandant=Univers.getCommandant(b[i][0]);
    k2[i]=Integer.toString(b[i][0])+":"+Integer.toString(b[i][1]);
    v2[i]=Univers.getMessage("DESCRIPTION_FLOTTE1",c.getLocale())+Integer.toString(b[i][1]+1)+
          Univers.getMessage("DESCRIPTION_FLOTTE2",c.getLocale())+commandant.getNomNumero();
    }
   ecrire(afficherA(Const.TABLE_FLOTTES_DETECTEES,k2,v2));
   return true;
   }
  return false;
  }

 public boolean utiliser_porte_galactique(){
  Map.Entry[] m=c.listeFlottesEtNumeros();
  if(m.length>0){
   ArrayList v1=new ArrayList(m.length);
   ArrayList k1=new ArrayList(m.length);
   for(int i=0;i<m.length;i++){
    Flotte f=(Flotte)m[i].getValue();
    int numFlotte=((Integer)m[i].getKey()).intValue();
    boolean bonnePlace=false;
    for(int j=0;j<Const.PASSAGES_GALACTIQUES.length;j++)
     if(Arrays.equals(Const.PASSAGES_GALACTIQUES[j],f.getPosition().getPos())) bonnePlace=true;
    if((bonnePlace)||(f.estInterGalactique())||(c.existenceHerosSurFlotte(numFlotte) ?
       c.getHerosSurFlotte(numFlotte).possedeCompetence(Const.COMPETENCE_LEADER_VOYAGE_INTERGALACTIQUE) : false)){
     k1.add(m[i].getKey());
     v1.add(f.getNomNumero(((Integer)m[i].getKey()).intValue()));
     }
    }
   if(k1.size()!=0){
    ecrire(afficherA(Const.TABLE_FLOTTE_GALACTIQUE,k1.toArray(new Integer[0]),v1.toArray(new String[0])));
    return true;
    }
   }
  return false;
  }

  public boolean utiliser_porte_intragalactique(){
  Map.Entry[] m=c.listeFlottesEtNumeros();
  if(m.length>0){
   ArrayList v1=new ArrayList(m.length);
   ArrayList k1=new ArrayList(m.length);
   for(int i=0;i<m.length;i++){
    Flotte f=(Flotte)m[i].getValue();
    if(f.estSurPorteIntraGalactique()){
     k1.add(m[i].getKey());
     v1.add(f.getNomNumero(((Integer)m[i].getKey()).intValue()));
     }
    }
   if(k1.size()!=0){
    ecrire(afficherA(Const.TABLE_FLOTTE_INTRAGALACTIQUE,k1.toArray(new Integer[0]),v1.toArray(new String[0])));
    return true;
    }
   }
  return false;
  }

 public boolean utiliser_colonisateur(){
  Map.Entry[] m=c.listeFlottesEtNumeros();
  ArrayList a=new ArrayList(m.length);
  for(int i=0;i<m.length;i++)
   if((((Flotte)m[i].getValue()).contientColonisateur())&&(c.existencePossession(((Flotte)m[i].getValue()).getPosition())))
    a.add(m[i]);
  if(a.size()!=0){
   m=(Map.Entry[])a.toArray(new Map.Entry[0]);
   Integer[] k1=new Integer[m.length];
   String[] v1=new String[m.length];
   for(int i=0;i<m.length;i++){
    k1[i]=(Integer)m[i].getKey();
    v1[i]=((Flotte)m[i].getValue()).getNomNumero(((Integer)m[i].getKey()).intValue());
    }
   ecrire(afficherA(Const.TABLE_FLOTTE_COLONISATEUR,k1,v1));
   return true;
   }
  return false;
  }

 public boolean larguer_mines(){
  Map.Entry[] m=c.listeFlottesEtNumeros();
  ArrayList a=new ArrayList(m.length);
  for(int i=0;i<m.length;i++)
   if(((Flotte)m[i].getValue()).estLanceurDeMines()) a.add(m[i]);
  if(a.size()!=0){
   m=(Map.Entry[])a.toArray(new Map.Entry[0]);
   Integer[] k1=new Integer[m.length];
   String[] v1=new String[m.length];
   for(int i=0;i<m.length;i++){
    k1[i]=(Integer)m[i].getKey();
    v1[i]=((Flotte)m[i].getValue()).getNomNumero(((Integer)m[i].getKey()).intValue());
    }
   ecrire(afficherA(Const.TABLE_FLOTTE_MINES,k1,v1));
   return true;
   }
  return false;
  }

 public boolean construire_flotte(){
  Map.Entry[] m=c.listeFlottesEtNumeros();
  ArrayList a=new ArrayList(m.length);
  int capaciteMax=0;
  for(int i=0;i<m.length;i++)
   if(((Flotte)m[i].getValue()).aCapaciteNavireUsine()){
    a.add(m[i]);
    capaciteMax=Math.max(capaciteMax,((Flotte)m[i].getValue()).getCapaciteMaximaleConstruction());
    }
  if(a.size()!=0){
   m=(Map.Entry[])a.toArray(new Map.Entry[0]);
   Integer[] k1=new Integer[m.length];
   String[] v1=new String[m.length];
   for(int i=0;i<m.length;i++){
    k1[i]=(Integer)m[i].getKey();
    v1[i]=((Flotte)m[i].getValue()).getNomNumero(((Integer)m[i].getKey()).intValue());
    }

   String[] plans=c.listePlansConnus();
   ArrayList a2=new ArrayList(plans.length);
   for(int i=0;i<plans.length;i++)
    if(Construction.getPointsNecessaires(plans[i])<=capaciteMax)
     a2.add(plans[i]);

   ecrire(afficherA(Const.TABLE_FLOTTE_USINES,k1,v1));
   ecrire(afficherT(Const.TABLE_PLANS_CONSTRUCTIBLES,a2.toArray(new String[0])));
   return true;
   }
  return false;
  }

 public boolean fusionner_flotte(){
  Map.Entry[] m=c.listeFlottesEtNumeros();
  if(m.length>1) return true;
  return false;
  }

 public boolean diviser_flotte(){
  Map.Entry[] m=c.listeFlottesEtNumeros();
  if(m.length>0) return true;
  return false;
  }

 public boolean transferer_centaures(){
  if(!c.estRuine()) return true;
  return false;
  }

 public boolean transferer_technologie(){
  String[] k2=c.listeTechnologiesNonPubliquesConnues();
  if(k2.length>0){
   String[] v2=new String[k2.length];
   Technologie[] t=Technologie.transformationCode(k2);
   for(int i=0;i<v2.length;i++)
    v2[i]=t[i].getNomComplet(c.getLocale());
   ecrire(afficherA(Const.TABLE_TECHNOLOGIES_TRANSFERABLES,k2,v2));
   return true;
   }
  return false;
  }

 public boolean transferer_systeme(){
  Map m=Univers.clonerListeCommandants();
  m.remove(new Integer(c.getNumero()));
  ecrire(afficherA(Const.TABLE_COMMANDANTS_TRANSFERT,
   m.keySet().toArray(new Integer[0]),m.values().toArray(new Commandant[0])));

  Position[] k2=c.listePossession();
  if(k2.length>0) return true;
  return false;
  }

 public boolean transferer_planete(){
  Position[] k2=c.listePossession();
  if(k2.length>0) return true;
  return false;
  }

 public boolean transferer_flotte(){
  Map.Entry[] me=c.listeFlottesEtNumeros();
  if(me.length>0) return true;
  return false;
  }

 public boolean vendre_flotte(){
  Map.Entry[] me=c.listeFlottesEtNumeros();
  if(me.length>0) return true;
  return false;
  }

 public boolean transferer_strategie(){
  String[] l=c.listeCodesStrategies();
  if(l.length>0){
   ecrire(afficherT(Const.TABLE_STRATEGIES,l));
   return true;
   }
  return false;
  }

 public boolean donner_plan(){
  String[] l=c.listePlansPrives();
  ArrayList a=new ArrayList(l.length);
  for(int i=0;i<l.length;i++)
   if(Univers.getPlanDeVaisseau(l[i]).estConcepteur(c.getNumero()))
    a.add(l[i]);
  if(a.size()!=0){
   ecrire(afficherT(Const.TABLE_PLANS_TRANSFERABLES,a.toArray(new String[0])));
   return true;
   }
  return false;
  }

 public boolean creer_plan(){
  if((!c.estRuine())&&(c.estTechnologieConnue("creplanI"))){
   ArrayList a1=new ArrayList(5);
   ArrayList a2=new ArrayList(5);
   a1.add(Messages.DOMAINES_PLAN_DE_VAISSEAU[0]);
   a2.add(Univers.getMessage("DOMAINES_PLAN_DE_VAISSEAU",0,c.getLocale()));
   a1.add(Messages.DOMAINES_PLAN_DE_VAISSEAU[1]);
   a2.add(Univers.getMessage("DOMAINES_PLAN_DE_VAISSEAU",1,c.getLocale()));
   a1.add(Messages.RACES[c.getRace()]);
   a2.add(Univers.getMessage("RACES",c.getRace(),c.getLocale()));
   int[] inter=c.numerosAlliances();
   for(int i=0;i<inter.length;i++){
    a1.add(Integer.toString(inter[i]));
    a2.add(Univers.getAlliance(inter[i]).getNom());
    }
   ecrire(afficherA(Const.TABLE_DOMAINE_PLAN,a1.toArray(new String[0]),a2.toArray(new String[0])));
   return true;
   }
  return false;
  }

 public boolean creer_strategie(){
  if(c.estTechnologieConnue("stratcoI"))
   return true;
  return false;
  }


 public boolean creer_alliance(){
  if((!c.estRuine())&&(!c.nombreAlliancesTropGrand())&&(c.estTechnologieConnue("diploI"))) return true;
  return false;
  }

 public boolean fixer_taux_poste(){
  if(c.listePossession().length!=0) return true;
   else return false;
  }

 public boolean renommer_systeme(){
  Position[] k1=c.listePossession();
  if(k1.length!=0){
   ArrayList a=new ArrayList(k1.length);
   for(int i=0;i<k1.length;i++)
    if(Univers.getSysteme(k1[i]).nbProprios()==1) a.add(k1[i]);
   if(a.size()>0){
    k1=(Position[])a.toArray(new Position[0]);
    String[] v1=Systeme.getListeDescription(Systeme.getListeSystemes(k1),c.getLocale());
    ecrire(afficherA(Const.TABLE_RENOMMER_SYSTEME,k1,v1));
    return true;
    }
   }
  return false;
  }

 public boolean renommer_planete(){
  Position[] k1=c.listePossession();
  if(k1.length!=0) return true;
  return false;
  }

 public boolean renommer_flotte(){
  Map.Entry[] m=c.listeFlottesEtNumeros();
  if(m.length>0) return true;
  return false;
  }


 public boolean renommer_lieutenant(){
  Leader[] h=c.listeHeros();
  Leader[] g=c.listeGouverneur();
  if(h.length+g.length!=0){
   ArrayList a=new ArrayList(h.length+g.length);
   for(int i=0;i<h.length;i++)
    if(!h[i].estNomme()) a.add(h[i]);
   for(int i=0;i<g.length;i++)
    if(!g[i].estNomme()) a.add(g[i]);
   if(a.size()!=0){
    ecrire(afficherT(Const.TABLE_RENOMMER_LIEUTENANT,a.toArray(new Leader[0])));
    return true;
    }
   }
  return false;
  }

 public boolean renommer_alliance(){
  if(c.estDirigeantAlliance()) return true;
  return false;
  }

 public boolean ecrire_adresse_commandant(){
  return true;
  }

 public boolean ecrire_adresse_alliance(){
  if(c.estDirigeantAlliance()) return true;
  return false;
  }

 public boolean ecrire_article(){
  return true;
  }

 public boolean diviser_flotte_ajouter(){
  Map.Entry[] m=c.listeFlottesEtNumeros();
  if(m.length>0){
   String[] v2=(String[])c.listeVaisseauxTriee().keySet().toArray(new String[0]);
   ecrire(afficherT(Const.TABLE_VAISSEAUX,v2));
   return true;
   }
  return false;
  }

 public boolean creer_plan_ajouter(){
  if((!c.estRuine())&&(c.estTechnologieConnue("creplanI"))){
   Technologie[] t=Technologie.transformationCode(c.listeTechnologiesConnues());
   ArrayList a1=new ArrayList(t.length);
   ArrayList a2=new ArrayList(t.length);
   for(int i=0;i<t.length;i++)
    if(t[i].estComposantDeVaisseau()){
     a1.add(t[i].getCode());
     a2.add(t[i].getNomComplet(c.getLocale()));
     }
   ecrire(afficherA(Const.TABLE_COMPOSANTS,a1.toArray(new String[0]),a2.toArray(new String[0])));
   return true;
   }
  return false;
  }

 public boolean creer_strategie_ajouter(){
  if(c.estTechnologieConnue("stratcoI")){
   String[] t=c.listePlansConnus();
   ecrire(afficherT(Const.TABLE_PLANS_CIBLES,t));
   return true;
   }
  return false;
  }












 }
