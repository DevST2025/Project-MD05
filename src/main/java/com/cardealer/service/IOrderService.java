package com.cardealer.service;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.dto.request.OrderRequest;
import com.cardealer.model.entity.Order;

public interface IOrderService {
    Order createOrder(OrderRequest orderRequest) throws NotFoundException;
}
