package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.PaymentStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusHistoryRepository extends JpaRepository<PaymentStatusHistory,Long> {

}
