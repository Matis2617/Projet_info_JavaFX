package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.*;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class ProduitFormView {

    // ----- Vue de création + tableau permanent -----
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

        // Tableau des gammes (au lieu d'une combo box)
        TableView<Gamme> gammeTable = new TableView<>(gammesList);
        gammeTable.setPrefHeight(220);
        gammeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Gamme, String> refCol = new TableColumn<>("Identifiant");
        refCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRefGamme()));

        TableColumn<Gamme, String> machinesCol = new TableColumn<>("Machines utilisées");
        machinesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getListeEquipements().stream()
                .filter(eq -> eq instanceof Machine)
                .map(eq -> ((Machine) eq).getDmachine())
                .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        TableColumn<Gamme, String> opsCol = new TableColumn<>("Opérations");
        opsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getOperations().stream()
                .map(Operation::getDescription)
                .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        gammeTable.getColumns().addAll(refCol, machinesCol, opsCol);

        // Pour sélectionner la gamme choisie
        final Gamme[] gammeSelectionnee = {null};
        gammeTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            gammeSelectionnee[0] = selected;
        });

        Button ajouterBtn = new Button("Ajouter le produit");
        Label msg = new Label();
        msg.setStyle("-fx-text-fill: green;");

        ajouterBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            Gamme gamme = gammeSelectionnee[0];
            if (id.isEmpty() || gamme == null) {
                msg.setText("Remplis tous les champs !");
                return;
            }
            // Vérifie unicité
            boolean existe = listeProduits.stream().anyMatch(p -> p.getId().equals(id));
            if (existe) {
                msg.setText("Cet identifiant de produit existe déjà !");
                return;
            }
            Produit produit = new Produit(id, gamme);
            listeProduits.add(produit);
            // Sauvegarde auto
            if (gamme != null && gamme.getAtelier() != null) {
                Atelier at = gamme.getAtelier();
                if (!at.getProduits().contains(produit)) at.getProduits().add(produit);
                AtelierSauvegarde.sauvegarderAtelier(at, "atelier_" + at.getNom().toLowerCase() + ".ser");
            }
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
                new Label("Sélectionnez une gamme de fabrication :"),
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

        TableColumn<Produit, String> machinesProdCol = new TableColumn<>("Machines utilisées");
        machinesProdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
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

        table.getColumns().addAll(idCol, gammeCol, machinesProdCol, operationsCol);

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
                if (selected.getGamme() != null && selected.getGamme().getAtelier() != null) {
                    Atelier at = selected.getGamme().getAtelier();
                    at.getProduits().remove(selected);
                    AtelierSauvegarde.sauvegarderAtelier(at, "atelier_" + at.getNom().toLowerCase() + ".ser");
                }
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
