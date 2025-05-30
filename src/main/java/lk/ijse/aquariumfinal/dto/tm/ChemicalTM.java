package lk.ijse.aquariumfinal.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class ChemicalTM{

    private String chemicalId;
    private String acidOrBase;
    private String concentration;
    private String storeType;
    private String name;

}
