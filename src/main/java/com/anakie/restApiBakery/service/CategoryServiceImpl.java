package com.anakie.restApiBakery.service;

import ch.qos.logback.core.spi.ErrorCodes;
import com.anakie.restApiBakery.entity.Category;
import com.anakie.restApiBakery.exception.CategoryNotFoundException;
import com.anakie.restApiBakery.repository.CategoryRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) throws ConstraintViolationException {
        if(category==null){
            throw new NullPointerException("Null category object not allowed, fix it");
        }
        if (category.getName() == null) {
            String errorMessage = "Null category name not allowed, please provide a valid name.";
            throw new ConstraintViolationException(errorMessage, new SQLException("Null category name"), "CATEGORY_NAME_NOT_NULL");
        }

        if(categoryRepository.findAll().stream().anyMatch(cat->cat.getName().equalsIgnoreCase(category.getName()))){
            String errorMessage = "Duplicate category names not allowed, provide unique name";
            throw new ConstraintViolationException(errorMessage, new SQLException("Duplicate category name"), "UNIQUE_CATEGORY_NAME");
        }
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> save(List<Category> categories) throws ConstraintViolationException {
        return null;
    }

    @Override
    public Category findById(Long id) throws CategoryNotFoundException {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category " + id + " not found! Use existing id"));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteById(Long id) throws CategoryNotFoundException {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Can't delete, category " + id + " not found! Use existing id");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Category update(Category category) throws CategoryNotFoundException {
        if (!categoryRepository.existsById(category.getId())) {
            throw new CategoryNotFoundException("Can't delete, category " + category.getId() + " not found! Use existing id");
        }
        return categoryRepository.save(category);
    }

    @Override
    public boolean existsById(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }

}
