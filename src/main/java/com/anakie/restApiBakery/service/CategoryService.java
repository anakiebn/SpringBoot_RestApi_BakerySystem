package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Category;
import com.anakie.restApiBakery.exception.CategoryNotFoundException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public interface CategoryService {
    Category save(Category category) throws ConstraintViolationException;
    List<Category> save(List<Category> categories)throws ConstraintViolationException;
    Category findById(Long id) throws CategoryNotFoundException;
    List<Category> findAll();
    void deleteById(Long id) throws CategoryNotFoundException;

    Category update(Category category) throws CategoryNotFoundException;
    boolean existsById(Long categoryId);
}
