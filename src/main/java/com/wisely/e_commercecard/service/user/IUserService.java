package com.wisely.e_commercecard.service.user;

import com.wisely.e_commercecard.dto.UserDto;
import com.wisely.e_commercecard.model.User;
import com.wisely.e_commercecard.requsets.CreateUserRequest;
import com.wisely.e_commercecard.requsets.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request , Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToUserDto(User user);
}
