package com.mycompany.projet_fx.model;

import java.io.Serializable;

public class Equipement implements Serializable {
    private int id_equipement;

    public Equipement(int id_equipement) {
        this.id_equipement = id_equipement;
    }

    public int getId_equipement() {
        return id_equipement;
    }

    public void setId_equipement(int id_equipement) {
        this.id_equipement = id_equipement;
    }

    public String affiche() {
        return "identifiant = " + id_equipement;
    }
}
