package com.mycompany.projet_fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("atelierlayout.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestion des ateliers");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
