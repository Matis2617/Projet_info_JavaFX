package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.Model.Personne;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonneController {
    private ObservableList<Personne> personnes = FXCollections.observableArrayList();

    public ObservableList<Personne> getPersonnes() {
        return personnes;
    }

    public void ajouterPersonne(Personne personne) {
        personnes.add(personne);
    }

    public void supprimerPersonne(Personne personne) {
        personnes.remove(personne);
    }
}
