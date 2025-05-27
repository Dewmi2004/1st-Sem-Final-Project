package lk.ijse.aquariumfinal.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class TankTM {
    private String tankId;
    private String glassType;
    private String fishOrPlant;
    private String waterType;
}
