// v2.02 24/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.02, 24/02/01
 */


import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.lang.reflect.Method;
import zIgzAg.utile.Fiche;
import zIgzAg.utile.Mdt;
import zIgzAg.utile.Mds;
import zIgzAg.sql.SessionMysql;

public class ReceptionOrdres{

 private static final String[] CLE={"NUMERO"};
 private static final String PLANETE_NON_PRECISE="100";
 private static final String AUTRE_NON_PRECISE="100000";

 private static Class[] PARAMETRE_METHODE;

 private HashMap ordresRendus;

 private transient int index;
 private transient int iC;
 private transient Commandant[] c;
 private transient Connection connection;
 private transient SessionMysql mySQL;
 private transient HashMap descriptionTables;
 private transient HashMap affectationRecherches;
 private transient HashMap budget;

 private transient HashMap offresLieutenants;

 public ReceptionOrdres(){
  ordresRendus=new HashMap();
  mySQL=new SessionMysql();
  connection=mySQL.getConnection("localhost","jeu.oceane");
  chargerDescriptionTables();
  c=Univers.getListeCommandants();
  PARAMETRE_METHODE=new Class[1];
  PARAMETRE_METHODE[0]=(new String[0]).getClass();
  offresLieutenants=new HashMap();
  }

/*
 public static String[] analyserFichier(String[] l){
  ArrayList a=new ArrayList(l.length);
  int c=0;
  String inter="";
  while(c<l.length){
   if((l[c].length()!=0)&&(l[c].charAt(0)!='#')){
    inter=inter+l[c];
    if(l[c].endsWith(";")){
     a.add(inter.substring(0,inter.length()-1));
     inter="";
     }
    }
   c++;
   }
  return (String[])a.toArray(new String[0]);
  }

 public static void chargementOrdres(){
  Connection c=SessionMysql.getConnection("localhost","jeu.oceane");
  SessionMysql.viderToutesTables(c);
  String[] l=analyserFichier(Fiche.lecture(Chemin.RACINE+"ok.txt"));
  for(int i=0;i<l.length;i++)
   SessionMysql.execute(c,l[i]);
  SessionMysql.fermerConnection(c);
  }
*/

 public int pla(String e){
  return (e.equals(PLANETE_NON_PRECISE) ? Integer.MIN_VALUE : tInt(Integer.parseInt(e)));
  }

 public int numT(String e){
  return (e.equals(AUTRE_NON_PRECISE) ? Integer.MIN_VALUE : tInt(Integer.parseInt(e)));
  }

 public String tStrat(String e){
  if(e.equals("par défaut")) return "{0}";
   else return e;
  }

 public int tInt(int e){
  if(e<0){
   Univers.ajouterErreur(c[iC].getNomNumero(),"ER_ORDRE_0000",Const.NOMS_TABLES_ORDRES[index],e);
   return 0;
   }
  else return e;
  }

 public int tInt(String e){
  try{return tInt(Integer.parseInt(e));
     }
  catch(NumberFormatException exception){
   Univers.ajouterErreur(c[iC].getNomNumero(),"ER_ORDRE_0001",Const.NOMS_TABLES_ORDRES[index],e);
   return 0;
   }
  }

 public String tCode(String e){
  if(e.equals(Messages.MINERAI)) return e;
  if(Univers.existenceTechnologie(e)) return e;
  if(Mdt.estPresent(Messages.RACES,e)) return Messages.RACES[Mdt.position(Messages.RACES,e)];
  return Messages.MARCHANDISES[tInt(e)];
  }


 public void chargerDescriptionTables(){
  String[] liste=mySQL.listeTables(connection);
  descriptionTables=new HashMap(liste.length);
  for(int i=0;i<liste.length;i++)
   descriptionTables.put(liste[i],mySQL.getNomsColonnes(connection,liste[i]));
  }



