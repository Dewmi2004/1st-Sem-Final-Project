package lk.ijse.aquariumfinal.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class FishTM {
    private String fishId;
    private String name;
    private String size;
    private String tankId;
    private String gender;
    private String waterType;
    private String country;
    private String inventoryId;
    private String colour;

}