package com.wisely.e_commercecard.repository;

import com.wisely.e_commercecard.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {


    Cart findByUserId(Long userId);
}
