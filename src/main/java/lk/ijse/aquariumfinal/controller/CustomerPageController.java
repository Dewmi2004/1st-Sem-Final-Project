package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.aquariumfinal.dto.CustomerDTO;
import lk.ijse.aquariumfinal.dto.tm.CustomerTM;
import lk.ijse.aquariumfinal.model.CustomerModel;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;


public class CustomerPageController {
private final CustomerModel Cmodel = new CustomerModel();
    public Button btnGReport;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public Button btnSave;
    @FXML
    private TableColumn<?, ?> clmAddress;

    @FXML
    private TableColumn<?, ?> clmContact;

    @FXML
    private TableColumn<?, ?> clmCusId;

    @FXML
    private TableColumn<?, ?> clmDob;

    @FXML
    private TableColumn<?, ?> clmEmail;

    @FXML
    private TableColumn<?, ?> clmGender;

    @FXML
    private TableColumn<?, ?> clmName;

    @FXML
    private DatePicker dpCustomer;

    @FXML
    private Label lblCusId;

    @FXML
    private TableView<CustomerTM> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtGender;

    @FXML
    private TextField txtName;
public void initialize() throws SQLException, ClassNotFoundException {
    setCellValueFactory();
    setNextId();
loadtable();
}

    private void loadtable() throws SQLException, ClassNotFoundException {
    ArrayList<CustomerDTO> customers = Cmodel.getAllCustomer();
    ObservableList<CustomerTM> obc = FXCollections.observableArrayList();
    for (CustomerDTO customer : customers) {
        CustomerTM CTM = new CustomerTM(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getGender(),
                customer.getDob(),
                customer.getEmail(),
                customer.getContact()
        );
       obc.add(CTM);
    }
    tblCustomer.setItems(obc);
    }


    private void setNextId() throws SQLException, ClassNotFoundException {
    String nextiD = Cmodel.getNextCustomer();
    lblCusId.setText(nextiD);

    }

    private void setCellValueFactory() {
    clmCusId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        clmGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        clmDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        clmEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        clmContact.setCellValueFactory(new PropertyValueFactory<>("contact"));

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
           String id = lblCusId.getText();

            if (id.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select a Customer to delete").show();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Customer?");
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(no, yes);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == yes) {
                try {
                    boolean isDeleted = Cmodel.deleteCustomer(id);

                    if (isDeleted) {
                        loadtable();
                        clearFields();
                        new Alert(Alert.AlertType.INFORMATION, "Customer Deleted").show();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Customer Not Deleted").show();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error occurred while deleting Customer").show();
                }
            }else{
                new Alert(Alert.AlertType.WARNING, " Customer is not deleted !").show();
            }
        }

        private void clearFields() throws SQLException, ClassNotFoundException {

        txtName.clear();
        txtAddress.clear();
        txtGender.clear();
        dpCustomer.getEditor().clear();
        txtEmail.clear();
        txtContact.clear();
        setNextId();
    }

    @FXML
    void btnGenarateROnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        txtName.clear();
        txtAddress.clear();
        txtGender.clear();
        dpCustomer.getEditor().clear();
        txtEmail.clear();
        txtContact.clear();

        setNextId();

        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        tblCustomer.getSelectionModel().clearSelection();


    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
String id = lblCusId.getText();
String name = txtName.getText();
String address = txtAddress.getText();
String gender = txtGender.getText();
String Dob = String.valueOf(dpCustomer.getValue());
String email = txtEmail.getText();
String contact = txtContact.getText();

CustomerDTO cusDto = new CustomerDTO(
        id,name,address,gender,Dob,email,contact
);
boolean isSave = CustomerModel.saveCustomer(cusDto);

        if (isSave) {
            lk.ijse.aquariumfinal.util.EmailUtil.sendCustomerWelcomeEmail(email, name);
            loadtable();
            setNextId();
            clearFields();
           new Alert(Alert.AlertType.INFORMATION, "Customer Saved", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Customer Not Saved", ButtonType.OK).show();
        }


    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String id = lblCusId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String gender = txtGender.getText();
        String Dob = String.valueOf(dpCustomer.getValue());
        String email = txtEmail.getText();
        String contact = txtContact.getText();

        CustomerDTO cusDto = new CustomerDTO(
                id,name,address,gender,Dob,email,contact
        );
        Boolean isUpdate = CustomerModel.updateCustomer(cusDto);

        if (isUpdate) {
            loadtable();
            setNextId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Customer Updated", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Customer Not Updated", ButtonType.OK).show();
        }

    }

    public void clickOnAction(MouseEvent mouseEvent) {
        CustomerTM selectedItem = tblCustomer.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblCusId.setText(selectedItem.getId());
            txtName.setText(selectedItem.getName());
            txtAddress.setText(selectedItem.getAddress());
            txtGender.setText(selectedItem.getGender());
            dpCustomer.setValue(LocalDate.parse(selectedItem.getDob()));
            txtEmail.setText(selectedItem.getEmail());
            txtContact.setText(selectedItem.getContact());

            // save button disable
            btnSave.setDisable(true);

            // update, delete button enable
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
}
    }
}
