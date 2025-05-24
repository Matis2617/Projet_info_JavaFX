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
        idField.setPromptText("ID opération (entier)");

        TextField dureeField = new TextField();
        dureeField.setPromptText("Durée (minutes)");

        TextField refEqField = new TextField();
        refEqField.setPromptText("Référence Équipement");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            try {
                String idOpStr = idField.getText().trim();
                String dureeStr = dureeField.getText().trim();
                String refEq = refEqField.getText().trim();
                if (idOpStr.isEmpty() || dureeStr.isEmpty() || refEq.isEmpty()) {
                    errorLabel.setText("Tous les champs sont obligatoires.");
                    return;
                }
                int idOp = Integer.parseInt(idOpStr);
                float duree = Float.parseFloat(dureeStr);
                Operation op = new Operation(idOp, duree, refEq);
                operations.add(op);
                idField.clear();
                dureeField.clear();
                refEqField.clear();
                errorLabel.setText("");
            } catch (NumberFormatException ex) {
                errorLabel.setText("L'ID et la durée doivent être des nombres valides.");
            } catch (Exception ex) {
                errorLabel.setText("Erreur: " + ex.getMessage());
            }
        });

        Button retourBtn = new Button("Retour");
        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        root.getChildren().addAll(
            titre, opList, idField, dureeField, refEqField, ajouterBtn, retourBtn, errorLabel
        );
    }

    public VBox getView() {
        return root;
    }
}
