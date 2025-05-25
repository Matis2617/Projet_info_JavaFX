package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.Model.Fiabilite;
import com.mycompany.projet_fx.Model.FiabiliteUtils;
import com.mycompany.projet_fx.Model.Machine;
import com.mycompany.projet_fx.controller.MachineController;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class FiabiliteView {

    private VBox root;
    private ObservableList<Fiabilite> fiabilites = FXCollections.observableArrayList();
    private ComboBox<Machine> machineComboBox;
    private ComboBox<String> causeComboBox;
    private MachineController machineController;

    public FiabiliteView(MachineController machineController) {
        this.machineController = machineController;
        createView();
    }

    private void createView() {
        // TableView pour les événements
        TableView<Fiabilite> table = new TableView<>(fiabilites);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Fiabilite, String> machineCol = new TableColumn<>("Machine");
        machineCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMachine().getDmachine()));

        TableColumn<Fiabilite, String> debutPanneCol = new TableColumn<>("Début Panne");
        debutPanneCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDebutPanne().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        TableColumn<Fiabilite, String> finPanneCol = new TableColumn<>("Fin Panne");
        finPanneCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFinPanne() != null ? data.getValue().getFinPanne().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "En cours"));

        TableColumn<Fiabilite, String> causeCol = new TableColumn<>("Cause");
        causeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCause()));

        table.getColumns().addAll(machineCol, debutPanneCol, finPanneCol, causeCol);

        // ComboBox pour sélectionner la machine et la cause
        machineComboBox = new ComboBox<>(machineController.getMachines());
        machineComboBox.setPromptText("Machine");

        causeComboBox = new ComboBox<>();
        causeComboBox.getItems().addAll("panne", "maintenance", "accident");
        causeComboBox.setPromptText("Cause");

        // Boutons pour enregistrer les pannes et les reprises
        Button enregistrerPanneBtn = new Button("Enregistrer Panne");
        enregistrerPanneBtn.setOnAction(e -> {
            Machine machine = machineComboBox.getValue();
            String cause = causeComboBox.getValue();
            if (machine != null && cause != null) {
                Fiabilite fiabilite = new Fiabilite(machine, LocalDateTime.now(), null, cause);
                fiabilites.add(fiabilite);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une machine et une cause.");
                alert.showAndWait();
            }
        });

        Button enregistrerRepriseBtn = new Button("Enregistrer Reprise");
        enregistrerRepriseBtn.setOnAction(e -> {
            Fiabilite selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.enregistrerReprise(LocalDateTime.now());
                table.refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une panne à terminer.");
                alert.showAndWait();
            }
        });

        Button genererFichierBtn = new Button("Générer fiabilite.txt");
        genererFichierBtn.setOnAction(e -> {
            FiabiliteUtils.ecrireFiabiliteDansFichier(new ArrayList<>(fiabilites), "fiabilite.txt");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Fichier fiabilite.txt généré.");
            alert.showAndWait();
        });

        HBox btnBox = new HBox(12, machineComboBox, causeComboBox, enregistrerPanneBtn, enregistrerRepriseBtn, genererFichierBtn);

        root = new VBox(14, table, btnBox);
        root.setPadding(new Insets(16));
    }

    public Parent getView() {
        return root;
    }
}