package lk.ijse.aquariumfinal.dto;

import javafx.scene.control.Button;
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
    private String date;
    private Button btn;
    @Getter
    private String itemType;
    private String totalqty;
    private String quantity;


    public InventoryDTO(String inventoryId, Date date, String supId) {
        this.inventoryId = inventoryId;
        this.supId = supId;
        this.date = date.toString();
    }
}
