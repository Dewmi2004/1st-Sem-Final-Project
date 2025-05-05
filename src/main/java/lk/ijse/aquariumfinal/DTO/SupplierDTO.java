package lk.ijse.aquariumfinal.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class SupplierDTO {
    private String supId;
    private String name;
    private String contact;
    private String companyAddress;
    private String supplyType;
    private String Email;
}
