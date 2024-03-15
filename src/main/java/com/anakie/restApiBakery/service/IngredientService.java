package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Ingredient;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.OutOfStockException;

import java.util.List;
import java.util.Map;

public interface IngredientService {

    Ingredient save(Ingredient ingredient);
    void reloadStockFromDb();

    boolean existsById(Long id);

    Ingredient findById(Long id) throws IngredientNotFoundException;

    void returnIngrToCopy(Long recipeIngrId, Double recipeIngrQty, int proQty);
    void confirmStockAvailability(Long recipeIngrId, double recipeIngrQty, int proQty) throws IngredientNotFoundException;

    List<Ingredient> findAll();
    void deleteById(Long id) throws IngredientNotFoundException;

    Ingredient update(Ingredient ingredient) throws IngredientNotFoundException;

    // at payment time, we then use this method to reduce stock directly from db
    void useIngredient(Long recipeIngrId, Double recipeIngrQty, int proQty) throws IngredientNotFoundException, OutOfStockException;

    void saveIngrMinQty(Map<Long, Double> ingredientMinQuantities);
}
