package lk.ijse.aquariumfinal.dto;

import lombok.*;

import java.sql.Date;
import java.sql.Time;
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class TicketDTO {
    private String ticketId;
    private int age;
    private double price;
    private Date date;
    private Time time;
    private String customerId;
    private String employeeId;

}