package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Recipe;
import com.anakie.restApiBakery.entity.RecipeDTO;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.exception.MissingIngredientException;
import com.anakie.restApiBakery.exception.RecipeNotFoundException;
import com.anakie.restApiBakery.service.RecipeService;
import lombok.RequiredArgsConstructor;
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



    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Recipe> findById(@PathVariable Long id) throws RecipeNotFoundException {
            return new ResponseEntity<>(recipeService.findById(id), HttpStatus.OK);

    }
    @GetMapping
    public ResponseEntity<List<Recipe>> findAll(){
            return new ResponseEntity<>(recipeService.findAll(), HttpStatus.OK);

    }
    @PostMapping("/save")
    public ResponseEntity<Recipe> save(@RequestBody RecipeDTO recipeDTO) throws IngredientNotFoundException, SQLIntegrityConstraintViolationException, MissingIngredientException {
            return new ResponseEntity<>(recipeService.save(recipeDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<Recipe> update(@RequestBody RecipeDTO recipeDTO) throws IngredientNotFoundException, SQLIntegrityConstraintViolationException, MissingIngredientException {
        return new ResponseEntity<>(recipeService.update(recipeDTO), HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws RecipeNotFoundException {

            recipeService.deleteRecipeById(id);
            return new ResponseEntity<>("Recipe id: "+id+" successfully removed", HttpStatus.GONE);

    }




}
