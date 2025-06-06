package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.aquariumfinal.dto.FishDTO;
import lk.ijse.aquariumfinal.dto.tm.FishTM;
import lk.ijse.aquariumfinal.model.FishModel;
import lk.ijse.aquariumfinal.model.TankModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class FishPageController {
    private final FishModel Fmodel = new FishModel();

    public TableView<FishTM> tblFish;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public Button btnSave;
    public Button btnGReport;
    public Label lblFishId;
    public ComboBox<String> cmbSize;
    public ComboBox<String> cmbTankId;
    public ComboBox<String> cmbGender;
    public ComboBox<String> cmbWaterType;
    public ComboBox <String> cmbCountry;
    public ComboBox <String> cmbInventoryId;
    public TextField txtColor;
    public TableColumn<?,?> clmSize;
    public TableColumn<?,?>  clmTankId;
    public TableColumn<?,?>  clmGender;
    public TableColumn<?,?>  clmWaterType;
    public TableColumn<?,?>  clmCountry;
    public TableColumn<?,?>  clmColor;
    public TextField txtName;
    public TableColumn<?,?>  clmName;
    public TableColumn<?,?>  clmFishId;
    public TextField txtQuantity;
    public TableColumn<?,?> clmQuantity;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setNextId();
        ComboDataSet();
        loadtable();
    }
    private boolean isValidInput() {
        String nameRegex = "^[A-Za-z\\s]{3,}$";
        String colorRegex = "^[A-Za-z\\s]{3,}$";
        String quantityRegex = "^\\d+$";
        String genderRegex = "^(?i)(male|female|other)$";

        if (!txtName.getText().matches(nameRegex)) {
            showAlert("Invalid Name! Only letters and spaces, minimum 3 characters.");
            return false;
        }

        if (cmbSize.getValue() == null) {
            showAlert("Please select a valid Fish Size.");
            return false;
        }

        if (cmbTankId.getValue() == null) {
            showAlert("Please select a valid Tank ID.");
            return false;
        }

        if (cmbGender.getValue() == null || !cmbGender.getValue().matches(genderRegex)) {
            showAlert("Invalid Gender! Use Male, Female, or Other.");
            return false;
        }

        if (cmbWaterType.getValue() == null) {
            showAlert("Please select a valid Water Type.");
            return false;
        }

        if (cmbCountry.getValue() == null) {
            showAlert("Please select a valid Country.");
            return false;
        }

        if (!txtColor.getText().matches(colorRegex)) {
            showAlert("Invalid Color! Only letters and spaces, minimum 3 characters.");
            return false;
        }

        if (!txtQuantity.getText().matches(quantityRegex)) {
            showAlert("Invalid Quantity! Must be a number.");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message).show();
    }

    private void ComboDataSet() throws SQLException, ClassNotFoundException {
        cmbSize.setItems(Fmodel.getFishSize());
        cmbTankId.setItems(TankModel.getTankId());
        cmbGender.setItems(Fmodel.getFishGender());
        cmbWaterType.setItems(Fmodel.getFishWatertype());
        cmbCountry.setItems(Fmodel.getFishCountry());

    }

    private void setCellValueFactory() {
        clmFishId.setCellValueFactory(new PropertyValueFactory<>("fishId"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        clmTankId.setCellValueFactory(new PropertyValueFactory<>("tankId"));
        clmGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        clmWaterType.setCellValueFactory(new PropertyValueFactory<>("waterType"));
        clmCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        clmColor.setCellValueFactory(new PropertyValueFactory<>("colour"));
        clmQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }


    private void loadtable() throws SQLException, ClassNotFoundException {
        ArrayList<FishDTO> fishs = Fmodel.getAllFish();
        ObservableList<FishTM> obc = FXCollections.observableArrayList();
        for (FishDTO fish : fishs) {
            FishTM FTM = new FishTM(
                    fish.getFishId(),
                    fish.getName(),
                    fish.getSize(),
                    fish.getTankId(),
                    fish.getGender(),
                    fish.getWaterType(),
                    fish.getCountry(),
                    fish.getColour(),
                    fish.getQuantity()
            );
            obc.add(FTM);
        }
        tblFish.setItems(obc);
    }

    private void setNextId() throws SQLException, ClassNotFoundException {
        String nextiD = Fmodel.getNextFish();
        lblFishId.setText(nextiD);

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
       if(!isValidInput()) return;
        String id = lblFishId.getText();
        String name = txtName.getText();
        String size = (String) cmbSize.getValue();
        String tankid = (String) cmbTankId.getValue();
        String gender = (String) cmbGender.getValue();
        String watertype = (String) cmbWaterType.getValue();
        String country = (String) cmbCountry.getValue();
        String colour = txtColor.getText();
        String quantity =txtQuantity.getText();

        FishDTO fishDto = new FishDTO(
                id,name,size,tankid,gender,watertype,country,colour,quantity
        );
        boolean isUpdate = FishModel.UpdateFish(fishDto);

        if (isUpdate) {
            loadtable();
            setNextId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, " Fish Updated", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, " Fish Not Updated", ButtonType.OK).show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblFishId.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a Fish to delete").show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Fish ?");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(no, yes);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yes) {
            try {
                boolean isDeleted = Fmodel.deleteFish(id);

                if (isDeleted) {
                    loadtable();
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Fish Deleted").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fish Not Deleted").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting Fish").show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING, " Fish is not deleted !").show();
        }
        }

        private void clearFields() throws SQLException, ClassNotFoundException {

            txtName.clear();
           cmbSize.getSelectionModel().clearSelection();
           cmbTankId.getSelectionModel().clearSelection();
           cmbGender.getSelectionModel().clearSelection();
           cmbWaterType.getSelectionModel().clearSelection();
           cmbCountry.getSelectionModel().clearSelection();
           txtColor.clear();
           txtQuantity.clear();
            setNextId();
        }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(!isValidInput()) return;

        String id = lblFishId.getText();
        String name = txtName.getText();
        String size = (String) cmbSize.getValue();
        String tankid = (String) cmbTankId.getValue();
        String gender = (String) cmbGender.getValue();
        String watertype = (String) cmbWaterType.getValue();
        String country = (String) cmbCountry.getValue();
        String colour = txtColor.getText();
        String quantity =txtQuantity.getText();

        FishDTO fishDto = new FishDTO(
                id,name,size,tankid,gender,watertype,country,colour,quantity
        );
        boolean isSave = FishModel.saveFish(fishDto);

        if (isSave) {
            loadtable();
            setNextId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, " Fish Saved", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, " Fish Not Saved", ButtonType.OK).show();
        }

    }

    public void clickOnAction(MouseEvent mouseEvent) {
        FishTM selectedItem =  tblFish.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblFishId.setText(selectedItem.getFishId());
            txtName.setText(selectedItem.getName());
            cmbSize.setValue(selectedItem.getSize());
            cmbTankId.setValue(selectedItem.getTankId());
            cmbGender.setValue(selectedItem.getGender());
            cmbWaterType.setValue(selectedItem.getWaterType());
            cmbCountry.setValue(selectedItem.getCountry());
            txtColor.setText(selectedItem.getColour());
            txtQuantity.setText(selectedItem.getQuantity());


            btnSave.setDisable(true);

            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void btnGenerateROnAction(ActionEvent actionEvent) {
    }

    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
      clearFields();

        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        tblFish.getSelectionModel().clearSelection();
    }
}
