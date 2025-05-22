package com.mycompany.projet_fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Optional;

public class AtelierInterface extends Application {

    private BorderPane root;
    private Atelier atelier;

    @Override
    public void start(Stage primaryStage) {
        // Saisie du nom de l'utilisateur
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Bienvenue !");
        dialog.setHeaderText("Bienvenue dans la gestion de l'atelier");
        dialog.setContentText("Veuillez entrer votre nom :");
        Optional<String> result = dialog.showAndWait();
        String nomUtilisateur = result.orElse("Utilisateur");

        ArrayList<Equipement> equipements = new ArrayList<>();
        ArrayList<Operateur> operateurs = new ArrayList<>();
        ArrayList<Personne> personnes = new ArrayList<>();

        atelier = new Atelier(1, nomUtilisateur, equipements, operateurs, personnes);

        // Menu minimaliste
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Navigation");
        MenuItem accueilItem = new MenuItem("Accueil");
        MenuItem machineItem = new MenuItem("Machines");
        MenuItem personnesItem = new MenuItem("Personnes");
        MenuItem posteItem = new MenuItem("Poste");
        MenuItem produitItem = new MenuItem("Produit");
        MenuItem stockBrutItem = new MenuItem("Stock Brut");
        menu.getItems().addAll(accueilItem, machineItem, personnesItem, posteItem, produitItem, stockBrutItem);
        menuBar.getMenus().add(menu);

        // Layout principal
        root = new BorderPane();
        root.setTop(menuBar);

        // Affichage de l'accueil avec le plan au centre
        afficherAccueil();

        // Menu navigation
        accueilItem.setOnAction(e -> afficherAccueil());
        machineItem.setOnAction(e -> afficherMachines());
        personnesItem.setOnAction(e -> afficherPlaceholder("Module Personnes à venir..."));
        posteItem.setOnAction(e -> afficherPlaceholder("Module Poste à venir..."));
        produitItem.setOnAction(e -> afficherPlaceholder("Module Produit à venir..."));
        stockBrutItem.setOnAction(e -> afficherPlaceholder("Module Stock Brut à venir..."));

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setTitle("Atelier de " + nomUtilisateur);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ACCUEIL : juste le plan au centre
    private void afficherAccueil() {
        VBox accueil = new VBox(15);
        accueil.setStyle("-fx-alignment: center; -fx-padding: 20;");
        accueil.getChildren().add(new Label("Bienvenue dans l'atelier de " + atelier.getNom() + "."));
        accueil.getChildren().add(new Label("Plan de l'atelier"));
        accueil.getChildren().add(creerPlanAtelier(false)); // false = pas de bouton d'ajout
        root.setCenter(accueil);
    }

    // Onglet MACHINES : plan + bouton d'ajout + formulaire
    private void afficherMachines() {
        VBox machinesBox = new VBox(15);
        machinesBox.setStyle("-fx-alignment: center; -fx-padding: 20;");
        machinesBox.getChildren().add(new Label("Gestion des machines dans l'atelier :"));
        machinesBox.getChildren().add(creerPlanAtelier(true)); // true = bouton d'ajout visible
        root.setCenter(machinesBox);
    }

    // Génération du plan de l’atelier (option bouton d’ajout)
    private Pane creerPlanAtelier(boolean boutonAjout) {
        Pane planPane = new Pane();
        int tailleAtelier = 500; // Taille en pixels pour 50x50 "mètres"
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
                carre.setFill(Color.BLACK);
                carre.setStroke(Color.DARKBLUE);
                carre.setArcWidth(8); carre.setArcHeight(8);

                // Détails au clic
                carre.setOnMouseClicked(ev -> afficherFicheMachine(m));

                planPane.getChildren().add(carre);
            }
        }

        // Bouton d'ajout uniquement dans "Machines"
        if (boutonAjout) {
            Button ajouterBtn = new Button("Ajouter une machine");
            ajouterBtn.setLayoutX(10);
            ajouterBtn.setLayoutY(10);
            ajouterBtn.setOnAction(e -> afficherFormulaireAjoutMachine());
            planPane.getChildren().add(ajouterBtn);
        }

