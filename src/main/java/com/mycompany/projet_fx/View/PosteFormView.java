package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Model.Machine;
import com.mycompany.projet_fx.Model.Equipement;
import com.mycompany.projet_fx.Model.Poste;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class PosteFormView {

    public static Node getPosteForm(Atelier atelier, String nomFichier, Runnable onRetourAccueil) {
        // Liste observable des machines
        ObservableList<Machine> machinesList = FXCollections.observableArrayList();
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) machinesList.add((Machine) eq);
        }

        // Liste observable des postes
        ObservableList<Poste> postesList = FXCollections.observableArrayList(atelier.getPostes());

        // Tableau des machines
        TableView<Machine> machineTable = new TableView<>(machinesList);
        machineTable.setPrefHeight(300);
        machineTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Machine, Number> idCol = new TableColumn<>("Identifiant");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRefmachine()));
        TableColumn<Machine, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDmachine()));
        TableColumn<Machine, Number> abscCol = new TableColumn<>("Abscisse");
        abscCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAbscisse()));
        TableColumn<Machine, Number> ordCol = new TableColumn<>("Ordonnée");
        ordCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getOrdonnee()));
        machineTable.getColumns().addAll(idCol, descCol, abscCol, ordCol);

        // Tableau des postes
        TableView<Poste> posteTable = new TableView<>(postesList);
        posteTable.setPrefHeight(300);
        posteTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Poste, String> posteNomCol = new TableColumn<>("Nom Poste");
        posteNomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomPoste()));
        // Couleur
        TableColumn<Poste, Poste> colorCol = new TableColumn<>("Couleur");
        colorCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Poste poste, boolean empty) {
                super.updateItem(poste, empty);
                setText(null);
                setGraphic(null);
                if (poste != null && !empty) {
                    Rectangle rect = new Rectangle(18, 18, getColorForPoste(postesList.indexOf(poste)));
                    setGraphic(rect);
                }
            }
            @Override
            public void updateIndex(int i) {
                super.updateIndex(i);
            }
        });
        colorCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue()));
        posteTable.getColumns().addAll(posteNomCol, colorCol);

        // Détail des machines du poste sélectionné
        VBox posteDetailsBox = new VBox(8);
        posteDetailsBox.setPadding(new Insets(8));
        posteDetailsBox.setStyle("-fx-background-color: #f7f7f7; -fx-border-color: #c0c0c0;");

        posteTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            posteDetailsBox.getChildren().clear();
            if (selected != null) {
                Color color = getColorForPoste(postesList.indexOf(selected));
                Label titre = new Label("Machines de ce poste :");
                titre.setStyle("-fx-font-weight: bold;");
                posteDetailsBox.getChildren().add(titre);
                for (Machine m : selected.getMachines()) {
                    HBox h = new HBox(7, new Rectangle(12,12,color), new Label(m.toString()));
                    posteDetailsBox.getChildren().add(h);
                }
                if (selected.getMachines().isEmpty()) {
                    posteDetailsBox.getChildren().add(new Label("(Aucune machine affectée)"));
                }
            }
        });

        // Formulaire pour créer/éditer un poste
        TextField nomField = new TextField();
        nomField.setPromptText("Nom du poste");
        Button ajouterBtn = new Button("Créer Poste");
        Button modifierBtn = new Button("Renommer");
        Button supprimerBtn = new Button("Supprimer");
        Button ajouterMachineBtn = new Button("Affecter machine →");
        Button retirerMachineBtn = new Button("← Retirer machine");

        Label msgLabel = new Label();
        msgLabel.setStyle("-fx-text-fill: green;");

        // Ajouter un poste
        ajouterBtn.setOnAction(e -> {
            String nom = nomField.getText().trim();
            if (nom.isEmpty()) {
                msgLabel.setText("Veuillez donner un nom au poste.");
                return;
            }
            // Vérifier unicité
            for (Poste p : postesList) if (p.getNomPoste().equalsIgnoreCase(nom)) {
                msgLabel.setText("Nom déjà pris.");
                return;
            }
            Poste p = new Poste(nom);
            postesList.add(p);
            atelier.getPostes().add(p);
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            msgLabel.setText("Poste ajouté !");
            nomField.clear();
        });

        // Renommer le poste sélectionné
        modifierBtn.setOnAction(e -> {
            Poste sel = posteTable.getSelectionModel().getSelectedItem();
            if (sel == null) {
                msgLabel.setText("Sélectionnez un poste.");
                return;
            }
            String nom = nomField.getText().trim();
            if (nom.isEmpty()) {
                msgLabel.setText("Le nom ne peut pas être vide.");
                return;
            }
            sel.setNomPoste(nom);
            posteTable.refresh();
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            msgLabel.setText("Nom modifié.");
        });

        // Supprimer un poste
        supprimerBtn.setOnAction(e -> {
            Poste sel = posteTable.getSelectionModel().getSelectedItem();
            if (sel == null) {
                msgLabel.setText("Sélectionnez un poste à supprimer.");
                return;
            }
            atelier.getPostes().remove(sel);
            postesList.remove(sel);
            posteTable.getSelectionModel().clearSelection();
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            msgLabel.setText("Poste supprimé.");
            posteDetailsBox.getChildren().clear();
        });

        // Ajouter machine sélectionnée à un poste
        ajouterMachineBtn.setOnAction(e -> {
            Machine m = machineTable.getSelectionModel().getSelectedItem();
            Poste p = posteTable.getSelectionModel().getSelectedItem();
            if (m == null || p == null) {
                msgLabel.setText("Sélectionnez la machine ET le poste.");
                return;
            }
            if (!p.getMachines().contains(m)) {
                p.getMachines().add(m);
                posteTable.refresh();
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                msgLabel.setText("Machine affectée.");
                posteTable.getSelectionModel().select(p);
            }
        });

        // Retirer machine sélectionnée du poste
        retirerMachineBtn.setOnAction(e -> {
            Poste p = posteTable.getSelectionModel().getSelectedItem();
            if (p == null) {
                msgLabel.setText("Sélectionnez un poste.");
                return;
            }
            // Choisir la machine à retirer parmi celles du poste
            if (p.getMachines().isEmpty()) {
                msgLabel.setText("Aucune machine à retirer.");
                return;
            }
            ChoiceDialog<Machine> dialog = new ChoiceDialog<>(p.getMachines().get(0), p.getMachines());
            dialog.setTitle("Retirer une machine");
            dialog.setHeaderText("Sélectionnez la machine à retirer du poste");
            dialog.setContentText("Machine :");
            dialog.showAndWait().ifPresent(machine -> {
                p.getMachines().remove(machine);
                posteTable.refresh();
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                msgLabel.setText("Machine retirée.");
                posteTable.getSelectionModel().select(p);
            });
        });

        // Layout principal
        VBox leftBox = new VBox(10, new Label("Machines"), machineTable, ajouterMachineBtn);
        VBox rightBox = new VBox(10, new Label("Postes"), posteTable, new HBox(7, nomField, ajouterBtn, modifierBtn, supprimerBtn), retirerMachineBtn, msgLabel, posteDetailsBox);

        leftBox.setPrefWidth(370);
        rightBox.setPrefWidth(380);

        HBox root = new HBox(16, leftBox, rightBox);
        root.setPadding(new Insets(20));

        // Retour accueil
        Button retourBtn = new Button("Retour");
        retourBtn.setOnAction(e -> { if (onRetourAccueil != null) onRetourAccueil.run(); });
        rightBox.getChildren().add(retourBtn);

        return root;
    }

    // Renvoie une couleur parmi la palette pour chaque poste (index)
    private static Color getColorForPoste(int i) {
        Color[] couleurs = {
                Color.ROYALBLUE, Color.DARKORANGE, Color.FORESTGREEN, Color.DARKVIOLET, Color.DARKCYAN,
                Color.CRIMSON, Color.DARKMAGENTA, Color.GOLD, Color.MEDIUMPURPLE, Color.DARKSLATEGRAY
        };
        return couleurs[i % couleurs.length];
    }
}
