package lk.ijse.aquariumfinal.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class FishDTO {
    private String fishId;
    private String name;
    private String size;
    private String tankId;
    private String gender;
    private String waterType;
    private String country;
    private String colour;

    }
