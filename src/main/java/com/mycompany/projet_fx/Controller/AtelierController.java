package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AtelierController {
    private Atelier atelier;
    private ObservableList<Produit> produitsFinis;
    private ObservableList<Gamme> gammes;
    private ObservableList<Operation> operations;
    private ObservableList<Equipement> equipements;
    private ObservableList<Poste> postes;

    public AtelierController(Atelier atelier) {
        this.atelier = atelier;
        this.produitsFinis = FXCollections.observableArrayList();
        this.gammes = FXCollections.observableArrayList();
        this.operations = FXCollections.observableArrayList();
        this.equipements = FXCollections.observableArrayList();
        this.postes = FXCollections.observableArrayList();
    }

    // Accès aux ObservableList pour la vue (ex : TableView, ListView)
    public ObservableList<Produit> getProduitsFinis() { return produitsFinis; }
    public ObservableList<Gamme> getGammes() { return gammes; }
    public ObservableList<Operation> getOperations() { return operations; }
    public ObservableList<Equipement> getEquipements() { return equipements; }
    public ObservableList<Poste> getPostes() { return postes; }

    // Ajout d'un produit fini
    public void ajouterProduit(Produit produit) {
        produitsFinis.add(produit);
        // Persistance si besoin
    }

    // Suppression d'un produit fini
    public void supprimerProduit(Produit produit) {
        produitsFinis.remove(produit);
    }

    // Ajout d'une gamme
    public void ajouterGamme(Gamme gamme) {
        gammes.add(gamme);
    }

    // ... Ajoute des méthodes similaires pour opérations, équipements, postes, etc.

    // Exemple : Ajout d'opération
    public void ajouterOperation(Operation operation) {
        operations.add(operation);
        atelier.getOperations().add(operation); // Si tu veux garder la synchro avec le modèle
    }

    // Exemple : Ajout d'équipement
    public void ajouterEquipement(Equipement equipement) {
        equipements.add(equipement);
        atelier.getEquipements().add(equipement);
    }

    // TODO : Méthodes de recherche, modification, etc.

    // Accès à l'atelier principal (si besoin)
    public Atelier getAtelier() { return atelier; }
}
