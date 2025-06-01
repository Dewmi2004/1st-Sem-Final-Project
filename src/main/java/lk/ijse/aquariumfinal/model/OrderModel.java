package lk.ijse.aquariumfinal.model;

import lk.ijse.aquariumfinal.dto.CustomerDTO;
import lk.ijse.aquariumfinal.dto.OrderDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderModel {

    public boolean saveOrder(OrderDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO orders VALUES (?, ?, ?, ?, ?)",
                dto.getOrderId(),
                dto.getPaymentId(),
                dto.getDate(),
                dto.getCustomerId(),
                dto.getItem()
        );
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
    public ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException {
        ArrayList<OrderDTO> list = new ArrayList<>();
        ResultSet rs = CrudUtil.execute("SELECT * FROM orders");
        while (rs.next()) {
            list.add(new OrderDTO(
                    rs.getString("order_Id"),
                    rs.getString("payment_Id"),
                    rs.getDate("date"),
                    rs.getString("customer_Id"),
                    rs.getString("item")
            ));
        }
        return list;
    }
}
