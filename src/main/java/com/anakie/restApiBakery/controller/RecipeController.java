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
    @GetMapping
    public ResponseEntity<List<Recipe>> findAll(){
        try {
            return new ResponseEntity<>(recipeService.getAllRecipes(), HttpStatus.OK);
        }catch (Exception ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity<Recipe> save(@RequestBody Recipe recipe){
        try {
            return new ResponseEntity<>(recipeService.addRecipe(recipe), HttpStatus.OK);
        }catch(MissingIngredientException | SQLIntegrityConstraintViolationException | IngredientNotFoundException ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        try {
            recipeService.deleteRecipeById(id);
            return new ResponseEntity<>("Recipe id: "+id+" successfully removed", HttpStatus.GONE);
        } catch (RecipeNotFoundException e) {
            log.error("Exception occurred: "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




}
