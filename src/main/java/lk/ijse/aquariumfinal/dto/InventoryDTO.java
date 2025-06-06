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
    private String quantity;


}
