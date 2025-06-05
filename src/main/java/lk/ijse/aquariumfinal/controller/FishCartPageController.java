package lk.ijse.aquariumfinal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lk.ijse.aquariumfinal.dto.CartDTO;
import lk.ijse.aquariumfinal.dto.CustomerDTO;
import lk.ijse.aquariumfinal.dto.FishDTO;
import lk.ijse.aquariumfinal.model.CustomerModel;
import lk.ijse.aquariumfinal.model.FishModel;
import lk.ijse.aquariumfinal.model.PlantModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FishCartPageController implements Initializable {

    public ComboBox<String> cmbFishId;
    public Label lblName;
    public Label lblUnitePrice;
    public TextField txtQty;

    public void btnSearchfishOnAction(ActionEvent actionEvent) {
        String fishId = cmbFishId.getValue();

        if (fishId == null || fishId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please select a Fish ID.");
            return;
        }

        try {
            FishDTO fish = FishModel.searchFishByName(fishId);
            CartDTO cart = FishModel.searchFishUnitPrice(fishId);

            boolean foundSomething = false;

            if (fish != null) {
                lblName.setText(fish.getName());
                foundSomething = true;
            } else {
                lblName.setText("N/A");
            }

            if (cart != null) {
                lblUnitePrice.setText(cart.getUnitPrice());
                foundSomething = true;
            } else {
                lblUnitePrice.setText("N/A");
            }

            if (foundSomething) {
                showAlert(Alert.AlertType.INFORMATION, "Fish details found.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Fish not found.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType alertType, String fishFound) {
        new Alert(alertType, fishFound).show();
    }
    public String getSelectedFishId() {
        return cmbFishId.getValue();
    }

    public String getFishName() {
        return lblName.getText();
    }

    public String getQuantity() {
        return txtQty.getText();
    }

    public String getUnitPrice() {
        return lblUnitePrice.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       loadFishIds();
    }

    public void loadFishIds() {
        try {
            cmbFishId.setItems(FishModel.getAllFishIDS());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
