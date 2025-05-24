package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.Model.EvenementMaintenance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementMaintenanceController {

    private ObservableList<EvenementMaintenance> evenements = FXCollections.observableArrayList();

    public ObservableList<EvenementMaintenance> getEvenements() {
        return evenements;
    }

    public void ajouterEvenement(EvenementMaintenance evt) {
        evenements.add(evt);
    }

    public void supprimerEvenement(EvenementMaintenance evt) {
        evenements.remove(evt);
    }

    // Option : Sauvegarde/chargement (binaire)
    public void sauvegarder(String chemin) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chemin))) {
            oos.writeObject(new ArrayList<>(evenements));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void charger(String chemin) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chemin))) {
            evenements.setAll((ArrayList<EvenementMaintenance>) ois.readObject());
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
}
