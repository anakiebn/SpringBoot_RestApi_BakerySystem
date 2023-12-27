package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Ingredient;
import com.anakie.restApiBakery.entity.IngredientMinQty;
import com.anakie.restApiBakery.entity.RecipeIngredient;
import com.anakie.restApiBakery.exception.DuplicateIngredientException;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.OutOfStockException;
import com.anakie.restApiBakery.repository.IngredientMinQtyRepository;
import com.anakie.restApiBakery.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ingredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private IngredientMinQtyRepository ingredientMinQtyRepository;

    @Override
    public boolean existsById(Long id) {
        return ingredientRepository.existsById(id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        if( ingredient==null){
            throw new NullPointerException("Null ingredient not allowed, provide none-null object");
        }
        if( ingredientRepository.findAll().stream().anyMatch(ingr->ingr.getName().equalsIgnoreCase(ingredient.getName()))){
            throw new DuplicateIngredientException("Duplicate ingredient name not allowed, use a distinct name");
        }
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
        ingredientRepository.deleteById(id);
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

    // checks if we have stock in our kitchen
    @Override
    public boolean stockAvailable(RecipeIngredient recipeIngr, int proQty) throws IngredientNotFoundException {
        Ingredient ingr = ingredientRepository.findById(recipeIngr.getId()).orElseThrow(() -> new IngredientNotFoundException("Ingredient doesn't exists"));
        return recipeIngr.getQuantity() * proQty <= ingr.getQuantity();
    }

    // it reduces the ingredient quantity since we're using the ingredient
    @Override
    public void useIngredient(RecipeIngredient recipeIngr, int proQty) throws IngredientNotFoundException, OutOfStockException {
        Ingredient ingredient = ingredientRepository.findById(recipeIngr.getId()).orElseThrow(() -> new IngredientNotFoundException("Ingredient "));
        double difference = ingredient.getQuantity() - recipeIngr.getQuantity() * proQty;
        if (difference < 0) {
            throw new OutOfStockException("Stock not enough for the order");
        }
        ingredient.setQuantity(difference);
        update(ingredient);
    }

    @Override
    public List<IngredientMinQty> save(List<IngredientMinQty> ingredientMinQuantities){

        // checks if we already have the ingredients in the db
        ingredientMinQuantities.forEach(ingrMinQty-> {
            if (ingredientMinQtyRepository.existsById(ingrMinQty.getIngredientId())) {
                throw new DuplicateIngredientException("Duplicate ingredient '" + ingrMinQty.getIngredientId() + "' not allowed, update instead");
            }
        });
        // if we're here then means we can add to db without problems
        return ingredientMinQtyRepository.saveAll(ingredientMinQuantities);
    }

}
