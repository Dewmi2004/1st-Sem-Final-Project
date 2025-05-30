package lk.ijse.aquariumfinal.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.aquariumfinal.dto.TankDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TankModel {
    public static ObservableList<String> getTankId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("select tank_Id from tank");
        ObservableList<String> TankDtoArrayList = FXCollections.observableArrayList();
        while (rs.next()) {
            TankDtoArrayList.add(rs.getString("tank_Id"));
        }
        return  TankDtoArrayList ;
    }

    public static boolean saveTank(TankDTO tankDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO tank VALUES (?, ?, ?, ?)",
                tankDto.getTankId(), tankDto.getGlassType(), tankDto.getFishOrPlant(), tankDto.getWaterType());
    }

    public static boolean updateTank(TankDTO tankDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE tank SET glass_Type = ?, fish_Or_Plant = ?, water_Type = ? WHERE tank_Id = ?",
                tankDto.getGlassType(), tankDto.getFishOrPlant(), tankDto.getWaterType(), tankDto.getTankId());
    }


    public ArrayList<TankDTO> getAllTanks() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM tank");
        ArrayList<TankDTO> tankList = new ArrayList<>();

        while (rs.next()) {
            TankDTO tankDto = new TankDTO(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
            );
            tankList.add(tankDto);
        }
        return tankList;
    }

    public String getNextTankId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT tank_Id FROM tank ORDER BY tank_Id DESC LIMIT 1");
        char prefix = 'T';

        if (rs.next()) {
            String lastId = rs.getString(1);
            String number = lastId.substring(1);
            int nextId = Integer.parseInt(number) + 1;
            return String.format("%c%03d", prefix, nextId);
        }
        return prefix + "001";
    }

    public boolean deleteTank(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM tank WHERE tank_Id = ?", id);
    }

    public ObservableList<String> getGlassTypes() {
        return FXCollections.observableArrayList(
                "Acrylic",
                "Glass",
                "Tempered Glass",
                "Plastic"
        );
    }

    public ObservableList<String> getTankTypes() {
        return FXCollections.observableArrayList(
                "Fish",
                "Plant",
                "Mixed"
        );
    }

    public ObservableList<String> getWaterTypes() {
        return FXCollections.observableArrayList(
                "Fresh Water",
                "Brackish Water",
                "Salt Water (Marine)",
                "Soft Water",
                "Hard Water",
                "Cold Water",
                "Tropical Water"
        );
    }
}
