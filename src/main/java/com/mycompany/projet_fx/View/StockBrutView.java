package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.controller.StockBrutController;
import com.mycompany.projet_fx.Model.Produit;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class StockBrutView {
    private VBox root;
    private final StockBrutController controller;

    public StockBrutView(StockBrutController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");
        Label titre = new Label("Stock Brut");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ListView<Produit> list = new ListView<>(controller.getProduitsBruts());
        list.setPrefHeight(120);

        // ... Ajout de produit brut

        root.getChildren().addAll(titre, list /*, champs d'ajout, bouton ... */);
    }

    public VBox getView() { return root; }
}
