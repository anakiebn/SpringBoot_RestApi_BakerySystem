package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.*;
import com.anakie.restApiBakery.exception.*;
import com.anakie.restApiBakery.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
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
    public Order save(OrderDTO orderDTO) throws  Exception {
        Order order=orderDTO.toOrder(userService);
        ShoppingCart shoppingCart=shoppingCartService.save(order.getShoppingCart());
        order.setShoppingCart(shoppingCart);
        // now we do the subtraction of ingredients
        shoppingCart.getCartItems().forEach(cartItem -> {
            try {
                useIngredients(cartItem);
            } catch (ProductNotFoundException | IngredientNotFoundException | OutOfStockException e) {
                throw new RuntimeException(e);
            }
        });
        return orderRepository.save(order);
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
        Product product=productService.findById(cartItem.getProductId());
        for(RecipeIngredient recipeIngredient:product.getRecipe().getRecipeIngredients()){
            ingredientService.useIngredient(recipeIngredient,cartItem.getProductQty());
        }
    }

    public boolean existsById(Long id){
        return orderRepository.existsById(id);
    }
    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
