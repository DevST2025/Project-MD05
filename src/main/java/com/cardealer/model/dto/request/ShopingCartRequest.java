package com.cardealer.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopingCartRequest {
    private String user_id;
    @NotBlank
    private String car_id;
//    @Size(min = 1)
    private Integer quantity;
}
