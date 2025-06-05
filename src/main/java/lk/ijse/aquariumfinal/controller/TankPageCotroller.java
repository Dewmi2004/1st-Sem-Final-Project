package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.aquariumfinal.dto.TankDTO;
import lk.ijse.aquariumfinal.dto.tm.TankTM;
import lk.ijse.aquariumfinal.model.TankModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class TankPageCotroller {
    public ComboBox<String> CBoxGlass;
    public ComboBox<String> CBoxTank;
    public ComboBox<String> CBoxWater;
    public TableView<TankTM> tblTank;
    public Button btnSave1;
    public Button btnReset1;
    public Button btnDelete1;
    public Button btnUpdate1;
    public Button btnGenarateR1;
    public TableColumn<?,?> colWaterType;
    public TableColumn<?,?> colTankType;
    public TableColumn<?,?> colGlassType;
    public TableColumn<?,?> colTankId;
    public Label lblTankId;
    private final TankModel Tmodel = new TankModel();

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setNextId();
        loadComboData();
        loadTable();
    }
    private void loadComboData() {
        CBoxGlass.setItems(Tmodel.getGlassTypes());
        CBoxTank.setItems(Tmodel.getTankTypes());
        CBoxWater.setItems(Tmodel.getWaterTypes());
    }

    private void setCellValueFactory() {
        colTankId.setCellValueFactory(new PropertyValueFactory<>("tankId"));
        colGlassType.setCellValueFactory(new PropertyValueFactory<>("glassType"));
        colTankType.setCellValueFactory(new PropertyValueFactory<>("fishOrPlant"));
        colWaterType.setCellValueFactory(new PropertyValueFactory<>("waterType"));
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        ArrayList<TankDTO> list = Tmodel.getAllTanks();
        ObservableList<TankTM> obList = FXCollections.observableArrayList();

        for (TankDTO dto : list) {
            obList.add(new TankTM(dto.getTankId(), dto.getGlassType(), dto.getFishOrPlant(), dto.getWaterType()));
        }
        tblTank.setItems(obList);
    }

    private void setNextId() throws SQLException, ClassNotFoundException {
        String nextId = Tmodel.getNextTankId();
        lblTankId.setText(nextId);
    }
    public void btnGenarateROnAction(ActionEvent actionEvent) {
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        TankDTO dto = new TankDTO(
                lblTankId.getText(),
                CBoxGlass.getValue(),
                CBoxTank.getValue(),
                CBoxWater.getValue()
        );
        if (Tmodel.updateTank(dto)) {
            loadTable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Tank Updated").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to Update Tank").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblTankId.getText();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure You Want To Delete Tank ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (Tmodel.deleteTank(id)) {
                loadTable();
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Tank Deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Tank Not Deleted").show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING, " Tank is not deleted !").show();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFields();
        btnSave1.setDisable(false);
        btnUpdate1.setDisable(true);
        btnDelete1.setDisable(true);
        tblTank.getSelectionModel().clearSelection();
    }
    private void clearFields() throws SQLException, ClassNotFoundException {
        CBoxGlass.getSelectionModel().clearSelection();
        CBoxTank.getSelectionModel().clearSelection();
        CBoxWater.getSelectionModel().clearSelection();
        setNextId();
    }
    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        TankDTO dto = new TankDTO(
                lblTankId.getText(),
                CBoxGlass.getValue(),
                CBoxTank.getValue(),
                CBoxWater.getValue()
        );
        if (Tmodel.saveTank(dto)) {
            loadTable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Tank Saved").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to Save Tank").show();
        }
    }

    public void btnclickOnAction(javafx.scene.input.MouseEvent mouseEvent) {
        TankTM selected =  tblTank.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblTankId.setText(selected.getTankId());
            CBoxGlass.setValue(selected.getGlassType());
            CBoxTank.setValue(selected.getFishOrPlant());
            CBoxWater.setValue(selected.getWaterType());

            btnSave1.setDisable(true);
            btnUpdate1.setDisable(false);
            btnDelete1.setDisable(false);
        }
    }
}
