package lk.ijse.aquariumfinal.DTO;

import lombok.*;

import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class FoodDTO {
    private String foodId;
    private String name;
    private String fishType;
    private Date exDate;
    private String inventoryId;


}
