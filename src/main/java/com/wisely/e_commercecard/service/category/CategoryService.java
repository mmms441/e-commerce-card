package com.wisely.e_commercecard.service.category;

import com.wisely.e_commercecard.exception.ResourceNotFoundException;
import com.wisely.e_commercecard.exception.AlreadyExistsException;
import com.wisely.e_commercecard.model.Category;
import com.wisely.e_commercecard.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("category not found"));
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

    @Override
    public List<Category> getAllCategories( ) {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(()->new AlreadyExistsException(category.getName()+"already exist"));
    }

    @Override
    public Category updateCategory(Category category , Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory->{
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(()->new ResourceNotFoundException("category not found"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete ,()->{
                    throw new ResourceNotFoundException("category not found");
                });

    }
}
