package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Produit;
import com.mycompany.projet_fx.Model.Gamme;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class ProduitFormView {

    public static Node getProduitForm(
            ObservableList<Produit> listeProduits,
            ObservableList<Gamme> listeGammes,
            Runnable onRetourAccueil
    ) {
        VBox box = new VBox(18);
        box.setPadding(new Insets(28));
        box.setStyle("-fx-alignment: center;");

        Label titre = new Label("Ajouter un produit fini");
        titre.setStyle("-fx-font-size: 19px; -fx-font-weight: bold;");

        TextField idField = new TextField();
        idField.setPromptText("Identifiant du produit");

        ComboBox<Gamme> gammeCombo = new ComboBox<>(listeGammes);
        gammeCombo.setPromptText("Choisir une gamme utilisée");

        Label erreurLabel = new Label();
        erreurLabel.setStyle("-fx-text-fill: red;");

        Button ajouterBtn = new Button("Ajouter le produit");
        ajouterBtn.setOnAction(e -> {
            try {
                String id = idField.getText().trim();
                Gamme gamme = gammeCombo.getValue();
                if (id.isEmpty() || gamme == null) {
                    erreurLabel.setText("Remplir tous les champs.");
                    return;
                }
                // Vérifier unicité
                boolean existe = listeProduits.stream().anyMatch(p -> p.getId().equals(id));
                if (existe) {
                    erreurLabel.setText("Identifiant déjà utilisé !");
                    return;
                }
                Produit produit = new Produit(id, gamme);
                listeProduits.add(produit);
                idField.clear();
                gammeCombo.setValue(null);
                erreurLabel.setText("");
            } catch (Exception ex) {
                erreurLabel.setText("Erreur : Données invalides.");
            }
        });

        Button retourBtn = new Button("Annuler");
        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        // Tableau des produits finis
        TableView<Produit> tableProduits = new TableView<>(listeProduits);
        tableProduits.setPrefHeight(220);
        tableProduits.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Produit, String> idCol = new TableColumn<>("Identifiant");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));

        TableColumn<Produit, String> gammeCol = new TableColumn<>("Gamme utilisée");
        gammeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getGamme() == null ? "" : data.getValue().getGamme().getRefGamme()
        ));

        TableColumn<Produit, Number> coutCol = new TableColumn<>("Coût total");
        coutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(
                data.getValue().getGamme() == null ? 0 : data.getValue().getGamme().coutGamme()
        ));

        TableColumn<Produit, Number> dureeCol = new TableColumn<>("Durée totale (h)");
        dureeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(
                data.getValue().getGamme() == null ? 0 : data.getValue().getGamme().dureeGamme()
        ));

        tableProduits.getColumns().addAll(idCol, gammeCol, coutCol, dureeCol);

        // Suppression
        Button supprimerBtn = new Button("Supprimer sélection");
        supprimerBtn.setOnAction(e -> {
            Produit sel = tableProduits.getSelectionModel().getSelectedItem();
            if (sel != null) {
                listeProduits.remove(sel);
                tableProduits.getSelectionModel().clearSelection();
            }
        });

        HBox actions = new HBox(12, ajouterBtn, supprimerBtn, retourBtn);
        actions.setStyle("-fx-alignment: center;");

        box.getChildren().addAll(
                titre,
                idField,
                gammeCombo,
                erreurLabel,
                tableProduits,
                actions
        );

        return box;
    }
}
