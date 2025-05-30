package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.aquariumfinal.dto.ChemicalDTO;
import lk.ijse.aquariumfinal.dto.tm.ChemicalTM;
import lk.ijse.aquariumfinal.model.ChemicalModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ChemicalPageController {
    public TextField txtConcentration;
    public TextField txtStoreType;
    public TextField txtName;
    public TableView<ChemicalTM> tblChemical;
    public Label lblChemicalId;
    public ComboBox<String> cBoxType;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public Button btnGReport;
    public TableColumn<?, ?> clmid;
    public TableColumn<?, ?> clmtype;
    public TableColumn<?, ?> clmconcentration;
    public TableColumn<?, ?> clmstoretype;
    public TableColumn<?, ?> clmname;

    private final ChemicalModel model = new ChemicalModel();

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setNextId();
        loadComboData();
        loadTable();
    }

    private void setCellValueFactory() {
        clmid.setCellValueFactory(new PropertyValueFactory<>("chemicalId"));
        clmtype.setCellValueFactory(new PropertyValueFactory<>("acidOrBase"));
        clmconcentration.setCellValueFactory(new PropertyValueFactory<>("concentration"));
        clmstoretype.setCellValueFactory(new PropertyValueFactory<>("storeType"));
        clmname.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void loadComboData() {
        cBoxType.setItems(model.getChemicalTypes());
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        ArrayList<ChemicalDTO> list = model.getAllChemicals();
        ObservableList<ChemicalTM> obList = FXCollections.observableArrayList();

        for (ChemicalDTO dto : list) {
            obList.add(new ChemicalTM(dto.getChemicalId(), dto.getAcidOrBase(), dto.getConcentration(), dto.getStoreType(), dto.getName()));
        }
        tblChemical.setItems(obList);
    }

    private void setNextId() throws SQLException, ClassNotFoundException {
        lblChemicalId.setText(model.getNextChemicalId());
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ChemicalDTO dto = new ChemicalDTO(
                lblChemicalId.getText(),
                cBoxType.getValue(),
                txtConcentration.getText(),
                txtStoreType.getText(),
                txtName.getText()
        );
        if (ChemicalModel.saveChemical(dto)) {
            loadTable();
            clearFields();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Chemical Saved").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to Save Chemical").show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ChemicalDTO dto = new ChemicalDTO(
                lblChemicalId.getText(),
                cBoxType.getValue(),
                txtConcentration.getText(),
                txtStoreType.getText(),
                txtName.getText()
        );
        if (ChemicalModel.updateChemical(dto)) {
            loadTable();
            clearFields();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Chemical Updated").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to Update Chemical").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblChemicalId.getText();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this chemical?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (model.deleteChemical(id)) {
                loadTable();
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Chemical Deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to Delete Chemical").show();
            }
        }else{
          new  Alert (Alert.AlertType.ERROR, "Failed to Delete Chemical").show();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFields();
        tblChemical.getSelectionModel().clearSelection();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    private void clearFields() throws SQLException, ClassNotFoundException {
        cBoxType.getSelectionModel().clearSelection();
        txtConcentration.clear();
        txtStoreType.clear();
        txtName.clear();
        setNextId();
    }

    public void btnGenerateROnAction(ActionEvent actionEvent) {
    }

    public void ChemicalOnMouseClick(MouseEvent mouseEvent) {
        ChemicalTM selected = tblChemical.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblChemicalId.setText(selected.getChemicalId());
            cBoxType.setValue(selected.getAcidOrBase());
            txtConcentration.setText(selected.getConcentration());
            txtStoreType.setText(selected.getStoreType());
            txtName.setText(selected.getName());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }
}
