package me.dinuka.guideme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDTO {
    private int id;
    private String address;
    private String city;
    private String postalCode;
    private int userId;
    private String userEmail;
}
