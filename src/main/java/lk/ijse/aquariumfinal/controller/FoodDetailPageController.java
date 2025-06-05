package lk.ijse.aquariumfinal.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lk.ijse.aquariumfinal.model.ChemicalModel;
import lk.ijse.aquariumfinal.model.FoodModel;

import java.sql.SQLException;

public class FoodDetailPageController {

    @FXML
    private ComboBox<String> cmbfoodId;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private TextField txtfoodQty;

    public void loadFoodIds() {
        try {
            ObservableList<String> foodIds = (ObservableList<String>) FoodModel.getAllFoodIDS();
            if (foodIds != null && !foodIds.isEmpty()) {
                cmbfoodId.setItems(foodIds);
            } else {
                showAlert(Alert.AlertType.WARNING, "No chemical IDs found.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load chemical IDs: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String s) {
        new Alert(alertType, s).show();
    }
    public String getSelectedFoodId() {
        return cmbfoodId.getValue();
    }

    public String getQuantity() {
        return txtUnitPrice.getText();
    }

    public String getUnitPrice() {
        return txtUnitPrice.getText();
    }

}
