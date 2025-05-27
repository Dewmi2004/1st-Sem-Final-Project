package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.aquariumfinal.dto.InventoryDTO;
import lk.ijse.aquariumfinal.dto.tm.InventryTM;
import lk.ijse.aquariumfinal.model.InventoryModel;
import lk.ijse.aquariumfinal.model.SupplierModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class InventoryPageController {
    private final InventoryModel Imodel = new InventoryModel();
    public DatePicker DPDate;
    public TableView<InventryTM> tblInventry;
    public ComboBox CboxSupplier;
    public Label lblInventryid;
    public TableColumn<?,?> clmInventryId;
    public TableColumn<?,?> clmSupplierId;
    public TableColumn<?,?> clmDate;
    public Button btnSave1;
    public Button btnReset1;
    public Button btnDelete1;
    public Button btnUpdate1;
    public Button btnGenarateR1;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setNextId();
     CboxSupplier.setItems(SupplierModel.getAllSupplierId());
        loadtable();
    }

    private void loadtable() throws SQLException, ClassNotFoundException {
        ArrayList<InventoryDTO> inventrys = Imodel.getAllInventry();
        ObservableList<InventryTM> obc = FXCollections.observableArrayList();
        for (InventoryDTO Inventry : inventrys) {
            InventryTM ITM = new InventryTM(
                    Inventry.getInventoryId(),
                    Inventry.getSupId(),
                    Inventry.getDate()

            );
            obc.add(ITM);
        }
        tblInventry.setItems(obc);
    }

    private void setNextId() throws SQLException, ClassNotFoundException {
        String nextiD = Imodel.getNextInventry();
        lblInventryid.setText(nextiD);
    }
    private void setCellValueFactory() {
        clmInventryId.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        clmSupplierId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    public void btnGenarateROnAction(ActionEvent actionEvent) {
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblInventryid.getText();
        String supid =  String.valueOf(CboxSupplier.getValue());
        String date = String.valueOf(String.valueOf(DPDate.getValue()));


        InventoryDTO inventryDTO = new InventoryDTO(
                id,supid,date
        );
        Boolean isUpdate = Imodel.updateInventry(inventryDTO);

        if (isUpdate) {
            loadtable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Inventry Updated", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Inventry Not Updated", ButtonType.OK).show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblInventryid.getText();
        Boolean isDelete = Imodel.deleteInventry(id);

        if (isDelete) {
            loadtable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Inventry Deleted", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Inventry Not Deleted", ButtonType.OK).show();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblInventryid.getText();
        String suppid = String.valueOf(CboxSupplier.getValue());
        String date = String.valueOf(DPDate.getValue());


        InventoryDTO inventoryDTO = new InventoryDTO(
                id,suppid,date
        );
        boolean isSave = Imodel.saveInventry(inventoryDTO);

        if (isSave) {
            loadtable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Inventry Saved", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Inventry Not Saved", ButtonType.OK).show();
        }
    }

    public void clickOnAction(MouseEvent mouseEvent) {
        InventryTM selectedItem =  tblInventry.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblInventryid.setText(selectedItem.getInventoryId());
            CboxSupplier.setValue(selectedItem.getSupId());
            DPDate.setValue(LocalDate.parse(selectedItem.getDate()));



            // save button disable
            btnSave1.setDisable(true);

            // update, delete button enable
            btnUpdate1.setDisable(false);
            btnDelete1.setDisable(false);
        }
    }
}
