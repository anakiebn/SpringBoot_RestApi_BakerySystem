package com.anakie.restApiBakery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "payment")
    private List<PaymentStatusHistory> paymentStatusHistories=new ArrayList<>();

    @Column( scale = 2)
    private double amount;

    @Enumerated(EnumType.STRING)
    private PAYMENT_METHOD paymentMethod;

}
