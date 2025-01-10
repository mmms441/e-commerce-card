package com.wisely.e_commercecard.repository;

import com.wisely.e_commercecard.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);



    boolean existsByName(String name);


}
