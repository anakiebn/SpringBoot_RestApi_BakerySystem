package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Ingredient;
import com.anakie.restApiBakery.exception.DuplicateIngredientException;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.OutOfStockException;
import com.anakie.restApiBakery.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ingredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;


    private static Map<Long, Double> stockDb;


    @Autowired
    public ingredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
        if (stockDb == null) {
         reloadStockFromDb();
        }
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        boolean duplicate= ingredientRepository.findAll().stream().anyMatch(ingr -> ingr.getName().equals(ingredient.getName()));
       if( ingredient.getId()!=null && ingredientRepository.existsById(ingredient.getId()) && duplicate){
           throw new DuplicateIngredientException("Duplicate ingredient name: "+ingredient.getName() +" not allowed!");
       }
        return ingredientRepository.save(ingredient);
    }

    @Override
    public void reloadStockFromDb(){
        stockDb = ingredientRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Ingredient::getId,
                        HashMap::new,
                        Collectors.summingDouble(Ingredient::getQuantity)
                ));
    }

    @Override
    public boolean existsById(Long id) {
        return ingredientRepository.existsById(id);
    }


    @Override
    public Ingredient findById(Long id) throws IngredientNotFoundException {
        return ingredientRepository.findById(id).orElseThrow(() -> new IngredientNotFoundException("Ingredient " + id + " not found, use an existing id!"));
    }

    @Override
    public void returnIngrToCopy(Long recipeIngrId, Double recipeIngrQty, int proQty) {

        if(stockDb.containsKey(recipeIngrId)){
            throw new IngredientNotFoundException("No such ingredient exists in stock db");
        }
        stockDb.replace(recipeIngrId,stockDb.get(recipeIngrId)+recipeIngrQty*proQty);
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
    public void confirmStockAvailability(Long recipeIngrId, double recipeIngrQty, int proQty) throws IngredientNotFoundException {

        if (!ingredientRepository.existsById(recipeIngrId)) {
            throw new IngredientNotFoundException("Ingredient id= " + recipeIngrId + " not found!!!");
        }

        if(recipeIngrQty*proQty>stockDb.get(recipeIngrId)){
            throw new OutOfStockException("Out of stock!!!");
        }
        stockDb.replace(recipeIngrId,stockDb.get(recipeIngrId)-recipeIngrQty*proQty); //reduce stock
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    // at payment time, we then use this method to reduce stock directly from db
    @Override
    public void useIngredient(Long recipeIngrId, Double recipeIngrQty, int proQty) throws IngredientNotFoundException, OutOfStockException {
        Ingredient ingredient=ingredientRepository.findById(recipeIngrId).orElseThrow(() -> new IngredientNotFoundException("Ingredient doesn't exists"));
        ingredient.setQuantity(ingredient.getQuantity()-recipeIngrQty*proQty);
        stockDb.replace(recipeIngrId,stockDb.get(recipeIngrId)-recipeIngrQty*proQty); // update the stock copy too
        update(ingredient); // update the db
    }

    @Override
    public void  saveIngrMinQty(Map<Long, Double> ingredientMinQuantities) {
        ingredientMinQuantities.forEach((key, value) -> {
            Ingredient ingr = ingredientRepository.findById(key).orElseThrow(() -> new IngredientNotFoundException("Ingredient doesn't exists"));
            ingr.setMin_quantity(value);
            update(ingr);
        });
    }

}
