package com.cardealer.service.impl;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.common.EOrderStatus;
import com.cardealer.model.dto.request.OrderRequest;
import com.cardealer.model.dto.response.OrderResponse;
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
import java.util.stream.Collectors;

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

    @Override
    public List<OrderResponse> findAllByUser() throws NotFoundException {
        User userLogin = getLoginUser();
        List<OrderResponse> orderResponses = orderRepository.findByUser(userLogin).stream().map(order -> {
            return OrderResponse.builder()
                    .serialNumber(order.getSerialNumber())
                    .totalPrice(order.getTotalPrice())
                    .note(order.getNote())
                    .status(order.getStatus())
                    .createdAt(order.getCreatedAt())
                    .build();
        }).toList();
        return orderResponses;
    }

    @Override
    public Order findBySerialNumber(String serialNumber) throws NotFoundException {
        return orderRepository.findBySerialNumberAndUser(serialNumber, getLoginUser()).orElseThrow(() -> new NotFoundException("Không tìm thấy order có số Serial: " + serialNumber));
    }

    @Override
    public List<OrderResponse> finAllByUserAndStatus(String status) throws NotFoundException {
        EOrderStatus eStatus = null;
        switch (status) {
            case "waiting" -> eStatus = EOrderStatus.WAITING;
            case "confirm" -> eStatus = EOrderStatus.CONFIRM;
            case "delivery" -> eStatus = EOrderStatus.DELIVERY;
            case "success" -> eStatus = EOrderStatus.SUCCESS;
            case "cancel" -> eStatus = EOrderStatus.CANCEL;
            case "denied" -> eStatus = EOrderStatus.DENIED;
        }
        return orderRepository.findByUserAndStatus(getLoginUser(), eStatus).stream().map(order -> {
            return OrderResponse.builder()
                    .serialNumber(order.getSerialNumber())
                    .totalPrice(order.getTotalPrice())
                    .note(order.getNote())
                    .status(order.getStatus())
                    .createdAt(order.getCreatedAt())
                    .build();
        }).toList();
    }

    @Override
    public void changeToCancelById(String orderId) throws NotFoundException {
        List<Order> orders = orderRepository.findByUser(getLoginUser()).stream().filter(order -> order.getStatus()==EOrderStatus.WAITING).toList();
        Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElseThrow(()-> new NotFoundException("Không tìm thấy order có ID: " + orderId));
        order.setStatus(EOrderStatus.CANCEL);
        orderRepository.save(order);
    }
    //Admin

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream().map(order -> {
            return OrderResponse.builder()
                    .serialNumber(order.getSerialNumber())
                    .totalPrice(order.getTotalPrice())
                    .note(order.getNote())
                    .status(order.getStatus())
                    .createdAt(order.getCreatedAt())
                    .build();
        }).toList();
    }

    @Override
    public List<OrderResponse> finAllByStatus(String status) {
        EOrderStatus eStatus = null;
        switch (status) {
            case "waiting" -> eStatus = EOrderStatus.WAITING;
            case "confirm" -> eStatus = EOrderStatus.CONFIRM;
            case "delivery" -> eStatus = EOrderStatus.DELIVERY;
            case "success" -> eStatus = EOrderStatus.SUCCESS;
            case "cancel" -> eStatus = EOrderStatus.CANCEL;
            case "denied" -> eStatus = EOrderStatus.DENIED;
        }
        return orderRepository.findByStatus(eStatus).stream().map(order -> {
            return OrderResponse.builder()
                    .serialNumber(order.getSerialNumber())
                    .totalPrice(order.getTotalPrice())
                    .note(order.getNote())
                    .status(order.getStatus())
                    .createdAt(order.getCreatedAt())
                    .build();
        }).toList();
    }

    @Override
    public Order findById(String id) throws NotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy order có ID: " + id));
    }

    @Override
    public void changeStatusById(String id, String status) throws NotFoundException {
        EOrderStatus eStatus = null;
        switch (status) {
            case "waiting" -> eStatus = EOrderStatus.WAITING;
            case "confirm" -> eStatus = EOrderStatus.CONFIRM;
            case "delivery" -> eStatus = EOrderStatus.DELIVERY;
            case "success" -> eStatus = EOrderStatus.SUCCESS;
            case "cancel" -> eStatus = EOrderStatus.CANCEL;
            case "denied" -> eStatus = EOrderStatus.DENIED;
        }
        Order order = findById(id);
        order.setStatus(eStatus);
        orderRepository.save(order);
    }

    @Override
    public Double revenue(Date start, Date end) {
        return orderRepository.revenue(start, end);
    }
}
