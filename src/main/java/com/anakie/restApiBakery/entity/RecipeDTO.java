package com.anakie.restApiBakery.entity;

import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.service.IngredientService;
import lombok.Data;

import javax.naming.ldap.HasControls;
import java.util.HashMap;
import java.util.Map;

@Data
public class RecipeDTO {

    private String name;

    private Map<Long,Double> ingredients;


    public Recipe toRecipe(IngredientService ingredientService){
        Map<Ingredient,Double> recipeIngredients=new HashMap<>();

        for(Long ingrId:ingredients.keySet()){
           recipeIngredients.put(ingredientService.findById(ingrId),ingredients.get(ingrId));
        }
        return new Recipe(name,recipeIngredients);
    }
}
