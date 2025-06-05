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
import java.util.Map;

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
    public static String getSupplierEmailById(String supplierId) {
        String sql = "SELECT Email FROM supplier WHERE sup_Id = ?";
        try {
            ResultSet resultSet = CrudUtil.execute(sql, supplierId);
            if (resultSet.next()) {
                return resultSet.getString("Email");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveInventory(InventoryDTO inventory, ArrayList<InventryTM> itemList, Map<String, Integer> updatedQuantities) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        con.setAutoCommit(false);

        try {
            boolean isInventorySaved = CrudUtil.execute(
                    "INSERT INTO inventory (inventory_Id, sup_Id, date) VALUES (?, ?, ?)",
                    inventory.getInventoryId(), inventory.getSupId(), inventory.getDate()
            );

            if (!isInventorySaved) {
                con.rollback();
                return false;
            }

            for (InventryTM item : itemList) {
                String itemId = item.getItemId();
                String quantityStr = item.getQuantity();
                String priceStr = item.getUnitPrice();
                String inventoryId = inventory.getInventoryId();
                int qtyToAdd = Integer.parseInt(quantityStr);

                boolean isSaved;
                ResultSet rs;
                int existingQty;
                int newQty;

                switch (inventory.getItemType()) {
                    case "Plant":
                        isSaved = CrudUtil.execute(
                                "INSERT INTO plant_detail (plant_id, quantity, price, inventoryId) VALUES (?, ?, ?, ?)",
                                itemId, quantityStr, priceStr, inventoryId
                        );
                        if (!isSaved) {
                            con.rollback();
                            return false;
                        }

                        rs = CrudUtil.execute(
                                "SELECT total_Quantity FROM plant_detail WHERE plant_id = ? ORDER BY inventoryId DESC LIMIT 1", itemId
                        );
                        existingQty = rs.next() ? Integer.parseInt(rs.getString("total_quantity")) : 0;
                        newQty = existingQty + qtyToAdd;
                        CrudUtil.execute("UPDATE plant_detail SET total_Quantity = ? WHERE plant_id = ?", String.valueOf(newQty), itemId);
                        updatedQuantities.put(itemId, newQty);
                        break;

                    case "Fish":
                        isSaved = CrudUtil.execute(
                                "INSERT INTO fish_detail (fish_id, quantity, price, inventoryId) VALUES (?, ?, ?, ?)",
                                itemId, quantityStr, priceStr, inventoryId
                        );
                        if (!isSaved) {
                            con.rollback();
                            return false;
                        }

                        rs = CrudUtil.execute(
                                "SELECT total_Quantity FROM fish_detail WHERE fish_id = ? ORDER BY inventoryId DESC LIMIT 1", itemId
                        );
                        existingQty = rs.next() ? Integer.parseInt(rs.getString("total_quantity")) : 0;
                        newQty = existingQty + qtyToAdd;
                        CrudUtil.execute("UPDATE fish_detail SET total_Quantity = ? WHERE fish_id = ?", String.valueOf(newQty), itemId);
                        updatedQuantities.put(itemId, newQty);
                        break;

                    case "Food":
                        isSaved = CrudUtil.execute(
                                "INSERT INTO food_detail (food_id, quantity, price, inventoryId) VALUES (?, ?, ?, ?)",
                                itemId, quantityStr, priceStr, inventoryId
                        );
                        if (!isSaved) {
                            con.rollback();
                            return false;
                        }

                        rs = CrudUtil.execute(
                                "SELECT total_Quantity FROM food_detail WHERE food_id = ? ORDER BY inventoryId DESC LIMIT 1", itemId
                        );
                        existingQty = rs.next() ? Integer.parseInt(rs.getString("total_quantity")) : 0;
                        newQty = existingQty + qtyToAdd;
                        CrudUtil.execute("UPDATE food_detail SET total_Quantity = ? WHERE food_id = ?", String.valueOf(newQty), itemId);
                        updatedQuantities.put(itemId, newQty);
                        break;

                    case "Chemical":
                        isSaved = CrudUtil.execute(
                                "INSERT INTO chemical_detail (chemical_id, quantity, price, inventoryId) VALUES (?, ?, ?, ?)",
                                itemId, quantityStr, priceStr, inventoryId
                        );
                        if (!isSaved) {
                            con.rollback();
                            return false;
                        }

                        rs = CrudUtil.execute(
                                "SELECT total_Quantity FROM chemical_detail WHERE chemical_id = ? ORDER BY inventoryId DESC LIMIT 1", itemId
                        );
                        existingQty = rs.next() ? Integer.parseInt(rs.getString("total_quantity")) : 0;
                        newQty = existingQty + qtyToAdd;
                        CrudUtil.execute("UPDATE chemical_detail SET total_Quantity = ? WHERE chemical_id = ?", String.valueOf(newQty), itemId);
                        updatedQuantities.put(itemId, newQty);
                        break;

                    default:
                        con.rollback();
                        return false;
                }
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }
    }





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