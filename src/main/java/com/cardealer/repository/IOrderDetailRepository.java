package com.cardealer.repository;

import com.cardealer.model.entity.OrderDetail;
import com.cardealer.model.entity.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {

}
