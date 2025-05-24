package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Machine;
import com.mycompany.projet_fx.Model.Equipement;
import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.cell.PropertyValueFactory;

public class MachineFormView {

    public static VBox getMachineForm(Atelier atelier, String nomFichier, Runnable onSuccess) {
        VBox box = new VBox(16);
        box.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Liste observable des machines existantes
        ObservableList<Machine> machinesList = FXCollections.observableArrayList();
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) machinesList.add((Machine) eq);
        }

        // TableView
        TableView<Machine> tableView = new TableView<>(machinesList);
        tableView.setPrefHeight(200);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        TableColumn<Machine, Number> idCol = new TableColumn<>("Identifiant");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRefmachine()));

        TableColumn<Machine, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDmachine()));

        TableColumn<Machine, Number> abscCol = new TableColumn<>("Abscisse");
        abscCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAbscisse()));

        TableColumn<Machine, Number> ordCol = new TableColumn<>("Ordonnée");
        ordCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getOrdonnee()));

        TableColumn<Machine, Number> coutCol = new TableColumn<>("Coût horaire");
        coutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getC()));
        
        TableColumn<Machine, String> etatCol = new TableColumn<>("État");
        etatCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEtat().toString()));
        
        TableColumn<Machine, String> operateurCol = new TableColumn<>("Opérateur");
        operateurCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getOperateur() != null ? data.getValue().getOperateur().getNom() + " " + data.getValue().getOperateur().getPrenom() : "Aucun"
        ));
        
        tableView.getColumns().addAll(idCol, descCol, abscCol, ordCol, coutCol,etatCol, operateurCol);

        // Champs de saisie
        TextField idField = new TextField();
        idField.setPromptText("Identifiant (unique)");
        TextField descField = new TextField();
        descField.setPromptText("Description");
        TextField abscField = new TextField();
        abscField.setPromptText("Abscisse (0-9)");
        TextField ordField = new TextField();
        ordField.setPromptText("Ordonnée (0-9)");
        TextField coutField = new TextField();
        coutField.setPromptText("Coût horaire (€)");

        ComboBox<Machine.ETAT> etatBox = new ComboBox<>();
        etatBox.getItems().addAll(Machine.ETAT.values());
        etatBox.setPromptText("État");

        Label erreurLabel = new Label();
        erreurLabel.setStyle("-fx-text-fill: red;");

        // Boutons
        Button ajouterBtn = new Button("Ajouter");
        Button modifierBtn = new Button("Modifier");
        Button supprimerBtn = new Button("Supprimer");
        Button retourBtn = new Button("Retour");

        // Ajouter une machine
        ajouterBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String desc = descField.getText();
                int absc = Integer.parseInt(abscField.getText());
                int ord = Integer.parseInt(ordField.getText());
                float cout = Float.parseFloat(coutField.getText());
                Machine.ETAT etat = etatBox.getValue();

                if (absc < 0 || absc >= 10 || ord < 0 || ord >= 10) {
                    erreurLabel.setText("Erreur : Coordonnées hors de l'atelier !");
                    return;
                }
                if (desc.isEmpty() || etat == null) {
                    erreurLabel.setText("Veuillez remplir tous les champs.");
                    return;
                }
                // Vérifie unicité identifiant et position
                for (Equipement eq : atelier.getEquipements()) {
                    if (eq instanceof Machine) {
                        Machine m = (Machine) eq;
                        if (m.getRefmachine() == id) {
                            erreurLabel.setText("Erreur : Identifiant déjà utilisé !");
                            return;
                        }
                        if (m.getAbscisse() == absc && m.getOrdonnee() == ord) {
                            erreurLabel.setText("Erreur : Coordonnées déjà utilisées !");
                            return;
                        }
                    }
                }

                Machine m = new Machine(
                        atelier.getEquipements().size() + 1, // id_equipement unique interne
                        id, desc, absc, ord, cout, etat
                );
                atelier.getEquipements().add(m);
                machinesList.add(m);
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);

                idField.clear();
                descField.clear();
                abscField.clear();
                ordField.clear();
                coutField.clear();
                etatBox.setValue(null);
                erreurLabel.setText("");
                if (onSuccess != null) onSuccess.run();

            } catch (Exception ex) {
                erreurLabel.setText("Erreur : Données invalides.");
            }
        });

        // Remplir les champs lors de la sélection d'une ligne
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, selected) -> {
            if (selected != null) {
                idField.setText(String.valueOf(selected.getRefmachine()));
                descField.setText(selected.getDmachine());
                abscField.setText(String.valueOf(selected.getAbscisse()));
                ordField.setText(String.valueOf(selected.getOrdonnee()));
                coutField.setText(String.valueOf(selected.getC()));
                etatBox.setValue(selected.getEtat());
            }
        });

        // Modifier une machine sélectionnée
        modifierBtn.setOnAction(e -> {
            Machine selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                erreurLabel.setText("Sélectionnez une machine à modifier.");
                return;
            }
            try {
                int id = Integer.parseInt(idField.getText());
                String desc = descField.getText();
                int absc = Integer.parseInt(abscField.getText());
                int ord = Integer.parseInt(ordField.getText());
                float cout = Float.parseFloat(coutField.getText());
                Machine.ETAT etat = etatBox.getValue();

                if (absc < 0 || absc >= 10 || ord < 0 || ord >= 10) {
                    erreurLabel.setText("Erreur : Coordonnées hors de l'atelier !");
                    return;
                }
                if (desc.isEmpty() || etat == null) {
                    erreurLabel.setText("Veuillez remplir tous les champs.");
                    return;
                }
                // Vérifie unicité sur les autres machines
                for (Machine m : machinesList) {
                    if (m != selected) {
                        if (m.getRefmachine() == id) {
                            erreurLabel.setText("Erreur : Identifiant déjà utilisé !");
                            return;
                        }
                        if (m.getAbscisse() == absc && m.getOrdonnee() == ord) {
                            erreurLabel.setText("Erreur : Coordonnées déjà utilisées !");
                            return;
                        }
                    }
                }
                selected.setRefmachine(id);
                selected.setDmachine(desc);
                selected.setAbscisse(absc);
                selected.setOrdonnee(ord);
                selected.setC(cout);
                selected.setEtat(etat);
                tableView.refresh();
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);

                erreurLabel.setText("Machine modifiée !");
            } catch (Exception ex) {
                erreurLabel.setText("Erreur : Données invalides.");
            }
        });

        // Supprimer une machine sélectionnée
        supprimerBtn.setOnAction(e -> {
            Machine selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                erreurLabel.setText("Sélectionnez une machine à supprimer.");
                return;
            }
            atelier.getEquipements().remove(selected);
            machinesList.remove(selected);
            tableView.getSelectionModel().clearSelection();
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            erreurLabel.setText("Machine supprimée.");
        });

        retourBtn.setOnAction(e -> {
            if (onSuccess != null) onSuccess.run();
        });

        HBox boutonsBox = new HBox(10, ajouterBtn, modifierBtn, supprimerBtn, retourBtn);

        box.getChildren().addAll(
                new Label("Machines créées :"), tableView,
                new Label("Ajouter ou modifier une machine :"),
                idField, descField, abscField, ordField, coutField,
                etatBox, boutonsBox, erreurLabel
        );

        return box;
    }
}
