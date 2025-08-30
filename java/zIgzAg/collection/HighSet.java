// v1.00 01/12/00
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.collection;

import java.util.Set;

/**
 * Cette interface hérite de l'interface <i>java.util.Set</i> mais
 * ses caractéristiques sont légèrement différentes.
 * Consultez la documentation de la classe HighHashSet à ce propos.
 *
 * @author  Julien Buret
 * @version 1.00, 20/12/00
 */


public interface HighSet extends Set{

 public boolean contains(int o);

 public Object get(int o);

 public boolean remove(int o);

 }
