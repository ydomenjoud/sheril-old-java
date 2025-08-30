// v2.00 13/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Locale;
import java.text.MessageFormat;
import java.io.Serializable;

//un arbre de commentaires à un niveau.

public class Commentaire implements Serializable{

 private TreeMap arbre;

 public Object getDomaine(){return Univers.getPhase();}

 public Commentaire(){
  initialiserArbre();
  }

 private void initialiserArbre(){if(arbre==null) arbre=new TreeMap();}

 private ArrayList creerBranche(){
  ArrayList v=new ArrayList();
  arbre.put(getDomaine(),v);
  return v;
  }

 private ArrayList getBranche(){
  Object o=arbre.get(getDomaine());
  if(o!=null) return (ArrayList)o;
   else return creerBranche();
  }

 public int nbMessages(){
  ArrayList[] a=(ArrayList[])arbre.values().toArray(new ArrayList[0]);
  int retour=0;
  for(int i=0;i<a.length;i++) retour=retour+a[i].size();
  return retour;
  }

 public ArrayList[] listeMessages(Locale l,int type){
  ArrayList[] a=(ArrayList[])arbre.values().toArray(new ArrayList[0]);
  ArrayList[] retour=new ArrayList[a.length];
  for(int i=0;i<a.length;i++){
   retour[i]=new ArrayList(a[i].size());
   for(int j=0;j<a[i].size();j++)
    retour[i].add(((Message)a[i].get(j)).traduction(l,type));
   }
  return retour;
  }

 public void ajouter(String message,Object[] parametresDuMessage){
  initialiserArbre();
  getBranche().add(new Message(message,parametresDuMessage));
  }

 public void ajouter(String message){
  getBranche().add(new Message(message,new Object[0]));
  }

 public void ajouter(String message,Object param){
  Object[] inter=new Object[1];
  inter[0]=param;
  ajouter(message,inter);
  }

 public void ajouter(String message,Object param1,Object param2){
  Object[] inter=new Object[2];
  inter[0]=param1;
  inter[1]=param2;
  ajouter(message,inter);
  }

 public void ajouter(String message,Object param1,Object param2,Object param3){
  Object[] inter=new Object[3];
  inter[0]=param1;
  inter[1]=param2;
  inter[2]=param3;
  ajouter(message,inter);
  }

 public void ajouter(String message,Object param1,Object param2,Object param3,Object param4){
  Object[] inter=new Object[4];
  inter[0]=param1;
  inter[1]=param2;
  inter[2]=param3;
  inter[3]=param4;
  ajouter(message,inter);
  }

 public void ajouter(String message,Object param1,Object param2,Object param3,Object param4,Object param5){
  Object[] inter=new Object[5];
  inter[0]=param1;
  inter[1]=param2;
  inter[2]=param3;
  inter[3]=param4;
  inter[4]=param5;
  ajouter(message,inter);
  }

 private class Message implements Serializable{

  private String textePrincipal;
  private Object[] parametres;

  private Message(String texte,Object[] param){
   textePrincipal=texte;
   parametres=param;
   }

  private String traduction(Locale l,int type){
   Object[] trad=new Object[parametres.length];
   for(int i=0;i<parametres.length;i++)
    trad[i]=colorier(traduction(parametres[i],l),i);
   String phrase=null;
   if(type==Const.MESSAGE_TYPE_COMMANDANT)
    if(Univers.existenceMessageInfo(textePrincipal))
     phrase=Univers.getMessageInfo(textePrincipal,l);
     else phrase=textePrincipal;
    else if(type==Const.MESSAGE_TYPE_SYSTEME)
      if(Univers.existenceMessageSysteme(textePrincipal))
        phrase=Univers.getMessageSysteme(textePrincipal)+" ({"+Integer.toString(parametres.length-1)+"})";
         else phrase=textePrincipal;
   MessageFormat m=new MessageFormat(phrase.replace('\'','£'));
   return m.format(trad).replace('£','\'');
   }

  private String colorier(String entree,int num){
   return "<i><font color=\"red\">"+entree+"</font></i>";
   }

  private String traduction(Object o,Locale l){
   if(o instanceof Position){
     Position p=(Position)o;
     if(!Univers.existenceSysteme(p)) return p.getDescription();
     else return Univers.getSysteme(p).getNomPosition();
     }
   if(o instanceof Float) return Float.toString(Utile.a1D(((Float)o).floatValue()));
   if(o instanceof Technologie) return ((Technologie)o).getNomComplet(l);
   return o.toString();
   }

  }

 }