 public String[][] getOrdres(){
  ArrayList a=new ArrayList();
  try{
   Statement s=connection.createStatement();
   String[] param=new String[1];
   param[0]=Integer.toString(c[iC].getNumero());
   ResultSet r=mySQL.selectionner(s,Const.NOMS_TABLES_ORDRES[index],CLE,param);
   int nb=((String[])descriptionTables.get(Const.NOMS_TABLES_ORDRES[index])).length;
   while(r.next()){
    String[] inter=new String[nb-1];
    for(int i=0;i<nb-1;i++){
     if((inter[i]=r.getString(i+2).replace('#','\'').replace(',',' '))==null) inter[i]="";
     inter[i]=Mds.remplacer(Mds.remplacer(Mds.remplacer(inter[i],"\\'","'"),"\\\"","\""),"\\\\","\\").trim();
     }
    a.add(inter);
    }
   r.close();
   s.close();
   }
  catch(SQLException e){
   System.out.println("SQLException: " + e.getMessage());
   System.out.println("SQLState:     " + e.getSQLState());
   System.out.println("VendorError:  " + e.getErrorCode());
   }

  if( ((index==Const.ORDRE_ENROLER_LIEUTENANT)&&(a.size()>Const.NOMBRE_LIMITE_ENROLER_LIEUTENANT)) ||
      ((index==Const.ORDRE_SERVICES_SPECIAUX)&&(a.size()>Const.NOMBRE_LIMITE_SERVICES_SPECIAUX)) ||
      ((index==Const.ORDRE_DON_TECHNOLOGIE)&&(a.size()>Const.NOMBRE_LIMITE_DON_TECHNOLOGIE)) ||
      ((index==Const.ORDRE_CREER_PLAN)&&(a.size()>Const.NOMBRE_LIMITE_CREATION_PLAN)) ||
      ((index==Const.ORDRE_CREER_STRATEGIE)&&(a.size()>Const.NOMBRE_LIMITE_CREATION_STRATEGIE)) ){
         a=new ArrayList();
         Univers.ajouterErreur(c[iC].getNomNumero(),"ER_ORDRE_0002",Const.NOMS_TABLES_ORDRES[index]);
         }

  return (String[][])a.toArray(new String[0][0]);
  }

 public boolean existenceOrdre(String nomTable,String[] param){
  Statement s=mySQL.getStatement(connection);
  boolean retour=(0!=mySQL.nombreLignesResultSet(
                   mySQL.selectionner(s,nomTable,(String[])descriptionTables.get(nomTable),param) ) );
  mySQL.fermerStatement(s);
  return retour;
  }

 public void resoudreMethode(){
  try{
   System.out.print(Const.NOMS_TABLES_ORDRES[index]+"-");
   Univers.phaseSuivante();
   Method m=getClass().getDeclaredMethod(Const.NOMS_TABLES_ORDRES[index],PARAMETRE_METHODE);
   for(iC=0;iC<c.length;iC++){
    String[][] r=getOrdres();
    if(r.length!=0){
     if(index==Const.ORDRE_AFFECTER_RECHERCHE) affectationRecherches=new HashMap();
     if(index==Const.ORDRE_MODIFIER_BUDGET) budget=new HashMap();
     Object[] inter=new Object[1];
     for(int j=0;j<r.length;j++){
      inter[0]=r[j];
      if(c[iC].getModeDetailOrdresPasses()) c[iC].ajouterOrdres("ORDRE_"+Const.NOMS_TABLES_ORDRES[index],r[j]);
      m.invoke(this,inter);
      ordresRendus.put(new Integer(c[iC].getNumero()),"");
      }
     if(index==Const.ORDRE_AFFECTER_RECHERCHE)
           c[iC].affecterDomainesDeRecherche((String[])affectationRecherches.keySet().toArray(new String[0]),
              Utile.transformer((Integer[])affectationRecherches.values().toArray(new Integer[0])));
     if(index==Const.ORDRE_MODIFIER_BUDGET){
      String[] po=(String[])budget.keySet().toArray(new String[0]);
      for(int i=0;i<po.length;i++){
       HashMap h=(HashMap)budget.get(po[i]);
       c[iC].modifierBudget(Position.traduction(po[i]),Utile.transformer3((String[])h.keySet().toArray(new String[0])),
                             Utile.transformer3((String[])h.values().toArray(new String[0])));
       }
      }

     }
    }
   }
  catch(Exception e){e.printStackTrace();System.out.println("com:"+c[iC].getNomNumero());}
  }

