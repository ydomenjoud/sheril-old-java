package zIgzAg.jeu.oceane;

import java.io.Serializable;

public class OffreMarche implements Serializable {

    private static final long serialVersionUID = 1L;

    private int numeroVendeur;
    private Position positionOrigine;
    private String codeMarchandise;
    private int quantite;
    private int prixUnitaire;
    private int id;

    public OffreMarche(int id, int numeroVendeur, Position positionOrigine, String codeMarchandise, int quantite, int prixUnitaire) {
        this.id = id;
        this.numeroVendeur = numeroVendeur;
        this.positionOrigine = positionOrigine;
        this.codeMarchandise = codeMarchandise;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
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

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
