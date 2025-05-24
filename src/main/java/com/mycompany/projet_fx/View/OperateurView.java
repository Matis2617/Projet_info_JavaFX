/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx.View;

/**
 *
 * @author Oscar
 */
import com.mycompany.projet_fx.Controller.OperateurController;
import com.mycompany.projet_fx.Model.Operateur;
import com.mycompany.projet_fx.Model.Machine.ETAT;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class OperateurView {
    private VBox root;
    private final OperateurController controller;

    public OperateurView(OperateurController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");

        Label titre = new Label("Gestion des Opérateurs");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<Operateur> operateurList = new ListView<>(controller.getOperateurs());
        operateurList.setPrefHeight(120);

        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");

        TextField competencesField = new TextField();
        competencesField.setPromptText("Compétences");

        TextField idOpField = new TextField();
        idOpField.setPromptText("ID Opérateur");

        ComboBox<ETAT> etatBox = new ComboBox<>();
        etatBox.getItems().addAll(ETAT.values());
        etatBox.setPromptText("État");

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            String id = idField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String competences = competencesField.getText();
            int idOp = Integer.parseInt(idOpField.getText());
            ETAT etat = etatBox.getValue();

            if (!id.isEmpty() && !nom.isEmpty() && !prenom.isEmpty() && !competences.isEmpty() && idOp > 0 && etat != null) {
                Operateur nouvelOperateur = new Operateur(id, nom, prenom, competences, idOp, etat);
                controller.ajouterOperateur(nouvelOperateur);

                idField.clear();
                nomField.clear();
                prenomField.clear();
                competencesField.clear();
                idOpField.clear();
                etatBox.getSelectionModel().clearSelection();
            }
        });

        root.getChildren().addAll(titre, operateurList, idField, nomField, prenomField, competencesField, idOpField, etatBox, ajouterBtn);
    }

    public VBox getView() {
        return root;
    }
}

