package com.wisely.e_commercecard.service.category;

import com.wisely.e_commercecard.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String categoryName);
    List<Category> getAllCategories( );
    Category addCategory(Category category);
    Category updateCategory(Category category , Long id);
    void deleteCategoryById(Long id);
}
