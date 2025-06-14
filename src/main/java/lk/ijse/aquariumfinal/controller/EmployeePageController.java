package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.aquariumfinal.dto.EmployeeDTO;
import lk.ijse.aquariumfinal.dto.tm.EmployeeTM;
import lk.ijse.aquariumfinal.model.EmployeeModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class  EmployeePageController {
private final EmployeeModel Emodel = new EmployeeModel();
    public DatePicker dpEmployee;
    public Label lblEmployeeid;
    public TableColumn<?,?> clmName;
    public TableColumn<?,?> clmEmployeeId;
    public TableColumn<?,?> clmAddress;
    public TableColumn<?,?> clmDob;
    public TableColumn<?,?> clmGender;
    public TableColumn<?,?> clmContact;
    public TableColumn<?,?> clmEmail;
    public Button btnSave;
    public Button btnReset;
    public Button btnDelete;
    public Button btnUpdate;
    public Button btnGReport;
    @FXML
    private TableView<EmployeeTM> tblEmployee;

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
    private boolean isValidInput() {
        String nameRegex = "^[A-Za-z\\s]{3,}$";
        String addressRegex = "^[\\w\\s,.-]{5,}$";
        String genderRegex = "^(?i)(male|female|other)$";
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";
        String contactRegex = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";

        if (!txtName.getText().matches(nameRegex)) {
            showAlert("Invalid Name! Only letters and spaces, minimum 3 characters.");
            return false;
        }

        if (!txtAddress.getText().matches(addressRegex)) {
            showAlert("Invalid Address! Minimum 5 characters.");
            return false;
        }

        if (!txtGender.getText().matches(genderRegex)) {
            showAlert("Invalid Gender! Use Male, Female, or Other.");
            return false;
        }

        if (dpEmployee.getValue() == null) {
            showAlert("Please select a valid Date of Birth.");
            return false;
        }

        if (!txtEmail.getText().matches(emailRegex)) {
            showAlert("Invalid Email format!");
            return false;
        }

        if (!txtContact.getText().matches(contactRegex)) {
            showAlert("Invalid Contact Number! Should be 10 digits or with country code.");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message).show();
    }

    private void loadtable() throws SQLException, ClassNotFoundException {
        ArrayList<EmployeeDTO> employees = Emodel.getAllEmployee();
        ObservableList<EmployeeTM> obc = FXCollections.observableArrayList();
        for (EmployeeDTO employee : employees) {
            EmployeeTM ETM = new EmployeeTM(
                    employee.getId(),
                    employee.getName(),
                    employee.getAddress(),
                    employee.getGender(),
                    employee.getDob(),
                    employee.getEmail(),
                    employee.getContact()
            );
            obc.add(ETM);
        }
        tblEmployee.setItems(obc);
    }

    private void setNextId() throws SQLException, ClassNotFoundException {
        String nextiD = Emodel.getNextEmployee();
        lblEmployeeid.setText(nextiD);

    }

    private void setCellValueFactory() {
        clmEmployeeId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        clmGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        clmDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        clmEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        clmContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String id = lblEmployeeid.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a Employee to delete").show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Employee ?");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(no, yes);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yes) {
            try {
                boolean isDeleted = Emodel.deleteEmployee(id);

                if (isDeleted) {
                    loadtable();
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Employee Deleted").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Employee Not Deleted").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting Employee").show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING, " Employee is not deleted !").show();
        }
    }

        private void clearFields() throws SQLException, ClassNotFoundException {

            txtName.clear();
            txtAddress.clear();
            txtGender.clear();
            dpEmployee.getEditor().clear();
            txtEmail.clear();
            txtContact.clear();
            setNextId();
        }

  @FXML
   public void btnGenarateROnAction(ActionEvent event) {

    }

    @FXML
   public void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
            txtName.clear();
            txtAddress.clear();
            txtGender.clear();
            dpEmployee.getEditor().clear();
            txtEmail.clear();
            txtContact.clear();

            setNextId();

            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            tblEmployee.getSelectionModel().clearSelection();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!isValidInput()) return;
        String id = lblEmployeeid.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String gender = txtGender.getText();
        String Dob = String.valueOf(dpEmployee.getValue());
        String email = txtEmail.getText();
        String contact = txtContact.getText();

        EmployeeDTO empdto = new EmployeeDTO(
                id,name,address,gender,Dob,email,contact
        );
        boolean isSave = EmployeeModel.saveEmployee(empdto);

        if (isSave) {
            lk.ijse.aquariumfinal.util.EmailUtil.sendEmployeeWelcomeEmail(email,name );

            loadtable();
            setNextId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Employee Saved", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Employee Not Saved", ButtonType.OK).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!isValidInput()) return;

        String id = lblEmployeeid.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String gender = txtGender.getText();
        String Dob = String.valueOf(dpEmployee.getValue());
        String email = txtEmail.getText();
        String contact = txtContact.getText();

        EmployeeDTO empDto = new EmployeeDTO(
                id,name,address,gender,Dob,email,contact
        );
        Boolean isUpdate = EmployeeModel.updateEmployee(empDto);

        if (isUpdate) {
            loadtable();
            setNextId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Employee Updated", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Employee Not Updated", ButtonType.OK).show();
        }
    }

    public void clickOnAction(MouseEvent mouseEvent) {
        EmployeeTM selectedItem = tblEmployee.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            lblEmployeeid.setText(selectedItem.getId());
            txtName.setText(selectedItem.getName());
            txtAddress.setText(selectedItem.getAddress());
            txtGender.setText(selectedItem.getGender());
            dpEmployee.setValue(LocalDate.parse(selectedItem.getDob()));
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
