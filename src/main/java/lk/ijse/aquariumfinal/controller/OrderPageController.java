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

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;


public class OrderPageController {
    @FXML
    public TextField txtCustomerPhone;

    @FXML
    private Label lblOrderrid;

    @FXML
    public Label lblPaymentId;

    @FXML
    public Button btnSearchCustomer;

    @FXML
    public Button btnPlaceOrder;

    @FXML
    public DatePicker datePickerDate;

    @FXML
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
    public Label lblCustomerName;
    private final OrderModel orderModel = new OrderModel();
    private final ObservableList<CartTM> cartList = FXCollections.observableArrayList();
    public Label CustomerId;

    public static String fishId = "";
    public static int fishQty = 0;

    public static String plantId = "";
    public static int plantQty = 0;


    private PlantCartPageController plantCartController;
    private FishCartPageController fishCartController;


    public void initialize() throws SQLException, ClassNotFoundException {
        setNextOrderId();
        setNextPaymentId();
        loadItemTypes();
        setupTableColumns();
    }

    private void loadItemTypes() throws SQLException, ClassNotFoundException {
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
                showAlert(Alert.AlertType.INFORMATION, "Customer Found");
            } else {
                showAlert(Alert.AlertType.WARNING, "Customer Not Found");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
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
        PlantDTO plant =new PlantDTO();
        plant.setPlantId(plantId);
        plant.setQuantity(String.valueOf(plantQty));

        FishDTO fish = new FishDTO();
        fish.setFishId(fishId);
        fish.setQuantity(String.valueOf(fishQty));


        ArrayList<CartDTO> cartDTOList = new ArrayList<>();
        for (CartTM tm : cartList) {
            CartDTO dto = new CartDTO(tm.getItemId(), tm.getName(), tm.getQuantity(), tm.getUnitPrice(), tm.getTotal());
            cartDTOList.add(dto);
        }

        try {
            boolean isPlaced = orderModel.saveOrder(order, fish ,plant);
            if (isPlaced) {

                showAlert(Alert.AlertType.INFORMATION, "Order placed successfully!");
                clearFields();
                setNextOrderId();
                setNextPaymentId();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to place order.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }

    }

    private void calculateTotal() {

        double total = 0;
        for (CartTM tm : cartList) {
            total += Double.parseDouble(tm.getTotal());
        }
        lblTotalAmount.setText(String.format("Rs. %.2f", total));
    }


    private void clearFields() {
        datePickerDate.setValue(null);
        cmbItemId.getSelectionModel().clearSelection();
        txtCustomerPhone.clear();
        CustomerId.setText("Customer ID");
        lblCustomerName.setText("Customer Name");
        cartList.clear();
        lblTotalAmount.setText("Rs. 0.00");
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

        switch (selectedItem) {
            case "Plant Order" -> {

                try {
                    itemUiLoadPane.getChildren().clear();

                    FXMLLoader fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/PlantCartPage.fxml"));
                    AnchorPane pane = fxmlLoader.load();
                    plantCartController = fxmlLoader.getController();
                    plantCartController.loadPlantIds();

                    pane.prefWidthProperty().bind(itemUiLoadPane.widthProperty());
                    pane.prefHeightProperty().bind(itemUiLoadPane.heightProperty());

                    itemUiLoadPane.getChildren().add(pane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case "Fish Order" -> {

                try {
                    itemUiLoadPane.getChildren().clear();

                    FXMLLoader fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/FishCartPage.fxml"));
                    AnchorPane pane = fxmlLoader.load();
                    fishCartController = fxmlLoader.getController();
                    fishCartController.loadFishIds();

                    pane.prefWidthProperty().bind(itemUiLoadPane.widthProperty());
                    pane.prefHeightProperty().bind(itemUiLoadPane.heightProperty());

                    itemUiLoadPane.getChildren().add(pane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

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

    public void btnAddtoCartOnAction(ActionEvent actionEvent) {
        String selectedItemType = cmbItemId.getSelectionModel().getSelectedItem();

        if (selectedItemType == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an item type first.");
            return;
        }

        try {
            String itemId, name, qtyStr, unitPriceStr;

            switch (selectedItemType) {
                case "Fish Order" -> {
                    if (fishCartController == null) {
                        showAlert(Alert.AlertType.WARNING, "Please load the Fish Order form first.");
                        return;
                    }

                    itemId = fishCartController.getSelectedFishId();
                    name = fishCartController.getFishName();
                    qtyStr = fishCartController.getQuantity();
                    unitPriceStr = fishCartController.getUnitPrice();
                }

                case "Plant Order" -> {
                    if (plantCartController == null) {
                        showAlert(Alert.AlertType.WARNING, "Please load the Plant Order form first.");
                        return;
                    }

                    itemId = plantCartController.getSelectedPlantId();
                    name = plantCartController.getPlantName();
                    qtyStr = plantCartController.getQuantity();
                    unitPriceStr = plantCartController.getUnitPrice();
                }

                default -> {
                    showAlert(Alert.AlertType.WARNING, "Unknown item type.");
                    return;
                }
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

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid quantity or unit price.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error adding item to cart: " + e.getMessage());
        }
    }

}