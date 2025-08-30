// v1.00 01/12/00
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.


package zIgzAg.collection;

import java.util.Iterator;
import java.util.Collection;

/**
 * Similaire à la classe <i>java.util.AbstractCollection</i> mais pour les entiers.
 *
 * @author  Julien Buret
 * @version 1.00, 20/12/00
 * @see java.util.AbstractCollection
 */

public abstract class AbstractIntCollection implements IntCollection {

 protected AbstractIntCollection() {}

 public abstract Iterator iterator();

 public abstract int size();

 public boolean isEmpty() {
  return size() == 0;
  }

 public boolean contains(int o) {
  IntIterator e = (IntIterator)iterator();
  while (e.hasNext())
   if (o==e.nextInt())
    return true;
  return false;
  }

 public boolean contains(Object o) {
  if(o==null) return false;
   else return contains(o.hashCode());
  }

 public Object[] toArray() {
  Integer[] result = new Integer[size()];
  Iterator e = iterator();
  for (int i=0; e.hasNext(); i++)
   result[i] = (Integer)e.next();
  return result;
  }

 public int[] toIntArray(){
  int[] r=new int[size()];
  IntIterator e=(IntIterator)iterator();
  for (int i=0; e.hasNext(); i++)
   r[i] = e.nextInt();
  return r;
  }

 public Object[] toArray(Object a[]){
  return toArray();
  }

 public boolean add(Object o) {
  throw new UnsupportedOperationException();
  }

 /**
  * Ajoute un entier à la collection.
  * @param l'entier à ajouter.
  * @return <tt>true</tt> si la collection a changée après l'utilisation de
  *         cette méthode.
  */
 public boolean add(int o) {throw new UnsupportedOperationException();}

 public boolean remove(int o){
  IntIterator e =(IntIterator)iterator();
  while (e.hasNext())
   if(o==e.nextInt()){
    e.remove();
    return true;
    }
  return false;
  }

 public boolean remove(Object o) {
  if(o==null) return false;
   else return remove(o.hashCode());
  }

 public boolean containsAll(Collection c) {
  if(c instanceof IntCollection){
   IntIterator e =(IntIterator) c.iterator();
   while (e.hasNext())
    if(!contains(e.nextInt()))
	 return false;
   }
  else{
   Iterator e = c.iterator();
   while (e.hasNext())
    if(!contains(e.next()))
	 return false;
   }

  return true;
  }

 public boolean addAll(Collection c) {
  boolean modified = false;
  if(c instanceof IntCollection){
   IntIterator e =(IntIterator)c.iterator();
   while (e.hasNext())
	if(add(e.nextInt()))
	 modified = true;
   }
  else{
   Iterator e = c.iterator();
   while (e.hasNext())
	if(add(e.next()))
	 modified = true;
   }

  return modified;
  }

 public boolean removeAll(Collection c) {
  boolean modified = false;
  if(c instanceof IntCollection){
   IntCollection c2=(IntCollection)c;
   IntIterator e = (IntIterator)iterator();
   while (e.hasNext())
    if(c2.contains(e.nextInt())){
     e.remove();
	 modified = true;
  	 }
   }
   else{
    Iterator e = iterator();
	while (e.hasNext())
	 if(c.contains(e.next())) {
	  e.remove();
	  modified = true;
	  }
	}

  return modified;
  }

 public boolean retainAll(Collection c) {
  boolean modified = false;
  if(c instanceof IntCollection){
   IntCollection c2=(IntCollection)c;
   IntIterator e = (IntIterator)iterator();
   while (e.hasNext())
	if(!c2.contains(e.nextInt())){
	 e.remove();
	 modified = true;
	 }
    }
   else{
   	Iterator e = iterator();
	while (e.hasNext())
     if(!c.contains(e.next())){
	  e.remove();
	  modified = true;
	  }
    }

  return modified;
  }

 public void clear(){
  IntIterator e = (IntIterator)iterator();
  while (e.hasNext()){
   e.nextInt();
   e.remove();
   }
  }

 public String toString() {
  StringBuffer buf = new StringBuffer();
  IntIterator e =(IntIterator) iterator();
  buf.append("[");
  int maxIndex = size() - 1;
  for (int i = 0; i <= maxIndex; i++){
   buf.append(String.valueOf(e.nextInt()));
   if (i < maxIndex)
	buf.append(", ");
   }
  buf.append("]");
  return buf.toString();
  }

 }
