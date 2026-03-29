package me.dinuka.guideme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private int id;
    private int bookingId;
    private String userEmail;
    private String review;
    private double rating;
    private String createdAt;
    private String userImageUrl;
    private String userName;
    private String userCountry;
}
