package com.mycompany.projet_fx.Model;

import java.io.Serializable;

public class Produit implements Serializable {
    private String id;
    private Gamme gamme; // Gamme utilisée pour fabriquer le produit

    public Produit(String id, Gamme gamme) {
        this.id = id;
        this.gamme = gamme;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Gamme getGamme() { return gamme; }
    public void setGamme(Gamme gamme) { this.gamme = gamme; }

    @Override
    public String toString() {
        return "Produit{" + "id=" + id + ", gamme=" + (gamme != null ? gamme.getRefGamme() : "") + '}';
    }
}
