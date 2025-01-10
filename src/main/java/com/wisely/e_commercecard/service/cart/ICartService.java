package com.wisely.e_commercecard.service.cart;

import com.wisely.e_commercecard.model.Cart;
import com.wisely.e_commercecard.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart  initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
