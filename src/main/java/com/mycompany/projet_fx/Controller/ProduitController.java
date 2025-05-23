package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.Model.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProduitController {
    private ObservableList<Produit> produits = FXCollections.observableArrayList();

    public ObservableList<Produit> getProduits() {
        return produits;
    }

    public void ajouterProduit(Produit produit) {
        produits.add(produit);
    }

    public void supprimerProduit(Produit produit) {
        produits.remove(produit);
    }
}
