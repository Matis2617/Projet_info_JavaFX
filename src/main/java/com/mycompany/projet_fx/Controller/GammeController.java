package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.Model.Gamme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GammeController {
    private ObservableList<Gamme> gammes = FXCollections.observableArrayList();

    public ObservableList<Gamme> getGammes() {
        return gammes;
    }

    public void ajouterGamme(Gamme gamme) {
        gammes.add(gamme);
    }

    public void supprimerGamme(Gamme gamme) {
        gammes.remove(gamme);
    }
}
