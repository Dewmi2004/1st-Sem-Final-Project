package lk.ijse.aquariumfinal.DTO;

import lombok.*;

import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class InventoryDTO {
    private String inventoryId;
    private String supId;
    private Date date;


}
