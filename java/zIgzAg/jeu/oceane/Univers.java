// v2.01 24/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.01, 24/02/01
 */

import zIgzAg.utile.Fiche;
import zIgzAg.utile.Mdt;
import zIgzAg.collection.IntMap;
import zIgzAg.collection.IntHashMap;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Random;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import java.io.*;

 public class Univers{

  private static final String SESSION_MYSQL="zo";
  private static final Locale LANGUE=Locale.FRENCH;

  private static ListeDescription LISTE_DESCRIPTION;
  private static Messages MESSAGES;
  private static MessagesInfo MESSAGES_INFO;

  private static Random HASARD;

  private static TreeMap LISTE_TECHNOLOGIES;
  private static TreeMap LISTE_CARAC_ARMES;
  private static TreeMap LISTE_CHANCES_TOUCHE;

  private static TreeMap SYSTEMES;
  private static TreeMap DEBRIS;
  private static TreeMap COMMANDANTS;
  private static TreeMap PLANS_DE_VAISSEAUX;
  private static TreeMap ALLIANCES;

  private static HashMap STATS;
  private static HashMap RELATIONS_RACES;
  private static HashMap TRANSFERTS;

  private static ArrayList LEADERS_EN_VENTE;
  private static ArrayList TECHNOLOGIES_PUBLIQUES;

  private static int NUMERO_DU_TOUR;
  private static Integer PHASE;
  private static Commentaire ORDRES_NON_CONFORMES;
  private static Commentaire EVENEMENTS_PUBLICS;
  private static Commentaire ARTICLES;

  private static IntMap RAPPORTS_COMBAT;

  private boolean complet;
  private String descriptionSession;
//  private int numeroUnivers;

  private static transient HashMap CAPACITES_SPECIALES_BATIMENTS;
  private static transient int[][] MARCHANDISES_UNIVERS;

  //Pour obtenir la langue système.

  public static Locale getLocale(){return LANGUE;}


  //Pour obtenir un message système.

  public static String getMessageSysteme(String code){
   return (String)((ListResourceBundle)ResourceBundle.getBundle("zIgzAg.jeu.oceane.MessagesSystemes",LANGUE)).handleGetObject(code);
   }

  public static boolean existenceMessageSysteme(String code){
   return ((ListResourceBundle)ResourceBundle.getBundle("zIgzAg.jeu.oceane.MessagesSystemes",Locale.FRENCH)).handleGetObject(code)!=null;
   }



  //Les méthodes statiques pour obtenir un message de base du programme
  //(respectivement à partir d'une String ou d'un tableau de String).

  public static String getMessage(String code,Locale loc){
   return (String)((ListResourceBundle)ResourceBundle.getBundle("zIgzAg.jeu.oceane.Messages",loc)).handleGetObject(code);
   }

  public static String getMessage(String code,int index,Locale loc){
   return getTableauMessage(code,loc)[index];
   }

  public static String[] getTableauMessage(String code,Locale loc){
   return (String[])((ListResourceBundle)ResourceBundle.getBundle("zIgzAg.jeu.oceane.Messages",loc)).handleGetObject(code);
   }

  //Les méthodes statiques pour obtenir un élément de document HTML.
  //(respectivement à partir d'une String ou d'un tableau de String).

  public static Object getMessageRapport(String code,Locale loc){
   return ((ListResourceBundle)ResourceBundle.getBundle("zIgzAg.jeu.oceane.MessagesRapport",loc)).handleGetObject(code);
   }

  public static String getMessageRapport(String code,int index,Locale loc){
   return ((String[])getMessageRapport(code,loc))[index];
   }

  //La méthode statique pour obtenir un message d'information du programme.

  public static String getMessageInfo(String code,Locale loc){
   return (String)((ListResourceBundle)ResourceBundle.getBundle("zIgzAg.jeu.oceane.MessagesInfo",loc)).handleGetObject(code);
   }

  public static boolean existenceMessageInfo(String code){
   return ((ListResourceBundle)ResourceBundle.getBundle("zIgzAg.jeu.oceane.MessagesInfo",Locale.FRENCH)).handleGetObject(code)!=null;
   }

  //Les méthodes statiques pour obtenir les descriptions d'un type de technologie.

  public static Object getTechnoDescription(String code,Locale loc){
   return ((ListResourceBundle)ResourceBundle.getBundle("zIgzAg.jeu.oceane.ListeDescription",loc)).handleGetObject(code);
   }

  public static String getNomTechno(String code,Locale loc){
   Object o=getTechnoDescription(code,loc);
   if(o!=null) return ((Description)o).getNom();
    else return getNomTechno(getTechnologie(code).getCorpsCode(),loc);
   }

  public static String getNomPlurielTechno(String code,Locale loc){
   Object o=getTechnoDescription(code,loc);
   if(o!=null) return ((Description)o).getNomPluriel();
    else return getNomPlurielTechno(getTechnologie(code).getCorpsCode(),loc);
   }

  public static String getDescriptionTechno(String code,Locale loc){
   Object o=getTechnoDescription(code,loc);
   if(o!=null) return ((Description)o).getDescription();
    else return getNomPlurielTechno(getTechnologie(code).getCorpsCode(),loc);
   }

  //Les méthodes statiques pour obtenir les caractéristiques d'un type d'arme.

  public static int[] getCaracArme(String code){
   return (int[])LISTE_CARAC_ARMES.get(code);
   }

  ////Les méthodes statiques pour obtenir les chances de toucher d'un type d'arme.

  public static int[] getChanceToucheArme(String code){
   return (int[])LISTE_CHANCES_TOUCHE.get(code);
   }

  //méthode statique pour savoir si un code est pris ou non

  public static boolean existenceCode(String code){
   if(Univers.existenceTechnologie(code)) return true;
   if(Univers.existencePlanDeVaisseau(code)) return true;
   return false;
   }



  //Les méthodes statiques pour accéder aux technologies.

  public static Technologie getTechnologie(String code){
   return (Technologie)LISTE_TECHNOLOGIES.get(code);
   }

  public static boolean existenceTechnologie(String code){
   return LISTE_TECHNOLOGIES.containsKey(code);
   }

  public static boolean existenceTechnologieBatiment(String code){
   Technologie t=getTechnologie(code);
   if((t!=null)&&(t.estBatiment())) return true;
    else return false;
   }

  public static String[] getListeCodeTechnologie(){
   return (String[])LISTE_TECHNOLOGIES.keySet().toArray(new String[0]);
   }

  public static Map.Entry[] getTechnologies(){
   return (Map.Entry[])LISTE_TECHNOLOGIES.entrySet().toArray(new Map.Entry[0]);
   }

  public static Technologie[] getListeTechnologies(){
   return (Technologie[])LISTE_TECHNOLOGIES.values().toArray(new Technologie[0]);
   }

  public static Technologie[] getListeTechnologiesDeTypeBatiment(){
   return trierTechnologies(
   (Technologie[])LISTE_TECHNOLOGIES.values().toArray(new Technologie[0]),Const.TECHNOLOGIE_TYPE_BATIMENT);
   }

  public static Technologie[] getListeTechnologiesDeTypeComposantDeVaisseau(){
   return trierTechnologies(
    (Technologie[])LISTE_TECHNOLOGIES.values().toArray(new Technologie[0]),Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU);
   }

  public static Technologie[] trierTechnologies(Technologie[] t,int typeTechno){
   ArrayList l=new ArrayList(t.length);
   for(int i=0;i<t.length;i++)
    if(typeTechno==Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU) {if(t[i].estComposantDeVaisseau()) l.add(t[i]);}
     else if(typeTechno==Const.TECHNOLOGIE_TYPE_BATIMENT) {if(t[i].estBatiment()) l.add(t[i]);}
      else if(t[i].estTechnologieSimple()) l.add(t[i]);
   return (Technologie[])l.toArray(new Technologie[0]);
   }

  public static Technologie[] getTechnologiesPossedantCapaciteSpeciale(int carac,int typeTechno){
   Technologie[] t=null;
   if(typeTechno==Const.TECHNOLOGIE_TYPE_BATIMENT){
     Object o=CAPACITES_SPECIALES_BATIMENTS.get(new Integer(carac));
     if(o==null) t=getListeTechnologiesDeTypeBatiment();
      else return (Technologie[])o;
     }
   if(t==null) return null;

   ArrayList liste=new ArrayList(t.length);
   for(int i=0;i<t.length;i++)
    if(t[i].possedeCaracteristiqueSpeciale(carac)) liste.add(t[i]);
   t=(Technologie[])liste.toArray(new Technologie[0]);

   if(typeTechno==Const.TECHNOLOGIE_TYPE_BATIMENT) CAPACITES_SPECIALES_BATIMENTS.put(new Integer(carac),t);
   return t;
   }

  //Les méthodes statiques pour accéder aux technologies publiques.

  public static void ajouterTechnologieAuDomainePublic(String code){
   TECHNOLOGIES_PUBLIQUES.add(code);
   }

  public static boolean estTechnologiePublique(String code){
   return TECHNOLOGIES_PUBLIQUES.contains(code);
   }

  public static String[] getListeCodeTechnologiePubliques(){
   return (String[])TECHNOLOGIES_PUBLIQUES.toArray(new String[0]);
   }


  //Les méthodes statiques pour obtenir un nombre, un booléen, un tableau
  //  ou le résultat d'un test(en pourcentage) au hasard.

  public static int getInt(int borne){
   return HASARD.nextInt(borne);
   }

  public static int[] getTabInt(int borne,int ajout,int dimension){
   int[] retour=new int[dimension];
   for(int i=0;i<dimension;i++) retour[i]=getInt(borne)+ajout;
   return retour;
   }

  public static boolean getBoolean(){
   return HASARD.nextBoolean();
   }

  public static int getInd(){
   if(getBoolean()) return 1; else return -1;
   }

  public static boolean getTest(int chance){
   if(getInt(100)<chance) return true;
    else return false;
   }

  //les méthodes d'accès au numéro du tour.

  public static void setTour(int t){NUMERO_DU_TOUR=t;}
  public static int getTour(){return NUMERO_DU_TOUR;}
  public static void augmenterNumeroTour(){
   NUMERO_DU_TOUR++;
   Chemin.initialiserChemins(NUMERO_DU_TOUR);
   }
  public static void chargerNumeroTour(){
   String s=Fiche.lecture(Chemin.NUMERO_DU_TOUR,1);
   if(s==null) NUMERO_DU_TOUR=0;
    else NUMERO_DU_TOUR=Integer.parseInt(s);
   Chemin.initialiserChemins(NUMERO_DU_TOUR);
   }
  public static void sauvegarderNumeroTour(){
   Fiche.initialisation(Chemin.NUMERO_DU_TOUR);
   Fiche.ecriture(Chemin.NUMERO_DU_TOUR,Integer.toString(NUMERO_DU_TOUR));
   }

  //les méthodes d'accès aux leaders en vente.

  public static void ajouterLeaderEnVente(Leader l){
   l.mettreEnReserve();
   LEADERS_EN_VENTE.add(l);
   }

  public static Leader[] listeLeadersEnVente(){
   return (Leader[])LEADERS_EN_VENTE.toArray(new Leader[0]);
   }

  public static void retirerLeaderEnVente(Leader l){
   LEADERS_EN_VENTE.remove(l);
   }

  //les méthodes d'accès aux alliances.

  public static Map clonerListeAlliance(){
   return (Map)ALLIANCES.clone();
   }

  public static void ajouterAlliance(Alliance entree){
   ALLIANCES.put(new Integer(entree.getNumero()),entree);
   }

  public static void eliminerAlliance(int numero){
   ALLIANCES.remove(new Integer(numero));
   }

  public static int getNombreAlliances(){
   return ALLIANCES.size();
   }

  public static Alliance[] getListeAlliances(){
   return  (Alliance[])ALLIANCES.values().toArray(new Alliance[0]);
   }

  public static Alliance[] getListeAlliancesNonSecretes(){
   Alliance[] a=getListeAlliances();
   ArrayList b=new ArrayList(a.length);
   for(int i=0;i<a.length;i++)
    if(!a[i].estSecrete()) b.add(a[i]);
   return (Alliance[])b.toArray(new Alliance[0]);
   }

  public static Integer[] getNumerosAlliances(){
   return  (Integer[])ALLIANCES.keySet().toArray(new Integer[0]);
   }

  public static Alliance getAlliance(int numero){
   return (Alliance)ALLIANCES.get(new Integer(numero));
   }

  public static boolean allianceExistante(int numero){
   return ALLIANCES.containsKey(new Integer(numero));
   }

  public static int trouverNumeroLibreAlliance(){
   Integer[] l=getNumerosAlliances();
   for(int i=0;i<l.length;i++)
    if(l[i].intValue()!=i) return i;
   return l.length;
   }

//méthode en sursis -->
  public static int getNumeroAlliance(String nom){
   Alliance[] a=getListeAlliances();
   for(int i=0;i<a.length;i++)
    if(a[i].getNom().equals(nom)) return a[i].getNumero();
   return -1;
   }

//méthode en sursis -->
  public static boolean allianceExistante(String nom){
   if(getNumeroAlliance(nom)==-1) return false;
    else return true;
   }


  //les méthodes d'accès aux débris.

  public static void setDebris(Debris entree){
   DEBRIS.put(entree.getPosition(),entree);
   }

  public static void ajouterDebris(Debris entree){
   Debris d=getDebris(entree.getPosition());
   if(d==null) setDebris(entree);
    else setDebris(d.fusion(entree));
   }

  public static Debris getDebris(Position pos){
   Object o=DEBRIS.get(pos);
   if(o==null) return null;
    else return (Debris)DEBRIS.get(pos);
   }

  public static Position[] listePositionDebris(){
   return (Position[])DEBRIS.keySet().toArray(new Position[0]);
   }

  //les méthodes d'accès aux systèmes de l'univers.

  public static void setSysteme(Systeme entree){
   SYSTEMES.put(entree.getPosition(),entree);
   }

  public static Systeme getSysteme(Position pos){
   return (Systeme)SYSTEMES.get(pos);
   }

  public static Systeme[] listeSystemes(Position[] pos){
   Systeme[] retour=new Systeme[pos.length];
   for(int i=0;i<pos.length;i++)
    retour[i]=getSysteme(pos[i]);
   return retour;
   }

  public static boolean existenceSysteme(Position pos){
   return SYSTEMES.containsKey(pos);
   }

  public static Position[] listePositionsSystemes(){
   return (Position[])SYSTEMES.keySet().toArray(new Position[0]);
   }

  public static Position[] listePositionsSystemesParGalaxie(int galaxie){
   Position[] p=listePositionsSystemes();
   ArrayList a=new ArrayList(1500);
   for(int i=0;i<p.length;i++)
    if(p[i].getNumeroGalaxie()==galaxie)
     a.add(p[i]);
   return (Position[])a.toArray(new Position[0]);
   }

  public static Position[] listePositionsSystemesParSecteur(int galaxie,int secteur){
   Position[] p=listePositionsSystemes();
   ArrayList a=new ArrayList(100);
   for(int i=0;i<p.length;i++)
    if((p[i].getNumeroGalaxie()==galaxie)&&(p[i].getNumeroSecteur()==secteur))
     a.add(p[i]);
   return (Position[])a.toArray(new Position[0]);
   }

  public static Position[] listePositionsSystemesParRaceDeDepart(int race){
   Position[] p=listePositionsSystemes();
   ArrayList a=new ArrayList(500);
   for(int i=0;i<p.length;i++)
    if(Utile.getRaceDeDepart(p[i])==race)
     a.add(p[i]);
   return (Position[])a.toArray(new Position[0]);
   }


  public static Position[] listePositionsSystemesDisponibles(int race){
   Position[] p=null;
   if(race==-1) p=listePositionsSystemes();
    else p=listePositionsSystemesParRaceDeDepart(race);

   ArrayList a=new ArrayList(500);
   for(int i=0;i<p.length;i++){
    Systeme s=getSysteme(p[i]);
    if((s.nbProprios()==1)&&(s.estProprio(0))&&(s.getPopulationMaximale(0)>1000))
     a.add(p[i]);
    }

   //System.out.println(race+"-"+p.length+"-"+a.size());
   return (Position[])a.toArray(new Position[0]);
   }

  //liste des flottes en jeu : cle --> [numero joueur][numéro flotte] valeur --> [position flotte]

  public static HashMap listeFlottes(){
   HashMap retour=new HashMap();
   Commandant[] c=getListeCommandants();
   for(int i=0;i<c.length;i++){
    Map.Entry[] m=c[i].listeFlottesEtNumeros();
    for(int j=0;j<m.length;j++){
     int[] inter=new int[2];
     inter[0]=c[i].getNumero();
     inter[1]=((Integer)m[j].getKey()).intValue();
     retour.put(inter,m[j].getValue());
     }
    }
   return retour;
   }

   //liste des flottes en jeu par directive :
   // cle --> position   valeur --> map h
   //                   h : cle --> directive(Integer)  valeur --> int[n][2] : [numero joueur][numéro flotte]

  public static Map listeFlottesDirectives(){
   HashMap retour=new HashMap();
   Commandant[] c=getListeCommandants();
   for(int i=0;i<c.length;i++){
    Map.Entry[] m=c[i].listeFlottesEtNumeros();
    for(int j=0;j<m.length;j++){
     Flotte f=(Flotte)m[j].getValue();
     int[] inter=new int[2];
     inter[0]=c[i].getNumero();
     inter[1]=((Integer)m[j].getKey()).intValue();
     int[][] inter2=new int[1][2];
     inter2[0]=inter;
     Integer cle=new Integer(f.getDirectiveComplete());
     Object o=retour.get(f.getPosition());

     if(o==null){
      HashMap h=new HashMap(3);
      h.put(cle,inter2);
      retour.put(f.getPosition(),h);
      }
      else{
       Map h=(Map)o;
       o=h.get(cle);
       if(o==null) h.put(cle,inter2);
        else h.put(cle,Mdt.ajoutCoupleIndex((int[][])o,inter));
       }
     }
    }
   return retour;
   }

  public static Map listeVaisseauxParType(){
   Commandant[] c=getListeCommandants();
   HashMap retour=new HashMap();
   for(int i=0;i<c.length;i++){
    Map m=c[i].listeVaisseauxTriee();
    Map.Entry[] e=(Map.Entry[])m.entrySet().toArray(new Map.Entry[0]);
    for(int j=0;j<e.length;j++){
     Object o=retour.get(e[j].getKey());
     if(o==null) retour.put(e[j].getKey(),e[j].getValue());
      else retour.put(e[j].getKey(),new Integer(((Integer)e[j].getValue()).intValue()+((Integer)o).intValue()));
     }
    }
   return retour;
   }

 //Les méthodes d'accès aux joueurs & commandants.

  public static void setJoueur(Joueur entree){
   COMMANDANTS.put(new Integer(entree.getNumero()),entree);
   }

  public static Joueur getJoueur(int numero){
   return (Joueur)COMMANDANTS.get(new Integer(numero));
   }

  public static Map clonerListeCommandants(){
   return (Map)COMMANDANTS.clone();
   }

  public static int getNombreCommandants(){return COMMANDANTS.size();}

  public static void setCommandant(Commandant entree){
   COMMANDANTS.put(new Integer(entree.getNumero()),entree);
   }

  public static Commandant getCommandant(int numero){
   return (Commandant)COMMANDANTS.get(new Integer(numero));
   }

  public static void supprimerCommandant(int numero){
   COMMANDANTS.remove(new Integer(numero));
   }

  public static boolean existenceCommandant(int numero){
   return COMMANDANTS.containsKey(new Integer(numero));
   }

  public static Commandant[] getListeCommandants(){
   return (Commandant[])COMMANDANTS.values().toArray(new Commandant[0]);
   }

  public static Commandant[] getListeCommandantsHumains(){
   Commandant[] d=getListeCommandants();
   ArrayList a=new ArrayList(d.length);
   for(int i=0;i<d.length;i++)
    if(d[i].estJoueurHumain()) a.add(d[i]);
   return (Commandant[])a.toArray(new Commandant[0]);
   }

  public static boolean existenceLogin(String login){
   Commandant[] c=getListeCommandantsHumains();
   for(int i=0;i<c.length;i++)
    if(c[i].getLogin().equals(login)) return true;
   return false;
   }

  public static int[] getNumerosCommandants(){
   Integer[] inter=(Integer[])COMMANDANTS.keySet().toArray(new Integer[0]);
   int[] retour=new int[inter.length];
   for(int i=0;i<inter.length;i++) retour[i]=inter[i].intValue();
   return retour;
   }

  //Les méthodes d'accès aux plans de vaisseaux.

  public static void ajouterPlanDeVaisseau(PlanDeVaisseau entree){
   PLANS_DE_VAISSEAUX.put(entree.getNom(),entree);
   }

  public static void supprimerPlanDeVaisseau(String nom){
   PLANS_DE_VAISSEAUX.remove(nom);
   }

  public static PlanDeVaisseau getPlanDeVaisseau(String nom){
   return (PlanDeVaisseau)PLANS_DE_VAISSEAUX.get(nom);
   }

  public static boolean existencePlanDeVaisseau(String nom){
   return PLANS_DE_VAISSEAUX.containsKey(nom);
   }

  public static PlanDeVaisseau[] listePlansDeVaisseaux(){
   return (PlanDeVaisseau[])PLANS_DE_VAISSEAUX.values().toArray(new PlanDeVaisseau[0]);
   }

  public static PlanDeVaisseau[] listePlansDeVaisseauxPublics(){
   PlanDeVaisseau[] l=listePlansDeVaisseaux();
   ArrayList a=new ArrayList(l.length);
   for(int i=0;i<l.length;i++)
    if(l[i].estPublic()) a.add(l[i]);
   return (PlanDeVaisseau[])a.toArray(new PlanDeVaisseau[0]);
   }


  //Les méthodes d'accès aux composants de vaisseaux.




  //Les méthodes d'accès aux batiments.


  //Les méthodes d'accès aux stats.
   public static Map getStats(){return STATS;}

   public static Map getStatsDernierTour(){
	String chemin=Chemin.RACINE+"tour"+Integer.toString(getTour()-1)+"/donnees/"+"stats.txt";
	return chargerMap(chemin);
    }

   public static Map getStatsDuTour(int tour){
	String chemin=Chemin.RACINE+"tour"+Integer.toString(tour)+"/donnees/"+"stats.txt";
    return chargerMap(chemin);
    }



  //Les méthodes d'accès aux relations entre races.

   public static void ajouterRelationRaces(Position pos,int race1,int race2,int modif){
    if(pos!=null)
     ajouterRelationRaces(pos.getNumeroGalaxie(),pos.getNumeroSecteur(),race1,race2,modif);
    }

   public static void ajouterRelationRaces(int galaxie,int secteur,int race1,int race2,int modif){
    String cle=Integer.toString(galaxie)+"-"+Integer.toString(secteur);
    Object o=RELATIONS_RACES.get(cle);
    String cle2=null;
    if(race1>race2) cle2=Integer.toString(race2)+"-"+Integer.toString(race1);
     else cle2=Integer.toString(race1)+"-"+Integer.toString(race2);
    if(o==null){
     HashMap h=new HashMap();
     h.put(cle2,new Integer(modif));
     RELATIONS_RACES.put(cle,h);
     }
    else{
     Object o2=((HashMap)o).get(cle2);
     if(o2==null) ((HashMap)o).put(cle2,new Integer(modif));
      else ((HashMap)o).put(cle2,new Integer(((Integer)o2).intValue()+modif));
     }
    }

   public static int getRelationRaces(Position pos,int race1,int race2){
    return getRelationRaces(pos.getNumeroGalaxie(),pos.getNumeroSecteur(),race1,race2);
    }

   public static int getRelationRaces(int galaxie,int secteur,int race1,int race2){
    String cle=Integer.toString(galaxie)+"-"+Integer.toString(secteur);
    Object o=RELATIONS_RACES.get(cle);
    if(o==null) return 0;
    String cle2=null;
    if(race1>race2) cle2=Integer.toString(race2)+"-"+Integer.toString(race1);
     else cle2=Integer.toString(race1)+"-"+Integer.toString(race2);
    Object o2=((HashMap)o).get(cle2);
    if(o2==null) return 0;
    else return ((Integer)o2).intValue();
    }

  //méthodes d'accès aux différents transferts.
   private static List ajouterTransfert(Object l,String cle,String mode,String message){
    if(l==null) l=new ArrayList();
    String[] inter=new String[3];
    inter[0]=cle;
    inter[1]=mode;
    inter[2]=message+" ("+Integer.toString(Univers.getTour())+")";
    ((List)l).add(inter);
    return (List)l;
    }

   public static void ajouterTransfert(Joueur donneur,Joueur beneficiaire,String message){
    Integer cle=new Integer(donneur.hashCode());
    Integer cle2=new Integer(beneficiaire.hashCode());
    TRANSFERTS.put(cle,ajouterTransfert(TRANSFERTS.get(cle),cle2.toString(),"D",message));
    TRANSFERTS.put(cle2,ajouterTransfert(TRANSFERTS.get(cle2),cle.toString(),"R",message));
    }

   public static void supprimerTransferts(Object cle){TRANSFERTS.remove(cle);}

   public static Map getTransferts(){return TRANSFERTS;}

  //pour stocker les rapports de combats par secteur -->

 public static void ajouterRapportCombat(RapportCombat r){
  int galaxie=r.getPosition().getNumeroGalaxie();
  int secteur=r.getPosition().getNumeroSecteur();
  int cle=25*secteur*(galaxie+1);
  Collection c=(Collection)RAPPORTS_COMBAT.get(cle);
  if(c==null) {
   c=new ArrayList();
   RAPPORTS_COMBAT.put(cle,c);
   }
  c.add(r);
  }

 //pour obtenir les rapports de combat par secteur -->

 public static RapportCombat[] getRapportsCombat(int galaxie,int secteur){
  int cle=25*secteur*(galaxie+1);
  Collection c=(Collection)RAPPORTS_COMBAT.get(cle);
  if(c==null) return new RapportCombat[0];
  return (RapportCombat[])c.toArray(new RapportCombat[0]);
  }



  //méthodes d'accès aux prix moyens de marchandises de l'univers

   public static int getPrixMoyenMarchandise(int marchandise){
    return MARCHANDISES_UNIVERS[marchandise][1];
    }


  protected Univers() {}

  //Le constructeur de cet Univers.

  public Univers(boolean e1,String e2){
   complet=e1;
   descriptionSession=e2;
   PHASE=new Integer(0);
   ORDRES_NON_CONFORMES=new Commentaire();
   EVENEMENTS_PUBLICS=new Commentaire();
   ARTICLES=new Commentaire();
   LISTE_DESCRIPTION=new ListeDescription();
   MESSAGES=new Messages();
   MESSAGES_INFO=new MessagesInfo();
   HASARD=new Random();
   LISTE_CARAC_ARMES=chargerDynamiquement(LISTE_CARAC_ARMES,"zIgzAg.jeu.oceane.ListeCaracArmes");
   LISTE_CHANCES_TOUCHE=chargerDynamiquement(LISTE_CHANCES_TOUCHE,"zIgzAg.jeu.oceane.ListeChancesDeToucherArmes");
   LISTE_TECHNOLOGIES=chargerDynamiquement(LISTE_TECHNOLOGIES,"zIgzAg.jeu.oceane.ListeTechnologique");
   charger();
   CAPACITES_SPECIALES_BATIMENTS=new HashMap();
   STATS=new HashMap();
   RAPPORTS_COMBAT=new IntHashMap();
   MARCHANDISES_UNIVERS=Systeme.getMarchandises(Univers.listeSystemes(Univers.listePositionsSystemes()));
   System.out.println("Fin du chargement de l'Univers...");
   System.out.println("mem Libre/tot:"+Runtime.getRuntime().freeMemory()+"/"+Runtime.getRuntime().totalMemory());
   }

  //fonctions de "bidouilles";
   public void setDescription(String s){
    descriptionSession=s;
    }

  //fonction initialisatrice : à n'appeler qu'une fois, lors de la création de l'univers.

  public static void initialisation(){
   //Univers univers=new Univers(true,Const.MESSAGE_U_00000);
   //for(int i=0;i<20;i++) univers.ajouterPlanDeVaisseau(new PlanDeVaisseau());
   //Commandant c=new Commandant();
   //c.initialiserFlottes();
   //c.initialiserDomaine();
   //univers.setCommandant(c);
   //univers.sauvegarder();
   }

  //Les accès fichiers.

  public static Map chargerMap(String fichier){
   File fiche=new File(fichier);
   if(!fiche.exists()) return new HashMap();
   Map retour=null;
   try{ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fiche));
       try{retour=(Map)ois.readObject();
          }
       catch(IOException e){/*e.printStackTrace();*/System.out.println(e.getMessage());System.exit(0);}
       finally {ois.close();}
       }
   catch(IOException e){e.printStackTrace();}
   catch(ClassNotFoundException e){}

   return retour;
   }

  public ArrayList chargerArrayList(String fichier){
   File fiche=new File(fichier);
   if(!fiche.exists()) return new ArrayList();
   ArrayList retour=null;
   try{ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fiche));
       try{retour=(ArrayList)ois.readObject();
          }
       catch(IOException e){e.printStackTrace();}
       finally {ois.close();}
       }
   catch(IOException e){e.printStackTrace();}
   catch(ClassNotFoundException e){}

   return retour;
   }

  public void sauvegarderMap(String fichier,Map champ){
   try{ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(fichier));
       oos.writeObject(champ);
       oos.close();
       }
   catch(IOException e){e.printStackTrace();}
   }

  public void sauvegarderArrayList(String fichier,ArrayList champ){
   try{ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(fichier));
       oos.writeObject(champ);
       oos.close();
       }
   catch(IOException e){e.printStackTrace();}
   }

  public void charger(){
   ecrireDansCarnetDeBord("Ouverture session  : "+descriptionSession);
   chargerNumeroTour();
   if(descriptionSession.equals(SESSION_MYSQL)) chargerMysql();
    else
     if(complet){
      COMMANDANTS=(TreeMap)chargerMap(Chemin.COMMANDANTS);
      DEBRIS=(TreeMap)chargerMap(Chemin.DEBRIS);
      SYSTEMES=(TreeMap)chargerMap(Chemin.SYSTEMES);
      PLANS_DE_VAISSEAUX=(TreeMap)chargerMap(Chemin.PLANS_DE_VAISSEAUX);
      ALLIANCES=(TreeMap)chargerMap(Chemin.ALLIANCES);
      RELATIONS_RACES=(HashMap)chargerMap(Chemin.RELATIONS_RACES);
      TRANSFERTS=(HashMap)chargerMap(Chemin.TRANSFERTS);
      LEADERS_EN_VENTE=chargerArrayList(Chemin.LEADERS_EN_VENTE);
      TECHNOLOGIES_PUBLIQUES=chargerArrayList(Chemin.TECHNOLOGIES_PUBLIQUES);
      }
      else{
       COMMANDANTS=(TreeMap)chargerMap(Chemin.COMMANDANTS);
       DEBRIS=null;
       SYSTEMES=null;
       PLANS_DE_VAISSEAUX=null;
       ALLIANCES=null;
       }
   }

  public void sauvegarderMysql(){
  // if(complet) SessionUnivers.sauvegarder(this);
   }

  public void chargerMysql(){
  // if(complet) SessionUnivers.charger(this);
   }



  public void sauvegarder(){
   sauvegarderNumeroTour();
   System.out.println("mem Libre/tot:"+Runtime.getRuntime().freeMemory()+"/"+Runtime.getRuntime().totalMemory());
   if(descriptionSession.equals(SESSION_MYSQL)) sauvegarderMysql();
    else
     if(complet){
      sauvegarderMap(Chemin.COMMANDANTS,COMMANDANTS);
      sauvegarderMap(Chemin.DEBRIS,DEBRIS);
      sauvegarderMap(Chemin.SYSTEMES,SYSTEMES);
      sauvegarderMap(Chemin.PLANS_DE_VAISSEAUX,PLANS_DE_VAISSEAUX);
      sauvegarderMap(Chemin.ALLIANCES,ALLIANCES);
      sauvegarderMap(Chemin.BASE_STATS,STATS);//System.out.println(STATS.size());
      sauvegarderMap(Chemin.RELATIONS_RACES,RELATIONS_RACES);
      sauvegarderMap(Chemin.TRANSFERTS,TRANSFERTS);
      sauvegarderArrayList(Chemin.LEADERS_EN_VENTE,LEADERS_EN_VENTE);
      sauvegarderArrayList(Chemin.TECHNOLOGIES_PUBLIQUES,TECHNOLOGIES_PUBLIQUES);
      }
      else sauvegarderMap(Chemin.COMMANDANTS,COMMANDANTS);

   ecrireDansCarnetDeBord("Sauvegarde session : "+descriptionSession);
   }

  private TreeMap chargerDynamiquement(TreeMap t,String nomDeClasse){
   t=new TreeMap();
   try{Field[] champs=(Class.forName(nomDeClasse)).getFields();
       for(int i=0;i<champs.length;i++)
        t.put(champs[i].getName(),champs[i].get(null));
       }
   catch(IllegalAccessException e){e.printStackTrace();}
   catch(ClassNotFoundException e){e.printStackTrace();}
   return t;
   }

  public static void ecrireDansCarnetDeBord(String laius){
   Fiche.ecriture(Chemin.CARNET_DE_BORD,Utile.getDate()+" "+laius);
   }

  //pour gérer les phases de développement de l'univers.

  public static Integer getPhase(){return PHASE;}

  public static void phaseSuivante(){PHASE=new Integer(PHASE.intValue()+1);}

 //pour gérer les ordres non conformes(erreur de programme,répétition de mêmes ordres ou triche).

  public static Commentaire getMessagesErreurs(){return ORDRES_NON_CONFORMES;}

  public static boolean ajouterErreur(String nomCommandant,String message,Object var1,Object var2){
   ORDRES_NON_CONFORMES.ajouter(message,var1,var2,nomCommandant);
   return false;
   }

  public static boolean ajouterErreur(String nomCommandant,String message){
   ORDRES_NON_CONFORMES.ajouter(message,nomCommandant);
   return false;
   }

  public static boolean ajouterErreur(String nomCommandant,String message,int var){
   ORDRES_NON_CONFORMES.ajouter(message,new Integer(var),nomCommandant);
   return false;
   }

  public static boolean ajouterErreur(String nomCommandant,String message,Object var){
   ORDRES_NON_CONFORMES.ajouter(message,var,nomCommandant);
   return false;
   }

  public static boolean ajouterErreur(String nomCommandant,String message,Object var1,int var2){
   ORDRES_NON_CONFORMES.ajouter(message,var1,new Integer(var2),nomCommandant);
   return false;
   }

  public static boolean ajouterErreur(String nomCommandant,String message,int var1,int var2){
   ORDRES_NON_CONFORMES.ajouter(message,new Integer(var1),new Integer(var2),nomCommandant);
   return false;
   }

  public static boolean ajouterErreur(String nomCommandant,String message,Object var1,int var2,int var3){
   ORDRES_NON_CONFORMES.ajouter(message,var1,new Integer(var2),new Integer(var3),nomCommandant);
   return false;
   }

 //pour gérer les évènements publics.

  public static Commentaire getMessagesEvenements(){return EVENEMENTS_PUBLICS;}

  public static boolean ajouterEvenement(String message,Object var1){
   EVENEMENTS_PUBLICS.ajouter(message,var1);
   return true;
   }

  public static boolean ajouterEvenement(String message,Object var1,Object var2,int var3){
   EVENEMENTS_PUBLICS.ajouter(message,var1,var2,new Integer(var3));
   return true;
   }

  public static boolean ajouterEvenement(String message,Object var1,Object var2,Object var3){
   EVENEMENTS_PUBLICS.ajouter(message,var1,var2,var3);
   return true;
   }

  public static boolean ajouterEvenement(String message,Object var1,Object var2){
   EVENEMENTS_PUBLICS.ajouter(message,var1,var2);
   return true;
   }

  public static boolean ajouterEvenement(String message,Object var1,Object var2,float var3){
   EVENEMENTS_PUBLICS.ajouter(message,var1,var2,new Float(var3));
   return true;
   }

  //pour gérer les articles produits par les commandants.

  public static Commentaire getMessagesArticles(){return ARTICLES;}

  public static void ajouterArticle(Commandant auteur,String article){
   ARTICLES.ajouter("<HR><BR>"+article+"<BR><BR><B><font color=\"red\">"+auteur.getNomNumero()+"</font></B><BR>\n");
   }

  }
