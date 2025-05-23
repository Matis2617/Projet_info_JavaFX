package com.mycompany.projet_fx.View;

import com.mycompany.projet_fx.Model.Poste;
import com.mycompany.projet_fx.Model.Personne;
import com.mycompany.projet_fx.Model.Operateur;
import com.mycompany.projet_fx.Model.Operation;
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

    private final Color[] couleursPostes = {
            Color.ROYALBLUE, Color.DARKORANGE, Color.FORESTGREEN, Color.DARKVIOLET, Color.DARKCYAN,
            Color.CRIMSON, Color.DARKMAGENTA, Color.GOLD, Color.MEDIUMPURPLE, Color.DARKSLATEGRAY
    };


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

    // ------- MODULE GAMME -------
    private void afficherGamme() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Label titre = new Label("Gestion des Gammes");
        titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Listes à sélectionner pour composer la gamme
        Label opLabel = new Label("Opérations disponibles :");
        ListView<Operation> listOp = new ListView<>();
        if (atelier.getOperations() != null) {
            listOp.getItems().addAll(atelier.getOperations());
        }
        listOp.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Label eqLabel = new Label("Équipements disponibles :");
        ListView<Equipement> listEq = new ListView<>();
        listEq.getItems().addAll(atelier.getEquipements());
        listEq.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Création d'une gamme
        Label refGammeLabel = new Label("Référence de la gamme :");
        TextField refGammeInput = new TextField();

        Button creerBtn = new Button("Créer la gamme");
        creerBtn.setStyle("-fx-font-weight: bold; -fx-background-color: #8fd14f;");
        Label creerMsg = new Label();
        creerMsg.setStyle("-fx-text-fill: green;");

        creerBtn.setOnAction(e -> {
            String ref = refGammeInput.getText().trim();
            if (ref.isEmpty()) {
                creerMsg.setText("Référence obligatoire !");
                return;
            }
            var selectedOps = listOp.getSelectionModel().getSelectedItems();
            var selectedEqs = listEq.getSelectionModel().getSelectedItems();
            if (selectedOps == null || selectedOps.isEmpty() || selectedEqs == null || selectedEqs.isEmpty()) {
                creerMsg.setText("Sélectionnez opérations ET équipements !");
                return;
            }
            Gamme gamme = new Gamme(new ArrayList<>(selectedOps));
            gamme.setRefGamme(ref);
            gamme.setListeEquipements(new ArrayList<>(selectedEqs));
            gammesList.add(gamme);
            creerMsg.setText("Gamme créée !");
            refGammeInput.clear();
            listOp.getSelectionModel().clearSelection();
            listEq.getSelectionModel().clearSelection();
        });

        // ListView des gammes
        Label gammesLbl = new Label("Gammes créées :");
        ListView<Gamme> gammesView = new ListView<>(gammesList);
        gammesView.setPrefHeight(100);
        gammesView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Gamme g, boolean empty) {
                super.updateItem(g, empty);
                setText(empty || g == null ? "" : g.getRefGamme());
            }
        });

        // Boutons d'action sur la gamme sélectionnée
        HBox actions = new HBox(10);
        Button afficherBtn = new Button("Afficher");
        Button modifierBtn = new Button("Modifier");
        Button coutBtn = new Button("Calculer coût");
        Button dureeBtn = new Button("Calculer durée");
        actions.getChildren().addAll(afficherBtn, modifierBtn, coutBtn, dureeBtn);

        Label infoGamme = new Label();
        infoGamme.setStyle("-fx-font-size: 13px; -fx-padding: 5;");

        // Actions
        afficherBtn.setOnAction(e -> {
            Gamme g = gammesView.getSelectionModel().getSelectedItem();
            if (g != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Référence : ").append(g.getRefGamme()).append("\n");
                sb.append("Opérations :\n");
                for (Operation op : g.getOperations())
                    sb.append("- ").append(op.getId_operation()).append("\n");
                sb.append("Équipements :\n");
                for (Equipement eq : g.getListeEquipements())
                    sb.append("- ").append(eq.affiche()).append("\n");
                infoGamme.setText(sb.toString());
            }
        });

        modifierBtn.setOnAction(e -> {
            Gamme g = gammesView.getSelectionModel().getSelectedItem();
            if (g == null) return;
            // Ouvre un mini formulaire pour resélectionner ops & eqs
            Dialog<Void> modifDlg = new Dialog<>();
            modifDlg.setTitle("Modifier la gamme");
            VBox modifBox = new VBox(8);
            modifBox.setPadding(new Insets(8));

            ListView<Operation> opEdit = new ListView<>();
            if (atelier.getOperations() != null)
                opEdit.getItems().addAll(atelier.getOperations());
            opEdit.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            for (Operation op : g.getOperations())
                opEdit.getSelectionModel().select(op);

            ListView<Equipement> eqEdit = new ListView<>();
            eqEdit.getItems().addAll(atelier.getEquipements());
            eqEdit.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            for (Equipement eq : g.getListeEquipements())
                eqEdit.getSelectionModel().select(eq);

            modifBox.getChildren().addAll(
                new Label("Modifier opérations :"), opEdit,
                new Label("Modifier équipements :"), eqEdit
            );
            Button valider = new Button("Valider");
            valider.setOnAction(evt -> {
                g.setOperations(new ArrayList<>(opEdit.getSelectionModel().getSelectedItems()));
                g.setListeEquipements(new ArrayList<>(eqEdit.getSelectionModel().getSelectedItems()));
                modifDlg.setResult(null);
                modifDlg.close();
            });
            modifBox.getChildren().add(valider);
            modifDlg.getDialogPane().setContent(modifBox);
            modifDlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            modifDlg.showAndWait();
        });

        coutBtn.setOnAction(e -> {
            Gamme g = gammesView.getSelectionModel().getSelectedItem();
            if (g != null)
                showAlert("Coût gamme", "Coût de la gamme : " + g.coutGamme());
        });

        dureeBtn.setOnAction(e -> {
            Gamme g = gammesView.getSelectionModel().getSelectedItem();
            if (g != null)
                showAlert("Durée gamme", "Durée de la gamme : " + g.dureeGamme());
        });

        vbox.getChildren().addAll(
            titre,
            opLabel, listOp,
            eqLabel, listEq,
            refGammeLabel, refGammeInput, creerBtn, creerMsg,
            new Separator(),
            gammesLbl, gammesView, actions, infoGamme
        );
        root.setCenter(vbox);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // ------- FIN MODULE GAMME -------

    // ------- MODULE PRODUIT -------
    private void afficherProduit() {
        VBox box = new VBox(10);
        box.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField codeField = new TextField();
        codeField.setPromptText("Code Produit");

        TextField idField = new TextField();
        idField.setPromptText("ID Produit");

        Label erreurLabel = new Label();
        erreurLabel.setStyle("-fx-text-fill: red;");

        Button ajouterBtn = new Button("Ajouter le produit");
        ajouterBtn.setOnAction(e -> {
            try {
                int code = Integer.parseInt(codeField.getText());
                String id = idField.getText();

                Produit produit = new Produit(code, id);
                listeProduits.add(produit); // Ajoute à la liste observable
                System.out.println("Produit ajouté : " + produit);
                afficherAccueil();
            } catch (Exception ex) {
                erreurLabel.setText("Erreur : Données invalides.");
            }
        });

        Button retourBtn = new Button("Annuler");
        retourBtn.setOnAction(e -> afficherAccueil());

        box.getChildren().addAll(
                new Label("Ajouter un produit :"),
                codeField, idField,
                ajouterBtn, retourBtn, erreurLabel
        );
        root.setCenter(box);
    }

   private void afficherListeProduits() {
    VBox box = new VBox(15);
    box.setStyle("-fx-padding: 20; -fx-alignment: center;");

    Label titre = new Label("Liste des produits finis :");
    ListView<Produit> produitsListView = new ListView<>(listeProduits);
    produitsListView.setPrefHeight(150);

    Label detailsProduit = new Label();
    detailsProduit.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

    // Interaction : Affiche infos produit sélectionné
    produitsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal != null) {
            detailsProduit.setText(
                "Identifiant : " + newVal.getId() + "\n" +
                "Code produit : " + newVal.getCode()
            );
        } else {
            detailsProduit.setText("");
        }
    });

    // Optionnel : bouton pour supprimer le produit sélectionné
    Button supprimerBtn = new Button("Supprimer");
    supprimerBtn.setOnAction(e -> {
        Produit prod = produitsListView.getSelectionModel().getSelectedItem();
        if (prod != null) {
            listeProduits.remove(prod);
            detailsProduit.setText("");
        }
    });

    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> afficherAccueil());

    HBox actions = new HBox(10, supprimerBtn, retourBtn);
    actions.setStyle("-fx-alignment: center;");

    box.getChildren().addAll(
        titre,
        produitsListView,
        detailsProduit,
        actions
    );
    root.setCenter(box);
}
    // ------- FIN MODULE PRODUIT -------

    public static void main(String[] args) {
        launch(args);
    }
}
