package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.model.Fiabilite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FiabiliteController {
    private ObservableList<Fiabilite> fiabilites = FXCollections.observableArrayList();

    public ObservableList<Fiabilite> getFiabilites() {
        return fiabilites;
    }

    public void ajouterFiabilite(Fiabilite fiabilite) {
        fiabilites.add(fiabilite);
    }

    public void supprimerFiabilite(Fiabilite fiabilite) {
        fiabilites.remove(fiabilite);
    }
}
