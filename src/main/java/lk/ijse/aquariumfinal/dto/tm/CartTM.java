package lk.ijse.aquariumfinal.dto.tm;
import javafx.scene.control.Button;
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
private String Total;
private Button btn ;

}
