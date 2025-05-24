package com.mycompany.projet_fx.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Fiabilite implements Serializable {
    private String nomMachine;
    private LocalDateTime debutPanne;
    private LocalDateTime finPanne;
    private String cause;

    public Fiabilite(String nomMachine, LocalDateTime debutPanne, LocalDateTime finPanne, String cause) {
        this.nomMachine = nomMachine;
        this.debutPanne = debutPanne;
        this.finPanne = finPanne;
        this.cause = cause;
    }

    public String getNomMachine() { return nomMachine; }
    public void setNomMachine(String nomMachine) { this.nomMachine = nomMachine; }

    public LocalDateTime getDebutPanne() { return debutPanne; }
    public void setDebutPanne(LocalDateTime debutPanne) { this.debutPanne = debutPanne; }

    public LocalDateTime getFinPanne() { return finPanne; }
    public void setFinPanne(LocalDateTime finPanne) { this.finPanne = finPanne; }

    public String getCause() { return cause; }
    public void setCause(String cause) { this.cause = cause; }

    public double calculerFiabilite() {
        long dureePanne = java.time.Duration.between(debutPanne, finPanne).toMinutes();
        if (dureePanne <= 0) return 0.0;
        return 1.0; // Simplified for example; adjust as needed
    }

    @Override
    public String toString() {
        return "Fiabilite{" + "nomMachine=" + nomMachine
            + ", debutPanne=" + debutPanne
            + ", finPanne=" + finPanne
            + ", cause=" + cause + '}';
    }
}
