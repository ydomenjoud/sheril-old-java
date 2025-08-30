// v1.00 01/12/00
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.collection;

import java.util.Collection;

/**
 * Similaire à la classe <i>java.util.AbstractSet</i>
 * mais pour les entiers.
 *
 * @author  Julien Buret
 * @version 1.00, 20/12/00
 * @see java.util.AbstractSet
 */

public abstract class AbstractIntSet extends AbstractIntCollection
                                     implements IntSet {
 protected AbstractIntSet() {}

 public boolean equals(Object o) {
  if (o == this)
   return true;
  if (!(o instanceof IntSet))
   return false;
  Collection c = (Collection) o;
  if (c.size() != size())
   return false;
  return containsAll(c);
  }

}

