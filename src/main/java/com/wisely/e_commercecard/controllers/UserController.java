package com.wisely.e_commercecard.controllers;

import com.wisely.e_commercecard.dto.UserDto;
import com.wisely.e_commercecard.exception.AlreadyExistsException;
import com.wisely.e_commercecard.exception.ResourceNotFoundException;
import com.wisely.e_commercecard.model.User;
import com.wisely.e_commercecard.requsets.CreateUserRequest;
import com.wisely.e_commercecard.requsets.UserUpdateRequest;
import com.wisely.e_commercecard.response.ApiResponse;
import com.wisely.e_commercecard.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse( e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest user){
        try {
            User newUser = userService.createUser(user);
            UserDto userDto = userService.convertUserToUserDto(newUser);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse( e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request,@PathVariable Long userId){
        try {
            User user = userService.updateUser(request,userId);
            UserDto userDto = userService.convertUserToUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse( e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("delete Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse( e.getMessage(), null));
        }
    }
}
