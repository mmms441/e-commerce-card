package com.wisely.e_commercecard.controllers;

import com.wisely.e_commercecard.dto.OrderDto;
import com.wisely.e_commercecard.exception.ResourceNotFoundException;
import com.wisely.e_commercecard.model.Order;
import com.wisely.e_commercecard.response.ApiResponse;
import com.wisely.e_commercecard.service.order.IOrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;


    @GetMapping("/{userId}/get-all")
    public ResponseEntity<ApiResponse> getOrders(@PathVariable Long userId) {
        try {
            List<OrderDto> order =orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Success", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse( e.getMessage(),null));
        }
    }
    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("order request Success", orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

        @GetMapping("{orderId}/order")
        public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
            try {
                OrderDto order = orderService.getOrder(orderId);
                return ResponseEntity.ok(new ApiResponse("found order success ", order));
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse( "Oops" ,e.getMessage()));
            }
        }



}