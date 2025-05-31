package lk.ijse.aquariumfinal.dto.tm;
import lombok.*;

import java.sql.Date;
import java.sql.Time;
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class TicketTM {
    private String ticketId;
    private String age;
    private String price;
    private Date date;
    private Time time;
    private String customerId;
    private String employeeId;
    private String Quantity;
    private String fullPrice;

}
