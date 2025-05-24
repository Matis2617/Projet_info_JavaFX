package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.*;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class GammeFormView {

    public static Node getGammeForm(
            Atelier atelier,
            ObservableList<Gamme> gammesList,
            ObservableList<Operation> operationsList,
            String nomFichier,
            Runnable onRetourAccueil
    ) {
        HBox root = new HBox(30);
        root.setPadding(new Insets(20, 35, 20, 35));

        // ----------- TABLEAU DES OPERATIONS -----------
        TableView<Operation> opTable = new TableView<>(operationsList);
        opTable.setPrefHeight(180);
        opTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        opTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn<Operation, Number> opIdCol = new TableColumn<>("ID");
        opIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId_operation()));
        TableColumn<Operation, String> opDescCol = new TableColumn<>("Description");
        opDescCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        TableColumn<Operation, Number> opDureeCol = new TableColumn<>("Durée (h)");
        opDureeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getDuree()));
        opTable.getColumns().addAll(opIdCol, opDescCol, opDureeCol);

        // ----------- TABLEAU DES MACHINES -----------
        ObservableList<Machine> machinesList = FXCollections.observableArrayList();
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) machinesList.add((Machine) eq);
        }

        TableView<Machine> machTable = new TableView<>(machinesList);
        machTable.setPrefHeight(180);
        machTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        machTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn<Machine, Number> machIdCol = new TableColumn<>("ID");
        machIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRefmachine()));
        TableColumn<Machine, String> machDescCol = new TableColumn<>("Description");
        machDescCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDmachine()));
        TableColumn<Machine, String> machPosteCol = new TableColumn<>("Poste");
        machPosteCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                getPosteNameForMachine(data.getValue(), atelier.getPostes())
        ));
        TableColumn<Machine, Color> machColorCol = new TableColumn<>("Couleur");
        machColorCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Color color, boolean empty) {
                super.updateItem(color, empty);
                setText(null);
                if (empty || color == null) setGraphic(null);
                else setGraphic(new Rectangle(16, 16, color));
            }
        });
        machColorCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(
                getColorForMachine(data.getValue(), atelier.getPostes())
        ));
        machTable.getColumns().addAll(machIdCol, machDescCol, machPosteCol, machColorCol);

        // ----------- TABLEAU DES GAMMES -----------
        TableView<Gamme> gammeTable = new TableView<>(gammesList);
        gammeTable.setPrefHeight(180);
        gammeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        gammeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn<Gamme, String> gammeRefCol = new TableColumn<>("Réf");
        gammeRefCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getRefGamme() == null ? "" : data.getValue().getRefGamme()));

        TableColumn<Gamme, String> gammeOpCol = new TableColumn<>("Opération");
        gammeOpCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getOperations().isEmpty()
                        ? "-"
                        : data.getValue().getOperations().get(0).getDescription()
        ));

        TableColumn<Gamme, String> gammeMachCol = new TableColumn<>("Machine");
        gammeMachCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getListeEquipements().isEmpty()
                        ? "-"
                        : (data.getValue().getListeEquipements().get(0) instanceof Machine
                        ? ((Machine) data.getValue().getListeEquipements().get(0)).getDmachine()
                        : data.getValue().getListeEquipements().get(0).affiche())
        ));

        gammeTable.getColumns().addAll(gammeRefCol, gammeOpCol, gammeMachCol);

        // ----------- INFOS DETAILLEES GAMME -----------
        Label infoGamme = new Label("Sélectionnez une gamme pour voir les détails.");
        infoGamme.setStyle("-fx-background-color: #f6f6f6; -fx-font-size: 15px; -fx-padding: 9;");
        infoGamme.setMinWidth(320);
        infoGamme.setWrapText(true);

        gammeTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Référence : ").append(selected.getRefGamme()).append("\n");
                sb.append("Opérations :\n");
                for (Operation op : selected.getOperations()) {
                    sb.append("  - [#").append(op.getId_operation()).append("] ").append(op.getDescription()).append(" (")
                            .append(op.getDuree()).append("h)\n");
                }
                sb.append("Machines/Eq. utilisées :\n");
                for (Equipement eq : selected.getListeEquipements()) {
                    if (eq instanceof Machine) {
                        Machine m = (Machine) eq;
                        sb.append("  - Machine: ").append(m.getDmachine());
                        sb.append(" [poste: ").append(getPosteNameForMachine(m, atelier.getPostes())).append("]");
                        sb.append(", coût horaire: ").append(m.getC()).append(" €\n");
                    } else {
                        sb.append("  - ").append(eq.affiche()).append("\n");
                    }
                }
                sb.append("\nCoût total de la gamme : ").append(selected.coutGamme()).append(" €");
                sb.append("\nDurée totale de la gamme : ").append(selected.dureeGamme()).append(" h");
                infoGamme.setText(sb.toString());
            } else {
                infoGamme.setText("Sélectionnez une gamme pour voir les détails.");
            }
        });

        // ----------- CREATION GAMME -----------
        TextField refGammeInput = new TextField();
        refGammeInput.setPromptText("Référence gamme (obligatoire)");

        Button creerBtn = new Button("Créer la gamme");
        creerBtn.setStyle("-fx-font-weight: bold; -fx-background-color: #8fd14f;");
        Label creerMsg = new Label();
        creerMsg.setStyle("-fx-text-fill: green;");

        creerBtn.setOnAction(e -> {
            String ref = refGammeInput.getText().trim();
            Operation op = opTable.getSelectionModel().getSelectedItem();
            Machine m = machTable.getSelectionModel().getSelectedItem();
            if (ref.isEmpty()) {
                creerMsg.setText("Référence obligatoire !");
                return;
            }
            if (op == null || m == null) {
                creerMsg.setText("Sélectionnez une opération et une machine !");
                return;
            }
            Gamme gamme = new Gamme();
            gamme.setRefGamme(ref);
            ArrayList<Operation> ops = new ArrayList<>();
            ops.add(op);
            ArrayList<Equipement> eqs = new ArrayList<>();
            eqs.add(m);
            gamme.setOperations(ops);
            gamme.setListeEquipements(eqs);

            gammesList.add(gamme);
            atelier.setGammes(new ArrayList<>(gammesList));
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);

            creerMsg.setText("Gamme créée !");
            refGammeInput.clear();
            opTable.getSelectionModel().clearSelection();
            machTable.getSelectionModel().clearSelection();
        });

        Button retourBtn = new Button("Retour Accueil");
        retourBtn.setOnAction(e -> { if (onRetourAccueil != null) onRetourAccueil.run(); });

        // ----------- LAYOUTS -----------
        VBox vboxForm = new VBox(14,
                new Label("Sélectionnez une opération :"), opTable,
                new Label("Sélectionnez une machine :"), machTable,
                new Label("Référence de la gamme :"), refGammeInput, creerBtn, creerMsg, retourBtn
        );
        vboxForm.setPrefWidth(370);

        VBox vboxListe = new VBox(15,
                new Label("Gammes créées :"), gammeTable, infoGamme
        );
        vboxListe.setPrefWidth(420);

        root.getChildren().addAll(vboxForm, new Separator(javafx.geometry.Orientation.VERTICAL), vboxListe);
        return root;
    }

    // Utilitaires couleurs et poste pour affichage machine
    private static String getPosteNameForMachine(Machine m, java.util.List<Poste> postesList) {
        for (Poste poste : postesList) {
            if (poste.getMachines().contains(m)) return poste.getNomPoste();
        }
        return "-";
    }

    private static Color getColorForMachine(Machine m, java.util.List<Poste> postesList) {
        Color[] couleurs = {
            Color.ROYALBLUE, Color.DARKORANGE, Color.FORESTGREEN, Color.DARKVIOLET, Color.DARKCYAN,
            Color.CRIMSON, Color.DARKMAGENTA, Color.GOLD, Color.MEDIUMPURPLE, Color.DARKSLATEGRAY
        };
        for (int i = 0; i < postesList.size(); i++) {
            Poste poste = postesList.get(i);
            if (poste.getMachines().contains(m)) {
                return couleurs[i % couleurs.length];
            }
        }
        return Color.LIGHTGRAY;
    }
}
