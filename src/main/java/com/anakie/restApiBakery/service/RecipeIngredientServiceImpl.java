package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.RecipeIngredient;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.RecipeIngredientNotFoundException;
import com.anakie.restApiBakery.repository.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RecipeIngredientServiceImpl implements RecipeIngredientService {

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;
    @Autowired
    private IngredientService ingredientService;

    @Override
    public RecipeIngredient addRecipeIngredient(RecipeIngredient recipeIngredient) throws IngredientNotFoundException {
        if(!ingredientService.existsById(recipeIngredient.getId())){
            throw new IngredientNotFoundException("Added ingredient does not exist");
        }
        return recipeIngredientRepository.save(recipeIngredient);
    }

    @Override
    public RecipeIngredient getRecipeIngredientById(Long id) throws RecipeIngredientNotFoundException {
        return recipeIngredientRepository.findById(id).orElseThrow(
                ()->new RecipeIngredientNotFoundException("Recipe ingredient of id: "+id+" is not found"));
    }

    @Override
    public int deleteRecipeIngredientById(Long id) throws RecipeIngredientNotFoundException {
        return 0;
    }

    @Override
    public RecipeIngredient updateRecipeIngredient(RecipeIngredient recipeIngredients) throws RecipeIngredientNotFoundException {
        return null;
    }


    @Override
    public List<RecipeIngredient> getAllRecipeIngredients() {
        return recipeIngredientRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return recipeIngredientRepository.existsById(id);
    }
}
