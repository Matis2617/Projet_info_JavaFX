package com.mycompany.projet_fx.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Fiabilite implements Serializable {
    private String nomMachine;
    private int totalTempsDeMarche; // en minutes
    private int totalTempsDePanne;  // en minutes
    private String cause;
    private LocalDateTime dateHeurePanne;

    public Fiabilite(String nomMachine, int totalTempsDeMarche, int totalTempsDePanne, String cause, LocalDateTime dateHeurePanne) {
        this.nomMachine = nomMachine;
        this.totalTempsDeMarche = totalTempsDeMarche;
        this.totalTempsDePanne = totalTempsDePanne;
        this.cause = cause;
        this.dateHeurePanne = dateHeurePanne;
    }

    public String getNomMachine() { return nomMachine; }
    public void setNomMachine(String nomMachine) { this.nomMachine = nomMachine; }

    public int getTotalTempsDeMarche() { return totalTempsDeMarche; }
    public void setTotalTempsDeMarche(int totalTempsDeMarche) { this.totalTempsDeMarche = totalTempsDeMarche; }

    public int getTotalTempsDePanne() { return totalTempsDePanne; }
    public void setTotalTempsDePanne(int totalTempsDePanne) { this.totalTempsDePanne = totalTempsDePanne; }

    public String getCause() { return cause; }
    public void setCause(String cause) { this.cause = cause; }

    public LocalDateTime getDateHeurePanne() { return dateHeurePanne; }
    public void setDateHeurePanne(LocalDateTime dateHeurePanne) { this.dateHeurePanne = dateHeurePanne; }

    public void ajouterTempsDeMarche(int minutes) {
        this.totalTempsDeMarche += minutes;
    }

    public void ajouterTempsDePanne(int minutes) {
        this.totalTempsDePanne += minutes;
    }

    public double calculerFiabilite() {
        int totalTemps = totalTempsDeMarche + totalTempsDePanne;
        if (totalTemps == 0) return 0.0;
        return (double) totalTempsDeMarche / totalTemps;
    }

    @Override
    public String toString() {
        return "Fiabilite{" + "nomMachine=" + nomMachine
            + ", totalTempsDeMarche=" + totalTempsDeMarche
            + ", totalTempsDePanne=" + totalTempsDePanne
            + ", cause=" + cause
            + ", dateHeurePanne=" + dateHeurePanne + '}';
    }
}
