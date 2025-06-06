package lk.ijse.aquariumfinal.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.aquariumfinal.dto.CartDTO;
import lk.ijse.aquariumfinal.dto.CustomerDTO;
import lk.ijse.aquariumfinal.dto.FishDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FishModel {
    public static boolean saveFish(FishDTO fishDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into fish values(?,?,?,?,?,?,?,?,? )", fishDto.getFishId(),fishDto.getName(),fishDto.getSize(),fishDto.getTankId(),fishDto.getGender(),fishDto.getWaterType(),fishDto.getCountry(),fishDto.getColour(),fishDto.getQuantity());
    }

    public static boolean UpdateFish(FishDTO fishDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update fish set name = ?,size = ?,tank_Id = ?,gender = ?,water_Type = ? ,country = ? ,colour = ? , quantity = ? where fish_Id = ?",fishDto.getName(),fishDto.getSize(),fishDto.getTankId(),fishDto.getGender(),fishDto.getWaterType(),fishDto.getCountry(),fishDto.getColour(),fishDto.getQuantity(),fishDto.getFishId());
    }

    public static ObservableList<String> getAllFishIDS() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT fish_Id FROM fish");
        ObservableList<String> fishIdList = FXCollections.observableArrayList();
        while (rs.next()) {
            fishIdList.add(rs.getString("fish_Id"));
        }
        return fishIdList;
    }

    public static FishDTO searchFishByName(String fishId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT name FROM fish WHERE fish_Id = ?", fishId);
        if (rs.next()) {
            return new FishDTO(
                    rs.getString("name")
            );
        }
        return null;
    }

    public static CartDTO searchFishUnitPrice(String fishId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT price FROM fish_detail WHERE fish_Id = ?", fishId);
        if (rs.next()) {
            return new CartDTO(
                    rs.getString("price")
            );
        }
        return null;
    }

    public ArrayList<FishDTO> getAllFish() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("select * from fish");
        ArrayList<FishDTO> fishDtoArrayList = new ArrayList<>();
        while (rs.next()) {
            FishDTO fishDto = new FishDTO(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9));
            fishDtoArrayList.add(fishDto);
        }
        return fishDtoArrayList;
    }

    public String getNextFish() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("select fish_Id from fish order by fish_Id DESC limit 1");
        char tableCharactor ='F';
        if(rs.next()){
            String lastId =rs.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString =String.format("F%03d", nextIdNumber);
            return nextIdString;
        }
        return tableCharactor+"001";
    }

    public Boolean deleteFish(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from fish where fish_Id = ?",id);

    }

    public ObservableList<String> getFishSize() {

        return FXCollections.observableArrayList(
                "Fry Fish",
                "Juvenile Fish",
                "Young Adult Fish",
                "Adult age",
                "Mature Adult Fish"
        );
    }

    public ObservableList<String> getFishGender() {
        return FXCollections.observableArrayList(
                "Male",
                "Female"
        );
    }

    public ObservableList<String> getFishWatertype() {
        return FXCollections.observableArrayList(
                "Fresh Water",
                "Brackish  Water",
                "Salt Water (Marian)",
                "Soft Water",
                "Hard Water",
                "Cold Water",
                "Tropical Water"
        );
    }

    public ObservableList<String> getFishCountry() {
        return FXCollections.observableArrayList(
                "Sri Lanka",
                "Maldives",
                "Djibouti",
                "Australia",
                "Japan",
                "Brazil",
                "Somalia",
                "United States",
                "Indonesia",
                "Ghana",
                "Malaysia"
        );
    }
}
