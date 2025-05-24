package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Operation;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class OperationView {
    private VBox root;
    private final ObservableList<Operation> operations;
    private final Runnable onRetourAccueil;

    public OperationView(ObservableList<Operation> operations, Runnable onRetourAccueil) {
        this.operations = operations;
        this.onRetourAccueil = onRetourAccueil;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30; -fx-alignment: center;");

        Label titre = new Label("Gestion des Opérations");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<Operation> opList = new ListView<>(operations);
        opList.setPrefHeight(150);

        TextField idField = new TextField();
        idField.setPromptText("ID opération");

        TextField dureeField = new TextField();
        dureeField.setPromptText("Durée");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            try {
                String idOp = idField.getText().trim();
                String dureeStr = dureeField.getText().trim();
                if (idOp.isEmpty() || dureeStr.isEmpty()) {
                    errorLabel.setText("Tous les champs sont obligatoires.");
                    return;
                }
                double duree = Double.parseDouble(dureeStr);
                Operation op = new Operation(idOp, duree, refEq); // Adapte selon ton constructeur
                operations.add(op);
                idField.clear();
                dureeField.clear();
                errorLabel.setText("");
            } catch (NumberFormatException ex) {
                errorLabel.setText("La durée doit être un nombre.");
            } catch (Exception ex) {
                errorLabel.setText("Erreur: " + ex.getMessage());
            }
        });

        Button retourBtn = new Button("Retour");
        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        root.getChildren().addAll(titre, opList, idField, dureeField, ajouterBtn, retourBtn, errorLabel);
    }

    public VBox getView() {
        return root;
    }
}
