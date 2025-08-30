// v2.00 13/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */

import java.lang.reflect.Field;
import java.util.ListResourceBundle;

public abstract class MessagesAbstraits extends ListResourceBundle {

  public Object[][] getContents() {
  Object[][] retour=null;
  try{Field[] champs=getClass().getFields();
      retour=new Object[champs.length][2];
      for(int i=0;i<champs.length;i++){
       retour[i][0]=champs[i].getName();
       retour[i][1]=champs[i].get(null);
       }
     }
  catch(IllegalAccessException e){}
  return retour;
  }

 }



