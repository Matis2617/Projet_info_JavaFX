package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.controller.PosteController;
import com.mycompany.projet_fx.model.Poste;
import com.mycompany.projet_fx.model.Machine;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class PosteView {
    private VBox root;
    private final PosteController controller;

    public PosteView(PosteController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");
        Label titre = new Label("Gestion des Postes");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<Poste> posteList = new ListView<>(controller.getPostes());
        posteList.setPrefHeight(120);

        TextField descField = new TextField();
        descField.setPromptText("Description poste");
        // Ajouter une ListView<Machine> pour sélectionner les machines liées

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            // Récupérer machines sélectionnées, créer poste, etc.
        });

        root.getChildren().addAll(titre, posteList, descField, ajouterBtn);
    }

    public VBox getView() { return root; }
}
