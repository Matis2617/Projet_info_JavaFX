package com.mycompany.projet_fx.view;

import com.mycompany.projet_fx.Model.EvenementMaintenance;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FiabiliteView {

    private VBox root;
    private ObservableList<EvenementMaintenance> evenements = FXCollections.observableArrayList();

    public FiabiliteView() {
        createView();
    }

    private void createView() {
        // TableView pour les événements
        TableView<EvenementMaintenance> table = new TableView<>(evenements);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<EvenementMaintenance, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDateHeure().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        TableColumn<EvenementMaintenance, String> machineCol = new TableColumn<>("Machine");
        machineCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMachine()));

        TableColumn<EvenementMaintenance, String> evtCol = new TableColumn<>("Evénement");
        evtCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEvenement()));

        TableColumn<EvenementMaintenance, String> opCol = new TableColumn<>("Opérateur");
        opCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getOperateur()));

        TableColumn<EvenementMaintenance, String> causeCol = new TableColumn<>("Cause");
        causeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCause()));

        table.getColumns().addAll(dateCol, machineCol, evtCol, opCol, causeCol);

        // Bouton charger le fichier
        Button chargerBtn = new Button("Charger suiviMaintenance.txt");
        chargerBtn.setOnAction(e -> {
            evenements.clear();
            chargerFichierEvenements("suiviMaintenance.txt", evenements);
        });

        // Bouton analyse fiabilité
        Button analyserBtn = new Button("Analyser fiabilité");
        analyserBtn.setOnAction(e -> {
            Map<String, Double> mapFiabilites = calculerFiabilites(evenements);
            List<Map.Entry<String, Double>> machinesTriees = trierMachinesParFiabilite(mapFiabilites);

            StringBuilder sb = new StringBuilder("Fiabilité des machines (décroissant) :\n");
            for (Map.Entry<String, Double> entry : machinesTriees) {
                sb.append(String.format("%s : %.2f %%\n", entry.getKey(), entry.getValue() * 100));
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString());
            alert.setTitle("Analyse de fiabilité");
            alert.setHeaderText("Résultat analyse fichier");
            alert.showAndWait();
        });

        HBox btnBox = new HBox(12, chargerBtn, analyserBtn);

        root = new VBox(14, table, btnBox);
        root.setPadding(new Insets(16));
    }

    public Parent getView() {
        return root;
    }

    /** ------------------- OUTILS D'ANALYSE ------------------- */

    // Charge les événements du fichier suiviMaintenance.txt
    private void chargerFichierEvenements(String fichier, ObservableList<EvenementMaintenance> liste) {
        DateTimeFormatter dte = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm");
        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.trim().equals("") || ligne.startsWith("-") || ligne.contains("End Of File")) continue;
                String[] tokens = ligne.trim().split("\\s+");
                if (tokens.length < 6) continue;
                // Ex: 20012020 06:42 Mach_5 A OP102 panne
                String dateStr = tokens[0];
                String heureStr = tokens[1].replace(":", ":"); // format "06:42"
                String machine = tokens[2];
                String evt = tokens[3];
                String op = tokens[4];
                String cause = tokens[5];
                LocalDateTime dateHeure = LocalDateTime.parse(dateStr + " " + heureStr, dte);
                EvenementMaintenance ev = new EvenementMaintenance(dateHeure, machine, evt, op, cause);
                liste.add(ev);
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur de lecture du fichier : " + ex.getMessage());
            alert.showAndWait();
        }
    }

    // Calcule la fiabilité de chaque machine selon l'algorithme donné
    private Map<String, Double> calculerFiabilites(List<EvenementMaintenance> evenements) {
        // Pour chaque machine, accumule durée de marche et durée de panne
        Map<String, List<EvenementMaintenance>> mapMachineEvts = new HashMap<>();
        for (EvenementMaintenance ev : evenements) {
            mapMachineEvts.computeIfAbsent(ev.getMachine(), k -> new ArrayList<>()).add(ev);
        }
        Map<String, Double> mapFiabilite = new HashMap<>();
        for (String machine : mapMachineEvts.keySet()) {
            List<EvenementMaintenance> evts = mapMachineEvts.get(machine);
            // Trie par date/heure croissant
            evts.sort(Comparator.comparing(EvenementMaintenance::getDateHeure));
            // Algorithme : calcule total marche et total panne
            double dureeMarche = 0;
            double dureeTotale = 0;
            LocalDateTime debut = null;
            boolean isEnMarche = false;

            for (EvenementMaintenance ev : evts) {
                if (ev.getEvenement().equals("A")) {
                    // Arrêt => Fin de marche, début de panne
                    if (debut != null && isEnMarche) {
                        dureeMarche += java.time.Duration.between(debut, ev.getDateHeure()).toMinutes();
                    }
                    debut = ev.getDateHeure();
                    isEnMarche = false;
                } else if (ev.getEvenement().equals("D")) {
                    // Reprise => Fin de panne, début de marche
                    if (debut != null && !isEnMarche) {
                        dureeTotale += java.time.Duration.between(debut, ev.getDateHeure()).toMinutes();
                    }
                    debut = ev.getDateHeure();
                    isEnMarche = true;
                }
            }
            // On ne connaît pas la durée d'observation totale, mais on peut approximer la fiabilité comme :
            // fiabilité = durée de marche / (durée de marche + durée de panne)
            double dureePanne = dureeTotale - dureeMarche;
            double fiabilite = (dureeMarche + dureePanne) > 0 ? (dureeMarche / (dureeMarche + dureePanne)) : 0.0;
            mapFiabilite.put(machine, fiabilite);
        }
        return mapFiabilite;
    }

    // Trie les machines par fiabilité décroissante
    private List<Map.Entry<String, Double>> trierMachinesParFiabilite(Map<String, Double> fiabilites) {
        List<Map.Entry<String, Double>> liste = new ArrayList<>(fiabilites.entrySet());
        liste.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        return liste;
    }
}
