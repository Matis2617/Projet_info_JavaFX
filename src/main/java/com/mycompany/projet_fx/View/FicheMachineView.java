package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Machine;
import javafx.scene.control.Alert;

public class FicheMachineView {

    public static void afficherFicheMachine(Machine m) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la machine");
        alert.setHeaderText(m.getDmachine());
        alert.setContentText(
                "Description: " + m.getDmachine() + "\n"
                        + "Abscisse: " + m.getAbscisse() + "\n"
                        + "Ordonnée: " + m.getOrdonnee() + "\n"
                        + "Coût: " + m.getC() + "\n"
                        + "État: " + m.getEtat()
        );
        alert.showAndWait();
    }
}
