package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.controller.EquipementController;
import com.mycompany.projet_fx.model.Equipement;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class EquipementView {
    private VBox root;
    private final EquipementController controller;

    public EquipementView(EquipementController controller) {
        this.controller = controller;
        createView();
    }

    private void createView() {
        root = new VBox(15);
        root.setStyle("-fx-padding: 30;");
        Label titre = new Label("Gestion des Equipements");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ListView<Equipement> eqList = new ListView<>(controller.getEquipements());
        eqList.setPrefHeight(120);

        // ... Ajout d'équipement selon besoin

        root.getChildren().addAll(titre, eqList /*, champs d'ajout, bouton ... */);
    }

    public VBox getView() { return root; }
}
