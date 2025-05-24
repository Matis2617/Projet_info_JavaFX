package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.*;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class ProduitFormView {

    public static Node getProduitForm(ObservableList<Produit> listeProduits, ObservableList<Gamme> gammesList, Runnable onRetourAccueil) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));

        // ---- Tableau permanent des produits finis ----
        TableView<Produit> tableView = new TableView<>(listeProduits);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(220);

        TableColumn<Produit, String> idCol = new TableColumn<>("ID Produit");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        TableColumn<Produit, String> gammeCol = new TableColumn<>("Gamme utilisée");
        gammeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getGamme() != null ? data.getValue().getGamme().getRefGamme() : "-"
        ));
        tableView.getColumns().addAll(idCol, gammeCol);

        // ---- Détail du produit sélectionné ----
        Label detailLabel = new Label("Sélectionnez un produit pour voir les détails.");
        detailLabel.setMinHeight(110);
        detailLabel.setStyle("-fx-background-color: #f5f5f8; -fx-border-color: #aaa; -fx-padding: 14; -fx-font-size: 15px;");
        detailLabel.setWrapText(true);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, prod) -> {
            if (prod != null && prod.getGamme() != null) {
                Gamme g = prod.getGamme();
                StringBuilder sb = new StringBuilder();
                sb.append("🆔 <b>ID Produit</b> : ").append(prod.getId()).append("\n\n");
                sb.append("<b>Gamme :</b> ").append(g.getRefGamme()).append("\n\n");
                sb.append("<b>Opérations :</b>\n");
                for (Operation op : g.getOperations()) {
                    sb.append("   • ").append(op.getDescription())
                      .append(" (Durée : ").append(op.getDuree()).append(" h)").append("\n");
                }
                sb.append("\n<b>Machines utilisées :</b>\n");
                for (Equipement eq : g.getListeEquipements()) {
                    if (eq instanceof Machine) {
                        Machine m = (Machine) eq;
                        sb.append("   • ").append(m.getDmachine())
                          .append(" (Coût horaire : ").append(m.getC()).append(" €)").append("\n");
                    } else {
                        sb.append("   • ").append(eq.affiche()).append("\n");
                    }
                }
                sb.append("\n<b>Coût total :</b> ").append(String.format("%.2f", g.coutGamme())).append(" €");
                sb.append("\n<b>Durée totale :</b> ").append(String.format("%.2f", g.dureeGamme())).append(" h");
                detailLabel.setText(sb.toString().replaceAll("<b>", "").replaceAll("</b>", "")); // JavaFX Label ne gère pas le bold sans HTML
            } else {
                detailLabel.setText("Sélectionnez un produit pour voir les détails.");
            }
        });

        // ---- Formulaire d'ajout ----
        VBox formBox = new VBox(14);
        formBox.setPadding(new Insets(10, 20, 20, 20));
        formBox.setStyle("-fx-background-color: #f8fbff; -fx-border-color: #c1c6d0; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label formTitle = new Label("Créer un produit fini");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField idField = new TextField();
        idField.setPromptText("Identifiant du produit");

        ComboBox<Gamme> gammeCombo = new ComboBox<>(gammesList);
        gammeCombo.setPromptText("Choisir la gamme");
        gammeCombo.setMaxWidth(Double.MAX_VALUE);

        Label gammeDetailLabel = new Label("Sélectionnez une gamme pour voir ses détails.");
        gammeDetailLabel.setWrapText(true);
        gammeDetailLabel.setMinHeight(90);
        gammeDetailLabel.setStyle("-fx-font-size: 13px; -fx-background-color: #f2f5fc; -fx-padding: 7;");

        gammeCombo.valueProperty().addListener((obs, oldVal, g) -> {
            if (g != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("🆔 <b>ID Gamme</b>: ").append(g.getRefGamme()).append("\n");
                sb.append("<b>Opérations :</b>\n");
                for (Operation op : g.getOperations()) {
                    sb.append("   • ").append(op.getDescription())
                      .append(" (").append(op.getDuree()).append(" h)").append("\n");
                }
                sb.append("<b>Machines :</b>\n");
                for (Equipement eq : g.getListeEquipements()) {
                    if (eq instanceof Machine) {
                        Machine m = (Machine) eq;
                        sb.append("   • ").append(m.getDmachine())
                          .append(" (").append(m.getC()).append(" €/h)").append("\n");
                    } else {
                        sb.append("   • ").append(eq.affiche()).append("\n");
                    }
                }
                sb.append("\n<b>Coût gamme :</b> ").append(String.format("%.2f", g.coutGamme())).append(" €");
                sb.append("   <b>Durée :</b> ").append(String.format("%.2f", g.dureeGamme())).append(" h");
                gammeDetailLabel.setText(sb.toString().replaceAll("<b>", "").replaceAll("</b>", ""));
            } else {
                gammeDetailLabel.setText("Sélectionnez une gamme pour voir ses détails.");
            }
        });

        Label erreurLabel = new Label();
        erreurLabel.setStyle("-fx-text-fill: red;");

        Button ajouterBtn = new Button("Ajouter le produit fini");
        ajouterBtn.setStyle("-fx-background-color: #92e079; -fx-font-weight: bold; -fx-font-size: 14px;");
        ajouterBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            Gamme g = gammeCombo.getValue();
            if (id.isEmpty() || g == null) {
                erreurLabel.setText("Remplissez l'identifiant et sélectionnez une gamme !");
                return;
            }
            // Vérifie unicité de l'ID produit
            for (Produit p : listeProduits) {
                if (p.getId().equalsIgnoreCase(id)) {
                    erreurLabel.setText("ID déjà utilisé !");
                    return;
                }
            }
            Produit p = new Produit(id, g);
            listeProduits.add(p);
            idField.clear();
            gammeCombo.setValue(null);
            erreurLabel.setText("");
        });

        Button retourBtn = new Button("Retour");
        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        formBox.getChildren().addAll(formTitle, idField, gammeCombo, gammeDetailLabel, ajouterBtn, retourBtn, erreurLabel);

        // ---- Layout principal ----
        VBox rightBox = new VBox(18, new Label("Détails du produit sélectionné :"), detailLabel);
        rightBox.setPadding(new Insets(12, 0, 0, 18));

        VBox leftBox = new VBox(16, new Label("Produits finis créés :"), tableView, formBox);
        leftBox.setPrefWidth(420);

        HBox hBox = new HBox(24, leftBox, rightBox);
        hBox.setPrefHeight(400);
        hBox.setStyle("-fx-alignment: stretch;");

        root.setCenter(hBox);

        return root;
    }
}
