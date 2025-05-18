package lk.ijse.aquariumfinal.dto.tm;

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
    private String gender;
    private String dob;
    private String email;
    private String contact;

}