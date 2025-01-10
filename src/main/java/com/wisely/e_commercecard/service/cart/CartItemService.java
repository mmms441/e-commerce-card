package com.wisely.e_commercecard.service.cart;

import com.wisely.e_commercecard.exception.ResourceNotFoundException;
import com.wisely.e_commercecard.model.Cart;
import com.wisely.e_commercecard.model.CartItem;
import com.wisely.e_commercecard.model.Product;
import com.wisely.e_commercecard.repository.CartItemRepository;
import com.wisely.e_commercecard.repository.CartRepository;
import com.wisely.e_commercecard.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;


    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item->item.getProduct()
                        .getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if(cartItem.getId()== null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }else{
            cartItem.setQuantity(cartItem.getQuantity() +quantity);

        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartToRemove = getCartItem(cartId, productId);
                cart.removeItem(cartToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long itemId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream().filter(item->item.getProduct().getId().equals(itemId))
                .findFirst().ifPresent(item->{
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getItems().stream().map(
                 CartItem ::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems().stream().filter(
                item->item.getProduct().getId().equals(productId)
        ).findFirst().orElseThrow(()->new ResourceNotFoundException("item not found"));
    }
}
