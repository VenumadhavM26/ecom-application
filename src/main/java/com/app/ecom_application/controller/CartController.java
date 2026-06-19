package com.app.ecom_application.controller;

import com.app.ecom_application.dto.CartItemRequest;
import com.app.ecom_application.model.CartItem;
import com.app.ecom_application.repository.ProductRepository;
import com.app.ecom_application.service.CartService;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<String> createCart(@RequestHeader("X-User-ID") String userId,
                                           @RequestBody CartItemRequest cartItemRequest){
        if(!cartService.createCart(userId, cartItemRequest)){
            return ResponseEntity.badRequest().body("Product Out of stock or User not found or product not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID")  String userId,
                                               @PathVariable Long productId){
        boolean deleted = cartService.deleteItemFromCart(userId, productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(cartService.getItemsFromCart(userId));
    }

}
