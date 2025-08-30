// v1.00 01/12/00
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.collection;

import java.util.Set;

/**
 * Cette interface prolonge l'interface <i>java.util.Set</i> pour les entiers.
 *
 * @author  Julien Buret
 * @version 1.00, 20/12/00
 */


public interface IntSet extends Set,IntCollection{

 public boolean contains(int o);

 public boolean add(int o);

 public boolean remove(int o);

 }
