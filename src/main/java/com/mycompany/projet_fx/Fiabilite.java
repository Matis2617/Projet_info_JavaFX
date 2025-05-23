package com.mycompany.projet_fx.model;

import java.io.Serializable;

/**
 * Représente la fiabilité d'une machine (temps de marche/panne).
 */
public class Fiabilite implements Serializable {
    private String nomMachine;
    private int totalTempsDeMarche; // en minutes
    private int totalTempsDePanne;  // en minutes

    // Constructeur
    public Fiabilite(String nomMachine, int totalTempsDeMarche, int totalTempsDePanne) {
        this.nomMachine = nomMachine;
        this.totalTempsDeMarche = totalTempsDeMarche;
        this.totalTempsDePanne = totalTempsDePanne;
    }

    public String getNomMachine() { return nomMachine; }
    public void setNomMachine(String nomMachine) { this.nomMachine = nomMachine; }

    public int getTotalTempsDeMarche() { return totalTempsDeMarche; }
    public void setTotalTempsDeMarche(int totalTempsDeMarche) { this.totalTempsDeMarche = totalTempsDeMarche; }

    public int getTotalTempsDePanne() { return totalTempsDePanne; }
    public void setTotalTempsDePanne(int totalTempsDePanne) { this.totalTempsDePanne = totalTempsDePanne; }

    // Méthodes pour ajouter du temps
    public void ajouterTempsDeMarche(int minutes) {
        this.totalTempsDeMarche += minutes;
    }

    public void ajouterTempsDePanne(int minutes) {
        this.totalTempsDePanne += minutes;
    }

    /**
     * Calcul du taux de fiabilité (proportion de temps de marche).
     * @return fiabilité entre 0 et 1
     */
    public double calculerFiabilite() {
        int totalTemps = totalTempsDeMarche + totalTempsDePanne;
        if (totalTemps == 0) return 0.0;
        return (double) totalTempsDeMarche / totalTemps;
    }

    @Override
    public String toString() {
        return "Fiabilite{" + "nomMachine=" + nomMachine 
            + ", totalTempsDeMarche=" + totalTempsDeMarche 
            + ", totalTempsDePanne=" + totalTempsDePanne + '}';
    }
}
