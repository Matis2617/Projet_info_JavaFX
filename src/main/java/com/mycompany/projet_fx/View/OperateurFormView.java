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
            ObservableList<Poste> postesList, // Ajout ici
            Atelier atelier,
            String nomFichier,
            Runnable onRetourAccueil
    ) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));

        VBox formBox = new VBox(12);
        formBox.setPadding(new Insets(8, 26, 8, 8));
        formBox.setStyle("-fx-background-color: #f8fbff; -fx-border-radius: 13; -fx-background-radius: 13; -fx-border-color: #dde3ec;");

        Label titre = new Label("Ajouter un opérateur");
        titre.setStyle("-fx-font-size: 19px; -fx-font-weight: bold;");

        TextField nomField = new TextField(); nomField.setPromptText("Nom");
        TextField prenomField = new TextField(); prenomField.setPromptText("Prénom");
        TextField idField = new TextField(); idField.setPromptText("Identifiant numérique");
        TextField compField = new TextField(); compField.setPromptText("Compétences");

        ComboBox<Machine.ETAT> etatBox = new ComboBox<>();
        etatBox.getItems().addAll(Machine.ETAT.values());
        etatBox.setPromptText("État");

        Label msg = new Label();
        msg.setStyle("-fx-text-fill: #854a3d; -fx-font-weight: bold;");

        Button ajouterBtn = new Button("Ajouter l'opérateur");
        ajouterBtn.setStyle("-fx-background-color: #274472; -fx-text-fill: white; -fx-background-radius: 8;");

        ajouterBtn.setOnAction(e -> {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String competences = compField.getText().trim();
            String idStr = idField.getText().trim();
            Machine.ETAT etat = etatBox.getValue();
            if (nom.isEmpty() || prenom.isEmpty() || competences.isEmpty() || idStr.isEmpty() || etat == null) {
                msg.setText("Remplis tous les champs !");
                return;
            }
            int id;
            try { id = Integer.parseInt(idStr); }
            catch (Exception ex) { msg.setText("Identifiant non valide."); return; }

            boolean existe = operateursList.stream().anyMatch(o -> o.getId_op() == id);
            if (existe) { msg.setText("Cet identifiant existe déjà !"); return; }

            Operateur op = new Operateur(nom, prenom, competences, id, etat);
            operateursList.add(op);
            if (atelier != null) {
                atelier.getOperateurs().clear();
                atelier.getOperateurs().addAll(operateursList);
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            }
            msg.setText("Opérateur ajouté !");
            nomField.clear(); prenomField.clear(); compField.clear(); idField.clear(); etatBox.setValue(null);
        });

        Button retourBtn = new Button("Retour");
        retourBtn.setStyle("-fx-background-color: #d6e0f5; -fx-text-fill: #274472; -fx-font-weight: bold; -fx-background-radius: 8;");
        retourBtn.setOnAction(e -> { if (onRetourAccueil != null) onRetourAccueil.run(); });

        formBox.getChildren().addAll(titre, nomField, prenomField, idField, compField, etatBox, ajouterBtn, retourBtn, msg);
        formBox.setPrefWidth(400);

        // Tableau des opérateurs
        VBox rightBox = new VBox(12);
        rightBox.setPadding(new Insets(10, 8, 8, 32));
        Label listeTitre = new Label("Liste des opérateurs");
        listeTitre.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");
        TableView<Operateur> tableOp = new TableView<>(operateursList);
        tableOp.setPrefHeight(320);
        tableOp.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        tableOp.getColumns().addAll(nomCol, prenomCol, idCol, compCol, etatCol);

        // Ajout : Association opérateur <-> poste
        Label assocTitre = new Label("Associer à un poste :");
        assocTitre.setStyle("-fx-font-weight: bold;");
        ComboBox<Poste> comboPostes = new ComboBox<>(postesList);
        comboPostes.setPromptText("Choisir un poste");
        Button associerBtn = new Button("Associer à ce poste");
        Label infoLabel = new Label();

        associerBtn.setOnAction(e -> {
            Operateur operateur = tableOp.getSelectionModel().getSelectedItem();
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
            tableOp.refresh();
            infoLabel.setText("Opérateur associé et machines occupées !");
        });

        Button supprimerBtn = new Button("Supprimer l'opérateur");
supprimerBtn.setStyle("-fx-background-color: #ffc5c2; -fx-text-fill: #b22915; -fx-background-radius: 8; -fx-font-weight: bold;");
supprimerBtn.setOnAction(e -> {
    Operateur selected = tableOp.getSelectionModel().getSelectedItem();
    if (selected != null) {
        operateursList.remove(selected);
        if (atelier != null) {
            atelier.getOperateurs().clear();
            atelier.getOperateurs().addAll(operateursList);
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
        }
    }
});

Button modifierBtn = new Button("Modifier");
modifierBtn.setStyle("-fx-background-color: #a8e0b6; -fx-text-fill: #14562d; -fx-background-radius: 8; -fx-font-weight: bold;");
modifierBtn.setOnAction(e -> {
    Operateur selected = tableOp.getSelectionModel().getSelectedItem();
    if (selected != null) {
        nomField.setText(selected.getNom());
        prenomField.setText(selected.getPrenom());
        idField.setText(String.valueOf(selected.getId_op()));
        compField.setText(selected.getCompetences());
        etatBox.setValue(selected.getEtat());
        // Associer à un poste
        ChoiceDialog<Poste> posteDialog = new ChoiceDialog<>(null, atelier.getPostes());
        posteDialog.setTitle("Associer à un poste");
        posteDialog.setHeaderText("Choisis un poste pour l'opérateur");
        posteDialog.setContentText("Poste :");
        Optional<Poste> res = posteDialog.showAndWait();
        res.ifPresent(poste -> {
            boolean posteLibre = (poste.getOperateur() == null);
            boolean machinesLibres = poste.getMachines().stream().allMatch(m -> m.getEtat() == Machine.ETAT.disponible);
            if (posteLibre && machinesLibres) {
                selected.setNom(nomField.getText());
                selected.setPrenom(prenomField.getText());
                selected.setCompetences(compField.getText());
                selected.setEtat(etatBox.getValue());
                poste.setOperateur(selected);
                for (Machine m : poste.getMachines()) {
                    m.setEtat(Machine.ETAT.occupe);
                    m.setOperateur(selected);
                }
                selected.setEtat(Machine.ETAT.occupe);
                msg.setText("Opérateur mis à jour et associé au poste " + poste.getNomPoste());
                AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                tableOp.refresh();
            } else {
                msg.setText("Le poste ou une machine est déjà occupé !");
            }
        });
    } else {
        msg.setText("Sélectionne un opérateur à modifier.");
    }
});
rightBox.getChildren().addAll(listeTitre, tableOp, new HBox(10, supprimerBtn, modifierBtn));


        rightBox.getChildren().addAll(listeTitre, tableOp,
            assocTitre, new HBox(10, comboPostes, associerBtn, infoLabel), supprimerBtn);

        HBox hMain = new HBox(24, formBox, new Separator(), rightBox);
        hMain.setFillHeight(true);
        HBox.setHgrow(formBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        root.setCenter(hMain);
        return root;
    }
}
