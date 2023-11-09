package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.RecipeIngredient;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.RecipeIngredientNotFoundException;

import java.util.List;

public interface RecipeIngredientService {
    RecipeIngredient addRecipeIngredient(RecipeIngredient recipeIngredient) throws IngredientNotFoundException;
    RecipeIngredient getRecipeIngredientById(Long id) throws RecipeIngredientNotFoundException;

    int deleteRecipeIngredientById(Long id) throws RecipeIngredientNotFoundException;

    RecipeIngredient updateRecipeIngredient(RecipeIngredient recipeIngredients) throws RecipeIngredientNotFoundException;

    List<RecipeIngredient> getAllRecipeIngredients();
    boolean existsById(Long id);
}
