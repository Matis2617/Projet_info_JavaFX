package com.mycompany.projet_fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;

public class AtelierInterface extends Application {

class Equipement {
    private String nom;

    public Equipement(String nom) {
        this.nom = nom;
    }

    public String affiche() {
        return nom;
    }
}

// Sous-classe pour les machines
class Machine extends Equipement {
    public Machine(String nom) {
        super(nom);
    }
}

// Sous-classe pour les opérateurs
class Operateur extends Equipement {
    public Operateur(String nom) {
        super(nom);
    }
}

// Classe Atelier
class Atelier {
    private int id;
    private String nom;
    private ArrayList<Equipement> equipements;

    public Atelier(int id, String nom, ArrayList<Equipement> equipements) {
        this.id = id;
        this.nom = nom;
        this.equipements = equipements;
    }

    public String getNom() {
        return nom;
    }

    public ArrayList<Equipement> getEquipement() {
        return equipements;
    }
}
    private BorderPane root;
    private Atelier atelier;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Interface de Gestion d'Atelier");
        
        ArrayList<Equipement> equipement = new ArrayList<>();

        // Initialisation de base
        atelier = new Atelier(id,nom,equipement);

        // Menu
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Navigation");

        MenuItem accueilItem = new MenuItem("Accueil");
        MenuItem atelierItem = new MenuItem("Atelier");
        MenuItem equipementItem = new MenuItem("Équipements");
        MenuItem machineItem = new MenuItem("Machines");
        MenuItem operateurItem = new MenuItem("Opérateurs");

        menu.getItems().addAll(accueilItem, atelierItem, equipementItem, machineItem, operateurItem);
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

        afficherAccueil();

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void afficherAccueil() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Bienvenue dans le système de gestion d'atelier."));
        box.getChildren().add(new Label("Utilisez le menu pour accéder aux différentes sections."));
        root.setCenter(box);
    }

    private void afficherAtelier() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Nom de l'atelier : " + atelier.getNom()));
        box.getChildren().add(new Label("Nombre d'équipements : " + atelier.getEquipement().size()));
        root.setCenter(box);
    }

    private void afficherEquipements() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Liste des équipements :"));
        for (Equipement eq : atelier.getEquipement()) {
            Label label = new Label(eq.affiche());
            box.getChildren().add(label);
        }

        Button ajouter = new Button("Ajouter un équipement");
        ajouter.setOnAction(e -> {
            Equipement eq = new Equipement("Eq" + (atelier.getEquipement().size() + 1));
            atelier.getEquipement().add(eq);
            afficherEquipements();
        });

        box.getChildren().add(ajouter);
        root.setCenter(box);
    }

    private void afficherMachines() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Liste des machines :"));
        for (Equipement eq : atelier.getEquipement()) {
            if (eq instanceof Machine) {
                Label label = new Label(eq.affiche());
                box.getChildren().add(label);
            }
        }

        Button ajouter = new Button("Ajouter une machine");
        ajouter.setOnAction(e -> {
            Machine m = new Machine("Machine" + (atelier.getEquipement().size() + 1));
            atelier.getEquipement().add(m);
            afficherMachines();
        });

        box.getChildren().add(ajouter);
        root.setCenter(box);
    }

    private void afficherOperateurs() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Liste des opérateurs :"));
        for (Equipement eq : atelier.getEquipement()) {
            if (eq instanceof Operateur) {
                Label label = new Label(eq.affiche());
                box.getChildren().add(label);
            }
        }

        Button ajouter = new Button("Ajouter un opérateur");
        ajouter.setOnAction(e -> {
            Operateur o = new Operateur("Op" + (atelier.getEquipement().size() + 1));
            atelier.getEquipement().add(o);
            afficherOperateurs();
        });

        box.getChildren().add(ajouter);
        root.setCenter(box);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
    