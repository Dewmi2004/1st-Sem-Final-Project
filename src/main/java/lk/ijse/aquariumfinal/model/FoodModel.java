package lk.ijse.aquariumfinal.model;

import lk.ijse.aquariumfinal.dto.FoodDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoodModel {
    public static boolean saveFood(FoodDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO food VALUES (?, ?, ?, ?)",
                dto.getFoodId(), dto.getName(), dto.getFishType(), dto.getExDate());
    }

    public static boolean updateFood(FoodDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE food SET name = ?, fish_Type = ?, ex_Date = ? WHERE food_Id = ?",
                dto.getName(), dto.getFishType(), dto.getExDate(),  dto.getFoodId());
    }

    public ArrayList<FoodDTO> getAllFoods() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM food");
        ArrayList<FoodDTO> list = new ArrayList<>();

        while (rs.next()) {
            FoodDTO dto = new FoodDTO(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getDate(4)
            );
            list.add(dto);
        }
        return list;
    }

    public String getNextFoodId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT food_Id FROM food ORDER BY food_Id DESC LIMIT 1");
        char prefix = 'F';

        if (rs.next()) {
            String lastId = rs.getString(1);
            String number = lastId.substring(1);
            int nextId = Integer.parseInt(number) + 1;
            return String.format("%c%03d", prefix, nextId);
        }
        return prefix + "001";
    }

    public boolean deleteFood(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM food WHERE food_Id = ?", id);
    }
}
