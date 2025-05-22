package com.mycompany.projet_fx;

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

public class AtelierInterface extends Application {

    private BorderPane root;
    private Atelier atelier;
    private String nomFichier;
    private ObservableList<Gamme> gammesList = FXCollections.observableArrayList();

    private final Color[] couleursPostes = {
            Color.ROYALBLUE, Color.DARKORANGE, Color.FORESTGREEN, Color.DARKVIOLET, Color.DARKCYAN,
            Color.CRIMSON, Color.DARKMAGENTA, Color.GOLD, Color.MEDIUMPURPLE, Color.DARKSLATEGRAY
    };

    // Sauvegarde l’atelier dans un fichier
    private void sauvegarderAtelier(Atelier atelier, String nomFichier) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
            oos.writeObject(atelier);
            System.out.println("[SAUVEGARDE] Atelier sauvegardé dans " + nomFichier
                + " (" + atelier.getEquipements().size() + " machines, " + atelier.getPostes().size() + " postes)");
        } catch (Exception e) {
            System.err.println("[ERREUR] Sauvegarde échouée !");
            e.printStackTrace();
        }
    }

    // Charge un atelier depuis un fichier
    private Atelier chargerAtelier(String nomFichier) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomFichier))) {
            Atelier a = (Atelier) ois.readObject();
            System.out.println("[CHARGEMENT] Atelier chargé depuis " + nomFichier
                + " (" + a.getEquipements().size() + " machines, " + a.getPostes().size() + " postes)");
            return a;
        } catch (Exception e) {
            System.err.println("[ERREUR] Chargement échoué !");
            e.printStackTrace();
            return null;
        }
    }

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
        menu.getItems().addAll(accueilItem, machineItem, personnesItem, posteItem, produitItem, stockBrutItem, gammeItem);
        menuBar.getMenus().add(menu);

        root = new BorderPane();
        root.setTop(menuBar);

        accueilItem.setOnAction(e -> afficherAccueil());
        machineItem.setOnAction(e -> afficherFormulaireAjoutMachine());
        personnesItem.setOnAction(e -> afficherPlaceholder("Module Personnes à venir..."));
        posteItem.setOnAction(e -> afficherPoste());
        produitItem.setOnAction(e -> afficherPlaceholder("Module Produit à venir..."));
        stockBrutItem.setOnAction(e -> afficherPlaceholder("Module Stock Brut à venir..."));
        gammeItem.setOnAction(e -> afficherGamme());

        afficherAccueil();

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setTitle("Atelier de " + atelier.getNom());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ACCUEIL : juste le plan au centre (centré visuellement)
    private void afficherAccueil() {
        VBox accueil = new VBox(15);
        accueil.setStyle("-fx-alignment: center; -fx-padding: 20;");
        accueil.getChildren().add(new Label("Bienvenue dans l'atelier de " + atelier.getNom() + "."));
        accueil.getChildren().add(new Label("Plan de l'atelier :"));

        HBox hbox = new HBox();
        hbox.setStyle("-fx-alignment: center;");
        hbox.getChildren().add(creerPlanAtelier());
        accueil.getChildren().add(hbox);

        root.setCenter(accueil);
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

    // Génération du plan de l’atelier
    private Pane creerPlanAtelier() {
        Pane planPane = new Pane();
        int tailleAtelier = 500;
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
                carre.setFill(getColorForMachine(m));
                carre.setStroke(Color.DARKBLUE);
                carre.setArcWidth(8); carre.setArcHeight(8);

                // Détails au clic
                carre.setOnMouseClicked(ev -> afficherFicheMachine(m));

                planPane.getChildren().add(carre);
            }
        }
        return planPane;
    }

    // Pop-up d’information sur une machine
    private void afficherFicheMachine(Machine m) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la machine");
        alert.setHeaderText(m.getDmachine());
        alert.setContentText(
                "Description: " + m.getDmachine() + "\n"
                + "Abscisse: " + m.getAbscisse() + "\n"
                + "Ordonnée: " + m.getOrdonnee() + "\n"
                + "Coût: " + m.getC() + "\n"
                + "Temps de préparation: " + m.getT() + "\n"
                + "État: " + m.getEtat()
        );
        alert.showAndWait();
    }

    // Formulaire d’ajout de machine
    private void afficherFormulaireAjoutMachine() {
        VBox box = new VBox(10);
        box.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField idField = new TextField();
        idField.setPromptText("Identifiant");

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
                            id, desc, absc, ord, cout, temps, etat
                    );
                    atelier.getEquipements().add(m);
                    sauvegarderAtelier(atelier, nomFichier);
                    System.out.println("[AJOUT] Machine ajoutée. Total: " + atelier.getEquipements().size());
                    afficherAccueil();
                }
            } catch (Exception ex) {
                erreurLabel.setText("Erreur : Données invalides.");
            }
        });

        Button retourBtn = new Button("Annuler");
        retourBtn.setOnAction(e -> afficherAccueil());

        box.getChildren().addAll(
                new Label("Ajouter une machine :"),
                idField, descField, abscField, ordField, coutField, tempsField,
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

    // Ajout d'un poste
    private void afficherPoste() {
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 30; -fx-alignment: center;");

        // Si aucune machine, avertir
        if (atelier.getEquipements().stream().noneMatch(eq -> eq instanceof Machine)) {
            vbox.getChildren().add(new Label("Aucune machine créée. Créez des machines avant de créer un poste."));
            root.setCenter(vbox);
            return;
        }

        // Sélection multiple des machines
        Label label = new Label("Sélectionnez les machines pour créer un poste :");
        ListView<Machine> machineListView = new ListView<>();
        for (Equipement eq : atelier.getEquipements()) {
            if (eq instanceof Machine) {
                machineListView.getItems().add((Machine) eq);
            }
        }
        machineListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        machineListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Machine m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty || m == null ? "" : m.getDmachine());
            }
        });

        TextField posteDescField = new TextField();
        posteDescField.setPromptText("Description du poste");

        Button creerBtn = new Button("Créer le poste");
        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        creerBtn.setOnAction(e -> {
            var selectedMachines = machineListView.getSelectionModel().getSelectedItems();
            String desc = posteDescField.getText().isBlank() ? "Poste sans nom" : posteDescField.getText();
            if (selectedMachines == null || selectedMachines.isEmpty()) {
                message.setText("Sélectionnez au moins une machine.");
                return;
            }
            // Crée le poste avec refposte auto-incrémenté
            ArrayList<Machine> machinesForPoste = new ArrayList<>(selectedMachines);
            Poste nouveauPoste = new Poste(
                    atelier.getPostes().size() + 1, desc, machinesForPoste, atelier.getPostes().size() + 1000);
            atelier.getPostes().add(nouveauPoste);
            sauvegarderAtelier(atelier, nomFichier);
            System.out.println("[AJOUT] Poste ajouté. Total: " + atelier.getPostes().size());
            message.setText("Poste créé !");
            afficherPoste();
        });

        // Affiche les postes déjà créés
        VBox postesBox = new VBox(10);
        postesBox.getChildren().add(new Label("Postes déjà créés :"));
        for (Poste p : atelier.getPostes()) {
            String machinesStr = String.join(", ",
                    p.getMachines().stream().map(Machine::getDmachine).toArray(String[]::new));
            postesBox.getChildren().add(new Label("• " + p.getDposte() + " : " + machinesStr));
        }

        vbox.getChildren().addAll(label, machineListView, posteDescField, creerBtn, message, new Separator(), postesBox);
        root.setCenter(vbox);
    }

    // ------- MODULE GAMME (Ajout du code de ton camarade) -------
    private void afficherGamme() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));

        // Référence Gamme
        Label refGammeLabel = new Label("Référence Gamme:");
        TextField refGammeInput = new TextField();
        refGammeInput.setPromptText("Référence Gamme");

        // Operations
        Label operationsLabel = new Label("Opérations:");
        ListView<Operation> operationsListView = new ListView<>();

        // Equipements
        Label equipementsLabel = new Label("Équipements:");
        ListView<Equipement> equipementsListView = new ListView<>();

        // ListView des gammes
        ListView<Gamme> gammesListView = new ListView<>(gammesList);

        // Boutons
        Button creerButton = new Button("Créer");
        Button modifierButton = new Button("Modifier");
        Button supprimerButton = new Button("Supprimer");
        Button afficherButton = new Button("Afficher");
        Button coutButton = new Button("Calculer Coût");
        Button dureeButton = new Button("Calculer Durée");

        // Ajoutez les composants au GridPane
        grid.add(refGammeLabel, 0, 0);
        grid.add(refGammeInput, 1, 0);
        grid.add(operationsLabel, 0, 1);
        grid.add(operationsListView, 1, 1);
        grid.add(equipementsLabel, 0, 2);
        grid.add(equipementsListView, 1, 2);
        grid.add(creerButton, 0, 3);
        grid.add(modifierButton, 1, 3);
        grid.add(supprimerButton, 2, 3);
        grid.add(afficherButton, 3, 3);
        grid.add(coutButton, 0, 4);
        grid.add(dureeButton, 1, 4);
        grid.add(gammesListView, 1, 5);

        // Actions des boutons
        creerButton.setOnAction(e -> {
            Gamme gamme = new Gamme(new ArrayList<>());
            gamme.creerGamme();
            gammesList.add(gamme);
            refGammeInput.setText(gamme.getRefGamme());
        });

        supprimerButton.setOnAction(e -> {
            Gamme selectedGamme = gammesListView.getSelectionModel().getSelectedItem();
            if (selectedGamme != null) {
                gammesList.remove(selectedGamme);
            }
        });

        afficherButton.setOnAction(e -> {
            Gamme selectedGamme = gammesListView.getSelectionModel().getSelectedItem();
            if (selectedGamme != null) {
                selectedGamme.afficheGamme();
            }
        });

        coutButton.setOnAction(e -> {
            Gamme selectedGamme = gammesListView.getSelectionModel().getSelectedItem();
            if (selectedGamme != null) {
                float cout = selectedGamme.coutGamme();
                showAlert("Coût de la Gamme", "Le coût de la gamme est: " + cout);
            }
        });

        dureeButton.setOnAction(e -> {
            Gamme selectedGamme = gammesListView.getSelectionModel().getSelectedItem();
            if (selectedGamme != null) {
                float duree = selectedGamme.dureeGamme();
                showAlert("Durée de la Gamme", "La durée de la gamme est: " + duree);
            }
        });

        root.setCenter(grid);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // ------- FIN MODULE GAMME -------

    public static void main(String[] args) {
        launch(args);
    }
}
