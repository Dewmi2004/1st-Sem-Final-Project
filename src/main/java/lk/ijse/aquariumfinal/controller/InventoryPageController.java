package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.util.Optional;

public class InventoryPageController {
    private final InventoryModel Imodel = new InventoryModel();
    public DatePicker DPDate;
    public TableView<InventryTM> tblInventry;
    public ComboBox<String> CboxSupplier;
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
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Inventory Updated", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Inventory Not Updated", ButtonType.OK).show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
            String id = lblInventryid.getText();

            if (id.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select a Inventory to delete").show();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Inventory ?");
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(no, yes);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == yes) {
                try {
                    boolean isDeleted = Imodel.deleteInventry(id);

                    if (isDeleted) {
                        loadtable();
                        clearFields();
                        new Alert(Alert.AlertType.INFORMATION, "Inventory Deleted").show();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Inventory Not Deleted").show();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error occurred while deleting Inventory").show();
                }
            }
        }

        private void clearFields() throws SQLException, ClassNotFoundException {

            CboxSupplier.getSelectionModel().clearSelection();
         DPDate.getEditor().clear();
            setNextId();
        }

        @FXML
        void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
            CboxSupplier.getSelectionModel().clearSelection();
            DPDate.getEditor().clear();

            setNextId();

            btnSave1.setDisable(false);
            btnUpdate1.setDisable(true);
            btnDelete1.setDisable(true);

            tblInventry.getSelectionModel().clearSelection();


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
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Inventory Saved", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Inventory Not Saved", ButtonType.OK).show();
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
