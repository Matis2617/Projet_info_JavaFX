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
import java.util.List;

public class GammeFormView {

    private static final Color[] couleursPostes = {
            Color.ROYALBLUE, Color.DARKORANGE, Color.FORESTGREEN, Color.DARKVIOLET, Color.DARKCYAN,
            Color.CRIMSON, Color.DARKMAGENTA, Color.GOLD, Color.MEDIUMPURPLE, Color.DARKSLATEGRAY
    };

    public static Node getGammeForm(
            Atelier atelier,
            ObservableList<Gamme> gammesList,
            ObservableList<Operation> operationsList,
            String nomFichier,
            Runnable onRetourAccueil
    ) {
        HBox root = new HBox(32);
        root.setPadding(new Insets(18));
        root.setStyle("-fx-background-color: #f4f8fb;");
        HBox.setHgrow(root, Priority.ALWAYS);

        // --- LEFT ---
        VBox leftBox = new VBox(16);
        leftBox.setPrefWidth(330);
        leftBox.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-color: #e2e2e2;");
        VBox.setVgrow(leftBox, Priority.ALWAYS);

        Label titreForm = new Label("Créer une gamme");
        titreForm.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 0 0 4 0;");

        // Table Opérations
        Label opLabel = new Label("Sélectionnez une opération :");
        TableView<Operation> tableOperations = new TableView<>(operationsList);
        tableOperations.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableOperations, Priority.ALWAYS);

