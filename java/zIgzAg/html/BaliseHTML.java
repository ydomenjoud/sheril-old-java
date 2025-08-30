// v 1.20 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/** Cette classe repésente une balise HTML et est utilisée dans la confection d'un document au format HTML.
  * @author Julien Buret
  * @version 1.2
  * @see DocumentHTML
  */
public class BaliseHTML implements Cloneable{

/** La <tt>HashMap</tt> où sont stockés les différents modèles.
  */

 private static HashMap MODELES;

/** Ajoute un modèle. Un modèle répond à une règle simple : lui et ses descendants ne contiennent chacun qu'un
  * descendant au plus.
  * @param nom le nom du modèle.
  * @param balise le modèle.
  */
 public static void ajouterModele(String nom,BaliseHTML balise){
  if(MODELES==null) MODELES=new HashMap();
  MODELES.put(nom,balise);
  }

/** Retourne un modèle.
  * @param nom le nom du modèle.
  * @return le modèle.
  */
 public static BaliseHTML getModele(String nom){
  if(MODELES==null) return null;
  return (BaliseHTML)MODELES.get(nom);
  }


/** Indique si la balise est une balise-modèle ou non.
  */
 private boolean modele;

/** Marque que la balise appartient à un modèle.
  */
 public void setModele(){modele=true;}

/** Indique si la balise coorespond à un modèle.
  * @return <tt>true</tt> si la balise correspond à un modèle, <tt>false</tt> sinon.
  */
 public boolean estModele(){return modele;}

/** Les balises &quot;filles&quot; de la balise.
  */
 private ArrayList descendants;

/** Le nom de la balise.
  */
 private String balise;

/** Les attributs de la balise et leurs valeurs.
  */
 private HashMap attributs;

/** Le texte éventuel contenu à l'interieur de la balise.
  */
 private String texteContenu;

/** Retourne le nom de la balise.
  * @return le nom de la balise.
  */
 public String getNomBalise(){return balise;}

/** Retourne les attributs de la balise.
  * @return une enumération des attributs de la balise ou <tt>null</tt> si il n'y en a pas.
  */
 public Map.Entry[] getAttributs(){
  if (attributs==null) return null; else return (Map.Entry[])attributs.entrySet().toArray(new Map.Entry[0]);
  }

/** Retourne la valeur de l'attribut correspondant.
  * @param attribut l'attribut dont la valeur est recherchée.
  * @return les valeurs des attributs de la balise ou <tt>null</tt> si il n'y en a pas.
  */
 public String getValeurAttribut(String attribut){return (String)attributs.get(attribut);}

/** Retourne le texte éventuellement contenu par la balise.
  * @return le texte contenu par la balise ou <tt>null</tt> si il n'y en a pas.
  */
 public String getTexteContenu(){return texteContenu;}

/** Retourne un <code>ArrayList</code> contenant les &quot;filles&quot; de la balise.
  * @return un <code>ArrayList</code> ou <tt>null</tt> si il n'y a pas de &quot;filles&quot;.
  */
 public ArrayList getDescendants(){return descendants;}

/** Définit le nom de la balise.
  * @param nom le nom de la balise.
  */
 public void setNomBalise(String nom){balise=nom;}

/** Définit les attributs de la balise avec leurs valeurs correspondantes.
  * @param att les attributs de la balise avec leurs valeurs correspondantes ou <tt>null</tt> si il n'y a pas d'attributs.
  * @exception IllegalArgumentException si l'un des composants des tableaux <code>att</code> ou <code>valAtt</code>
  *                                      est <tt>null</tt>.
  */
 public void setAttributs(String[][] att){
  if(att==null) attributs=null;
   else{
    if(attributs==null) attributs=new HashMap(att.length*2,1F);
    for(int i=0;i<att.length;i++)
     if((att[i][0]!=null)&&(att[i][1]!=null)) attributs.put(att[i][0],att[i][1]);
      else throw new IllegalArgumentException("Les attributs et leurs valeurs ne peuvent être null");
    }
  }

/** Définit l'unique attribut de la balise avec sa valeur.
  * @param att l'attribut de la balise ou <tt>null</tt> si il n'y a pas d'attribut.
  * @param valAtt la valeur de l'attribut de la balise ou <tt>null</tt> si il n'y a pas d'attribut.
  * @exception IllegalArgumentException si <code>val</code> ou <code>valAtt</code> est <tt>null</tt>.
  */
 public void setAttribut(String val,String valAtt){
  String[][] a=new String[1][2];
  a[0][0]=val;
  a[0][1]=valAtt;
  setAttributs(a);
  }

/** Assigne la <tt>Map</tt> décrivant les attributs.
  * @param map la map.
  */
 public void setAttribut(Map map){
  if(map!=null) attributs=new HashMap(map);
   else map=null;
  }

/** Définit le texte contenu par la balise.
  * @param texte le texte contenu par la balise ou <tt>null</tt> pour l'effacer.
  * @return la balise.
  */
 public BaliseHTML setTexteContenu(String texte){texteContenu=texte;return this;}

/** Définit un <code>ArrayList</code> contenant les &quot;filles&quot; de la balise.
  * @param filles un <code>ArrayList</code> ou <tt>null</tt> pour effacer les descendants de la balise.
  */
 public void setDescendants(ArrayList filles){descendants=filles;}

/** Un constructeur d'une balise toute simple.
  * @param nom le nom de la balise.
  */
 public BaliseHTML(String nom){
  setNomBalise(nom);
  setAttributs(null);
  setTexteContenu(null);
  setDescendants(null);
  }

/** Un constructeur d'une balise toute simple avec le texte qu'elle contient.
  * @param nom le nom de la balise.
  * @param texte le texte qu'elle contient.
  * @param m indique si la balise est un modèle ou non.
  */
 public BaliseHTML(String nom,String texte,boolean m){
  modele=m;
  setNomBalise(nom);
  setAttributs(null);
  setTexteContenu(texte);
  setDescendants(null);
  }

/** Un constructeur d'une balise toute simple avec le texte qu'elle contient.
  * @param nom le nom de la balise.
  * @param texte le texte qu'elle contient.
  */
 public BaliseHTML(String nom,String texte){
  setNomBalise(nom);
  setAttributs(null);
  setTexteContenu(texte);
  setDescendants(null);
  }

/** Un constructeur d'une balise avec le texte qu'elle contient ainsi que ses attributs.
  * @param nom le nom de la balise.
  * @param texte le texte qu'elle contient.
  * @param attri les attributs et leurs valeurs.
  * @exception IllegalArgumentException si l'un des composants des tableaux <code>att</code> ou <code>valAtt</code>
  *                                      est <tt>null</tt>.

  */
 public BaliseHTML(String nom,String texte,String[][] att){
  setNomBalise(nom);
  setAttributs(att);
  setTexteContenu(texte);
  setDescendants(null);
  }

/** Un constructeur d'une balise (qui ne contient pas de texte) avec ses attributs.
  * @param nom le nom de la balise.
  * @param att les attributs et leurs valeurs.
  * @exception IllegalArgumentException si l'un des composants des tableaux <code>att</code> ou <code>valAtt</code>
  *                                      est <tt>null</tt>.

  */
 public BaliseHTML(String nom,String[][] att){
  this(nom,null,att);
  }

/** Un constructeur d'une balise avec le texte qu'elle contient et un seul attribut.
  * @param nom le nom de la balise.
  * @param texte le texte qu'elle contient.
  * @param att l'attribut.
  * @param valAtt la valeur de l'attribut.
  * @exception IllegalArgumentException si <code>val</code> ou <code>valAtt</code> est <tt>null</tt>.
  */
 public BaliseHTML(String nom,String texte,String att,String valAtt){
  setNomBalise(nom);
  setAttribut(att,valAtt);
  setTexteContenu(texte);
  setDescendants(null);
  }

/** Un constructeur d'une balise (qui ne contient pas de texte) avec un seul attribut.
  * @param nom le nom de la balise.
  * @param att l'attribut.
  * @param valAtt la valeur de l'attribut.
  * @exception IllegalArgumentException si <code>val</code> ou <code>valAtt</code> est <tt>null</tt>.
  */
 public BaliseHTML(String nom,String att,String valAtt){
  this(nom,null,att,valAtt);
  }

/** Une méthode pour ajouter une &quot;fille&quot; à cette balise.
  * @param ajout la balise à ajouter.
  * @return un pointeur sur la balise obtenue.
  */
 public BaliseHTML ajout(BaliseHTML ajout){
  if (descendants==null) descendants=new ArrayList(1);
  descendants.add(ajout);
  return this;
  }

/** Une méthode pour ajouter un attribut et sa valeur à cette balise.
  * @param att l'attribut à rajouter.
  * @param valAtt la valeur de l'attribut à rajouter.
  * @return un pointeur sur la balise obtenue.
  * @exception IllegalArgumentException si <code>val</code> ou <code>valAtt</code> est <tt>null</tt>.
  */
 public BaliseHTML ajout(String att,String valAtt){
  if((att==null)||(valAtt==null)) throw new IllegalArgumentException("Une valeur null n'est pas permise");
  if(attributs==null) setAttribut(att,valAtt);
   else attributs.put(att,valAtt);
  return this;
  }

/** Renvoit un clone de la balise. Attention, les attributs et les descendants ne sont eux pas clonés.
  * @return le clone de la balise.
  */
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


/** Cette méthode renvoit une <code>String</code> représentant la balise.
  * @return la représentation de la balise.
  */
 public String toString(){
  return affiche(new StringBuffer(200),this).toString();
  }

/** Cette méthode renvoit descendant final de la balise <tt>b</tt>. (ca ne marche que en cas de descendant unique!)
  * @param b la balise en paramètre.
  * @return la balise descendant final.
  */
 private static BaliseHTML getDescendantFinal(BaliseHTML b){
  BaliseHTML retour=b;
  while(retour.getDescendants()!=null)
   retour=(BaliseHTML)((List)retour.getDescendants()).get(0);
  return retour;
  }

