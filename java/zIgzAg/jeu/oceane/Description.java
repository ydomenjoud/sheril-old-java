// v2.00 13/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */

public class Description{

 private String nom;
 private String nomPluriel;
 private String description;

 public String getNom(){return nom;}
 public String getNomPluriel(){return nomPluriel;}
 public String getDescription(){return description;}

 Description(String n,String nP,String des){
  nom=n;
  nomPluriel=nP;
  description=des;
  }

 }



