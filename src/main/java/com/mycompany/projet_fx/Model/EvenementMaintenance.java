package com.mycompany.projet_fx.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class EvenementMaintenance implements Serializable {
    private LocalDateTime dateHeure;
    private String machine;
    private String evenement; // "A" ou "D"
    private String operateur;
    private String cause;

    public EvenementMaintenance(LocalDateTime dateHeure, String machine, String evenement, String operateur, String cause) {
        this.dateHeure = dateHeure;
        this.machine = machine;
        this.evenement = evenement;
        this.operateur = operateur;
        this.cause = cause;
    }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public String getMachine() { return machine; }
    public String getEvenement() { return evenement; }
    public String getOperateur() { return operateur; }
    public String getCause() { return cause; }
}