 /** Cette méthode renvoit un pointeur sur la <code>StringBuffer</code> de base augmentée de la représentation
  * de la balise <i>entree</i>.
  * @param base la <code>StringBuffer</code> de base.(non pas une <code>String</code> pour des raisons de rapidité.
  * @param entree la balise à représenter.
  * @return un pointeur sur la base.
  */
 public static StringBuffer affiche(StringBuffer base,BaliseHTML entree){
  if(entree.estModele()){
   BaliseHTML m=getModele(entree.getNomBalise());
   getDescendantFinal(m).setTexteContenu(entree.getTexteContenu());
   //System.out.print(entree.getTexteContenu()+"/"+getDescendantFinal(entree).getTexteContenu());
   affiche(base,m);
   }
   else{
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

  return base;
  }


 //Un ensemble de balises courantes.

 /** La balise HTML. */
 public static final String HTML="HTML";
/** La balise HEAD. */
 public static final String HEAD="HEAD";
/** La balise TITLE. */
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

 //Un ensemble d'attributs courants

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

 //Certaines valeurs d'attributs communs

 /**La valeur d'attribut "center".*/
 public static final String CENTER="center";

 /**La valeur d'attribut "2".*/
 public static final String T_2="2";
 /**La valeur d'attribut "5".*/
 public static final String T_5="5";
 /**La valeur d'attribut "6".*/
 public static final String T_6="6";




 }