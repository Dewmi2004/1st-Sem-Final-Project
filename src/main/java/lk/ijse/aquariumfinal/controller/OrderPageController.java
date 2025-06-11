package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.aquariumfinal.AppInitializer;
import lk.ijse.aquariumfinal.dto.*;
import lk.ijse.aquariumfinal.dto.tm.CartTM;
import lk.ijse.aquariumfinal.model.CustomerModel;
import lk.ijse.aquariumfinal.model.FishModel;
import lk.ijse.aquariumfinal.model.OrderModel;
import lk.ijse.aquariumfinal.model.PlantModel;
import lk.ijse.aquariumfinal.util.EmailUtil;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderPageController {

    @FXML public TextField txtCustomerPhone;
    @FXML public Label lblcustomerEmail;
    @FXML private Label lblOrderrid;
    @FXML public Label lblPaymentId;
    @FXML public Button btnSearchCustomer;
    @FXML public Button btnPlaceOrder;
    @FXML public DatePicker datePickerDate;
    @FXML public Label lblChange;
    @FXML public Button btnCheckBalance;
    @FXML public TextField txtPaidAmount;
    @FXML public Label lblTotalAmount;
    @FXML public ComboBox<String> cmbMethod;
    @FXML public TableColumn<CartTM, String> colItemId;
    @FXML public TableColumn<CartTM, String> colName;
    @FXML public TableColumn<CartTM, Integer> colQty;
    @FXML public TableColumn<CartTM, Double> colUnitPrice;
    @FXML public TableColumn<CartTM, Double> colTotal;
    @FXML public TableColumn<CartTM, Button> colRemove;
    @FXML public TableView<CartTM> tblCart;
    @FXML public AnchorPane itemUiLoadPane;
    @FXML public Button btnAddToCart;
    @FXML public Button btnSearchItem;
    @FXML public ComboBox<String> cmbItemId;
    @FXML public Label lblCustomerName;
    @FXML public Label CustomerId;

    private final OrderModel orderModel = new OrderModel();
    private final ObservableList<CartTM> cartList = FXCollections.observableArrayList();

    public String customerEmail = "";
    public static String fishId = "";
    public static int fishQty = 0;
    public static String plantId = "";
    public static int plantQty = 0;
    double total = 0;

    private PlantCartPageController plantCartController;
    private FishCartPageController fishCartController;
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
                CustomerId.setText(customer.getId());
                lblCustomerName.setText(customer.getName());
                lblcustomerEmail.setText(customer.getEmail());
                showAlert(Alert.AlertType.INFORMATION, "Customer Found");
            } else {
                showAlert(Alert.AlertType.WARNING, "Customer Not Found");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    private void calculateTotal() {
        total = 0;
        for (CartTM tm : cartList) {
            total += Double.parseDouble(tm.getTotal().replace("Rs. ", ""));
        }
        lblTotalAmount.setText(String.format("Rs. %.2f", total));
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        if (datePickerDate.getValue() == null || cmbItemId.getValue() == null || cartList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please complete the form and add items to cart.");
            return;
        }

        OrderDTO order = new OrderDTO(
                lblOrderrid.getText(),
                lblPaymentId.getText(),
                Date.valueOf(datePickerDate.getValue()),
                CustomerId.getText(),
                cmbItemId.getValue(),
                cmbMethod.getValue(),
                lblTotalAmount.getText()
        );

        PlantDTO plant = new PlantDTO(plantId, null, null, null, null, null, null, null, String.valueOf(plantQty));
        FishDTO fish = new FishDTO(fishId, null, null, null, null, null, null, null, String.valueOf(fishQty));

        ArrayList<CartDTO> cartDTOList = new ArrayList<>();
        for (CartTM tm : cartList) {
            cartDTOList.add(new CartDTO(tm.getItemId(), tm.getName(), tm.getQuantity(), tm.getUnitPrice(), tm.getTotal()));
        }

        try {
            boolean isPlaced = orderModel.saveOrder(order, fish, plant);
            if (isPlaced) {
                EmailUtil.sendOrderAllert(total,lblcustomerEmail.getText(), CustomerId.getText());
                showAlert(Alert.AlertType.CONFIRMATION, "Email sent!");
                showAlert(Alert.AlertType.INFORMATION, "Order placed successfully!");
                clearFields();
                setNextOrderId();
                setNextPaymentId();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to place order.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        datePickerDate.setValue(null);
        cmbItemId.getSelectionModel().clearSelection();
        txtCustomerPhone.clear();
        CustomerId.setText("Customer ID");
        lblCustomerName.setText("Customer Name");
        cartList.clear();
        lblTotalAmount.setText("Rs. 0.00");
        fishId = "";
        fishQty = 0;
        plantId = "";
        plantQty = 0;

    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }

    public void btnSearchItemOnAction(ActionEvent actionEvent) {
        String selectedItem = cmbItemId.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an item type first.");
            return;
        }

        try {
            itemUiLoadPane.getChildren().clear();
            FXMLLoader fxmlLoader;
            AnchorPane pane;

            if (selectedItem.equals("Plant Order")) {
                fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/PlantCartPage.fxml"));
                pane = fxmlLoader.load();
                plantCartController = fxmlLoader.getController();
                plantCartController.loadPlantIds();
            } else {
                fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/FishCartPage.fxml"));
                pane = fxmlLoader.load();
                fishCartController = fxmlLoader.getController();
                fishCartController.loadFishIds();
            }

            pane.prefWidthProperty().bind(itemUiLoadPane.widthProperty());
            pane.prefHeightProperty().bind(itemUiLoadPane.heightProperty());
            itemUiLoadPane.getChildren().add(pane);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnCheckBalanceOnAction(ActionEvent actionEvent) {
        try {
            double paid = Double.parseDouble(txtPaidAmount.getText());
            double total = Double.parseDouble(lblTotalAmount.getText().replace("Rs. ", ""));
            lblChange.setText(String.format("Rs. %.2f", paid - total));
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid amount");
        }
    }

    public void btnAddtoCartOnAction(ActionEvent actionEvent) {
        String selectedItemType = cmbItemId.getSelectionModel().getSelectedItem();

        if (selectedItemType == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an item type first.");
            return;
        }

        try {
            String itemId, name, qtyStr, unitPriceStr;

            if (selectedItemType.equals("Fish Order")) {
                if (fishCartController == null) {
                    showAlert(Alert.AlertType.WARNING, "Please load the Fish Order form first.");
                    return;
                }

                itemId = fishCartController.getSelectedFishId();
                name = fishCartController.getFishName();
                qtyStr = fishCartController.getQuantity();
                unitPriceStr = fishCartController.getUnitPrice();
            } else {
                if (plantCartController == null) {
                    showAlert(Alert.AlertType.WARNING, "Please load the Plant Order form first.");
                    return;
                }

                itemId = plantCartController.getSelectedPlantId();
                name = plantCartController.getPlantName();
                qtyStr = plantCartController.getQuantity();
                unitPriceStr = plantCartController.getUnitPrice();
            }

            int quantity = Integer.parseInt(qtyStr);
            double unitPrice = Double.parseDouble(unitPriceStr);
            double total = quantity * unitPrice;

            for (CartTM tm : cartList) {
                if (tm.getItemId().equals(itemId)) {
                    showAlert(Alert.AlertType.WARNING, "Item already in cart.");
                    return;
                }
            }

            Button btnRemove = new Button("Remove");
            CartTM cartTM = new CartTM(itemId, name, qtyStr, unitPriceStr, String.valueOf(total), btnRemove);

            btnRemove.setOnAction(e -> {
                cartList.remove(cartTM);
                calculateTotal();
            });

            cartList.add(cartTM);
            calculateTotal();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error adding item to cart: " + e.getMessage());
        }
    }
}
