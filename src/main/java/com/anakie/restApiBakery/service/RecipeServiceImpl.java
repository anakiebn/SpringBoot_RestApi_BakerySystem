package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Recipe;
import com.anakie.restApiBakery.entity.RecipeDTO;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.MissingIngredientException;
import com.anakie.restApiBakery.exception.RecipeNotFoundException;
import com.anakie.restApiBakery.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private final RecipeRepository recipeRepository;
    @Autowired
    private final IngredientService ingredientService;

    @Override
    public Recipe save(RecipeDTO recipeDTO) throws MissingIngredientException, IngredientNotFoundException, SQLIntegrityConstraintViolationException {

        Recipe recipe = recipeDTO.toRecipe(ingredientService);

        if (recipe.getRecipeIngredients() == null) {
            throw new MissingIngredientException("Ingredients not found, Recipe must have ingredients!");
        }

        if (recipeRepository.findAll().stream().anyMatch(rec -> rec.getName().equalsIgnoreCase(recipe.getName()))) {
            throw new SQLIntegrityConstraintViolationException("Recipe name " + recipe.getName() + " exists, duplicate names not allowed!!!");
        }

        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe findById(Long id) throws RecipeNotFoundException {
        return recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe id " + id + " not found!!!"));
    }

    @Override
    public void deleteRecipeById(Long id) throws RecipeNotFoundException {
        recipeRepository.delete(recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe id " + id + " not found!!!")));
    }

    @Override
    public Recipe update(RecipeDTO recipeDTO) throws RecipeNotFoundException, IngredientNotFoundException, SQLIntegrityConstraintViolationException, MissingIngredientException {
        return null;
    }

    @Override
    public Recipe update(RecipeDTO recipeDTO, Long recipeId) throws RecipeNotFoundException, IngredientNotFoundException, SQLIntegrityConstraintViolationException, MissingIngredientException {
        if (!recipeRepository.existsById(recipeId)) {
            throw new RecipeNotFoundException("Recipe not found, can't update recipe");
        }
        Recipe recipe = recipeDTO.toRecipe(ingredientService);
        recipe.setId(recipeId);
        return save(recipeDTO);
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe addRecipeIngredients(Map<Long, Double> recipeIngredient, Long recipeId) throws RecipeNotFoundException {
        return null;
    }

    @Override
    public boolean existsById(Long recipeId) {
        return recipeRepository.existsById(recipeId);
    }
}
