package com.mycompany.projet_fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class AtelierInterface extends Application {

    private BorderPane root;
    private Atelier atelier;
    private TextField nomAtelierField;
    private TextField nomPersonneField;
    private TextField prenomPersonneField;
    private TextField competencesField;
    private ComboBox<String> roleComboBox;
    private ListView<Personne> personnesListView;
    
    ArrayList<Gamme> gammes = new ArrayList<>();
    ArrayList<Fiabilite> fiabilites = new ArrayList<>();
    ArrayList<Poste> postes = new ArrayList<>();
    ArrayList<Produit> produits = new ArrayList<>();
    ArrayList<StockBrut> stockBrut = new ArrayList<>();
    
    @Override
    public void start(Stage primaryStage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Bienvenue !");
        dialog.setHeaderText("Bienvenue dans la gestion de l'atelier");
        dialog.setContentText("Veuillez entrer votre nom :");
        
        Optional<String> result = dialog.showAndWait();
        String nomUtilisateur = result.orElse("Utilisateur");
        
        primaryStage.setTitle("Atelier de "+nomUtilisateur);
        
        ArrayList<Equipement> equipements = new ArrayList<>();
        ArrayList<Operateur> operateurs = new ArrayList<>();
        ArrayList<Personne> personnes = new ArrayList<>();
        
// Initialisation de base
        atelier = new Atelier(1,nomUtilisateur,equipements,operateurs,personnes);

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
        MenuItem planAtelierItem =new MenuItem("Plan");
        

        menu.getItems().addAll(accueilItem, atelierItem, equipementItem, machineItem, operateurItem, personnesItem, gammeItem, fiabiliteItem, posteItem, produitItem, stockBrutItem,planAtelierItem);
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
        gammeItem.setOnAction(e -> afficherGamme());
        fiabiliteItem.setOnAction(e -> afficherFiabilite());
        posteItem.setOnAction(e -> afficherPoste());
        produitItem.setOnAction(e -> afficherProduit());
        stockBrutItem.setOnAction(e -> afficherStockBrut());
        planAtelierItem.setOnAction(e -> afficherPlanAtelier());

        afficherAccueil();

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void afficherAccueil() {
        VBox box = new VBox(20);
        box.getChildren().add(new Label("Bienvenue dans l'atelier de "+atelier.getNom()+"."));
        box.getChildren().add(new Label("Utilisez le menu pour accéder aux différentes fonctionnalités."));
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

        nomPersonneField = new TextField();
        nomPersonneField.setPromptText("Nom");

        prenomPersonneField = new TextField();
        prenomPersonneField.setPromptText("Prénom");

        competencesField = new TextField();
        competencesField.setPromptText("Compétences");

        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Opérateur", "Chef d'Atelier");
        roleComboBox.setPromptText("Rôle");

        Button ajouter = new Button("Ajouter la personne");
        ajouter.setOnAction(e -> {
            String idPersonne = "ID" + (atelier.getPersonnes().size() + 1);
            String nom = nomPersonneField.getText();
            String prenom = prenomPersonneField.getText();
            String competences = competencesField.getText();
            String role = roleComboBox.getValue();
            
            Personne personne = null;

            if (role != null) {
                if (role.equals("Opérateur")) {
                    personne = new Operateur(idPersonne, nom, prenom, competences, 1, Machine.ETAT.disponible);
                    atelier.getOperateurs().add((Operateur) personne);
                } else if (role.equals("Chef d'Atelier")) {
                    personne = new ChefAtelier(idPersonne, nom, prenom);
                    atelier.setChefAtelier((ChefAtelier) personne);
                }
                atelier.getPersonnes().add(personne);
                afficherPersonnes();
            }
        });
        personnesListView = new ListView<>();
        personnesListView.getItems().setAll(atelier.getPersonnes());

        Button supprimer = new Button("Supprimer la personne sélectionnée");
        supprimer.setOnAction(e -> {
            Personne selectedPersonne = personnesListView.getSelectionModel().getSelectedItem();
            if (selectedPersonne != null) {
                atelier.getPersonnes().remove(selectedPersonne);
                if (selectedPersonne instanceof Operateur) {
                    atelier.getPersonnes().remove(selectedPersonne);
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
        supprimerGammeButton.setOnAction(e -> {
            Gamme selectedGamme = gammesListView.getSelectionModel().getSelectedItem();
            if (selectedGamme != null){
                gammes.remove(selectedGamme);
                afficherGamme();
            }
        });
        
        box.getChildren().addAll(refGammeField, creerGammeButton, gammesListView, supprimerGammeButton);
        root.setCenter(box);
    }
    private void afficherFiabilite(){
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Gestion de la fiabilité :"));
        
        TextField nomMachineField = new TextField();
        nomMachineField.setPromptText("Nom de la machine");
        
        Button ajouterFiabiliteButton = new Button ("Ajouter une fiabilité");
        ajouterFiabiliteButton.setOnAction(e ->{
            Fiabilite fiabilite = new Fiabilite(nomMachineField.getText());
            fiabilites.add(fiabilite);
            afficherFiabilite();
        });
        
        ListView<Fiabilite> fiabilitesListView = new ListView <>();
        fiabilitesListView.getItems().setAll(fiabilites);
        
        Button supprimerFiabiliteButton = new Button ("Supprimer la game selectionnée");
        supprimerFiabiliteButton.setOnAction(e->{
            Fiabilite selectedFiabilite = fiabilitesListView.getSelectionModel().getSelectedItem();
            if (selectedFiabilite != null){
                fiabilites.remove(selectedFiabilite);
                afficherFiabilite();
            }
        });
        
        box.getChildren().addAll(nomMachineField, ajouterFiabiliteButton, fiabilitesListView, supprimerFiabiliteButton);
        root.setCenter(box);
    }
    private void afficherPoste(){
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Gestion des postes :"));
        
        TextField refPosteField = new TextField();
        refPosteField.setPromptText("Référence du poste");
        
        TextField dPosteField = new TextField();
        dPosteField.setPromptText("Description du poste");
        
        Button ajouterPosteButton = new Button ("Ajouter un poste");
        ajouterPosteButton.setOnAction(e ->{
            Poste poste = new Poste(1,dPosteField.getText(),new ArrayList<>(),1);
            postes.add(poste);
            afficherPoste();
        });
        
        ListView<Poste> postesListView = new ListView <>();
        postesListView.getItems().setAll(postes);
        
        Button supprimerPosteButton = new Button ("Supprimer le poste selectionné");
        supprimerPosteButton.setOnAction(e->{
            Poste selectedPoste = postesListView.getSelectionModel().getSelectedItem();
            if (selectedPoste != null){
                postes.remove(selectedPoste);
                afficherPoste();
            }
        });
        
        box.getChildren().addAll(refPosteField, dPosteField, ajouterPosteButton, postesListView,supprimerPosteButton);
        root.setCenter(box);
    }
    private void afficherProduit(){
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Module Produit à developper..."));
        root.setCenter(box);
    }
    private void afficherStockBrut(){
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Module StockBrut à developper..."));
        root.setCenter(box);
    }
    public static void main(String[] args) {
        launch(args);
    }
   private void afficherPlanAtelier() {
    Pane planPane = new Pane();
    planPane.setPrefSize(700, 500);

    // Affichage des machines sur le plan
    for (Equipement eq : atelier.getEquipements()) {
        if (eq instanceof Machine) {
            Machine m = (Machine) eq;
            double x = m.getAbscisse();  // Attention : à adapter à l'échelle de ton plan
            double y = m.getOrdonnee();
            // Affichage simple : cercle cliquable
            Circle circle = new Circle(x, y, 25, Color.LIGHTBLUE);
            circle.setStroke(Color.DARKBLUE);
            planPane.getChildren().add(circle);

            // Détails au clic
            circle.setOnMouseClicked(event -> afficherFicheMachine(m));
        }
    }

    // Bouton pour ajouter une machine
    Button ajouterBtn = new Button("Ajouter une machine");
    ajouterBtn.setLayoutX(10);
    ajouterBtn.setLayoutY(10);
    ajouterBtn.setOnAction(e -> afficherFormulaireAjoutMachine());

    planPane.getChildren().add(ajouterBtn);

    root.setCenter(planPane);
}
    private void afficherFicheMachine(Machine m){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la machine");
        alert.setHeaderText("Machine "+m.getRefmachine());
        alert.setContentText(
                "Type: "+m.getType()+"\n"
                +"Description: "+m.getDmachine()+"\n"
                +"Référence: "+m.getRefEquipement()+"\n"
                +"Coût: "+m.getC()+"\n"
                +"Temps de préparation: "+m.getT()+"\n"
                +"Etat: "+m.getEtat());
        alert.showAndWait();
    }
    private void afficherFormulaireAjoutMachine() {
    VBox box = new VBox(10);
    TextField idField = new TextField();
    idField.setPromptText("Identifiant");

    TextField refField = new TextField();
    refField.setPromptText("Référence");

    TextField typeField = new TextField();
    typeField.setPromptText("Type");

    TextField descField = new TextField();
    descField.setPromptText("Description");

    TextField abscField = new TextField();
    abscField.setPromptText("Abscisse (x)");

    TextField ordField = new TextField();
    ordField.setPromptText("Ordonnée (y)");

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
            float absc = Float.parseFloat(abscField.getText());
            float ord = Float.parseFloat(ordField.getText());
            float cout = Float.parseFloat(coutField.getText());
            float temps = Float.parseFloat(tempsField.getText());
            Machine.ETAT etat = etatBox.getValue();

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
                Machine m = new Machine(atelier.getEquipements().size() + 1, id, desc, type, absc, ord, cout, temps, etat);
                m.setRefEquipement(ref);
                atelier.getEquipements().add(m);
                erreurLabel.setText("Machine ajoutée !");
                afficherPlanAtelier(); // Pour rafraîchir la vue
            }

        } catch (Exception ex) {
            erreurLabel.setText("Erreur : Données invalides.");
        }
    });

    box.getChildren().addAll(
            new Label("Ajouter une machine :"),
            idField, refField, typeField, descField,
            abscField, ordField, coutField, tempsField,
            etatBox, ajouterBtn, erreurLabel
    );
    root.setCenter(box);
}

}
    
