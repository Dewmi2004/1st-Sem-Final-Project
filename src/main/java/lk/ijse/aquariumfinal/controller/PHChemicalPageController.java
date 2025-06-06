package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.aquariumfinal.dto.PHChemicalDTO;
import lk.ijse.aquariumfinal.dto.tm.PHChemicalTM;
import lk.ijse.aquariumfinal.model.PHChemicalModel;
import lk.ijse.aquariumfinal.model.TankModel;
import lk.ijse.aquariumfinal.model.ChemicalModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class PHChemicalPageController {

    public TableView<PHChemicalTM> tblPHChemical;
    public TableColumn<?, ?> clmChemicalId;
    public TableColumn<?, ?> clmTankId;
    public TableColumn<?, ?> clmPhLevel;
    public TableColumn<?, ?> clmDate;
    public TableColumn<?, ?> clmTime;

    public ComboBox<String> cmbChemicalId;
    public ComboBox<String> cmbTankId;
    public ComboBox<String> cmbPhLevel;
    public DatePicker datePickerdate;
    public TextField txtTime;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public Button btnGReport;

    private final PHChemicalModel phModel = new PHChemicalModel();

    public TableColumn<?,?> clmPHLevel;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        loadComboData();
        loadTable();

    }

    private void setCellValueFactory() {
        clmTankId.setCellValueFactory(new PropertyValueFactory<>("tankId"));
        clmChemicalId.setCellValueFactory(new PropertyValueFactory<>("chemicalId"));
        clmPHLevel.setCellValueFactory(new PropertyValueFactory<>("phLevel"));
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmTime.setCellValueFactory(new PropertyValueFactory<>("time"));
    }

    private void loadComboData() throws SQLException, ClassNotFoundException {
            ObservableList<String> tankIds = TankModel.getTankId();
            cmbTankId.setItems(tankIds);
            cmbChemicalId.setItems(ChemicalModel.getChemicalId());
            cmbPhLevel.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
        }


    private void loadTable() throws SQLException, ClassNotFoundException {
        ArrayList<PHChemicalDTO> list = phModel.getAllPHChemical();
        ObservableList<PHChemicalTM> obList = FXCollections.observableArrayList();
        for (PHChemicalDTO dto : list) {
            obList.add(new PHChemicalTM(
                    dto.getTankId(),
                    dto.getChemicalId(),
                    dto.getPhLevel(),
                    dto.getDate(),
                    dto.getTime()
            ));
        }
        tblPHChemical.setItems(obList);
    }

    public void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (cmbTankId.getValue() == null || cmbChemicalId.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select valid Chemical ID and Tank ID").show();
            return;
        }

        PHChemicalDTO dto = new PHChemicalDTO(
                cmbTankId.getValue(),
                cmbChemicalId.getValue(),
                cmbPhLevel.getValue(),
                datePickerdate.getValue().toString(),
                txtTime.getText()
        );

        boolean isSaved = phModel.savePHChemical(dto);
        if (isSaved) {
            loadTable();
            resetForm();
            new Alert(Alert.AlertType.INFORMATION, "Saved!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save").show();
        }
    }


    public void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        PHChemicalDTO dto = new PHChemicalDTO(
                cmbTankId.getValue(),
                cmbChemicalId.getValue(),
                cmbPhLevel.getValue(),
                datePickerdate.getValue().toString(),
                txtTime.getText()
        );

        boolean isUpdated = phModel.updatePHChemical(dto);
        if (isUpdated) {
            loadTable();
            resetForm();
            new Alert(Alert.AlertType.INFORMATION, "Updated!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String date = String.valueOf(datePickerdate.getValue());
        Optional<ButtonType> confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO).showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.YES) {
            boolean isDeleted = phModel.deletePHChemical(date);
            if (isDeleted) {
                loadTable();
                resetForm();
                new Alert(Alert.AlertType.INFORMATION, "Deleted!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete").show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent event) {
        resetForm();
    }

    private void resetForm() {
        cmbChemicalId.getSelectionModel().clearSelection();
        cmbTankId.getSelectionModel().clearSelection();
        cmbPhLevel.getSelectionModel().clearSelection();
        datePickerdate.setValue(LocalDate.now());
        txtTime.clear();
    }

    public void btnGenerateROnAction(ActionEvent event) {

    }

    public void clickOnAction(MouseEvent event) {
        PHChemicalTM selected = tblPHChemical.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cmbTankId.setValue(selected.getTankId());
            cmbChemicalId.setValue(selected.getChemicalId());
            cmbPhLevel.setValue(selected.getPhLevel());
            datePickerdate.setValue(LocalDate.parse(selected.getDate()));
            txtTime.setText(selected.getTime());
        }
    }
}