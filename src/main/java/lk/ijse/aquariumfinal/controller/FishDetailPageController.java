
package lk.ijse.aquariumfinal.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lk.ijse.aquariumfinal.model.FishModel;

import java.sql.SQLException;

public class FishDetailPageController {

    @FXML
    private ComboBox<String> cmbfishId;

    @FXML
    private TextField txtFishQty;

    @FXML
    private TextField txtUnitPrice;

    public void loadFishIds() {
        try {
            cmbfishId.setItems(FishModel.getAllFishIDS());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
