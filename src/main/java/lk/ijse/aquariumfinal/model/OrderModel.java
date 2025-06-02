package lk.ijse.aquariumfinal.model;

import lk.ijse.aquariumfinal.db.DBConnection;
import lk.ijse.aquariumfinal.dto.CartDTO;
import lk.ijse.aquariumfinal.dto.OrderDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderModel {

    public boolean saveOrder(OrderDTO order, ArrayList<CartDTO> cartList) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        try {
            con.setAutoCommit(false);

            boolean isOrderSaved = CrudUtil.execute(
                    "INSERT INTO orders VALUES (?, ?, ?, ?, ?)",
                    order.getOrderId(),
                    order.getPaymentId(),
                    order.getDate(),
                    order.getCustomerId(),
                    order.getItem()
            );

            if (!isOrderSaved) {
                con.rollback();
                return false;
            }

            for (CartDTO cart : cartList) {
                boolean isCartSaved = CrudUtil.execute(
                        "INSERT INTO cart VALUES (?, ?, ?, ?, ?, ?)",
                        order.getOrderId(),
                        cart.getItemId(),
                        cart.getName(),
                        cart.getQuantity(),
                        cart.getUnitPrice(),
                        cart.getTotal()
                );

                if (!isCartSaved) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

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
