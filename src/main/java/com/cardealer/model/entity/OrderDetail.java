package com.cardealer.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_details")

public class OrderDetail {
    @EmbeddedId
    private OrderDetailId id;
    @JsonIgnore
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;
    @JsonIgnore
    @ManyToOne
    @MapsId("carId")
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(length = 100)
    private String name;
    private Double unitPrice;
    private Integer orderQuantity;
}


