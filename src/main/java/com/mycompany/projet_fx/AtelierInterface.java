package com.mycompany.projet_fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;

public class AtelierInterface extends Application {

    private BorderPane root;
    private Atelier atelier;
    private TextField nomAtelierField;
    private TextField nomPersonneField;
    private TextField prenomPersonneField;
    private TextField competencesField;
    private ComboBox<String> roleComboBox;
    private ListView<Personne> personnesListView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Interface de Gestion d'Atelier");
        
        ArrayList<Equipement> equipements = new ArrayList<>();
        ArrayList<Operateur> operateurs = new ArrayList<>();
        ArrayList<Personne> personnes = new ArrayList<>();
        ArrayList<Gamme> gammes = new ArrayList<>();
        ArrayList<Fiabilite> fiabilites = new ArrayList<>();
        ArrayList<Poste> postes = new ArrayList<>();
        ArrayList<Produit> produits = new ArrayList<>();
        ArrayList<StockBrut> stockBrut = new ArrayList<>();

        // Initialisation de base
        atelier = new Atelier(1,"Atelier de ...",equipements,operateurs,personnes);

        // Menu
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Navigation");

        MenuItem accueilItem = new MenuItem("Accueil");
        MenuItem atelierItem = new MenuItem("Atelier");
        MenuItem equipementItem = new MenuItem("Équipements");
        MenuItem machineItem = new MenuItem("Machines");
        MenuItem operateurItem = new MenuItem("Opérateurs");
        MenuItem personnesItem = new MenuItem("Personnes");
        MenuItem gammeItem = new MenuItem("Gamme");
        MenuItem fiabiliteItem = new MenuItem("Fiabilite");
        MenuItem posteItem = new MenuItem("Poste");
        MenuItem produitItem = new MenuItem("Produit");
        MenuItem stockBrutItem = new MenuItem("Stock Brut");
        

        menu.getItems().addAll(accueilItem, atelierItem, equipementItem, machineItem, operateurItem, personnesItem, gammeItem, posteItem, produitItem, stockBrutItem);
        menuBar.getMenus().add(menu);

        // Layout principal
        root = new BorderPane();
        root.setTop(menuBar);

        // Actions
        accueilItem.setOnAction(e -> afficherAccueil());
        atelierItem.setOnAction(e -> afficherAtelier());
        equipementItem.setOnAction(e -> afficherEquipements());
        machineItem.setOnAction(e -> afficherMachines());
        operateurItem.setOnAction(e -> afficherOperateurs());
        personnesItem.setOnAction(e -> afficherPersonnes());
        personnesItem.setOnAction(e -> afficherGamme());
        personnesItem.setOnAction(e -> afficherFaibilite());
        personnesItem.setOnAction(e -> afficherPoste());
        personnesItem.setOnAction(e -> afficherProduit());
        personnesItem.setOnAction(e -> afficherStockBrut());

