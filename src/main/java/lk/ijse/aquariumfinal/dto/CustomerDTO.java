package lk.ijse.aquariumfinal.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class CustomerDTO {
    private String id;
    private String name;
    private String address;
    private String gender;
    private String dob;
    private String email;
    private String contact;
public CustomerDTO(String id, String name) {
    this.id = id;
    this.name = name;
}


}

