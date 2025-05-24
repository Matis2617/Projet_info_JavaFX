// OperateurFormView.java
package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.*;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class OperateurFormView {

    public static Node getOperateurForm(
            ObservableList<Operateur> operateursList,
            ObservableList<Poste> postesList,
            Atelier atelier,
            String nomFichier,
            Runnable onRetourAccueil
    ) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));

        // --- COLONNE GAUCHE : création opérateur
        VBox formBox = new VBox(12);
        formBox.setPadding(new Insets(8, 30, 8, 8));
        Label titre = new Label("Créer un opérateur");
        titre.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");
        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");
        TextField idField = new TextField();
        idField.setPromptText("Identifiant numérique");
        TextField competencesField = new TextField();
        competencesField.setPromptText("Compétences");

        ComboBox<Machine.ETAT> etatBox = new ComboBox<>();
        etatBox.getItems().addAll(Machine.ETAT.values());
        etatBox.setPromptText("État");

        Label msg = new Label();

        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> {
            try {
                String nom = nomField.getText().trim();
                String prenom = prenomField.getText().trim();
                String competences = competencesField.getText().trim();
                int idOp = Integer.parseInt(idField.getText());
                Machine.ETAT etat = etatBox.getValue();
                if (nom.isEmpty() || prenom.isEmpty() || competences.isEmpty() || etat == null) {
                    msg.setText("Tous les champs sont obligatoires !");
                    return;
                }
                // Unicité identifiant
                for (Operateur op : operateursList) {
                    if (op.getId_op() == idOp) {
                        msg.setText("Identifiant déjà utilisé !");
                        return;
                    }
                }
                Operateur op = new Operateur(nom, prenom, competences, idOp, etat);
                operateursList.add(op);
                atelier.getOperateurs().clear();
                atelier.getOperateurs().addAll(operateursList);
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                msg.setText("Opérateur ajouté !");
                nomField.clear(); prenomField.clear(); idField.clear(); competencesField.clear(); etatBox.setValue(null);
            } catch (Exception ex) {
                msg.setText("Vérifiez les champs.");
            }
        });

        Button retourBtn = new Button("Retour");
        retourBtn.setOnAction(e -> { if (onRetourAccueil != null) onRetourAccueil.run(); });

        formBox.getChildren().addAll(titre, nomField, prenomField, idField, competencesField, etatBox, ajouterBtn, retourBtn, msg);

        // --- COLONNE DROITE : liste opérateurs + association poste
        VBox rightBox = new VBox(16);
        rightBox.setPadding(new Insets(8, 8, 8, 30));
        Label listTitre = new Label("Opérateurs enregistrés");
        listTitre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TableView<Operateur> tableOperateurs = new TableView<>(operateursList);
        tableOperateurs.setPrefHeight(300);
        tableOperateurs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Operateur, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        TableColumn<Operateur, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrenom()));
        TableColumn<Operateur, Number> idCol = new TableColumn<>("Identifiant");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId_op()));
        TableColumn<Operateur, String> compCol = new TableColumn<>("Compétences");
        compCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCompetences()));
        TableColumn<Operateur, String> etatCol = new TableColumn<>("État");
        etatCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEtat().toString()));
        tableOperateurs.getColumns().addAll(nomCol, prenomCol, idCol, compCol, etatCol);

        // ---- Association opérateur <-> poste
        Label assocTitre = new Label("Associer à un poste :");
        ComboBox<Poste> comboPostes = new ComboBox<>(postesList);
        comboPostes.setPromptText("Choisir un poste");

        Button associerBtn = new Button("Associer à ce poste");
        Label infoLabel = new Label();

        associerBtn.setOnAction(e -> {
            Operateur operateur = tableOperateurs.getSelectionModel().getSelectedItem();
            Poste poste = comboPostes.getValue();
            if (operateur == null || poste == null) {
                infoLabel.setText("Sélectionnez un opérateur et un poste.");
                return;
            }
            // Vérifie que toutes les machines du poste sont disponibles
            boolean machineOccupee = poste.getMachines().stream().anyMatch(m -> m.getEtat() == Machine.ETAT.occupe);
            if (machineOccupee) {
                infoLabel.setText("Impossible : une machine du poste est occupée !");
                return;
            }
            poste.setOperateur(operateur);
            operateur.setEtat(Machine.ETAT.occupe);
            poste.getMachines().forEach(m -> {
                m.setEtat(Machine.ETAT.occupe);
                m.setOperateur(operateur);
            });
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            tableOperateurs.refresh();
            infoLabel.setText("Opérateur associé et machines occupées !");
        });

        HBox assocBox = new HBox(10, comboPostes, associerBtn, infoLabel);

        rightBox.getChildren().addAll(listTitre, tableOperateurs, assocTitre, assocBox);

        HBox mainBox = new HBox(28, formBox, new Separator(), rightBox);
        root.setCenter(mainBox);
        return root;
    }
}
