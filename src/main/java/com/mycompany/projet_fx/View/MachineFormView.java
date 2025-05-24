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

public class MachineFormView {

    public static VBox getMachineForm(Atelier atelier, String nomFichier, Runnable onSuccess) {
        VBox box = new VBox(16);
        box.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // ----------- TABLEAU DES MACHINES -----------
        ObservableList<Machine> machinesList = FXCollections.observableArrayList();
        // Synchronise avec les équipements de l’atelier
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) machinesList.add((Machine) eq);
        }
        ListView<Machine> listView = new ListView<>(machinesList);
        listView.setPrefHeight(170);

        // ----------- FORMULAIRE D'AJOUT/EDITION -----------
        TextField idField = new TextField();
        idField.setPromptText("Identifiant");

        TextField descField = new TextField();
        descField.setPromptText("Description");

        TextField abscField = new TextField();
        abscField.setPromptText("Abscisse (0-9)");

        TextField ordField = new TextField();
        ordField.setPromptText("Ordonnée (0-9)");

        TextField coutField = new TextField();
        coutField.setPromptText("Coût");

        ComboBox<Machine.ETAT> etatBox = new ComboBox<>();
        etatBox.getItems().addAll(Machine.ETAT.values());
        etatBox.setPromptText("État");

        Label erreurLabel = new Label();
        erreurLabel.setStyle("-fx-text-fill: red;");

        // ----------- BOUTON AJOUTER -----------
        Button ajouterBtn = new Button("Ajouter la machine");
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

                // Vérification unicité
                boolean existe = false;
                for (Equipement eq : atelier.getEquipements()) {
                    if (eq instanceof Machine) {
                        Machine m = (Machine) eq;
                        if (m.getRefmachine() == id) {
                            existe = true;
                            erreurLabel.setText("Erreur : Identifiant déjà utilisé !");
                            break;
                        }
                        if (m.getAbscisse() == absc && m.getOrdonnee() == ord) {
                            existe = true;
                            erreurLabel.setText("Erreur : Coordonnées déjà utilisées !");
                            break;
                        }
                    }
                }
                if (!existe) {
                    Machine m = new Machine(
                            atelier.getEquipements().size() + 1, // id_equipement (unique)
                            id, desc, absc, ord, cout, etat
                    );
                    atelier.getEquipements().add(m);
                    machinesList.add(m);
                    AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                    erreurLabel.setText("Machine ajoutée !");
                    // reset form
                    idField.clear(); descField.clear(); abscField.clear();
                    ordField.clear(); coutField.clear(); etatBox.getSelectionModel().clearSelection();
                    if (onSuccess != null) onSuccess.run();
                }
            } catch (Exception ex) {
                erreurLabel.setText("Erreur : Données invalides.");
            }
        });

        // ----------- BOUTON MODIFIER -----------
        Button modifierBtn = new Button("Modifier");
        modifierBtn.setOnAction(ev -> {
            Machine selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Popup édition
                modifierMachine(selected, atelier, nomFichier, () -> {
                    listView.refresh();
                    AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                });
            }
        });

        // ----------- BOUTON SUPPRIMER -----------
        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setOnAction(ev -> {
            Machine selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                atelier.getEquipements().remove(selected);
                machinesList.remove(selected);
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                erreurLabel.setText("Machine supprimée.");
            }
        });

        Button retourBtn = new Button("Annuler");
        retourBtn.setOnAction(e -> { if (onSuccess != null) onSuccess.run(); });

        HBox boutonsBox = new HBox(10, ajouterBtn, modifierBtn, supprimerBtn, retourBtn);

        box.getChildren().addAll(
                new Label("Machines créées :"), listView,
                new Label("Ajouter une machine :"),
                idField, descField, abscField, ordField, coutField,
                etatBox, boutonsBox, erreurLabel
        );
        return box;
    }

    public static void modifierMachine(Machine machine, Atelier atelier, String nomFichier, Runnable onRefresh) {
        Dialog<Void> dlg = new Dialog<>();
        dlg.setTitle("Modifier la machine");
        VBox box = new VBox(10);
        box.setPadding(new Insets(8));

        TextField descField = new TextField(machine.getDmachine());
        TextField abscField = new TextField(String.valueOf(machine.getAbscisse()));
        TextField ordField = new TextField(String.valueOf(machine.getOrdonnee()));
        TextField coutField = new TextField(String.valueOf(machine.getC()));
        ComboBox<Machine.ETAT> etatBox = new ComboBox<>();
        etatBox.getItems().addAll(Machine.ETAT.values());
        etatBox.setValue(machine.getEtat());

        Button valider = new Button("Valider");
        valider.setOnAction(ev -> {
            machine.setDmachine(descField.getText());
            machine.setAbscisse(Integer.parseInt(abscField.getText()));
            machine.setOrdonnee(Integer.parseInt(ordField.getText()));
            machine.setC(Float.parseFloat(coutField.getText()));
            machine.setEtat(etatBox.getValue());
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            dlg.setResult(null);
            dlg.close();
            if (onRefresh != null) onRefresh.run();
        });

        box.getChildren().addAll(
                new Label("Description :"), descField,
                new Label("Abscisse :"), abscField,
                new Label("Ordonnée :"), ordField,
                new Label("Coût :"), coutField,
                new Label("État :"), etatBox,
                valider
        );
        dlg.getDialogPane().setContent(box);
        dlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dlg.showAndWait();
    }
}
