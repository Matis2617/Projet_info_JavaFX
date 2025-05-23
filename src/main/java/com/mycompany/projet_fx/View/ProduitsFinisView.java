package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.controller.ProduitsFinisController;
import com.mycompany.projet_fx.Model.Produit;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class ProduitsFinisView {
    private VBox root;
    private final ProduitsFinisController controller;

    public ProduitsFinisView(ProduitsFinisController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");
        Label titre = new Label("Produits Finis");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ListView<Produit> list = new ListView<>(controller.getProduitsFinis());
        list.setPrefHeight(120);

        // ... Ajout de produit fini

        root.getChildren().addAll(titre, list /*, champs d'ajout, bouton ... */);
    }

    public VBox getView() { return root; }
}