        return planPane;
    }

    // Pop-up d’information sur une machine
    private void afficherFicheMachine(Machine m) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la machine");
        alert.setHeaderText("Machine " + m.getRefmachine());
        alert.setContentText(
            "Type: " + m.getType() + "\n"
            + "Description: " + m.getDmachine() + "\n"
            + "Référence: " + m.getRefEquipement() + "\n"
            + "Coût: " + m.getC() + "\n"
            + "Temps de préparation: " + m.getT() + "\n"
            + "État: " + m.getEtat()
        );
        alert.showAndWait();
    }

    // Formulaire d’ajout de machine
    private void afficherFormulaireAjoutMachine() {
        VBox box = new VBox(10);
        box.setStyle("-fx-padding: 20;");
        TextField idField = new TextField();
        idField.setPromptText("Identifiant");

        TextField refField = new TextField();
        refField.setPromptText("Référence");

        TextField typeField = new TextField();
        typeField.setPromptText("Type");

        TextField descField = new TextField();
        descField.setPromptText("Description");

        TextField abscField = new TextField();
        abscField.setPromptText("Abscisse (0-49)");

        TextField ordField = new TextField();
        ordField.setPromptText("Ordonnée (0-49)");

        TextField coutField = new TextField();
        coutField.setPromptText("Coût");

        TextField tempsField = new TextField();
        tempsField.setPromptText("Temps préparation");

        ComboBox<Machine.ETAT> etatBox = new ComboBox<>();
        etatBox.getItems().addAll(Machine.ETAT.values());
        etatBox.setPromptText("État");

        Label erreurLabel = new Label();
        erreurLabel.setStyle("-fx-text-fill: red;");

        Button ajouterBtn = new Button("Ajouter la machine");
        ajouterBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String ref = refField.getText();
                String type = typeField.getText();
                String desc = descField.getText();
                int absc = Integer.parseInt(abscField.getText());
                int ord = Integer.parseInt(ordField.getText());
                float cout = Float.parseFloat(coutField.getText());
                float temps = Float.parseFloat(tempsField.getText());
                Machine.ETAT etat = etatBox.getValue();

                if (absc < 0 || absc >= 50 || ord < 0 || ord >= 50) {
                    erreurLabel.setText("Erreur : Coordonnées hors de l'atelier !");
                    return;
                }

                // Vérification unicité
                boolean existe = false;
                for (Equipement eq : atelier.getEquipements()) {
                    if (eq instanceof Machine) {
                        Machine m = (Machine) eq;
                        if (m.getRefmachine() == id) {
                            existe = true;
                            erreurLabel.setText("Erreur : Identifiant déjà utilisé !");
                            break;
                        }
                        if (m.getAbscisse() == absc && m.getOrdonnee() == ord) {
                            existe = true;
                            erreurLabel.setText("Erreur : Coordonnées déjà utilisées !");
                            break;
                        }
                    }
                }
                if (!existe) {
                    Machine m = new Machine(
                        atelier.getEquipements().size() + 1,
                        id, desc, type, absc, ord, cout, temps, etat
                    );
                    m.setRefEquipement(ref);
                    atelier.getEquipements().add(m);
                    afficherMachines(); // Retour au plan de machines
                }
            } catch (Exception ex) {
                erreurLabel.setText("Erreur : Données invalides.");
            }
        });

        Button retourBtn = new Button("Annuler");
        retourBtn.setOnAction(e -> afficherMachines());

        box.getChildren().addAll(
                new Label("Ajouter une machine :"),
                idField, refField, typeField, descField,
                abscField, ordField, coutField, tempsField,
                etatBox, ajouterBtn, retourBtn, erreurLabel
        );
        root.setCenter(box);
    }

    // Placeholder pour les autres modules
    private void afficherPlaceholder(String texte) {
        VBox box = new VBox(15);
        box.setStyle("-fx-alignment: center; -fx-padding: 60;");
        box.getChildren().add(new Label(texte));
        root.setCenter(box);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
