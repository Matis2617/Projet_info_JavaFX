package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.Model.Fiabilite;
import com.mycompany.projet_fx.controller.FiabiliteController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class FiabiliteView {

    private FiabiliteController controller;

    public FiabiliteView(FiabiliteController controller) {
        this.controller = controller;
    }

    public void afficher(Stage primaryStage) {
        primaryStage.setTitle("Gestion de la Fiabilité des Machines");

        // TableView pour afficher les fiabilités
        TableView<Fiabilite> table = new TableView<>();
        table.setItems(controller.getFiabilites());

        TableColumn<Fiabilite, String> nomCol = new TableColumn<>("Nom Machine");
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomMachine()));

        TableColumn<Fiabilite, String> marcheCol = new TableColumn<>("Temps de marche (min)");
        marcheCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getTotalTempsDeMarche())));

        TableColumn<Fiabilite, String> panneCol = new TableColumn<>("Temps de panne (min)");
        panneCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getTotalTempsDePanne())));

        TableColumn<Fiabilite, String> tauxCol = new TableColumn<>("Taux de fiabilité");
        tauxCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.format("%.2f %%", data.getValue().calculerFiabilite() * 100)));

        table.getColumns().addAll(nomCol, marcheCol, panneCol, tauxCol);

        // Champs de saisie
        TextField nomField = new TextField();
        nomField.setPromptText("Nom machine");

        TextField marcheField = new TextField();
        marcheField.setPromptText("Temps marche");

        TextField panneField = new TextField();
        panneField.setPromptText("Temps panne");

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            try {
                String nom = nomField.getText();
                int marche = Integer.parseInt(marcheField.getText());
                int panne = Integer.parseInt(panneField.getText());

                Fiabilite f = new Fiabilite(nom, marche, panne);
                controller.ajouterFiabilite(f);

                nomField.clear();
                marcheField.clear();
                panneField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Erreur de saisie", "Temps de marche et de panne doivent être des entiers.");
            }
        });

        Button supprimerBtn = new Button("Supprimer sélection");
        supprimerBtn.setOnAction(e -> {
            Fiabilite selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.supprimerFiabilite(selected);
            }
        });

        HBox inputBox = new HBox(10, nomField, marcheField, panneField, ajouterBtn, supprimerBtn);
        inputBox.setPadding(new Insets(10));

        VBox root = new VBox(10, table, inputBox);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
