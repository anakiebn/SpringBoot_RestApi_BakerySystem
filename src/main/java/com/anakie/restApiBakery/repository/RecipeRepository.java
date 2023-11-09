package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.Ingredient;
import com.anakie.restApiBakery.entity.Recipe;
import com.anakie.restApiBakery.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
public interface RecipeRepository extends JpaRepository<Recipe,Long> {

}
