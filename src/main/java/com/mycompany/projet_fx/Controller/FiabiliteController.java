package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.Model.Fiabilite;
import com.mycompany.projet_fx.Model.Machine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class FiabiliteController {
    private ObservableList<Fiabilite> fiabilites;

    public FiabiliteController() {
        this.fiabilites = FXCollections.observableArrayList();
    }

    public FiabiliteController(List<Fiabilite> fiabilitesSource) {
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

    public void mettreAJourFiabilite(Fiabilite fiabilite) {
        // Logique pour mettre à jour un événement de fiabilité
        // Par exemple, si vous avez besoin de mettre à jour un événement existant
    }

    public void enregistrerPanne(Machine machine, String cause) {
        Fiabilite fiabilite = new Fiabilite(machine, java.time.LocalDateTime.now(), null, cause);
        ajouterFiabilite(fiabilite);
    }

    public void enregistrerReprise(Fiabilite fiabilite) {
        fiabilite.enregistrerReprise(java.time.LocalDateTime.now());
    }
}