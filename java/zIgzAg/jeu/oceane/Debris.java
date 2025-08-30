// v2.00 13/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Debris implements Serializable{

 static final long serialVersionUID = -7719554156860093481L;

 private Position position;
 private HashMap debris;

 //les méthodes d'accès

 public Position getPosition(){return position;}

 public Integer[] listeTypeDebris2(){
  return (Integer[])debris.keySet().toArray(new Integer[0]);
  }

 public Integer[] listeNombreDebris2(){
  return (Integer[])debris.values().toArray(new Integer[0]);
  }

 public Map.Entry[] listeDebris(){
  return (Map.Entry[])debris.entrySet().toArray(new Map.Entry[0]);
  }

 public int getNombreTotalDebris(){
  int retour=0;
  Integer[] l=listeNombreDebris2();
  for(int i=0;i<l.length;i++) retour=retour+l[i].intValue();
  return retour;
  }




 //les méthodes statiques


 //Le constructeur

 private Debris(){}

 public Debris(Position pos){
  position=(Position)pos.clone();
  }

 public Debris(Position pos,int type,int nombre){
  this(pos);
  ajouterObstacle(type,nombre);
  }

 //les autres méthodes

 public void ajouterObstacle(int type,int nombre){
  if (debris==null) debris=new HashMap(1);
  Integer cle=new Integer(type);
  Integer dejaPresent=(Integer)debris.get(cle);
  if(dejaPresent==null)
   debris.put(cle,new Integer(nombre));
   else debris.put(cle,new Integer(nombre+dejaPresent.intValue()));
  }

 public Debris fusion(Debris d){
  Integer[] lt=d.listeTypeDebris2();
  Integer[] ln=d.listeNombreDebris2();
  for(int i=0;i<lt.length;i++)
   ajouterObstacle(lt[i].intValue(),ln[i].intValue());
  return this;
  }

 public void gererCollisions(Commandant c,Flotte f){
  Map.Entry[] m=listeDebris();
  int draguage=f.capaciteDraguageMines();
  if(draguage!=0){
   for(int i=0;i<m.length;i++){
    int potentiel=((Integer)m[i].getValue()).intValue();
    int enleve=Math.min(draguage,potentiel);
    if(enleve==potentiel) debris.remove(m[i].getKey());
     else debris.put(m[i].getKey(),new Integer(potentiel-enleve));

    if(((Integer)m[i].getKey()).intValue()==Const.DEBRIS_MINES_CLASSIQUES)
     c.ajouterEvenement("EV_DEBRIS_COLLISION_0003",f.getNomNumero(c.numeroFlotte(f)),enleve);
    if(((Integer)m[i].getKey()).intValue()==Const.DEBRIS_RESTE_VAISSEAUX)
     c.ajouterEvenement("EV_DEBRIS_COLLISION_0004",f.getNomNumero(c.numeroFlotte(f)),enleve);
    if(draguage==enleve) break;
    }
   m=listeDebris();
   }

  for(int i=0;i<m.length;i++){
   if(((Integer)m[i].getKey()).intValue()==Const.DEBRIS_RESTE_VAISSEAUX){
    int nbDommages=f.gererCollision(((Integer)m[i].getValue()).intValue());
    if(nbDommages!=0)
     c.ajouterEvenement("EV_DEBRIS_COLLISION_0005",f.getNomNumero(c.numeroFlotte(f)),nbDommages);
    else c.ajouterEvenement("EV_DEBRIS_COLLISION_0006",f.getNomNumero(c.numeroFlotte(f)),nbDommages);
    }
   if(((Integer)m[i].getKey()).intValue()==Const.DEBRIS_MINES_CLASSIQUES){
    int nbDommages=f.gererCollision(((Integer)m[i].getValue()).intValue());
    if(nbDommages!=0)
     c.ajouterEvenement("EV_DEBRIS_COLLISION_0000",f.getNomNumero(c.numeroFlotte(f)),nbDommages);
     else c.ajouterEvenement("EV_DEBRIS_COLLISION_0002",f.getNomNumero(c.numeroFlotte(f)),nbDommages);
    }
   c.ajouterEvenement("EV_DEBRIS_COLLISION_0007",m[i].getValue());
   }

  if(f.getNombreDeVaisseaux()==0){
   c.ajouterEvenement("EV_DEBRIS_COLLISION_0001",f.getNomNumero(c.numeroFlotte(f)));
   if(c.getHerosSurFlotte(c.numeroFlotte(f))!=null)
    c.getHerosSurFlotte(c.numeroFlotte(f)).mourir(c);
   c.eliminerFlotte(c.numeroFlotte(f));
   }

  }




 }
