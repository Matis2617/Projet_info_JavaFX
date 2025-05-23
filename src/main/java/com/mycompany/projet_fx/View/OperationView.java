package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.controller.OperationController;
import com.mycompany.projet_fx.Model.Operation;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class OperationView {
    private VBox root;
    private final OperationController controller;

    public OperationView(OperationController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");
        Label titre = new Label("Gestion des Opérations");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ListView<Operation> opList = new ListView<>(controller.getOperations());
        opList.setPrefHeight(120);

        TextField idField = new TextField();
        idField.setPromptText("ID opération");
        TextField dureeField = new TextField();
        dureeField.setPromptText("Durée");

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            // Créer l’opération avec les champs saisis
        });

        root.getChildren().addAll(titre, opList, idField, dureeField, ajouterBtn);
    }

    public VBox getView() { return root; }
}
