package com.cardealer.service.impl;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.dto.request.ShopingCartRequest;
import com.cardealer.model.entity.Car;
import com.cardealer.model.entity.ShopingCart;
import com.cardealer.model.entity.User;
import com.cardealer.repository.ICarRepository;
import com.cardealer.repository.ICartRepository;
import com.cardealer.repository.IUserRepository;
import com.cardealer.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;
    private final ICarRepository carRepository;
    public User getLoginUser() throws NotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new NotFoundException("Không tìm thấy user có username: " + userDetails.getUsername()));
    }
    @Override
    public void addCarToCart(ShopingCartRequest shopingCartRequest) throws NotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new NotFoundException("Không tìm thấy user có username: " + userDetails.getUsername()));
        Car car = carRepository.findById(shopingCartRequest.getCar_id()).orElseThrow(() -> new NotFoundException("Không tìm thấy xe nào có ID: " + shopingCartRequest));
        ShopingCart shopingCart = ShopingCart.builder()
                .user(user)
                .car(car)
                .quantity(shopingCartRequest.getQuantity())
                .build();
        cartRepository.save(shopingCart);
    }

    @Override
    public ShopingCart changeQuantity(String id, int quantity) throws NotFoundException {
        ShopingCart shopingCart = cartRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy Cart Item có ID: " + id));
        shopingCart.setId(id);
        shopingCart.setQuantity(quantity);
        cartRepository.save(shopingCart);
        return shopingCart;
    }

    @Override
    public void deleteById(String id) throws NotFoundException {
        cartRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy Cart Item có ID: " + id));
        cartRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAllCartItem() throws NotFoundException {
        User user = getLoginUser();
        cartRepository.deleteByUser(user);
    }
}
