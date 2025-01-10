package com.wisely.e_commercecard.controllers;

import com.wisely.e_commercecard.exception.AlreadyExistsException;
import com.wisely.e_commercecard.exception.ResourceNotFoundException;
import com.wisely.e_commercecard.model.Category;
import com.wisely.e_commercecard.response.ApiResponse;
import com.wisely.e_commercecard.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;
/*
    private final IProductService productService;
*/
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> allCategories() {

        try {
            List<Category> allCategory = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("success", allCategory));
        } catch (Exception e) {
            return   ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            Category newCategory = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("success", newCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getCategoryById/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("success", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getCategoryByName/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("success", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/deleteCategoryById/{id}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/updateCategoryById/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category ,@PathVariable Long categoryId) {
        try {
            Category catchCategory = categoryService.updateCategory(category,categoryId);
                return ResponseEntity.ok(new ApiResponse("success", catchCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
