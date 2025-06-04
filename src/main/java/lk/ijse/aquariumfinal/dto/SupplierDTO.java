package lk.ijse.aquariumfinal.dto;

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


    public SupplierDTO(String supplierId, String name) {
        this.supId = supplierId;
        this.name = name;

    }
}
