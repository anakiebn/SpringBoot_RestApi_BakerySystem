package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Recipe;
import com.anakie.restApiBakery.entity.RecipeDTO;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.MissingIngredientException;
import com.anakie.restApiBakery.exception.RecipeNotFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;

public interface RecipeService {
    Recipe save(RecipeDTO recipeDTO) throws MissingIngredientException, IngredientNotFoundException, SQLIntegrityConstraintViolationException;
    Recipe findById(Long id) throws RecipeNotFoundException;
    void deleteRecipeById(Long id) throws RecipeNotFoundException;
    Recipe update(RecipeDTO recipeDTO) throws RecipeNotFoundException, IngredientNotFoundException, SQLIntegrityConstraintViolationException, MissingIngredientException;

    Recipe update(RecipeDTO recipeDTO, Long id) throws RecipeNotFoundException, IngredientNotFoundException, SQLIntegrityConstraintViolationException, MissingIngredientException;

    List<Recipe> findAll();
    Recipe addRecipeIngredients(Map<Long, Double> recipeIngredient, Long recipeId) throws RecipeNotFoundException;

    boolean existsById(Long recipeId);
}
