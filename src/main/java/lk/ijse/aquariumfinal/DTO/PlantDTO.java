package lk.ijse.aquariumfinal.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class PlantDTO {

    private String plantId;
    private String name;
    private String waterType;
    private String inventoryId;
    private String tankId;
    private String size;

}
