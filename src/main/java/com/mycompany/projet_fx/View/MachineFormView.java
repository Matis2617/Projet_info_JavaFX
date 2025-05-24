package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Machine;
import com.mycompany.projet_fx.Model.Equipement;
import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde; // Si tu as centralisé la sauvegarde ici

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.ObservableList;

import javafx.geometry.Insets;

public class MachineFormView {

    // Peut renvoyer un VBox prêt à être inséré dans le root.setCenter(box)
    public static VBox getMachineForm(Atelier atelier, String nomFichier, Runnable onSuccess) {
        VBox box = new VBox(10);
        box.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField idField = new TextField();
        idField.setPromptText("Identifiant");

        TextField descField = new TextField();
        descField.setPromptText("Description");

        TextField abscField = new TextField();
        abscField.setPromptText("Abscisse (0-49)");

        TextField ordField = new TextField();
        ordField.setPromptText("Ordonnée (0-49)");

        TextField coutField = new TextField();
        coutField.setPromptText("Coût");

        TextField tempsField = new TextField();
        tempsField.setPromptText("Temps préparation");

        ComboBox<Machine.ETAT> etatBox = new ComboBox<>();
        etatBox.getItems().addAll(Machine.ETAT.values());
        etatBox.setPromptText("État");

        Label erreurLabel = new Label();
        erreurLabel.setStyle("-fx-text-fill: red;");

        Button ajouterBtn = new Button("Ajouter la machine");
        ajouterBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String desc = descField.getText();
                int absc = Integer.parseInt(abscField.getText());
                int ord = Integer.parseInt(ordField.getText());
                float cout = Float.parseFloat(coutField.getText());
                float temps = Float.parseFloat(tempsField.getText());
                Machine.ETAT etat = etatBox.getValue();

                if (absc < 0 || absc >= 50 || ord < 0 || ord >= 50) {
                    erreurLabel.setText("Erreur : Coordonnées hors de l'atelier !");
                    return;
                }

                // Vérification unicité
                boolean existe = false;
                for (Equipement eq : atelier.getEquipements()) {
                    if (eq instanceof Machine) {
                        Machine m = (Machine) eq;
                        if (m.getRefmachine() == id) {
                            existe = true;
                            erreurLabel.setText("Erreur : Identifiant déjà utilisé !");
                            break;
                        }
                        if (m.getAbscisse() == absc && m.getOrdonnee() == ord) {
                            existe = true;
                            erreurLabel.setText("Erreur : Coordonnées déjà utilisées !");
                            break;
                        }
                    }
                }
                if (!existe) {
                    Machine m = new Machine(
                            atelier.getEquipements().size() + 1,
                            id, desc, absc, ord, cout, temps, etat
                    );
                    atelier.getEquipements().add(m);
                    AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
                    System.out.println("[AJOUT] Machine ajoutée. Total: " + atelier.getEquipements().size());
                    if (onSuccess != null) onSuccess.run(); // callback pour afficherAccueil, etc.
                }
            } catch (Exception ex) {
                erreurLabel.setText("Erreur : Données invalides.");
            }
        });

        Button retourBtn = new Button("Annuler");
        retourBtn.setOnAction(e -> {
            if (onSuccess != null) onSuccess.run();
        });

        box.getChildren().addAll(
                new Label("Ajouter une machine :"),
                idField, descField, abscField, ordField, coutField, tempsField,
                etatBox, ajouterBtn, retourBtn, erreurLabel
        );
        return box;
    }
    public static void modifierMachine(Machine machine, Atelier atelier, String nomFichier, Runnable onRefresh) {
    Dialog<Void> dlg = new Dialog<>();
    dlg.setTitle("Modifier la machine");
    VBox box = new VBox(10);
    box.setPadding(new Insets(8));

    TextField descField = new TextField(machine.getDmachine());
    TextField abscField = new TextField(String.valueOf(machine.getAbscisse()));
    TextField ordField = new TextField(String.valueOf(machine.getOrdonnee()));
    TextField coutField = new TextField(String.valueOf(machine.getC()));
    TextField tempsField = new TextField(String.valueOf(machine.getT()));
    ComboBox<Machine.ETAT> etatBox = new ComboBox<>();
    etatBox.getItems().addAll(Machine.ETAT.values());
    etatBox.setValue(machine.getEtat());

    Button valider = new Button("Valider");
    valider.setOnAction(ev -> {
        machine.setDmachine(descField.getText());
        machine.setAbscisse(Integer.parseInt(abscField.getText()));
        machine.setOrdonnee(Integer.parseInt(ordField.getText()));
        machine.setC(Float.parseFloat(coutField.getText()));
        machine.setT(Float.parseFloat(tempsField.getText()));
        machine.setEtat(etatBox.getValue());
        // Sauvegarder atelier...
        // AtelierSauvegarde.sauvegarderAtelier(atelier, nomFichier);
        dlg.setResult(null);
        dlg.close();
        if (onRefresh != null) onRefresh.run();
    });

    box.getChildren().addAll(
        new Label("Description :"), descField,
        new Label("Abscisse :"), abscField,
        new Label("Ordonnée :"), ordField,
        new Label("Coût :"), coutField,
        new Label("Temps préparation :"), tempsField,
        new Label("État :"), etatBox,
        valider
    );
    dlg.getDialogPane().setContent(box);
    dlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    dlg.showAndWait();
}

}
