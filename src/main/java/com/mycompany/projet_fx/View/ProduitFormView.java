package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.*;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class ProduitFormView {

    // Vue principale avec création et tableau permanent
    public static Node getProduitForm(
            ObservableList<Produit> listeProduits,
            ObservableList<Gamme> gammesList,
            Runnable onRetourAccueil,
            Atelier atelier,
            String nomFichier
    ) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));

        // ------- Formulaire à gauche -------
        VBox leftBox = new VBox(18);
        leftBox.setPadding(new Insets(12, 22, 12, 0));

        Label titre = new Label("Créer un produit fini");
        titre.setStyle("-fx-font-size: 19px; -fx-font-weight: bold;");

        TextField idField = new TextField();
        idField.setPromptText("Identifiant du produit");

        // Table des gammes pour choisir la gamme utilisée
        TableView<Gamme> gammeTable = new TableView<>(gammesList);
        gammeTable.setPrefHeight(200);
        gammeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Gamme, String> refCol = new TableColumn<>("Identifiant");
        refCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRefGamme()));

        TableColumn<Gamme, String> machinesCol = new TableColumn<>("Machines");
        machinesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getListeEquipements().stream()
                        .filter(eq -> eq instanceof Machine)
                        .map(eq -> ((Machine) eq).getDmachine())
                        .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        TableColumn<Gamme, String> operationsCol = new TableColumn<>("Opérations");
        operationsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getOperations().stream()
                        .map(Operation::getDescription)
                        .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        gammeTable.getColumns().addAll(refCol, machinesCol, operationsCol);

        Label gammeLabel = new Label("Sélectionne une gamme :");
        gammeLabel.setStyle("-fx-font-size: 13px;");

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
            // Vérifie unicité (optionnel)
            boolean existe = listeProduits.stream().anyMatch(p -> p.getId().equals(id));
            if (existe) {
                msg.setStyle("-fx-text-fill: red;");
                msg.setText("Cet identifiant de produit existe déjà !");
                return;
            }
            Produit produit = new Produit(id, gamme);
            listeProduits.add(produit);
            // Ajout dans l'atelier et sauvegarde immédiate
            atelier.getProduits().add(produit);
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
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
                gammeLabel,
                gammeTable,
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

        TableColumn<Produit, String> machinesColP = new TableColumn<>("Machines utilisées");
        machinesColP.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getGamme() == null ? "-" :
                        data.getValue().getGamme().getListeEquipements().stream()
                                .filter(eq -> eq instanceof Machine)
                                .map(eq -> ((Machine) eq).getDmachine())
                                .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        TableColumn<Produit, String> operationsColP = new TableColumn<>("Opérations");
        operationsColP.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getGamme() == null ? "-" :
                        data.getValue().getGamme().getOperations().stream()
                                .map(Operation::getDescription)
                                .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        table.getColumns().addAll(idCol, gammeCol, machinesColP, operationsColP);

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
                atelier.getProduits().remove(selected);
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
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
