package com.mycompany.projet_fx.Model;

import java.io.Serializable;

public class Produit implements Serializable {
    private String id;
    private Gamme gamme; // La gamme utilisée pour le fabriquer

    public Produit(String id, Gamme gamme) {
        this.id = id;
        this.gamme = gamme;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Gamme getGamme() { return gamme; }
    public void setGamme(Gamme gamme) { this.gamme = gamme; }

    // Prix = coût total de la gamme ici
    public float getPrix() { return gamme != null ? gamme.coutGamme() : 0; }
    public float getDuree() { return gamme != null ? gamme.dureeGamme() : 0; }

    @Override
    public String toString() {
        return "Produit " + id + " (gamme: " + (gamme != null ? gamme.getRefGamme() : "-") + ")";
    }
}
