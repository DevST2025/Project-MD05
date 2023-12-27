package com.cardealer.model.entity;

import com.cardealer.model.common.ECarStatus;
import com.cardealer.model.common.EColor;
import com.cardealer.model.common.EType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cars")
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, length = 100)
    private String name;
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "car_color", joinColumns = @JoinColumn(name = "car_id"), inverseJoinColumns = @JoinColumn(name = "color_id"))
    private Set<Color> colors = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private EType type;
    @Column(columnDefinition = "text")
    private String description;
    private Double unitPrice;
    private Integer stockQuantity;
    private String image;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @Column(name = "created_at")
    private Date createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @Column(name = "updated_at")
    private Date updatedAt;
    @Enumerated(EnumType.STRING)
    private ECarStatus status;
//    @JsonIgnore
//    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
//    private List<ShopingCart> shopingCarts = new ArrayList<>();
//    @JsonIgnore
//    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
//    private List<WishList> wishLists = new ArrayList<>();

    @Column(length = 100)
    private String sku;
}
