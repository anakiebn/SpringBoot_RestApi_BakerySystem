package com.anakie.restApiBakery.entity;

import com.anakie.restApiBakery.exception.UserNotFoundException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Entity
@RequiredArgsConstructor
@Table(name="`order`")
@Setter
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    @Column(name="orderDateTime")
    private final LocalDateTime dateTime=LocalDateTime.now();

}
