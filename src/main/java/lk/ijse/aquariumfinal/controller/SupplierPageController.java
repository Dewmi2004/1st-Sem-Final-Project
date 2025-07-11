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
import java.util.Optional;

public class SupplierPageController {
    private final SupplierModel Smodel = new SupplierModel();
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtEmail;
    public TableView<SupplierTM> tblSupplier;
    public TextField txtContact;
    public Button btnSave1;
    public Button btnReset1;
    public Button btnDelete1;
    public Button btnUpdate1;
    public TableColumn<?,?> colSupplierId;
    public TableColumn<?,?> colName;
    public TableColumn<?,?> colEmail;
    public TableColumn<?,?> colContact;
    public TableColumn<?,?> colCompanyAddress;
    public TableColumn<?,?> colSupplyType;
    public Label lblSupplierId;
    public ComboBox<String> CBoxSupplyType;
    public Button btnGenerateR1;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setNextId();
        CBoxSupplyType.setItems(Smodel.getSupplyTypes());
        loadtable();
    }
    private boolean validateInputs() {
        String nameRegex = "^[A-Za-z ]{2,30}$";
        String contactRegex = "^0[0-9]{9}$";
        String addressRegex = "^[\\w\\s,.-]{3,50}$";
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$";

        if (!txtName.getText().matches(nameRegex)) {
            showAlert("Invalid Name. Use 2–30 letters only.");
            return false;
        }
        if (!txtContact.getText().matches(contactRegex)) {
            showAlert("Invalid Contact. Use a 10-digit number starting with 0.");
            return false;
        }
        if (!txtAddress.getText().matches(addressRegex)) {
            showAlert("Invalid Address. Must be 3–50 characters.");
            return false;
        }
        if (CBoxSupplyType.getValue() == null) {
            showAlert("Please select a Supply Type.");
            return false;
        }
        if (!txtEmail.getText().matches(emailRegex)) {
            showAlert("Invalid Email format.");
            return false;
        }

        return true;
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
   public void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
            String id = lblSupplierId.getText();

            if (id.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select a supplier to delete").show();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this supplier?");
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.LEFT);
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.RIGHT);
            alert.getButtonTypes().setAll(no, yes);

            Optional<ButtonType> result = alert.showAndWait();

            result.ifPresent(buttonType -> {
                if (buttonType == yes) {
                    try {
                        if (Smodel.hasInventoryReferences(id)) {
                            new Alert(Alert.AlertType.WARNING, "Cannot delete supplier: It is referenced in Inventory").show();
                            return;
                        }

                        boolean isDeleted = Smodel.deleteSupplier(id);
                        if (isDeleted) {
                            loadtable();
                            setNextId();
                            clearFields();
                            new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted").show();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Supplier Not Deleted").show();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Error occurred while deleting supplier").show();
                    }
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier not deleted").show();
                }
            });
        }

    private void clearFields() throws SQLException, ClassNotFoundException {

            txtName.clear();
            txtContact.clear();
            txtAddress.clear();
            txtEmail.clear();
            CBoxSupplyType.getSelectionModel().clearSelection();
            setNextId();
        }

    @FXML
   public void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(!validateInputs()) return;
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
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Supplier Saved", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Supplier Not Saved", ButtonType.OK).show();
        }


    }

    @FXML
   public void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(!validateInputs()) return;

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
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Supplier Updated", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Supplier Not Updated", ButtonType.OK).show();
        }

    }

    public void clickOnAction(MouseEvent mouseEvent) {
        SupplierTM selectedItem = tblSupplier.getSelectionModel().getSelectedItem();

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
    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
            txtName.clear();
            txtContact.clear();
            txtAddress.clear();
            txtEmail.clear();
            CBoxSupplyType.setValue(null);

            setNextId();

            btnSave1.setDisable(false);
            btnUpdate1.setDisable(true);
            btnDelete1.setDisable(true);

            tblSupplier.getSelectionModel().clearSelection();
        }


    public void btnGenerateROnAction(ActionEvent actionEvent) {
    }
}
