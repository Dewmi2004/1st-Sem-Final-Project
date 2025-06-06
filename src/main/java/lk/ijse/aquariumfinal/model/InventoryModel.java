package lk.ijse.aquariumfinal.model;

import lk.ijse.aquariumfinal.db.DBConnection;
import lk.ijse.aquariumfinal.dto.*;
import lk.ijse.aquariumfinal.dto.tm.InventryTM;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class InventoryModel {
    private PlantDTO plant =new PlantDTO();
    private FishDTO fish =new FishDTO();
    private ChemicalDTO chemical = new ChemicalDTO();
    private FoodDTO food = new FoodDTO();
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
String plantquantity = plant.getQuantity();
String fishquantity = fish.getQuantity();
String chemicalquantity =chemical.getQuantity();
String foodquantity = food.getQuantity();
                boolean isSaved;
                boolean isUpdated;




                switch (inventory.getItemType()) {
                    case "Plant":
                        isSaved = CrudUtil.execute(
                                "INSERT INTO plant_detail (plant_Id, quantity, price, inventory_Id) VALUES (?, ?, ?, ?)",
                                itemId, quantityStr, priceStr, inventoryId
                        );
                        if (!isSaved) {
                            con.rollback();
                            return false;
                        }

                         isUpdated = CrudUtil.execute("UPDATE plant SET quantity = quantity + ? WHERE plant_Id=?",plantquantity, itemId);

                        if (!isUpdated) {
                            con.rollback();
                            return false;
                        }
                        updatedQuantities.put(itemId, Integer.valueOf(plantquantity));
                        break;

                    case "Fish":
                        isSaved = CrudUtil.execute(
                                "INSERT INTO fish_detail (fish_Id, quantity, price, inventory_Id) VALUES (?, ?, ?, ?)",
                                itemId, quantityStr, priceStr, inventoryId
                        );
                        if (!isSaved) {
                            con.rollback();
                            return false;
                        }
                         isUpdated = CrudUtil.execute("UPDATE fish SET quantity = quantity + ? WHERE fish_Id=?",fishquantity, itemId);

                        if (!isUpdated) {
                            con.rollback();
                            return false;
                        }
                       updatedQuantities.put(itemId, Integer.valueOf(fishquantity));
                        break;

                    case "Food":
                        isSaved = CrudUtil.execute(
                                "INSERT INTO food_detail (food_Id, quantity, price, inventory_Id) VALUES (?, ?, ?, ?)",
                                itemId, quantityStr, priceStr, inventoryId
                        );
                        if (!isSaved) {
                            con.rollback();
                            return false;
                        }
                         isUpdated = CrudUtil.execute("UPDATE food SET quantity = quantity + ? WHERE food_Id=?",chemicalquantity, itemId);

                        if (!isUpdated) {
                            con.rollback();
                            return false;
                        }
                      updatedQuantities.put(itemId, Integer.valueOf(chemicalquantity));
                        break;

                    case "Chemical":
                        isSaved = CrudUtil.execute(
                                "INSERT INTO chemical_detail (chemical_Id, quantity, price, inventory_Id) VALUES (?, ?, ?, ?)",
                                itemId, quantityStr, priceStr, inventoryId
                        );
                        if (!isSaved) {
                            con.rollback();
                            return false;
                        }
                         isUpdated = CrudUtil.execute("UPDATE chemical SET quantity = quantity + ? WHERE chemical_Id=?",foodquantity, itemId);

                        if (!isUpdated) {
                            con.rollback();
                            return false;
                        }
                       updatedQuantities.put(itemId, Integer.valueOf(foodquantity));
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