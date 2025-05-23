package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.model.Poste;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PosteController {
    private ObservableList<Poste> postes = FXCollections.observableArrayList();

    public ObservableList<Poste> getPostes() {
        return postes;
    }

    public void ajouterPoste(Poste poste) {
        postes.add(poste);
    }

    public void supprimerPoste(Poste poste) {
        postes.remove(poste);
    }
}
