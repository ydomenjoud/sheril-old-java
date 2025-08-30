// v2.00 13/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */

import java.io.Serializable;

public class ConstructionPlanetaire implements Serializable{

 static final long serialVersionUID=-769569858268517477L;

 private String code;
 private int dommages;
 private int experience;

 private transient boolean detruit;
 private transient Batiment batiment;

 private void determinerBatiment() {if(batiment==null) batiment=(Batiment)Univers.getTechnologie(code);}

 public int getNiveauExperience(){return Vaisseau.retournerNiveauExperience(experience);}

 public float getPrix(){
  return ((Batiment)Univers.getTechnologie(code)).getPrix();
  }

 public float getEntretien(){
  return getPrix()/5;
  }

 public void reparation(int nb){
  if(dommages==0) return;
  if(estBouclier()) dommages=0;
   else dommages=Math.max(0,dommages-nb);
  }

 public void ajouterDommages(int nb){
  determinerBatiment();
  dommages=dommages+nb;
  if(dommages>batiment.getPointsDeStructure()) detruit=true;
  }

 public void augmenterExperience(int mod){
  experience=experience+mod*(Univers.getInt(Const.BASE_NIVEAU_EXPERIENCE)/Const.NOMBRE_SALVE_BATTERIE);
  }

 public boolean estDefensePlanetaire(){
  determinerBatiment();
  return batiment.estDefensePlanetaire();
  }

 public boolean estBouclier(){
  determinerBatiment();
  return batiment.possedeCaracteristiqueSpeciale(Const.BATIMENT_CAPACITE_BOUCLIER);
  }

 public void tir(Vaisseau v,Gouverneur g,Heros h,boolean boutPortant){
  determinerBatiment();
  if((!v.estDetruit())&&(batiment.estDefensePlanetaire())){
   Arme arme=batiment.getArme();
   if(!Univers.getTest(arme.getFiabilite())){
    int portee;
    if(boutPortant) portee=0; else portee=arme.getPortee()-1;
    int chance=arme.getChanceDeToucher(v.getTaille(),portee);
    int dommBouc=arme.getDommagesBouclier();
    int dommCoque=arme.getDommagesCoque();
    tirArme(chance,v,g,h,dommBouc,dommCoque);
    }
   }
  }

 private void tirArme(int chanceDeToucher,Vaisseau v,Gouverneur g,Heros h,int dommageBouc,int dommageCoque){
  if(chanceDeToucher!=0)
   if(reussiteTir(chanceDeToucher,v,g,h)){
    v.diminuerMoral();
    int b=v.getNumeroBouclierValide();
    if(b!=-1) v.ajouterDommagesBouclier(b,dommageBouc);
     else{
      if(!v.ajouterDommagesAuHasard(dommageCoque)) v.notifierDestruction();
      augmenterExperience(1+g.getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_SAVOIR));
      v.augmenterExperience(1+h.getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_SAVOIR));
      if(h!=null) h.augmenterExperience();
      if(g!=null) g.augmenterExperience();
      }
    }
  }

 private boolean reussiteTir(int chanceDepart,Vaisseau cible,Gouverneur g,Heros h){
  int test=chanceDepart;
  test=test+getNiveauExperience()+g.getAttaqueModifie()+
   g.getNiveauCompetence(Const.COMPETENCE_LEADER_INSPIRATION_FANATIQUE);
  test=test-cible.getNiveauExperience()-h.getDefenseModifie();
  if(h.estDeRace(cible.getRaceEquipage()))
   test=test-1-h.getNiveauCompetence(Const.COMPETENCE_LEADER_INSPIRATION_FANATIQUE);
  return Univers.getTest(Math.max(1,test));
  }

 public String getCode(){return code;}
 public int getDommages(){return dommages;}
 public int getPointsDeStructureRestants(){return Math.max(0,batiment.getPointsDeStructure()-dommages);}

 public boolean estBatimentDeType(String c){return code.equals(c);}
 public boolean estDetruit(){return detruit;}

 private ConstructionPlanetaire(){}

 public ConstructionPlanetaire(String type){
  code=type;
  }



 }








