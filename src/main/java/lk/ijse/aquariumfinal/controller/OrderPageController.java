package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lk.ijse.aquariumfinal.dto.CustomerDTO;
import lk.ijse.aquariumfinal.dto.OrderDTO;
import lk.ijse.aquariumfinal.model.CustomerModel;
import lk.ijse.aquariumfinal.model.OrderModel;

import java.sql.Date;
import java.sql.SQLException;

public class OrderPageController {
    public TextField txtCustomerPhone;
    public Label lblOrderrid;
    public Label lblPaymentId;
    public Button btnSearchCustomer;
    public Button btnPlaceOrder;

    private final OrderModel orderModel = new OrderModel();
    public DatePicker datePickerDate;
    public Label lblChange;
    public Button btnCheckBalance;
    public TextField txtPaidAmount;
    public Label lblTotalAmount;
    public ComboBox cmbMethod;
    public TableColumn colRemove;
    public TableColumn colTotal;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colItemId;
    public TableColumn colName;
    public TableView tblCart;
    public AnchorPane itemUiLoadPane;
    public Button btnAddToCart;
    public Button btnSearchItem;
    public ComboBox cmbItemId;
    public Label Customerdetails;
    public Label lblCustomerName;

    public void initialize() throws SQLException, ClassNotFoundException {
        setNextOrderId();
        setNextPaymentId();
        loadItemTypes();
    }

    private void loadItemTypes() {
        cmbItemId.setItems(FXCollections.observableArrayList("Plant Order", "Fish Order"));
        cmbMethod.setItems(FXCollections.observableArrayList("Card ","Cash "));
    }

    private void setNextOrderId() throws SQLException, ClassNotFoundException {
        lblOrderrid.setText(orderModel.generateNextOrderId());
    }

    private void setNextPaymentId() throws SQLException, ClassNotFoundException {
        lblPaymentId.setText(orderModel.generateNextPaymentId());
    }

    public void btnSearchCustomerOnAction(ActionEvent actionEvent) {
        String phone = txtCustomerPhone.getText();
        try {
            CustomerDTO customer = CustomerModel.searchCustomerByPhone(phone);
            if (customer != null) {
                Customerdetails.setText(customer.getId());
                lblCustomerName.setText(customer.getName());
                showAlert(Alert.AlertType.INFORMATION, "Customer Found");
            } else {
                showAlert(Alert.AlertType.WARNING, "Customer Not Found");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        if (datePickerDate.getValue() == null || cmbItemId.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Fill all required fields");
            return;
        }

        OrderDTO dto = new OrderDTO(
                lblOrderrid.getText(),
                lblPaymentId.getText(),
                Date.valueOf(datePickerDate.getValue()),
                Customerdetails.getText(),
                (String) cmbItemId.getValue()
        );

        try {
            boolean saved = orderModel.saveOrder(dto);
            if (saved) {
                showAlert(Alert.AlertType.INFORMATION, "Order Placed Successfully");
                clearFields();
                setNextOrderId();
                setNextPaymentId();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to place order");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

//    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
//        clearFields();
//        setNextOrderId();
//        setNextPaymentId();
//    }

    private void clearFields() {
        datePickerDate.setValue(null);
        cmbItemId.getSelectionModel().clearSelection();
        txtCustomerPhone.clear();
        Customerdetails.setText("Customer ID");
        lblCustomerName.setText("Customer Name");
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }

    public void btnSearchItemOnAction(ActionEvent actionEvent) {
    }

    public void btnAddtoCartOnAction(ActionEvent actionEvent) {
    }

    public void btnCheckBalanceOnAction(ActionEvent actionEvent) {
    }
}