        afficherAccueil();

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void afficherAccueil() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Bienvenue dans le système de gestion d'atelier."));
        box.getChildren().add(new Label("Utilisez le menu pour accéder aux différentes classes."));
        root.setCenter(box);
    }

    private void afficherAtelier() {
        VBox box = new VBox(10);
        Label label = new Label("Nom de l'atelier : " + atelier.getNom());
        nomAtelierField = new TextField(atelier.getNom());

        Button modifierButton = new Button("Modifier le nom");
        modifierButton.setOnAction(e -> {
            atelier.setNom(nomAtelierField.getText());
            label.setText("Nom de l'atelier : " + atelier.getNom());
        });

        box.getChildren().addAll(label, nomAtelierField, modifierButton);
        box.getChildren().add(new Label("Nombre d'équipements : " + atelier.getEquipements().size()));
        box.getChildren().add(new Label("Nombre d'opérateurs : " + atelier.getOperateurs().size()));
        
        if (atelier.getChefAtelier() != null) {
            box.getChildren().add(new Label("Chef d'atelier : " + atelier.getChefAtelier().affiche()));
        }

        root.setCenter(box);
    }

    private void afficherEquipements() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Liste des équipements :"));
        for (Equipement eq : atelier.getEquipements()) {
            Label label = new Label(eq.affiche());
            box.getChildren().add(label);
        }

        Button ajouter = new Button("Ajouter un équipement");
        ajouter.setOnAction(e -> {
            Equipement eq = new Equipement(atelier.getEquipements().size() + 1);
            atelier.getEquipements().add(eq);
            afficherEquipements();
        });

        box.getChildren().add(ajouter);
        root.setCenter(box);
    }

    private void afficherMachines() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Liste des machines :"));
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) {
                Label label = new Label(eq.affiche());
                box.getChildren().add(label);
            }
        }

        Button ajouter = new Button("Ajouter une machine");
        ajouter.setOnAction(e -> {
            Machine m = new Machine(atelier.getEquipements().size() + 1,1,"Machine","Type",0,0,0,0,Machine.ETAT.disponible);
            atelier.getEquipements().add(m);
            afficherMachines();
        });

        box.getChildren().add(ajouter);
        root.setCenter(box);
    }

    private void afficherOperateurs() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Liste des opérateurs :"));
        for (Operateur op : atelier.getOperateurs()) {
                Label label = new Label(op.affiche());
                box.getChildren().add(label);
            
        }

        Button ajouter = new Button("Ajouter un opérateur");
        ajouter.setOnAction(e -> {
            Operateur o = new Operateur("ID" + (atelier.getOperateurs().size() + 1),"Nom","Prenom","Competences",1,Machine.ETAT.disponible);
            atelier.getOperateurs().add(o);
            afficherOperateurs();
        });

        box.getChildren().add(ajouter);
        root.setCenter(box);
    }
    
    private void afficherPersonnes() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Ajouter une personne :"));

        TextField nomPersonneField = new TextField();
        nomPersonneField.setPromptText("Nom");

        TextField prenomPersonneField = new TextField();
        prenomPersonneField.setPromptText("Prénom");

        TextField competencesField = new TextField();
        competencesField.setPromptText("Compétences");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Opérateur", "Chef d'Atelier");
        roleComboBox.setPromptText("Rôle");

        Button ajouter = new Button("Ajouter la personne");
        ajouter.setOnAction(e -> {
            String idPersonne = "ID" + (atelier.getPersonnes().size() + 1);
            String nom = nomPersonneField.getText();
            String prenom = prenomPersonneField.getText();
            String competences = competencesField.getText();
            String role = roleComboBox.getValue();

            if (role != null) {
                Personne personne;
                if (role.equals("Opérateur")) {
                    personne = new Operateur(idPersonne, nom, prenom, competences, 1, Machine.ETAT.disponible);
                    atelier.getOperateurs().add((Operateur) personne);
                } else if (role.equals("Chef d'Atelier")) {
                    personne = new ChefAtelier(idPersonne, nom, prenom);
                    atelier.setChefAtelier((ChefAtelier) personne);
                }
                afficherPersonnes();
            }
        });
        ListView<Personne> personnesListView = new ListView<>();
        personnesListView.getItems().setAll(atelier.getPersonnes());

        Button supprimer = new Button("Supprimer la personne sélectionnée");
        supprimer.setOnAction(e -> {
            Personne selectedPersonne = personnesListView.getSelectionModel().getSelectedItem();
            if (selectedPersonne != null) {
                atelier.getPersonnes().remove(selectedPersonne);
                if (selectedPersonne instanceof Operateur) {
                    atelier.getOperateurs().remove(selectedPersonne);
                } else if (selectedPersonne instanceof ChefAtelier) {
                    atelier.setChefAtelier(null);
                }
                afficherPersonnes();
            }
        });
        
        box.getChildren().addAll(nomPersonneField, prenomPersonneField, competencesField, roleComboBox, ajouter, personnesListView, supprimer);
        root.setCenter(box);
    }

    private void afficherGamme(){
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Gestion des Gammes :"));
        
        TextField refGammeField = new TextField();
        refGammeField.setPromptText("Référence de la gamme");
        
        Button creerGammeButton = new Button ("Créer une gamme");
        creerGammeButton.setOnAction(e ->{
            Gamme gamme = new Gamme(new ArrayList<>());
            gamme.creerGamme();
            gammes.add(gamme);
            afficherGamme();
        });
        
        ListView<Gamme> gammesListView = new ListView <>();
        gammesListView.getItems().setAll(gammes);
        
        Button supprimerGammeButton = new Button ("Supprimer la game selectionnée");
        supprimerGamme.button.setOnAction(e->{
            Gamme selectedGamme = gammesListView.getSelectionModel().getSelectedItem();
            if (selectedGamme != null){
                gammes.remove(selectedGamme);
                afficherGamme();
            }
        });
        
        box.getChildren().addAll(refGammeField, creerGammeButton, gammesListView, supprimerGammeButton);
        root.setCenter(box);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
    