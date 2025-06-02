package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.aquariumfinal.dto.CustomerDTO;
import lk.ijse.aquariumfinal.dto.tm.CartTM;
import lk.ijse.aquariumfinal.model.CustomerModel;
import lk.ijse.aquariumfinal.model.OrderModel;
import java.sql.SQLException;


public class OrderPageController {
    public TextField txtCustomerPhone;
    public Label lblOrderrid;
    public Label lblPaymentId;
    public Button btnSearchCustomer;
    public Button btnPlaceOrder;
    public DatePicker datePickerDate;
    public Label lblChange;
    public Button btnCheckBalance;
    public TextField txtPaidAmount;
    public Label lblTotalAmount;
    public ComboBox<String> cmbMethod;
    public TableColumn<CartTM, String> colItemId;
    public TableColumn<CartTM, String> colName;
    public TableColumn<CartTM, Integer> colQty;
    public TableColumn<CartTM, Double> colUnitPrice;
    public TableColumn<CartTM, Double> colTotal;
    public TableColumn<CartTM, Button> colRemove;
    public TableView<CartTM> tblCart;
    public AnchorPane itemUiLoadPane;
    public Button btnAddToCart;
    public Button btnSearchItem;
    public ComboBox<String> cmbItemId;
    public Label Customerdetails;
    public Label lblCustomerName;

    private final OrderModel orderModel = new OrderModel();
    private final ObservableList<CartTM> cartList = FXCollections.observableArrayList();


    public void initialize() throws SQLException, ClassNotFoundException {
        setNextOrderId();
        setNextPaymentId();
        loadItemTypes();
        setupTableColumns();
    }

    private void loadItemTypes() {
        cmbItemId.setItems(FXCollections.observableArrayList("Plant Order", "Fish Order"));
        cmbMethod.setItems(FXCollections.observableArrayList("Card", "Cash"));
    }
    private void nevigateTo(String s) {
        try {
            itemUiLoadPane.getChildren().clear();
            AnchorPane pane = FXMLLoader.load(getClass().getResource(s));

            pane.prefWidthProperty().bind(itemUiLoadPane.widthProperty());
            pane.prefHeightProperty().bind(itemUiLoadPane.heightProperty());

            itemUiLoadPane.getChildren().add(pane);
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR,"Page Not Found!").show();
            e.printStackTrace();

        }
    }
    private void setupTableColumns() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("btn"));
        tblCart.setItems(cartList);
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

//    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
//        if (datePickerDate.getValue() == null || cmbItemId.getValue() == null || cartList.isEmpty()) {
//            showAlert(Alert.AlertType.WARNING, "Please complete the form and add items to cart.");
//            return;
//        }
//
//        OrderDTO order = new OrderDTO(
//                lblOrderrid.getText(),
//                lblPaymentId.getText(),
//                Date.valueOf(datePickerDate.getValue()),
//                Customerdetails.getText(),
//                cmbItemId.getValue(),
//
//        );
//
//        ArrayList<CartDTO> cartDTOList = new ArrayList<>();
//        for (CartTM tm : cartList) {
//            CartDTO dto = new CartDTO(tm.getItemId(), tm.getName(), tm.getQuantity(), tm.getUnitPrice(), tm.getTotal());
//            cartDTOList.add(dto);
//        }
//
//        try {
//            boolean isPlaced = orderModel.saveOrder(order, cartDTOList);
//            if (isPlaced) {
//                showAlert(Alert.AlertType.INFORMATION, "Order placed successfully!");
//                clearFields();
//                setNextOrderId();
//                setNextPaymentId();
//            } else {
//                showAlert(Alert.AlertType.ERROR, "Failed to place order.");
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
//        }
//    }
//
    private void calculateTotal() {
        double total = 0;
        for (CartTM tm : cartList) {
            total += tm.getTotal();
        }
        lblTotalAmount.setText(String.format("Rs. %.2f", total));
    }

    private void clearFields() {
        datePickerDate.setValue(null);
        cmbItemId.getSelectionModel().clearSelection();
        txtCustomerPhone.clear();
        Customerdetails.setText("Customer ID");
        lblCustomerName.setText("Customer Name");
        cartList.clear();
        lblTotalAmount.setText("Rs. 0.00");
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }

    public void btnSearchItemOnAction(ActionEvent actionEvent) {
        if(cmbItemId.getSelectionModel().getSelectedItem().equals("Plant Order")) {
            nevigateTo("/view/PlantCartPage.fxml");
        } else if (cmbItemId.getSelectionModel().getSelectedItem().equals( "Fish Order")) {
            nevigateTo("/view/FishCartPage.fxml");
        }
    }

    public void btnCheckBalanceOnAction(ActionEvent actionEvent) {
        try {
            double paid = Double.parseDouble(txtPaidAmount.getText());
            String totalStr = lblTotalAmount.getText().replace("Rs. ", "");
            double total = Double.parseDouble(totalStr);

            double change = paid - total;
            lblChange.setText(String.format("Rs. %.2f", change));
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid amount");
        }
    }

//    public void btnAddtoCartOnAction(ActionEvent actionEvent) {
//
//        Button btn = new Button("Remove");
//        CartTM cartTM = new CartTM(itemId, name, qty, unitPrice, total, btn);
//        cartList.add(cartTM);
//
//        btn.setOnAction(e -> {
//            cartList.remove(cartTM);
//            calculateTotal();
//        });
//
//        calculateTotal();
//    }


    public void btnAddtoCartOnAction(ActionEvent actionEvent) {
        String itemId = "PL001";
        String name = "Anubias";
        int qty = 2;
        double unitPrice = 150.0;
        double total = qty * unitPrice;
        Button btn = new Button("Remove");
        CartTM cartTM = new CartTM(itemId, name, qty, unitPrice, total, btn);
        cartList.add(cartTM);
        btn.setOnAction(e -> {
            cartList.remove(cartTM);
            calculateTotal();
        });

        calculateTotal();
    }


    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
    }
}