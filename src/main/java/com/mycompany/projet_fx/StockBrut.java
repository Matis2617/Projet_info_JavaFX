package com.mycompany.projet_fx.model;

import java.io.Serializable;
import java.util.ArrayList;

public class StockBrut implements Serializable {
    private ArrayList<Produit> produits;

    public StockBrut() {
        this.produits = new ArrayList<>();
    }

    public void ajouterProduit(Produit produit) {
        produits.add(produit);
    }

    public void supprimerProduit(Produit produit) {
        produits.remove(produit);
    }

    public int getNombreProduits() {
        return produits.size();
    }

    public void afficherStock() {
        for (Produit produit : produits) {
            System.out.println(produit);
        }
    }

    // (optionnel) un getter pour la liste (utile pour la vue)
    public ArrayList<Produit> getProduits() {
        return produits;
    }
}
