package lk.ijse.aquariumfinal.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.aquariumfinal.dto.InventoryDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryModel {
    public ArrayList<InventoryDTO> getAllInventry() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("select * from inventory");
        ArrayList<InventoryDTO> inventryDtoArrayList = new ArrayList<>();
        while (rs.next()) {
            InventoryDTO inventryDto = new InventoryDTO(rs.getString(1),rs.getString(2),rs.getString(3));
            inventryDtoArrayList.add(inventryDto);
        }
        return inventryDtoArrayList;
    }

    public String getNextInventry() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("select inventory_Id from inventory order by inventory_Id DESC limit 1");
        char tableCharactor ='I';
        if(rs.next()){
            String lastId =rs.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString =String.format("I%03d", nextIdNumber);
            return nextIdString;
        }
        return tableCharactor+"001";
    }

    public Boolean updateInventry(InventoryDTO I) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update inventory set sup_Id = ?,date = ? where inventory_Id =  ?",I.getSupId(),I.getDate(),I.getInventoryId());
    }

    public Boolean deleteInventry(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from inventory where inventory_Id = ?",id);
    }

    public boolean saveInventry(InventoryDTO inventoryDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into inventory values(?,?,? )", inventoryDTO.getInventoryId(), inventoryDTO.getSupId(), inventoryDTO.getDate());
    }
    public ObservableList getAllSupplierId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("select sup_Id from supplier");
        ObservableList<String> supplierDtoArrayList = FXCollections.observableArrayList();
        while (rs.next()) {
            supplierDtoArrayList.add(rs.getString("sup_Id"));
        }
        return  supplierDtoArrayList;
    }
}
