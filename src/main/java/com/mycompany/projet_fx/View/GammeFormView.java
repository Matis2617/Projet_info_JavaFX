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

    // Palette de couleurs pour postes (doit être identique partout !)
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
        root.setPadding(new Insets(22, 38, 22, 38));

        // --- Section gauche : Sélection opération/machine ---
        VBox leftBox = new VBox(18);
        leftBox.setPrefWidth(350);

        Label titreForm = new Label("Créer une gamme");
        titreForm.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Tableau opérations
        Label opLabel = new Label("Sélectionnez une opération :");
        TableView<Operation> tableOperations = new TableView<>(operationsList);
        tableOperations.setPrefHeight(150);
        tableOperations.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Operation, Number> opIdCol = new TableColumn<>("ID");
        opIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId_operation()));
        TableColumn<Operation, String> opDescCol = new TableColumn<>("Description");
        opDescCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        TableColumn<Operation, Number> opDureeCol = new TableColumn<>("Durée (h)");
        opDureeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getDuree()));
        tableOperations.getColumns().addAll(opIdCol, opDescCol, opDureeCol);

        // Tableau machines
        ObservableList<Machine> machinesList = FXCollections.observableArrayList();
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) machinesList.add((Machine) eq);
        }

        // Pour les postes (pour trouver couleur et nom de poste)
        ObservableList<Poste> postesList = FXCollections.observableArrayList(atelier.getPostes());

        Label machLabel = new Label("Sélectionnez une machine :");
        TableView<Machine> tableMachines = new TableView<>(machinesList);
        tableMachines.setPrefHeight(150);
        tableMachines.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        // Champ référence
        Label refGammeLabel = new Label("Référence de la gamme :");
        TextField refGammeInput = new TextField();

        // Boutons
        Button creerBtn = new Button("Créer la gamme");
        creerBtn.setStyle("-fx-font-weight: bold; -fx-background-color: #8fd14f;");
        Label creerMsg = new Label();
        creerMsg.setStyle("-fx-text-fill: green;");

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
        retourBtn.setOnAction(e -> { if (onRetourAccueil != null) onRetourAccueil.run(); });

        leftBox.getChildren().addAll(
                titreForm,
                opLabel, tableOperations,
                machLabel, tableMachines,
                refGammeLabel, refGammeInput,
                creerBtn, creerMsg, retourBtn
        );

        // --- Section droite : Gammes créées et détails ---
        VBox rightBox = new VBox(14);
        rightBox.setPrefWidth(440);

        Label gammesLbl = new Label("Gammes créées");
        gammesLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<Gamme> gammeTable = new TableView<>(gammesList);
        gammeTable.setPrefHeight(210);
        gammeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        // Label/VBox détails de la gamme
        Label detailsTitre = new Label("Détails de la gamme sélectionnée");
        detailsTitre.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-underline: true;");

        VBox gammeDetailsBox = new VBox(7);
        gammeDetailsBox.setStyle("-fx-background-color: #f6f8fa; -fx-padding: 16; -fx-border-color: #b3b3b3; -fx-border-radius: 8; -fx-background-radius: 8;");
        gammeDetailsBox.setMinWidth(350);

        gammeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, gamme) -> {
            gammeDetailsBox.getChildren().clear();
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

                gammeDetailsBox.getChildren().addAll(ref, sep1, ops, opsBox, sep2, eqs, eqBox, sep3, cout, duree);
            } else {
                gammeDetailsBox.getChildren().add(new Label("Sélectionnez une gamme pour voir les détails."));
            }
        });

        rightBox.getChildren().addAll(
                gammesLbl, gammeTable,
                detailsTitre, gammeDetailsBox
        );

        root.getChildren().addAll(leftBox, new Separator(javafx.geometry.Orientation.VERTICAL), rightBox);
        return root;
    }

    // Utilitaires pour trouver le poste associé à une machine
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
}
