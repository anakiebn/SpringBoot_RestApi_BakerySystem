package com.anakie.restApiBakery.entity;

import com.anakie.restApiBakery.exception.OrderNotFoundException;
import com.anakie.restApiBakery.service.OrderService;
import com.anakie.restApiBakery.service.PaymentService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDTO {

    private Long id;
    private Long orderId;

    private double amount;

    public Payment toPayment(OrderService orderService) throws OrderNotFoundException {
        Payment payment=new Payment();
        payment.setOrder(orderService.findById(orderId));
        payment.setAmount(amount);
        return payment;
    }
}
