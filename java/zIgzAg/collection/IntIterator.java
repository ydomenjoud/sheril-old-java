// v1.00 01/12/00
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.collection;

import java.util.Iterator;

/**
 * Un IntIterator joue le même rôle que l'interface <i>java.util.Iterator</i>
 * mais concerne spécifiquement les entiers.
 *
 * @author  Julien Buret
 * @version 1.00, 20/12/00
 */

public interface IntIterator extends java.util.Iterator{

 int nextInt();

}
