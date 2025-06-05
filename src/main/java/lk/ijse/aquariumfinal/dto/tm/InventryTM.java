package lk.ijse.aquariumfinal.dto.tm;

import javafx.scene.control.Button;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class InventryTM {
    private String inventoryId;
    private String supId;
    private String date;
    private Button btn;
    private String quantity;
    private String unitPrice;
    private String itemId;
    private String fishId;
    private String foodId;
    private String chemicalId;
    private String plantId;
    public InventryTM(String inventoryId, String itemId, String quantity, String unitPrice, Button btn) {

        this.inventoryId = inventoryId;
        this.btn = btn;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.itemId = itemId;

    }
}
