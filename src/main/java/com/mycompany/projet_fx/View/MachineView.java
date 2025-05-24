package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.Model.Operateur;
import com.mycompany.projet_fx.controller.OperateurController;
import com.mycompany.projet_fx.controller.MachineController;
import com.mycompany.projet_fx.Model.Machine;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class MachineView {
    private VBox root;
    private final MachineController machineController;
    private final OperateurController operateurController;

    public MachineView(MachineController machineController, OperateurController operateurController) {
        this.machineController = machineController;
        this.operateurController = operateurController;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");

        Label titre = new Label("Gestion des Machines");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<Machine> machineList = new ListView<>(machineController.getMachines());
        machineList.setPrefHeight(180);
        
        ListView<Operateur> operateurList = new ListView<>(operateurController.getOperateurs());
        operateurList.setPrefHeight(180);

        TextField idField = new TextField();
        idField.setPromptText("Identifiant machine");
        TextField descField = new TextField();
        descField.setPromptText("Description");

        // ... autres champs comme abscisse, ordonnée, coût, etc.

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            try {
                // Récupère les valeurs et crée la machine (complète selon tes besoins)
                int id = Integer.parseInt(idField.getText());
                String desc = descField.getText();
                // Ajouter abscisse, ordonnée, coût, etc.
                Machine m = new Machine(id, desc, 0, 0, 0f, Machine.ETAT.disponible,id); // adapte
                machineController.ajouterMachine(m);
                idField.clear();
                descField.clear();
            } catch (Exception ex) { ex.printStackTrace(); }
        });
        
        Button lierOperateurBtn = new Button("Lier Opérateur");
        lierOperateurBtn.setOnAction(e -> {
            Machine machineSelectionnee = machineList.getSelectionModel().getSelectedItem();
            Operateur operateurSelectionne = operateurList.getSelectionModel().getSelectedItem();

            if (machineSelectionnee != null && operateurSelectionne != null) {
                machineController.lierOperateur(machineSelectionnee, operateurSelectionne);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune sélection");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une machine et un opérateur à lier.");
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(titre, machineList, idField, descField, lierOperateurBtn);
        }

    public VBox getView() {
        return root;
    }
}
