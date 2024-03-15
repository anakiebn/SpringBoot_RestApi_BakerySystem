package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.*;
import com.anakie.restApiBakery.exception.OrderNotFoundException;
import com.anakie.restApiBakery.exception.OutOfStockException;
import com.anakie.restApiBakery.exception.ProductNotFoundException;
import com.anakie.restApiBakery.repository.OrderRepository;
import com.anakie.restApiBakery.repository.OrderStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Autowired
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    @Autowired
    private  final  OrderRepository orderRepository;
    @Autowired
    private  final UserService userService;
    @Autowired
    private  final  ShoppingCartService shoppingCartService;
    @Autowired
    private  final  ProductService productService;
    @Autowired
    private  final  IngredientService ingredientService;

    @Override
    public Order save(OrderDTO orderDTO) throws Exception {
        if (orderDTO == null) {
            throw new NullPointerException("Null order not allowed, provide none-null object");
        }

        Order order = orderDTO.toOrder(userService);
        order.setShoppingCart(shoppingCartService.save(order.getShoppingCart()));
        order.setTotalPrice(calculateTotal(order)); // set the total price of the order
        changeOrderStatus(order = orderRepository.save(order), Status.Processing);
        return order;
    }

    @Override
    public void changeOrderStatus(Order order, Status status) {
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(order);
        orderStatusHistory.setDateTime(LocalDateTime.now());
        orderStatusHistory.setStatus(status);
        orderStatusHistoryRepository.save(orderStatusHistory);
    }

    private double calculateTotal(Order order) throws ProductNotFoundException {
        double totalPrice = 0;
        for (CartItem cartItem : order.getShoppingCart().getCartItems()) {
            totalPrice += productService.totalAmount(cartItem.getProductId(), cartItem.getProductQty());
        }
        return totalPrice;
    }


    // once an order is made, we bake, when we bake we use ingredients, meaning, we subtract
    // all used ingredients according to the recipe
    public void bakeProducts(Order order) throws OutOfStockException {
        order.getShoppingCart().getCartItems().forEach(cartItem -> {

            Map<Ingredient, Double> recipeIngredients=productService.findById(cartItem.getProductId()).getRecipe().getRecipeIngredients();
            for (Ingredient recipeIngr: recipeIngredients.keySet()) {
                // this method is the one that does the magic o reducing ingredients
                ingredientService.useIngredient(recipeIngr.getId(),recipeIngredients.get(recipeIngr),cartItem.getProductQty());
            }
        });
    }

    @Override
    public Order findById(Long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order " + id + " not found, use an existing id!"));
    }

    @Override
    public void deleteById(Long id) throws OrderNotFoundException {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Can't delete order " + id + " not found, use an existing id!");
        }
        orderRepository.deleteById(id);
    }


    @Override
    public Order update(Order order) throws OrderNotFoundException {
        if (!orderRepository.existsById(order.getId())) {
            throw new OrderNotFoundException("Can't update order " + order.getId() + " not found, use an existing id!");
        }
        return orderRepository.save(order);
    }

    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }


    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
