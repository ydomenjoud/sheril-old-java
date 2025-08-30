// v2.01 03/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.01, 03/02/01
 */

public class Batiment extends Produit{

 private int structure;
 private int pointsDeConstructionNecessaires;
 private String codeArme;

 //les méthodes d'accès

 public int getPointsDeConstruction(){return pointsDeConstructionNecessaires;}
 public int getPointsDeStructure(){return structure;}

 public boolean estDefensePlanetaire(){return (codeArme!=null);}
 public Arme getArme(){
  if(!estDefensePlanetaire()) return null;
   else return (Arme)Univers.getTechnologie(codeArme+getRepresentationNiveau());
  }

 //les méthodes statiques

 //Le constructeur

 private Batiment(){}

 public Batiment(String code,int niv,String[] parent,int recherche,int[][] caracS,int minerai,
                 float pri,int[][] mar,int struct,int ptConstru,String cA){

  super(code,niv,parent,recherche,caracS,minerai,pri,mar);
  structure=struct;
  pointsDeConstructionNecessaires=ptConstru;
  codeArme=cA;
  }

 //les autres méthodes





 }
