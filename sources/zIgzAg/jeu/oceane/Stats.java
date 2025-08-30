// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

import zIgzAg.html.BaliseHTML;
import zIgzAg.html.DocumentHTML;

public class Stats {

	public static final String FICHIER_PUISSANCE = "puissance.htm";
	public static final String FICHIER_PLANETES = "planetes.htm";
	public static final String FICHIER_REPUTATION = "reputation.htm";
	public static final String FICHIER_TAUX_POSTE = "taux_poste.htm";
	public static final String FICHIER_ALLIANCES = "alliances.htm";
	public static final String FICHIER_CENTAURES = "centaures.htm";
	public static final String FICHIER_POPULATION = "pop.htm";
	public static final String FICHIER_ENCHERES = "encheres.htm";
	public static final String FICHIER_VAISSEAUX_PUBLICS = "vapub.htm";
	public static final String FICHIER_VAISSEAUX_NOMBRE = "vaisseaux.htm";
	public static final String FICHIER_UNIVERS = "univers.htm";
	public static final String FICHIER_HEROS = "hero.htm";
	public static final String FICHIER_FLOTTES = "flotte.htm";

	public static Map STATS_DERNIER_TOUR;

	public static <K, V> SortedMap<K, V> mapDuPlusGrandAuPlusPetit() {
		return new TreeMap<>(new ComparateurInverse());
	}

