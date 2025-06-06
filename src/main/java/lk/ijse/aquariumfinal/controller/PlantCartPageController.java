package lk.ijse.aquariumfinal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lk.ijse.aquariumfinal.dto.CartDTO;
import lk.ijse.aquariumfinal.dto.PlantDTO;
import lk.ijse.aquariumfinal.model.PlantModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PlantCartPageController implements Initializable {

    public ComboBox<String> cmbPlantId;
    public Label lblPlantName;
    public Label lblUnitplantPrice;
    public TextField txtplantQty;

    public void btnSearchplantOnAction(ActionEvent actionEvent) {
        String plantId = cmbPlantId.getValue();

        if (plantId == null || plantId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please select a Plant ID.");
            return;
        }

        try {
            PlantDTO plant = PlantModel.searchPlantByName(plantId);
            CartDTO cart = PlantModel.searchPlantUnitprice(plantId);

            OrderPageController.plantId = plantId;

            boolean foundSomething = false;

            if (plant != null) {
                lblPlantName.setText(plant.getName());
                foundSomething = true;
            } else {
                lblPlantName.setText("N/A");
            }


            if (cart != null) {
                lblUnitplantPrice.setText(cart.getUnitPrice().toString());
                foundSomething = true;
            } else {
                lblUnitplantPrice.setText("N/A");
            }

            if (foundSomething) {
                showAlert(Alert.AlertType.INFORMATION, "Plant details found.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Plant not found.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    public String getSelectedPlantId() {
        return cmbPlantId.getValue();
    }

    public String getPlantName() {
        return lblPlantName.getText();
    }

    public String getQuantity() {
        return txtplantQty.getText();
    }

    public String getUnitPrice() {
        return lblUnitplantPrice.getText();
    }

    private void showAlert(Alert.AlertType alertType, String plantFound) {
        new Alert(alertType, plantFound).show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPlantIds();

        txtplantQty.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            try {
                OrderPageController.plantQty = Integer.parseInt(txtplantQty.getText());
            } catch (Exception e) {
            }
        });
    }

    void loadPlantIds() {
        try {
            cmbPlantId.setItems(PlantModel.getAllPlantIDS());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
