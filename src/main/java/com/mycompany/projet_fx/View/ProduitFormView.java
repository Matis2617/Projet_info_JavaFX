package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Produit;
import com.mycompany.projet_fx.Model.Gamme;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class ProduitFormView {

    // Vue d'ajout d'un produit
   public static Node getProduitForm(ObservableList<Produit> listeProduits, ObservableList<Gamme> gammesList, Runnable onRetourAccueil) {
    VBox box = new VBox(10);
    box.setStyle("-fx-padding: 20; -fx-alignment: center;");

    TextField idField = new TextField();
    idField.setPromptText("ID Produit");

    ComboBox<Gamme> gammeCombo = new ComboBox<>(gammesList);
    gammeCombo.setPromptText("Sélectionnez la gamme");

    Label erreurLabel = new Label();
    erreurLabel.setStyle("-fx-text-fill: red;");

    Button ajouterBtn = new Button("Ajouter le produit");
    ajouterBtn.setOnAction(e -> {
        try {
            String id = idField.getText().trim();
            Gamme selectedGamme = gammeCombo.getValue();
            if (id.isEmpty() || selectedGamme == null) {
                erreurLabel.setText("ID produit et gamme obligatoires !");
                return;
            }
            Produit produit = new Produit(id, selectedGamme);
            listeProduits.add(produit);
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
            idField, gammeCombo,
            ajouterBtn, retourBtn, erreurLabel
    );
    return box;
}

    // Vue d'affichage et gestion de la liste des produits finis
    public static Node getListeProduitsView(ObservableList<Produit> listeProduits, Runnable onRetourAccueil) {
        VBox box = new VBox(18);
        box.setStyle("-fx-padding: 30; -fx-alignment: center;");

        Label titre = new Label("Liste des produits finis :");
        TableView<Produit> produitsTable = new TableView<>(listeProduits);
        produitsTable.setPrefHeight(200);
        produitsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Produit, String> idCol = new TableColumn<>("Identifiant");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));

        TableColumn<Produit, String> gammeCol = new TableColumn<>("Gamme utilisée");
        gammeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getGamme() != null ? data.getValue().getGamme().getRefGamme() : "-"
        ));

        TableColumn<Produit, Number> prixCol = new TableColumn<>("Coût (prix)");
        prixCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(
                data.getValue().getPrix()
        ));

        TableColumn<Produit, Number> dureeCol = new TableColumn<>("Durée de production (h)");
        dureeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(
                data.getValue().getDuree()
        ));

        produitsTable.getColumns().addAll(idCol, gammeCol, prixCol, dureeCol);

        Label detailsProduit = new Label();
        detailsProduit.setStyle("-fx-font-size: 15px; -fx-padding: 12;");

        produitsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Identifiant : ").append(newVal.getId()).append("\n");
                if (newVal.getGamme() != null) {
                    sb.append("Gamme : ").append(newVal.getGamme().getRefGamme()).append("\n");
                    sb.append("Détail gamme :\n");
                    for (int i = 0; i < newVal.getGamme().getOperations().size(); i++) {
                        sb.append(" - Opération ").append(i+1).append(" : ")
                                .append(newVal.getGamme().getOperations().get(i).getDescription()).append("\n");
                    }
                    sb.append("Coût total : ").append(newVal.getPrix()).append(" €\n");
                    sb.append("Durée totale : ").append(newVal.getDuree()).append(" h");
                }
                detailsProduit.setText(sb.toString());
            } else {
                detailsProduit.setText("");
            }
        });

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setOnAction(e -> {
            Produit prod = produitsTable.getSelectionModel().getSelectedItem();
            if (prod != null) {
                listeProduits.remove(prod);
                detailsProduit.setText("");
            }
        });

        Button retourBtn = new Button("Retour");
        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        HBox actions = new HBox(12, supprimerBtn, retourBtn);
        actions.setStyle("-fx-alignment: center;");

        box.getChildren().addAll(
            titre,
            produitsTable,
            detailsProduit,
            actions
        );
        return box;
    }
}
