/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
/**
 *
 * @author Matis
 */
public class AtelierController {
    @FXML
    private ListView<String> listViewClasses;
    
    @FXML
    private MenuItem menuItemOuvrir;
    
    @FXML
    public void initialize(){
        listViewClasses.getItems().add("Classes A");
        listViewClasses.getItems().add("Classes B");
        listViewClasses.getItems().add("Classes C");
    }
    
    @FXML
    public void handleOuvrirMenuItem(MouseEvent event) {
        String selectedItem = listViewClasses.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println("Vous avez sélectionné : " + selectedItem);
        } else {
            System.out.println("Aucune classe sélectionnée !");
        }
}
