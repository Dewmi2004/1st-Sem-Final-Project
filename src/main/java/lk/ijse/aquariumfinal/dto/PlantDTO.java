package lk.ijse.aquariumfinal.dto;

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
    private String tankId;
    private String size;
    private String quantity;

    public PlantDTO(String name) {
        this.name = name;
    }

    public PlantDTO(String plantId, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, String s) {
    }
}
