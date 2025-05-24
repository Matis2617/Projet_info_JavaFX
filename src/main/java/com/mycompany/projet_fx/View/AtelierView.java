package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.*;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import com.mycompany.projet_fx.controller.FiabiliteController;
import com.mycompany.projet_fx.view.FiabiliteView;
import com.mycompany.projet_fx.controller.PersonneController;
import com.mycompany.projet_fx.view.PersonneView;
import com.mycompany.projet_fx.controller.OperateurController;
import com.mycompany.projet_fx.view.OperateurView;
import com.mycompany.projet_fx.controller.StockBrutController;
import com.mycompany.projet_fx.view.StockBrutView;

public class AtelierView extends Application {

    private BorderPane root;
    private Atelier atelier;
    private String nomFichier;
    private FiabiliteController fiabiliteController = new FiabiliteController();

    // Listes observables partagées avec les vues
    private ObservableList<Gamme> gammesList = FXCollections.observableArrayList();
    private ObservableList<Produit> listeProduits = FXCollections.observableArrayList();
    private ObservableList<Operation> operationsList = FXCollections.observableArrayList();

    // Couleurs pour l’affichage des postes (plan atelier)
    private final javafx.scene.paint.Color[] couleursPostes = {
            javafx.scene.paint.Color.ROYALBLUE, javafx.scene.paint.Color.DARKORANGE, javafx.scene.paint.Color.FORESTGREEN, javafx.scene.paint.Color.DARKVIOLET, javafx.scene.paint.Color.DARKCYAN,
            javafx.scene.paint.Color.CRIMSON, javafx.scene.paint.Color.DARKMAGENTA, javafx.scene.paint.Color.GOLD, javafx.scene.paint.Color.MEDIUMPURPLE, javafx.scene.paint.Color.DARKSLATEGRAY
    };

    @Override
    public void start(Stage primaryStage) {
        // Saisie nom utilisateur
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Bienvenue !");
        dialog.setHeaderText("Bienvenue dans la gestion de l'atelier");
        dialog.setContentText("Veuillez entrer votre nom :");
        Optional<String> result = dialog.showAndWait();
        String nomUtilisateur = result.orElse("Utilisateur");
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

        // Synchronisation des listes observables depuis atelier à l’ouverture
        if (atelier.getGammes() != null) gammesList.addAll(atelier.getGammes());
        if (atelier.getOperations() != null) operationsList.addAll(atelier.getOperations());

        // Barre de menu
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        MenuItem accueilItem = new MenuItem("Accueil");
        MenuItem machineItem = new MenuItem("Machines");
        MenuItem personnesItem = new MenuItem("Personnes");
        MenuItem operateurItem = new MenuItem("Operateur");
        MenuItem posteItem = new MenuItem("Poste");
        MenuItem operationItem = new MenuItem("Opérations");
        MenuItem produitItem = new MenuItem("Produit");
        MenuItem gammeItem = new MenuItem("Gamme");
        MenuItem listeProduitItem = new MenuItem("Produits finis");
        MenuItem fiabiliteItem = new MenuItem("Fiabilité");
        MenuItem stockBrutItem = new MenuItem("Stock Brut");
        menu.getItems().addAll(
                accueilItem, machineItem, personnesItem, posteItem, operationItem,
                produitItem, gammeItem, listeProduitItem, fiabiliteItem, stockBrutItem,
                operateurItem
        );
        menuBar.getMenus().add(menu);

        root = new BorderPane();
        root.setTop(menuBar);

        // Actions menu
        accueilItem.setOnAction(e -> afficherAccueil());
        machineItem.setOnAction(e -> afficherFormulaireAjoutMachine());
        posteItem.setOnAction(e -> afficherPoste());
        operationItem.setOnAction(e -> afficherOperation());
        produitItem.setOnAction(e -> afficherProduit());
        gammeItem.setOnAction(e -> afficherGamme());
        listeProduitItem.setOnAction(e -> afficherListeProduits());
        fiabiliteItem.setOnAction(e -> root.setCenter(new FiabiliteView(fiabiliteController).getView()));
        personnesItem.setOnAction(e -> afficherPersonne());
        operateurItem.setOnAction(e -> afficherOperateur());
        stockBrutItem.setOnAction(e -> afficherStockBrut());

        afficherAccueil();

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setTitle("Atelier de " + atelier.getNom());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void afficherAccueil() {
        AccueilView accueilView = new AccueilView(atelier, couleursPostes);
        root.setCenter(accueilView.getAccueilPane());
    }

    private void afficherFormulaireAjoutMachine() {
        root.setCenter(MachineFormView.getMachineForm(atelier, nomFichier, this::afficherAccueil));
    }

    private void afficherPoste() {
        root.setCenter(PosteFormView.getPosteForm(atelier, nomFichier, this::afficherAccueil));
    }

    private void afficherOperation() {
        // Nouvelle signature : passe atelier, operationsList, nomFichier, callback
        OperationView opView = new OperationView(atelier, operationsList, nomFichier, this::afficherAccueil);
        root.setCenter(opView.getView());
    }

    private void afficherProduit() {
        root.setCenter(ProduitFormView.getProduitForm(listeProduits, this::afficherAccueil));
    }

    private void afficherGamme() {
        // Passe bien le nomFichier (important pour la sauvegarde !)
        root.setCenter(GammeFormView.getGammeForm(atelier, gammesList, operationsList, nomFichier, this::afficherAccueil));
    }

    private void afficherListeProduits() {
        root.setCenter(ProduitFormView.getListeProduitsView(listeProduits, this::afficherAccueil));
    }

    private void afficherPersonne() {
        PersonneView personneView = new PersonneView(new PersonneController());
        root.setCenter(personneView.getView());
    }
    
    private void afficherOperateur() {
        OperateurView operateurView = new OperateurView(new OperateurController());
        root.setCenter(operateurView.getView());
    }

    private void afficherStockBrut() {
        StockBrutController stockBrutController = new StockBrutController();
        StockBrutView stockBrutView = new StockBrutView(stockBrutController,atelier,nomFichier);
        root.setCenter(stockBrutView);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
