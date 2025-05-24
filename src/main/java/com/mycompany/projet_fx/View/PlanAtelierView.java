package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Model.Equipement;
import com.mycompany.projet_fx.Model.Machine;
import com.mycompany.projet_fx.Model.Poste;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Alert;

public class PlanAtelierView {

    private Atelier atelier;
    private Color[] couleursPostes;

    // Mettre ici la taille de l’atelier et des cases
    public static final int NB_LIGNES = 10;
    public static final int NB_COLONNES = 10;
    public static final int TAILLE_CASE = 50;           // 50 pixels pour bien voir tout le plan
    public static final int TAILLE_PLAN = NB_LIGNES * TAILLE_CASE;

    public PlanAtelierView(Atelier atelier, Color[] couleursPostes) {
        this.atelier = atelier;
        this.couleursPostes = couleursPostes;
    }

    public Pane creerPlanAtelier() {
        Pane planPane = new Pane();
        planPane.setPrefSize(TAILLE_PLAN, TAILLE_PLAN);

        // Fond atelier
        Rectangle fond = new Rectangle(0, 0, TAILLE_PLAN, TAILLE_PLAN);
        fond.setFill(Color.LIGHTGRAY);
        fond.setStroke(Color.GRAY);
        planPane.getChildren().add(fond);

        // Quadrillage
        for (int i = 0; i < NB_LIGNES; i++) {
            for (int j = 0; j < NB_COLONNES; j++) {
                Rectangle carre = new Rectangle(j * TAILLE_CASE, i * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
                carre.setFill(Color.rgb(245, 245, 245, 0.5));
                carre.setStroke(Color.LIGHTGRAY);
                planPane.getChildren().add(carre);
            }
        }

        // Machines sur la grille
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) {
                Machine m = (Machine) eq;
                int x = m.getAbscisse();
                int y = m.getOrdonnee();

                // Ignore si la machine est hors limites
                if (x >= 0 && x < NB_COLONNES && y >= 0 && y < NB_LIGNES) {
                    Rectangle carre = new Rectangle(x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
                    carre.setFill(getColorForMachine(m));
                    carre.setStroke(Color.DARKBLUE);
                    carre.setArcWidth(10); carre.setArcHeight(10);

                    // Afficher détails au clic
                    carre.setOnMouseClicked(ev -> afficherFicheMachine(m));

                    planPane.getChildren().add(carre);
                }
            }
        }
        return planPane;
    }

    // CORRECTION : utilise == pour comparer les instances de Machine !
    private Color getColorForMachine(Machine m) {
        for (int i = 0; i < atelier.getPostes().size(); i++) {
            Poste poste = atelier.getPostes().get(i);
            for (Machine pm : poste.getMachines()) {
                if (pm == m) {
                    return couleursPostes[i % couleursPostes.length];
                }
            }
        }
        return Color.DARKSLATEBLUE; // Machine sans poste
    }

    private void afficherFicheMachine(Machine m) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la machine");
        alert.setHeaderText("Machine #" + m.getRefmachine());
        alert.setContentText(
                "Description : " + m.getDmachine() + "\n"
                + "Abscisse : " + m.getAbscisse() + "\n"
                + "Ordonnée : " + m.getOrdonnee() + "\n"
                + "Coût : " + m.getC() + "\n"
                + "État : " + m.getEtat()
        );
        alert.showAndWait();
    }
}
