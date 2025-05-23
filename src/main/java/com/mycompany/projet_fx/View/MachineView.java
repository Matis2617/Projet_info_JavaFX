package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.controller.MachineController;
import com.mycompany.projet_fx.model.Machine;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class MachineView {
    private VBox root;
    private final MachineController controller;

    public MachineView(MachineController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");

        Label titre = new Label("Gestion des Machines");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<Machine> machineList = new ListView<>(controller.getMachines());
        machineList.setPrefHeight(180);

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
                Machine m = new Machine(id, id, desc, 0, 0, 0f, 0f, Machine.ETAT.disponible); // adapte
                controller.ajouterMachine(m);
                idField.clear(); descField.clear();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        root.getChildren().addAll(titre, machineList, idField, descField, ajouterBtn);
    }

    public VBox getView() {
        return root;
    }
}
