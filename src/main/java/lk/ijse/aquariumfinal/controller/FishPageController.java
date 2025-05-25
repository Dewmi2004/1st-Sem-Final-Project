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

import java.sql.SQLException;
import java.util.ArrayList;

public class FishPageController {
    private final FishModel Fmodel = new FishModel();

    public TableView<FishTM> tblFish;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public Button btnSave;
    public Button btnGReport;
    public Label lblFishId;
    public ComboBox cmbSize;
    public ComboBox cmbTankId;
    public ComboBox cmbGender;
    public ComboBox cmbWaterType;
    public ComboBox cmbCountry;
    public ComboBox cmbInventoryId;
    public TextField txtColor;
    public TableColumn<?,?> clmSize;
    public TableColumn<?,?>  clmTankId;
    public TableColumn<?,?>  clmGender;
    public TableColumn<?,?>  clmWaterType;
    public TableColumn<?,?>  clmCountry;
    public TableColumn<?,?>  clmInventoryId;
    public TableColumn<?,?>  clmColor;
    public TextField txtName;
    public TableColumn<?,?>  clmName;
    public TableColumn<?,?>  clmFishId;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setNextId();
        cmbSize.setItems(Fmodel.getFishSize());
        loadtable();
    }

    private void setCellValueFactory() {
        clmFishId.setCellValueFactory(new PropertyValueFactory<>("fishId"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        clmTankId.setCellValueFactory(new PropertyValueFactory<>("tankId"));
        clmGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        clmWaterType.setCellValueFactory(new PropertyValueFactory<>("waterType"));
        clmCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        clmInventoryId.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        clmColor.setCellValueFactory(new PropertyValueFactory<>("colour"));
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
                    fish.getInventoryId(),
                    fish.getColour()
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
        String id = lblFishId.getText();
        String name = txtName.getText();
        String size = (String) cmbSize.getValue();
        String tankid = (String) cmbTankId.getValue();
        String gender = (String) cmbGender.getValue();
        String watertype = (String) cmbWaterType.getValue();
        String country = (String) cmbCountry.getValue();
        String inventryid = (String) cmbInventoryId.getValue();
        String colour = txtColor.getText();

        FishDTO fishDto = new FishDTO(
                id,name,size,tankid,gender,watertype,country,inventryid,colour
        );
        boolean isUpdate = FishModel.UpdateFish(fishDto);

        if (isUpdate) {
            loadtable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, " Fish Updated", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, " Fish Not Updated", ButtonType.OK).show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblFishId.getText();
        Boolean isDelete = Fmodel.deleteFish(id);

        if (isDelete) {
            loadtable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Fish Deleted", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Fish Not Deleted", ButtonType.OK).show();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblFishId.getText();
        String name = txtName.getText();
        String size = (String) cmbSize.getValue();
        String tankid = (String) cmbTankId.getValue();
        String gender = (String) cmbGender.getValue();
        String watertype = (String) cmbWaterType.getValue();
        String country = (String) cmbCountry.getValue();
        String inventryid = (String) cmbInventoryId.getValue();
        String colour = txtColor.getText();

        FishDTO fishDto = new FishDTO(
                id,name,size,tankid,gender,watertype,country,inventryid,colour
        );
        boolean isSave = FishModel.saveFish(fishDto);

        if (isSave) {
            loadtable();
            setNextId();
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
            cmbInventoryId.setValue(selectedItem.getInventoryId());
            txtColor.setText(selectedItem.getColour());


            // save button disable
            btnSave.setDisable(true);

            // update, delete button enable
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void btnGenerateROnAction(ActionEvent actionEvent) {
    }
}
