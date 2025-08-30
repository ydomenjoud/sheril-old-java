// v 1.20 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class BaliseHTML implements Cloneable,java.io.Serializable{



 private static HashMap MODELES;

 public static void ajouterModele(String nom,BaliseHTML balise){
  if(MODELES==null) MODELES=new HashMap();
  MODELES.put(nom,balise);
  }


 public static BaliseHTML getModele(String nom){
  if(MODELES==null) return null;
  return (BaliseHTML)MODELES.get(nom);
  }



 private boolean modele;


 public void setModele(){modele=true;}

 public boolean estModele(){return modele;}


 private ArrayList descendants;

 private String balise;


 private HashMap attributs;

 private String texteContenu;



 public String getNomBalise(){return balise;}

 public Map.Entry[] getAttributs(){
  if (attributs==null) return null; else return (Map.Entry[])attributs.entrySet().toArray(new Map.Entry[0]);
  }


 public String getValeurAttribut(String attribut){return (String)attributs.get(attribut);}


 public String getTexteContenu(){return texteContenu;}

 public ArrayList getDescendants(){return descendants;}


 public void setNomBalise(String nom){balise=nom;}


 public void setAttributs(String[][] att){
  if(att==null) attributs=null;
   else{
    if(attributs==null) attributs=new HashMap(att.length*2,1F);
    for(int i=0;i<att.length;i++)
     if((att[i][0]!=null)&&(att[i][1]!=null)) attributs.put(att[i][0],att[i][1]);
      else throw new IllegalArgumentException("Les attributs et leurs valeurs ne peuvent être null");
    }
  }


 public void setAttribut(String val,String valAtt){
  String[][] a=new String[1][2];
  a[0][0]=val;
  a[0][1]=valAtt;
  setAttributs(a);
  }


 public void setAttribut(Map map){
  if(map!=null) attributs=new HashMap(map);
   else map=null;
  }


 public BaliseHTML setTexteContenu(String texte){texteContenu=texte;return this;}

 public void setDescendants(ArrayList filles){descendants=filles;}

 public BaliseHTML(String nom){
  setNomBalise(nom);
  setAttributs(null);
  setTexteContenu(null);
  setDescendants(null);
  }

 public BaliseHTML(String nom,String texte,boolean m){
  modele=m;
  setNomBalise(nom);
  setAttributs(null);
  setTexteContenu(texte);
  setDescendants(null);
  }


 public BaliseHTML(String nom,String texte){
  setNomBalise(nom);
  setAttributs(null);
  setTexteContenu(texte);
  setDescendants(null);
  }


 public BaliseHTML(String nom,String texte,String[][] att){
  setNomBalise(nom);
  setAttributs(att);
  setTexteContenu(texte);
  setDescendants(null);
  }


 public BaliseHTML(String nom,String[][] att){
  this(nom,null,att);
  }


 public BaliseHTML(String nom,String texte,String att,String valAtt){
  setNomBalise(nom);
  setAttribut(att,valAtt);
  setTexteContenu(texte);
  setDescendants(null);
  }


 public BaliseHTML(String nom,String att,String valAtt){
  this(nom,null,att,valAtt);
  }


 public BaliseHTML ajout(BaliseHTML ajout){
  if (descendants==null) descendants=new ArrayList(1);
  descendants.add(ajout);
  return this;
  }


 public BaliseHTML ajout(String att,String valAtt){
  if((att==null)||(valAtt==null)) throw new IllegalArgumentException("Une valeur null n'est pas permise");
  if(attributs==null) setAttribut(att,valAtt);
   else attributs.put(att,valAtt);
  return this;
  }


 public BaliseHTML ajout(String contenu){
     return setTexteContenu(contenu);

  }


 public Object clone(){
  BaliseHTML retour=new BaliseHTML(balise,texteContenu);
  if(attributs!=null)
   retour.setAttribut(attributs);
   /*
   {
   String[] cle=(String[])attributs.keySet().toArray(new String[0]);
   for(int i=0;i<cle.length;i++)
    retour.setAttribut(cle[i],(String)attributs.get(cle[i]));
   }
   */
  if(descendants!=null) retour.setDescendants((ArrayList)descendants.clone());
  return retour;
  }


 public String toString(){
  return affiche(new StringBuffer(200),this).toString();
  }


 private static BaliseHTML getDescendantFinal(BaliseHTML b){
  BaliseHTML retour=b;
  while(retour.getDescendants()!=null)
   retour=(BaliseHTML)((List)retour.getDescendants()).get(0);
  return retour;
  }


 public static StringBuffer affiche(StringBuffer base,BaliseHTML entree){
  if(entree.estModele()){
   BaliseHTML m=getModele(entree.getNomBalise());
   getDescendantFinal(m).setTexteContenu(entree.getTexteContenu());
   //System.out.print(entree.getTexteContenu()+"/"+getDescendantFinal(entree).getTexteContenu());
   affiche(base,m);
   }
  else if(!entree.getNomBalise().equals("")) {
    base.append('<');
    base.append(entree.getNomBalise());
    Map.Entry[] a=entree.getAttributs();
    if(a!=null){
     for(int i=0;i<a.length;i++){
      base.append(' ');
      base.append(a[i].getKey());
      base.append('=');
      base.append('\"');
      base.append(a[i].getValue());
      base.append('\"');
      }
     }

    base.append('>');

    if(entree.getTexteContenu()!=null)
     base.append(entree.getTexteContenu());

    if(entree.getDescendants()!=null)
     for(int i=0;i<entree.getDescendants().size();i++)
      affiche(base,(BaliseHTML)entree.getDescendants().get(i));

    base.append('<');
    base.append('/');
    base.append(entree.getNomBalise());
    base.append('>');
    }
  else{    
      if(entree.getTexteContenu()!=null)
     base.append(entree.getTexteContenu());
    }

  return base;
  }


 //Un ensemble de balises courantes.

 /** La balise HTML. */
 public static final String HTML="HTML";
