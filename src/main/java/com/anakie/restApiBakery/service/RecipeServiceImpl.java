package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Recipe;
import com.anakie.restApiBakery.entity.RecipeIngredient;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.MissingIngredientException;
import com.anakie.restApiBakery.exception.RecipeNotFoundException;
import com.anakie.restApiBakery.repository.IngredientRepository;
import com.anakie.restApiBakery.repository.RecipeIngredientRepository;
import com.anakie.restApiBakery.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;


    @Override
    public Recipe addRecipe(Recipe recipe) throws MissingIngredientException, IngredientNotFoundException, SQLIntegrityConstraintViolationException {

        if(recipe.getRecipeIngredients()==null){
            throw new MissingIngredientException("Ingredients not found, Recipe must have ingredients!");
        }
        for (RecipeIngredient recipeIngr : recipe.getRecipeIngredients()) {
            if (!ingredientRepository.existsById(recipeIngr.getId())) {
                throw new IngredientNotFoundException("Ingredient not found, can't add recipe");
            }
        }
        if(recipeRepository.findAll().stream().anyMatch(rec->rec.getName().equalsIgnoreCase(recipe.getName()))){
            throw new SQLIntegrityConstraintViolationException("Recipe name "+recipe.getName()+" exists, duplicate names not allowed!!!");
        }
        recipeIngredientRepository.saveAll(recipe.getRecipeIngredients());
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe getRecipeById(Long id) throws RecipeNotFoundException {
        return recipeRepository.findById(id).orElseThrow(()-> new RecipeNotFoundException("Recipe id "+id+" not found!!!"));
    }

    @Override
    public void deleteRecipeById(Long id) throws RecipeNotFoundException {
        recipeRepository.delete(recipeRepository.findById(id).orElseThrow(()->new RecipeNotFoundException("Recipe id "+id+" not found!!!")));
    }

    @Override
    public Recipe updateRecipe(Recipe recipe) throws RecipeNotFoundException, IngredientNotFoundException, SQLIntegrityConstraintViolationException, MissingIngredientException {
        if(!recipeRepository.existsById(recipe.getId())){
            throw new RecipeNotFoundException("Recipe not found, can't update recipe");
        }
        return addRecipe(recipe);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe addRecipeIngredients(List<RecipeIngredient> recipeIngredients, Long recipeId) throws RecipeNotFoundException {
        return null;
    }

    @Override
    public boolean existsById(Long recipeId) {
        if(recipeId==null){
            return false;
        }
        return recipeRepository.existsById(recipeId);
    }
}
