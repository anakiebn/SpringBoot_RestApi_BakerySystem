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
    public List<Ingredient> save(List<Ingredient> ingredients) {
        return null;
    }

    /**
     * Copies the stock ingredients from the database to ensure that each user has their own copy to operate on.
     *
     * This method ensures that we start reducing the stock for real only when an order is being paid for, not when the user places the order.
     * We do this to ensure that we only start baking when the user has paid for their order.
     *
     * We perform all the reducing and adding of ingredients on this temporary stock database when the user places an order.
     * When the user has paid for their order, we update the changes in the real database accordingly.
     */
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

    /**
     * Used to return ingredients back into our stock ingredient which acts as a copy of our real stock db, since we've now removed a product from our cart meaning
     * we never used the ingredients, so we return them to stock. Remember that we remove them for real at payment time not
     * order time
     *
     * @param recipeIngrId Ingredient you want to add
     * @param recipeIngrQty The quantity of the ingredient
     * @param proQty The number of products you are dealing with
     */
    @Override
    public void returnIngrToStockDb(Long recipeIngrId, Double recipeIngrQty, int proQty) {

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


    /**
     * This method is used to confirm if we have enough ingredients to bake the ordered amount of product/s
     *
     * @param recipeIngrId The recipe's id you want to check if it's available or not
     * @param recipeIngrQty The recipes' ingredient's quantity
     * @param proQty The number of products you have
     * @throws IngredientNotFoundException If you pass an id of an ingredient not known to our system, an exception will be thrown
     */
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

    /**
     *
     * Used to save all the minimum quantities to have in each ingredient, if this quantity is reached your system will flag
     * that you are running low on stock, pass this value, you should really order or add ingredient.
     * @param ingredientMinQuantities This map hold ingredients with minimum id, ingredients id used as key and double as the minimum quantity
     */
    @Override
    public void  saveIngrMinQty(Map<Long, Double> ingredientMinQuantities) {
        ingredientMinQuantities.forEach((key, value) -> {
            Ingredient ingr = ingredientRepository.findById(key).orElseThrow(() -> new IngredientNotFoundException("Ingredient doesn't exists"));
            ingr.setMin_quantity(value);
            update(ingr);
        });
    }

}
