package com.cardealer.service;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.dto.request.ShopingCartRequest;
import com.cardealer.model.entity.ShopingCart;

import java.util.List;

public interface ICartService {
    void addCarToCart(ShopingCartRequest shopingCartRequest) throws NotFoundException;
    ShopingCart changeQuantity(String id, int quantity) throws NotFoundException;
    void deleteById(String id) throws NotFoundException;
    void deleteAllCartItem() throws NotFoundException;

}
