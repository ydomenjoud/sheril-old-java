// v 1.00 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.sql;

import java.io.Serializable;
import java.lang.reflect.Array;

public class TableSQL implements Serializable{

 private String dim;
 private String membres;
 private String type;

 public TableSQL(Object o,int[] membres){
  this.type=getTypeBaseArray(o).getName();
  this.membres=traductionDimension(membres);
  this.dim=traductionDimension(getDimensionsArray(o));
  }

  public static int getDimensionArray(Object o){
  int retour=0;
  Class c=o.getClass().getComponentType();
  while(c!=null){
   retour++;
   c=c.getComponentType();
   }
  return retour;
  }

 public static Class getTypeBaseArray(Object o){
  if(!o.getClass().isArray()) return null;
  Class c=o.getClass().getComponentType();
  while(c.getComponentType()!=null)
   c=c.getComponentType();
  return c;
  }

 public static int[] getDimensionsArray(Object o){
  int[] retour=new int[getDimensionArray(o)];
  boolean tableauDimension0=false;
  Object o2=o;
  for(int i=0;i<retour.length;i++){
   int l=Array.getLength(o2);
   if(l==0){
    tableauDimension0=true;
    break;
    }
    else{
     retour[i]=l;
     Class c=o2.getClass().getComponentType();
     o2=Array.get(o2,0);
     }
   }

  if(tableauDimension0)
   for(int i=0;i<retour.length;i++)
    retour[i]=0;

  return retour;
  }

 public static String traductionDimension(int[] c){
  StringBuffer retour=new StringBuffer();
  for(int i=0;i<c.length;i++){
   retour.append(c[i]);
   retour.append('-');
   }
  return retour.toString();
  }

 public static int[] dimensionTraduction(String s){
  char[] t=s.toCharArray();
  int dim=0;
  for(int i=0;i<t.length;i++)
   if(t[i]=='-') dim++;
  int[] retour=new int[dim];
  dim=0;
  for(int i=0;i<retour.length;i++){
   StringBuffer inter=new StringBuffer(3);
   while(t[dim]!='-') inter.append(t[dim++]);
   retour[i]=Integer.parseInt(inter.toString());
   dim++;
   }
  return retour;
  }


 }