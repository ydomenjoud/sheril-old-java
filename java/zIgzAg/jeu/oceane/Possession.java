// v2.00 06/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.01, 06/02/01
 */

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Locale;
import java.io.Serializable;

public class Possession implements Serializable{

 static final long serialVersionUID=1656583489195831918L;

 private ArrayList constructions;
 private String programmationConstruction;
 private int politique;
 private HashMap budget;
 private HashMap poste;

 // Les méthodes d'accès

 public void setPolitique(int entree){politique=entree;}
 public int getPolitique(){return politique;}
 public String getDescriptionPolitique(Locale l){return Univers.getMessage("POLITIQUES",politique,l);}
 public boolean aPolitiqueAnti(){return racePolitiqueAnti()!=-1;}
 public int racePolitiqueAnti(){
  if(politique==Const.POLITIQUE_ANTI_HUMAIN) return 0;
  if(politique==Const.POLITIQUE_ANTI_ZORGLUB) return 1;
  if(politique==Const.POLITIQUE_ANTI_GOLO) return 2;
  if(politique==Const.POLITIQUE_ANTI_YOZDA) return 3;
  if(politique==Const.POLITIQUE_ANTI_JONDOISHI) return 4;
  if(politique==Const.POLITIQUE_ANTI_NOMADE) return 5;
  if(politique==Const.POLITIQUE_ANTI_DREWIN) return 6;
  if(politique==Const.POLITIQUE_ANTI_TONK) return 7;
  if(politique==Const.POLITIQUE_ANTI_GOLUB) return 8;
  if(politique==Const.POLITIQUE_ANTI_ZOOUSH) return 9;
  return -1;
  }

 public boolean possedeStockImportantPoste(int marchandise){
  return getQuantiteMarchandise(marchandise)>=100;
  }


 public String getProgrammationConstruction(){return programmationConstruction;}

 public boolean possedeProgrammationConstruction(){return programmationConstruction!=null;}

 public void programmerConstruction(String c){programmationConstruction=c;}



 public void ajouterConstruction(Construction c){constructions.add(c);}

 public Construction[] listeConstructions(){
  return (Construction[])constructions.toArray(new Construction[0]);
  }



 public int getNombreConstructions(){return constructions.size();}

 public boolean constructionEnCours(){return (getNombreConstructions()!=0);}

 public int pointsNecessairesPourConstructionDeType(String code){
  int retour=0;
  for(int i=0;i<constructions.size();i++)
   if(((Construction)constructions.get(i)).getCode().equals(code))
    retour=retour+((Construction)constructions.get(i)).getPointsNecessaires();
  return retour;
  }


 public void setBudget(int typeDeChamp,int pourcentage){budget.put(new Integer(typeDeChamp),new Integer(pourcentage));}

 public int getBudget(int typeDeChamp){
  Object o=budget.get(new Integer(typeDeChamp));
  if(o==null) return 0;
   else return((Integer)o).intValue();
  }

 public boolean conformiteBudget(){
  int total=0;
  for(int i=0;i<Const.NB_DOMAINES_BUDGET;i++) total=total+getBudget(i);
  if(total>100) return false; else return true;
  }

 public boolean modifierBudget(int[] typeDeChamp,int[] pourcentage){
  HashMap inter=(HashMap)budget.clone();
  for(int i=0;i<typeDeChamp.length;i++) setBudget(typeDeChamp[i],pourcentage[i]);
  if(conformiteBudget()) return true;
   else {
    budget=inter;
    return false;
    }
  }

 public int[] listeNumerosMarchandises(){
  Integer[] l=(Integer[])poste.keySet().toArray(new Integer[0]);
  int[] retour=new int[l.length];
  for(int i=0;i<l.length;i++) retour[i]=l[i].intValue();
  return retour;
  }

 public void setMarchandise(int marchandise,int[] carac){
  poste.put(new Integer(marchandise),carac);
  }

 public void setMarchandise(int marchandise,int prix,int stock){
  int[] inter=new int[2];
  inter[0]=prix;
  inter[1]=stock;
  setMarchandise(marchandise,inter);
  }

 public boolean contientMarchandise(int marchandise){
  return poste.containsKey(new Integer(marchandise));
  }

 public int[] getMarchandise(int marchandise){
  return (int[])poste.get(new Integer(marchandise));
  }

 public int getPrixMarchandise(int marchandise){
  if(contientMarchandise(marchandise)) return getMarchandise(marchandise)[0]; else return Const.PRIX_MARCHANDISE_PAR_DEFAUT;
  }

 public float getPrixMarchandiseFloat(int marchandise){
  return ((float)getPrixMarchandise(marchandise))/10F;
  }

 public void setPrixMarchandise(int marchandise,int prix){
  setMarchandise(marchandise,Math.min(100,Math.max(1,prix)),getQuantiteMarchandise(marchandise));
  }

