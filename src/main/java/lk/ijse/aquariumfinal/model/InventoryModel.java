package lk.ijse.aquariumfinal.model;

import lk.ijse.aquariumfinal.db.DBConnection;
import lk.ijse.aquariumfinal.dto.InventoryDTO;
import lk.ijse.aquariumfinal.dto.SupplierDTO;
import lk.ijse.aquariumfinal.dto.tm.InventryTM;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryModel {
    public static SupplierDTO searchSupplierByPhone(String phone) {
        try {
            ResultSet rs = CrudUtil.execute("SELECT sup_Id,Name FROM supplier WHERE Contact = ?", phone);
            if (rs.next()) {
                return new SupplierDTO(
                        rs.getString("sup_Id"),
                        rs.getString("Name")
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveInventory(InventoryDTO inventory, ArrayList<InventryTM> itemList) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        con.setAutoCommit(false);
        try {
            boolean isInventorySaved = CrudUtil.execute(
                    "INSERT INTO inventory (inventory_Id,sup_Id,date) VALUES (?,?,?)",
                    inventory.getInventoryId(), inventory.getSupId(), inventory.getDate()
            );
            if (isInventorySaved) {
                if (inventory.getItemType().equals("Plant")) {
//                    if()
                } else if (inventory.getItemType().equals("Fish")) {

                } else if (inventory.getItemType().equals("Food")) {

                }else if (inventory.getItemType().equals("Chemical")) {

                }
                con.commit();
                return true;
            }else {
                con.rollback();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            con.rollback();

        }finally {
            con.setAutoCommit(true);
        }
return false;
    }
// plant,food,chemical,fish detail wlt aluth ekk nam inseart wennai,okkoma ek wage item id thiyen ewa  nam quantity update wennai
    public String generateNextInventoryId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT inventory_Id FROM inventory ORDER BY CAST(SUBSTRING(inventory_Id, 4) AS UNSIGNED) DESC LIMIT 1");
        if (rs.next()) {
            String lastId = rs.getString(1);
            int nextId = Integer.parseInt(lastId.replace("I", "")) + 1;
            return String.format("I%03d", nextId);
        } else {
            return "I001";
        }
    }
}