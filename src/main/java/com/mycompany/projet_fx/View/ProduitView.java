package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.controller.ProduitController;
import com.mycompany.projet_fx.Model.Produit;
import com.mycompany.projet_fx.Model.Gamme;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class ProduitView {
    private VBox root;
    private final ProduitController controller;

    public ProduitView(ProduitController controller, ObservableList<Gamme> gammesList) {
        this.controller = controller;
        createView(gammesList);
    }

    private void createView(ObservableList<Gamme> gammesList) {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");
        Label titre = new Label("Gestion des Produits");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ListView<Produit> list = new ListView<>(controller.getProduits());
        list.setPrefHeight(120);

        TextField idField = new TextField();
        idField.setPromptText("ID produit");

        ComboBox<Gamme> gammeCombo = new ComboBox<>(gammesList);
        gammeCombo.setPromptText("Sélectionnez la gamme");

        Button ajouterBtn = new Button("Ajouter");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        ajouterBtn.setOnAction(e -> {
            try {
                String id = idField.getText().trim();
                Gamme selectedGamme = gammeCombo.getValue();
                if (id.isEmpty() || selectedGamme == null) {
                    errorLabel.setText("ID produit et gamme obligatoires !");
                    return;
                }
                controller.ajouterProduit(new Produit(id, selectedGamme));
                idField.clear();
                gammeCombo.getSelectionModel().clearSelection();
                errorLabel.setText("");
            } catch (Exception ex) {
                errorLabel.setText("Erreur lors de l'ajout du produit.");
            }
        });

        root.getChildren().addAll(
            titre, list,
            new Label("Ajout d'un produit :"),
            idField, gammeCombo,
            ajouterBtn, errorLabel
        );
    }

    public VBox getView() {
        return root;
    }
}
