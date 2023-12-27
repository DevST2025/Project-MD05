package com.cardealer.service.impl;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.common.EOrderStatus;
import com.cardealer.model.dto.request.OrderRequest;
import com.cardealer.model.entity.*;
import com.cardealer.repository.ICartRepository;
import com.cardealer.repository.IOrderDetailRepository;
import com.cardealer.repository.IOrderRepository;
import com.cardealer.repository.IUserRepository;
import com.cardealer.service.IOrderService;
import com.cardealer.service.IUserService;
import com.cardealer.util.SerialGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;
    private final ICartRepository cartRepository;
    private final IOrderDetailRepository orderDetailRepository;
    public User getLoginUser() throws NotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new NotFoundException("Không tìm thấy user có username: " + userDetails.getUsername()));
    }
    @Transactional
    @Override
    public Order createOrder(OrderRequest orderRequest) throws NotFoundException {
        User user = getLoginUser();

        List<ShopingCart> cartList = cartRepository.findByUser(user);
        Order order = new Order();
        order.setSerialNumber(SerialGenerator.generateSerial());
        order.setUser(user);
        order.setTotalPrice(cartList.stream().mapToDouble(cart -> cart.getQuantity() * cart.getCar().getUnitPrice()).sum());
        order.setStatus(EOrderStatus.WAITING);
        order.setNote(orderRequest.getNote());
        order.setReceiveName(orderRequest.getReceiveName());
        order.setReceiveAddress(orderRequest.getReceiveAddress());
        order.setReceivePhone(orderRequest.getReceivePhone());
        order.setCreatedAt(new Date(System.currentTimeMillis()));
        order.setReceivedAt(new Date(System.currentTimeMillis() + 34560000));
        Order o=  orderRepository.save(order);
        List<OrderDetail> detailList = new ArrayList<>();
        for (ShopingCart item : cartList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(new OrderDetailId(o.getId(),item.getCar().getId()));
            orderDetail.setOrder(o);
            orderDetail.setCar(item.getCar());
            orderDetail.setName(item.getCar().getName());
            orderDetail.setOrderQuantity(item.getQuantity());
            orderDetail.setUnitPrice(item.getCar().getUnitPrice());
            detailList.add(orderDetail);
        }
        orderDetailRepository.saveAll(detailList);
        cartRepository.deleteByUser(user);
        return order;
    }
}
