package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Ingredient;
import com.anakie.restApiBakery.exception.IngredientNotFoundException;
import com.anakie.restApiBakery.service.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/v1/ingredients")
public class IngredientController {


    @Autowired
    private IngredientService ingredientService;
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> findById(@PathVariable Long id){
        try {
             return new ResponseEntity<>(ingredientService.findById(id), HttpStatus.OK);
        }catch (IngredientNotFoundException ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Ingredient> > findAll(){
        try {
            return new ResponseEntity<>(ingredientService.findAll(), HttpStatus.OK);
        }catch (Exception ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity<Ingredient> save(@RequestBody Ingredient ingredient){
        try {
            return new ResponseEntity<>(ingredientService.save(ingredient), HttpStatus.OK);
        }catch (Exception ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        try {
            ingredientService.deleteById(id);
            return new ResponseEntity<>("Ingredient id: "+id+" successfully removed", HttpStatus.GONE);
        }catch (Exception ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
