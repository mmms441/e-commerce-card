package com.wisely.e_commercecard.controllers;

import com.wisely.e_commercecard.exception.ResourceNotFoundException;
import com.wisely.e_commercecard.model.Cart;
import com.wisely.e_commercecard.model.User;
import com.wisely.e_commercecard.response.ApiResponse;
import com.wisely.e_commercecard.service.cart.ICartItemService;
import com.wisely.e_commercecard.service.cart.ICartService;
import com.wisely.e_commercecard.service.user.UserService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@AllArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItem")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final UserService userService;

    @PostMapping("item/add")
    public ResponseEntity<ApiResponse> addItem(
                                               @RequestParam Long productId,
                                               @RequestParam Integer quantity) {
        try {

            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(user);

            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return  ResponseEntity.ok(new ApiResponse("add item success" ,null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse( e.getMessage() ,null));
        }catch (JwtException e){
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse( e.getMessage() ,null));
        }
    }
    @DeleteMapping("cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItem (@PathVariable Long cartId,@PathVariable Long itemId, Integer quantity) {
        try {
            cartItemService.removeItemFromCart(cartId,itemId);
            return  ResponseEntity.ok(new ApiResponse("remove item success" ,null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse( e.getMessage() ,null));
        }
    }

    @PutMapping("cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateQuantity (@PathVariable Long cartId,
                                                       @PathVariable Long itemId,
                                                       @RequestParam Integer quantity) {

        try {
            cartItemService.updateItemQuantity(cartId,itemId,quantity);
            return  ResponseEntity.ok(new ApiResponse("update quantity success" ,null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse( e.getMessage() ,null));
        }
    }
}
