package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Model.Machine;
import com.mycompany.projet_fx.Model.Equipement;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class AccueilView {

    private Atelier atelier;
    private Color[] couleursPostes;

    public AccueilView(Atelier atelier, Color[] couleursPostes) {
        this.atelier = atelier;
        this.couleursPostes = couleursPostes;
    }

    public VBox getAccueilPane() {
        VBox accueil = new VBox(15);
        accueil.setStyle("-fx-alignment: center; -fx-padding: 20;");
        accueil.getChildren().add(new Label("Bienvenue dans l'atelier de " + atelier.getNom() + "."));
        accueil.getChildren().add(new Label("Plan de l'atelier :"));

        HBox hbox = new HBox();
        hbox.setStyle("-fx-alignment: center;");
        PlanAtelierView planView = new PlanAtelierView(atelier, couleursPostes);
        hbox.getChildren().add(planView.creerPlanAtelier());
        accueil.getChildren().add(hbox);

        return accueil;
    }

    // Génération du plan de l’atelier
    private Pane creerPlanAtelier() {
        Pane planPane = new Pane();
        int tailleAtelier = 500;
        int grille = 50;
        double unite = (double) tailleAtelier / grille;

        planPane.setPrefSize(tailleAtelier, tailleAtelier);

        // Fond atelier
        javafx.scene.shape.Rectangle fond = new javafx.scene.shape.Rectangle(0, 0, tailleAtelier, tailleAtelier);
        fond.setFill(Color.LIGHTGRAY);
        fond.setStroke(Color.GRAY);
        planPane.getChildren().add(fond);

        // Dessine les machines
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) {
                Machine m = (Machine) eq;
                double x = m.getAbscisse() * unite;
                double y = m.getOrdonnee() * unite;

                javafx.scene.shape.Rectangle carre = new javafx.scene.shape.Rectangle(x, y, unite, unite);
                carre.setFill(getColorForMachine(m));
                carre.setStroke(Color.DARKBLUE);
                carre.setArcWidth(8); carre.setArcHeight(8);

                // TODO : ajouter un event au clic si besoin

                planPane.getChildren().add(carre);
            }
        }
        return planPane;
    }

    // Donne la couleur du poste auquel appartient la machine
    private Color getColorForMachine(Machine m) {
        for (int i = 0; i < atelier.getPostes().size(); i++) {
            if (atelier.getPostes().get(i).getMachines().contains(m)) {
                return couleursPostes[i % couleursPostes.length];
            }
        }
        return Color.BLACK; // machine sans poste
    }
}
