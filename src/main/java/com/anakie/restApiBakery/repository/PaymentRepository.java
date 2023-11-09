package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

}
