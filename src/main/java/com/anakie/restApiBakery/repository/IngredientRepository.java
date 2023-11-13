package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {



}
