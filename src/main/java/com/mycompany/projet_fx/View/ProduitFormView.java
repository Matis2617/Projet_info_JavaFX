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

        // ------- Formulaire à gauche -------
        VBox leftBox = new VBox(18);
        leftBox.setPadding(new Insets(12, 22, 12, 0));

        Label titre = new Label("Créer un produit fini");
        titre.setStyle("-fx-font-size: 19px; -fx-font-weight: bold;");

        TextField idField = new TextField();
        idField.setPromptText("Identifiant du produit");

        // Tableau de sélection de gamme
        Label gammeTableLabel = new Label("Sélectionnez une gamme utilisée :");
        gammeTableLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        TableView<Gamme> gammeTable = new TableView<>(gammesList);
        gammeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        gammeTable.setPrefHeight(180);
        gammeTable.setMaxWidth(520);

        TableColumn<Gamme, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRefGamme()));

        TableColumn<Gamme, String> machinesCol = new TableColumn<>("Machines");
        machinesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getListeEquipements().stream()
                .filter(eq -> eq instanceof Machine)
                .map(eq -> ((Machine) eq).getDmachine())
                .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        TableColumn<Gamme, String> opCol = new TableColumn<>("Opérations");
        opCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getOperations().stream()
                .map(Operation::getDescription)
                .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        gammeTable.getColumns().addAll(idCol, machinesCol, opCol);

        // Sélection unique !
        gammeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Button ajouterBtn = new Button("Ajouter le produit");
        Label msg = new Label();
        msg.setStyle("-fx-text-fill: green;");

        ajouterBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            Gamme gamme = gammeTable.getSelectionModel().getSelectedItem();
            if (id.isEmpty() || gamme == null) {
                msg.setStyle("-fx-text-fill: red;");
                msg.setText("Remplis tous les champs et sélectionne une gamme !");
                return;
            }
            boolean existe = listeProduits.stream().anyMatch(p -> p.getId().equals(id));
            if (existe) {
                msg.setStyle("-fx-text-fill: red;");
                msg.setText("Cet identifiant de produit existe déjà !");
                return;
            }
            Produit produit = new Produit(id, gamme);
            listeProduits.add(produit);
            msg.setStyle("-fx-text-fill: green;");
            msg.setText("Produit ajouté !");
            idField.clear();
            gammeTable.getSelectionModel().clearSelection();
        });

        Button retourBtn = new Button("Retour");
        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        leftBox.getChildren().addAll(
                titre,
                idField,
                gammeTableLabel,
                gammeTable,
                ajouterBtn, retourBtn, msg
        );
        leftBox.setPrefWidth(540);

        // -------- Tableau des produits finis à droite -------
        VBox rightBox = new VBox(16);
        rightBox.setPadding(new Insets(12, 0, 12, 12));

        Label listeTitre = new Label("Produits finis");
        listeTitre.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        TableView<Produit> table = new TableView<>(listeProduits);
        table.setPrefHeight(450);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Produit, String> prodIdCol = new TableColumn<>("Identifiant");
        prodIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));

        TableColumn<Produit, String> prodGammeCol = new TableColumn<>("Gamme utilisée");
        prodGammeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getGamme() == null ? "-" : data.getValue().getGamme().getRefGamme()
        ));

        TableColumn<Produit, String> prodMachinesCol = new TableColumn<>("Machines");
        prodMachinesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getGamme() == null ? "-" :
            data.getValue().getGamme().getListeEquipements().stream()
                .filter(eq -> eq instanceof Machine)
                .map(eq -> ((Machine)eq).getDmachine())
                .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        TableColumn<Produit, String> prodOpCol = new TableColumn<>("Opérations");
        prodOpCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getGamme() == null ? "-" :
            data.getValue().getGamme().getOperations().stream()
                .map(Operation::getDescription)
                .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        TableColumn<Produit, String> prodCoutCol = new TableColumn<>("Prix (€)");
        prodCoutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getGamme() == null ? "-" :
            String.format("%.2f", data.getValue().getGamme().coutGamme())
        ));

        TableColumn<Produit, String> prodDureeCol = new TableColumn<>("Durée (h)");
        prodDureeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getGamme() == null ? "-" :
            String.format("%.2f", data.getValue().getGamme().dureeGamme())
        ));

        table.getColumns().addAll(prodIdCol, prodGammeCol, prodMachinesCol, prodOpCol, prodCoutCol, prodDureeCol);

        // Zone de détail produit (présentation moderne)
        VBox detailBox = new VBox(7);
        detailBox.setPadding(new Insets(15, 10, 10, 10));
        detailBox.setStyle("-fx-background-color: #f7faff; -fx-border-color: #aac4ea; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label detailTitre = new Label("Détail du produit sélectionné");
        detailTitre.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2856a8;");
        Label detailContent = new Label("Sélectionnez un produit pour voir les détails.");
        detailContent.setWrapText(true);
        detailContent.setStyle("-fx-font-size: 14px;");

        detailBox.getChildren().addAll(detailTitre, detailContent);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, prod) -> {
            if (prod == null) {
                detailContent.setText("Sélectionnez un produit pour voir les détails.");
            } else {
                detailContent.setText(formatProduitDetail(prod));
            }
        });

        // Optionnel : bouton de suppression
        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setOnAction(e -> {
            Produit selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                listeProduits.remove(selected);
                detailContent.setText("Produit supprimé.");
            }
        });

        HBox actions = new HBox(10, supprimerBtn);
        actions.setStyle("-fx-alignment: center;");

        rightBox.getChildren().addAll(listeTitre, table, detailBox, actions);

        // --- Organisation responsive
        HBox hMain = new HBox(16, leftBox, new Separator(), rightBox);
        hMain.setFillHeight(true);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        root.setCenter(hMain);
        return root;
    }

    // Helper : présentation améliorée d’un produit
    private static String formatProduitDetail(Produit produit) {
        StringBuilder sb = new StringBuilder();
        sb.append("🟦 Identifiant : ").append(produit.getId()).append("\n\n");
        if (produit.getGamme() != null) {
            Gamme gamme = produit.getGamme();
            sb.append("➡️  Gamme : ").append(gamme.getRefGamme()).append("\n");
            sb.append("🛠️ Machines utilisées : ");
            String machines = gamme.getListeEquipements().stream()
                    .filter(eq -> eq instanceof Machine)
                    .map(eq -> ((Machine) eq).getDmachine())
                    .reduce((a, b) -> a + ", " + b).orElse("Aucune");
            sb.append(machines).append("\n");
            sb.append("📝 Opérations : ");
            String ops = gamme.getOperations().stream()
                    .map(Operation::getDescription)
                    .reduce((a, b) -> a + ", " + b).orElse("Aucune");
            sb.append(ops).append("\n");
            sb.append("💶 Prix (coût total) : ").append(String.format("%.2f", gamme.coutGamme())).append(" €\n");
            sb.append("⏱️ Durée totale : ").append(String.format("%.2f", gamme.dureeGamme())).append(" h");
        } else {
            sb.append("• Gamme : -");
        }
        return sb.toString();
    }
}
