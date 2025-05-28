package lk.ijse.aquariumfinal.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class PlantTM {
    private String plantId;
    private String name;
    private String waterType;
    private String tankId;
    private String size;

}
