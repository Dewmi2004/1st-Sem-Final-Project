package lk.ijse.aquariumfinal.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class TankDTO {
    private String tankId;
    private String glassType;
    private String fishOrPlant;
    private String waterType;


}
