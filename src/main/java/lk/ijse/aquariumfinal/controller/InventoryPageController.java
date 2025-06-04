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
import lk.ijse.aquariumfinal.dto.InventoryDTO;
import lk.ijse.aquariumfinal.dto.SupplierDTO;
import lk.ijse.aquariumfinal.dto.tm.InventryTM;
import lk.ijse.aquariumfinal.model.InventoryModel;


import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryPageController {

    @FXML
    private Label SupplierId;

    @FXML
    private Button btnAddToCart, btnPlaceOrder, btnSearchCustomer, btnSearchItem;

    @FXML
    private ComboBox<String> cmbItemId;

    @FXML
    private TableColumn<InventryTM, String> colInventoryId, colItemId;

    @FXML
    private TableColumn<InventryTM, Integer> colQty;

    @FXML
    private TableColumn<InventryTM, Button> colRemove;

    @FXML
    private TableColumn<InventryTM, Double> colUnitPrice;

    @FXML
    private DatePicker datePickerDate;

    @FXML
    private AnchorPane itemUiLoadPane;

    @FXML
    private Label lblInventoryId, lblSupplierName;

    @FXML
    private TableView<InventryTM> tblCart;

    @FXML
    private TextField txtSupplierPhone;

    private final InventoryModel inventoryModel = new InventoryModel();
    private final ObservableList<InventryTM> cartList = FXCollections.observableArrayList();

    public void initialize() throws SQLException, ClassNotFoundException {
        setNextInventoryId();
        loadItemTypes();
        setupTableColumns();
    }

    private void setupTableColumns() {
        colInventoryId.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("btn"));
        tblCart.setItems(cartList);
    }

    private void loadItemTypes() throws SQLException, ClassNotFoundException {
        cmbItemId.setItems(FXCollections.observableArrayList("Plant", "Fish", "Chemical","Food"));
    }

    private void setNextInventoryId() throws SQLException, ClassNotFoundException {
        lblInventoryId.setText(inventoryModel.generateNextInventoryId());
    }

    @FXML
    void btnSearchSupplierOnAction(ActionEvent event) {
        String phone = txtSupplierPhone.getText();
        SupplierDTO supplier = InventoryModel. searchSupplierByPhone(phone);
        if (supplier != null) {
            SupplierId.setText(supplier.getSupId());
            lblSupplierName.setText(supplier.getName());
            showAlert(Alert.AlertType.INFORMATION, "Supplier Found");
        } else {
            showAlert(Alert.AlertType.WARNING, "Supplier Not Found");
        }
    }

    @FXML
    void btnSearchItemOnAction(ActionEvent event) {
        String selectedItem = cmbItemId.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an item type first.");
            return;
        }

        try {
            itemUiLoadPane.getChildren().clear();
            FXMLLoader fxmlLoader;

            if ("Plant".equals(selectedItem)) {
                fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/PlantDetail.fxml"));
            } else if ("Fish".equals(selectedItem)) {
                fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/FishDetail.fxml"));
            } else if ("Chemical".equals(selectedItem)) {
                fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/ChemicalDetail.fxml"));
            }else {
                fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/FoodDetail.fxml"));
            }

            AnchorPane pane = fxmlLoader.load();
            pane.prefWidthProperty().bind(itemUiLoadPane.widthProperty());
            pane.prefHeightProperty().bind(itemUiLoadPane.heightProperty());

            itemUiLoadPane.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddtoCartOnAction(ActionEvent event) {
        String inventoryId = lblInventoryId.getText();
        String itemId = "PL001"; // placeholder
        String qty = String.valueOf((10)); // placeholder
        String unitPrice = String.valueOf((100.0)); // placeholder

        Button btn = new Button("Remove");
        InventryTM tm = new InventryTM(inventoryId, itemId, qty, unitPrice, btn);

        btn.setOnAction(e -> {
            cartList.remove(tm);
        });

        cartList.add(tm);
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        if (datePickerDate.getValue() == null || cartList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please select a date and add items to the cart.");
            return;
        }

        InventoryDTO inventory = new InventoryDTO(
                lblInventoryId.getText(),
                Date.valueOf(datePickerDate.getValue()),
                SupplierId.getText()
        );

        ArrayList<InventryTM> itemList = new ArrayList<>(cartList);

        try {
            boolean isSaved = inventoryModel.saveInventory(inventory, itemList);
            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Inventory placed successfully!");
                clearFields();
                setNextInventoryId();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to place inventory.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        datePickerDate.setValue(null);
        cmbItemId.getSelectionModel().clearSelection();
        txtSupplierPhone.clear();
        SupplierId.setText("Supplier ID");
        lblSupplierName.setText("Supplier Name");
        cartList.clear();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }
}