        TableColumn<Operation, Number> opIdCol = new TableColumn<>("ID");
        opIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId_operation()));
        TableColumn<Operation, String> opDescCol = new TableColumn<>("Description");
        opDescCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        TableColumn<Operation, Number> opDureeCol = new TableColumn<>("Durée (h)");
        opDureeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getDuree()));
        tableOperations.getColumns().addAll(opIdCol, opDescCol, opDureeCol);

        // Table Machines
        ObservableList<Machine> machinesList = FXCollections.observableArrayList();
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) machinesList.add((Machine) eq);
        }
        ObservableList<Poste> postesList = FXCollections.observableArrayList(atelier.getPostes());

        Label machLabel = new Label("Sélectionnez une machine :");
        TableView<Machine> tableMachines = new TableView<>(machinesList);
        tableMachines.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableMachines, Priority.ALWAYS);

        TableColumn<Machine, Number> machIdCol = new TableColumn<>("ID");
        machIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRefmachine()));
        TableColumn<Machine, String> machDescCol = new TableColumn<>("Description");
        machDescCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDmachine()));
        TableColumn<Machine, String> machPosteCol = new TableColumn<>("Poste");
        machPosteCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(getPosteNameForMachine(data.getValue(), postesList)));
        TableColumn<Machine, Color> machColorCol = new TableColumn<>("Couleur Poste");
        machColorCol.setCellFactory(col -> new TableCell<Machine, Color>() {
            @Override
            protected void updateItem(Color color, boolean empty) {
                super.updateItem(color, empty);
                setText(null);
                if (empty || color == null) {
                    setGraphic(null);
                } else {
                    Rectangle r = new Rectangle(16, 16, color);
                    setGraphic(r);
                }
            }
        });
        machColorCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(getColorForMachine(data.getValue(), postesList)));
        tableMachines.getColumns().addAll(machIdCol, machDescCol, machPosteCol, machColorCol);

        // Formulaire référence gamme
        Label refGammeLabel = new Label("Référence de la gamme :");
        TextField refGammeInput = new TextField();

        Button creerBtn = new Button("Créer la gamme");
        creerBtn.setStyle("-fx-font-weight: bold; -fx-background-color: #8fd14f; -fx-padding: 5 24 5 24;");
        Label creerMsg = new Label();
        creerMsg.setStyle("-fx-text-fill: #1a8838;");

        creerBtn.setOnAction(e -> {
            String ref = refGammeInput.getText().trim();
            Operation op = tableOperations.getSelectionModel().getSelectedItem();
            Machine mach = tableMachines.getSelectionModel().getSelectedItem();
            if (ref.isEmpty()) {
                creerMsg.setText("Référence obligatoire !");
                return;
            }
            if (op == null || mach == null) {
                creerMsg.setText("Sélectionnez une opération ET une machine !");
                return;
            }
            Gamme gamme = new Gamme(new ArrayList<>());
            gamme.setRefGamme(ref);
            gamme.getOperations().add(op);
            gamme.getListeEquipements().add(mach);
            gammesList.add(gamme);
            atelier.setGammes(new ArrayList<>(gammesList));
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            creerMsg.setText("Gamme créée !");
            refGammeInput.clear();
            tableOperations.getSelectionModel().clearSelection();
            tableMachines.getSelectionModel().clearSelection();
        });

        Button retourBtn = new Button("Retour Accueil");
        retourBtn.setStyle("-fx-background-color: #e4e4e4; -fx-font-weight: bold;");
        retourBtn.setMaxWidth(Double.MAX_VALUE);
        retourBtn.setOnAction(e -> { if (onRetourAccueil != null) onRetourAccueil.run(); });

        leftBox.getChildren().addAll(
                titreForm,
                opLabel, tableOperations,
                machLabel, tableMachines,
                refGammeLabel, refGammeInput,
                creerBtn, creerMsg, retourBtn
        );
        VBox.setVgrow(tableOperations, Priority.ALWAYS);
        VBox.setVgrow(tableMachines, Priority.ALWAYS);

        // --- RIGHT ---
        VBox rightBox = new VBox(18);
        rightBox.setPrefWidth(480);
        rightBox.setStyle("-fx-background-color: #fff; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-color: #e2e2e2;");
        VBox.setVgrow(rightBox, Priority.ALWAYS);

        Label gammesLbl = new Label("Gammes créées");
        gammesLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 2 0 2 0;");

        TableView<Gamme> gammeTable = new TableView<>(gammesList);
        gammeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        gammeTable.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(gammeTable, Priority.ALWAYS);

        TableColumn<Gamme, String> gammeRefCol = new TableColumn<>("Référence");
        gammeRefCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRefGamme() == null ? "" : data.getValue().getRefGamme()));
        TableColumn<Gamme, String> gammeOpCol = new TableColumn<>("Opération");
        gammeOpCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getOperations().isEmpty() ? "-" : data.getValue().getOperations().get(0).getDescription()
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

        // Détails
        Label detailsTitre = new Label("Détails de la gamme sélectionnée");
        detailsTitre.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-underline: true;");

        VBox gammeDetailsBox = new VBox(7);
        gammeDetailsBox.setStyle("-fx-background-color: #f7fafc; -fx-padding: 16; -fx-border-color: #d7d7d7; -fx-border-radius: 8; -fx-background-radius: 8;");
        gammeDetailsBox.setMinWidth(370);
        VBox.setVgrow(gammeDetailsBox, Priority.ALWAYS);

        // --- Bouton Modifier ---
        Button modifierBtn = new Button("Modifier cette gamme");
        modifierBtn.setStyle("-fx-background-color: #ffca3a; -fx-font-weight: bold; -fx-padding: 7 18 7 18;");
        modifierBtn.setDisable(true);

        gammeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, gamme) -> {
            gammeDetailsBox.getChildren().clear();
            modifierBtn.setDisable(gamme == null);
            if (gamme != null) {
                Label ref = new Label("Référence : " + gamme.getRefGamme());
                ref.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #2b3a67;");
                Separator sep1 = new Separator();

                Label ops = new Label("Opérations associées :");
                ops.setStyle("-fx-font-weight: bold;");
                VBox opsBox = new VBox(2);
                for (Operation op : gamme.getOperations()) {
                    Label l = new Label("• " + op.getId_operation() + " — " + op.getDescription() + " (" + op.getDuree() + "h)");
                    l.setStyle("-fx-padding: 0 0 0 8;");
                    opsBox.getChildren().add(l);
                }
                Separator sep2 = new Separator();

                Label eqs = new Label("Machines / Équipements :");
                eqs.setStyle("-fx-font-weight: bold;");
                VBox eqBox = new VBox(2);
                for (Equipement eq : gamme.getListeEquipements()) {
                    String txt = (eq instanceof Machine)
                            ? "• " + ((Machine) eq).getDmachine() + " (coût horaire : " + ((Machine) eq).getC() + " €)"
                            : "• " + eq.affiche();
                    Label l = new Label(txt);
                    l.setStyle("-fx-padding: 0 0 0 8;");
                    eqBox.getChildren().add(l);
                }
                Separator sep3 = new Separator();

                Label cout = new Label("Coût total : " + String.format("%.2f", gamme.coutGamme()) + " €");
                cout.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c974b;");
                Label duree = new Label("Durée totale : " + String.format("%.2f", gamme.dureeGamme()) + " h");
                duree.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c974b;");

                gammeDetailsBox.getChildren().addAll(ref, sep1, ops, opsBox, sep2, eqs, eqBox, sep3, cout, duree, modifierBtn);
            } else {
                gammeDetailsBox.getChildren().add(new Label("Sélectionnez une gamme pour voir les détails."));
            }
        });

        modifierBtn.setOnAction(e -> {
            Gamme selectedGamme = gammeTable.getSelectionModel().getSelectedItem();
            if (selectedGamme == null) return;
            afficherPopupModificationGamme(selectedGamme, atelier, operationsList, machinesList, gammesList, nomFichier);
        });

        rightBox.getChildren().addAll(
                gammesLbl, gammeTable,
                detailsTitre, gammeDetailsBox
        );
        VBox.setVgrow(gammeTable, Priority.ALWAYS);
        VBox.setVgrow(gammeDetailsBox, Priority.ALWAYS);

        HBox.setHgrow(leftBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        root.getChildren().addAll(leftBox, new Separator(javafx.geometry.Orientation.VERTICAL), rightBox);
        HBox.setHgrow(root, Priority.ALWAYS);

        return root;
    }

    private static String getPosteNameForMachine(Machine m, List<Poste> postesList) {
        for (Poste poste : postesList) {
            if (poste.getMachines().contains(m)) return poste.getNomPoste();
        }
        return "-";
    }

    private static Color getColorForMachine(Machine m, List<Poste> postesList) {
        for (int i = 0; i < postesList.size(); i++) {
            Poste poste = postesList.get(i);
            if (poste.getMachines().contains(m)) {
                return couleursPostes[i % couleursPostes.length];
            }
        }
        return Color.LIGHTGRAY;
    }

    private static void afficherPopupModificationGamme(
            Gamme gamme,
            Atelier atelier,
            ObservableList<Operation> operationsList,
            ObservableList<Machine> machinesList,
            ObservableList<Gamme> gammesList,
            String nomFichier
    ) {
        Dialog<Void> modifDlg = new Dialog<>();
        modifDlg.setTitle("Modifier la gamme");
        VBox modifBox = new VBox(10);
        modifBox.setPadding(new Insets(10));

        TextField refField = new TextField(gamme.getRefGamme());

        TableView<Operation> opEdit = new TableView<>(operationsList);
        opEdit.setPrefHeight(120);
        TableColumn<Operation, Number> opIdCol = new TableColumn<>("ID");
        opIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId_operation()));
        TableColumn<Operation, String> opDescCol = new TableColumn<>("Description");
        opDescCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        opEdit.getColumns().addAll(opIdCol, opDescCol);

        if (!gamme.getOperations().isEmpty())
            opEdit.getSelectionModel().select(gamme.getOperations().get(0));

        TableView<Machine> machEdit = new TableView<>(machinesList);
        machEdit.setPrefHeight(120);
        TableColumn<Machine, Number> machIdCol = new TableColumn<>("ID");
        machIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRefmachine()));
        TableColumn<Machine, String> machDescCol = new TableColumn<>("Description");
        machDescCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDmachine()));
        machEdit.getColumns().addAll(machIdCol, machDescCol);

        if (!gamme.getListeEquipements().isEmpty() && gamme.getListeEquipements().get(0) instanceof Machine)
            machEdit.getSelectionModel().select((Machine) gamme.getListeEquipements().get(0));

        Button valider = new Button("Valider");
        valider.setStyle("-fx-background-color: #8fd14f; -fx-font-weight: bold;");
        Label modifMsg = new Label();
        modifMsg.setStyle("-fx-text-fill: green;");

        valider.setOnAction(ev -> {
            String ref = refField.getText().trim();
            Operation op = opEdit.getSelectionModel().getSelectedItem();
            Machine mach = machEdit.getSelectionModel().getSelectedItem();
            if (ref.isEmpty()) {
                modifMsg.setText("Référence obligatoire !");
                return;
            }
            if (op == null || mach == null) {
                modifMsg.setText("Sélectionnez une opération et une machine !");
                return;
            }
            gamme.setRefGamme(ref);
            gamme.getOperations().clear();
            gamme.getOperations().add(op);
            gamme.getListeEquipements().clear();
            gamme.getListeEquipements().add(mach);
            modifMsg.setText("Gamme modifiée !");
            AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
            modifDlg.close();
        });

        modifBox.getChildren().addAll(
                new Label("Référence :"), refField,
                new Label("Opération :"), opEdit,
                new Label("Machine :"), machEdit,
                valider, modifMsg
        );
        modifDlg.getDialogPane().setContent(modifBox);
        modifDlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        modifDlg.showAndWait();
    }
}
