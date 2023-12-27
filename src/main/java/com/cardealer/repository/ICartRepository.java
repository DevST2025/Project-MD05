package com.cardealer.repository;

import com.cardealer.model.entity.ShopingCart;
import com.cardealer.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartRepository extends JpaRepository<ShopingCart, String> {
    void deleteByUser(User user);
    List<ShopingCart> findByUser(User user);
}
