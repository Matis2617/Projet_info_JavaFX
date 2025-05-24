package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.Model.Fiabilite;
import com.mycompany.projet_fx.controller.FiabiliteController;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FiabiliteView {

    private FiabiliteController controller;
    private VBox root;

    public FiabiliteView(FiabiliteController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        // TableView pour afficher les fiabilités
        TableView<Fiabilite> table = new TableView<>();
        table.setItems(controller.getFiabilites());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Fiabilite, String> nomCol = new TableColumn<>("Nom Machine");
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomMachine()));

        TableColumn<Fiabilite, String> debutCol = new TableColumn<>("Début Panne");
        debutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDebutPanne().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        TableColumn<Fiabilite, String> finCol = new TableColumn<>("Fin Panne");
        finCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFinPanne().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        TableColumn<Fiabilite, String> causeCol = new TableColumn<>("Cause");
        causeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCause()));

        TableColumn<Fiabilite, String> tauxCol = new TableColumn<>("Taux de fiabilité");
        tauxCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.format("%.2f %%", data.getValue().calculerFiabilite() * 100)));

        table.getColumns().addAll(nomCol, debutCol, finCol, causeCol, tauxCol);

        // Champs de saisie
        TextField nomField = new TextField();
        nomField.setPromptText("Nom machine");

        DatePicker debutDatePicker = new DatePicker();
        TextField debutHeureField = new TextField();
        debutHeureField.setPromptText("Heure début (HH:mm)");

        DatePicker finDatePicker = new DatePicker();
        TextField finHeureField = new TextField();
        finHeureField.setPromptText("Heure fin (HH:mm)");

        TextField causeField = new TextField();
        causeField.setPromptText("Cause");

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            try {
                String nom = nomField.getText();
                LocalDateTime debutPanne = debutDatePicker.getValue().atTime(
                    java.time.LocalTime.parse(debutHeureField.getText(), DateTimeFormatter.ofPattern("HH:mm"))
                );
                LocalDateTime finPanne = finDatePicker.getValue().atTime(
                    java.time.LocalTime.parse(finHeureField.getText(), DateTimeFormatter.ofPattern("HH:mm"))
                );
                String cause = causeField.getText();

                Fiabilite f = new Fiabilite(nom, debutPanne, finPanne, cause);
                controller.ajouterFiabilite(f);

                nomField.clear();
                debutDatePicker.setValue(null);
                debutHeureField.clear();
                finDatePicker.setValue(null);
                finHeureField.clear();
                causeField.clear();
            } catch (Exception ex) {
                showAlert("Erreur de saisie", "Veuillez vérifier les entrées.");
            }
        });

        Button supprimerBtn = new Button("Supprimer sélection");
        supprimerBtn.setOnAction(e -> {
            Fiabilite selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.supprimerFiabilite(selected);
            }
        });

        HBox inputBox = new HBox(10, nomField, debutDatePicker, debutHeureField, finDatePicker, finHeureField, causeField, ajouterBtn, supprimerBtn);
        inputBox.setPadding(new Insets(10));

        root = new VBox(10, table, inputBox);
        root.setPadding(new Insets(10));
    }

    public Parent getView() {
        return root;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
