// v 1.00 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.sql;

import java.io.Serializable;

public class CollectionSQL implements Serializable{

 private int table;
 private String type;

 public CollectionSQL(Object o,int table){
  this.type=o.getClass().getName();
  this.table=table;
  }
 }