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
     private int Quantity;
     private double UnitPrice;
     private double Total;
     private Button btn ;
     private String fishId;
     private String plantId;

     public CartDTO(String itemId, String name, int quantity, double unitPrice, double total) {
          this.ItemId = itemId;
          this.Name = name;
          this.Quantity = quantity;
          this.UnitPrice = unitPrice;
          this.Total = total;

     }
}
