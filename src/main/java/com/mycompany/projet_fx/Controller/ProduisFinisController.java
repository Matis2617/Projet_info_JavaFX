package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.model.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProduitsFinisController {
    private ObservableList<Produit> produitsFinis = FXCollections.observableArrayList();

    public ObservableList<Produit> getProduitsFinis() {
        return produitsFinis;
    }

    public void ajouterProduitFini(Produit produit) {
        produitsFinis.add(produit);
    }

    public void supprimerProduitFini(Produit produit) {
        produitsFinis.remove(produit);
    }
}
