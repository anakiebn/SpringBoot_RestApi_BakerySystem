package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {

}
