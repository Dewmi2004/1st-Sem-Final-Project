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
    private int Quantity;
    private double UnitPrice;
private double Total;
private Button btn ;

}
