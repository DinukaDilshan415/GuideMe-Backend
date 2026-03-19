package me.dinuka.guideme.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String name;
    private String email;
    private String mobile;
    private String otp;
    private String createdAt;
    private String updatedAt;
    private String imageUrl;
    private int statusId;
}
