package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Model.Gamme;
import com.mycompany.projet_fx.Model.Operation;
import com.mycompany.projet_fx.Model.Equipement;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class GammeFormView {

    public static Node getGammeForm(Atelier atelier, ObservableList<Gamme> gammesList, Runnable onRetourAccueil) {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Label titre = new Label("Gestion des Gammes");
        titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Listes à sélectionner pour composer la gamme
        Label opLabel = new Label("Opérations disponibles :");
        ListView<Operation> listOp = new ListView<>();
        if (atelier.getOperations() != null) {
            listOp.getItems().addAll(atelier.getOperations());
        }
        listOp.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Label eqLabel = new Label("Équipements disponibles :");
        ListView<Equipement> listEq = new ListView<>();
        listEq.getItems().addAll(atelier.getEquipements());
        listEq.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Création d'une gamme
        Label refGammeLabel = new Label("Référence de la gamme :");
        TextField refGammeInput = new TextField();

        Button creerBtn = new Button("Créer la gamme");
        creerBtn.setStyle("-fx-font-weight: bold; -fx-background-color: #8fd14f;");
        Label creerMsg = new Label();
        creerMsg.setStyle("-fx-text-fill: green;");

        creerBtn.setOnAction(e -> {
            String ref = refGammeInput.getText().trim();
            if (ref.isEmpty()) {
                creerMsg.setText("Référence obligatoire !");
                return;
            }
            var selectedOps = listOp.getSelectionModel().getSelectedItems();
            var selectedEqs = listEq.getSelectionModel().getSelectedItems();
            if (selectedOps == null || selectedOps.isEmpty() || selectedEqs == null || selectedEqs.isEmpty()) {
                creerMsg.setText("Sélectionnez opérations ET équipements !");
                return;
            }
            Gamme gamme = new Gamme(new ArrayList<>(selectedOps));
            gamme.setRefGamme(ref);
            gamme.setListeEquipements(new ArrayList<>(selectedEqs));
            gammesList.add(gamme);
            creerMsg.setText("Gamme créée !");
            refGammeInput.clear();
            listOp.getSelectionModel().clearSelection();
            listEq.getSelectionModel().clearSelection();
        });

        // ListView des gammes
        Label gammesLbl = new Label("Gammes créées :");
        ListView<Gamme> gammesView = new ListView<>(gammesList);
        gammesView.setPrefHeight(100);
        gammesView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Gamme g, boolean empty) {
                super.updateItem(g, empty);
                setText(empty || g == null ? "" : g.getRefGamme());
            }
        });

        // Boutons d'action sur la gamme sélectionnée
        HBox actions = new HBox(10);
        Button afficherBtn = new Button("Afficher");
        Button modifierBtn = new Button("Modifier");
        Button coutBtn = new Button("Calculer coût");
        Button dureeBtn = new Button("Calculer durée");
        Button retourBtn = new Button("Retour Accueil");
        actions.getChildren().addAll(afficherBtn, modifierBtn, coutBtn, dureeBtn, retourBtn);

        Label infoGamme = new Label();
        infoGamme.setStyle("-fx-font-size: 13px; -fx-padding: 5;");

        // Actions
        afficherBtn.setOnAction(e -> {
            Gamme g = gammesView.getSelectionModel().getSelectedItem();
            if (g != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Référence : ").append(g.getRefGamme()).append("\n");
                sb.append("Opérations :\n");
                for (Operation op : g.getOperations())
                    sb.append("- ").append(op.getId_operation()).append("\n");
                sb.append("Équipements :\n");
                for (Equipement eq : g.getListeEquipements())
                    sb.append("- ").append(eq.affiche()).append("\n");
                infoGamme.setText(sb.toString());
            }
        });

        modifierBtn.setOnAction(e -> {
            Gamme g = gammesView.getSelectionModel().getSelectedItem();
            if (g == null) return;
            // Ouvre un mini formulaire pour resélectionner ops & eqs
            Dialog<Void> modifDlg = new Dialog<>();
            modifDlg.setTitle("Modifier la gamme");
            VBox modifBox = new VBox(8);
            modifBox.setPadding(new Insets(8));

            ListView<Operation> opEdit = new ListView<>();
            if (atelier.getOperations() != null)
                opEdit.getItems().addAll(atelier.getOperations());
            opEdit.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            for (Operation op : g.getOperations())
                opEdit.getSelectionModel().select(op);

            ListView<Equipement> eqEdit = new ListView<>();
            eqEdit.getItems().addAll(atelier.getEquipements());
            eqEdit.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            for (Equipement eq : g.getListeEquipements())
                eqEdit.getSelectionModel().select(eq);

            modifBox.getChildren().addAll(
                new Label("Modifier opérations :"), opEdit,
                new Label("Modifier équipements :"), eqEdit
            );
            Button valider = new Button("Valider");
            valider.setOnAction(evt -> {
                g.setOperations(new ArrayList<>(opEdit.getSelectionModel().getSelectedItems()));
                g.setListeEquipements(new ArrayList<>(eqEdit.getSelectionModel().getSelectedItems()));
                modifDlg.setResult(null);
                modifDlg.close();
            });
            modifBox.getChildren().add(valider);
            modifDlg.getDialogPane().setContent(modifBox);
            modifDlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            modifDlg.showAndWait();
        });

        coutBtn.setOnAction(e -> {
            Gamme g = gammesView.getSelectionModel().getSelectedItem();
            if (g != null)
                showAlert("Coût gamme", "Coût de la gamme : " + g.coutGamme());
        });

        dureeBtn.setOnAction(e -> {
            Gamme g = gammesView.getSelectionModel().getSelectedItem();
            if (g != null)
                showAlert("Durée gamme", "Durée de la gamme : " + g.dureeGamme());
        });

        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        vbox.getChildren().addAll(
            titre,
            opLabel, listOp,
            eqLabel, listEq,
            refGammeLabel, refGammeInput, creerBtn, creerMsg,
            new Separator(),
            gammesLbl, gammesView, actions, infoGamme
        );
        return vbox;
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
