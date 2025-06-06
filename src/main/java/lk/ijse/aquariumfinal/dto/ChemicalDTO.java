package lk.ijse.aquariumfinal.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class ChemicalDTO {

    private String chemicalId;
    private String acidOrBase;
    private String concentration;
    private String storeType;
    private String name;
    private String quantity;


}
