// v1.00 01/12/00
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.collection;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.AbstractSet;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.io.IOException;

/**
 * Cette classe est similaire à la classe <i>java.util.HashMap</i>, excepté
 * le fait qu'elle n'accepte que des entiers comme clés.
 *
 * La compatibilité avec l'interface <i>java.util.Map</i> est assurée,
 * mais comme d'habitude pour les classes de ce package,
 * il faut garder à l'esprit que les objets éventuellement stockés comme clés
 * ne le sont que sous la forme d'entiers représentant le résultat
 * de leur méthode <i>hashCode()</i>.
 *
 * @author  Julien Buret
 * @version 1.00, 20/12/00
 */

public class IntHashMap extends AbstractMap implements IntMap, Cloneable, Serializable {

 private transient Entry table[];

 private transient int count;

 private int threshold;

 private float loadFactor;

 private transient int modCount = 0;

 public IntHashMap(int initialCapacity, float loadFactor) {
  if (initialCapacity < 0)
   throw new IllegalArgumentException("Illegal Initial Capacity: "+initialCapacity);
  if (loadFactor <= 0)
   throw new IllegalArgumentException("Illegal Load factor: "+loadFactor);
  if (initialCapacity==0)
   initialCapacity = 1;
  this.loadFactor = loadFactor;
  table = new Entry[initialCapacity];
  threshold = (int)(initialCapacity * loadFactor);
  }

 public IntHashMap(int initialCapacity) {
  this(initialCapacity, 0.75f);
  }

 public IntHashMap() {
  this(101, 0.75f);
  }

 public IntHashMap(Map t) {
  this(Math.max(2*t.size(), 11), 0.75f);
  putAll(t);
  }

 public int size() {
  return count;
  }

 public boolean isEmpty() {
  return count == 0;
  }

 public boolean containsValue(Object value) {
  Entry tab[] = table;
  if (value==null) {
   for (int i = tab.length ; i-- > 0 ;)
	for (Entry e = tab[i] ; e != null ; e = e.next)
	 if (e.value==null)
	  return true;
   }
   else {
    for (int i = tab.length ; i-- > 0 ;)
	 for (Entry e = tab[i] ; e != null ; e = e.next)
	  if (value.equals(e.value))
	   return true;
	}

  return false;
  }

 public boolean containsKey(int key) {
  Entry tab[] = table;
  int index = (key & 0x7FFFFFFF) % tab.length;
  for (Entry e = tab[index]; e != null; e = e.next)
   if (e.hash==key) return true;
  return false;
  }

 public boolean containsKey(Object key) {
  if (key != null) return containsKey(key.hashCode());
   else return false;
  }

 public Object get(int key) {
  Entry tab[] = table;
  int index = (key & 0x7FFFFFFF) % tab.length;
  for (Entry e = tab[index]; e != null; e = e.next)
   if(e.hash == key)
    return e.value;
  return null;
  }

 public Object get(Object key) {
  if (key != null) return get(key.hashCode());
   else return null;
  }

 private void rehash() {
  int oldCapacity = table.length;
  Entry oldMap[] = table;

  int newCapacity = oldCapacity * 2 + 1;
  Entry newMap[] = new Entry[newCapacity];

  modCount++;
  threshold = (int)(newCapacity * loadFactor);
  table = newMap;

  for (int i = oldCapacity ; i-- > 0 ;)
   for (Entry old = oldMap[i] ; old != null ; ) {
    Entry e = old;
    old = old.next;

	int index = (e.hash & 0x7FFFFFFF) % newCapacity;
	e.next = newMap[index];
	newMap[index] = e;
    }

  }

 public Object put(int key, Object value) {
  Entry tab[] = table;
  int index = (key & 0x7FFFFFFF) % tab.length;
  for (Entry e = tab[index] ; e != null ; e = e.next)
   if (e.hash == key){
    Object old = e.value;
    e.value = value;
    return old;
    }

  modCount++;
  if (count >= threshold) {
   rehash();
   tab = table;
   index = (key & 0x7FFFFFFF) % tab.length;
   }


  Entry e = new Entry(key, value, tab[index]);
  tab[index] = e;
  count++;
  return null;
  }

 public Object put(Object key, Object value) {
  if(key==null) return null;
   else return put(key.hashCode(),value);
   }

