package com.cardealer.controller;

import com.cardealer.exception.NotFoundException;
import com.cardealer.exception.PasswordNotMatchException;
import com.cardealer.model.dto.request.OrderRequest;
import com.cardealer.model.dto.request.PasswordRequest;
import com.cardealer.model.dto.request.ShopingCartRequest;
import com.cardealer.model.dto.request.UserRequest;
import com.cardealer.model.dto.response.OrderResponse;
import com.cardealer.model.entity.Order;
import com.cardealer.model.entity.ShopingCart;
import com.cardealer.model.entity.User;
import com.cardealer.repository.ICartRepository;
import com.cardealer.service.ICartService;
import com.cardealer.service.IOrderService;
import com.cardealer.service.IUserService;
import com.cardealer.util.JsonArg;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.cardealer.com/v1/user")
public class UserController {
    private final ICartService cartService;
    private final IUserService userService;
    private final IOrderService orderService;
    @PostMapping("/shopping-cart")
    public ResponseEntity<String> addCarToCart(@Valid @RequestBody ShopingCartRequest shopingCartRequest) throws NotFoundException {
        cartService.addCarToCart(shopingCartRequest);
        return new ResponseEntity<>("Đã thêm vào giõ hàng", HttpStatus.OK);
    }
    @GetMapping("/shopping-cart")
    public ResponseEntity<List<ShopingCart>> findAllCartItem() throws NotFoundException {
        return new ResponseEntity<>(userService.findAllCartItem(), HttpStatus.OK);
    }
    @PutMapping("/shopping-cart/{cartItemId}")
    public ResponseEntity<ShopingCart> changeQuantityCartItem(@PathVariable String cartItemId, @RequestBody Map<String, Integer> map) throws NotFoundException {
        return new ResponseEntity<>(cartService.changeQuantity(cartItemId, map.get("quantity")), HttpStatus.OK);
    }
    @DeleteMapping("/shopping-cart/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable String cartItemId) throws NotFoundException {
        cartService.deleteById(cartItemId);
        return new ResponseEntity<>("Xoá thành công", HttpStatus.OK);
    }

    @DeleteMapping("/shopping-cart")
    public ResponseEntity<String> deleteAllCartItem() throws NotFoundException {
        cartService.deleteAllCartItem();
        return new ResponseEntity<>("Xoá thành công", HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@RequestBody OrderRequest orderRequest) throws NotFoundException {
        return new ResponseEntity<>(orderService.createOrder(orderRequest), HttpStatus.OK);
    }

    @GetMapping("/account")
    public ResponseEntity<User> showProfile() throws NotFoundException {
        return new ResponseEntity<>(userService.showProfile(), HttpStatus.OK);
    }

    @PutMapping("/account")
    public ResponseEntity<User> editProfile(@ModelAttribute UserRequest userRequest) throws NotFoundException {
        return new ResponseEntity<>(userService.editProfile(userRequest), HttpStatus.OK);
    }

    @PutMapping("/account/change-password")
    public ResponseEntity<String> changePass(@RequestBody PasswordRequest passwordRequest) throws NotFoundException, PasswordNotMatchException {
        userService.changePassword(passwordRequest);
        return new ResponseEntity<>("Đổi mật khẩu thành công", HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderResponse>> getListOrder() throws NotFoundException {
        return new ResponseEntity<>(orderService.findAllByUser(), HttpStatus.OK);
    }

    @GetMapping("/history/{serialNumber}/serial")
    public ResponseEntity<Order> findBySerialNumber(@PathVariable String serialNumber) throws NotFoundException {
        return new ResponseEntity<>(orderService.findBySerialNumber(serialNumber), HttpStatus.OK);
    }

    @GetMapping("/history/{orderStatus}/status")
    public ResponseEntity<List<OrderResponse>> getListOrderByStatus(@PathVariable String orderStatus) throws NotFoundException {
        return new ResponseEntity<>(orderService.finAllByUserAndStatus(orderStatus), HttpStatus.OK);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable String orderId) throws NotFoundException {
        orderService.changeToCancelById(orderId);
        return new ResponseEntity<>("Huỷ đơn hàng thành công", HttpStatus.OK);
    }




}
