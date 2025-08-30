// v2.01 03/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.01, 03/02/01
 */
public class Arme extends ComposantDeVaisseau{

 private int[] caracteristiquesArmes;
 private int[] chanceToucher;
 private String typeArme;


 //les méthodes d'accès

  public int[] getCaracArme(){return caracteristiquesArmes;}
  public int[] getChanceToucher(){return chanceToucher;}

  public int getVitesse(){return caracteristiquesArmes[Const.ARME_VITESSE_DE_BASE];}
  public int getDommagesBouclier(){return caracteristiquesArmes[Const.ARME_DOMMAGES_BOUCLIER];}
  public int getDommagesCoque(){return caracteristiquesArmes[Const.ARME_DOMMAGES_COQUE];}
  public int getDommagesSol(){return caracteristiquesArmes[Const.ARME_DOMMAGES_SOL];}
  public int getPortee(){return caracteristiquesArmes[Const.ARME_PORTEE];}
  public int getFiabilite(){return caracteristiquesArmes[Const.ARME_FIABILITE];}

  public int getForceSpatiale(){
   return getDommagesCoque()+getDommagesBouclier();
   }

  public int getForcePlanetaire(){
   return getDommagesSol();
   }

  public int getChanceDeToucher(int typeCoque,int distance){
   if(distance>=getPortee()) return 0;
   return (chanceToucher[typeCoque]*(100-(90*distance)/getPortee()))/100;
   }

 //les méthodes statiques

 //Le constructeur

 protected Arme(){}

 public Arme(String code,int niv,String[] parent,int recherche,int[][] caracS,int minerai,
                 float pri,int[][] mar,int nbCases,String typeA){

  super(code,niv,parent,recherche,caracS,minerai,pri,mar,Const.CV_ARME,nbCases);
  typeArme=typeA;
  calculChancesDeToucher();
  calculCaracteristiquesArmes();
  }

 //les autres méthodes

 public boolean estCombatSpatial(){
  if((typeArme.equals(Const.CV_ARME_CS))||(typeArme.equals(Const.CV_ARME_M))) return true;
   else return false;
  }

 public boolean estCombatPlanetaire(){
  if((typeArme.equals(Const.CV_ARME_CP))||(typeArme.equals(Const.CV_ARME_M))) return true;
   else return false;
  }

 public void calculChancesDeToucher(){
  if(estCombatSpatial()){
   int[] chanceToucherDeBase=(int[])Univers.getChanceToucheArme(getCorpsCode());
   chanceToucher=(int[])chanceToucherDeBase.clone();
   for(int i=0;i<10;i++)
    chanceToucher[i]=chanceToucher[i]+getNiveau()*Math.max(chanceToucherDeBase[i]/10,1);
   }
   else chanceToucher=new int[10];
  }

 public void calculCaracteristiquesArmes(){
  caracteristiquesArmes=(int[])Univers.getCaracArme(getCorpsCode()).clone();
  caracteristiquesArmes[5]=Math.max(0,caracteristiquesArmes[5]-getNiveau());

  if((getNiveau()!=0)&&(estCombatSpatial())){
   caracteristiquesArmes[4]=caracteristiquesArmes[4]+getNiveau();
   if(getNiveau()==1) caracteristiquesArmes[0]=caracteristiquesArmes[0]+1;
    else if(getNiveau()<5) caracteristiquesArmes[0]=caracteristiquesArmes[0]+2;
     else if(getNiveau()==5) caracteristiquesArmes[0]=caracteristiquesArmes[0]+3;
      else if(getNiveau()<9) caracteristiquesArmes[0]=caracteristiquesArmes[0]+4;
       else caracteristiquesArmes[0]=caracteristiquesArmes[0]+5;
   if(getNiveau()>2)
    if(getNiveau()<7) caracteristiquesArmes[1]=caracteristiquesArmes[1]+1;
     else if(getNiveau()<9) caracteristiquesArmes[1]=caracteristiquesArmes[1]+2;
      else caracteristiquesArmes[1]=caracteristiquesArmes[1]+3;
   if(getNiveau()>3)
    if(getNiveau()<8) caracteristiquesArmes[2]=caracteristiquesArmes[2]+1;
     else if(getNiveau()==8) caracteristiquesArmes[2]=caracteristiquesArmes[2]+2;
      else caracteristiquesArmes[2]=caracteristiquesArmes[2]+3;
   }

  if((getNiveau()!=0)&&(estCombatPlanetaire()))
   caracteristiquesArmes[3]=caracteristiquesArmes[3]+getNiveau();
  }







 }
