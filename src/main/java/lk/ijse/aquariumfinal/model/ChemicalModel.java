package lk.ijse.aquariumfinal.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.aquariumfinal.dto.ChemicalDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChemicalModel {

    public static boolean saveChemical(ChemicalDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO chemical VALUES (?, ?, ?, ?, ?)",
                dto.getChemicalId(), dto.getAcidOrBase(), dto.getConcentration(), dto.getStoreType(), dto.getName());
    }

    public static boolean updateChemical(ChemicalDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE chemical SET acid_Or_Base = ?, concentration = ?, store_Type = ?, name = ? WHERE chemical_Id = ?",
                dto.getAcidOrBase(), dto.getConcentration(), dto.getStoreType(), dto.getName(), dto.getChemicalId());
    }

    public boolean deleteChemical(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM chemical WHERE chemical_Id = ?", id);
    }

    public ArrayList<ChemicalDTO> getAllChemicals() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM chemical");
        ArrayList<ChemicalDTO> list = new ArrayList<>();

        while (rs.next()) {
            ChemicalDTO dto = new ChemicalDTO(
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

    public String getNextChemicalId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT chemical_Id FROM chemical ORDER BY chemical_Id DESC LIMIT 1");
        char prefix = 'C';

        if (rs.next()) {
            String lastId = rs.getString(1);
            String number = lastId.substring(1);
            int nextId = Integer.parseInt(number) + 1;
            return String.format("%c%03d", prefix, nextId);
        }
        return prefix + "001";
    }

    public ObservableList<String> getChemicalTypes() {
        return FXCollections.observableArrayList("Acid", "Base", "Neutral");
    }
}
