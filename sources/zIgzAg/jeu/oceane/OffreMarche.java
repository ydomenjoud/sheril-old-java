package zIgzAg.jeu.oceane;

import java.io.Serializable;

public class OffreMarche implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int numeroVendeur;
    private final Position positionOrigine;
    private final String codeMarchandise;
    private final int quantite;
    private final int prixUnitaire;
    private final int id;
    private final int tourFin;

    public OffreMarche(int id, int numeroVendeur, Position positionOrigine, String codeMarchandise, int quantite, int prixUnitaire, int tourFin) {
        this.id = id;
        this.numeroVendeur = numeroVendeur;
        this.positionOrigine = positionOrigine;
        this.codeMarchandise = codeMarchandise;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.tourFin = tourFin;
    }

    public static void gererFinDeVieEncheres() {
        int tourActuel = Univers.getTour();
        Univers.getListeOffresMarche()
                .removeIf(offre -> {
                    if (offre.getTourFin() < tourActuel) {
                        // Annulation de la vente
                        Systeme sys = Univers.getSysteme(offre.getPositionOrigine());
                        Commandant vendeur = Univers.getCommandant(offre.getNumeroVendeur());
                        if (vendeur != null) {
                            if (sys == null || !sys.estProprio(vendeur.getNumero())) {
                                vendeur.ajouterEvenement("EV_COMMANDANT_VENTE_GALACTIQUE_0001",
                                        offre.getDescription(),
                                        offre.getPositionOrigine());
                            } else {
                                sys.ajouterRichesses(vendeur.getNumero(), new ObjetSimpleTransporte(offre.getCodeMarchandise(), offre.getQuantite()), -1);
                                vendeur.ajouterEvenement("EV_COMMANDANT_VENTE_GALACTIQUE_0002",
                                        offre.getDescription(),
                                        offre.getPositionOrigine());
                            }
                        }
                        return true;
                    }
                    return false;
                });
    }

    private int getTourFin() {
        return tourFin;
    }

    public int getId() {
        return id;
    }

    public int getNumeroVendeur() {
        return numeroVendeur;
    }

    public Position getPositionOrigine() {
        return positionOrigine;
    }

    public String getCodeMarchandise() {
        return codeMarchandise;
    }

    public int getQuantite() {
        return quantite;
    }

    public int getPrixUnitaire() {
        return prixUnitaire;
    }

    public ObjetSimpleTransporte getObjetTransporte() {
        return new ObjetSimpleTransporte(codeMarchandise, quantite);
    }

    public String traductionChargement(){
        return getObjetTransporte().traductionChargement();
    }

    public String getDescription(){
        return getObjetTransporte().getDescription();
    }

}


