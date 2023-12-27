package com.cardealer.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String note;
    private String receiveName;
    private String receiveAddress;
    private String receivePhone;
}
