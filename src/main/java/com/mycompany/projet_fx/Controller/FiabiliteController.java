package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.Model.Fiabilite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

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

    public void sauvegarderFiabilites(String fichier) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(new ArrayList<>(fiabilites));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void chargerFiabilites(String fichier) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {
            fiabilites.setAll((ArrayList<Fiabilite>) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
