package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Ingredient;
import com.anakie.restApiBakery.entity.IngredientMinQty;
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
    @GetMapping("/id")
    public ResponseEntity<Ingredient> findById(@PathVariable Long id) throws IngredientNotFoundException {
        return new ResponseEntity<>(ingredientService.findById(id), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<Ingredient> > findAll(){
            return new ResponseEntity<>(ingredientService.findAll(), HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<Ingredient> save(@RequestBody Ingredient ingredient){
            return new ResponseEntity<>(ingredientService.save(ingredient), HttpStatus.OK);
    }
    @PostMapping("/update")
    public ResponseEntity<Ingredient> update(@RequestBody Ingredient ingredient){
        return new ResponseEntity<>(ingredientService.update(ingredient), HttpStatus.OK);
    }
    @DeleteMapping("/id")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws IngredientNotFoundException {
            ingredientService.deleteById(id);
            return new ResponseEntity<>("Ingredient id: "+id+" successfully removed", HttpStatus.GONE);
    }

    @PostMapping("/saveIngrMinQty")
    public ResponseEntity<List<IngredientMinQty>> save(@RequestBody List<IngredientMinQty> ingredientMinQty) {
        return new ResponseEntity<>(ingredientService.save(ingredientMinQty), HttpStatus.OK);
    }

}
