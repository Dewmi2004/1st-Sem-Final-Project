package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.aquariumfinal.dto.PHChemicalDTO;
import lk.ijse.aquariumfinal.dto.tm.PHChemicalTM;
import lk.ijse.aquariumfinal.model.PHChemicalModel;
import lk.ijse.aquariumfinal.model.TankModel;
import lk.ijse.aquariumfinal.model.ChemicalModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class PHChemicalPageController {

    public TableView<PHChemicalTM> tblPHChemical;
    public TableColumn<?, ?> clmChemicalId;
    public TableColumn<?, ?> clmTankId;
    public TableColumn<?, ?> clmPhLevel;
    public TableColumn<?, ?> clmDate;
    public TableColumn<?, ?> clmTime;

    public ComboBox<String> cmbChemicalId;
    public ComboBox<String> cmbTankId;
    public ComboBox<String> cmbPhLevel;
    public DatePicker datePickerdate;
    public TextField txtTime;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public Button btnGReport;

    private final PHChemicalModel phModel = new PHChemicalModel();

    public TableColumn<?,?> clmPHLevel;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        loadComboData();
        loadTable();

    }

    private void setCellValueFactory() {
        clmChemicalId.setCellValueFactory(new PropertyValueFactory<>("chemicalId"));
        clmTankId.setCellValueFactory(new PropertyValueFactory<>("tankId"));
        clmPHLevel.setCellValueFactory(new PropertyValueFactory<>("phLevel"));
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmTime.setCellValueFactory(new PropertyValueFactory<>("time"));
    }

    private void loadComboData() throws SQLException, ClassNotFoundException {
            ObservableList<String> tankIds = TankModel.getTankId();
            System.out.println("Loaded Tank IDs: " + tankIds); // Debug log
            cmbTankId.setItems(tankIds);

            cmbChemicalId.setItems(ChemicalModel.getChemicalId());
            cmbPhLevel.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
        }


    private void loadTable() throws SQLException, ClassNotFoundException {
        ArrayList<PHChemicalDTO> list = phModel.getAllPHChemical();
        ObservableList<PHChemicalTM> obList = FXCollections.observableArrayList();
        for (PHChemicalDTO dto : list) {
            obList.add(new PHChemicalTM(
                    dto.getChemicalId(),
                    dto.getTankId(),
                    dto.getPhLevel(),
                    dto.getDate(),
                    dto.getTime()
            ));
        }
        tblPHChemical.setItems(obList);
    }

    public void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (cmbTankId.getValue() == null || cmbChemicalId.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select valid Chemical ID and Tank ID").show();
            return;
        }

        PHChemicalDTO dto = new PHChemicalDTO(
                cmbChemicalId.getValue(),
                cmbTankId.getValue(),
                cmbPhLevel.getValue(),
                datePickerdate.getValue().toString(),
                txtTime.getText()
        );

        boolean isSaved = phModel.savePHChemical(dto);
        if (isSaved) {
            loadTable();
            resetForm();
            new Alert(Alert.AlertType.INFORMATION, "Saved!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save").show();
        }
    }


    public void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        PHChemicalDTO dto = new PHChemicalDTO(
                cmbChemicalId.getValue(),
                cmbTankId.getValue(),
                cmbPhLevel.getValue(),
                datePickerdate.getValue().toString(),
                txtTime.getText()
        );

        boolean isUpdated = phModel.updatePHChemical(dto);
        if (isUpdated) {
            loadTable();
            resetForm();
            new Alert(Alert.AlertType.INFORMATION, "Updated!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String date = String.valueOf(datePickerdate.getValue());
        Optional<ButtonType> confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO).showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.YES) {
            boolean isDeleted = phModel.deletePHChemical(date);
            if (isDeleted) {
                loadTable();
                resetForm();
                new Alert(Alert.AlertType.INFORMATION, "Deleted!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete").show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent event) {
        resetForm();
    }

    private void resetForm() {
        cmbChemicalId.getSelectionModel().clearSelection();
        cmbTankId.getSelectionModel().clearSelection();
        cmbPhLevel.getSelectionModel().clearSelection();
        datePickerdate.setValue(LocalDate.now());
        txtTime.clear();
    }

    public void btnGenerateROnAction(ActionEvent event) {

    }

    public void clickOnAction(MouseEvent event) {
        PHChemicalTM selected = tblPHChemical.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cmbChemicalId.setValue(selected.getChemicalId());
            cmbTankId.setValue(selected.getTankId());
            cmbPhLevel.setValue(selected.getPhLevel());
            datePickerdate.setValue(LocalDate.parse(selected.getDate()));
            txtTime.setText(selected.getTime());
        }
    }
}
//package lk.ijse.aquarium.model;
//
//import lk.ijse.aquarium.db.CrudUtil;
//import lk.ijse.aquarium.dto.PhChemicalDto;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PhChemicalModel {
//
//    public static boolean save(PhChemicalDto dto) throws SQLException {
//        String sql = "INSERT INTO ph_chemical (chemical_id, name, brand, size, quantity, supplier_id, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
//        return CrudUtil.execute(sql,
//                dto.getChemicalId(),
//                dto.getName(),
//                dto.getBrand(),
//                dto.getSize(),
//                dto.getQuantity(),
//                dto.getSupplierId(),
//                dto.getPrice()
//        );
//    }
//
//    public static boolean update(PhChemicalDto dto) throws SQLException {
//        String sql = "UPDATE ph_chemical SET name=?, brand=?, size=?, quantity=?, supplier_id=?, price=? WHERE chemical_id=?";
//        return CrudUtil.execute(sql,
//                dto.getName(),
//                dto.getBrand(),
//                dto.getSize(),
//                dto.getQuantity(),
//                dto.getSupplierId(),
//                dto.getPrice(),
//                dto.getChemicalId()
//        );
//    }
//
//    public static boolean delete(String chemicalId) throws SQLException {
//        String sql = "DELETE FROM ph_chemical WHERE chemical_id=?";
//        return CrudUtil.execute(sql, chemicalId);
//    }
//
//    public static PhChemicalDto search(String chemicalId) throws SQLException {
//        String sql = "SELECT * FROM ph_chemical WHERE chemical_id=?";
//        ResultSet resultSet = CrudUtil.execute(sql, chemicalId);
//
//        if (resultSet.next()) {
//            return new PhChemicalDto(
//                    resultSet.getString("chemical_id"),
//                    resultSet.getString("name"),
//                    resultSet.getString("brand"),
//                    resultSet.getString("size"),
//                    resultSet.getInt("quantity"),
//                    resultSet.getString("supplier_id"),
//                    resultSet.getDouble("price")
//            );
//        }
//        return null;
//    }
//
//    public static List<PhChemicalDto> getAll() throws SQLException {
//        List<PhChemicalDto> list = new ArrayList<>();
//        String sql = "SELECT * FROM ph_chemical";
//        ResultSet resultSet = CrudUtil.execute(sql);
//
//        while (resultSet.next()) {
//            list.add(new PhChemicalDto(
//                    resultSet.getString("chemical_id"),
//                    resultSet.getString("name"),
//                    resultSet.getString("brand"),
//                    resultSet.getString("size"),
//                    resultSet.getInt("quantity"),
//                    resultSet.getString("supplier_id"),
//                    resultSet.getDouble("price")
//            ));
//        }
//        return list;
//    }
//
//    public static String generateNextChemicalId() throws SQLException {
//        String sql = "SELECT chemical_id FROM ph_chemical ORDER BY chemical_id DESC LIMIT 1";
//        ResultSet resultSet = CrudUtil.execute(sql);
//
//        if (resultSet.next()) {
//            String lastId = resultSet.getString("chemical_id");
//            int nextId = Integer.parseInt(lastId.replace("PC", "")) + 1;
//            return String.format("PC%03d", nextId);
//        } else {
//            return "PC001";
//        }
//    }
//}