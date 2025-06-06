package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.aquariumfinal.dto.FoodDTO;
import lk.ijse.aquariumfinal.dto.tm.FoodTM;
import lk.ijse.aquariumfinal.model.FoodModel;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class FoodPageController {
    public TextField txtName;

    public TextField txtFishType;
    public DatePicker DPExDate;
    public TableView<FoodTM> tblFood;
    public Button btnGenarateR;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public Button btnSave;
    public Label lblFoodId;
    private final FoodModel model = new FoodModel();
    public TableColumn<?, ?> clmFoodId;
    public TableColumn<?, ?> clmName;
    public TableColumn<?, ?> clmFishType;
    public TableColumn<?, ?> dpExDate;
    public TextField txtQuantity;
    public TableColumn<?,?> clmQuantity;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setNextId();
        loadTable();
    }

    private void setCellValueFactory() {
        clmFoodId.setCellValueFactory(new PropertyValueFactory<>("foodId"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmFishType.setCellValueFactory(new PropertyValueFactory<>("fishType"));
        dpExDate.setCellValueFactory(new PropertyValueFactory<>("exDate"));
        clmQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

    }


    private void loadTable() throws SQLException, ClassNotFoundException {
        ArrayList<FoodDTO> list = model.getAllFoods();
        ObservableList<FoodTM> obList = FXCollections.observableArrayList();

        for (FoodDTO dto : list) {
            obList.add(new FoodTM(dto.getFoodId(), dto.getName(), dto.getFishType(), dto.getExDate(),dto.getQuantity()));
        }
        tblFood.setItems(obList);
    }

    private void setNextId() throws SQLException, ClassNotFoundException {
        lblFoodId.setText(model.getNextFoodId());
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        FoodDTO dto = new FoodDTO(
                lblFoodId.getText(),
                txtName.getText(),
                txtFishType.getText(),
                Date.valueOf(DPExDate.getValue()),
                txtQuantity.getText()
        );

        if (FoodModel.saveFood(dto)) {
            loadTable();
            setNextId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Food Saved").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to Save").show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        FoodDTO dto = new FoodDTO(
                lblFoodId.getText(),
                txtName.getText(),
                txtFishType.getText(),
                Date.valueOf(DPExDate.getValue()),
                txtQuantity.getText()
        );

        if (FoodModel.updateFood(dto)) {
            loadTable();
            setNextId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Food Updated").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to Update").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblFoodId.getText();
        if (id.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a Food to delete").show();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure You Want To Delete Food?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = model.deleteFood(id);

                if (isDeleted) {
                    loadTable();
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Food Deleted").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Food Not Deleted").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting Food").show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING, " Food is not deleted !").show();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFields();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        tblFood.getSelectionModel().clearSelection();
    }

    private void clearFields() throws SQLException, ClassNotFoundException {
        txtName.clear();
        txtFishType.clear();
        DPExDate.setValue(null);
        txtQuantity.clear();
        setNextId();
    }

    public void btnClickOnACtion(MouseEvent mouseEvent) {
        FoodTM selected = tblFood.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblFoodId.setText(selected.getFoodId());
            txtName.setText(selected.getName());
            txtFishType.setText(selected.getFishType());
            DPExDate.setValue(selected.getExDate().toLocalDate());
            txtQuantity.setText(String.valueOf(selected.getQuantity()));

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }

    }

    public void btnGenarateROnAction(ActionEvent actionEvent) {
    }
}


