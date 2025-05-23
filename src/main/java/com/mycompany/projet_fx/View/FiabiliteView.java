package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.controller.FiabiliteController;
import com.mycompany.projet_fx.Model.Fiabilite;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class FiabiliteView {
    private VBox root;
    private final FiabiliteController controller;

    public FiabiliteView(FiabiliteController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");
        Label titre = new Label("Gestion de la Fiabilité");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ListView<Fiabilite> fiabList = new ListView<>(controller.getFiabilites());
        fiabList.setPrefHeight(120);

        // ... Champs pour ajouter une fiabilité

        root.getChildren().addAll(titre, fiabList /*, champs d'ajout, bouton ... */);
    }

    public VBox getView() { return root; }
}