 public void reglerEncheres(){
  Leader[] l=Univers.listeLeadersEnVente();
  Map.Entry[] m=(Map.Entry[])offresLieutenants.entrySet().toArray(new Map.Entry[0]);
  for(int i=0;i<m.length;i++){
   int numeroLieutenant=Integer.parseInt((String)m[i].getKey());
   String s=(String)m[i].getValue();
   int offre=Integer.parseInt(s.substring(0,s.indexOf('*')));
   int numC=Integer.parseInt(s.substring(s.indexOf('*')+1,s.length()));
   Commandant c=Univers.getCommandant(numC);
   if((float)offre>=l[numeroLieutenant].getValeur()){
    if(l[numeroLieutenant] instanceof Heros) c.ajouterHeros((Heros)l[numeroLieutenant]);
     else c.ajouterGouverneur((Gouverneur)l[numeroLieutenant]);
    c.modifierBudget(Const.BUDGET_COMMANDANT_ACHAT_LIEUTENANT,-(float)offre);
    c.ajouterEvenement("EV_COMMANDANT_ACHETER_LIEUTENANT_0000",l[numeroLieutenant].getNom(),(float)offre);
    Univers.ajouterEvenement("PUBLIC_ACHETER_COMMANDANT_0000",c.getNomNumero(),l[numeroLieutenant].getNom(),(float)offre);
    Univers.retirerLeaderEnVente(l[numeroLieutenant]);
    }
    else c.ajouterEvenement("EV_COMMANDANT_ACHETER_LIEUTENANT_0001",l[numeroLieutenant].getNom(),offre);
   }
  }


 public Map deroulementOrdres(){
  for(index=0;index<Const.BORNE_ORDRES_VISIBLES;index++){
   resoudreMethode();
   if(index==Const.ORDRE_EXCLURE_ALLIANCE) Alliance.traiterVotes();
   if(index==Const.ORDRE_ENROLER_LIEUTENANT) reglerEncheres();
   if(index==Const.ORDRE_DEPLACEMENT_FLOTTE) Flotte.deplacerVersBut();
   }
  mySQL.fermerConnection(connection);
  return ordresRendus;
  }

 public void adherer_alliance(String[] o){
  if(Univers.allianceExistante(tInt(o[0]))){
   Alliance a=Univers.getAlliance(tInt(o[0]));
   String[] param=new String[3];

   param[0]=Integer.toString(a.getNumeroDirigeant());
   param[1]=Integer.toString(c[iC].getNumero());
   param[2]=o[0];
   if(existenceOrdre(Const.NOMS_TABLES_ORDRES[Const.ORDRE_VALIDER_ADHESION_ALLIANCE],param))
     c[iC].adhererAlliance(tInt(o[0]));
     else c[iC].ajouterEvenement("EV_COMMANDANT_ALLIANCE_0003",a.getNom());
   }
  }

 public void valider_adhesion_alliance(String[] o){}

 public void nommer_dirigeant(String[] o){
  c[iC].voterElectionDirigeant(tInt(o[0]),tInt(o[1]));
  }

 public void exclure_alliance(String[] o){
  c[iC].voterExclusionCommandant(tInt(o[0]),tInt(o[1]));
  }

 public void quitter_alliance(String[] o){
  c[iC].quitterAlliance(tInt(o[0]));
  }

 public void creer_alliance(String[] o){
  c[iC].creerAlliance(tInt(o[3]),o[0],tInt(o[2]),(tInt(o[1])==0 ? true : false));
  }

 public void renommer_alliance(String[] o){
  c[iC].renommerAlliance(o[0],tInt(o[1]));
  }

 public void ecrire_adresse_alliance(String[] o){

  c[iC].ecrireAdresseAlliance(o[0],tInt(o[1]));
  }

 public void signer_pacte(String[] o){
  String[] param=new String[2];
  param[0]=o[0];
  param[1]=Integer.toString(c[iC].getNumero());
  if(existenceOrdre(Const.NOMS_TABLES_ORDRES[Const.ORDRE_SIGNER_PACTE],param)) c[iC].signerPacteDeNonAgression(tInt(o[0]));
   else c[iC].ajouterEvenement("EV_COMMANDANT_PACTE_0003",tInt(o[0]));
  }

 public void rompre_pacte(String[] o){
  c[iC].dechirerPacteDeNonAgression(tInt(o[0]));
  }

 public void affecter_heros(String[] o){
  c[iC].affecterHeros(o[0],tInt(o[1]));
  }

 public void affecter_gouverneur(String[] o){
  c[iC].affecterGouverneur(o[0],Position.traduction(o[1]));
  }

 public void liberer_lieutenant(String[] o){
  c[iC].licencierLieutenant(o[0]);
  }

 public void enroler_lieutenant(String[] o){
  if((float)tInt(o[0])<=c[iC].getCentaures())
   if(!offresLieutenants.containsKey(o[1])) offresLieutenants.put(o[1],o[0]+"*"+Integer.toString(c[iC].getNumero()));
    else{
     String r1=(String)offresLieutenants.get(o[1]);
     String s=r1.substring(0,r1.indexOf('*'));
     String numC=r1.substring(r1.indexOf('*')+1,r1.length());
     int modif1=(Univers.getCommandant(tInt(numC)).listeLieutenants().length>0 ? 1 : 2);
     int modif2=(c[iC].listeLieutenants().length>0 ? 1 : 2);

     if(tInt(s)*modif1<tInt(o[0])*modif2)
      offresLieutenants.put(o[1],o[0]+"*"+Integer.toString(c[iC].getNumero()));
     }
   else c[iC].ajouterErreur("ER_COMMANDANT_ACHETER_LIEUTENANT_0000",o[1]);
  }

