package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Model.Operation;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;

import java.util.ArrayList;

public class OperationView {
    private VBox root;
    private final ObservableList<Operation> operations;
    private final Runnable onRetourAccueil;
    private final Atelier atelier;
    private final String nomFichier;

    public OperationView(Atelier atelier, ObservableList<Operation> operations, String nomFichier, Runnable onRetourAccueil) {
        this.atelier = atelier;
        this.operations = operations;
        this.nomFichier = nomFichier;
        this.onRetourAccueil = onRetourAccueil;
        createView();
    }

    private void createView() {
        root = new VBox(18);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-alignment: center;");

        Label titre = new Label("Gestion des Opérations");
        titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Tableau des opérations
        TableView<Operation> tableView = new TableView<>(operations);
        tableView.setPrefHeight(200);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Operation, Number> idCol = new TableColumn<>("Identifiant");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId_operation()));

        TableColumn<Operation, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));

        TableColumn<Operation, Number> dureeCol = new TableColumn<>("Durée (h)");
        dureeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getDureeOperation()));

        tableView.getColumns().addAll(idCol, descCol, dureeCol);

        // Champs d'ajout/modification
        TextField idField = new TextField();
        idField.setPromptText("Identifiant opération (numérique)");
        TextField descField = new TextField();
        descField.setPromptText("Description de l'opération");
        TextField dureeField = new TextField();
        dureeField.setPromptText("Durée (en heures)");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button ajouterBtn = new Button("Ajouter");
        Button supprimerBtn = new Button("Supprimer sélection");
        Button retourBtn = new Button("Retour");

        // Ajout d'une opération
        ajouterBtn.setOnAction(e -> {
            try {
                String idStr = idField.getText().trim();
                String description = descField.getText().trim();
                String dureeStr = dureeField.getText().trim();
                if (idStr.isEmpty() || description.isEmpty() || dureeStr.isEmpty()) {
                    errorLabel.setText("Tous les champs sont obligatoires.");
                    return;
                }
                int idOp = Integer.parseInt(idStr);
                float duree = Float.parseFloat(dureeStr);
                boolean existe = operations.stream().anyMatch(op -> op.getId_operation() == idOp);
                if (existe) {
                    errorLabel.setText("Cet identifiant existe déjà !");
                    return;
                }
                Operation op = new Operation(idOp, description, duree);
                operations.add(op);
                atelier.setOperations(new ArrayList<>(operations));
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                idField.clear();
                descField.clear();
                dureeField.clear();
                errorLabel.setText("");
            } catch (NumberFormatException ex) {
                errorLabel.setText("Identifiant et durée doivent être numériques.");
            } catch (Exception ex) {
                errorLabel.setText("Erreur: " + ex.getMessage());
            }
        });

        // Suppression
        supprimerBtn.setOnAction(e -> {
            Operation selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                operations.remove(selected);
                atelier.setOperations(new ArrayList<>(operations));
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            }
        });

        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        // Organisation
        HBox champsBox = new HBox(10, idField, descField, dureeField, ajouterBtn);
        champsBox.setStyle("-fx-alignment: center;");
        HBox boutonsBox = new HBox(12, supprimerBtn, retourBtn);
        boutonsBox.setStyle("-fx-alignment: center;");

        root.getChildren().addAll(
                titre,
                tableView,
                new Label("Ajout d'une opération :"),
                champsBox,
                boutonsBox,
                errorLabel
        );
    }

    public VBox getView() {
        return root;
    }
}
