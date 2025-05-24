package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.*;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class ProduitFormView {

    // Vue principale avec création et tableau permanent
    public static Node getProduitForm(ObservableList<Produit> listeProduits, ObservableList<Gamme> gammesList, Runnable onRetourAccueil) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));

        // ------- Formulaire à gauche -------
        VBox leftBox = new VBox(18);
        leftBox.setPadding(new Insets(12, 22, 12, 0));

        Label titre = new Label("Créer un produit fini");
        titre.setStyle("-fx-font-size: 19px; -fx-font-weight: bold;");

        TextField idField = new TextField();
        idField.setPromptText("Identifiant du produit");

        // ComboBox pour choisir la gamme, affichage détaillé
        ComboBox<Gamme> gammeCombo = new ComboBox<>(gammesList);
        gammeCombo.setPromptText("Choisir la gamme utilisée");
        gammeCombo.setPrefWidth(480);

        gammeCombo.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Gamme gamme, boolean empty) {
                super.updateItem(gamme, empty);
                if (empty || gamme == null) {
                    setText(null);
                } else {
                    setText(formatGammeDetail(gamme));
                }
            }
        });
        gammeCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Gamme gamme, boolean empty) {
                super.updateItem(gamme, empty);
                setText(empty || gamme == null ? null : formatGammeDetail(gamme));
            }
        });

        Button ajouterBtn = new Button("Ajouter le produit");
        Label msg = new Label();
        msg.setStyle("-fx-text-fill: green;");

        ajouterBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            Gamme gamme = gammeCombo.getValue();
            if (id.isEmpty() || gamme == null) {
                msg.setText("Remplis tous les champs !");
                return;
            }
            // Vérifie unicité (optionnel)
            boolean existe = listeProduits.stream().anyMatch(p -> p.getId().equals(id));
            if (existe) {
                msg.setText("Cet identifiant de produit existe déjà !");
                return;
            }
            Produit produit = new Produit(id, gamme);
            listeProduits.add(produit);
            msg.setText("Produit ajouté !");
            idField.clear();
            gammeCombo.getSelectionModel().clearSelection();
        });

        Button retourBtn = new Button("Retour");
        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        leftBox.getChildren().addAll(
                titre,
                idField,
                new Label("Choisir la gamme de fabrication :"),
                gammeCombo,
                ajouterBtn, retourBtn, msg
        );
        leftBox.setPrefWidth(480);

        // -------- Tableau des produits finis à droite -------
        VBox rightBox = new VBox(16);
        rightBox.setPadding(new Insets(12, 0, 12, 12));

        Label listeTitre = new Label("Produits finis");
        listeTitre.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        TableView<Produit> table = new TableView<>(listeProduits);
        table.setPrefHeight(450);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Produit, String> idCol = new TableColumn<>("Identifiant");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));

        TableColumn<Produit, String> gammeCol = new TableColumn<>("Gamme utilisée");
        gammeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getGamme() == null ? "-" : data.getValue().getGamme().getRefGamme()
        ));

        TableColumn<Produit, String> machinesCol = new TableColumn<>("Machines utilisées");
        machinesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getGamme() == null ? "-" :
            data.getValue().getGamme().getListeEquipements().stream()
                .filter(eq -> eq instanceof Machine)
                .map(eq -> ((Machine)eq).getDmachine())
                .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        TableColumn<Produit, String> operationsCol = new TableColumn<>("Opérations");
        operationsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getGamme() == null ? "-" :
            data.getValue().getGamme().getOperations().stream()
                .map(Operation::getDescription)
                .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        table.getColumns().addAll(idCol, gammeCol, machinesCol, operationsCol);

        // Zone de détail produit
        Label detail = new Label("Sélectionnez un produit pour voir les détails.");
        detail.setStyle("-fx-font-size: 14px; -fx-padding: 14; -fx-background-color: #f5f7fb; -fx-border-radius: 7; -fx-background-radius: 7;");
        detail.setWrapText(true);
        detail.setMaxWidth(450);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, prod) -> {
            if (prod == null) {
                detail.setText("Sélectionnez un produit pour voir les détails.");
            } else {
                detail.setText(formatProduitDetail(prod));
            }
        });

        // Optionnel : bouton de suppression
        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setOnAction(e -> {
            Produit selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                listeProduits.remove(selected);
                detail.setText("Produit supprimé.");
            }
        });

        HBox actions = new HBox(10, supprimerBtn);
        actions.setStyle("-fx-alignment: center;");

        rightBox.getChildren().addAll(listeTitre, table, detail, actions);

        // --- Organisation responsive
        HBox hMain = new HBox(16, leftBox, new Separator(), rightBox);
        hMain.setFillHeight(true);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        root.setCenter(hMain);
        return root;
    }

    // Helper pour formater la présentation d'une gamme dans la ComboBox
    private static String formatGammeDetail(Gamme gamme) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ID: ").append(gamme.getRefGamme()).append("] ");
        sb.append("Machines: ");
        String machines = gamme.getListeEquipements().stream()
                .filter(eq -> eq instanceof Machine)
                .map(eq -> ((Machine) eq).getDmachine())
                .reduce((a, b) -> a + ", " + b).orElse("Aucune");
        sb.append(machines).append(" | ");
        sb.append("Opérations: ");
        String ops = gamme.getOperations().stream()
                .map(Operation::getDescription)
                .reduce((a, b) -> a + ", " + b).orElse("Aucune");
        sb.append(ops);
        return sb.toString();
    }

    // Helper pour formater les détails d'un produit
    private static String formatProduitDetail(Produit produit) {
        StringBuilder sb = new StringBuilder();
        sb.append("🔎 **Détail du produit**\n\n");
        sb.append("• Identifiant : ").append(produit.getId()).append("\n");
        if (produit.getGamme() != null) {
            Gamme gamme = produit.getGamme();
            sb.append("• Gamme utilisée : ").append(gamme.getRefGamme()).append("\n");
            sb.append("• Machines utilisées : ");
            String machines = gamme.getListeEquipements().stream()
                    .filter(eq -> eq instanceof Machine)
                    .map(eq -> ((Machine) eq).getDmachine())
                    .reduce((a, b) -> a + ", " + b).orElse("Aucune");
            sb.append(machines).append("\n");
            sb.append("• Opérations réalisées : ");
            String ops = gamme.getOperations().stream()
                    .map(Operation::getDescription)
                    .reduce((a, b) -> a + ", " + b).orElse("Aucune");
            sb.append(ops).append("\n");
            sb.append("• Coût total gamme : ").append(gamme.coutGamme()).append(" €\n");
            sb.append("• Durée totale gamme : ").append(gamme.dureeGamme()).append(" h");
        } else {
            sb.append("• Gamme : -");
        }
        return sb.toString();
    }
}
