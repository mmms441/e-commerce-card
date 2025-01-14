package com.wisely.e_commercecard.controllers;

import com.wisely.e_commercecard.requsets.JwtResponse;
import com.wisely.e_commercecard.requsets.LoginRequest;
import com.wisely.e_commercecard.response.ApiResponse;
import com.wisely.e_commercecard.security.jwt.JwtUtils;
import com.wisely.e_commercecard.security.user.ShopUserDetails;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@AllArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
private final AuthenticationManager authenticationManager;
private final JwtUtils jwtUtils;

@PostMapping("/login")
public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
    try {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateTokenForUser(authentication);
        ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
        JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
        return ResponseEntity.ok(new ApiResponse("login success", jwtResponse));
    } catch (AuthenticationException e) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse( e.getMessage(),null)) ;
    }
}

}
