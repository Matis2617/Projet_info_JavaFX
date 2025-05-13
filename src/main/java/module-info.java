module com.mycompany.projet_fx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.projet_fx to javafx.fxml;
    exports com.mycompany.projet_fx;
}

