package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Poste;
import com.mycompany.projet_fx.Model.Personne;
import com.mycompany.projet_fx.Model.Operateur;
import com.mycompany.projet_fx.Model.Operation;
import com.mycompany.projet_fx.Utils.AtelierSauvegarde;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Optional;
import java.io.*;
import javafx.geometry.Insets;
import com.mycompany.projet_fx.Model.Machine;
import com.mycompany.projet_fx.Model.Atelier;
import com.mycompany.projet_fx.Model.ChefAtelier;
import com.mycompany.projet_fx.Model.Equipement;
import com.mycompany.projet_fx.Model.Fiabilite;
import com.mycompany.projet_fx.Model.FiabiliteUtils;
import com.mycompany.projet_fx.Model.Gamme;
import com.mycompany.projet_fx.Model.Operateur;
import com.mycompany.projet_fx.Model.Operation;
import com.mycompany.projet_fx.Model.Personne;
import com.mycompany.projet_fx.Model.Poste;
import com.mycompany.projet_fx.Model.Produit;
import com.mycompany.projet_fx.Model.ProduitsFinis;
import com.mycompany.projet_fx.Model.StockBrut;

public class AtelierView extends Application {

    private BorderPane root;
    private Atelier atelier;
    private String nomFichier;
    private ObservableList<Gamme> gammesList = FXCollections.observableArrayList();
    private ObservableList<Produit> listeProduits = FXCollections.observableArrayList(); // Liste des produits

    @Override
    public void start(Stage primaryStage) {
        // Saisie du nom de l'utilisateur
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
            // Proposer de charger ou de créer un nouveau
            Alert choix = new Alert(Alert.AlertType.CONFIRMATION);
            choix.setTitle("Projet existant trouvé");
            choix.setHeaderText("Un projet existe déjà sous ce prénom.");
            choix.setContentText("Voulez-vous poursuivre l'ancien projet ou en créer un nouveau ?");
            ButtonType btnAncien = new ButtonType("Poursuivre");
            ButtonType btnNouveau = new ButtonType("Nouveau projet");
            choix.getButtonTypes().setAll(btnAncien, btnNouveau);
            Optional<ButtonType> option = choix.showAndWait();
            if (option.isPresent() && option.get() == btnAncien) {
                atelierCharge = chargerAtelier(nomFichier);
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

        // MenuBar/Menu
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        MenuItem accueilItem = new MenuItem("Accueil");
        MenuItem machineItem = new MenuItem("Machines");
        MenuItem personnesItem = new MenuItem("Personnes");
        MenuItem posteItem = new MenuItem("Poste");
        MenuItem produitItem = new MenuItem("Produit");
        MenuItem stockBrutItem = new MenuItem("Stock Brut");
        MenuItem gammeItem = new MenuItem("Gamme"); // Ajout du module gamme
        MenuItem listeProduitItem = new MenuItem("Produits finis");
        menu.getItems().addAll(accueilItem, machineItem, personnesItem, posteItem, produitItem, stockBrutItem, gammeItem, listeProduitItem);
        menuBar.getMenus().add(menu);

        root = new BorderPane();
        root.setTop(menuBar);

        accueilItem.setOnAction(e -> afficherAccueil());
        machineItem.setOnAction(e -> afficherFormulaireAjoutMachine());
        personnesItem.setOnAction(e -> afficherPlaceholder("Module Personnes à venir..."));
        posteItem.setOnAction(e -> afficherPoste());
        produitItem.setOnAction(e -> afficherProduit());
        stockBrutItem.setOnAction(e -> afficherPlaceholder("Module Stock Brut à venir..."));
        gammeItem.setOnAction(e -> afficherGamme());
        listeProduitItem.setOnAction(e -> afficherListeProduits());

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


    
    root.setCenter(MachineFormView.getMachineForm(atelier, nomFichier, this::afficherAccueil));
    root.setCenter(PlaceholderView.getPlaceholder("Module Personnes à venir..."));
    root.setCenter(PosteFormView.getPosteForm(atelier, nomFichier, this::afficherAccueil));
    root.setCenter(GammeFormView.getGammeForm(atelier, gammesList, this::afficherAccueil));
    root.setCenter(ProduitFormView.getProduitForm(listeProduits, this::afficherAccueil));
    root.setCenter(ProduitFormView.getListeProduitsView(listeProduits, this::afficherAccueil));

    public static void main(String[] args) {
        launch(args);
    }
}
