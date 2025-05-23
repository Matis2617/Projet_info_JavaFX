package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.controller.GammeController;
import com.mycompany.projet_fx.Model.Gamme;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class GammeView {
    private VBox root;
    private final GammeController controller;

    public GammeView(GammeController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");
        Label titre = new Label("Gestion des Gammes");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ListView<Gamme> gammeList = new ListView<>(controller.getGammes());
        gammeList.setPrefHeight(120);

        TextField refField = new TextField();
        refField.setPromptText("Référence gamme");

        // Ajouter ListView/ComboBox pour sélectionner opérations et équipements

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            // Récupérer sélection, créer gamme, etc.
        });

        root.getChildren().addAll(titre, gammeList, refField, ajouterBtn);
    }

    public VBox getView() { return root; }
}
