package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.*;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ProduitFormView {

    public static Node getProduitForm(ObservableList<Produit> listeProduits, ObservableList<Gamme> gammesList, Atelier atelier, String nomFichier, Runnable onRetourAccueil) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(28, 18, 18, 28));

        // ===== COLONNE GAUCHE : Création produit =====
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(8, 32, 8, 8));
        formBox.setStyle("-fx-background-color: #f9fbff; -fx-border-radius: 15; -fx-background-radius: 15; -fx-border-color: #dde3ec;");
        Label titre = new Label("Créer un produit fini");
        titre.setFont(Font.font("Segoe UI Semibold", 20));
        titre.setTextFill(Color.web("#274472"));

        TextField idField = new TextField();
        idField.setPromptText("Identifiant du produit");

        // Sélection gamme via un tableau cliquable
        Label gammeLabel = new Label("Sélectionne la gamme utilisée :");
        TableView<Gamme> tableGammes = new TableView<>(gammesList);
        tableGammes.setPrefHeight(180);
        tableGammes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Gamme, String> refCol = new TableColumn<>("Identifiant");
        refCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRefGamme()));

        TableColumn<Gamme, String> machinesCol = new TableColumn<>("Machines");
        machinesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getListeEquipements().isEmpty() ?
                "-" :
                joinMachines(data.getValue())
        ));

        TableColumn<Gamme, String> opCol = new TableColumn<>("Opérations");
        opCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getOperations().isEmpty() ?
                "-" :
                data.getValue().getOperations().stream()
                    .map(Operation::getDescription)
                    .reduce((a, b) -> a + ", " + b).orElse("-")
        ));

        TableColumn<Gamme, String> coutCol = new TableColumn<>("Prix (€)");
        coutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.2f", data.getValue().coutGamme())
        ));

        TableColumn<Gamme, String> dureeCol = new TableColumn<>("Durée (h)");
        dureeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.2f", data.getValue().dureeGamme())
        ));

        tableGammes.getColumns().addAll(refCol, machinesCol, opCol, coutCol, dureeCol);

        Label msg = new Label();
        msg.setStyle("-fx-text-fill: #6a3c2a; -fx-font-weight: bold;");

        Button ajouterBtn = new Button("Créer le produit");
        ajouterBtn.setStyle("-fx-background-color: #284785; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 6 20 6 20; -fx-background-radius: 7;");

        ajouterBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            Gamme gamme = tableGammes.getSelectionModel().getSelectedItem();
            if (id.isEmpty() || gamme == null) {
                msg.setText("→ Remplis tous les champs et sélectionne une gamme !");
                return;
            }
            boolean existe = listeProduits.stream().anyMatch(p -> p.getId().equals(id));
            if (existe) {
                msg.setText("Cet identifiant existe déjà !");
                return;
            }
            Produit produit = new Produit(id, gamme);
            listeProduits.add(produit);
            // === Sauvegarde immédiate ===
            if (atelier != null) {
                atelier.getProduits().clear();
                atelier.getProduits().addAll(listeProduits);
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            }
            msg.setText("Produit ajouté !");
            idField.clear();
            tableGammes.getSelectionModel().clearSelection();
        });

        Button retourBtn = new Button("Retour");
        retourBtn.setStyle("-fx-background-color: #d6e0f5; -fx-text-fill: #274472; -fx-font-weight: bold; -fx-background-radius: 8;");
        retourBtn.setOnAction(e -> {
            if (onRetourAccueil != null) onRetourAccueil.run();
        });

        formBox.getChildren().addAll(titre, idField, gammeLabel, tableGammes, ajouterBtn, retourBtn, msg);
        formBox.setPrefWidth(450);

        // ====== COLONNE DROITE : Table produits & détails ======
        VBox rightBox = new VBox(16);
        rightBox.setPadding(new Insets(10, 8, 8, 32));
        Label listeTitre = new Label("Produits finis enregistrés");
        listeTitre.setFont(Font.font("Segoe UI Semibold", 18));
        TableView<Produit> tableProduits = new TableView<>(listeProduits);
        tableProduits.setPrefHeight(390);
        tableProduits.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Produit, String> idCol = new TableColumn<>("Identifiant");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));

        TableColumn<Produit, String> gammeCol = new TableColumn<>("Gamme");
        gammeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getGamme() != null ? data.getValue().getGamme().getRefGamme() : "-"
        ));

        TableColumn<Produit, String> prixCol = new TableColumn<>("Prix (€)");
        prixCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getGamme() != null ? String.format("%.2f", data.getValue().getGamme().coutGamme()) : "-"
        ));

        TableColumn<Produit, String> tempsCol = new TableColumn<>("Durée (h)");
        tempsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getGamme() != null ? String.format("%.2f", data.getValue().getGamme().dureeGamme()) : "-"
        ));

        tableProduits.getColumns().addAll(idCol, gammeCol, prixCol, tempsCol);

        // --- Détail moderne ---
        VBox ficheDetail = new VBox(8);
        ficheDetail.setPadding(new Insets(16, 10, 12, 10));
        ficheDetail.setStyle("-fx-background-color: #f4f7fc; -fx-border-color: #c4d3ea; -fx-background-radius: 16; -fx-border-radius: 16; -fx-effect: dropshadow(three-pass-box, #cbe1ff66, 8,0,0,2);");
        ficheDetail.setPrefWidth(340);

        Label detailTitre = new Label("Détail du produit sélectionné");
        detailTitre.setFont(Font.font("Segoe UI Semibold", 16));
        detailTitre.setTextFill(Color.web("#254370"));
        Label detailId = new Label();
        detailId.setStyle("-fx-font-size: 14px;");
        Label detailGamme = new Label();
        Label detailMachines = new Label();
        Label detailOps = new Label();
        Label detailPrix = new Label();
        Label detailDuree = new Label();

        ficheDetail.getChildren().addAll(detailTitre, detailId, detailGamme, detailMachines, detailOps, detailPrix, detailDuree);

        tableProduits.getSelectionModel().selectedItemProperty().addListener((obs, old, prod) -> {
            ficheDetail.setVisible(prod != null);
            if (prod == null) {
                detailId.setText("Sélectionne un produit…");
                detailGamme.setText("");
                detailMachines.setText("");
                detailOps.setText("");
                detailPrix.setText("");
                detailDuree.setText("");
            } else {
                detailId.setText("Identifiant : " + prod.getId());
                if (prod.getGamme() != null) {
                    Gamme gamme = prod.getGamme();
                    detailGamme.setText("• Gamme : " + gamme.getRefGamme());
                    detailMachines.setText("• Machines : " + joinMachines(gamme));
                    detailOps.setText("• Opérations : " + gamme.getOperations().stream()
                            .map(Operation::getDescription)
                            .reduce((a, b) -> a + ", " + b).orElse("Aucune"));
                    detailPrix.setText("• Prix total : " + String.format("%.2f", gamme.coutGamme()) + " €");
                    detailDuree.setText("• Temps total : " + String.format("%.2f", gamme.dureeGamme()) + " h");
                } else {
                    detailGamme.setText("Aucune gamme.");
                    detailMachines.setText("");
                    detailOps.setText("");
                    detailPrix.setText("");
                    detailDuree.setText("");
                }
            }
        });
        ficheDetail.setVisible(false);

        // Optionnel : bouton de suppression stylé
        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setStyle("-fx-background-color: #ffc5c2; -fx-text-fill: #b22915; -fx-font-weight: bold; -fx-background-radius: 8;");
        supprimerBtn.setOnAction(e -> {
            Produit selected = tableProduits.getSelectionModel().getSelectedItem();
            if (selected != null) {
                listeProduits.remove(selected);
                if (atelier != null) {
                    atelier.getProduits().clear();
                    atelier.getProduits().addAll(listeProduits);
                    AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                }
                ficheDetail.setVisible(false);
            }
        });

        HBox actionsBox = new HBox(12, supprimerBtn);
        actionsBox.setStyle("-fx-alignment: center;");

        rightBox.getChildren().addAll(listeTitre, tableProduits, ficheDetail, actionsBox);

        // Organisation principale
        HBox hMain = new HBox(28, formBox, new Separator(), rightBox);
        hMain.setFillHeight(true);
        HBox.setHgrow(formBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        root.setCenter(hMain);

        return root;
    }

    // Helpers
    private static String joinMachines(Gamme gamme) {
        StringBuilder sb = new StringBuilder();
        for (Equipement eq : gamme.getListeEquipements()) {
            if (eq instanceof Machine) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(((Machine) eq).getDmachine());
            }
        }
        return sb.length() == 0 ? "Aucune" : sb.toString();
    }
}
