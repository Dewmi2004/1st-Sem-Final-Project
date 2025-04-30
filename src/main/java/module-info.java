module lk.ijse.aquariumfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires static lombok;


    opens lk.ijse.aquariumfinal to javafx.fxml;
    exports lk.ijse.aquariumfinal;

    exports lk.ijse.aquariumfinal.controller;
    opens lk.ijse.aquariumfinal.controller to javafx.fxml;
}