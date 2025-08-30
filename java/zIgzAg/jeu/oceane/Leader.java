// v2.00 13/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public abstract class Leader implements Serializable{

 static final long serialVersionUID=-2352891051087487701L;

 private String nom;

 private HashMap competencesDepart;
 private int vitesseDepart;
 private int attaqueDepart;
 private int defenseDepart;
 private int moralDepart;
 private int marchandDepart;

 private HashMap competences;
 private int vitesse;
 private int attaque;
 private int defense;
 private int moral;
 private int marchand;

 private int race;
 private int experience;
 private int niveau;

 private int tourDeCreation;
 private int tourApparition;
 private boolean changementNom;

 private int nombreDeMorts;

 //méthodes d'accès.

 public void setExperience(int xp){experience=xp;}
 public void setTourApparition(int ta){tourApparition=ta;}
 public void setNombreDeMorts(int nb){nombreDeMorts=nb;}
 public void setMoral(int e){moral=e;}
 public void setDefense(int e){defense=e;}
 public void setAttaque(int e){attaque=e;}
 public void setVitesse(int e){vitesse=e;}
 public void setMarchand(int e){marchand=e;}
 public void setNom(String e){nom=e;changementNom=true;}
 public void setNiveau(int e){niveau=e;}

 public boolean estDeRace(int r){return (r==race);}

 public boolean estNomme(){return changementNom;}
 public int getMoral(){return moral;}
 public int getDefense(){return defense;}
 public int getAttaque(){return attaque;}
 public int getVitesse(){return vitesse;}
 public int getMarchand(){return marchand;}
 public String getNom(){return nom;}
 public String toString(){return nom;}
 public int getRace(){return race;}
 public int getExperience(){return experience;}

 public boolean estHeros(){return this instanceof Heros;}
 public boolean estGouverneur(){return this instanceof Gouverneur;}

 public int getMoralModifie(){
  return moral+(getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_MORAL)*50*moral)/100;
  }
 public int getDefenseModifie(){
  return defense+(getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_DEFENSE)*50*defense)/100;
  }
 public int getAttaqueModifie(){
  return attaque+(getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_ATTAQUE)*50*attaque)/100;
  }
 public int getVitesseModifie(){
  return vitesse+(getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_VITESSE)*50*vitesse)/100;
  }
 public int getMarchandModifie(){
  return marchand+(getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_MARCHANDAGE)*50*marchand)/100;
  }

 public void augmenterExperience(){
  if(Univers.getTest(50))
   experience=experience+Univers.getInt(2+getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_SAVOIR));
  }

 public Map.Entry[] getListeCompetences(){
  return (Map.Entry[])competences.entrySet().toArray(new Map.Entry[0]);
  }

 public String getDescriptionCompetences(Locale l){
  Map.Entry[] m=getListeCompetences();
  String[] t=Univers.getTableauMessage("COMPETENCES_LEADER",l);
  String retour=new String();
  for(int i=0;i<m.length;i++){
   retour=retour+Utile.maj(t[((Integer)m[i].getKey()).intValue()])+" "+
            Utile.ROMAINS[Math.max(0,((Integer)m[i].getValue()).intValue()-1)]; //modifier après béta
   if(m.length!=i+1) retour=retour+"<BR>";
   }
  return retour;
  }

 public boolean possedeCompetence(int comp){
  return getNiveauCompetence(comp)!=0;
  }

 public int getNiveauCompetence(int comp){
  Object o=competences.get(new Integer(comp));
  if(o==null) return 0;
  //à modifier après la béta.
   if(((Integer)o).intValue()==0) return 1;
    else return ((Integer)o).intValue();
  }

 public void ajouterNiveauCompetence(int comp){
  competences.put(new Integer(comp),new Integer(getNiveauCompetence(comp)+1));
  }

 public float getValeur(){
  float retour=0F;
  retour=retour+(vitesse+attaque+defense+moral+marchand)*Const.VALEUR_UNITE;
  Map.Entry[] m=getListeCompetences();
  for(int i=0;i<m.length;i++){
   int c=((Integer)m[i].getKey()).intValue();
   if((c==Const.COMPETENCE_LEADER_VOYAGE_INTRAGALACTIQUE)||(c==Const.COMPETENCE_LEADER_VOYAGE_INTERGALACTIQUE))
    retour=retour+Const.VALEUR_COMPETENCE_SPECIALE;
    else retour=retour+Const.VALEUR_COMPETENCE_NORMAL*((Integer)m[i].getValue()).intValue();
   }
  return retour;
  }

 public float getEntretien(){
  float retour=getValeur()/10F;
  retour=retour-(retour*20*getNiveauCompetence(Const.COMPETENCE_LEADER_ENTRETIEN_LEADER))/100F;
  return retour;
  }


 public int getNiveau(){
  if (experience<1000) return 1;
   else if(experience<2000) return 2;
   else if(experience<3000) return 3;
   else if(experience<4000) return 4;
   else if(experience<6000) return 5;
   else if(experience<8000) return 6;
   else if(experience<10000) return 7;
   else if(experience<13000) return 8;
   else if(experience<16000) return 9;
   else if(experience<19000) return 10;
   else if(experience<22000) return 11;
   else if(experience<26000) return 12;
   else if(experience<30000) return 13;
   else if(experience<35000) return 14;
   else if(experience<40000) return 15;
   else if(experience<50000) return 16;
   else if(experience<60000) return 17;
   else if(experience<70000) return 18;
   else if(experience<80000) return 19;
   else if(experience<90000) return 20;
   else if(experience<100000) return 21;
   else return 22;
  }

 public int[] augmenterNiveau(){
  int[] retour=new int[2];
  retour[0]=augmenterCaracteristiqueAuHasard();
  retour[1]=ajouterCompetenceAuHasard();
  return retour;
  }

 protected void initialiserLeader(){
  competences=(HashMap)competencesDepart.clone();
  vitesse=vitesseDepart;
  attaque=attaqueDepart;
  defense=defenseDepart;
  moral=moralDepart;
  marchand=marchandDepart;
  experience=0;
  niveau=1;
  }

 //méthodes abstraites.

 public abstract boolean estEnReserve();
 public abstract void mettreEnReserve();
 public abstract int nombreDeNiveauxDeCompetence(int comp);
 public abstract int competenceNouvelleAuHasard(int ra);
 public abstract String descriptionPosition(Locale l);

 //méthodes statiques.

 public static Leader creer(String sorte){
  Leader l;
  if(sorte.equals("heros"))
   l=new Heros(Utile.getNom(),new int[0][0],Univers.getInt(3),Univers.getInt(3),Univers.getInt(3),Univers.getInt(3),
               Univers.getInt(3),Univers.getInt(Const.NB_RACES),Univers.getTour());
   else
    l=new Gouverneur(Utile.getNom(),new int[0][0],Univers.getInt(3),Univers.getInt(3),Univers.getInt(3),Univers.getInt(3),
               Univers.getInt(3),Univers.getInt(Const.NB_RACES),Univers.getTour());
  l.ajouterCompetenceAuHasard();
  return l;
  }


 public static void produireEncheres(){
  Leader[] l=Univers.listeLeadersEnVente();
  int nbHeros=0;
  int nbGouverneurs=0;
  for(int i=0;i<l.length;i++)
   if(l[i].estHeros()) nbHeros++;
    else nbGouverneurs++;
  for(int i=0;i<10-nbHeros;i++)
   Univers.ajouterLeaderEnVente(Leader.creer("heros"));
  for(int i=0;i<10-nbGouverneurs;i++)
   Univers.ajouterLeaderEnVente(Leader.creer("gouverneur"));
  }


 //le constructeur.

 protected Leader(){}

 public Leader(String n,int[][] comp,int vit,int att,int def,int mor,int march,int rac,int tour){
  nom=n;
  competencesDepart=new HashMap();
  for(int i=0;i<comp.length;i++)
   competencesDepart.put(new Integer(comp[i][0]),new Integer(comp[i][1]));
  vitesseDepart=vit;
  attaqueDepart=att;
  defenseDepart=def;
  moralDepart=mor;
  marchandDepart=march;
  initialiserLeader();
  race=rac;
  tourDeCreation=tour;
  tourApparition=tour;
  }

 // autres méthodes.

 public int ajouterCompetenceAuHasard(){
  int retour=competenceNouvelleAuHasard(race);
  ajouterNiveauCompetence(retour);
  return retour;
  }

 public int augmenterCaracteristiqueAuHasard(){
  int hasard=Univers.getInt(100);
  int index=0;
  for(int i=0;i<5;i++)
   if(hasard>0){
    hasard=hasard-Const.CHANCE_AUGMENTER_CARACTERISTIQUE_LEADER[race][i];
    index=i;
    }
  if(index==0) vitesse++;
   else if(index==1) attaque++;
   else if(index==2) defense++;
   else if(index==3) moral++;
   else if(index==4) marchand++;
  return index;
  }

 public void testerProgressionNiveau(Commandant c){
  if(getNiveau()!=niveau){
   int nb=getNiveau()-niveau;
   for(int i=0;i<nb;i++){
    int[] augmentation=augmenterNiveau();
    c.ajouterEvenement("HEROS_AUGMENTATION_NIVEAU_0000",getNom(),
      Univers.getMessage("CARACTERISTIQUES_LEADER",augmentation[0],c.getLocale()),
      Univers.getMessage("COMPETENCES_LEADER",augmentation[1],c.getLocale()));
    niveau++;
    }
   }
  }

 public void mourir(Commandant c){
  int chance=1+getNiveauCompetence(Const.COMPETENCE_LEADER_IMMORTALITE)*20;
  if(Univers.getTest(chance)){
   c.ajouterEvenement("HEROS_MORT_0000",nom);
   mettreEnReserve();
   return;
   }
  c.ajouterEvenement("HEROS_MORT_0001",nom);
  Univers.ajouterEvenement("HEROS_MORT_0002",nom,c.getNomNumero());
  mettreEnReserve();
  initialiserLeader();
  Univers.ajouterLeaderEnVente(this);
  c.supprimerLieutenant(nom);
  }





}