 public Object remove(int key) {
  Entry tab[] = table;

  int index = (key & 0x7FFFFFFF) % tab.length;
  for (Entry e = tab[index], prev = null;e != null;prev = e, e = e.next)
   if (e.hash == key){
    modCount++;
    if (prev != null) prev.next = e.next;
     else tab[index] = e.next;
    count--;
    Object oldValue = e.value;
    e.value = null;
    return oldValue;
    }
  return null;
  }

 public Object remove(Object key) {
  if(key==null) return null;
   else return remove(key.hashCode());
  }

 public void putAll(Map t) {
  Iterator i = t.entrySet().iterator();
  if(t instanceof IntMap)
   while (i.hasNext()){
    IntMap.Entry e = (IntMap.Entry) i.next();
    put(e.getIntKey(), e.getValue());
	}
   else
    while (i.hasNext()){
     Map.Entry e = (Map.Entry) i.next();
	 put(e.getKey(), e.getValue());
	 }
  }

 public void clear() {
  Entry tab[] = table;
  modCount++;
  for (int index = tab.length; --index >= 0; )
   tab[index] = null;
  count = 0;
  }

 public Object clone() {
  try{
   IntHashMap t = (IntHashMap)super.clone();
   t.table = new Entry[table.length];
   for (int i = table.length ; i-- > 0 ; )
    t.table[i] = (table[i] != null) ? (Entry)table[i].clone() : null;
   t.keySet = null;
   t.entrySet = null;
   t.values = null;
   t.modCount = 0;
   return t;
   }
  catch (CloneNotSupportedException e) {
   throw new InternalError();
   }
  }

 // Views

 private transient IntSet keySet = null;
 private transient Set entrySet = null;
 private transient Collection values = null;

 public Set keySet() {
  if (keySet == null) {
   keySet = new AbstractIntSet() {

  	 public Iterator iterator() {return new HashIterator(KEYS);}

	 public int size() {return count;}

     public boolean contains(Object o) {return containsKey(o);}

     public boolean contains(int o) {return containsKey(o);}

  	 public boolean remove(Object o) {return IntHashMap.this.remove(o) != null;}

     public boolean remove(int o) {return IntHashMap.this.remove(o) != null;}

     public void clear() {IntHashMap.this.clear();}

     };
   }
  return keySet;
  }

 public Collection values() {
  if (values==null) {
   values = new AbstractCollection() {

    public Iterator iterator() {return new HashIterator(VALUES);}

    public int size() {return count;}

    public boolean contains(Object o) {return containsValue(o);}

    public void clear() {IntHashMap.this.clear();}

    };
   }
  return values;
  }

 public Set entrySet() {
  if (entrySet==null) {
   entrySet = new AbstractSet() {

     public Iterator iterator() {return new HashIterator(ENTRIES);}

     public boolean contains(Object o) {
      if (!(o instanceof IntMap.Entry))
       return false;
      IntMap.Entry entry = (IntMap.Entry)o;
      Entry tab[] = table;
      int hash = entry.getIntKey();
      int index = (hash & 0x7FFFFFFF) % tab.length;

      for (Entry e = tab[index]; e != null; e = e.next)
       if (e.hash==hash && e.equals(entry))
        return true;
      return false;
      }

  	 public boolean remove(Object o) {
      if (!(o instanceof IntMap.Entry))
       return false;
      IntMap.Entry entry = (IntMap.Entry)o;

      Entry tab[] = table;
      int hash = entry.getIntKey();
      int index = (hash & 0x7FFFFFFF) % tab.length;

      for (Entry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
       if (e.hash==hash && e.equals(entry)) {
        modCount++;
        if (prev != null) prev.next = e.next;
         else tab[index] = e.next;

        count--;
        e.value = null;
        return true;
        }
       }
      return false;
      }

     public int size() {return count;}

     public void clear() {IntHashMap.this.clear();}

     };
   }

  return entrySet;
  }

 private static class Entry implements IntMap.Entry {
  int hash;
  Object value;
  Entry next;

  Entry(int hash, Object value, Entry next) {
   this.hash = hash;
   this.value = value;
   this.next = next;
   }

  protected Object clone() {
   return new Entry(hash, value,(next==null ? null : (Entry)next.clone()));
   }

   //juste là pour l'interface(à ne pas utiliser :) )
  public Object getKey() {return new Integer(hash);}

  public int getIntKey(){return hash;}

  public Object getValue() {return value;}

  public Object setValue(Object value) {
   Object oldValue = this.value;
   this.value = value;
   return oldValue;
   }

