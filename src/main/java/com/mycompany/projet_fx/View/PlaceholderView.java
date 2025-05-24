package com.mycompany.projet_fx.View;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class PlaceholderView {
    public static VBox getPlaceholder(String texte) {
        VBox box = new VBox(15);
        box.setStyle("-fx-alignment: center; -fx-padding: 60;");
        box.getChildren().add(new Label(texte));
        return box;
    }
}
