package com.cardealer.service;

import com.cardealer.exception.NotFoundException;
import com.cardealer.exception.PasswordNotMatchException;
import com.cardealer.model.dto.request.PasswordRequest;
import com.cardealer.model.dto.request.RegisterRequest;
import com.cardealer.model.dto.request.UserRequest;
import com.cardealer.model.entity.ShopingCart;
import com.cardealer.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IUserService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    User findByUsername(String username) throws NotFoundException;
    User findByEmail(String email) throws NotFoundException;
    User findByPhone(String phone) throws NotFoundException;
    User loadByUsername(String username) throws NotFoundException;
    void register(RegisterRequest request);

    Page<User> findAll(Pageable pageable);
    User upgradeAccount(String id) throws NotFoundException;
    User downgradeAccount(String id) throws NotFoundException;
    User receptorAccount(String id) throws NotFoundException;
    Page<User> findUserByName(String name, Pageable pageable);
    List<ShopingCart> findAllCartItem() throws NotFoundException;
    User showProfile() throws NotFoundException;
    User editProfile(UserRequest userRequest) throws NotFoundException;
    void changePassword(PasswordRequest passwordRequest) throws NotFoundException, PasswordNotMatchException;


}