  public boolean equals(Object o) {
   if (!(o instanceof IntMap.Entry))
    return false;
   IntMap.Entry e = (IntMap.Entry)o;

   return ((value==null ? e.getValue()==null : value.equals(e.getValue()))
	       &&(hash==e.getIntKey()));
   }

   public int hashCode() {return hash ^ (value==null ? 0 : value.hashCode());}

   public String toString() {
    return Integer.toString(hash)+"="+value.toString();
	}

   }

 // Types of Iterators
 private static final int KEYS = 0;
 private static final int VALUES = 1;
 private static final int ENTRIES = 2;

 private class HashIterator implements IntIterator {
  Entry[] table = IntHashMap.this.table;
  int index = table.length;
  Entry entry = null;
  Entry lastReturned = null;
  int type;

  private int expectedModCount = modCount;

  HashIterator(int type) {this.type = type;}

  public boolean hasNext() {
   while (entry==null && index>0)
	entry = table[--index];
   return entry != null;
   }

   //A ne pas utiliser pour les KEYS(hip lent) !!!!
  public Object next() {
   if (modCount != expectedModCount)
	throw new ConcurrentModificationException();

   while (entry==null && index>0)
	entry = table[--index];

   if (entry != null) {
	Entry e = lastReturned = entry;
	entry = e.next;
	return type == KEYS ? new Integer(e.hash) : (type == VALUES ? e.value : e);
	}
   throw new NoSuchElementException();
   }

   //Pour les KEYS.
   public int nextInt(){
    if (modCount != expectedModCount)
	 throw new ConcurrentModificationException();
    while (entry==null && index>0)
	 entry = table[--index];

	if (entry != null) {
	 Entry e = lastReturned = entry;
	 entry = e.next;
	 return type == KEYS ? e.hash : (type == VALUES ? e.value.hashCode() : e.hashCode());
	 }
	throw new NoSuchElementException();
    }

	public void remove() {
	 if (lastReturned == null)
	  throw new IllegalStateException();
	 if (modCount != expectedModCount)
	  throw new ConcurrentModificationException();

     Entry[] tab = IntHashMap.this.table;
     int index = (lastReturned.hash & 0x7FFFFFFF) % tab.length;
     for (Entry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
	  if (e == lastReturned) {
	   modCount++;
	   expectedModCount++;
	   if (prev == null) tab[index] = e.next;
	    else prev.next = e.next;
	   count--;
	   lastReturned = null;
	   return;
	   }
	  }
	throw new ConcurrentModificationException();
	}

   }

  private void writeObject(java.io.ObjectOutputStream s)
        throws IOException{
   s.defaultWriteObject();
   s.writeInt(table.length);
   s.writeInt(count);
   for (int index = table.length-1; index >= 0; index--) {
	Entry entry = table[index];
    while (entry != null) {
	 s.writeInt(entry.hash);
	 s.writeObject(entry.value);
	 entry = entry.next;
	 }
	}
   }

  private void readObject(java.io.ObjectInputStream s)
         throws IOException, ClassNotFoundException{
   s.defaultReadObject();
   int numBuckets = s.readInt();
   table = new Entry[numBuckets];
   int size = s.readInt();
   for (int i=0; i<size; i++) {
	int key = s.readInt();
	Object value = s.readObject();
	put(key, value);
	}
   }

  int capacity() {return table.length;}

  float loadFactor() {return loadFactor;}

  public boolean equals(Object o) {
   if (o == this)
    return true;
   if(!(o instanceof IntMap))
    return false;
   IntMap t = (IntMap) o;
   if (t.size() != size())
    return false;

   Iterator i = entrySet().iterator();
   while (i.hasNext()) {
    Entry e = (Entry) i.next();
	int key = e.getIntKey();
	Object value = e.getValue();
	if (value == null) {
	 if (!(t.get(key)==null && t.containsKey(key)))
	  return false;
	 }
     else
	  if (!value.equals(t.get(key)))
	   return false;
    }
   return true;
   }

  public String toString() {
   int max = size() - 1;
   StringBuffer buf = new StringBuffer();
   Iterator i = entrySet().iterator();

   buf.append("{");
   for (int j = 0; j <= max; j++) {
    Entry e = (Entry) (i.next());
	buf.append(e.getIntKey() + "=" + e.getValue());
	if (j < max)
     buf.append(", ");
	}
   buf.append("}");
   return buf.toString();
   }

}
