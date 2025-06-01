package lk.ijse.aquariumfinal.dto;
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
}