 public void renommer_lieutenant(String[] o){
  c[iC].renommerLieutenant(o[0],o[1]);
  }

 public void changer_capitale(String[] o){
  c[iC].changerCapitale(Position.traduction(o[0]));
  }

 public void affecter_recherche(String[] o){
  affectationRecherches.put(o[0],new Integer(tInt(o[1])));
  }

 public void abandonner_technologie(String[] o){
  c[iC].abandonnerTechnologie(o[0]);
  }

 public void services_speciaux(String[] o){
  c[iC].effectuerMissionSpeciale(Position.traduction(o[0]),tInt(o[1]),pla(o[2]));
  }

 public void annuler_construction(String[] o){
  c[iC].annulerConstruction(Position.traduction(o[0]));
  }

 public void construire(String[] o){
  c[iC].mettreEnChantier(Position.traduction(o[0]),tInt(o[2]),o[1],pla(o[3]));
  }

 public void programmer_construction(String[] o){
  c[iC].programmerConstruction(Position.traduction(o[0]),o[1]);
  }

 public void deprogrammer_construction(String[] o){
  c[iC].annulerProgramationConstruction(Position.traduction(o[0]));
  }

 public void modifier_politique(String[] o){
  c[iC].modifierPolitique(Position.traduction(o[0]),tInt(o[1]));
  }

 public void modifier_budget(String[] o){
  Object ob=budget.get(o[0]);
  if(ob==null){
   HashMap h=new HashMap();
   h.put(o[1],o[2]);
   budget.put(o[0],h);
   }
   else ((HashMap)ob).put(o[1],o[2]);
  }

 public void modifier_taxation_systeme(String[] o){
  c[iC].modifierTaxation(Position.traduction(o[0]),tInt(o[1]));
  }

 public void modifier_taxation_planete(String[] o){
  c[iC].modifierTaxationPlanete(Position.traduction(o[0]),tInt(o[1]),tInt(o[2]));
  }

 public void terraformer_systeme(String[] o){
  c[iC].terraformer(Position.traduction(o[0]));
  }

 public void terraformer_planete(String[] o){
  c[iC].terraformerPlanete(Position.traduction(o[0]),pla(o[1]));
  }

 public void mettre_au_rebus(String[] o){
  c[iC].detruireBatiments(Position.traduction(o[0]),o[1],tInt(o[2]),pla(o[3]));
  }

 public void charger_cargo(String[] o){
  c[iC].chargerCargo(tInt(o[0]),numT(o[3]),tInt(o[2]),tCode(o[1]),numT(o[4]),pla(o[5]));
  }

 public void decharger_cargo(String[] o){
  c[iC].dechargerCargo(tInt(o[0]),numT(o[3]),tInt(o[2]),tCode(o[1]),numT(o[4]),pla(o[5]));
  }

 public void deplacer_flotte(String[] o){
  Position p=new Position(tInt(o[3]),tInt(o[1]),tInt(o[2]));
  int[] d=Flotte.nombreDonneDirective(tInt(o[4]));
  c[iC].deplacerFlotte(tInt(o[0]),p,d[0],d[1],tStrat(o[5]));
  }

 public void pister(String[] o){
  int a=tInt(o[1].substring(0,o[1].indexOf(':')));
  int b=tInt(o[1].substring(o[1].indexOf(':')+1,o[1].length()));
  c[iC].pisterFlotte(tInt(o[0]),b,a);
  }

 public void utiliser_porte_galactique(String[] o){
  c[iC].utiliserPorteGalactique(tInt(o[0]),tInt(o[1]));
  }

 public void utiliser_porte_intragalactique(String[] o){
  c[iC].utiliserPorteIntraGalactique(tInt(o[0]));
  }

 public void utiliser_colonisateur(String[] o){
  c[iC].coloniserPlanetes(tInt(o[0]),tInt(o[1]));
  }

 public void larguer_mines(String[] o){
  c[iC].lancerMines(tInt(o[0]));
  }

 public void construire_flotte(String[] o){
  c[iC].programmerConstructionFlotte(tInt(o[0]),o[1]);
  }

