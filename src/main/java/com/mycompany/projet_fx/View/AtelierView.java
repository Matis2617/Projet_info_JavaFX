package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.*;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.Font;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class AtelierView extends Application {

    private BorderPane root;
    private Atelier atelier;
    private String nomFichier;

    private ObservableList<Gamme> gammesList = FXCollections.observableArrayList();
    private ObservableList<Produit> listeProduits = FXCollections.observableArrayList();
    private ObservableList<Operation> operationsList = FXCollections.observableArrayList();
    private ObservableList<Machine> machinesList = FXCollections.observableArrayList();
    private ObservableList<Poste> postesList = FXCollections.observableArrayList();

    private final javafx.scene.paint.Color[] couleursPostes = {
            javafx.scene.paint.Color.ROYALBLUE, javafx.scene.paint.Color.DARKORANGE, javafx.scene.paint.Color.FORESTGREEN, javafx.scene.paint.Color.DARKVIOLET, javafx.scene.paint.Color.DARKCYAN,
            javafx.scene.paint.Color.CRIMSON, javafx.scene.paint.Color.DARKMAGENTA, javafx.scene.paint.Color.GOLD, javafx.scene.paint.Color.MEDIUMPURPLE, javafx.scene.paint.Color.DARKSLATEGRAY
    };

    private String nomUtilisateur;

    @Override
    public void start(Stage primaryStage) {
        // Saisie nom utilisateur
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Bienvenue !");
        dialog.setHeaderText("Bienvenue dans la gestion de l'atelier");
        dialog.setContentText("Veuillez entrer votre nom :");
        Optional<String> result = dialog.showAndWait();
        nomUtilisateur = result.orElse("Utilisateur").trim();
        nomFichier = "atelier_" + nomUtilisateur.toLowerCase() + ".ser";

        Atelier atelierCharge = null;
        File f = new File(nomFichier);
        if (f.exists()) {
            Alert choix = new Alert(Alert.AlertType.CONFIRMATION);
            choix.setTitle("Projet existant trouvé");
            choix.setHeaderText("Un projet existe déjà sous ce prénom.");
            choix.setContentText("Voulez-vous poursuivre l'ancien projet ou en créer un nouveau ?");
            ButtonType btnAncien = new ButtonType("Poursuivre");
            ButtonType btnNouveau = new ButtonType("Nouveau projet");
            choix.getButtonTypes().setAll(btnAncien, btnNouveau);
            Optional<ButtonType> option = choix.showAndWait();
            if (option.isPresent() && option.get() == btnAncien) {
                atelierCharge = AtelierSauvegarde.chargerAtelier(nomFichier);
            }
        }
        if (atelierCharge == null) {
            ArrayList<Equipement> equipements = new ArrayList<>();
            ArrayList<Operateur> operateurs = new ArrayList<>();
            ArrayList<Personne> personnes = new ArrayList<>();
            atelier = new Atelier(1, nomUtilisateur, equipements, operateurs, personnes);
        } else {
            atelier = atelierCharge;
        }

        // Chargement des listes pour les TableView/tableaux
        if (atelier.getGammes() != null) gammesList.addAll(atelier.getGammes());
        if (atelier.getOperations() != null) operationsList.addAll(atelier.getOperations());
        if (atelier.getEquipements() != null) {
            for (Equipement eq : atelier.getEquipements()) {
                if (eq instanceof Machine) machinesList.add((Machine) eq);
            }
        }
        if (atelier.getPostes() != null) postesList.addAll(atelier.getPostes());

        // Barre de menu
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-font-family: 'Segoe UI Semibold', 'Arial', sans-serif; -fx-font-size: 15px;");

        Menu menu = new Menu("Menu");
        MenuItem accueilItem = new MenuItem("Accueil");
        MenuItem machineItem = new MenuItem("Machines");
        MenuItem personnesItem = new MenuItem("Personnes");
        MenuItem operateurItem = new MenuItem("Opérateur");
        MenuItem posteItem = new MenuItem("Poste");
        MenuItem operationItem = new MenuItem("Opérations");
        MenuItem produitItem = new MenuItem("Produit");
        MenuItem gammeItem = new MenuItem("Gamme");
        MenuItem listeProduitItem = new MenuItem("Produits finis");
        MenuItem syntheseItem = new MenuItem("Synthèse");
        menu.getItems().addAll(
                accueilItem, machineItem, personnesItem, operateurItem, posteItem, operationItem,
                produitItem, gammeItem, listeProduitItem, syntheseItem
        );
        menuBar.getMenus().add(menu);

        root = new BorderPane();
        root.setTop(menuBar);

        accueilItem.setOnAction(e -> afficherAccueil());
        machineItem.setOnAction(e -> afficherFormulaireAjoutMachine());
        posteItem.setOnAction(e -> afficherPoste());
        operationItem.setOnAction(e -> afficherOperation());
        produitItem.setOnAction(e -> afficherProduit());
        gammeItem.setOnAction(e -> afficherGamme());
        listeProduitItem.setOnAction(e -> afficherListeProduits());
        syntheseItem.setOnAction(e -> afficherSynthese());
        personnesItem.setOnAction(e -> afficherPersonne());
        operateurItem.setOnAction(e -> afficherOperateur());

        afficherAccueil();

        Scene scene = new Scene(root, 1120, 800);
        primaryStage.setTitle(getTitreAtelier());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Ateliers de / d'
    private String getTitreAtelier() {
        if (nomUtilisateur.isEmpty()) return "Atelier";
        char first = Character.toLowerCase(nomUtilisateur.charAt(0));
        String voyelles = "aeiouyéèêëàâäîïôöùûü";
        String prefixe = voyelles.indexOf(first) >= 0 ? "Atelier d'" : "Atelier de ";
        return prefixe + nomUtilisateur;
    }

    private void afficherAccueil() {
        VBox vbox = new VBox(24);
        vbox.setAlignment(Pos.CENTER);

        Label titre = new Label("Bienvenue dans " + getTitreAtelier());
        titre.setFont(Font.font("Segoe UI Semibold", 26));
        titre.setStyle("-fx-text-fill: #284785; -fx-font-weight: bold; -fx-padding: 30 0 15 0;");

        // Plan centré
        HBox centerHBox = new HBox();
        centerHBox.setAlignment(Pos.CENTER);
        centerHBox.getChildren().add(new PlanAtelierView(atelier, couleursPostes).creerPlanAtelier());

        vbox.getChildren().addAll(titre, centerHBox);
        root.setCenter(vbox);
    }

    private void afficherFormulaireAjoutMachine() {
        root.setCenter(MachineFormView.getMachineForm(atelier, nomFichier, this::refreshAfterMachineChange));
    }

    private void afficherPoste() {
        root.setCenter(PosteFormView.getPosteForm(atelier, nomFichier, this::refreshAfterPosteChange));
    }

    private void afficherOperation() {
        root.setCenter(new OperationView(atelier, operationsList, nomFichier, this::refreshAfterOperationChange).getView());
    }

    private void afficherProduit() {
        root.setCenter(ProduitFormView.getProduitForm(listeProduits, this::afficherAccueil));
    }

    private void afficherGamme() {
        root.setCenter(GammeFormView.getGammeForm(atelier, gammesList, operationsList, nomFichier, this::refreshAfterGammeChange));
    }

    private void afficherListeProduits() {
        root.setCenter(ProduitFormView.getListeProduitsView(listeProduits, this::afficherAccueil));
    }

    private void afficherPersonne() {
        // À compléter selon ta logique de PersonneView
    }

    private void afficherOperateur() {
        // À compléter selon ta logique de OperateurView
    }

    private void afficherSynthese() {
        // On met à jour les listes pour la synthèse
        machinesList.setAll();
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) machinesList.add((Machine) eq);
        }
        postesList.setAll(atelier.getPostes());
        gammesList.setAll(atelier.getGammes());
        operationsList.setAll(atelier.getOperations());

    }

    // --- Rafraîchissements : chaque modification doit remettre à jour les listes pour la vue synthétique ---
    private void refreshAfterMachineChange() {
        machinesList.setAll();
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) machinesList.add((Machine) eq);
        }
        afficherAccueil();
    }

    private void refreshAfterPosteChange() {
        postesList.setAll(atelier.getPostes());
        afficherAccueil();
    }

    private void refreshAfterGammeChange() {
        gammesList.setAll(atelier.getGammes());
        afficherAccueil();
    }

    private void refreshAfterOperationChange() {
        operationsList.setAll(atelier.getOperations());
        afficherAccueil();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
