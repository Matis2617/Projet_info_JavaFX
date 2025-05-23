package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.model.Atelier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AtelierController {
    private Atelier atelier;

    public AtelierController(Atelier atelier) {
        this.atelier = atelier;
    }

    public Atelier getAtelier() {
        return atelier;
    }

    // Si besoin : méthodes globales, coordination/navigation entre modules
}
