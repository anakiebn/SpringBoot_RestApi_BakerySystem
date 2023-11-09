package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Ingredient;
import com.anakie.restApiBakery.entity.Recipe;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.MissingIngredientException;
import com.anakie.restApiBakery.exception.RecipeNotFoundException;
import com.anakie.restApiBakery.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/recipes")
public class RecipeController {


    @Autowired
    private RecipeService recipeService;
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(recipeService.getRecipeById(id), HttpStatus.OK);
        }catch (RecipeNotFoundException ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @GetMapping("/recipeIngredients/{id}")
//    public ResponseEntity<List<Ingredient>> getRecipeIngredients(@PathVariable Long id){
//        try {
//            return new ResponseEntity<>(recipeService.getRecipeIngredients(id), HttpStatus.OK);
//        }catch (RecipeNotFoundException ex){
//            log.error("Exception occurred: "+ex.getMessage());
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }catch (Exception ex){
//            log.error("Exception occurred: "+ex.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipe(){
        try {
            return new ResponseEntity<>(recipeService.getAllRecipes(), HttpStatus.OK);
        }catch (Exception ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe){
        try {
            return new ResponseEntity<>(recipeService.addRecipe(recipe), HttpStatus.OK);
        }catch(MissingIngredientException | SQLIntegrityConstraintViolationException | IngredientNotFoundException ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
//    @PostMapping("/id")
//    public ResponseEntity<Recipe> addRecipeIngredients(@RequestBody List<Ingredient> ingredients,Long recipeId){
//        try {
//            return new ResponseEntity<>(recipeService.addRecipeIngredients(ingredients,recipeId), HttpStatus.OK);
//      }//catch(MissingIngredientException ex){
////            log.error("Exception occurred: "+ex.getMessage());
////            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
////        }
//        catch (Exception ex){
//            log.error("Exception occurred: "+ex.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id){
        try {
            recipeService.deleteRecipeById(id);
            return new ResponseEntity<>("Recipe id: "+id+" successfully removed", HttpStatus.GONE);
        } catch (RecipeNotFoundException e) {
            log.error("Exception occurred: "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




}
