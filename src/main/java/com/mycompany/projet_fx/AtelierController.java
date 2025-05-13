package com.mycompany.projet_fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

public class AtelierController {

    @FXML
    private ListView<String> listViewClasses;

    @FXML
    private MenuItem menuItemOuvrir;

    @FXML
    public void initialize() {
        listViewClasses.getItems().addAll("Classe A", "Classe B", "Classe C");
    }

    @FXML
    public void handleOuvrirMenuItem(ActionEvent event) {
        String selectedItem = listViewClasses.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println("Vous avez sélectionné : " + selectedItem);
        } else {
            System.out.println("Aucune classe sélectionnée !");
        }
    }
}
