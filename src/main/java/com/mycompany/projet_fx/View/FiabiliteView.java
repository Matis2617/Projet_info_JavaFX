package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Model.Fiabilite;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
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

    private Atelier atelier;           // Ajout pour la sauvegarde
    private String nomFichier;         // Ajout pour la sauvegarde

    // Modifiez le constructeur pour accepter atelier et nomFichier
    public FiabiliteView(FiabiliteController controller, Atelier atelier, String nomFichier) {
        this.controller = controller;
        this.atelier = atelier;
        this.nomFichier = nomFichier;
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

                // === SAUVEGARDE APRÈS AJOUT ===
                if (atelier != null) {
                    atelier.getFiabilites().clear();
                    atelier.getFiabilites().addAll(controller.getFiabilites());
                    AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                }

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

                // === SAUVEGARDE APRÈS SUPPRESSION ===
                if (atelier != null) {
                    atelier.getFiabilites().clear();
                    atelier.getFiabilites().addAll(controller.getFiabilites());
                    AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                }
            }
        });
        Button analyserBtn = new Button("Analyser fiabilité depuis le fichier");
analyserBtn.setOnAction(e -> {
    // Remplace le chemin si besoin
    String cheminFichier = "suiviMaintenance.txt";

    // Ex : observation sur le 20/01/2020 06h00 à 20h00 (à ajuster si ton fichier contient plusieurs jours)
    LocalDateTime heureDebut = LocalDateTime.of(2020, 1, 20, 6, 0);
    LocalDateTime heureFin   = LocalDateTime.of(2020, 1, 20, 20, 0);

    Map<String, List<FiabiliteUtils.PeriodePanne>> pannes = FiabiliteUtils.chargerPannesMachines(cheminFichier);
    Map<String, Double> fiabilites = FiabiliteUtils.calculerFiabilites(pannes, heureDebut, heureFin);
    List<Map.Entry<String, Double>> tri = FiabiliteUtils.trierMachinesParFiabilite(fiabilites);

    StringBuilder sb = new StringBuilder("Fiabilité des machines (décroissant):\n");
    for (Map.Entry<String, Double> entry : tri) {
        sb.append(String.format("%s : %.2f %%\n", entry.getKey(), entry.getValue() * 100));
    }
    Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString());
    alert.setTitle("Analyse de fiabilité");
    alert.setHeaderText("Résultat analyse fichier");
    alert.showAndWait();
});


        HBox inputBox = new HBox(10, nomField, debutDatePicker, debutHeureField, finDatePicker, finHeureField, causeField, ajouterBtn, supprimerBtn);
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
