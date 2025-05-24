package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Produit;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class ProduitFormView {

    // Vue d'ajout d'un produit
    public static Node getProduitForm(ObservableList<Produit> listeProduits, Runnable onRetourAccueil) {
        VBox box = new VBox(10);
        box.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField codeField = new TextField();
        codeField.setPromptText("Code Produit");

        TextField idField = new TextField();
        idField.setPromptText("ID Produit");

        Label erreurLabel = new Label();
        erreurLabel.setStyle("-fx-text-fill: red;");

        Button ajouterBtn = new Button("Ajouter le produit");
        ajouterBtn.setOnAction(e -> {
            try {
                int code = Integer.parseInt(codeField.getText());
                String id = idField.getText();

                Produit produit = new Produit(code, id);
                listeProduits.add(produit); // Ajoute à la liste observable
                System.out.println("Produit ajouté : " + produit);
                if (onRetourAccueil != null) onRetourAccueil.run();
            } catch (Exception ex) {
                erreurLabel.setText("Erreur : Données invalides.");
            }
        });

        Button retourBtn = new Button("Annuler");
        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        box.getChildren().addAll(
                new Label("Ajouter un produit :"),
                codeField, idField,
                ajouterBtn, retourBtn, erreurLabel
        );
        return box;
    }

    // Vue d'affichage et gestion de la liste des produits
    public static Node getListeProduitsView(ObservableList<Produit> listeProduits, Runnable onRetourAccueil) {
        VBox box = new VBox(15);
        box.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titre = new Label("Liste des produits finis :");
        ListView<Produit> produitsListView = new ListView<>(listeProduits);
        produitsListView.setPrefHeight(150);

        Label detailsProduit = new Label();
        detailsProduit.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        // Interaction : Affiche infos produit sélectionné
        produitsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                detailsProduit.setText(
                    "Identifiant : " + newVal.getId() + "\n" +
                    "Code produit : " + newVal.getCode()
                );
            } else {
                detailsProduit.setText("");
            }
        });

        // Optionnel : bouton pour supprimer le produit sélectionné
        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setOnAction(e -> {
            Produit prod = produitsListView.getSelectionModel().getSelectedItem();
            if (prod != null) {
                listeProduits.remove(prod);
                detailsProduit.setText("");
            }
        });

        Button retourBtn = new Button("Retour");
        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        HBox actions = new HBox(10, supprimerBtn, retourBtn);
        actions.setStyle("-fx-alignment: center;");

        box.getChildren().addAll(
            titre,
            produitsListView,
            detailsProduit,
            actions
        );
        return box;
    }
}

