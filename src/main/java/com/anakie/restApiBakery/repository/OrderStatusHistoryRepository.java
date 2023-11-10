package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory,Long> {
}