 public void fusionner_flotte(String[] o){
  int[] d=Flotte.nombreDonneDirective(tInt(o[2]));
  c[iC].fusionnerFlotte(tInt(o[0]),tInt(o[1]),d[0],d[1]);
  }

 public void diviser_flotte(String[] o){
  index=Const.ORDRE_DIVISER_FLOTTE_DETAIL;
  String[][] s=getOrdres();
  index=Const.ORDRE_DIVISER_FLOTTE;

  ArrayList type=new ArrayList();
  ArrayList nombre=new ArrayList();
  for(int i=0;i<s.length;i++)
   if((s[i][0].equals(o[0]))&&(s[i][1].equals(o[2]))){
    type.add(s[i][2]);
    nombre.add(s[i][3]);
    }
  if(type.size()>0)
   c[iC].diviserFlotte(tInt(o[0]),(String[])type.toArray(new String[0]),
                            Utile.transformer3(nombre.toArray(new String[0])),o[1],tInt(o[2]));

  }

 public void transferer_centaures(String[] o){
  c[iC].transfertCentaures(tInt(o[0]),tInt(o[1]),tInt(o[2]));
  }

 public void transferer_technologie(String[] o){
  c[iC].transfertTechnologie(tInt(o[0]),o[1],tInt(o[2]));
  }

 public void transferer_systeme(String[] o){
  c[iC].transfertSysteme(tInt(o[0]),Position.traduction(o[1]),tInt(o[2]));
  }

 public void transferer_planete(String[] o){
  c[iC].transfertPlanete(tInt(o[0]),Position.traduction(o[1]),tInt(o[2]),tInt(o[3]));
  }

 public void transferer_flotte(String[] o){
  c[iC].transfertFlotte(tInt(o[0]),tInt(o[1]),tInt(o[2]),tInt(o[3]));
  }

 public void transferer_strategie(String[] o){
  c[iC].donnerStrategie(o[1],tInt(o[0]),tInt(o[2]));
  }

 public void donner_plan(String[] o){
  c[iC].donnerPlanDeVaisseau(o[0],tInt(o[1]),tInt(o[2]));
  }

 public void creer_plan(String[] o){
  index=Const.ORDRE_CREER_PLAN_DETAIL;
  String[][] s=getOrdres();
  index=Const.ORDRE_CREER_PLAN;

  ArrayList type=new ArrayList();
  ArrayList nombre=new ArrayList();
  for(int i=0;i<s.length;i++){
   type.add(s[i][0]);
   nombre.add(s[i][1]);
   }
  c[iC].creerPlanDeVaisseau(o[0],o[1],o[2],tInt(o[3]),(String[])type.toArray(new String[0]),
                            Utile.transformer3(nombre.toArray(new String[0])));
  }

 public void creer_strategie(String[] o){
  index=Const.ORDRE_CREER_STRATEGIE_DETAIL;
  String[][] s=getOrdres();
  index=Const.ORDRE_CREER_STRATEGIE;

  ArrayList type=new ArrayList();
  ArrayList pos=new ArrayList();
  ArrayList cible=new ArrayList();
  for(int i=0;i<s.length;i++){
   type.add(s[i][0]);
   int[] inter1=new int[2];
   inter1[0]=tInt(s[i][1]);
   inter1[1]=tInt(s[i][2]);
   pos.add(inter1);
   int[] inter2=new int[10];
   for(int j=0;j<10;j++) inter2[j]=tInt(s[i][3+j]);
   cible.add(inter2);
   }
  c[iC].creerStrategie(o[0],tInt(o[1]),tInt(o[2]),(String[])type.toArray(new String[0]),
                           (int[][])pos.toArray(new int[0][0]),(int[][])cible.toArray(new int[0][0]));
  }

 public void renommer_systeme(String[] o){
  c[iC].renommerSysteme(Position.traduction(o[0]),o[1]);
  }

 public void renommer_planete(String[] o){
  c[iC].renommerPlanete(Position.traduction(o[0]),o[1],tInt(o[2]));
  }

 public void renommer_flotte(String[] o){
  c[iC].renommerFlotte(tInt(o[0]),o[1]);
  }

 public void ecrire_adresse_commandant(String[] o){
  c[iC].setSite(o[0]);
  }

 public void ecrire_article(String[] o){
  Univers.ajouterArticle(c[iC],o[0]);
  }

 public void vendre_flotte(String[] o){
  c[iC].venteFlotte(tInt(o[0]),tInt(o[1]));
  }

 public void fixer_taux_poste(String[] o){
  c[iC].fixerTauxPostes(tInt(o[0]));
  }





  }








