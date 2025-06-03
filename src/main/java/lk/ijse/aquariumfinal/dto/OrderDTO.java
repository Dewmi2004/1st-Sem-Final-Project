package lk.ijse.aquariumfinal.dto;

import lombok.*;


import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class OrderDTO {

    private String orderId;
    private String paymentId;
    private Date date;
    private String customerId;
    private String itemType;
    private String payment_Id;
   private String method;
   private String amount;
   private String fishId;
   private String plantId;

    public OrderDTO(String orderId, String paymentId, Date date, String customerId, String itemType) {
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.date = date;
        this.customerId = customerId;
        this.itemType = itemType;

    }
}
