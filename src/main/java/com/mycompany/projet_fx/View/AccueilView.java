package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Atelier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AccueilView {

    private Atelier atelier;
    private Color[] couleursPostes;

    public AccueilView(Atelier atelier, Color[] couleursPostes) {
        this.atelier = atelier;
        this.couleursPostes = couleursPostes;
    }

    public VBox getAccueilPane() {
        VBox accueil = new VBox(18);
        accueil.setAlignment(Pos.CENTER); // CENTRAGE VERTICAL & HORIZONTAL
        accueil.setPadding(new Insets(22, 0, 0, 0));

        // Message de bienvenue stylé
        Label bienvenue = new Label("Bienvenue dans l'atelier " +
                (commenceParVoyelle(atelier.getNom()) ? "d'" : "de ") + atelier.getNom() + ".");
        bienvenue.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: 'Segoe UI', 'Arial', sans-serif; -fx-text-fill: #23374d; -fx-padding: 0 0 14 0;");

        // Titre du plan
        Label titrePlan = new Label("Plan de l'atelier :");
        titrePlan.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");

        // HBox pour centrer le plan horizontalement
        HBox planBox = new HBox();
        planBox.setAlignment(Pos.CENTER);
        planBox.getChildren().add(new PlanAtelierView(atelier, couleursPostes).creerPlanAtelier());

        accueil.getChildren().addAll(bienvenue, titrePlan, planBox);

        return accueil;
    }

    // Utilitaire pour l'apostrophe selon la première lettre
    private boolean commenceParVoyelle(String nom) {
        if (nom == null || nom.isEmpty()) return false;
        char c = Character.toLowerCase(nom.charAt(0));
        return "aeiouy".indexOf(c) != -1;
    }
}
