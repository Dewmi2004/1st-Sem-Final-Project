package lk.ijse.aquariumfinal.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lk.ijse.aquariumfinal.model.ChemicalModel;

import java.sql.SQLException;

public class ChemicalDetailPageController {

    @FXML
    private ComboBox<String> cmbchemicalId;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private TextField txtchemicalQty;

    public void loadChemicalIds() {
        try {
            ObservableList<String> chemicalIds = (ObservableList<String>) ChemicalModel.getAllChemicalIDS();
            if (chemicalIds != null && !chemicalIds.isEmpty()) {
                cmbchemicalId.setItems(chemicalIds);
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

}
