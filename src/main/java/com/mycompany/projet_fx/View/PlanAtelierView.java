package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Model.Equipement;
import com.mycompany.projet_fx.Model.Machine;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Alert;

public class PlanAtelierView {

    private Atelier atelier;
    private Color[] couleursPostes;

    // Le listener au clic peut être passé en paramètre si tu veux rendre la classe plus générique

    public PlanAtelierView(Atelier atelier, Color[] couleursPostes) {
        this.atelier = atelier;
        this.couleursPostes = couleursPostes;
    }

    public Pane creerPlanAtelier() {
        Pane planPane = new Pane();
        int tailleAtelier = 500;
        int grille = 50;
        double unite = (double) tailleAtelier / grille;

        planPane.setPrefSize(tailleAtelier, tailleAtelier);

        // Fond atelier
        Rectangle fond = new Rectangle(0, 0, tailleAtelier, tailleAtelier);
        fond.setFill(Color.LIGHTGRAY);
        fond.setStroke(Color.GRAY);
        planPane.getChildren().add(fond);

        // Dessine les machines
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) {
                Machine m = (Machine) eq;
                double x = m.getAbscisse() * unite;
                double y = m.getOrdonnee() * unite;

                Rectangle carre = new Rectangle(x, y, unite, unite);
                carre.setFill(getColorForMachine(m));
                carre.setStroke(Color.DARKBLUE);
                carre.setArcWidth(8); carre.setArcHeight(8);

                // Détails au clic
                carre.setOnMouseClicked(ev -> afficherFicheMachine(m));

                planPane.getChildren().add(carre);
            }
        }
        return planPane;
    }

    private Color getColorForMachine(Machine m) {
        for (int i = 0; i < atelier.getPostes().size(); i++) {
            if (atelier.getPostes().get(i).getMachines().contains(m)) {
                return couleursPostes[i % couleursPostes.length];
            }
        }
        return Color.BLACK; // machine sans poste
    }

    // Optionnel, si tu veux déplacer aussi la fiche machine ici
    private void afficherFicheMachine(Machine m) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la machine");
        alert.setHeaderText(m.getDmachine());
        alert.setContentText(
                "Description: " + m.getDmachine() + "\n"
                + "Abscisse: " + m.getAbscisse() + "\n"
                + "Ordonnée: " + m.getOrdonnee() + "\n"
                + "Coût: " + m.getC() + "\n"
                + "Temps de préparation: " + m.getT() + "\n"
                + "État: " + m.getEtat()
        );
        alert.showAndWait();
    }
}
