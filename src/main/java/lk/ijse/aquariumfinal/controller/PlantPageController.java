
package lk.ijse.aquariumfinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.aquariumfinal.dto.PlantDTO;
import lk.ijse.aquariumfinal.dto.tm.PlantTM;
import lk.ijse.aquariumfinal.model.PlantModel;
import lk.ijse.aquariumfinal.model.TankModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PlantPageController {
    private final PlantModel model = new PlantModel();

    public TableView<PlantTM> tblPlant;
    public Button btnUpdate1;
    public Button btnDelete1;
    public Button btnReset1;
    public Button btnSave1;
    public Button btnGenarateR1;
    public Label lblPlantId;
    public ComboBox<String> CBoxSize;
    public ComboBox<String> CBoxTank;
    public ComboBox<String> CBoxType;
    public TextField txtName;
    public TableColumn<?,?> colplantId;
    public TableColumn<?,?> colname;
    public TableColumn<?,?> colwaterrtype;
    public TableColumn<?,?> coltankId;
    public TableColumn<?,?> colsize;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setNextId();
        loadComboData();
        loadTable();
    }
    
    private void setCellValueFactory() {
        colplantId.setCellValueFactory(new PropertyValueFactory<>("plantId"));
        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        colwaterrtype.setCellValueFactory(new PropertyValueFactory<>("waterType"));
        coltankId.setCellValueFactory(new PropertyValueFactory<>("tankId"));
        colsize.setCellValueFactory(new PropertyValueFactory<>("size"));
    }

    private void loadComboData() throws SQLException, ClassNotFoundException {
        CBoxType.setItems(model.getPlantTypes());
        CBoxSize.setItems(model.getPlantSizes());
        CBoxTank.setItems(TankModel.getTankId());

    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        ArrayList<PlantDTO> plants = model.getAllPlants();
        ObservableList<PlantTM> obList = FXCollections.observableArrayList();

        for (PlantDTO dto : plants) {
            PlantTM tm = new PlantTM(
                    dto.getPlantId(),
                    dto.getName(),
                    dto.getWaterType(),
                    dto.getTankId(),
                    dto.getSize()
            );
            obList.add(tm);
        }
        tblPlant.setItems(obList);
    }

    private void setNextId() throws SQLException, ClassNotFoundException {
        String nextId = model.getNextPlantId();
        lblPlantId.setText(nextId);
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblPlantId.getText();
        String name = txtName.getText();
        String waterType = CBoxType.getValue();
        String tankId = CBoxTank.getValue();
        String size = CBoxSize.getValue();

        PlantDTO dto = new PlantDTO(id, name, waterType, tankId, size);
        boolean isSaved = PlantModel.savePlant(dto);

        if (isSaved) {
            loadTable();
            setNextId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Plant Saved").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Plant Not Saved").show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblPlantId.getText();
        String name = txtName.getText();
        String waterType = CBoxType.getValue();
        String tankId = CBoxTank.getValue();
        String size = CBoxSize.getValue();

        PlantDTO dto = new PlantDTO(id, name, waterType, tankId, size);
        boolean isUpdated = PlantModel.updatePlant(dto);

        if (isUpdated) {
            loadTable();
            setNextId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Plant Updated").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Plant Not Updated").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = lblPlantId.getText();
        if (id.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Select a plant to delete").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this plant?");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        confirm.getButtonTypes().setAll(no, yes);

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == yes) {
            boolean deleted = model.deletePlant(id);
            if (deleted) {
                loadTable();
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Plant Deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Plant Not Deleted").show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Plant Not Deleted ").show();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFields();
        btnSave1.setDisable(false);
        btnUpdate1.setDisable(true);
        btnDelete1.setDisable(true);
        tblPlant.getSelectionModel().clearSelection();
    }

    public void btnGenarateROnAction(ActionEvent actionEvent) {

    }

    public void clickOnAction(MouseEvent mouseEvent) {
        PlantTM selected = tblPlant.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblPlantId.setText(selected.getPlantId());
            txtName.setText(selected.getName());
            CBoxType.setValue(selected.getWaterType());
            CBoxTank.setValue(selected.getTankId());
            CBoxSize.setValue(selected.getSize());

            btnSave1.setDisable(true);
            btnUpdate1.setDisable(false);
            btnDelete1.setDisable(false);
        }
    }

    private void clearFields() throws SQLException, ClassNotFoundException {
        txtName.clear();
        CBoxType.getSelectionModel().clearSelection();
        CBoxTank.getSelectionModel().clearSelection();
        CBoxSize.getSelectionModel().clearSelection();
        setNextId();
    }
}
