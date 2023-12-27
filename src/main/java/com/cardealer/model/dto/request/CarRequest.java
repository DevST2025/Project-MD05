package com.cardealer.model.dto.request;

import com.cardealer.model.common.ECarStatus;
import com.cardealer.model.common.EType;
import com.cardealer.model.entity.Brand;
import com.cardealer.model.entity.Color;
import com.cardealer.model.entity.ShopingCart;
import com.cardealer.model.entity.WishList;
import com.cardealer.util.SKUGenerator;
import com.cardealer.util.validator.CarNameUnique;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {
    @NotBlank(message = "Car's name cannot be left blank.")
    private String name;
    private Set<String> listColors;
    @NotBlank(message = "Car's type cannot be left blank.")
    private String type;
    private String description;
    private Double unitPrice;
    private Integer stockQuantity;
    private MultipartFile file;
    @NotBlank(message = "Car's brand cannot be left blank.")
    private String brandId;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date updatedAt;
    @NotBlank(message = "Car's status cannot be left blank.")
    private String status;
    private String sku;
}
