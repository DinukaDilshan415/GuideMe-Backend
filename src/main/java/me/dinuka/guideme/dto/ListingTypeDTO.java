package me.dinuka.guideme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingTypeDTO {
    private int id;
    private String type;
    private String icon;
}
