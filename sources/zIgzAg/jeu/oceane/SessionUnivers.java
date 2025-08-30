// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionUnivers implements Serializable {

	private Map SYSTEMES;
	private Map DEBRIS;
	private Map COMMANDANTS;
	private Map PLANS_DE_VAISSEAUX;
	private Map ALLIANCES;
	private List TECHNOLOGIES_PUBLIQUES;
	private List LEADERS_EN_VENTE;

	public SessionUnivers(Univers u) {
		try {
			Field[] f = u.getClass().getDeclaredFields();
			AccessibleObject.setAccessible(f, true);
			Map m = new HashMap();
			for (int i = 0; i < f.length; i++)
				m.put(f[i].getName(), f[i]);
			SYSTEMES = (Map) ((Field) m.get("SYSTEMES")).get(u);
			DEBRIS = (Map) ((Field) m.get("DEBRIS")).get(u);
			COMMANDANTS = (Map) ((Field) m.get("COMMANDANTS")).get(u);
			PLANS_DE_VAISSEAUX = (Map) ((Field) m.get("PLANS_DE_VAISSEAUX"))
					.get(u);
			ALLIANCES = (Map) ((Field) m.get("ALLIANCES")).get(u);
			TECHNOLOGIES_PUBLIQUES = (List) ((Field) m
					.get("TECHNOLOGIES_PUBLIQUES")).get(u);
			LEADERS_EN_VENTE = (List) ((Field) m.get("LEADERS_EN_VENTE"))
					.get(u);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("???");
			System.exit(0);
		}

	}

}
