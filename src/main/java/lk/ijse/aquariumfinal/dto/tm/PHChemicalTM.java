package lk.ijse.aquariumfinal.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class PHChemicalTM {
    private String tankId;
    private String chemicalId;
    private String phLevel;
    private String date;
    private String time;
}