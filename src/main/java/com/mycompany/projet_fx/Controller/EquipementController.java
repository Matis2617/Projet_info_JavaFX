package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.model.Equipement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EquipementController {
    private ObservableList<Equipement> equipements = FXCollections.observableArrayList();

    public ObservableList<Equipement> getEquipements() {
        return equipements;
    }

    public void ajouterEquipement(Equipement equipement) {
        equipements.add(equipement);
    }

    public void supprimerEquipement(Equipement equipement) {
        equipements.remove(equipement);
    }
}
