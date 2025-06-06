package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.controller.PersonneController;
import com.mycompany.projet_fx.Model.Personne;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class PersonneView {
    private VBox root;
    private final PersonneController controller;

    public PersonneView(PersonneController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");
        Label titre = new Label("Gestion des Personnes");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ListView<Personne> personneList = new ListView<>(controller.getPersonnes());
        personneList.setPrefHeight(120);

        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            String id = idField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();

            if (!id.isEmpty() && !nom.isEmpty() && !prenom.isEmpty()) {
                Personne nouvellePersonne = new Personne(id, nom, prenom);
                controller.ajouterPersonne(nouvellePersonne);

                idField.clear();
                nomField.clear();
                prenomField.clear();
            }
        });

        root.getChildren().addAll(titre, personneList, idField, nomField, prenomField, ajouterBtn);
    }

    public VBox getView() { return root; }
}