// v1.00 01/12/00
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.collection;

import java.util.Collection;

/**
 * Cette interface prolonge l'interface <i>java.util.Collection</i> pour les entiers.
 *
 * @author  Julien Buret
 * @version 1.00, 20/12/00
 */

public interface IntCollection extends Collection {

 boolean contains(int o);

 int[] toIntArray();

 boolean add(int o);

 boolean remove(int o);

 }
