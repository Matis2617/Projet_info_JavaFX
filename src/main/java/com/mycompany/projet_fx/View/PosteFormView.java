package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Model.Equipement;
import com.mycompany.projet_fx.Model.Machine;
import com.mycompany.projet_fx.Model.Poste;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class PosteFormView {

    // Palette de couleurs pour affichage postes (utile si tu colories dans la vue)
    private static final Color[] couleursPostes = {
            Color.ROYALBLUE, Color.DARKORANGE, Color.FORESTGREEN, Color.DARKVIOLET, Color.DARKCYAN,
            Color.CRIMSON, Color.DARKMAGENTA, Color.GOLD, Color.MEDIUMPURPLE, Color.DARKSLATEGRAY
    };

    // Ici, le callback est appelé après création (ex : retourner à l'accueil ou rafraîchir)
    public static VBox getPosteForm(
            Atelier atelier,
            String nomFichier,
            Runnable onPosteCree // callback pour retour ou refresh
    ) {
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 30; -fx-alignment: center;");

        // Si aucune machine, avertir
        if (atelier.getEquipements().stream().noneMatch(eq -> eq instanceof Machine)) {
            vbox.getChildren().add(new Label("Aucune machine créée. Créez des machines avant de créer un poste."));
            return vbox;
        }

        // Sélection multiple des machines
        Label label = new Label("Sélectionnez les machines pour créer un poste :");
        ListView<Machine> machineListView = new ListView<>();
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) {
                machineListView.getItems().add((Machine) eq);
            }
        }
        machineListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        machineListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Machine m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty || m == null ? "" : m.getDmachine());
            }
        });

        TextField posteDescField = new TextField();
        posteDescField.setPromptText("Description du poste");

        Button creerBtn = new Button("Créer le poste");
        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        creerBtn.setOnAction(e -> {
            var selectedMachines = machineListView.getSelectionModel().getSelectedItems();
            String desc = posteDescField.getText().isBlank() ? "Poste sans nom" : posteDescField.getText();
            if (selectedMachines == null || selectedMachines.isEmpty()) {
                message.setText("Sélectionnez au moins une machine.");
                return;
            }
            // Crée le poste avec refposte auto-incrémenté
            ArrayList<Machine> machinesForPoste = new ArrayList<>(selectedMachines);
            Poste nouveauPoste = new Poste(
                    atelier.getPostes().size() + 1, desc, machinesForPoste, atelier.getPostes().size() + 1000);
            atelier.getPostes().add(nouveauPoste);

            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            System.out.println("[AJOUT] Poste ajouté. Total: " + atelier.getPostes().size());
            message.setText("Poste créé !");
            if (onPosteCree != null) onPosteCree.run();
        });

        // Affiche les postes déjà créés
        VBox postesBox = new VBox(10);
        postesBox.getChildren().add(new Label("Postes déjà créés :"));
        for (Poste p : atelier.getPostes()) {
            String machinesStr = String.join(", ",
                    p.getMachines().stream().map(Machine::getDmachine).toArray(String[]::new));
            postesBox.getChildren().add(new Label("• " + p.getDposte() + " : " + machinesStr));
        }

        vbox.getChildren().addAll(label, machineListView, posteDescField, creerBtn, message, new Separator(), postesBox);
        return vbox;
    }
}
