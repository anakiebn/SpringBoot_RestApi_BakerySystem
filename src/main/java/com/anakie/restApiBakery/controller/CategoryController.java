package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Category;
import com.anakie.restApiBakery.exception.CategoryNotFoundException;
import com.anakie.restApiBakery.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) throws CategoryNotFoundException {
            return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Category>> findAll(){

            return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Category> save(@RequestBody Category category){

            return new ResponseEntity<>(categoryService.save(category), HttpStatus.OK);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws CategoryNotFoundException {
        categoryService.deleteById(id);
            return new ResponseEntity<>("Category id: "+id+" successfully removed", HttpStatus.GONE);
    }
}
