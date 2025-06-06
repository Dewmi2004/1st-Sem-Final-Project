package lk.ijse.aquariumfinal.dto;
import javafx.scene.control.Button;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class CartDTO {
     private String ItemId;
     private String Name;
     private String Quantity;
     private String UnitPrice;
     private String Total;
     private Button btn ;
     private String fishId;
     private String plantId;
     private String totalQuantity;

     public CartDTO(String itemId, String name, String quantity, String unitPrice, String total) {
          this.ItemId = itemId;
          this.Name = name;
          this.Quantity = quantity;
          this.UnitPrice = unitPrice;
          this.Total = total;

     }

    public CartDTO(String unitPrice) {
          this.UnitPrice = unitPrice;
    }
}
