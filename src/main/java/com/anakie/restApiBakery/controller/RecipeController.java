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
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) throws RecipeNotFoundException {
            return new ResponseEntity<>(recipeService.getRecipeById(id), HttpStatus.OK);

    }
    @GetMapping
    public ResponseEntity<List<Recipe>> findAll(){
            return new ResponseEntity<>(recipeService.getAllRecipes(), HttpStatus.OK);

    }
    @PostMapping
    public ResponseEntity<Recipe> save(@RequestBody Recipe recipe) throws IngredientNotFoundException, SQLIntegrityConstraintViolationException, MissingIngredientException {
            return new ResponseEntity<>(recipeService.addRecipe(recipe), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws RecipeNotFoundException {

            recipeService.deleteRecipeById(id);
            return new ResponseEntity<>("Recipe id: "+id+" successfully removed", HttpStatus.GONE);

    }




}
