package com.cardealer.repository;

import com.cardealer.model.common.EOrderStatus;
import com.cardealer.model.entity.Car;
import com.cardealer.model.entity.Order;
import com.cardealer.model.entity.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);
    List<Order> findByUserAndStatus(User user, EOrderStatus status);
    List<Order> findByStatus(EOrderStatus status);
    Optional<Order> findBySerialNumber(String serialNumber);
    Optional<Order> findBySerialNumberAndUser(String serialNumber, User user);

    @Query(value = "select SUM(o.totalPrice) from orders o WHERE o.status = 'SUCCESS' AND o.created_at BETWEEN ?1 AND  ?2", nativeQuery = true)
    Double revenue(Date start, Date end);


}
