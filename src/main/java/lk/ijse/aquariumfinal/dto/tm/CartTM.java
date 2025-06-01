package lk.ijse.aquariumfinal.dto.tm;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class CartTM {
    private String ItemId;
    private String Name;
    private String Quantity;
    private String UnitPrice;
}
