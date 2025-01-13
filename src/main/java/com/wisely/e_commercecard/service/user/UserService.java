package com.wisely.e_commercecard.service.user;

import com.wisely.e_commercecard.dto.UserDto;
import com.wisely.e_commercecard.exception.AlreadyExistsException;
import com.wisely.e_commercecard.exception.ResourceNotFoundException;
import com.wisely.e_commercecard.model.User;
import com.wisely.e_commercecard.repository.UserRepository;
import com.wisely.e_commercecard.requsets.CreateUserRequest;
import com.wisely.e_commercecard.requsets.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).
                orElseThrow(()->new ResourceNotFoundException("user not found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(
                req->{
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    return userRepository.save(user);
                }
        ).orElseThrow(()->new AlreadyExistsException("Oops" + request.getEmail() + "user already exist!"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser->{
                existingUser.setFirstName(request.getFirstName());
                existingUser.setLastName(request.getLastName());
                return userRepository.save(existingUser);
        } ).orElseThrow(()->new ResourceNotFoundException("user not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, ()-> {
                    throw new  ResourceNotFoundException("user not found!");
                });
        };

    @Override
    public UserDto convertUserToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

}

