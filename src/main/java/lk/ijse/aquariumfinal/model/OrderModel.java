package lk.ijse.aquariumfinal.model;

import lk.ijse.aquariumfinal.db.DBConnection;
import lk.ijse.aquariumfinal.dto.CartDTO;
import lk.ijse.aquariumfinal.dto.FishDTO;
import lk.ijse.aquariumfinal.dto.OrderDTO;
import lk.ijse.aquariumfinal.dto.PlantDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderModel {

    public boolean saveOrder(OrderDTO dto,FishDTO fish,PlantDTO plant) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        con.setAutoCommit(false);
        try {
            boolean isPaymentSaved = CrudUtil.execute(
                    "INSERT INTO payment(payment_Id,method,date,amount) VALUES (?,?,?,?)",
                    dto.getPaymentId(),dto.getMethod(),dto.getDate(),dto.getAmount()
            );

if (isPaymentSaved) {
    boolean isOrderSaved = CrudUtil.execute(
            "INSERT INTO orders(order_Id, payment_Id,date, customer_Id, item) VALUES (?, ?, ?, ?, ?)",
            dto.getOrderId(), dto.getPaymentId(), dto.getDate(), dto.getCustomerId(), dto.getItemType()
    );
    if (isOrderSaved) {
        boolean isItemOrderSaved = false;
        boolean isQuantityUpdated = false;

        if (dto.getItemType().equals("Fish Order")) {
            isItemOrderSaved = CrudUtil.execute(
                    "INSERT INTO order_fish(order_Id, fish_Id) VALUES (?, ?)",
                    dto.getOrderId(),fish.getFishId()
            );

            isQuantityUpdated = CrudUtil.execute(
                    "UPDATE fish SET quantity = quantity - ? WHERE fish_Id = ?",
                    fish.getQuantity(), fish.getFishId()

            );

        } else if (dto.getItemType().equals("Plant Order")) {
            isItemOrderSaved = CrudUtil.execute(
                    "INSERT INTO order_plant(order_Id, plant_Id) VALUES (?, ?)",
                    dto.getOrderId(),plant.getPlantId()
            );

            isQuantityUpdated = CrudUtil.execute(
                    "UPDATE plant SET quantity = quantity - ? WHERE plant_Id = ?",
                    plant.getQuantity(), plant.getPlantId()
            );
        }
        con.commit();
        return true;
    }else {
        con.rollback();
        return false;
    }
}else{
    con.rollback();
    return false;
}
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public String generateNextOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT order_Id FROM orders ORDER BY CAST(SUBSTRING(order_Id, 4) AS UNSIGNED) DESC LIMIT 1");
        if (rs.next()) {
            String lastId = rs.getString(1);
            int nextId = Integer.parseInt(lastId.replace("ORD", "")) + 1;
            return String.format("ORD%03d", nextId);
        } else {
            return "ORD001";
        }
    }


    public String generateNextPaymentId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT payment_Id FROM orders ORDER BY CAST(SUBSTRING(payment_Id, 4) AS UNSIGNED) DESC LIMIT 1");
        if (rs.next()) {
            String lastId = rs.getString(1);
            int nextId = Integer.parseInt(lastId.replace("PAY", "")) + 1;
            return String.format("PAY%03d", nextId);
        } else {
            return "PAY001";
        }
    }


}