 public void setPrixMarchandise(int marchandise,float prix){
  setMarchandise(marchandise,(int)(10*prix),getQuantiteMarchandise(marchandise));
  }

 public int getQuantiteMarchandise(int marchandise){
  if(contientMarchandise(marchandise)) return getMarchandise(marchandise)[1]; else return 0;
  }

 public void setQuantiteMarchandise(int marchandise,int quantite){
  setMarchandise(marchandise,getPrixMarchandise(marchandise),quantite);
  }

 public void ajouterMarchandise(int marchandise,int quantite){
  if(contientMarchandise(marchandise)) setQuantiteMarchandise(marchandise,quantite+getQuantiteMarchandise(marchandise));
   else setQuantiteMarchandise(marchandise,quantite);
  setPrixMarchandise(marchandise,getPrixMarchandise(marchandise)-quantite);
  }

 public void supprimerMarchandise(int marchandise,int quantite){
  if(contientMarchandise(marchandise))
   setQuantiteMarchandise(marchandise,Math.max(0,getQuantiteMarchandise(marchandise)-quantite));
  setPrixMarchandise(marchandise,getPrixMarchandise(marchandise)+quantite);
  }


 public void initialiserConstructions(){constructions=new ArrayList(0);}
 public void initialiserProgrammationConstruction(){programmationConstruction=null;}
 public void initialiserBudget(){budget=new HashMap(Const.NB_DOMAINES_BUDGET);}
 public void initialiserPoste(){poste=new HashMap(Const.NB_MARCHANDISES);}

 //Le constructeur

 public Possession(){
  initialiserConstructions();
  initialiserBudget();
  initialiserPoste();
  initialiserProgrammationConstruction();
  }

 // Les méthodes statiques.

 public static Possession creerAuHasard(){
  Possession retour=new Possession();
  retour.setBudget(0,Univers.getInt(25));
  retour.setBudget(1,Univers.getInt(25));
  retour.setBudget(2,Univers.getInt(25));
  retour.setPolitique(Univers.getInt(Const.NB_POLITIQUES));
  return retour;
  }

 //méthodes pour gérer les fins de tour.

 public void miseAJourReputation(Commandant c){
  if(politique==Const.POLITIQUE_INTEGRISME) c.ajouterReputation(-20);
  if(politique==Const.POLITIQUE_TOTALITAIRE) c.ajouterReputation(-80);
  if(politique==Const.POLITIQUE_ESCLAVAGISTE) c.ajouterReputation(-150);
  if(aPolitiqueAnti()) c.ajouterReputation(-300);
  }

 public void evolutionPosteCo(int numero,int taux,Systeme s){
  int bonus=0;
  if(politique==Const.POLITIQUE_COMMERCE) bonus=2;
  if(politique==Const.POLITIQUE_TOTALITAIRE) bonus=-1;
  int bonusNourriture=0;
  if(possedeStockImportantPoste(Const.PRODUIT_MATERIEL_AGRICOLE)) bonusNourriture=s.nbPlanetesHabitees(numero);
  for(int i=0;i<Const.NB_MARCHANDISES;i++){
   int prod=s.getProductionMarchandise(numero,i);
   if(prod!=0) ajouterMarchandise(i,(i==Const.PRODUIT_NOURRITURE ? (prod+bonus+bonusNourriture) : prod+bonus));
   }
  for(int i=0;i<Const.NB_MARCHANDISES;i++)
   if(contientMarchandise(i)!=0){
    int ancienPrix=getPrixMarchandise(i);
    setPrixMarchandise(i,ancienPrix - ((100-taux)/100)*
        ((ancienPrix-Univers.getPrixMoyenMarchandise(i))/(2+getQuantiteMarchandise(i)/10)));
    }
  }

 public void programmationConstructions(Commandant com,Systeme s){
  if(possedeProgrammationConstruction()){
   int aFaire=pointsNecessairesPourConstructionDeType(programmationConstruction);
   int potentiel=s.getPointsDeConstruction(com.getNumero(),com.getGouverneurSurPossession(s.getPosition()),this);
   if(potentiel>aFaire){
    int necessaire=Construction.getPointsNecessaires(programmationConstruction);
    int nb=Math.max(1,(potentiel-aFaire)/necessaire);
    Construction c=new Construction(programmationConstruction,nb,Integer.MIN_VALUE);
    ajouterConstruction(c);
    }
   }
  }

