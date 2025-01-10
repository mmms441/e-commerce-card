package com.wisely.e_commercecard.controllers;

import com.wisely.e_commercecard.dto.ProductDto;
import com.wisely.e_commercecard.exception.AlreadyExistsException;
import com.wisely.e_commercecard.exception.ResourceNotFoundException;
import com.wisely.e_commercecard.model.Product;
import com.wisely.e_commercecard.requsets.AddProductRequest;
import com.wisely.e_commercecard.requsets.ProductUpdateRequest;
import com.wisely.e_commercecard.response.ApiResponse;
import com.wisely.e_commercecard.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> products=productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("found" ,convertedProducts));
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        try {
            Product product=productService.getProductById(id);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("found" ,productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() ,null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
        try {
            Product theproduct =productService.addProduct(product);
             return ResponseEntity.ok(new ApiResponse("added" ,theproduct));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage() ,null));
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest product ,@PathVariable Long productId){
        try {
            productService.updateProduct(product,productId);
            return ResponseEntity.ok(new ApiResponse("updated" ,product));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() ,null));
        }
    }

    @DeleteMapping("/deleteById/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId){
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("deleted" ,productId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() ,null));
        }
    }

    @GetMapping("getProductByBrandAndName")
    public ResponseEntity<ApiResponse> getProductByBrandAndName (@RequestParam String brand,@RequestParam String name){
        try {
            List<Product> product =productService.getProductsByBrandAndName(brand,name);
        if(product.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("not found" ,null));
        }
        List<ProductDto> convertedProducts = productService.getConvertedProducts(product);

            return ResponseEntity.ok(new ApiResponse("found" ,convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() ,null));        }
    }

    @GetMapping("getProductByCategoryAndBrand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand (@RequestParam String category,@RequestParam String brand){
        try {
            List<Product> product =productService.getProductsByCategoryAndBrand(category,brand);
            if(product.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("not found" ,null));
            }
            return ResponseEntity.ok(new ApiResponse("found" ,product));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() ,null));        }
    }

    @GetMapping("/getProductByName")
    public ResponseEntity<ApiResponse> getProductByName(@RequestParam String name){
        try {
            List<Product> product = productService.getProductsByName(name);
            if(product.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("not found" ,null));
            }
            return ResponseEntity.ok(new ApiResponse("found" ,product));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() ,null));
        }
    }
    @GetMapping("/getProductByBrand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand){
        try {
            List<Product> product = productService.getProductsByBrand(brand);
            if(product.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("not found" ,null));
            }
            return ResponseEntity.ok(new ApiResponse("found" ,product));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() ,null));
        }
    }
    @GetMapping("/getProductByCategory")
    public ResponseEntity<ApiResponse> getProductByCategory(@RequestParam String category){
        try {
            List<Product> product = productService.getProductsByCategory(category);
            if(product.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("not found" ,null));
            }
            return ResponseEntity.ok(new ApiResponse("found" ,product));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() ,null));
        }
    }

    @GetMapping("/productCount")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand,@RequestParam String name){
        try {
            var productCount = productService.countProductsBtBrandAndName(brand,name);
            return ResponseEntity.ok(new ApiResponse("found" ,productCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage() ,null));
        }
    }
}
