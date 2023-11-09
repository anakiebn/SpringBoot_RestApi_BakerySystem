package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Ingredient;
import com.anakie.restApiBakery.entity.RecipeIngredient;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.OutOfStockException;

import java.util.List;

public interface IngredientService {

    boolean existsById(Long id);
    Ingredient save(Ingredient ingredient);
    Ingredient findById(Long id) throws IngredientNotFoundException;
    List<Ingredient> findAll();
    void deleteById(Long id) throws IngredientNotFoundException;

    Ingredient update(Ingredient ingredient) throws IngredientNotFoundException;

    boolean stockAvailable(RecipeIngredient recipeIngredient, int quantity) throws IngredientNotFoundException;

    void useIngredient(RecipeIngredient recipeIngr, int proQty) throws IngredientNotFoundException, OutOfStockException;
}
