package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Operation;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
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
        root = new VBox(15);
        root.setStyle("-fx-padding: 30; -fx-alignment: center;");

        Label titre = new Label("Gestion des Opérations");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<Operation> opList = new ListView<>(operations);
        opList.setPrefHeight(150);

        TextField idField = new TextField();
        idField.setPromptText("Identifiant opération (numérique)");

        TextField descField = new TextField();
        descField.setPromptText("Description de l'opération");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            try {
                String idStr = idField.getText().trim();
                String description = descField.getText().trim();

                if (idStr.isEmpty() || description.isEmpty()) {
                    errorLabel.setText("Tous les champs sont obligatoires.");
                    return;
                }
                int idOp = Integer.parseInt(idStr);
                // Vérification unicité (optionnel)
                boolean existe = operations.stream().anyMatch(op -> op.getId_operation() == idOp);
                if (existe) {
                    errorLabel.setText("Cet identifiant existe déjà !");
                    return;
                }
                Operation op = new Operation(idOp, description);
                operations.add(op);
                atelier.setOperations(new ArrayList<>(operations));
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                idField.clear();
                descField.clear();
                errorLabel.setText("");
            } catch (NumberFormatException ex) {
                errorLabel.setText("L'identifiant doit être un nombre entier.");
            } catch (Exception ex) {
                errorLabel.setText("Erreur: " + ex.getMessage());
            }
        });

        Button retourBtn = new Button("Retour");
        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        root.getChildren().addAll(
            titre, opList,
            new Label("Ajout d'une opération :"),
            idField, descField,
            ajouterBtn, retourBtn, errorLabel
        );
    }

    public VBox getView() {
        return root;
    }
}