/** La balise HEAD. */
 public static final String HEAD="HEAD";
/** La balise LINK. */
 public static final String TITLE="TITLE";
/** La balise META. */
 public static final String META="META";
/** La balise BODY. */
 public static final String BODY="BODY";
/** La balise P. */
 public static final String P="P";
/** La balise B. */
 public static final String B="B";
/** La balise I. */
 public static final String I="I";
/** La balise H1. */
 public static final String H1="H1";
/** La balise TABLE. */
 public static final String TABLE="TABLE";
/** La balise TR. */
 public static final String TR="TR";
/** La balise TD. */
 public static final String TD="TD";
/** La balise FONT. */
 public static final String FONT="FONT";
/** La balise DIV. */
 public static final String DIV="DIV";
/** La balise IMG. */
 public static final String IMG="IMG";
/** La balise A. */
 public static final String A="A";
/** La balise FRAMESET. */
 public static final String FRAMESET="FRAMESET";
/** La balise FRAME. */
 public static final String FRAME="FRAME";
/** La balise BR. */
 public static final String BR="BR";
/** La balise UL. */
 public static final String UL="UL";
/** La balise OL. */
 public static final String OL="OL";
/** La balise LI. */
 public static final String LI="LI";
/** La balise U. */
 public static final String U="U";
    /** la Balise SPAN. */
    public static final String SPAN="SPAN";

 /** L'attribut align. */
 public static final String ALIGN="align";
 /** L'attribut content. */
 public static final String CONTENT="content";
 /** L'attribut bgcolor. */
 public static final String BGCOLOR="bgcolor";
 /** L'attribut background. */
 public static final String BACKGROUND="background";
 /** L'attribut color. */
 public static final String COLOR="color";
 /** L'attribut size. */
 public static final String SIZE="size";
 /** L'attribut http-equiv. */
 public static final String HTTP_EQUIV="http-equiv";
 /** L'attribut name. */
 public static final String NAME="name";
 /** L'attribut text. */
 public static final String TEXT="text";
 /** L'attribut type. */
 public static final String TYPE="type";
 /** L'attribut link. */
 public static final String LINK="link";
 /** L'attribut vlink. */
 public static final String VLINK="vlink";
 /** L'attribut border. */
 public static final String BORDER="border";
 /** L'attribut cellspacing. */
 public static final String CELLSPACING="cellspacing";
 /** L'attribut cellpadding. */
 public static final String CELLPADING="cellpadding";
 /** L'attribut colspan. */
 public static final String COLSPAN="colspan";
/** L'attribut src. */
 public static final String SRC="src";
/** L'attribut width. */
 public static final String WIDTH="width";
/** L'attribut height. */
 public static final String HEIGHT="height";
/** L'attribut href. */
 public static final String HREF="href";
 /** L'attribut valign. */
 public static final String VALIGN="valign";
 /** L'attribut frameborder. */
 public static final String FRAMEBORDER="frameborder";
 /** L'attribut framespacing. */
 public static final String FRAMESPACING="framespacing";
 /** L'attribut cols. */
 public static final String COLS="cols";
 /** L'attribut target. */
 public static final String TARGET="target";
 /** L'attribut class. */
 public static final String CLASS="class";
/** L'attibut class.*/
 public static final String REL="rel";
/** l'attribut style.*/
 public static final String STYLE="style";

 /**La valeur d'attribut "center".*/
 public static final String CENTER="center";

 /**La valeur d'attribut "2".*/
 public static final String T_2="2";
 /**La valeur d'attribut "5".*/
 public static final String T_5="5";
 /**La valeur d'attribut "6".*/
 public static final String T_6="6";




 }
