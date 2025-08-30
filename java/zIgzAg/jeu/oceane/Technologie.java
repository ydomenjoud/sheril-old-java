// v2.01 03/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.01, 03/02/01
 */

import zIgzAg.utile.Mdt;
import java.util.Locale;
import java.util.ArrayList;
import java.io.Serializable;

public class Technologie implements Serializable{

 static final long serialVersionUID = 1220715082795147854L;

 private transient String codeDeBase;
 private transient int niveau;
 private transient String[] parents;
 private transient int pointsDeRecherche;
 private int[][] caracteristiquesSpeciales;

 //les méthodes d'accès

 public String getCode(){return (codeDeBase+getRepresentationNiveau());}
 public String getCorpsCode(){return codeDeBase;}
 public int getNiveau(){return niveau;}
 public String getRepresentationNiveau(){return Utile.ROMAINS[niveau];}

 public String getNom(Locale loc){return Univers.getNomTechno(getCorpsCode(),loc);}
 public String getNomPluriel(Locale loc){return Univers.getNomPlurielTechno(getCorpsCode(),loc);}
 public String getDescription(Locale loc){return Univers.getDescriptionTechno(getCorpsCode(),loc);}
 public String getNomComplet(Locale loc){
  if((niveau==0)&&(!Univers.existenceTechnologie(codeDeBase+Utile.ROMAINS[1])))
   return getNom(loc);
  else return getNom(loc)+Univers.getMessage("DE_TYPE",loc)+getRepresentationNiveau();
  }
 public String getNomPlurielComplet(Locale loc){
  if((niveau==0)&&(!Univers.existenceTechnologie(codeDeBase+Utile.ROMAINS[1])))
   return getNomPluriel(loc);
   else return getNomPluriel(loc)+Univers.getMessage("DE_TYPE",loc)+getRepresentationNiveau();
  }

 public String getParent(int num){if(num<getNombreParents()) return parents[num]; else return null;}
 public int getNombreParents(){if (parents==null) return 0; else return parents.length;}
 public String[] getParents(){return parents;}
 public int getPointsDeRecherche(){return pointsDeRecherche*Const.MODIFICATEUR_DIFFICULTE_RECHERCHE;}
 public String getDescriptionParents(Locale l){
  if(parents==null) return Utile.maj(Univers.getMessage("TECHNOLOGIE_AUCUN_PARENT",l));
  String retour="";
  for(int i=0;i<parents.length;i++){
   retour=retour+Utile.maj(Univers.getTechnologie(parents[i]).getNomComplet(l));
   if(i!=parents.length-1) retour=retour+"<BR>";
   }
  return retour;
  }

 public int[][] getCaracteristiquesSpeciales(){ return caracteristiquesSpeciales;}
 public boolean possedeCaracteristiqueSpeciale(int c){
  if(getValeurCaracteristiqueSpeciale(c)!=0) return true; else return false;
  }
 public int getValeurCaracteristiqueSpeciale(int c){
  if(caracteristiquesSpeciales==null) return 0;
  return Mdt.valeurCorrespondante(caracteristiquesSpeciales,c);
  }

 public String getDescriptionCaracteristiquesSpeciales(Locale l){
  String retour="";
  if(caracteristiquesSpeciales==null) return null;
  for(int i=0;i<caracteristiquesSpeciales.length;i++){
   if(estBatiment()){
    retour=retour+Utile.maj(Univers.getMessage("CARAC_SPECIALES_BATIMENTS",caracteristiquesSpeciales[i][0],l));
    if((caracteristiquesSpeciales[i][0]==Const.BATIMENT_PORTEE_RADAR)||
       (caracteristiquesSpeciales[i][0]==Const.BATIMENT_PORTEE_RADAR))
     retour=retour+" : "+Integer.toString(caracteristiquesSpeciales[i][1]);
    }
   if(estComposantDeVaisseau()){
    retour=retour+Utile.maj(Univers.getMessage("CARAC_SPECIALES_COMPOSANTS",caracteristiquesSpeciales[i][0],l));
    if((caracteristiquesSpeciales[i][0]!=Const.COMPOSANT_CAPACITE_PROPULSION_INTRAGALACTIQUE)&&
       (caracteristiquesSpeciales[i][0]!=Const.COMPOSANT_CAPACITE_PROPULSION_INTERGALACTIQUE)&&
       (caracteristiquesSpeciales[i][0]!=Const.COMPOSANT_CAPACITE_COLONISATEUR))
     retour=retour+" : "+Integer.toString(caracteristiquesSpeciales[i][1]);
    }
   if(i!=caracteristiquesSpeciales.length-1) retour=retour+"<BR>";
   }
 return retour;
 }

 public boolean estBatiment(){return this instanceof Batiment;}
 public boolean estComposantDeVaisseau(){return this instanceof ComposantDeVaisseau;}
 public boolean estTechnologieSimple(){return (!estBatiment())&&(!estComposantDeVaisseau());}

 //les méthodes statiques

 public static Technologie[] transformationCode(String[] entree){
  Technologie[] retour=new Technologie[entree.length];
  for(int i=0;i<entree.length;i++)
   retour[i]=Univers.getTechnologie(entree[i]);
  return retour;
  }

 public static String[] listeDesTechnologiesAtteignables(String[] technologiesConnues){
  ArrayList liste=new ArrayList();
  Technologie[] t=Univers.getListeTechnologies();
  boolean possible;
  for(int i=0;i<t.length;i++)
   if(!Mdt.estPresent(technologiesConnues,t[i].getCode()))
    if(t[i].parents==null) liste.add(t[i].getCode());
     else{
      possible=true;
      for(int j=0;j<t[i].parents.length;j++)
       if(!Mdt.estPresent(technologiesConnues,t[i].parents[j])) {possible=false;break;}
      if(possible) liste.add(t[i].getCode());
      }
  return (String[])liste.toArray(new String[0]);
  }

 public static void testDevenirTechnologiesPubliques(){
  Technologie[] t=Univers.getListeTechnologies();
  Commandant[] c=Univers.getListeCommandantsHumains();
  for(int i=0;i<t.length;i++)
   if(!Univers.estTechnologiePublique(t[i].getCode())){
	int nb=0;
	for(int j=0;j<c.length;j++)
	 if(c[j].estTechnologieConnue(t[i].getCode())) nb++;
	if(nb>(c.length*75)/100){
	  Univers.ajouterEvenement("PUBLIC_TECHNOLOGIE_0000",t[i]);
	  Univers.ajouterTechnologieAuDomainePublic(t[i].getCode());
	  for(int j=0;j<c.length;j++)
	   if(c[j].estTechnologieConnue(t[i].getCode()))
	    c[j].eliminerTechnologieConnue(t[i].getCode());
      }
    }
  }

 //Le constructeur
 protected Technologie(){}

 public Technologie(String code,int niv,String[] parent,int recherche,int[][] caracS){
  codeDeBase=code;
  niveau=niv;
  parents=parent;
  pointsDeRecherche=recherche;
  caracteristiquesSpeciales=caracS;
  }

 //les autres méthodes







 }
