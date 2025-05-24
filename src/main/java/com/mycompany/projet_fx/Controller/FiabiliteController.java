package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.Model.Fiabilite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class FiabiliteController {
    private ObservableList<Fiabilite> fiabilites;

    public FiabiliteController(List<Fiabilite> fiabilitesSource) {
        // Prend la liste depuis l'atelier
        this.fiabilites = FXCollections.observableArrayList(fiabilitesSource);
    }

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
