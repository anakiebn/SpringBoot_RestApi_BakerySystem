package com.anakie.restApiBakery.entity;

import com.anakie.restApiBakery.exception.CartEmptyException;
import com.anakie.restApiBakery.exception.ProductNotFoundException;
import com.anakie.restApiBakery.exception.ShoppingCartNotFound;
import com.anakie.restApiBakery.exception.UserNotFoundException;
import com.anakie.restApiBakery.service.UserService;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@Slf4j
public class OrderDTO {

    private final LocalDateTime dateTime = LocalDateTime.now();
    private Long id;
    private Long userId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;
    public Order toOrder(UserService userService) throws UserNotFoundException {
        Order order = new Order();
        //if we don't find the user we throw an exception
        User user = userService.getUserById(userId);
        order.setUser(user);
        order.setShoppingCart(this.getShoppingCart());
        return order;

    }
}
