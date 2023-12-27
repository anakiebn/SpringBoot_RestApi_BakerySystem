package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Recipe;
import com.anakie.restApiBakery.entity.RecipeIngredient;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.MissingIngredientException;
import com.anakie.restApiBakery.exception.RecipeNotFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface RecipeService {
    Recipe save(Recipe recipe) throws MissingIngredientException, IngredientNotFoundException, SQLIntegrityConstraintViolationException;
    Recipe findById(Long id) throws RecipeNotFoundException;
    void deleteRecipeById(Long id) throws RecipeNotFoundException;
    Recipe update(Recipe recipe) throws RecipeNotFoundException, IngredientNotFoundException, SQLIntegrityConstraintViolationException, MissingIngredientException;
    List<Recipe> findAll();
    public Recipe addRecipeIngredients(List<RecipeIngredient> recipeIngredients, Long recipeId) throws RecipeNotFoundException;

    boolean existsById(Long recipeId);
}
