/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx.view;

/**
 *
 * @author Oscar
 */
import com.mycompany.projet_fx.controller.OperateurController;
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
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String competences = competencesField.getText();
            int idOp = Integer.parseInt(idOpField.getText());
            ETAT etat = etatBox.getValue();

            if (!nom.isEmpty() && !prenom.isEmpty() && !competences.isEmpty() && idOp > 0 && etat != null) {
                Operateur nouvelOperateur = new Operateur(nom, prenom, competences, idOp, etat);
                controller.ajouterOperateur(nouvelOperateur);

                nomField.clear();
                prenomField.clear();
                competencesField.clear();
                idOpField.clear();
                etatBox.getSelectionModel().clearSelection();
            }
        });
        
        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setOnAction(e -> {
            Operateur operateurSelectionne = operateurList.getSelectionModel().getSelectedItem();
            if (operateurSelectionne != null) {
                controller.supprimerOperateur(operateurSelectionne);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune sélection");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner un opérateur à supprimer.");
                alert.showAndWait();
            }
        });
        
        ComboBox<ETAT> modifierEtatBox = new ComboBox<>();
        modifierEtatBox.getItems().addAll(ETAT.values());
        modifierEtatBox.setPromptText("Nouvel État");
        
        Button modifierEtatBtn = new Button("Modifier État");
        modifierEtatBtn.setOnAction(e -> {
            Operateur operateurSelectionne = operateurList.getSelectionModel().getSelectedItem();
            ETAT nouvelEtat = modifierEtatBox.getValue();

            if (operateurSelectionne != null && nouvelEtat != null) {
                controller.setEtat(nouvelEtat, operateurSelectionne);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune sélection");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner un opérateur et un nouvel état.");
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(titre, operateurList, nomField, prenomField, competencesField,
                idOpField, etatBox, ajouterBtn, supprimerBtn, modifierEtatBox);
        
    }

    public VBox getView() {
        return root;
    }
}