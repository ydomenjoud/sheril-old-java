// v 1.00 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.sql;

import java.io.Serializable;

public class MapSQL implements Serializable{

 private String type;
 private int cles;
 private int valeurs;

 public MapSQL(Object o,int cles,int valeurs){
  this.type=o.getClass().getName();
  this.cles=cles;
  this.valeurs=valeurs;
  }
 }