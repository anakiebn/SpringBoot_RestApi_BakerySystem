package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.CartItem;
import com.anakie.restApiBakery.entity.Product;
import com.anakie.restApiBakery.entity.RecipeIngredient;
import com.anakie.restApiBakery.entity.ShoppingCart;
import com.anakie.restApiBakery.exception.*;
import com.anakie.restApiBakery.repository.CartItemRepository;
import com.anakie.restApiBakery.repository.ShoppingCartRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    // this cart simply holds what we have at the moment
    private final Map<Long, Integer> cart = new HashMap<>();

    @Autowired
    private ProductService productService;
    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    public boolean addProduct(Long productId, int productQty) throws OutOfStockException, ProductNotFoundException {

        Product product = productService.findById(productId);
        boolean available = true;
        int qty = productQty; //get product quantity

        if (cart.containsKey(productId)) {
            qty = cart.get(productId) + productQty; // if it exists already we add the quantity
        }

        for (RecipeIngredient ingredient : product.getRecipe().getRecipeIngredients()) {

            if (!ingredientService.existsById(ingredient.getId())) {
                log.info("On addProduct() method an exception was thrown");
                throw new IngredientNotFoundException("Ingredient not found, provide valid ingredients");
            }
            if (!ingredientService.stockAvailable(ingredient, qty)) {
                throw new OutOfStockException("Can't add product to cart, we don't have enough ingredients");
            }
        }
        // this code is only reached when no outOfStockException/Ingredient was thrown
        if (cart.containsKey(productId)) {
            return cart.replace(productId, cart.get(productId), qty);
        } else {
            cart.put(productId, qty);
            return cart.containsKey(productId);
        }

    }


    @Override
    public boolean removeProduct(Long productId, int productQty) throws ProductNotFoundException, InvalidProductQuantity {

        if (productService.existsById(productId)) {
            throw new ProductNotFoundException("Product id " + productId + "not found");
        }
        int currentProductQty = cart.get(productId); // get its quantity from the cart

        if (cart.containsKey(productId)) { //check if we have it in the cart
            if (currentProductQty >= productQty) { // if the quantity is valid, we don't want them subtracting 5 items when we only have 2 of that kind
                // suppose quantities are equal, we remove the product from the cart e.g. if you reduce 2 products from a cart where you have only 2 of that kind,
                // that means that you won't be left with any kind of that product cause you took out all of them.
                if (currentProductQty == productQty) {
                    cart.remove(productId);
                    return !cart.containsKey(productId);
                } else {
                    // or we can just reduce the quantity instead
                    return cart.replace(productId, currentProductQty, currentProductQty - productQty);
                }
            } else {
                //if the quantity is bigger than the available one, we throw an invalid quantity exception
                throw new InvalidProductQuantity("Provided quantity of product " + productId + " is larger than the available quantity on the cart");
            }
        }
        throw new ProductNotFoundException("Product not present in shopping cart");
    }

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) throws Exception {
        // we add products to our cart
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            if (!addProduct(cartItem.getProductId(), cartItem.getProductQty())) { // this method adds products to our cart only if we have enough stock
                throw new Exception("Error occurred while adding products to cart");
            }
        }

        List<CartItem> distinctShoppingCart = new ArrayList<>();
        for (Long productId : cart.keySet()) {  // converting our map cart to arraylist
            distinctShoppingCart.add(new CartItem(productId, cart.get(productId)));
        }
        shoppingCart.setCartItems(distinctShoppingCart); // update the shopping cart
        shoppingCart=shoppingCartRepository.save(shoppingCart); // add to db to get its id

        // link each cartItem to a shopping cart
        for(CartItem cartItem:distinctShoppingCart){
            cartItem.setShoppingCart(shoppingCart);
            cartItemRepository.save(cartItem);
        }

        return shoppingCart;
    }

    @Override
    public List<ShoppingCart> findAll() {
        List<ShoppingCart> shoppingCart = new ArrayList<>();
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
    public boolean cartValid(ShoppingCart shoppingCart) throws ShoppingCartNotFoundException, CartEmptyException, ProductNotFoundException {
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
