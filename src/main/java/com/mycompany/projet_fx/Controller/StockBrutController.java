package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.Model.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StockBrutController {
    private ObservableList<Produit> produitsBruts = FXCollections.observableArrayList();

    public ObservableList<Produit> getProduitsBruts() {
        return produitsBruts;
    }

    public void ajouterProduitBrut(Produit produit) {
        produitsBruts.add(produit);
    }

    public void supprimerProduitBrut(Produit produit) {
        produitsBruts.remove(produit);
    }
}
