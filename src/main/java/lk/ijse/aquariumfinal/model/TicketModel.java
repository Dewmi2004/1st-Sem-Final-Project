package lk.ijse.aquariumfinal.model;
import lk.ijse.aquariumfinal.dto.TicketDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TicketModel {

    public boolean saveTicket(TicketDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO ticket VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                dto.getTicketId(),
                dto.getAge(),
                dto.getPrice(),
                dto.getDate(),
                dto.getTime(),
                dto.getCustomerId(),
                dto.getEmployeeId(),
                dto.getQuantity(),
                dto.getFullPrice());
    }

    public boolean updateTicket(TicketDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE ticket SET age=?, price=?, date=?, time=?, customer_Id=?, employee_Id=?, Quantity=? WHERE ticket_Id=?",
                dto.getAge(),
                dto.getPrice(),
                dto.getDate(),
                dto.getTime(),
                dto.getCustomerId(),
                dto.getEmployeeId(),
                dto.getQuantity(),
                dto.getTicketId());
    }

    public boolean deleteTicket(String ticketId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM ticket WHERE ticket_Id = ?", ticketId);
    }

    public ArrayList<TicketDTO> getAllTickets() throws SQLException, ClassNotFoundException {
        ArrayList<TicketDTO> list = new ArrayList<>();
        ResultSet rs = CrudUtil.execute("SELECT * FROM ticket");
        while (rs.next()) {
            list.add(new TicketDTO(
                    rs.getString("ticket_Id"),
                    rs.getString("age"),
                    rs.getString("price"),
                    rs.getDate("date"),
                    rs.getTime("time"),
                    rs.getString("customer_Id"),
                    rs.getString("employee_Id"),
                    rs.getString("Quantity"),
                    rs.getString("Full_Price")
            ));
        }
        return list;
    }

    public String getNextTicketId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT ticket_Id FROM ticket ORDER BY CAST(SUBSTRING(ticket_Id, 5) AS UNSIGNED) DESC LIMIT 1");
        if (rs.next()) {
            String lastId = rs.getString(1);
            int nextId = Integer.parseInt(lastId.replace("TKT-", "")) + 1;
            return String.format("TKT-%03d", nextId);
        } else {
            return "TKT-001";
        }
    }
}
