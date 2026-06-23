package zIgzAg.jeu.oceane;

import java.util.*;
import java.util.List;

enum PointDeVictoireCategorie {
    PLANETES, // Territorial
    COMBATS, // Bataille
    POPULATION, // culturelle
    RECHERCHE, // scientifique
    MERVEILLE, // merveille
    POPULATION_VS // coloniale
}

record StatCategorie(int position, int valeur) {
}


public class PointDeVictoire {
    static TreeMap<Integer, Map<PointDeVictoireCategorie, StatCategorie>> pdvAttribuesCetour = new TreeMap<>();

    static Map<PointDeVictoireCategorie, List<Commandant>> classement = new EnumMap<>(PointDeVictoireCategorie.class);

    static List<Commandant> classementGeneral;

    static final Map<PointDeVictoireCategorie, List<Integer>> config = new EnumMap<>(PointDeVictoireCategorie.class);

    static void calculerPointDeVictoire() {
        // configuration de la répartition des points de victoire
        config.put(PointDeVictoireCategorie.PLANETES, List.of(4, 2, 1));
        config.put(PointDeVictoireCategorie.COMBATS, List.of(5, 3, 1));
        config.put(PointDeVictoireCategorie.POPULATION, List.of(2, 1));
        config.put(PointDeVictoireCategorie.RECHERCHE, List.of(3, 2, 1));
        config.put(PointDeVictoireCategorie.MERVEILLE, List.of(2, 1));
        config.put(PointDeVictoireCategorie.POPULATION_VS, List.of(3, 2, 1));

        // planetes : evolution du tour
        // combats : evolution du tour
        // recherche : evolution du tour
        // pop vs : evolution du tour
        // population : stock actuel
        // merveille : stock actuel


        // classement
        // on vire les commandant qui sont arrivés ce tour
        List<Commandant> baseList = Arrays.stream(Univers.getListeCommandantsHumains())
                .filter(c -> c.getTourArrivee() != Univers.getTour())
                .toList();

        // EVOLUTION
        classement.put(PointDeVictoireCategorie.PLANETES, baseList.stream()
                .sorted(Comparator.comparing(Commandant::getEvolutionPossession).reversed())
                .toList());
        classement.put(PointDeVictoireCategorie.COMBATS, baseList.stream()
                .sorted(Comparator.comparing(Commandant::getEvolutionDegatsInfligeesCeTour).reversed())
                .toList());
        classement.put(PointDeVictoireCategorie.POPULATION_VS, baseList.stream()
                .filter(c -> c.getTotalPopulationVS() > 0)
                .sorted(Comparator.comparing(Commandant::getEvolutionPopulationFlotte).reversed())
                .toList());
        classement.put(PointDeVictoireCategorie.RECHERCHE, baseList.stream()
                .sorted(Comparator.comparing(Commandant::getEvolutionTechnologique).reversed())
                .toList());
        // STOCK
        classement.put(PointDeVictoireCategorie.POPULATION, baseList.stream()
                .sorted(Comparator.comparing(Commandant::getPopulationTotale).reversed())
                .toList());
        classement.put(PointDeVictoireCategorie.MERVEILLE, baseList.stream()
                .sorted(Comparator.comparing(Commandant::getMeilleurRayonnement).reversed())
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
                    int points = 0;
                    if (rangDeCeJoueur <= pointsAttribues.size()) {
                        points = pointsAttribues.get(rangDeCeJoueur - 1);
                        c.ajouterPointsDeVictoire(points, rangDeCeJoueur, categorie);
                    }
                    // on stock globalement pour les stats futures
                    pdvAttribuesCetour
                            .computeIfAbsent(c.getNumero(), t -> new HashMap<>())
                            .put(categorie, new StatCategorie(rangDeCeJoueur, valeurActuelle));

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


    // Helper pour extraire la donnée numérique selon la catégorie
    private static int getValeurSelonCategorie(PointDeVictoireCategorie cat, Commandant cmd) {
        return switch (cat) {
            case PLANETES -> cmd.getEvolutionPossession();
            case COMBATS -> cmd.getEvolutionDegatsInfligeesCeTour();
            case RECHERCHE -> cmd.getEvolutionTechnologique();
            case POPULATION_VS -> cmd.getEvolutionPopulationFlotte();
            // stock
            case POPULATION -> cmd.getPopulationTotale();
            case MERVEILLE -> (int)cmd.getMeilleurRayonnement();
        };
    }
}
