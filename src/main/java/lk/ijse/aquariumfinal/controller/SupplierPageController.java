package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.aquariumfinal.DTO.CustomerDTO;
import lk.ijse.aquariumfinal.DTO.SupplierDTO;
import lk.ijse.aquariumfinal.DTO.tm.CustomerTM;
import lk.ijse.aquariumfinal.DTO.tm.SupplierTM;
import lk.ijse.aquariumfinal.Model.CustomerModel;
import lk.ijse.aquariumfinal.Model.SupplierModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class SupplierPageController {
    private final SupplierModel Smodel = new SupplierModel();
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtEmail;
    public TableView tblSupplier;
    public TextField txtContact;
    public ComboBox cbSupplyType;
    public Label lblSupId;
    public TableColumn clmEmail;
    public TableColumn clmSupplierType;
    public TableColumn clmCompanyAddress;
    public TableColumn clmContact;
    public TableColumn clmName;
    public TableColumn clmSupplierID;
    public Button btnGenarateOnAction;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public Button btnSave;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setNextId();
        loadtable();
    }

    private void loadtable() throws SQLException, ClassNotFoundException {
        ArrayList<SupplierDTO> suppliers = Smodel.getAllSupplier();
        ObservableList<SupplierTM> obc = FXCollections.observableArrayList();
        for (SupplierDTO supplier : suppliers) {
            SupplierTM STM = new SupplierTM(
                    supplier.getSupId(),
                    supplier.getName(),
                    supplier.getContact(),
                    supplier.getCompanyAddress(),
                    supplier.getSupplyType(),
                    supplier.getEmail()
            );
            obc.add(STM);
        }
        tblSupplier.setItems(obc);
    }


    private void setNextId() throws SQLException, ClassNotFoundException {
        String nextiD = Smodel.getNextSupplier();
        lblSupId.setText(nextiD);

    }

    private void setCellValueFactory() {
        clmSupplierID.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        clmCompanyAddress.setCellValueFactory(new PropertyValueFactory<>("company Address"));
        clmSupplierType.setCellValueFactory(new PropertyValueFactory<>("Supplier_Type"));
        clmEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String id = lblSupId.getText();
        Boolean isDelete = Smodel.deleteSupplier(id);

        if (isDelete) {
            loadtable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Supplier Not Deleted", ButtonType.OK).show();
        }
    }



    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String id = lblSupId.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String Address = txtAddress.getText();
        String SupplierType = String.valueOf(cbSupplyType.getValue());
        String email = txtEmail.getText();


        SupplierDTO supDTO = new SupplierDTO(
                id,name,contact,Address,SupplierType,email
        );
        boolean isSave = SupplierModel.saveSupplier(supDTO);

        if (isSave) {
            loadtable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Supplier Saved", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Supplier Not Saved", ButtonType.OK).show();
        }


    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String id = lblSupId.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String supplyType = String.valueOf(cbSupplyType.getValue());
        String email = txtEmail.getText();


        SupplierDTO supDTO = new SupplierDTO(
                id,name,contact,address,supplyType,email
        );
        Boolean isUpdate = SupplierModel.updateSupplier(supDTO);

        if (isUpdate) {
            loadtable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Supplier Updated", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Supplier Not Updated", ButtonType.OK).show();
        }

    }

    public void clickOnAction(MouseEvent mouseEvent) {
        SupplierTM selectedItem = (SupplierTM) tblSupplier.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblSupId.setText(selectedItem.getSupId());
            txtName.setText(selectedItem.getName());
            txtContact.setText(selectedItem.getContact());
            txtAddress.setText(selectedItem.getCompany_Address());
            cbSupplyType.setValue(LocalDate.parse(selectedItem.getSupply_Type()));
            txtEmail.setText(selectedItem.getEmail());


            // save button disable
            btnSave.setDisable(true);

            // update, delete button enable
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }
    public void btnGenarateROnAction(ActionEvent actionEvent) {
    }



    public void btnResetOnAction(ActionEvent actionEvent) {
    }


}
