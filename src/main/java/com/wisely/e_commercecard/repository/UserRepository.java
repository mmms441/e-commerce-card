package com.wisely.e_commercecard.repository;

import com.wisely.e_commercecard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
