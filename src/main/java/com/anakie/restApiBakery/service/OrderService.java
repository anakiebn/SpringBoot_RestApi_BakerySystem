package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Order;
import com.anakie.restApiBakery.entity.OrderDTO;
import com.anakie.restApiBakery.entity.Status;
import com.anakie.restApiBakery.exception.*;

import java.util.List;

public interface OrderService {

    void bakeProducts(Order order) throws OutOfStockException;

    void changeOrderStatus(Order order, Status status);

    Order save(OrderDTO orderDTO) throws Exception;
    Order findById(Long id) throws OrderNotFoundException;

    void deleteById(Long id) throws OrderNotFoundException;

    Order update(Order orders) throws OrderNotFoundException;

    List<Order> findAll();
    boolean existsById(Long id);

}
