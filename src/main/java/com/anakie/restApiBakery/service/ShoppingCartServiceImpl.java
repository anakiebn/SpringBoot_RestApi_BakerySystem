package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.CartItem;
import com.anakie.restApiBakery.entity.Product;
import com.anakie.restApiBakery.entity.ShoppingCart;
import com.anakie.restApiBakery.exception.*;
import com.anakie.restApiBakery.repository.CartItemRepository;
import com.anakie.restApiBakery.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    // The cart we have, productId as key, and it's quantity as value
    private Map<Long, Integer> cart;
    private final ProductService productService;
    private final IngredientService ingredientService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    public ShoppingCartServiceImpl(ProductService productService, IngredientService ingredientService, ShoppingCartRepository shoppingCartRepository, CartItemRepository cartItemRepository) {
        this.productService = productService;
        this.ingredientService = ingredientService;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public boolean addProduct(Long productId, int productQty) throws OutOfStockException, ProductNotFoundException {

        Product product = productService.findById(productId);

        Map<Long, Double> recipeIngr = product.getRecipe().toRecipeIngrId();

        recipeIngr.forEach((id, ingrQty) -> ingredientService.confirmStockAvailability(id, ingrQty, productQty)); // since we're just adding to a cart, we must first check if we have enough stock

        // if we reach here, means that we have enough stock

        cart.compute(productId, (key, value) -> value == null ? productQty : value + productQty);
        return cart.containsKey(productId);

    }


    @Override
    public boolean removeProduct(Long productId, int productQty) throws ProductNotFoundException, InvalidProductQuantity {
        // if we're trying to remove a product that doesn't exist in a cart, we throw an exception
        if (!cart.containsKey(productId)) {
            throw new ProductNotFoundException("Product not available in the cart");
        }
        Product product = productService.findById(productId);

        // since we're removing a product from a cart, then we must return ingredients to the db copy we just had.
        product.getRecipe().toRecipeIngrId().forEach((key, value) -> ingredientService.returnIngrToStockDb(key, value, productQty));

        int currentProductQty = cart.get(productId) - productQty; // subtract

        if (currentProductQty > 0) {
            cart.put(productId, currentProductQty); // remove product
            return true;
        } else {
            return cart.remove(productId)!=null;
        }

    }

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) throws Exception {
        // we add products to our cart
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            if (!addProduct(cartItem.getProductId(), cartItem.getProductQty())) { // this method adds products to our cart only if we have enough stock
                throw new Exception("Error occurred while adding products to cart");
            }
        }

        List<CartItem> distinctShoppingCart = cart.entrySet().stream().map(e -> new CartItem(e.getKey(), e.getValue())).toList();

        shoppingCart.setCartItems(distinctShoppingCart); // update the shopping cart
        shoppingCart = shoppingCartRepository.save(shoppingCart); // add to db to get its id

        // link each cartItem to a shopping cart
        for (CartItem cartItem : distinctShoppingCart) {
            cartItem.setShoppingCart(shoppingCart);
            cartItemRepository.save(cartItem);
        }

        return shoppingCart;
    }

    @Override
    public List<ShoppingCart> findAll() {
        return shoppingCartRepository.findAll();
    }

    @Override
    public void deleteById(Long id) throws ShoppingCartNotFoundException {
        if (shoppingCartRepository.findById(id).isEmpty()) {
            throw new ShoppingCartNotFoundException("Shopping cart of id: " + id + " not found, can't delete");
        }
        shoppingCartRepository.deleteById(id);
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) throws ShoppingCartNotFoundException {
        if (shoppingCartRepository.findById(shoppingCart.getId()).isEmpty()) {
            throw new ShoppingCartNotFoundException("Shopping cart of id: " + shoppingCart.getId() + " not found, can't update");
        }
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart findById(Long id) throws ShoppingCartNotFoundException {
        return shoppingCartRepository.findById(id).orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart of id: " + id + " not found, can't update"));
    }

    @Override
    public boolean existsById(Long id) {
        return shoppingCartRepository.existsById(id);
    }

    @Override
    public boolean validateCart(ShoppingCart shoppingCart) throws ShoppingCartNotFoundException, CartEmptyException, ProductNotFoundException {
        if (shoppingCart == null) {
            throw new ShoppingCartNotFoundException("Shopping cart not found!");
        }
        if (shoppingCart.getCartItems() == null) {
            throw new CartEmptyException("Cart is empty, no products available");
        }
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            if (!productService.existsById(cartItem.getProductId())) {
                throw new ProductNotFoundException("Product " + cartItem.getProductId() + " does not exist in the database");
            }
        }
        return true;
    }

}
