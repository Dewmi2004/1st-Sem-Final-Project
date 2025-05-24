package lk.ijse.aquariumfinal.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

public class OrderPageController {
    public TextField txtPaymentId;
    public DatePicker DPDate;
    public TextField txtCustomerId;
    public TextField txtItem;
    public TableView tblOrder;
    public Button btnPlaceOrder;
    public TableColumn colRemove;
    public TableColumn colUnitPrice;
    public TableColumn colTotal;
    public TableColumn colQty;
    public TableColumn colDescription;
    public TableColumn colItemId;
    public TableView tblCart;
    public Button btnAddToCart;
    public ComboBox cmbPaymentId;
    public Label Itemdetails;
    public Button btnSearchItem;
    public ComboBox cmbItemId;
    public Label Customerdetails;
    public Button btnSearchCustomer;
    public ComboBox cmbCustomerId;
    public DatePicker datePickerDate;
    public Label lblOrderrid;

    public void btnGenarateROnAction(ActionEvent actionEvent) {
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
    }
}
