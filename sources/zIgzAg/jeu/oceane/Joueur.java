// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public class Joueur implements Serializable {

	static final long serialVersionUID = 7395505175740159403L;

	private String adresseElectronique;

	private String motDePasse;

	private String login;

	private int tourArrivee;

	private int dernierTourRendu;

	private String site;

	private boolean ordrePasseEnLocale;

	private boolean envoiStats;

	private boolean rapportSansBgNoir;

	private boolean detailOrdresPasses;

	private int maniereDeRecevoirSesRapports;

	// 0 mail 1 ftp

	private Locale langue;

	protected String nom;

	protected int race;

	protected int numero;

	protected int typeDeJoueur;

	// 0 humain 1 commandant neutre 2 robot

	// Les méthodes d'accès

	public int getNumero() {
		return numero;
	}

	public int getRace() {
		return race;
	}

	public String getNom() {
		return nom;
	}

	public String getNomNumerobis() {
		if (numero != 0)
			return nom + " (" + Integer.toString(numero) + ") ";
		else
			return nom;
	}

	public String getNomNumero() {
		if (numero != 0)
			return "<span class=\"race" + getRace() + "\">" + nom + "&nbsp;("
					+ Integer.toString(numero) + ")</span>";
		else
			return "<span class=\"neutre\">" + nom + "</span>";
	}

	public String toString() {
		return getNomNumero();
	}

	public String getAdresseElectronique() {
		return adresseElectronique;
	}

	public String getLogin() {
		return login;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public boolean aUnSite() {
		return site != null;
	}

	public String getSite() {
		return site;
	}

	public int getTourArrivee() {
		return tourArrivee;
	}

	public int getDernierTourRendu() {
		return dernierTourRendu;
	}

	public Locale getLocale() {
		if (langue == null)
			return Locale.FRENCH;
		else
			return langue;
	}

	public boolean getModePassationOrdres() {
		return ordrePasseEnLocale;
	}

	public boolean getEnvoiStats() {
		return envoiStats;
	}

	public boolean getRapportSansBgNoir() {
		return rapportSansBgNoir;
	}

	public boolean getModeDetailOrdresPasses() {
		return detailOrdresPasses;
	}

	public int getTypeReceptionRapport() {
		return maniereDeRecevoirSesRapports;
	}

	public int hashCode() {
		return numero * 10000 + tourArrivee;
	}

	public void setSite(String s) {
		site = s;
	}

	public void setNom(String n) {
		nom = n;
	}

	public void setRace(int r) {
		race = r;
	}

	public void setNumero(int n) {
		numero = n;
	}

	public void setTypeDeJoueur(int t) {
		typeDeJoueur = t;
	}

	public void setAdresseElectronique(String a) {
		adresseElectronique = a;
	}

	public void setMotDePasse(String m) {
		motDePasse = m;
	}

	public void setLogin(String l) {
		login = l;
	}

	public void setTourArrivee(int t) {
		tourArrivee = t;
	}

	public void setDernierTourRendu(int d) {
		dernierTourRendu = d;
	}

	public void setModePassationOrdres(boolean m) {
		ordrePasseEnLocale = m;
	}

	public void setEnvoiStats(boolean m) {
		envoiStats = m;
	}

	public void setRapportSansBgNoir(boolean m) {
		rapportSansBgNoir = m;
	}

	public void setModeDetailOrdresPasses(boolean m) {
		detailOrdresPasses = m;
	}

	public void setTypeReceptionRapport(int t) {
		maniereDeRecevoirSesRapports = t;
	}

	public boolean estJoueurHumain() {
		return !estJoueurNeutre();
	}

	public boolean estJoueurNeutre() {
		return numero == 0 ? true : false;
	}

	// le constructeur.

	protected Joueur() {
	}

	public Joueur(String n, int ra, int num, String adresse, String log,
			String mot, int tour) {
		nom = n;
		race = ra;
		numero = num;
		adresseElectronique = adresse;
		login = log;
		motDePasse = mot;
		tourArrivee = tour;
		dernierTourRendu = tour;
	}

	public boolean equals(Object o) {
		if (((Joueur) o).getNumero() == numero)
			return true;
		else
			return false;
	}

	public static Object getJoueur(int hashCode) {
		int num = hashCode / 10000;
		if (!Univers.existenceCommandant(num))
			return Integer.toString(num);
		Commandant c = Univers.getCommandant(num);
		if (c.getTourArrivee() == (hashCode % 10000))
			return c;
		else
			return Integer.toString(num);
	}

	public static void supprimerCommandant(Commandant c) {
		Commandant neutre = Univers.getCommandant(0);

		Position[] p = c.listePossession();
		for (int i = 0; i < p.length; i++) {
			Systeme s = Univers.getSysteme(p[i]);
			s.changementDeProprietaire(c.getNumero(), 0);
			neutre.transfererPossession(p[i], c.getPossession(p[i]));
		}

		Alliance[] a = Univers.getListeAlliances();
		for (int i = 0; i < a.length; i++) {
			if (a[i].estDirigeePar(c.getNumero()))
				a[i].setDirigeant(-1);
			if (a[i].getNumeroConcepteur() == c.getNumero())
				a[i].concepteurDisparu();
		}

		Leader[] l = c.listeLieutenants();
		for (int i = 0; i < l.length; i++)
			Univers.ajouterLeaderEnVente(l[i]);

		int[] pacte = c.listePactesDeNonAgression();
		for (int i = 0; i < pacte.length; i++) {
			Commandant com = Univers.getCommandant(pacte[i]);
			com.romprePacteDeNonAgression(c.getNumero());
		}

		PlanDeVaisseau[] plan = Univers.listePlansDeVaisseaux();
		for (int i = 0; i < plan.length; i++)
			if (plan[i].estConcepteur(c.getNumero()))
				plan[i].concepteurDisparu();

		Flotte[] flo = c.listeFlottes();
		for (int i = 0; i < flo.length; i++)
			if (flo[i].estLoueEnPartie())
				flo[i].retourLocation(c, -1, -1);

		Commandant[] listeC = Univers.getListeCommandants();
		for (int i = 0; i < listeC.length; i++) {
			flo = listeC[i].listeFlottes();
			for (int j = 0; j < flo.length; j++)
				if (flo[j].estLoueEnPartie())
					flo[j].retourLocation(listeC[i], -1, c.getNumero());
		}


		flo = c.listeFlottes();
		for (int i = 0; i < flo.length; i++)
			flo[i].initialiserVaisseaux();
		
		Univers.supprimerCommandant(c.getNumero());
		Univers.ajouterEvenement("ELIMINATION_COMMANDANT_0000", c
				.getNomNumero());
	}

	public static Commandant creerCommandant(String n, String a, int r, Map m) {
		// recherche du numéro de commandant
		int[] num = Univers.getNumerosCommandants();
		int numF = -1;
		for (int i = 0; i < num.length; i++)
			if (num[i] != i) {
				numF = i;
				break;
			}
		if (numF == -1)
			numF = num.length;

		Commandant c = new Commandant(n, r, numF, a, Utile.choisirLogin(), a
				.substring(0, Math.min(a.length(), 5)), Univers.getTour());
		c.setCentaures(20000F);

		String[] t = Const.RACE_TECHNOLOGIES[c.getRace()];
		if (t != null) {
			for (int z = 0; z < t.length; z++) {
				c.ajouterTechnologieConnue(t[z]);
			}
		}

		// On regarde les endroit pour débarquer le joueur
		Position[] p = Univers.listePositionsSystemesDisponibles(c.getRace());
		if (p.length == 0) {
			p = Univers.listePositionsSystemesDisponibles(-1);
			System.out.println("Systeme nouveau commandant non trouvé");
			System.exit(0);
		}
		Position choix = p[Univers.getInt(p.length)];
		p = null;

		// On en choisi un
		Commandant neutre = Univers.getCommandant(0);
		Systeme s = Univers.getSysteme(choix);
		s.changementDeProprietaire(0, c.getNumero());
		c.transfererPossession(choix, neutre.getPossession(choix));
		neutre.eliminerPossession(choix);
		c.setCapitale(choix);
		ObjetSimpleTransporte ost = new ObjetSimpleTransporte(Messages.MINERAI,
				300);
		s.ajouterRichesses(c.getNumero(), ost, Integer.MIN_VALUE);

		Planete[] pla = s.getPlanetes();
		for (int i = 0; i < pla.length; i++) {
			pla[i].ajouterBatiment(new ConstructionPlanetaire("mineI"));
			if (!pla[i].estHabite() && pla[i].meilleurPop() != -1) {
				pla[i].explorerPlanete(pla[i].meilleurPop());
				pla[i].ajouterPopulation(pla[i].meilleurPop(), Univers
						.getInt(50) + 300);
			}

		}

		pla[Univers.getInt(pla.length)]
				.ajouterBatiment(new ConstructionPlanetaire("chantierI"));
		pla[Univers.getInt(pla.length)]
				.ajouterBatiment(new ConstructionPlanetaire("radarII"));

		// On va créer le deuxième système

		// On cherche les positions vierges à 1 case de la capitale
		int[][] dis = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { 1, 1 },
				{ 1, -1 }, { -1, -1 }, { -1, 1 } };
		Position pos = new Position(0, 0, 0);
		boolean trouve = false;
		for (int i = 0; i < dis.length && !trouve; i++) {
			int cy = (choix.getY() + dis[i][0]) % (Const.BORNE_MAX + 1);
			int cx = (choix.getX() + dis[i][1]) % (Const.BORNE_MAX + 1);
			if (cy == 0)
				cy = 1;
			if (cx == 1)
				cx = 1;
			pos = new Position(0, cy, cx);
			if (!Univers.existenceSysteme(pos) && Position.existe(pos)) {
				trouve = true;
			}
		}
        System.out.println("2e système position " + pos);

		// On en prend un au hasard
		Position pos2 = pos;

		// On créé le système sur cette position
		Systeme s2 = Systeme.creerAuHasard(pos2, 30 - s.getNombrePlanetes());
		Univers.setSysteme(s2);
		c.ajouterPossession(pos2, Possession.creerAuHasard());

		// On l'améliore comme le premier
		ObjetSimpleTransporte ost2 = new ObjetSimpleTransporte(
				Messages.MINERAI, 300);
		s2.ajouterRichesses(c.getNumero(), ost2, Integer.MIN_VALUE);

		pla = s2.getPlanetes();
		for (int i = 0; i < pla.length; i++) {
			pla[i].setProprio(c.getNumero());
			pla[i].ajouterBatiment(new ConstructionPlanetaire("mineI"));
			if (!pla[i].estHabite() && pla[i].meilleurPop() != -1) {
				pla[i].explorerPlanete(pla[i].meilleurPop());
				pla[i].ajouterPopulation(pla[i].meilleurPop(), Univers
						.getInt(50)
						+ 300 * Univers.getTour());
			}

		}

		pla[Univers.getInt(pla.length)]
				.ajouterBatiment(new ConstructionPlanetaire("chantierI"));
		for (int e = 0; e < Univers.getTour(); e++)
			pla[Univers.getInt(pla.length)]
					.ajouterBatiment(new ConstructionPlanetaire("optpI"));
		pla[Univers.getInt(pla.length)]
				.ajouterBatiment(new ConstructionPlanetaire("radarII"));

		// Le reste pour la creation du joueur
		Flotte.choixFlotteDeDepart(c, m);

        // les soussous
        c.setCentaures(20000 + Univers.getTour() * 1000);
        // Si Cyborg on rajoute plus d'argent à la base
        if (c.getRace() == 5 && false)
            c.setCentaures(30000);

		c.setBudget(Const.BUDGET_COMMANDANT_TOUR_PRECEDENT, c.getCentaures());
		c.setBudget(Const.BUDGET_COMMANDANT_TOTAL_DISPONIBLE, c.getCentaures());

		Heros h = new Heros(Utile.getNomLeader(), new int[][] { { 8, 2 },
				{ 12, 1 } }, 2, 2, 2, 2, 2, c.getRace(), 0);
		Gouverneur g = new Gouverneur(Utile.getNomLeader(), new int[][] {
				{ 8, 2 }, { 12, 1 } }, 2, 2, 2, 2, 2, c.getRace(), 0);

		c.ajouterHeros(h);
		c.ajouterGouverneur(g);

		if (c.getRace() == 5)
			c.setReputation(-5000);

		Univers.setCommandant(c);
		Univers
				.ajouterEvenement("APPARITION_COMMANDANT_0000", c
						.getNomNumero());

		return c;
	}

}
