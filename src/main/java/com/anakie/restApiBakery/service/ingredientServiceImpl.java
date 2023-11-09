package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Ingredient;
import com.anakie.restApiBakery.entity.RecipeIngredient;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.OutOfStockException;
import com.anakie.restApiBakery.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ingredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public boolean existsById(Long id) {
        return ingredientRepository.existsById(id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public Ingredient findById(Long id) throws IngredientNotFoundException {
        return ingredientRepository.findById(id).orElseThrow(() -> new IngredientNotFoundException("Ingredient " + id + " not found, use an existing id!"));
    }

    @Override
    public void deleteById(Long id) throws IngredientNotFoundException {
        if (!ingredientRepository.existsById(id)) {
            throw new IngredientNotFoundException("Can't delete ingredient " + id + " not found, use an existing id!");
        }
        ingredientRepository.deleteIngredientById(id);
    }


    @Override
    public Ingredient update(Ingredient ingredient) throws IngredientNotFoundException {
        if (!ingredientRepository.existsById(ingredient.getId())) {
            throw new IngredientNotFoundException("Can't update ingredient " + ingredient.getId() + " not found, use an existing id!");
        }
        return ingredientRepository.save(ingredient);
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }
    @Override
    public boolean stockAvailable(RecipeIngredient recipeIngr, int proQty) throws IngredientNotFoundException {
        Ingredient ingr = ingredientRepository.findById(recipeIngr.getId()).orElseThrow(() -> new IngredientNotFoundException("Ingredient doesn't exists"));
        return recipeIngr.getQuantity() * proQty <= ingr.getQuantity();
    }

    @Override
    public void useIngredient(RecipeIngredient recipeIngr, int proQty) throws IngredientNotFoundException, OutOfStockException {
        Ingredient ingredient = ingredientRepository.findById(recipeIngr.getId()).orElseThrow(() -> new IngredientNotFoundException("Ingredient "));
        double difference = ingredient.getQuantity() - recipeIngr.getQuantity() * proQty;
        ingredient.setQuantity(difference);

        if (difference < 0) {
            throw new OutOfStockException("Stock not enough for the order");
        }
        update(ingredient);
    }


}
