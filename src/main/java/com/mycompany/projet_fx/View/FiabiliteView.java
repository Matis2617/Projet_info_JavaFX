package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.Model.EvenementMaintenance;
import com.mycompany.projet_fx.Model.FiabiliteUtils;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FiabiliteView {

    private VBox root;
    private ObservableList<EvenementMaintenance> evenements = FXCollections.observableArrayList();

    public FiabiliteView() {
        createView();
    }

    private void createView() {
        // TableView pour les événements
        TableView<EvenementMaintenance> table = new TableView<>(evenements);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<EvenementMaintenance, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDateHeure().toLocalDate().toString()));

        TableColumn<EvenementMaintenance, String> heureCol = new TableColumn<>("Heure");
        heureCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDateHeure().toLocalTime().toString()));

        TableColumn<EvenementMaintenance, String> machineCol = new TableColumn<>("Machine");
        machineCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMachine()));

        TableColumn<EvenementMaintenance, String> eventCol = new TableColumn<>("Evénement");
        eventCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEvenement()));

        TableColumn<EvenementMaintenance, String> operateurCol = new TableColumn<>("Opérateur");
        operateurCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getOperateur()));

        TableColumn<EvenementMaintenance, String> causeCol = new TableColumn<>("Cause");
        causeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCause()));

        table.getColumns().addAll(dateCol, heureCol, machineCol, eventCol, operateurCol, causeCol);

        // Champs de saisie
        DatePicker datePicker = new DatePicker();
        TextField heureField = new TextField();  heureField.setPromptText("Heure (HH:mm)");
        TextField machineField = new TextField(); machineField.setPromptText("Machine");
        ComboBox<String> eventBox = new ComboBox<>();
        eventBox.getItems().addAll("A", "D");
        eventBox.setPromptText("Evénement");
        TextField operateurField = new TextField(); operateurField.setPromptText("Opérateur");
        TextField causeField = new TextField();    causeField.setPromptText("Cause");

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            try {
                LocalDate date = datePicker.getValue();
                LocalTime heure = LocalTime.parse(heureField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                String machine = machineField.getText();
                String evenement = eventBox.getValue();
                String operateur = operateurField.getText();
                String cause = causeField.getText();

                if (date == null || machine.isEmpty() || evenement == null || operateur.isEmpty() || cause.isEmpty()) {
                    showAlert("Erreur", "Veuillez remplir tous les champs.");
                    return;
                }

                evenements.add(new EvenementMaintenance(LocalDateTime.of(date, heure), machine, evenement, operateur, cause));
                datePicker.setValue(null);
                heureField.clear();
                machineField.clear();
                eventBox.setValue(null);
                operateurField.clear();
                causeField.clear();
            } catch (Exception ex) {
                showAlert("Erreur", "Format de saisie incorrect.");
            }
        });

        Button analyserBtn = new Button("Analyser la fiabilité");
        analyserBtn.setOnAction(e -> {
            // Construction de la map machine -> liste des événements (pour FiabiliteUtils)
            Map<String, List<EvenementMaintenance>> map = new HashMap<>();
            for (EvenementMaintenance ev : evenements) {
                map.computeIfAbsent(ev.getMachine(), k -> new ArrayList<>()).add(ev);
            }
            // On trie les événements par date (important)
            for (List<EvenementMaintenance> list : map.values()) {
                list.sort(Comparator.comparing(EvenementMaintenance::getDateHeure));
            }

            // Conversion vers périodes de panne pour FiabiliteUtils
            Map<String, List<FiabiliteUtils.PeriodePanne>> pannesParMachine = new HashMap<>();
            for (String machine : map.keySet()) {
                List<EvenementMaintenance> evts = map.get(machine);
                List<FiabiliteUtils.PeriodePanne> pannes = new ArrayList<>();
                LocalDateTime debut = null;
                for (EvenementMaintenance ev : evts) {
                    if (ev.getEvenement().equals("A")) debut = ev.getDateHeure();
                    else if (ev.getEvenement().equals("D") && debut != null) {
                        pannes.add(new FiabiliteUtils.PeriodePanne(debut, ev.getDateHeure()));
                        debut = null;
                    }
                }
                pannesParMachine.put(machine, pannes);
            }

            // Période d'observation = de la première à la dernière date
            if (evenements.isEmpty()) {
                showAlert("Info", "Aucun événement saisi.");
                return;
            }
            LocalDateTime obsDebut = evenements.stream().map(EvenementMaintenance::getDateHeure).min(LocalDateTime::compareTo).orElse(LocalDateTime.now());
            LocalDateTime obsFin = evenements.stream().map(EvenementMaintenance::getDateHeure).max(LocalDateTime::compareTo).orElse(LocalDateTime.now()).plusMinutes(1);

            Map<String, Double> fiabilites = FiabiliteUtils.calculerFiabilites(pannesParMachine, obsDebut, obsFin);
            List<Map.Entry<String, Double>> tri = FiabiliteUtils.trierMachinesParFiabilite(fiabilites);

            StringBuilder sb = new StringBuilder("Fiabilité des machines (décroissant):\n");
            for (Map.Entry<String, Double> entry : tri) {
                sb.append(String.format("%s : %.2f %%\n", entry.getKey(), entry.getValue() * 100));
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString());
            alert.setTitle("Analyse de fiabilité");
            alert.setHeaderText("Résultat analyse événements");
            alert.showAndWait();
        });

        HBox inputBox = new HBox(10, datePicker, heureField, machineField, eventBox, operateurField, causeField, ajouterBtn);
        inputBox.setPadding(new Insets(10));

        root = new VBox(10, table, inputBox, analyserBtn);
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
