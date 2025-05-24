package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.controller.ProduitController;
import com.mycompany.projet_fx.Model.Produit;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class ProduitView {
    private VBox root;
    private final ProduitController controller;

    public ProduitView(ProduitController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");
        Label titre = new Label("Gestion des Produits");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ListView<Produit> list = new ListView<>(controller.getProduits());
        list.setPrefHeight(120);

        TextField codeField = new TextField();
        codeField.setPromptText("Code produit");
        TextField idField = new TextField();
        idField.setPromptText("ID produit");

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            try {
                String id = idField.getText().trim();
                controller.ajouterProduit(new Produit(id)); 
                codeField.clear(); idField.clear();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        root.getChildren().addAll(titre, list, codeField, idField, ajouterBtn);
    }

    public VBox getView() {
        return root;
    }
}
