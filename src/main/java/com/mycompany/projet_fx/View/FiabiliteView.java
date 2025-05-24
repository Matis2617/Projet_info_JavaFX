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

        TableColumn<Fiabilite, String> nomCol = new TableColumn<>("Nom Machine");
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomMachine()));

        TableColumn<Fiabilite, String> marcheCol = new TableColumn<>("Temps de marche (min)");
        marcheCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getTotalTempsDeMarche())));

        TableColumn<Fiabilite, String> panneCol = new TableColumn<>("Temps de panne (min)");
        panneCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getTotalTempsDePanne())));

        TableColumn<Fiabilite, String> causeCol = new TableColumn<>("Cause");
        causeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCause()));

        TableColumn<Fiabilite, String> dateHeureCol = new TableColumn<>("Date/Heure Panne");
        dateHeureCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDateHeurePanne().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        TableColumn<Fiabilite, String> tauxCol = new TableColumn<>("Taux de fiabilité");
        tauxCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.format("%.2f %%", data.getValue().calculerFiabilite() * 100)));

        table.getColumns().addAll(nomCol, marcheCol, panneCol, causeCol, dateHeureCol, tauxCol);

        // Champs de saisie
        TextField nomField = new TextField();
        nomField.setPromptText("Nom machine");

        TextField marcheField = new TextField();
        marcheField.setPromptText("Temps marche");

        TextField panneField = new TextField();
        panneField.setPromptText("Temps panne");

        TextField causeField = new TextField();
        causeField.setPromptText("Cause");

        DatePicker datePicker = new DatePicker();
        TextField heureField = new TextField();
        heureField.setPromptText("Heure (HH:mm)");

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            try {
                String nom = nomField.getText();
                int marche = Integer.parseInt(marcheField.getText());
                int panne = Integer.parseInt(panneField.getText());
                String cause = causeField.getText();
                LocalDateTime dateHeurePanne = datePicker.getValue().atTime(
                    LocalTime.parse(heureField.getText(), DateTimeFormatter.ofPattern("HH:mm"))
                );

                Fiabilite f = new Fiabilite(nom, marche, panne, cause, dateHeurePanne);
                controller.ajouterFiabilite(f);

                nomField.clear();
                marcheField.clear();
                panneField.clear();
                causeField.clear();
                datePicker.setValue(null);
                heureField.clear();
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

        HBox inputBox = new HBox(10, nomField, marcheField, panneField, causeField, datePicker, heureField, ajouterBtn, supprimerBtn);
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
