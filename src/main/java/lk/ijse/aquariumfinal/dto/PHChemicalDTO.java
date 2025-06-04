package lk.ijse.aquariumfinal.dto;

import lombok.*;

import java.sql.Date;
import java.sql.Time;
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class PHChemicalDTO {
    private String tankId;
    private String chemicalId;
    private String phLevel;
    private String date;
    private String time;


}
