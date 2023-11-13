package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
