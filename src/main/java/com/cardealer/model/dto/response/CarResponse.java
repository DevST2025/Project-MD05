package com.cardealer.model.dto.response;

import com.cardealer.model.common.ECarStatus;
import com.cardealer.model.common.EType;
import com.cardealer.model.entity.Brand;
import com.cardealer.model.entity.Color;
import com.cardealer.model.entity.ShopingCart;
import com.cardealer.model.entity.WishList;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarResponse {
    private String id;
    private String name;
    private Set<Color> colors;
    private EType type;
    private String description;
    private Double unitPrice;
    private Integer stockQuantity;
    private String image;
    private Brand brand;
    private ECarStatus status;
    private String sku;
}
