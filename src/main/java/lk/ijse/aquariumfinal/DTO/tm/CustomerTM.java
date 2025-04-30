package lk.ijse.aquariumfinal.DTO.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CustomerTM {
    private String id;
    private String name;
    private String address;
    private String dob;
    private String gender;
    private String contact;
    private String email;
}