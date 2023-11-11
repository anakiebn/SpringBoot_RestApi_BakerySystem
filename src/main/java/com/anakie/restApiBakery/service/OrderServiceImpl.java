package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.*;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.OrderNotFoundException;
import com.anakie.restApiBakery.exception.OutOfStockException;
import com.anakie.restApiBakery.exception.ProductNotFoundException;
import com.anakie.restApiBakery.repository.OrderRepository;
import com.anakie.restApiBakery.repository.OrderStatusHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderStatusHistoryRepository orderStatusHistoryRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private IngredientService ingredientService;


    @Override
    public Order save(OrderDTO orderDTO) throws Exception {
        Order order = orderDTO.toOrder(userService);
        ShoppingCart shoppingCart = shoppingCartService.save(order.getShoppingCart());
        order.setShoppingCart(shoppingCart);
        // this method subtracts ingredients in database, since baking means using stock ingredients
        bakeProducts(order);
        order.setTotalPrice(calculateTotal(order)); // set the total price of the order
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setStatus(Status.PROCESSING_ORDER); // new orders are given this status
        orderStatusHistory.setOrder(order=orderRepository.save(order)); // save order to get its ID so the status can reference it
        orderStatusHistoryRepository.save(orderStatusHistory);
        return order;
    }
    private double calculateTotal(Order order) throws ProductNotFoundException {
        double totalPrice = 0;
        for (CartItem cartItem : order.getShoppingCart().getCartItems()) {
            totalPrice += productService.totalAmount(cartItem.getProductId(), cartItem.getProductQty());
        }
        return totalPrice;
    }

      // once an order is made, we bake, when we bake we use ingredients, meaning, we subtract
     //  all used ingredients according to their recipe
    private void bakeProducts(Order order) {
        order.getShoppingCart().getCartItems().forEach(cartItem -> {
            try {
                useIngredients(cartItem);
            } catch (ProductNotFoundException | IngredientNotFoundException | OutOfStockException e) {
                throw new RuntimeException(e);
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
        orderRepository.deleteOrderById(id);
    }


    @Override
    public Order update(Order order) throws OrderNotFoundException {
        if (!orderRepository.existsById(order.getId())) {
            throw new OrderNotFoundException("Can't update order " + order.getId() + " not found, use an existing id!");
        }
        return orderRepository.save(order);
    }

    private void useIngredients(CartItem cartItem) throws ProductNotFoundException, IngredientNotFoundException, OutOfStockException {
        Product product = productService.findById(cartItem.getProductId());
        for (RecipeIngredient recipeIngredient : product.getRecipe().getRecipeIngredients()) {
            ingredientService.useIngredient(recipeIngredient, cartItem.getProductQty());
        }
    }

    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }


    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
