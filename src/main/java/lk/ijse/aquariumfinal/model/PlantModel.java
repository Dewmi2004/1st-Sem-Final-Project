
package lk.ijse.aquariumfinal.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.aquariumfinal.dto.PlantDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlantModel {

    public static boolean savePlant(PlantDTO dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO plant (plant_Id, name, water_Type, tank_Id, size) VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, dto.getPlantId(), dto.getName(), dto.getWaterType(), dto.getTankId(), dto.getSize());
    }

    public static boolean updatePlant(PlantDTO dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE plant SET name = ?, water_Type = ?, tank_Id = ?, size = ? WHERE plant_Id = ?";
        return CrudUtil.execute(sql, dto.getName(), dto.getWaterType(), dto.getTankId(), dto.getSize(), dto.getPlantId());
    }

    public static boolean deletePlant(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM plant WHERE plant_Id = ?";
        return CrudUtil.execute(sql, id);
    }

    public static ArrayList<PlantDTO> getAllPlants() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM plant";
        ResultSet rs = CrudUtil.execute(sql);
        ArrayList<PlantDTO> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new PlantDTO(
                    rs.getString("plant_Id"),
                    rs.getString("name"),
                    rs.getString("water_Type"),
                    rs.getString("tank_Id"),
                    rs.getString("size")
            ));
        }

        return list;
    }

    public String getNextPlantId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT plant_Id FROM plant ORDER BY plant_Id DESC LIMIT 1";
        ResultSet rs = CrudUtil.execute(sql);

        if (rs.next()) {
            String lastId = rs.getString("plant_Id");
            int num = Integer.parseInt(lastId.replace("PL", ""));
            return String.format("PL%03d", num + 1);
        } else {
            return "PL001";
        }
    }

    public ObservableList<String> getPlantTypes() {
        return FXCollections.observableArrayList("Freshwater", "Saltwater", "Brackish");
    }

    public ObservableList<String> getPlantSizes() {
        return FXCollections.observableArrayList("Small", "Medium", "Large");
    }
}
