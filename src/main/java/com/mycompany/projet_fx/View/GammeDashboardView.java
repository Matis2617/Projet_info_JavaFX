package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.*;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;

import java.util.List;

public class GammeDashboardView {
    private VBox root;
    private TableView<Operation> tableOperations;
    private TableView<Machine> tableMachines;
    private TableView<Gamme> tableGammes;
    private Label detailsLabel;

    public GammeDashboardView(
            ObservableList<Operation> operationsList,
            ObservableList<Machine> machinesList,
            ObservableList<Gamme> gammesList,
            ObservableList<Poste> postesList // Pour la couleur et le nom du poste de chaque machine
    ) {
        createView(operationsList, machinesList, gammesList, postesList);
    }

    private void createView(
            ObservableList<Operation> operationsList,
            ObservableList<Machine> machinesList,
            ObservableList<Gamme> gammesList,
            ObservableList<Poste> postesList
    ) {
        root = new VBox(24);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;");

        // ----- Tableau Opérations -----
        tableOperations = new TableView<>(operationsList);
        tableOperations.setPrefHeight(140);
        tableOperations.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Operation, Number> opIdCol = new TableColumn<>("ID Opération");
        opIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId_operation()));
        TableColumn<Operation, String> opDescCol = new TableColumn<>("Description");
        opDescCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        TableColumn<Operation, Number> opDureeCol = new TableColumn<>("Durée (h)");
        opDureeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getDuree()));
        tableOperations.getColumns().addAll(opIdCol, opDescCol, opDureeCol);

        // ----- Tableau Machines -----
        tableMachines = new TableView<>(machinesList);
        tableMachines.setPrefHeight(140);
        tableMachines.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Machine, Number> machIdCol = new TableColumn<>("ID Machine");
        machIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRefmachine()));
        TableColumn<Machine, String> machDescCol = new TableColumn<>("Description");
        machDescCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDmachine()));
        TableColumn<Machine, String> machPosteCol = new TableColumn<>("Poste");
        machPosteCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                getPosteNameForMachine(data.getValue(), postesList)
        ));
        TableColumn<Machine, Color> machColorCol = new TableColumn<>("Couleur Poste");
        machColorCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Color color, boolean empty) {
                super.updateItem(color, empty);
                setText(null);
                if (empty || color == null) {
                    setGraphic(null);
                } else {
                    Rectangle r = new Rectangle(18, 18, color);
                    setGraphic(r);
                }
            }
        });
        machColorCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(
                getColorForMachine(data.getValue(), postesList)
        ));

        tableMachines.getColumns().addAll(machIdCol, machDescCol, machPosteCol, machColorCol);

        // ----- Tableau Gammes -----
        tableGammes = new TableView<>(gammesList);
        tableGammes.setPrefHeight(140);
        tableGammes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Gamme, String> gammeRefCol = new TableColumn<>("ID Gamme");
        gammeRefCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRefGamme() == null ? "" : data.getValue().getRefGamme()));

        TableColumn<Gamme, String> gammeOpCol = new TableColumn<>("Opérations");
        gammeOpCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getOperations().isEmpty()
                        ? "-"
                        : data.getValue().getOperations().get(0).getDescription() // Première opération seulement pour l'aperçu
        ));

        TableColumn<Gamme, String> gammeMachCol = new TableColumn<>("Machine");
        gammeMachCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getListeEquipements().isEmpty()
                        ? "-"
                        : (data.getValue().getListeEquipements().get(0) instanceof Machine
                        ? ((Machine) data.getValue().getListeEquipements().get(0)).getDmachine()
                        : data.getValue().getListeEquipements().get(0).affiche())
        ));

        tableGammes.getColumns().addAll(gammeRefCol, gammeOpCol, gammeMachCol);

        // ----- Détails gamme -----
        detailsLabel = new Label("Sélectionnez une gamme pour voir les détails.");
        detailsLabel.setStyle("-fx-font-size: 15px; -fx-background-color: #f6f6f6; -fx-padding: 10;");
        detailsLabel.setMaxWidth(700);
        detailsLabel.setWrapText(true);

        tableGammes.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                showDetails(selected, postesList);
            } else {
                detailsLabel.setText("Sélectionnez une gamme pour voir les détails.");
            }
        });

        Label titre1 = new Label("Tableau des Opérations existantes");
        titre1.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label titre2 = new Label("Tableau des Machines créées");
        titre2.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label titre3 = new Label("Tableau des Gammes créées");
        titre3.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        root.getChildren().addAll(
                titre1, tableOperations,
                titre2, tableMachines,
                titre3, tableGammes,
                detailsLabel
        );
    }

    private String getPosteNameForMachine(Machine m, List<Poste> postesList) {
        for (Poste poste : postesList) {
            if (poste.getMachines().contains(m)) return poste.getNomPoste();
        }
        return "-";
    }

    private Color getColorForMachine(Machine m, List<Poste> postesList) {
        for (int i = 0; i < postesList.size(); i++) {
            Poste poste = postesList.get(i);
            if (poste.getMachines().contains(m)) {
                return getColorForPoste(i);
            }
        }
        return Color.LIGHTGRAY;
    }

    private Color getColorForPoste(int i) {
        Color[] couleurs = {
                Color.ROYALBLUE, Color.DARKORANGE, Color.FORESTGREEN, Color.DARKVIOLET, Color.DARKCYAN,
                Color.CRIMSON, Color.DARKMAGENTA, Color.GOLD, Color.MEDIUMPURPLE, Color.DARKSLATEGRAY
        };
        return couleurs[i % couleurs.length];
    }

    private void showDetails(Gamme gamme, List<Poste> postesList) {
        StringBuilder sb = new StringBuilder();
        sb.append("Référence : ").append(gamme.getRefGamme()).append("\n");
        sb.append("Opérations :\n");
        for (Operation op : gamme.getOperations()) {
            sb.append("  - [#").append(op.getId_operation()).append("] ").append(op.getDescription()).append(" (")
                    .append(op.getDuree()).append("h)\n");
        }
        sb.append("Machines/Eq. utilisées :\n");
        for (Equipement eq : gamme.getListeEquipements()) {
            if (eq instanceof Machine) {
                Machine m = (Machine) eq;
                sb.append("  - Machine: ").append(m.getDmachine());
                sb.append(" [poste: ").append(getPosteNameForMachine(m, postesList)).append("]");
                sb.append(", coût horaire: ").append(m.getC()).append(" €\n");
            } else {
                sb.append("  - ").append(eq.affiche()).append("\n");
            }
        }
        sb.append("\nCoût horaire total de la gamme : ").append(gamme.coutGamme()).append(" €");
        sb.append("\nDurée totale de la gamme : ").append(gamme.dureeGamme()).append(" h");
        detailsLabel.setText(sb.toString());
    }

    public VBox getView() { return root; }
}
