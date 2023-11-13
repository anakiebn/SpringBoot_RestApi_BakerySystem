package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

}
