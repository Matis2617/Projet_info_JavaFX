package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.Model.Produit;
import com.mycompany.projet_fx.controller.StockBrutController;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class StockBrutView extends VBox {
    private StockBrutController controller;
    private ListView<Produit> listViewProduits;

    public StockBrutView(StockBrutController controller) {
        this.controller = controller;

        // Initialisation de la ListView pour afficher les produits
        listViewProduits = new ListView<>(controller.getProduitsBruts());

        // Boutons pour ajouter et supprimer des produits
        Button btnAjouter = new Button("Ajouter Produit");
        Button btnSupprimer = new Button("Supprimer Produit");

        // Gestion des événements pour les boutons
        btnAjouter.setOnAction(event -> {
            // Logique pour ajouter un produit
            Produit nouveauProduit = new Produit(10,"Nouveau Produit"); // Exemple
            controller.ajouterProduitBrut(nouveauProduit);
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
        });

        btnSupprimer.setOnAction(event -> {
            // Logique pour supprimer le produit sélectionné
            Produit produitSelectionne = listViewProduits.getSelectionModel().getSelectedItem();
            if (produitSelectionne != null) {
                controller.supprimerProduitBrut(produitSelectionne);
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            }
        });

        // Ajout des composants à la vue
        this.getChildren().addAll(listViewProduits, btnAjouter, btnSupprimer);
    }
}
