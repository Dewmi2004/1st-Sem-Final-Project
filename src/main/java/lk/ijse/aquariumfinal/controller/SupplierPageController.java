package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.aquariumfinal.dto.SupplierDTO;
import lk.ijse.aquariumfinal.dto.tm.SupplierTM;
import lk.ijse.aquariumfinal.model.SupplierModel;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierPageController {
    private final SupplierModel Smodel = new SupplierModel();
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtEmail;
    public TableView tblSupplier;
    public TextField txtContact;
    public Button btnSave1;
    public Button btnReset1;
    public Button btnDelete1;
    public Button btnUpdate1;
    public Button btnGenarateR1;
    public TableColumn colSupplierId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colCompanyAddress;
    public TableColumn colSupplyType;
    public Label lblSupplierId;
    public ComboBox CBoxSupplyType;
    public Button btnGenerateR1;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setNextId();
        CBoxSupplyType.setItems(Smodel.getSupplyTypes());
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
        lblSupplierId.setText(nextiD);

    }

    private void setCellValueFactory() {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colCompanyAddress.setCellValueFactory(new PropertyValueFactory<>("companyAddress"));
        colSupplyType.setCellValueFactory(new PropertyValueFactory<>("supplyType"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String id = lblSupplierId.getText();
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
        String id = lblSupplierId.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String Address = txtAddress.getText();
        String SupplierType = String.valueOf(CBoxSupplyType.getValue());
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
        String id = lblSupplierId.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String supplyType = String.valueOf(CBoxSupplyType.getValue());
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
            lblSupplierId.setText(selectedItem.getSupId());
            txtName.setText(selectedItem.getName());
            txtContact.setText(selectedItem.getContact());
            txtAddress.setText(selectedItem.getCompanyAddress());
            CBoxSupplyType.setValue(selectedItem.getSupplyType());
            txtEmail.setText(selectedItem.getEmail());


            // save button disable
            btnSave1.setDisable(true);

            // update, delete button enable
            btnUpdate1.setDisable(false);
            btnDelete1.setDisable(false);
        }
    }
    public void btnGenarateROnAction(ActionEvent actionEvent) {
    }



    public void btnResetOnAction(ActionEvent actionEvent) {
    }


    public void btnGenerateROnAction(ActionEvent actionEvent) {
    }
}
