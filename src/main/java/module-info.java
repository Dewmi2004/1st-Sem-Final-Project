module lk.ijse.aquariumfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires com.google.protobuf;
    requires mysql.connector.j;
    requires jakarta.mail;


    opens lk.ijse.aquariumfinal to javafx.fxml;
    exports lk.ijse.aquariumfinal;

    exports lk.ijse.aquariumfinal.controller;
    opens lk.ijse.aquariumfinal.controller to javafx.fxml;

    exports lk.ijse.aquariumfinal.dto;
    opens lk.ijse.aquariumfinal.dto to javafx.fxml;

//    exports lk.ijse.aquariumfinal.dto.tm;

    opens lk.ijse.aquariumfinal.dto.tm;



}