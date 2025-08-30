// v1.00 01/12/00
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.collection;

import java.util.Map;

/**
 * Cette interface prolonge l'interface <i>java.util.Map</i>,
 * mais avec des entiers comme clés.
 *
 * @author  Julien Buret
 * @version 1.00, 20/12/00
 */

public interface IntMap extends Map{

 boolean containsKey(int key);

 Object get(int key);

 Object put(int key, Object value);

 Object remove(int key);

 public interface Entry extends java.util.Map.Entry {

  public int getIntKey();

  }

 }
