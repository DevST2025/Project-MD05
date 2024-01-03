package com.cardealer.service;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.common.EOrderStatus;
import com.cardealer.model.dto.request.OrderRequest;
import com.cardealer.model.dto.response.OrderResponse;
import com.cardealer.model.entity.Order;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IOrderService {
    //User
    Order createOrder(OrderRequest orderRequest) throws NotFoundException;
    List<OrderResponse> findAllByUser() throws NotFoundException;
    Order findBySerialNumber(String serialNumber) throws NotFoundException;
    List<OrderResponse> finAllByUserAndStatus(String status) throws NotFoundException;
    void changeToCancelById(String orderId) throws NotFoundException;

    //Admin
    List<OrderResponse> findAll();
    List<OrderResponse> finAllByStatus(String status);
    Order findById(String id) throws NotFoundException;
    void changeStatusById(String id, String status) throws NotFoundException;
    Double revenue(Date start, Date end);
}
