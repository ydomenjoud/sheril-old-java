package zIgzAg.jeu.oceane;

import java.util.*;

enum PointDeVictoireCategorie {
    PLANETES, // Territorial
    COMBATS, // Bataille
    POPULATION, // culturelle
    RECHERCHE, // scientifique
    MERVEILLE, // merveille
    POPULATION_VS // coloniale
}

class StatCategorie {
    private final int position;
    private final int valeur;

    public StatCategorie(int position, int valeur) {
        this.position = position;
        this.valeur = valeur;
    }

    public int getPosition() { return position; }
    public int getValeur() { return valeur; }
}


public class PointDeVictoire {
    static Map<PointDeVictoireCategorie, List<Commandant>> classement = new EnumMap<>(PointDeVictoireCategorie.class);

    static List<Commandant> classementGeneral;

    static final Map<PointDeVictoireCategorie, List<Integer>> config = new EnumMap<>(PointDeVictoireCategorie.class);

    static void calculerPointDeVictoire() {
        // configuration
        config.put(PointDeVictoireCategorie.PLANETES, List.of(4, 2, 1));
        config.put(PointDeVictoireCategorie.COMBATS, List.of(5, 3, 1));
        config.put(PointDeVictoireCategorie.POPULATION, List.of(2, 1));
        config.put(PointDeVictoireCategorie.RECHERCHE, List.of(3, 2, 1));
        config.put(PointDeVictoireCategorie.MERVEILLE, List.of(2, 1));
        config.put(PointDeVictoireCategorie.POPULATION_VS, List.of(3, 2, 1));

        // classement
        List<Commandant> baseList = Arrays.stream(Univers.getListeCommandantsHumains())
                .filter(c -> c.getTourArrivee() != Univers.getTour())
                .toList();
        classement.put(PointDeVictoireCategorie.PLANETES, baseList.stream()
                .sorted(Comparator.comparing(Commandant::getEvolutionPossession).reversed())
                .toList());
        classement.put(PointDeVictoireCategorie.COMBATS, baseList.stream()
                .sorted(Comparator.comparing(Commandant::getDegatsInfligesCeTour).reversed())
                .toList());
        classement.put(PointDeVictoireCategorie.POPULATION, baseList.stream()
                .sorted(Comparator.comparing(Commandant::getPopulationTotale).reversed())
                .toList());
        classement.put(PointDeVictoireCategorie.RECHERCHE, baseList.stream()
                .sorted(Comparator.comparing(Commandant::getScoreTechnologique).reversed())
                .toList());
        classement.put(PointDeVictoireCategorie.MERVEILLE, baseList.stream()
                .sorted(Comparator.comparing(Commandant::getMeilleurSystemeScore).reversed())
                .toList());
        classement.put(PointDeVictoireCategorie.POPULATION_VS, baseList.stream()
                .sorted(Comparator.comparing(Commandant::getEvolutionPopulationFlotte).reversed())
                .toList());

        // On parcourt chaque catégorie définie dans la config
        config.forEach((categorie, pointsAttribues) -> {
            List<Commandant> trie = classement.get(categorie);
            if (trie != null && !trie.isEmpty()) {

                int dernierRangAttribue = 0;
                int derniereValeur = -1;

                for (int i = 0; i < trie.size(); i++) {
                    Commandant c = trie.get(i);
                    int valeurActuelle = getValeurSelonCategorie(categorie, c);

                    // Calcul du rang (avec saut)
                    int rangDeCeJoueur = (i > 0 && valeurActuelle == derniereValeur)
                            ? dernierRangAttribue
                            : i + 1;

                    // On ne donne des points que si le rang est dans la config (1, 2 ou 3)
                    if (rangDeCeJoueur <= pointsAttribues.size()) {
                        int points = pointsAttribues.get(rangDeCeJoueur - 1);
                        c.ajouterPointsDeVictoire(points, rangDeCeJoueur, categorie);
                    }

                    dernierRangAttribue = rangDeCeJoueur;
                    derniereValeur = valeurActuelle;
                }
            }
        });

        // classement définitif
        classementGeneral = baseList.stream()
                .sorted(Comparator.comparing(Commandant::getPointsDeVictoire))
                .toList().reversed();
    }
    public static Map<Commandant, Map<PointDeVictoireCategorie, StatCategorie>> genererSyntheseCommandants() {
        List<Commandant> commandants = Arrays.asList(Univers.getListeCommandantsHumains());

        // On pré-calcule les rangs avec égalités pour chaque catégorie
        Map<PointDeVictoireCategorie, Map<Commandant, Integer>> rangsReels = new EnumMap<>(PointDeVictoireCategorie.class);

        for (PointDeVictoireCategorie cat : PointDeVictoireCategorie.values()) {
            List<Commandant> trie = classement.get(cat);
            Map<Commandant, Integer> mapPourCetteCat = new HashMap<>();

            for (int i = 0; i < trie.size(); i++) {
                Commandant actuel = trie.get(i);

                // Si c'est le premier, il est 1er.
                // Sinon, si sa valeur est égale au précédent, il prend le même rang.
                // Sinon, il prend sa position physique dans la liste + 1 (le saut de rang).
                if (i > 0 && getValeurSelonCategorie(cat, actuel) == getValeurSelonCategorie(cat, trie.get(i - 1))) {
                    mapPourCetteCat.put(actuel, mapPourCetteCat.get(trie.get(i - 1)));
                } else {
                    mapPourCetteCat.put(actuel, i + 1);
                }
            }
            rangsReels.put(cat, mapPourCetteCat);
        }

        // On construit la synthèse triée par numéro de commandant
        commandants.sort(Comparator.comparing(Commandant::getNumero));
        Map<Commandant, Map<PointDeVictoireCategorie, StatCategorie>> synthese = new LinkedHashMap<>();

        for (Commandant cmd : commandants) {
            Map<PointDeVictoireCategorie, StatCategorie> statsCmd = new EnumMap<>(PointDeVictoireCategorie.class);
            boolean estNouveau = cmd.getTourArrivee() == Univers.getTour();

            for (PointDeVictoireCategorie cat : PointDeVictoireCategorie.values()) {
                if (estNouveau) {
                    statsCmd.put(cat, new StatCategorie(0, 0));
                } else {
                    Integer rang = rangsReels.get(cat).get(cmd);
                    int valeur = getValeurSelonCategorie(cat, cmd);
                    statsCmd.put(cat, new StatCategorie(rang != null ? rang : 0, valeur));
                }
            }
            synthese.put(cmd, statsCmd);
        }

        return synthese;
    }

    // Helper pour extraire la donnée numérique selon la catégorie
    private static int getValeurSelonCategorie(PointDeVictoireCategorie cat, Commandant cmd) {
        return switch (cat) {
            case PLANETES -> cmd.getEvolutionPossession();
            case COMBATS -> (int)cmd.getDegatsInfligesCeTour();
            case POPULATION -> cmd.getPopulationTotale();
            case RECHERCHE -> cmd.getScoreTechnologique();
            case MERVEILLE -> cmd.getMeilleurSystemeScore();
            case POPULATION_VS -> cmd.getEvolutionPopulationFlotte();
        };
    }
}