 public void resolutionConstructions(Commandant com,Systeme s){
  Construction[] c=listeConstructions();
  int potentiel=s.getPointsDeConstruction(com.getNumero(),com.getGouverneurSurPossession(s.getPosition()),this);
  int[] ptNecessaires=new int[c.length];
  for(int i=0;i<c.length;i++) ptNecessaires[i]=c[i].getPointsNecessaires();
  int[] sauvegarde=(int[])ptNecessaires.clone();
  boolean boucle=true;
  if(c.length==0) boucle=false;
  while(boucle){
   boolean constructionRestante=false;
   for(int i=0;i<c.length;i++)
    if(potentiel>0)
     if(ptNecessaires[i]>0){
      ptNecessaires[i]--;
      potentiel--;
      constructionRestante=true;
      }
   if((potentiel==0)||(!constructionRestante)) break;
   }

  for(int i=0;i<c.length;i++){
   c[i].setPointsEffectues(c[i].getPointsEffectues()+sauvegarde[i]-ptNecessaires[i]);
   if(ptNecessaires[i]==0){
    String descriptionTechno=null;
    if(Univers.existenceTechnologie(c[i].getCode()))
     descriptionTechno=Univers.getTechnologie(c[i].getCode()).getNomComplet(com.getLocale());
     else descriptionTechno=c[i].getCode();
    boolean reussite=true;
    if(com.getCentaures()<c[i].getPrix()){
     com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0000",s.getPosition(),descriptionTechno,c[i].getNombre());
     reussite=false;
     }
    if((reussite)&&(s.getStockMinerai(com.getNumero())<c[i].getMinerai())){
     com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0001",s.getPosition(),descriptionTechno,c[i].getNombre());
     reussite=false;
     }
    if(reussite){
     int[][] m=c[i].getMarchandises();
     for(int j=0;j<m.length;j++)
      if(getQuantiteMarchandise(m[j][0])<m[j][1]){
       com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0002",s.getPosition(),descriptionTechno,
           Univers.getMessage("MARCHANDISES",m[j][0],com.getLocale()),c[i].getNombre());
       reussite=false;
       break;
       }
     }

    if(reussite)
     if(!Univers.existenceTechnologie(c[i].getCode()))
      if(s.capaciteSpecialeBatiment(com.getNumero(),Const.BATIMENT_CAPACITE_PRODUCTION_VAISSEAU)==0){
       reussite=false;
       com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0007",s.getPosition(),descriptionTechno,c[i].getNombre());
       }

    if(reussite){
     s.supprimerRichesses(com.getNumero(),Messages.MINERAI,c[i].getMinerai(),Integer.MIN_VALUE);
     int[][] m=c[i].getMarchandises();
     for(int j=0;j<m.length;j++) supprimerMarchandise(m[j][0],m[j][1]);
     com.modifierBudget(Const.BUDGET_COMMANDANT_REALISATION_CONSTRUCTION,-c[i].getPrix());
     constructions.remove(c[i]);
     com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0003",s.getPosition(),descriptionTechno,c[i].getNombre());

    if(c[i].getNombre()!=0)
     if(Univers.existenceTechnologie(c[i].getCode())){
      ObjetComplexeTransporte objet=new ObjetComplexeTransporte(c[i].getCode());
      for(int j=0;j<c[i].getNombre();j++) objet.ajouterObjet(new ConstructionPlanetaire(c[i].getCode()));
      s.ajouterRichesses(com.getNumero(),objet,c[i].getPlanete());
      }
     else{
      Flotte flotte=new Flotte(s.getNom(),s.getPosition());
      int race=s.getRaceMajoritaire(com.getNumero());

      PlanDeVaisseau plan=Univers.getPlanDeVaisseau(c[i].getCode());
      if((plan.getRoyalties()!=0)&&(plan.concepteurExistant())){
       Commandant beneficiaire=Univers.getCommandant(plan.getNumeroConcepteur());
       float benefice=(plan.getRoyalties()*c[i].getPrix())/(100+plan.getRoyalties());
       beneficiaire.modifierBudget(Const.BUDGET_COMMANDANT_PERCEPTION_ROYALTIES,benefice);
       }

      for(int j=0;j<c[i].getNombre();j++)
       flotte.ajouterVaisseau(Vaisseau.creer(c[i].getCode(),race));

      Flotte[] f=com.listeFlottes();
      boolean fusion=false;
      for(int j=0;j<f.length;j++)
       if(f[j].getPosition().equals(s.getPosition())){
        f[j].fusion(flotte);
        fusion=true;
        break;
        }
      if(!fusion) com.ajouterFlotte(flotte);
      }
     }
    }
   }
  }


 //autres méthodes.

 public void transfererPossession(Possession p){
  Construction[] c=p.listeConstructions();
  for(int i=0;i<c.length;i++) ajouterConstruction(c[i]);

  int[] l=p.listeNumerosMarchandises();
  for(int i=0;i<l.length;i++)
   ajouterMarchandise(l[i],p.getQuantiteMarchandise(l[i])/2);
  }



}






