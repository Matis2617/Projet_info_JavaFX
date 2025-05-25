package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.*;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.stage.Screen;

import com.mycompany.projet_fx.view.FiabiliteView;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class AtelierView extends Application {

    private BorderPane root;
    private Atelier atelier;
    private String nomFichier;
    private String nomUtilisateur;

    private ObservableList<Gamme> gammesList = FXCollections.observableArrayList();
    private ObservableList<Produit> listeProduits = FXCollections.observableArrayList();
    private ObservableList<Operation> operationsList = FXCollections.observableArrayList();
    private ObservableList<Machine> machinesList = FXCollections.observableArrayList();
    private ObservableList<Poste> postesList = FXCollections.observableArrayList();
    private ObservableList<Operateur> operateursList = FXCollections.observableArrayList();

    private final javafx.scene.paint.Color[] couleursPostes = {
            javafx.scene.paint.Color.ROYALBLUE, javafx.scene.paint.Color.DARKORANGE, javafx.scene.paint.Color.FORESTGREEN, javafx.scene.paint.Color.DARKVIOLET, javafx.scene.paint.Color.DARKCYAN,
            javafx.scene.paint.Color.CRIMSON, javafx.scene.paint.Color.DARKMAGENTA, javafx.scene.paint.Color.GOLD, javafx.scene.paint.Color.MEDIUMPURPLE, javafx.scene.paint.Color.DARKSLATEGRAY
    };

    @Override
    public void start(Stage primaryStage) {
        // ----------- Boîte de saisie personnalisée pour le nom utilisateur -----------
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Bienvenue !");
        dialog.setHeaderText(null);

        dialog.getDialogPane().setStyle("-fx-background-color: #f8fbff; -fx-border-radius: 14; -fx-background-radius: 14;");

        Label titre = new Label("Bienvenue dans la gestion de l'atelier");
        titre.setStyle("-fx-font-size: 20px; -fx-font-family: 'Segoe UI', 'Arial', sans-serif; -fx-font-weight: bold; -fx-text-fill: #23374d;");

        Label prompt = new Label("Veuillez entrer votre nom :");
        prompt.setStyle("-fx-font-size: 14px; -fx-text-fill: #274472; -fx-padding: 12 0 4 0;");

        TextField nameField = new TextField();
        nameField.setPromptText("Votre prénom...");
        nameField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bcd4f6; -fx-background-color: #fff; -fx-font-size: 15px; -fx-padding: 8;");

        VBox vbox = new VBox(10, titre, prompt, nameField);
        vbox.setStyle("-fx-padding: 20;");
        vbox.setAlignment(Pos.CENTER_LEFT);

        dialog.getDialogPane().setContent(vbox);
        ButtonType valider = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().setAll(valider);

        dialog.setResultConverter(b -> {
            if (b == valider) {
                return nameField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        nomUtilisateur = result.orElse("Utilisateur").trim();
        if (nomUtilisateur.isEmpty()) nomUtilisateur = "Utilisateur";
        nomFichier = "atelier_" + nomUtilisateur.toLowerCase() + ".ser";

        Atelier atelierCharge = null;
        File f = new File(nomFichier);
        if (f.exists()) {
            Dialog<ButtonType> choix = new Dialog<>();
            choix.setTitle("Projet existant trouvé");
            choix.setHeaderText(null);

            choix.getDialogPane().setStyle("-fx-background-color: #f4faff; -fx-border-radius: 14; -fx-background-radius: 14;");

            Label titre2 = new Label("Ce nom existe déjà dans les projets sauvegardés.");
            titre2.setStyle("-fx-font-size: 17px; -fx-font-family: 'Segoe UI', 'Arial', sans-serif; -fx-font-weight: bold; -fx-text-fill: #23374d;");

            Label contenu = new Label("Voulez-vous poursuivre l'ancien projet ou en créer un nouveau ?");
            contenu.setStyle("-fx-font-size: 14px; -fx-text-fill: #274472; -fx-padding: 10 0 4 0;");

            VBox vbox2 = new VBox(8, titre2, contenu);
            vbox2.setStyle("-fx-padding: 22;");

            choix.getDialogPane().setContent(vbox2);

            ButtonType btnAncien = new ButtonType("Poursuivre", ButtonBar.ButtonData.YES);
            ButtonType btnNouveau = new ButtonType("Nouveau projet", ButtonBar.ButtonData.NO);

            choix.getDialogPane().getButtonTypes().setAll(btnAncien, btnNouveau);

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

        if (atelier.getGammes() != null) gammesList.addAll(atelier.getGammes());
        if (atelier.getOperations() != null) operationsList.addAll(atelier.getOperations());
        if (atelier.getEquipements() != null) {
            for (Equipement eq : atelier.getEquipements()) {
                if (eq instanceof Machine) machinesList.add((Machine) eq);
            }
        }
        if (atelier.getPostes() != null) postesList.addAll(atelier.getPostes());
        if (atelier.getProduits() != null) listeProduits.addAll(atelier.getProduits());
        if (atelier.getOperateurs() != null) operateursList.addAll(atelier.getOperateurs());

        root = new BorderPane();

        // ----------- Onglets / TabPane ----------- //
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab accueilTab = new Tab("Accueil");
        Tab machineTab = new Tab("Machines");
        Tab posteTab = new Tab("Postes");
        Tab operationTab = new Tab("Opérations");
        Tab operateurTab = new Tab("Opérateurs");
        Tab gammeTab = new Tab("Gammes");
        Tab produitTab = new Tab("Produits");
        Tab fiabiliteTab = new Tab("Fiabilité");

        tabPane.getTabs().addAll(
                accueilTab,
                machineTab,
                posteTab,
                operationTab,
                operateurTab,
                gammeTab,
                produitTab,
                fiabiliteTab
        );

        // Style: chaque tab prend toute la largeur, adaptatif
        tabPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            double largeur = newVal.doubleValue();
            double largeurTab = largeur / tabPane.getTabs().size();
            for (Tab tab : tabPane.getTabs()) {
                tab.setStyle("-fx-tab-min-width: " + largeurTab + "px; -fx-tab-max-width: " + largeurTab + "px;");
            }
        });

        // Changement de contenu central selon l’onglet sélectionné
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == accueilTab) afficherAccueil();
            else if (newTab == machineTab) afficherFormulaireAjoutMachine();
            else if (newTab == posteTab) afficherPoste();
            else if (newTab == operationTab) afficherOperation();
            else if (newTab == operateurTab) afficherOperateur();
            else if (newTab == gammeTab) afficherGamme();
            else if (newTab == produitTab) afficherProduit();
            else if (newTab == fiabiliteTab) afficherFiabilite();
        });

        root.setTop(tabPane);

        // Affiche d'abord l'accueil
        tabPane.getSelectionModel().select(0);
        
        // Obtenir les dimensions de l'écran
        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Créer la scène avec les dimensions de l'écran
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        
        primaryStage.setTitle(getTitreAtelier());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String getTitreAtelier() {
        if (nomUtilisateur == null || nomUtilisateur.isEmpty()) return "Atelier";
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

    private void afficherGamme() {
        root.setCenter(GammeFormView.getGammeForm(atelier, gammesList, operationsList, nomFichier, this::refreshAfterGammeChange));
    }

    private void afficherProduit() {
        root.setCenter(ProduitFormView.getProduitForm(listeProduits, gammesList, atelier, nomFichier, this::afficherAccueil));
    }

    private void afficherOperateur() {
        root.setCenter(
                OperateurFormView.getOperateurForm(
                        operateursList,
                        postesList,
                        atelier, nomFichier,
                        this::afficherAccueil
                )
        );
    }

    private void afficherFiabilite() {
        root.setCenter(new FiabiliteView().getView());
    }

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
