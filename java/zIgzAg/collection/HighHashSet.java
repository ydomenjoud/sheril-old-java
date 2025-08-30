// v1.00 01/12/00
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.AbstractSet;
import java.io.Serializable;

/** Similaire à la classe <i>java.util.HashSet</i>, sauf que si les références des objets
 * sont bien stockés, ils sont classés en utilisant le résultat de leur méthode
 * <i>hashCode()</i> uniquement. Deux objets ayant le même résultat par la méthode
 * <i>hashCode()</i> seront donc considérés comme identiques.
 * Le gros avantage de cette classe est que l'on peut récupérer les objets stockés
 * grace à la méthode <i>get(int o)</i>.
 *
 * @author  Julien Buret
 * @version 1.00, 26/12/00
 * @see java.util.HashSet
 */

public class HighHashSet extends AbstractSet implements HighSet, Cloneable, Serializable{

 /**
  * La map utilisé pour le stockage.
  */
 private transient IntHashMap map;

 public HighHashSet() {
  map = new IntHashMap();
  }

 public HighHashSet(Collection c) {
  map = new IntHashMap(Math.max(2*c.size(), 11));
  addAll(c);
  }

 public HighHashSet(int initialCapacity, float loadFactor) {
  map = new IntHashMap(initialCapacity, loadFactor);
  }

 public HighHashSet(int initialCapacity) {
  map = new IntHashMap(initialCapacity);
  }

 public Iterator iterator() {
  return map.values().iterator();
  }

 public int size() {
  return map.size();
  }

 public boolean isEmpty() {
  return map.isEmpty();
  }

 public boolean contains(Object o) {
  return map.containsKey(o.hashCode());
  }

 public boolean contains(int o) {
  return map.containsKey(o);
  }

 public boolean add(Object o) {
  return map.put(o.hashCode(), o)==null;
  }

 public Object get(int o) {
  return map.get(o);
  }

 public boolean remove(Object o) {
  return map.remove(o.hashCode())==null;
  }

 public boolean remove(int o) {
  return map.remove(o)==null;
  }

 public void clear() {
  map.clear();
  }

 public Object clone() {
  try{
   HighHashSet newSet = (HighHashSet)super.clone();
   newSet.map = (IntHashMap)map.clone();
   return newSet;
   }
  catch (CloneNotSupportedException e) {throw new InternalError();}
  }

// static final long serialVersionUID = 8286240980841280929L;

 private synchronized void writeObject(java.io.ObjectOutputStream s)
         throws java.io.IOException {
  s.defaultWriteObject();

  s.writeInt(map.capacity());
  s.writeFloat(map.loadFactor());

  s.writeInt(map.size());

  for (Iterator i=map.keySet().iterator(); i.hasNext(); )
   s.writeObject(i.next());
   }

 private synchronized void readObject(java.io.ObjectInputStream s)
         throws java.io.IOException, ClassNotFoundException {
  s.defaultReadObject();

  int capacity = s.readInt();
  float loadFactor = s.readFloat();
  map = new IntHashMap(capacity, loadFactor);

  int size = s.readInt();

  for (int i=0; i<size; i++) {
   Object e = s.readObject();
   add(e);
   }
  }

}
