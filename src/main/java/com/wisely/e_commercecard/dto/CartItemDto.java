package com.wisely.e_commercecard.dto;

import com.wisely.e_commercecard.model.Product;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CartItemDto {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
