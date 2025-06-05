package lk.ijse.aquariumfinal.model;

import lk.ijse.aquariumfinal.dto.PHChemicalDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PHChemicalModel {

    public ArrayList<PHChemicalDTO> getAllPHChemical() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM ph_chemical");
        ArrayList<PHChemicalDTO> list = new ArrayList<>();

        while (rs.next()) {
            PHChemicalDTO dto = new PHChemicalDTO(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)
            );
            list.add(dto);
        }
        return list;
    }

    public boolean savePHChemical(PHChemicalDTO dto) throws SQLException, ClassNotFoundException {
        System.out.println("Saving: " + dto);
        String sql = "INSERT INTO ph_chemical (ph_Level ,tank_Id , check_In_Time , chemical_Id , date) VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                dto.getPhLevel(),
                dto.getTankId(),
                dto.getTime(),
                dto.getChemicalId(),
                dto.getDate()
        );
    }


    public boolean updatePHChemical(PHChemicalDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE ph_chemical SET ph_Level = ? ,check_In_Time = ? ,chemical_Id = ? , date = ? WHERE tank_Id = ?",
                dto.getPhLevel(),dto.getTime(),dto.getChemicalId(),dto.getDate(),dto.getTankId());

    }

    public boolean deletePHChemical(String date) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM ph_chemical WHERE date = ?", date);

    }
}
