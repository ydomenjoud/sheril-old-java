// v1.00 01/12/00
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.collection;

import java.util.Collection;
import java.util.Iterator;
import java.io.Serializable;

/** Similaire à la classe <i>java.util.HashSet</i>, sauf que seuls
 * des entiers sont stockés.
 *
 * Comme d'habitude pour les classes de ce package,
 * il faut garder à l'esprit que les objets éventuellement stockés
 * ne le sont que sous la forme d'entiers représentant le résultat
 * de leur méthode <i>hashCode()</i>.
 *
 * @author  Julien Buret
 * @version 1.00, 26/12/00
 * @see java.util.HashSet
 */

public class IntHashSet extends AbstractIntSet implements IntSet, Cloneable, Serializable{

 /**
  * La map utilisé pour le stockage.
  */
 private transient IntHashMap map;


 /**
  * L'objet stocké (toujours le même, normal non? :o) )
  */
 private static final Object PRESENT = new Object();

 public IntHashSet() {
  map = new IntHashMap();
  }

 public IntHashSet(Collection c) {
  map = new IntHashMap(Math.max(2*c.size(), 11));
  addAll(c);
  }

 public IntHashSet(int initialCapacity, float loadFactor) {
  map = new IntHashMap(initialCapacity, loadFactor);
  }

 public IntHashSet(int initialCapacity) {
  map = new IntHashMap(initialCapacity);
  }

 public Iterator iterator() {
  return map.keySet().iterator();
  }

 public int size() {
  return map.size();
  }

 public boolean isEmpty() {
  return map.isEmpty();
  }

 public boolean contains(Object o) {
  return map.containsKey(o);
  }

 public boolean contains(int o) {
  return map.containsKey(o);
  }

 public boolean add(Object o) {
  return map.put(o, PRESENT)==null;
  }

 public boolean add(int o) {
  return map.put(o, PRESENT)==null;
  }

 public boolean remove(Object o) {
  return map.remove(o)==PRESENT;
  }

 public boolean remove(int o) {
  return map.remove(o)==PRESENT;
  }

 public void clear() {
  map.clear();
  }

 public Object clone() {
  try{
   IntHashSet newSet = (IntHashSet)super.clone();
   newSet.map = (IntHashMap)map.clone();
   return newSet;
   }
  catch (CloneNotSupportedException e) {throw new InternalError();}
  }

 static final long serialVersionUID = 8286240980841280929L;

 private synchronized void writeObject(java.io.ObjectOutputStream s)
         throws java.io.IOException {
  s.defaultWriteObject();

  s.writeInt(map.capacity());
  s.writeFloat(map.loadFactor());

  s.writeInt(map.size());

  for (IntIterator i=(IntIterator)map.keySet().iterator(); i.hasNext(); )
   s.writeInt(i.nextInt());
   }

 private synchronized void readObject(java.io.ObjectInputStream s)
         throws java.io.IOException, ClassNotFoundException {
  s.defaultReadObject();

  int capacity = s.readInt();
  float loadFactor = s.readFloat();
  map = new IntHashMap(capacity, loadFactor);

  int size = s.readInt();

  for (int i=0; i<size; i++) {
   int e = s.readInt();
   map.put(e, PRESENT);
   }
  }

}
