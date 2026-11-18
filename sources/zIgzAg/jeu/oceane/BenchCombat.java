package zIgzAg.jeu.oceane;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Banc d'essai du combat spatial : rejoue N fois des scénarios décrits dans des fichiers
 * texte, avec le vrai moteur (Combat.resoudreCombatSpatial), sous plusieurs jeux de règles.
 *
 * Usage :
 *   BenchCombat <fichier.bench|dossier>... [--n 1000] [--graine 1]
 *               [--regles actuel,mono,fiab,mono+fiab] [--csv resultats.csv]
 *
 * Le format des scénarios est décrit dans benchmarks/LISEZMOI.md.
 */
public class BenchCombat {

	private static final String[] ROMAINS = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };
	private static final String[] AGRESSIVITES = { "pillage", "fuyard", "prudent", "normal", "combatif", "rage" };
	private static final String[] CIBLES = { "proche", "chasseur", "bombardier", "cargo" };
	private static final int[] SEUILS_XP = { 0, 2, 10, 20, 50 };
	private static final int NOMBRE_SELON_BUDGET = -1;

	// ------------------------------------------------------------------ modèle

	private static class TypeVaisseau {
		String nom;
		String plan;
		int nombre;
		String[] composants;
		int xp = 0;
		int moral = 4;
		int race = 0;
		int[] pos;
		int[] cibles;

		int minerai() {
			return PlanDeVaisseau.determinerMineraiNecessaire(null, composants);
		}
	}

	private static class Camp {
		String lettre;
		int agressivite = Const.STRATEGIE_AGRESSIVITE_NORMAL;
		int cible = Const.STRATEGIE_CIBLE_PROCHE;
		boolean heros = false;
		int hAtt, hDef, hVit, hMoral, hSavoir, hFanatique;
		int hRace = -1;
		/** Budget de minerai de la flotte, ou -1 s'il n'est pas fixé. */
		int budgetMinerai = -1;
		List<TypeVaisseau> types = new ArrayList<>();

		int minerai() {
			int n = 0;
			for (TypeVaisseau t : types)
				n += t.nombre * t.minerai();
			return n;
		}

		int prix() {
			float n = 0;
			for (TypeVaisseau t : types)
				n += t.nombre * PlanDeVaisseau.determinerPrix(null, t.composants, 0);
			return Math.round(n);
		}

		int cases() {
			int n = 0;
			for (TypeVaisseau t : types)
				n += t.nombre * PlanDeVaisseau.determinerNombreDeCases(t.composants);
			return n;
		}

		String composition() {
			StringBuilder retour = new StringBuilder();
			for (TypeVaisseau t : types)
				retour.append(retour.length() == 0 ? "" : " + ").append(t.nombre).append(" ").append(t.nom);
			return retour.toString();
		}

		/** Calcule le nombre du type noté « * » pour remplir le budget de minerai. */
		void appliquerBudget() {
			TypeVaisseau libre = null;
			int fixe = 0;
			for (TypeVaisseau t : types)
				if (t.nombre == NOMBRE_SELON_BUDGET) {
					if (libre != null)
						throw new ErreurScenario("un seul type « * » est permis par camp (camp " + lettre + ")");
					libre = t;
				} else
					fixe += t.nombre * t.minerai();
			if (libre != null) {
				if (budgetMinerai < 0)
					throw new ErreurScenario("le type « * " + libre.nom + " » demande une option minerai=N pour le camp " + lettre);
				libre.nombre = (budgetMinerai - fixe) / libre.minerai();
				if (libre.nombre < 1)
					throw new ErreurScenario("le budget de " + budgetMinerai + " minerai du camp " + lettre
							+ " ne permet aucun « " + libre.nom + " » (" + libre.minerai() + " minerai pièce)");
			} else if ((budgetMinerai >= 0) && (fixe > budgetMinerai))
				throw new ErreurScenario("le camp " + lettre + " coûte " + fixe + " minerai, plus que son budget de " + budgetMinerai);
		}

		int nombreDeVaisseaux() {
			int n = 0;
			for (TypeVaisseau t : types)
				n += t.nombre;
			return n;
		}
	}

	private static class Scenario {
		String nom;
		String variante = "";
		Camp[] camps = { new Camp(), new Camp() };
	}

	private static class Variante {
		String nom;
		ReglesCombat regles = new ReglesCombat();
	}

	private static class Stats {
		int n, victoiresA, victoiresD, mutuelles, sansVainqueur;
		long rounds, pertesA, pertesD, dommagesA, dommagesD;
	}

	private static class ErreurScenario extends RuntimeException {
		ErreurScenario(String message) {
			super(message);
		}
	}

	// ------------------------------------------------------------------ lecture

	private static List<Scenario> lireFichier(Path fichier) throws IOException {
		List<Scenario> retour = new ArrayList<>();
		String nom = null;
		List<String> lignes = new ArrayList<>();
		List<Integer> numeros = new ArrayList<>();
		int numero = 0;
		for (String brute : Files.readAllLines(fichier, StandardCharsets.UTF_8)) {
			numero++;
			String ligne = brute.replaceFirst("#.*$", "").trim();
			if (ligne.isEmpty())
				continue;
			if (ligne.startsWith("[") && ligne.endsWith("]")) {
				if (nom != null)
					retour.addAll(developper(fichier, nom, lignes, numeros));
				nom = ligne.substring(1, ligne.length() - 1).trim();
				lignes = new ArrayList<>();
				numeros = new ArrayList<>();
			} else if (nom == null)
				throw new ErreurScenario(fichier + ":" + numero + " : ligne hors scénario, un titre [nom] est attendu avant");
			else {
				lignes.add(ligne);
				numeros.add(numero);
			}
		}
		if (nom != null)
			retour.addAll(developper(fichier, nom, lignes, numeros));
		return retour;
	}

	/** Développe les lignes « varier X = a,b,c » en un scénario par combinaison de valeurs. */
	private static List<Scenario> developper(Path fichier, String nom, List<String> lignes, List<Integer> numeros) {
		LinkedHashMap<String, String[]> variables = new LinkedHashMap<>();
		List<String> corps = new ArrayList<>();
		List<Integer> numerosCorps = new ArrayList<>();
		Pattern varier = Pattern.compile("^varier\\s+(\\w+)\\s*=\\s*(.+)$");
		for (int i = 0; i < lignes.size(); i++) {
			Matcher m = varier.matcher(lignes.get(i));
			if (m.matches())
				variables.put(m.group(1), m.group(2).trim().split("\\s*,\\s*"));
			else {
				corps.add(lignes.get(i));
				numerosCorps.add(numeros.get(i));
			}
		}
		List<Map<String, String>> combinaisons = new ArrayList<>();
		combinaisons.add(new LinkedHashMap<>());
		for (Map.Entry<String, String[]> v : variables.entrySet()) {
			List<Map<String, String>> suivantes = new ArrayList<>();
			for (Map<String, String> c : combinaisons)
				for (String valeur : v.getValue()) {
					Map<String, String> copie = new LinkedHashMap<>(c);
					copie.put(v.getKey(), valeur);
					suivantes.add(copie);
				}
			combinaisons = suivantes;
		}

		List<Scenario> retour = new ArrayList<>();
		for (Map<String, String> combinaison : combinaisons) {
			Scenario s = new Scenario();
			s.nom = nom;
			s.camps[0].lettre = "A";
			s.camps[1].lettre = "D";
			StringBuilder variante = new StringBuilder();
			for (Map.Entry<String, String> e : combinaison.entrySet())
				variante.append(variante.length() == 0 ? "" : " | ").append(e.getKey()).append("=").append(e.getValue());
			s.variante = variante.toString();
			for (int passe = 0; passe < 5; passe++)
				for (Map.Entry<String, String> e : combinaison.entrySet())
					s.variante = s.variante.replace("{" + e.getKey() + "}", e.getValue());
			for (int i = 0; i < corps.size(); i++) {
				String ligne = corps.get(i);
				// plusieurs passes : la valeur d'une variable peut elle-même contenir une autre variable
				for (int passe = 0; passe < 5; passe++)
					for (Map.Entry<String, String> e : combinaison.entrySet())
						ligne = ligne.replace("{" + e.getKey() + "}", e.getValue());
				try {
					lireLigne(s, ligne);
				} catch (ErreurScenario e) {
					throw new ErreurScenario(fichier + ":" + numerosCorps.get(i) + " : " + e.getMessage());
				}
			}
			for (Camp c : s.camps) {
				try {
					c.appliquerBudget();
				} catch (ErreurScenario e) {
					throw new ErreurScenario(fichier + " : scénario [" + nom + "]" + (s.variante.isEmpty() ? "" : " [" + s.variante + "]")
							+ " : " + e.getMessage());
				}
				if (c.nombreDeVaisseaux() == 0)
					throw new ErreurScenario(fichier + " : le scénario [" + nom + "] n'a aucun vaisseau pour le camp " + c.lettre);
			}
			retour.add(s);
		}
		return retour;
	}

	private static void lireLigne(Scenario s, String ligne) {
		Matcher m = Pattern.compile("^([AD])\\s*:\\s*(.*)$").matcher(ligne);
		if (!m.matches())
			throw new ErreurScenario("ligne non comprise (attendu « A: … », « D: … » ou « varier X = … ») : " + ligne);
		Camp camp = s.camps[m.group(1).equals("A") ? 0 : 1];
		String contenu = m.group(2).trim();
		if (contenu.matches("^(\\d+|\\*)\\s.*"))
			camp.types.add(lireVaisseaux(camp, contenu));
		else
			for (String option : contenu.split("\\s+"))
				lireOptionCamp(camp, option);
	}

	private static void lireOptionCamp(Camp camp, String option) {
		String[] kv = option.split("=", 2);
		if (kv.length != 2)
			throw new ErreurScenario("option de flotte non comprise : " + option);
		switch (kv[0]) {
		case "agressivite":
			camp.agressivite = indexOuEntier(AGRESSIVITES, kv[1], "agressivité");
			break;
		case "cible":
			camp.cible = indexOuEntier(CIBLES, kv[1], "type de cible");
			break;
		case "minerai":
			camp.budgetMinerai = borne(entier(kv[1], "minerai"), 1, 100000000, "minerai");
			break;
		case "heros":
			camp.heros = true;
			for (String champ : kv[1].split(",")) {
				String[] cv = champ.split(":", 2);
				if (cv.length != 2)
					throw new ErreurScenario("caractéristique de héros non comprise : " + champ);
				int valeur = entier(cv[1], "héros " + cv[0]);
				switch (cv[0]) {
				case "att": camp.hAtt = valeur; break;
				case "def": camp.hDef = valeur; break;
				case "vit": camp.hVit = valeur; break;
				case "moral": camp.hMoral = valeur; break;
				case "race": camp.hRace = valeur; break;
				case "savoir": camp.hSavoir = valeur; break;
				case "fanatique": camp.hFanatique = valeur; break;
				default:
					throw new ErreurScenario("caractéristique de héros inconnue : " + cv[0]
							+ " (att, def, vit, moral, race, savoir, fanatique)");
				}
			}
			break;
		default:
			throw new ErreurScenario("option de flotte inconnue : " + kv[0] + " (agressivite, cible, minerai, heros)");
		}
	}

	/** « 10 Chasseur = moteur@1 laser@2x3 ; xp=1 moral=6 pos=15,5 cibles=petits race=0 » */
	private static TypeVaisseau lireVaisseaux(Camp camp, String contenu) {
		Matcher m = Pattern.compile("^(\\d+|\\*)\\s+(.+?)\\s*=\\s*([^;]+)(?:;(.*))?$").matcher(contenu);
		if (!m.matches())
			throw new ErreurScenario("ligne de vaisseaux non comprise (attendu « <nombre> <nom> = <composants> [; options] ») : " + contenu);
		TypeVaisseau t = new TypeVaisseau();
		t.nombre = m.group(1).equals("*") ? NOMBRE_SELON_BUDGET : Integer.parseInt(m.group(1));
		t.nom = m.group(2);
		t.plan = camp.lettre + ":" + t.nom;
		for (TypeVaisseau autre : camp.types)
			if (autre.nom.equals(t.nom))
				throw new ErreurScenario("le type « " + t.nom + " » est défini deux fois pour le camp " + camp.lettre);

		List<String> composants = new ArrayList<>();
		Pattern composant = Pattern.compile("^(.+?)(?:@(\\d+))?(?:x(\\d+))?$");
		for (String jeton : m.group(3).trim().split("\\s+")) {
			Matcher c = composant.matcher(jeton);
			if (!c.matches())
				throw new ErreurScenario("composant non compris : " + jeton);
			String code = c.group(1);
			if (c.group(2) != null) {
				int niveau = Integer.parseInt(c.group(2));
				if ((niveau < 1) || (niveau > 10))
					throw new ErreurScenario("niveau hors de 1..10 : " + jeton);
				code = code + ROMAINS[niveau - 1];
			}
			if (!(Univers.getTechnologie(code) instanceof ComposantDeVaisseau))
				throw new ErreurScenario("composant de vaisseau inconnu : " + code + " (dans « " + jeton + " »)");
			int quantite = c.group(3) == null ? 1 : Integer.parseInt(c.group(3));
			for (int i = 0; i < quantite; i++)
				composants.add(code);
		}
		t.composants = composants.toArray(new String[0]);
		if (PlanDeVaisseau.determinerNombreDeCases(t.composants) > Const.TAILLE_VAISSEAUX[9][1])
			throw new ErreurScenario("le type « " + t.nom + " » dépasse la taille maximale d'un vaisseau");

		if (m.group(4) != null)
			for (String option : m.group(4).trim().split("\\s+")) {
				if (option.isEmpty())
					continue;
				String[] kv = option.split("=", 2);
				if (kv.length != 2)
					throw new ErreurScenario("option de vaisseau non comprise : " + option);
				switch (kv[0]) {
				case "xp":
					t.xp = borne(entier(kv[1], "xp"), 0, 4, "xp");
					break;
				case "moral":
					t.moral = borne(entier(kv[1], "moral"), 0, 11, "moral");
					break;
				case "race":
					t.race = borne(entier(kv[1], "race"), 0, Const.NB_RACES - 1, "race");
					break;
				case "pos":
					String[] xy = kv[1].split(",");
					if (xy.length != 2)
						throw new ErreurScenario("position attendue sous la forme pos=x,y : " + option);
					t.pos = new int[] { entier(xy[0], "pos"), entier(xy[1], "pos"), 0 };
					break;
				case "cibles":
					t.cibles = lireCibles(kv[1]);
					break;
				default:
					throw new ErreurScenario("option de vaisseau inconnue : " + kv[0] + " (xp, moral, race, pos, cibles)");
				}
			}
		return t;
	}

	/** Ordre de préférence des tailles de cible, de 1 (la plus petite) à 10. */
	private static int[] lireCibles(String valeur) {
		int[] retour = new int[Const.TAILLE_MAXIMAL_VAISSEAU];
		if (valeur.equals("petits")) {
			for (int i = 0; i < retour.length; i++)
				retour[i] = i + 1;
			return retour;
		}
		if (valeur.equals("gros")) {
			for (int i = 0; i < retour.length; i++)
				retour[i] = retour.length - i;
			return retour;
		}
		String[] tailles = valeur.split(",");
		boolean[] vues = new boolean[retour.length + 1];
		int n = 0;
		for (String taille : tailles) {
			int v = borne(entier(taille, "cibles"), 1, retour.length, "taille de cible");
			if (!vues[v]) {
				vues[v] = true;
				retour[n++] = v;
			}
		}
		// les tailles non citées suivent, de la plus grande à la plus petite (ordre par défaut du moteur)
		for (int v = retour.length; v >= 1; v--)
			if (!vues[v])
				retour[n++] = v;
		return retour;
	}

	private static int entier(String valeur, String quoi) {
		try {
			return Integer.parseInt(valeur.trim());
		} catch (NumberFormatException e) {
			throw new ErreurScenario("nombre entier attendu pour " + quoi + " : " + valeur);
		}
	}

	private static int borne(int valeur, int min, int max, String quoi) {
		if ((valeur < min) || (valeur > max))
			throw new ErreurScenario(quoi + " hors de " + min + ".." + max + " : " + valeur);
		return valeur;
	}

	private static int indexOuEntier(String[] noms, String valeur, String quoi) {
		for (int i = 0; i < noms.length; i++)
			if (noms[i].equals(valeur))
				return i;
		return borne(entier(valeur, quoi), 0, noms.length - 1, quoi);
	}

	// ------------------------------------------------------------------ construction

	private static void enregistrerPlans(Scenario s) {
		for (Camp c : s.camps)
			for (TypeVaisseau t : c.types) {
				String[] comp = t.composants;
				Univers.ajouterPlanDeVaisseau(new PlanDeVaisseau(0, "Banc d'essai", (String[]) comp.clone(), t.plan, "", "",
						0, -1, 0, PlanDeVaisseau.determinerCaracteristiquesSpeciales(null, comp),
						PlanDeVaisseau.determinerMineraiNecessaire(null, comp),
						PlanDeVaisseau.determinerPrix(null, comp, 0),
						PlanDeVaisseau.determinerMarchandisesNecessaires(null, comp),
						PlanDeVaisseau.determinerNombreDeCases(comp), 0, true));
			}
	}

	private static void supprimerPlans(Scenario s) {
		for (Camp c : s.camps)
			for (TypeVaisseau t : c.types)
				Univers.supprimerPlanDeVaisseau(t.plan);
	}

	private static Flotte construireFlotte(Camp c) {
		Flotte f = new Flotte(c.lettre, new Position(0, 0, 0));
		for (TypeVaisseau t : c.types)
			for (int i = 0; i < t.nombre; i++) {
				Vaisseau v = new Vaisseau(t.nom, t.plan, t.race);
				v.setExperience(SEUILS_XP[t.xp] * Const.BASE_NIVEAU_EXPERIENCE);
				v.setMoral(t.moral <= 9 ? t.moral * Const.BASE_NIVEAU_MORAL + Const.BASE_NIVEAU_MORAL / 2
						: (t.moral == 10 ? 12 : 15) * Const.BASE_NIVEAU_MORAL);
				f.ajouterVaisseau(v);
			}
		return f;
	}

	/** Une stratégie neuve à chaque combat : le moteur peut la modifier (symétrie du défenseur). */
	private static StrategieDeCombatSpatial construireStrategie(Camp c) {
		StrategieDeCombatSpatial s = new StrategieDeCombatSpatial("banc", c.agressivite, c.cible, new String[0],
				new int[0][0], new int[0][0]);
		for (TypeVaisseau t : c.types) {
			if (t.pos != null)
				s.positionnement.put(t.plan, (int[]) t.pos.clone());
			if (t.cibles != null)
				s.comportement.put(t.plan, (int[]) t.cibles.clone());
		}
		return s;
	}

	private static Heros construireHeros(Camp c) {
		if (!c.heros)
			return Heros.HEROS_NON_PRESENT;
		List<int[]> competences = new ArrayList<>();
		if (c.hSavoir > 0)
			competences.add(new int[] { Const.COMPETENCE_LEADER_MAITRISE_SAVOIR, c.hSavoir });
		if (c.hFanatique > 0)
			competences.add(new int[] { Const.COMPETENCE_LEADER_INSPIRATION_FANATIQUE, c.hFanatique });
		return new Heros("Héros " + c.lettre, competences.toArray(new int[0][0]), c.hVit, c.hAtt, c.hDef, c.hMoral, 0,
				c.hRace, 0);
	}

	// ------------------------------------------------------------------ exécution

	private static Stats jouer(Scenario s, Variante variante, int n, long graine) {
		ReglesCombat.activer(variante.regles);
		Stats stats = new Stats();
		int departA = s.camps[0].nombreDeVaisseaux();
		int departD = s.camps[1].nombreDeVaisseaux();
		for (int i = 0; i < n; i++) {
			Flotte f1 = construireFlotte(s.camps[0]);
			Flotte f2 = construireFlotte(s.camps[1]);
			StrategieDeCombatSpatial s1 = construireStrategie(s.camps[0]);
			StrategieDeCombatSpatial s2 = construireStrategie(s.camps[1]);
			Heros h1 = construireHeros(s.camps[0]);
			Heros h2 = construireHeros(s.camps[1]);
			// même graine pour le combat i de chaque variante : les comparaisons sont appariées
			Univers.setGraine(graine + i);
			ResultatCombatSpatial r = Combat.resoudreCombatSpatial(null, null, f1, f2, s1, s2, h1, h2, null, null);
			stats.n++;
			if (r.vainqueur1 && r.vainqueur2)
				stats.mutuelles++;
			else if (r.vainqueur1)
				stats.victoiresA++;
			else if (r.vainqueur2)
				stats.victoiresD++;
			else
				stats.sansVainqueur++;
			stats.rounds += r.nbRounds;
			stats.pertesA += departA - f1.getNombreDeVaisseaux();
			stats.pertesD += departD - f2.getNombreDeVaisseaux();
			stats.dommagesA += r.dommages1;
			stats.dommagesD += r.dommages2;
		}
		return stats;
	}

	private static Variante lireVariante(String nom) {
		Variante v = new Variante();
		v.nom = nom;
		v.regles.journal = false;
		for (String regle : nom.split("\\+"))
			switch (regle) {
			case "actuel":
				break;
			case "multi": // ancien moteur (avant le tir continu) : multi-cible par passes, sans enrayement
			case "ancien":
				v.regles.multiCible = true;
				v.regles.tirContinu = false;
				v.regles.ciblesVivantes = false;
				v.regles.fiabilite = false;
				break;
			case "mono": // moteur d'origine : une seule cible par vaisseau et par round, sans enrayement
				v.regles.multiCible = false;
				v.regles.tirContinu = false;
				v.regles.ciblesVivantes = false;
				v.regles.fiabilite = false;
				break;
			case "fiab":
				v.regles.fiabilite = true;
				break;
			default:
				if (regle.matches("fiab\\d+")) {
					v.regles.fiabilite = true;
					v.regles.enrayementUniforme = 100 - Integer.parseInt(regle.substring(4));
					break;
				}
				if (regle.equals("vivantes")) {
					v.regles.ciblesVivantes = true;
					break;
				}
				if (regle.equals("continu")) {
					v.regles.tirContinu = true;
					break;
				}
				if (regle.equals("fiaborigine")) {
					v.regles.fiabilite = true;
					v.regles.enrayementOrigine = true;
					break;
				}
				if (regle.equals("fiabniveautaille")) {
					v.regles.fiabilite = true;
					v.regles.enrayementNiveauTaille = true;
					break;
				}
				if (regle.matches("fiabplustaille\\d+")) {
					v.regles.fiabilite = true;
					v.regles.hausseParTaille = Integer.parseInt(regle.substring(14));
					break;
				}
				if (regle.matches("fiabtaille\\d+")) {
					v.regles.fiabilite = true;
					v.regles.reductionParTaille = Integer.parseInt(regle.substring(10));
					break;
				}
				java.util.regex.Matcher fiabt = java.util.regex.Pattern.compile("fiabt(\\d+)-(\\d+)").matcher(regle);
				if (fiabt.matches()) {
					v.regles.fiabilite = true;
					v.regles.enrayementT1 = Integer.parseInt(fiabt.group(1));
					v.regles.enrayementT10 = Integer.parseInt(fiabt.group(2));
					break;
				}
				throw new ErreurScenario("règle inconnue : " + regle + " (actuel, ancien, multi, mono, fiab, fiabNN, fiabtA-B, fiabtailleN, fiabplustailleK, fiabniveautaille, fiaborigine, continu, vivantes, combinables avec +)");
			}
		return v;
	}

	private static String pct(long partie, long total) {
		return String.format(Locale.ROOT, "%.1f", total == 0 ? 0.0 : 100.0 * partie / total);
	}

	private static String moy(long somme, long n) {
		return String.format(Locale.ROOT, "%.2f", n == 0 ? 0.0 : (double) somme / n);
	}

	private static String csv(String valeur) {
		return "\"" + valeur.replace("\"", "\"\"") + "\"";
	}

	private static void usage() {
		System.err.println("Usage : BenchCombat <fichier.bench|dossier>... [--n 1000] [--graine 1]");
		System.err.println("                    [--regles actuel,mono,fiab,mono+fiab] [--csv resultats.csv]");
	}

	public static void main(String[] args) throws IOException {
		int n = 1000;
		long graine = 1;
		String listeRegles = "actuel";
		String sortieCsv = null;
		List<Path> entrees = new ArrayList<>();
		try {
			for (int i = 0; i < args.length; i++) {
				switch (args[i]) {
				case "--n":
					n = borne(entier(args[++i], "--n"), 1, 10000000, "--n");
					break;
				case "--graine":
					graine = Long.parseLong(args[++i]);
					break;
				case "--regles":
					listeRegles = args[++i];
					break;
				case "--csv":
					sortieCsv = args[++i];
					break;
				default:
					if (args[i].startsWith("--"))
						throw new ErreurScenario("option inconnue : " + args[i]);
					entrees.add(Paths.get(args[i]));
				}
			}
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException | ErreurScenario e) {
			System.err.println("Arguments invalides : " + e.getMessage());
			usage();
			System.exit(2);
		}
		if (entrees.isEmpty()) {
			usage();
			System.exit(2);
		}

		Univers.initialiserPourBancDEssai(graine);

		List<Scenario> scenarios = new ArrayList<>();
		List<Variante> variantes = new ArrayList<>();
		try {
			for (String nom : listeRegles.split(","))
				variantes.add(lireVariante(nom.trim()));
			for (Path entree : entrees) {
				List<Path> fichiers = new ArrayList<>();
				if (Files.isDirectory(entree))
					try (Stream<Path> contenu = Files.list(entree)) {
						contenu.filter(p -> p.toString().endsWith(".bench")).sorted().forEach(fichiers::add);
					}
				else
					fichiers.add(entree);
				for (Path fichier : fichiers)
					scenarios.addAll(lireFichier(fichier));
			}
		} catch (ErreurScenario e) {
			System.err.println("Erreur : " + e.getMessage());
			System.exit(2);
		}
		if (scenarios.isEmpty()) {
			System.err.println("Aucun scénario trouvé (fichiers .bench attendus).");
			System.exit(2);
		}

		PrintWriter fichierCsv = sortieCsv == null ? null
				: new PrintWriter(Files.newBufferedWriter(Paths.get(sortieCsv), StandardCharsets.UTF_8));
		if (fichierCsv != null)
			fichierCsv.println("scenario,variante,regles,combats,vaisseaux_a,vaisseaux_d,victoires_a_pct,victoires_d_pct,"
					+ "destructions_mutuelles_pct,sans_vainqueur_pct,rounds_moy,pertes_a_moy,pertes_d_moy,"
					+ "dommages_subis_a_moy,dommages_subis_d_moy,minerai_a,minerai_d,prix_a,prix_d,cases_a,cases_d,"
					+ "composition_a,composition_d");

		long debut = System.currentTimeMillis();
		String format = "%-14s %7s %7s %7s %7s %7s %9s %9s %9s %9s%n";
		for (Scenario s : scenarios) {
			System.out.println();
			System.out.println("== " + s.nom + (s.variante.isEmpty() ? "" : " [" + s.variante + "]") + " — " + n
					+ " combats ==");
			for (Camp c : s.camps)
				System.out.println("   " + c.lettre + " : " + c.composition() + " — " + c.nombreDeVaisseaux() + " vaisseaux, "
						+ c.minerai() + " minerai, " + c.prix() + " centaures, " + c.cases() + " cases");
			System.out.printf(format, "règles", "vict.A%", "vict.D%", "mutuel%", "aucun%", "rounds", "pertes A", "pertes D",
					"dom. A", "dom. D");
			enregistrerPlans(s);
			for (Variante v : variantes) {
				Stats st = jouer(s, v, n, graine);
				System.out.printf(format, v.nom, pct(st.victoiresA, st.n), pct(st.victoiresD, st.n),
						pct(st.mutuelles, st.n), pct(st.sansVainqueur, st.n), moy(st.rounds, st.n), moy(st.pertesA, st.n),
						moy(st.pertesD, st.n), moy(st.dommagesA, st.n), moy(st.dommagesD, st.n));
				if (fichierCsv != null)
					fichierCsv.println(String.join(",", csv(s.nom), csv(s.variante), csv(v.nom), Integer.toString(st.n),
							Integer.toString(s.camps[0].nombreDeVaisseaux()),
							Integer.toString(s.camps[1].nombreDeVaisseaux()), pct(st.victoiresA, st.n),
							pct(st.victoiresD, st.n), pct(st.mutuelles, st.n), pct(st.sansVainqueur, st.n),
							moy(st.rounds, st.n), moy(st.pertesA, st.n), moy(st.pertesD, st.n), moy(st.dommagesA, st.n),
							moy(st.dommagesD, st.n), Integer.toString(s.camps[0].minerai()),
							Integer.toString(s.camps[1].minerai()), Integer.toString(s.camps[0].prix()),
							Integer.toString(s.camps[1].prix()), Integer.toString(s.camps[0].cases()),
							Integer.toString(s.camps[1].cases()), csv(s.camps[0].composition()),
							csv(s.camps[1].composition())));
			}
			supprimerPlans(s);
		}
		if (fichierCsv != null)
			fichierCsv.close();
		System.out.println();
		System.out.println(scenarios.size() + " scénario(s), " + variantes.size() + " jeu(x) de règles, "
				+ (System.currentTimeMillis() - debut) + " ms." + (sortieCsv == null ? "" : " CSV : " + sortieCsv));
	}

}
