package com.cardealer.model.entity;

import com.cardealer.model.common.EOrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 100)
    private String serialNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    private EOrderStatus status;
    @Column(length = 100)
    private String note;
    @Column(length = 100)
    private String receiveName;
    private String receiveAddress;
    @Column(length = 12)
    private String receivePhone;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd/MM/yyyy")
    @Column(name = "created_at")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd/MM/yyyy")
    @Column(name = "received_at")
    private Date receivedAt;

//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
//    private List<OrderDetail> orderDetails = new ArrayList<>();
}
