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

    public static Node getGammeForm(
            Atelier atelier,
            ObservableList<Gamme> gammesList,
            ObservableList<Operation> operationsList,
            Runnable onRetourAccueil
    ) {
        // Layout principal en HBox : Formulaire à gauche, liste à droite
        HBox root = new HBox(25);
        root.setPadding(new Insets(16, 32, 16, 32));

        // ---- Formulaire de création/modif à gauche ----
        VBox vboxForm = new VBox(14);
        vboxForm.setPadding(new Insets(5));
        vboxForm.setPrefWidth(370);

        Label titre = new Label("Créer une gamme");
        titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label opLabel = new Label("Opérations :");
        ListView<Operation> listOp = new ListView<>();
        listOp.setItems(operationsList);
        listOp.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listOp.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Operation op, boolean empty) {
                super.updateItem(op, empty);
                setText((empty || op == null) ? "" : op.toString());
            }
        });

        Label eqLabel = new Label("Équipements :");
        ListView<Equipement> listEq = new ListView<>();
        listEq.getItems().addAll(atelier.getEquipements());
        listEq.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listEq.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Equipement eq, boolean empty) {
                super.updateItem(eq, empty);
                setText((empty || eq == null) ? "" : eq.affiche());
            }
        });

        Label refGammeLabel = new Label("Référence :");
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

        Button retourBtn = new Button("Retour Accueil");
        retourBtn.setOnAction(e -> { if (onRetourAccueil != null) onRetourAccueil.run(); });

        vboxForm.getChildren().addAll(
            titre,
            opLabel, listOp,
            eqLabel, listEq,
            refGammeLabel, refGammeInput,
            creerBtn, retourBtn, creerMsg
        );

        // ---- Liste et détails des gammes à droite ----
        VBox vboxListe = new VBox(16);
        vboxListe.setPadding(new Insets(5));
        vboxListe.setPrefWidth(400);

        Label gammesLbl = new Label("Gammes créées");
        gammesLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<Gamme> gammesView = new ListView<>(gammesList);
        gammesView.setPrefHeight(220);
        gammesView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Gamme g, boolean empty) {
                super.updateItem(g, empty);
                setText((empty || g == null) ? "" : (
                    "Réf: " + g.getRefGamme()
                    + " | Opérations: " + g.getOperations().size()
                    + " | Équipements: " + g.getListeEquipements().size()
                ));
            }
        });

        // Label de détail qui s'affiche instantanément quand on clique sur une gamme
        Label infoGamme = new Label();
        infoGamme.setStyle("-fx-font-size: 13px; -fx-padding: 6; -fx-background-color: #f4f4f4;");
        infoGamme.setMinHeight(120);
        infoGamme.setMaxWidth(390);
        infoGamme.setWrapText(true);

        // Bouton Modifier gamme (popup)
        Button modifierBtn = new Button("Modifier cette gamme");
        modifierBtn.setStyle("-fx-background-color: #ffca3a; -fx-font-weight: bold;");

        // Sélection d'une gamme -> affiche toutes les infos
        gammesView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, g) -> {
            if (g != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Référence : ").append(g.getRefGamme()).append("\n\n");
                sb.append("Opérations :\n");
                for (Operation op : g.getOperations())
                    sb.append(" - ").append(op.toString()).append("\n");
                sb.append("\nÉquipements :\n");
                for (Equipement eq : g.getListeEquipements())
                    sb.append(" - ").append(eq.affiche()).append("\n");
                sb.append("\nCoût total : ").append(g.coutGamme());
                sb.append("\nDurée totale : ").append(g.dureeGamme()).append(" min\n");
                infoGamme.setText(sb.toString());
            } else {
                infoGamme.setText("");
            }
        });

        modifierBtn.setOnAction(e -> {
            Gamme selectedGamme = gammesView.getSelectionModel().getSelectedItem();
            if (selectedGamme == null) return;
            afficherPopupModificationGamme(selectedGamme, atelier, operationsList, gammesList);
        });

        vboxListe.getChildren().addAll(
            gammesLbl, gammesView, infoGamme, modifierBtn
        );

        // Ajoute les deux blocs au root horizontalement
        root.getChildren().addAll(vboxForm, new Separator(javafx.geometry.Orientation.VERTICAL), vboxListe);
        return root;
    }

    private static void afficherPopupModificationGamme(
            Gamme gamme,
            Atelier atelier,
            ObservableList<Operation> operationsList,
            ObservableList<Gamme> gammesList
    ) {
        Dialog<Void> modifDlg = new Dialog<>();
        modifDlg.setTitle("Modifier la gamme");
        VBox modifBox = new VBox(10);
        modifBox.setPadding(new Insets(10));

        TextField refField = new TextField(gamme.getRefGamme());

        ListView<Operation> opEdit = new ListView<>();
        opEdit.getItems().addAll(operationsList);
        opEdit.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        opEdit.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Operation op, boolean empty) {
                super.updateItem(op, empty);
                setText((empty || op == null) ? "" : op.toString());
            }
        });
        for (Operation op : gamme.getOperations())
            opEdit.getSelectionModel().select(op);

        ListView<Equipement> eqEdit = new ListView<>();
        eqEdit.getItems().addAll(atelier.getEquipements());
        eqEdit.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        eqEdit.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Equipement eq, boolean empty) {
                super.updateItem(eq, empty);
                setText((empty || eq == null) ? "" : eq.affiche());
            }
        });
        for (Equipement eq : gamme.getListeEquipements())
            eqEdit.getSelectionModel().select(eq);

        Button valider = new Button("Valider");
        valider.setStyle("-fx-background-color: #8fd14f; -fx-font-weight: bold;");
        Label modifMsg = new Label();
        modifMsg.setStyle("-fx-text-fill: green;");

        valider.setOnAction(ev -> {
            String ref = refField.getText().trim();
            if (ref.isEmpty()) {
                modifMsg.setText("Référence obligatoire !");
                return;
            }
            if (opEdit.getSelectionModel().isEmpty() || eqEdit.getSelectionModel().isEmpty()) {
                modifMsg.setText("Sélectionnez opérations ET équipements !");
                return;
            }
            gamme.setRefGamme(ref);
            gamme.setOperations(new ArrayList<>(opEdit.getSelectionModel().getSelectedItems()));
            gamme.setListeEquipements(new ArrayList<>(eqEdit.getSelectionModel().getSelectedItems()));
            modifDlg.setResult(null);
            modifDlg.close();
        });

        modifBox.getChildren().addAll(
            new Label("Modifier référence :"), refField,
            new Label("Modifier opérations :"), opEdit,
            new Label("Modifier équipements :"), eqEdit,
            valider, modifMsg
        );
        modifDlg.getDialogPane().setContent(modifBox);
        modifDlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        modifDlg.showAndWait();
    }
}
