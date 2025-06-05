package lk.ijse.aquariumfinal.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lk.ijse.aquariumfinal.model.PlantModel;

import java.sql.SQLException;

public class PlantDetailPageController {

    @FXML
    private ComboBox<String> cmbPlantId;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private TextField txtplantQty;

    public void loadPlantIds() {
        try {
            cmbPlantId.setItems(PlantModel.getAllPlantIDS());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
