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

public class  EmployeePageController {
private final EmployeeModel Emodel = new EmployeeModel();
    public DatePicker dpEmployee;
    public Label lblEmployeeid;
    public TableColumn clmName;
    public TableColumn clmEmployeeId;
    public TableColumn clmAddress;
    public TableColumn clmDob;
    public TableColumn clmGender;
    public TableColumn clmContact;
    public TableColumn clmEmail;
    public Button btnSave;
    public Button btnReset;
    public Button btnDelete;
    public Button btnUpdate;
    public Button btnGenarateR;
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
        Boolean isDelete = Emodel.deleteEmployee(id);

        if (isDelete) {
            loadtable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Employee Deleted", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Employee Not Deleted", ButtonType.OK).show();
        }
    }

    @FXML
    void btnGenarateROnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
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
            loadtable();
            setNextId();
            new Alert(Alert.AlertType.INFORMATION, "Employee Saved", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Employee Not Saved", ButtonType.OK).show();
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
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
