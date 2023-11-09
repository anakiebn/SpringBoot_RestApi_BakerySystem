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
    public ResponseEntity<Category> findById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
        }catch (Exception ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public ResponseEntity<List<Category>> findAll(){
        try {
            return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
        }
        catch (Exception ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity<Category> save(@RequestBody Category category){
        try {
            return new ResponseEntity<>(categoryService.save(category), HttpStatus.OK);
        } catch (Exception ex){
            log.error("Exception occurred: "+ex.getCause());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        try {
            categoryService.deleteById(id);
            return new ResponseEntity<>("Category id: "+id+" successfully removed", HttpStatus.GONE);
        }catch (Exception ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
