// v1.00 01/12/00
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.collection;

import java.util.Map;

/**
 * Cette interface prolonge l'interface <i>java.util.Map</i>,
 * mais avec des entiers comme clés et comme valeurs.
 *
 * @author  Julien Buret
 * @version 1.00, 20/12/00
 */

public interface IntIntMap extends Map{

  boolean containsKey(int key);

  boolean containsValue(int value);

  int get(int key);

  int put(int key,int value);

  int remove(int key);

  public interface Entry extends java.util.Map.Entry {

   public int getIntKey();

   public int getIntValue();

   public int setValue(int value);

   }

 }