	public static <K, V> SortedMap<K, V> mapDuPlusPetitAuPlusGrand() {
		return new TreeMap<>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void ajouterDonnee(Map m, Object cle, Object valeur) {
		Object o = m.get(cle);
		if (o == null) {
			m.put(cle, valeur);
			return;
		}
		if (o instanceof List) {
			((List) o).add(valeur);
			// m.put(cle,o);
		} else {
			ArrayList a = new ArrayList();
			a.add(o);
			a.add(valeur);
			m.put(cle, a);
		}
	}

	@SuppressWarnings("unchecked")
	public static <K, V> V getPremier(SortedMap<K, V> m) throws NoSuchElementException {
		K key = m.firstKey();
		Object value = m.get(key);
		if (value instanceof List) {
			List<V> list = (List<V>) value;
			return list.get(0);
		} else {
			return (V) value;
		}
	}

	@SuppressWarnings("unchecked")
	public static <K, V> V getDernier(SortedMap<K, V> m) throws NoSuchElementException {
		K key = m.lastKey();
		Object value = m.get(key);
		if (value instanceof List) {
			List<V> list = (List<V>) value;
			return list.get(list.size() - 1);
		} else {
			return (V) value;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List[] getListe(SortedMap m, String f) {
		ArrayList k = new ArrayList(m.size());
		ArrayList v = new ArrayList(m.size());
		Map.Entry[] me = (Map.Entry[]) m.entrySet().toArray(new Map.Entry[0]);
		for (int i = 0; i < me.length; i++)
			if (me[i].getValue() instanceof List) {
				List l = (List) me[i].getValue();
				for (int j = 0; j < l.size(); j++) {
					k.add(me[i].getKey());
					v.add(l.get(j));
				}
			} else {
				k.add(me[i].getKey());
				v.add(me[i].getValue());
			}

		Map h = new HashMap();
		for (int i = 0; i < v.size(); i++)
			h.put(v.get(i).hashCode(), k.get(i));
		Univers.getStats().put(f, h);
		// System.out.println(Univers.getStats().get(f));

		Map msdt = (Map) STATS_DERNIER_TOUR.get(f);
		// System.out.println(msdt.size());
		if (msdt != null)
			for (int i = 0; i < k.size(); i++) {
				int modif = 0;
				Object o = msdt.get(v.get(i).hashCode());
				if ((o != null) && (o instanceof Number))
					modif = ((Number) k.get(i)).intValue()
							- ((Number) o).intValue();
				k.set(i, getModif(k.get(i), modif));
			}

		List[] retour = new List[2];
		retour[0] = k;
		retour[1] = v;
		return retour;
	}

	public static Object getModif(Object o, int modif) {
		String retour = null;
		if (o instanceof Float)
			retour = Float.toString(Utile.a1D(((Float) o).floatValue()));
		else
			retour = o.toString();
		if (modif != 0)
			if (modif > 0)
				retour = retour + " <FONT color=\"red\" size=\"2\"> (+"
						+ Integer.toString(modif) + ")</FONT>";
			else
				retour = retour + " <FONT color=\"#008000\" size=\"2\">("
						+ Integer.toString(modif) + ")</FONT>";
		return retour;
	}

	public static SortedMap<Integer, Commandant> trierParPuissance(Commandant[] c) {
		SortedMap<Integer, Commandant> st = mapDuPlusGrandAuPlusPetit();
		for (int i = 0; i < c.length; i++)
			ajouterDonnee(st, c[i].getPuissance(), c[i]);
		return st;
	}

	public static SortedMap<Integer, Commandant> trierParReputation(Commandant[] c) {
		SortedMap<Integer, Commandant> st = mapDuPlusGrandAuPlusPetit();
		for (int i = 0; i < c.length; i++)
			ajouterDonnee(st, c[i].getReputation(), c[i]);
		return st;
	}

	public static SortedMap<Integer, Commandant> trierParPlanetes(Commandant[] c) {
		SortedMap<Integer, Commandant> st = mapDuPlusGrandAuPlusPetit();
		for (int i = 0; i < c.length; i++)
			ajouterDonnee(st, c[i].getNombrePlanetesPossedees(),
					c[i]);
		return st;
	}

	public static SortedMap<Integer, Commandant> trierParTauxPoste(Commandant[] c) {
		SortedMap<Integer, Commandant> st = mapDuPlusPetitAuPlusGrand();
		for (int i = 0; i < c.length; i++)
			ajouterDonnee(st, c[i].getTauxTaxationPoste(), c[i]);
		return st;
	}

	public static SortedMap<Float, Commandant> trierParCentaures(Commandant[] c) {
		SortedMap<Float, Commandant> st = mapDuPlusGrandAuPlusPetit();
		for (int i = 0; i < c.length; i++)
			ajouterDonnee(st, c[i].getCentaures(), c[i]);
		return st;
	}

	public static SortedMap<Integer, Commandant> trierParPopulation(Commandant[] c) {
		SortedMap<Integer, Commandant> st = mapDuPlusGrandAuPlusPetit();
		for (int i = 0; i < c.length; i++)
			ajouterDonnee(st, c[i].getPopulationTotale(), c[i]);
		return st;
	}

	public static SortedMap<Integer, Commandant> trierParFlottes(Commandant[] c) {
		SortedMap<Integer, Commandant> st1 = mapDuPlusGrandAuPlusPetit();
		SortedMap<Integer, Commandant> st2 = mapDuPlusGrandAuPlusPetit();
		Flotte[] flottes;
		int flotte = 0;
		int ct = 0;
		int high;

		// Pour chaque commandant
		for (int i = 0; i < c.length; i++) {

			high = c[i].getPlusGrossePuissanceDeFlotte();
			// Si On a trouvÃ© au moins une flotte ( -1 = pas de flotte )
			if (high > -1)
				// On garde cette puissance
				ajouterDonnee(st1, high, c[i]);

		}
		ct = st1.size();
		for (int a = 0; a < Math.min(15, ct); a++) {
			Integer key = st1.firstKey();
			ajouterDonnee(st2, key, st1.get(key));
			st1.remove(key);
		}

		return st2;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List definirParametresPopulation(List[] l, Locale loc) {
		List retour = new ArrayList(l[0].size());
		Object[] p = null;
		for (int i = 0; i < l[0].size(); i++) {
			p = new Object[4];
			Commandant c = (Commandant) l[1].get(i);
			p[0] = c.getNom();
			p[1] = Rapport.getFont(Rapport.cC[6], null).setTexteContenu(
					Integer.toString(c.getNumero()));
			p[2] = Rapport.getRace(c.getRace(), loc);
			p[3] = l[0].get(i);
			retour.add(p);
		}
		return retour;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List definirParametresCentaures(List[] l, Locale loc) {
		List retour = new ArrayList(l[0].size());
		Object[] p = null;
		for (int i = 0; i < l[0].size(); i++) {
			p = new Object[4];
			Commandant c = (Commandant) l[1].get(i);
			p[0] = c.getNom();
			p[1] = Rapport.getFont(Rapport.cC[6], null).setTexteContenu(
					Integer.toString(c.getNumero()));
			p[2] = Rapport.getRace(c.getRace(), loc);
			p[3] = l[0].get(i);
			retour.add(p);
		}
		return retour;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List definirParametresTauxPoste(List[] l, Locale loc) {
		List retour = new ArrayList(l[0].size());
		Object[] p = null;
		for (int i = 0; i < l[0].size(); i++) {
			p = new Object[4];
			Commandant c = (Commandant) l[1].get(i);
			p[0] = c.getNom();
			p[1] = Rapport.getFont(Rapport.cC[6], null).setTexteContenu(
					Integer.toString(c.getNumero()));
			p[2] = Rapport.getRace(c.getRace(), loc);
			p[3] = l[0].get(i);
			retour.add(p);
		}
		return retour;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List definirParametresPlanetes(List[] l, Locale loc) {
		List retour = new ArrayList(l[0].size());
		Object[] p = null;
		for (int i = 0; i < l[0].size(); i++) {
			p = new Object[5];
			Commandant c = (Commandant) l[1].get(i);
			p[0] = c.getNom();
			p[1] = Rapport.getFont(Rapport.cC[6], null).setTexteContenu(
					Integer.toString(c.getNumero()));
			p[2] = Rapport.getRace(c.getRace(), loc);
			p[3] = l[0].get(i);
			p[4] = Utile.maj(c.getGrade());
			retour.add(p);
		}
		return retour;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List definirParametresReputation(List[] l, Locale loc) {
		List retour = new ArrayList(l[0].size());
		Object[] p = null;
		for (int i = 0; i < l[0].size(); i++) {
			p = new Object[5];
			Commandant c = (Commandant) l[1].get(i);
			p[0] = c.getNom();
			p[1] = Rapport.getFont(Rapport.cC[6], null).setTexteContenu(
					Integer.toString(c.getNumero()));
			p[2] = Rapport.getRace(c.getRace(), loc);
			p[3] = l[0].get(i);
			p[4] = Utile.maj(c.getStatutReputation());
			retour.add(p);
		}
		return retour;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List definirParametresPuissance(List[] l, Locale loc) {
		List retour = new ArrayList(l[0].size());
		Object[] p = null;
		for (int i = 0; i < l[0].size(); i++) {
			p = new Object[4];
			Commandant c = (Commandant) l[1].get(i);
			p[0] = c.getNom();
			p[1] = Rapport.getFont(Rapport.cC[6], null).setTexteContenu(
					Integer.toString(c.getNumero()));
			p[2] = Rapport.getRace(c.getRace(), loc);
			p[3] = l[0].get(i);
			retour.add(p);
		}
		return retour;
	}

	/** Parametre FLotte ! */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List definirParametresFlottes(List[] l, Locale loc) {
		List retour = new ArrayList(l[0].size());
		Object[] p = null;
		Flotte flotte;

		for (int i = 0; i < l[0].size(); i++) {
			p = new Object[4];
			Commandant c = (Commandant) l[1].get(i);
			p[0] = c.getNom();
			p[1] = Rapport.getFont(Rapport.cC[6], null).setTexteContenu(
					Integer.toString(c.getNumero()));
			p[2] = Rapport.getRace(c.getRace(), loc);
			flotte = c.getPlusGrosseFlotte();
			p[3] = flotte.getDescriptionPuissance(loc);
			retour.add(p);
		}
		return retour;
	}

	public static SortedMap<Integer, String> trierVaisseaux() {
		SortedMap<Integer, String> st = mapDuPlusGrandAuPlusPetit();
		Map<String, Integer> m = Univers.listeVaisseauxParType();
		Map.Entry<String, Integer>[] e = m.entrySet().toArray(new Map.Entry[0]);
		for (int i = 0; i < e.length; i++)
			ajouterDonnee(st, e[i].getValue(), e[i].getKey());
		return st;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List definirParametresVaisseaux(List[] l, Locale loc) {
		List retour = new ArrayList(l[0].size());
		Object[] p = null;
		for (int i = 0; i < l[0].size(); i++) {
			p = new Object[2];
			p[0] = l[1].get(i);
			p[1] = l[0].get(i);
			retour.add(p);
		}
		return retour;
	}

	public static SortedMap<Integer, Alliance> trierParMembres(Alliance[] c) {
		SortedMap<Integer, Alliance> st = mapDuPlusGrandAuPlusPetit();
		for (int i = 0; i < c.length; i++)
			ajouterDonnee(st, c[i].nombreDeMembres(), c[i]);
		return st;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List definirParametresAlliances(List[] l, Locale loc) {
		List retour = new ArrayList(l[0].size());
		Object[] p = null;
		for (int i = 0; i < l[0].size(); i++) {
			p = new Object[6];
			Alliance c = (Alliance) l[1].get(i);
			Commandant[] lc = c.getAdherents();
			p[0] = c.getNom();
			p[1] = c.getDescriptionDirigeant(loc);
			p[2] = l[0].get(i);
			p[3] = Commandant.getListeCommandants(lc);
			p[4] = c.getDescriptionType(loc);
			p[5] = Rapport.getALienE(c.getSite())
					.ajout(Rapport.getText(c.getSite()))
					.ajout(BaliseHTML.TARGET, "_blank");
			retour.add(p);
		}
		return retour;
	}

	public static void ecrireHeros(Heros[] heros, Locale loc) {
		String[] t = (String[]) Univers.getMessageRapport("STATS_HEROS", loc);
		BaliseHTML racine = Rapport.getDiv();
		racine.ajout(Rapport.getListeLeaders(heros, loc))
				.ajout(Rapport.sautP());
		DocumentHTML d = Rapport.getDocument(Chemin.STATS + FICHIER_HEROS,
				t[0], Rapport.getBody().ajout(racine));
		d.ecrire();
	}

	public static void ecrireEncheres(Locale loc) {
		String[] t = (String[]) Univers
				.getMessageRapport("STATS_ENCHERES", loc);
		Leader[] l = Univers.listeLeadersEnVente();
		ArrayList<Leader> h = new ArrayList<>(15);
		ArrayList<Leader> g = new ArrayList<>(15);
		for (int i = 0; i < l.length; i++)
			if (l[i].estHeros())
				h.add(l[i]);
			else
				g.add(l[i]);
		BaliseHTML racine = Rapport.getDiv();
		racine.ajout(Rapport.getFont(Rapport.cC[3], "6")
				.ajout(Rapport.getText(t[1])).ajout(Rapport.sautP()));
		racine.ajout(
				Rapport.getListeLeaders(h.toArray(new Heros[0]), loc))
				.ajout(Rapport.sautP());
		racine.ajout(Rapport.getFont(Rapport.cC[3], "6")
				.ajout(Rapport.getText(t[2])).ajout(Rapport.sautP()));
		racine.ajout(
				Rapport.getListeLeaders(
						g.toArray(new Gouverneur[0]), loc))
				.ajout(Rapport.sautP());
		DocumentHTML d = Rapport.getDocument(Chemin.STATS + FICHIER_ENCHERES,
				t[0], Rapport.getBody().ajout(racine));
		d.ecrire();
	}

	public static void ecrireVaisseauxPublics(Locale loc) {
		String[] t = (String[]) Univers.getMessageRapport(
				"STATS_VAISSEAUX_PUBLICS", loc);
		String[] t2 = (String[]) Univers.getMessageRapport(
				"PLANS_DE_VAISSEAUX", loc);
		PlanDeVaisseau[] p = Univers.listePlansDeVaisseauxPublics();
		BaliseHTML racine = Rapport.getDiv();
		racine.ajout(Rapport.getFont(Rapport.cC[3], "6")
				.ajout(Rapport.getText(t[0])).ajout(Rapport.sautP()));
		racine.ajout(Rapport.getPlansDeVaisseaux(p, t2, loc));
		DocumentHTML d = Rapport.getDocument(Chemin.STATS
				+ FICHIER_VAISSEAUX_PUBLICS, t[0],
				Rapport.getBody().ajout(racine));
		d.ecrire();
	}

	public static void ecrireUnivers(Locale loc) {
		String[] t = (String[]) Univers.getMessageRapport("STATS_UNIVERS", loc);
		String borneG = "g";
		BaliseHTML racine = Rapport.getDiv();
		racine.ajout(Rapport.getFont(Rapport.cC[3], "6")
				.ajout(Rapport.getText(t[0])).ajout(Rapport.sautP()));
		racine.ajout(Rapport.getABorne(borneG));
		racine.ajout(Rapport.getPopulation(
				Univers.listeSystemes(Univers.listePositionsSystemes()), loc));
		racine.ajout(Rapport.getPoste(Systeme.getMarchandises(Univers
				.listeSystemes(Univers.listePositionsSystemes())), loc));
		for (int i = 0; i < Const.NB_GALAXIES; i++) {
			racine.ajout(Rapport.getALienI(Integer.toString(i))
					.ajout(Rapport.getText(Univers.getMessage("NOMS_GALAXIES",
							i, loc))));
			racine.ajout(Rapport.getText(" - "));
			for (int j = 0; j < Const.NB_SECTEURS; j++) {
				racine.ajout(Rapport.getALienI(
						Integer.toString(i) + "-" + Integer.toString(j)).ajout(
						Rapport.getText(Integer.toString(j + 1))));
				racine.ajout(Rapport.getText(" - "));
			}
			racine.ajout(Rapport.sautP());
		}
		for (int i = 0; i < Const.NB_GALAXIES; i++) {
			racine.ajout(Rapport.getABorne(Integer.toString(i)).ajout(
					Rapport.getFont(Rapport.cC[3], "6").ajout(
							Rapport.getText(Univers.getMessage("NOMS_GALAXIES",
									i, loc)))));
			racine.ajout(Rapport.sautP());
			racine.ajout(Rapport.getPopulation(Univers.listeSystemes(Univers
					.listePositionsSystemesParGalaxie(i)), loc));
			racine.ajout(Rapport.getPoste(
					Systeme.getMarchandises(Univers.listeSystemes(Univers
							.listePositionsSystemesParGalaxie(i))), loc));
			for (int j = 0; j < Const.NB_SECTEURS; j++) {
				racine.ajout(Rapport.getABorne(
						Integer.toString(i) + "-" + Integer.toString(j)).ajout(
						Rapport.getFont(Rapport.cC[3], "6").ajout(
								Rapport.getText(Utile.maj(Univers.getMessage(
										"SECTEUR", loc))
										+ Integer.toString(j + 1)))));
				racine.ajout(Rapport.sautP());
				racine.ajout(Rapport.getPopulation(Univers
						.listeSystemes(Univers
								.listePositionsSystemesParSecteur(i, j + 1)),
						loc));
				racine.ajout(Rapport.getPoste(Systeme.getMarchandises(Univers
						.listeSystemes(Univers
								.listePositionsSystemesParSecteur(i, j + 1))),
						loc));
				racine.ajout(Rapport.getRelations(i, j + 1, loc));
			}
			racine.ajout(Rapport.sautP());
		}

		DocumentHTML d = Rapport.getDocument(Chemin.STATS + FICHIER_UNIVERS,
				t[0], Rapport.getBody().ajout(racine));
		d.ecrire();
	}

	public static void ecrire(String fichier, List l, Object o) {
		String[] t = (String[]) o;
		BaliseHTML[][] a = new BaliseHTML[l.size() + 1][t.length - 1];
		BaliseHTML div = Rapport.getDiv();

		div.ajout(
				Rapport.getFont(Rapport.cC[3], "6")
						.ajout(Rapport.getText(t[0]))).ajout(Rapport.sautP());
		for (int i = 1; i < t.length; i++)
			a[0][i - 1] = Rapport.getTD("center", null).ajout(
					Rapport.getFont(Rapport.cC[4], null).ajout(
							Rapport.getText(t[i])));
		for (int i = 0; i < l.size(); i++) {
			a[i + 1][0] = Rapport.getTD("center", null).ajout(
					Rapport.getFont(Rapport.cC[3], null).ajout(
							Rapport.getText(Integer.toString(i + 1))));
			for (int j = 0; j < t.length - 2; j++)
				a[i + 1][j + 1] = Rapport.getTD("center", null).ajout(
						Rapport.getText(((Object[]) l.get(i))[j].toString()));
		}

		div.ajout(DocumentHTML.creerTable(
				Rapport.getTable().ajout(BaliseHTML.WIDTH, "80%"), a));
		DocumentHTML d = Rapport.getDocument(Chemin.STATS + fichier, t[0],
				Rapport.getBody().ajout(div));
		d.ecrire();
	}

	public static void afficher(Locale l) {
		Commandant[] c = Univers.getListeCommandantsHumains();
		Alliance[] a = Univers.getListeAlliancesNonSecretes();
		STATS_DERNIER_TOUR = Univers.getStatsDernierTour();

		ecrire(FICHIER_PUISSANCE,
				definirParametresPuissance(
						getListe(trierParPuissance(c), FICHIER_PUISSANCE), l),
				Univers.getMessageRapport("STATS_PUISSANCE", l));
		ecrire(FICHIER_PLANETES,
				definirParametresPlanetes(
						getListe(trierParPlanetes(c), FICHIER_PLANETES), l),
				Univers.getMessageRapport("STATS_PLANETES", l));
		ecrire(FICHIER_REPUTATION,
				definirParametresReputation(
						getListe(trierParReputation(c), FICHIER_REPUTATION), l),
				Univers.getMessageRapport("STATS_REPUTATION", l));
		ecrire(FICHIER_TAUX_POSTE,
				definirParametresTauxPoste(
						getListe(trierParTauxPoste(c), FICHIER_TAUX_POSTE), l),
				Univers.getMessageRapport("STATS_TAUX_POSTE", l));
		ecrire(FICHIER_CENTAURES,
				definirParametresCentaures(
						getListe(trierParCentaures(c), FICHIER_CENTAURES), l),
				Univers.getMessageRapport("STATS_CENTAURES", l));
		ecrire(FICHIER_POPULATION,
				definirParametresPopulation(
						getListe(trierParPopulation(c), FICHIER_POPULATION), l),
				Univers.getMessageRapport("STATS_POPULATION", l));
		ecrire(FICHIER_VAISSEAUX_NOMBRE,
				definirParametresVaisseaux(
						getListe(trierVaisseaux(), FICHIER_VAISSEAUX_NOMBRE), l),
				Univers.getMessageRapport("STATS_VAISSEAUX", l));
		ecrire(FICHIER_ALLIANCES,
				definirParametresAlliances(
						getListe(trierParMembres(a), FICHIER_ALLIANCES), l),
				Univers.getMessageRapport("STATS_ALLIANCES", l));
		ecrire(FICHIER_FLOTTES,
				definirParametresFlottes(
						getListe(trierParFlottes(c), FICHIER_FLOTTES), l),
				Univers.getMessageRapport("STATS_FLOTTES", l));

		ecrireVaisseauxPublics(l);
		ecrireEncheres(l);
		ecrireUnivers(l);
		Rapport.ecrireLiensSites(l);

	}

	public static class ComparateurInverse implements Comparator<Object> {

		public ComparateurInverse() {
		}

		@SuppressWarnings("unchecked")
		public int compare(Object o1, Object o2) {
			return -((Comparable<Object>) o1).compareTo(o2);
		}

		public boolean equals(Object o) {
			if (this == o)
				return true;
			else
				return false;
		}

	}

}
