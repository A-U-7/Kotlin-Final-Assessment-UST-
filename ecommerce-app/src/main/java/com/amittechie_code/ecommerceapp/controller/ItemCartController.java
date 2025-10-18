package com.amittechie_code.ecommerceapp.controller;

import com.amittechie_code.ecommerceapp.model.ItemCart;
import com.amittechie_code.ecommerceapp.service.ItemCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class ItemCartController {

    private final ItemCartService itemCartService;

    @Autowired
    public ItemCartController(ItemCartService itemCartService) {
        this.itemCartService = itemCartService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ItemCart>> getCartItems(@PathVariable Long userId) {
        List<ItemCart> cartItems = itemCartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/add")
    public ResponseEntity<ItemCart> addItemToCart(@RequestParam Long userId,
                                                 @RequestParam Long productId,
                                                 @RequestParam Integer quantity) {
        try {
            ItemCart cartItem = itemCartService.addItemToCart(userId, productId, quantity);
            return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<ItemCart> updateCartItemQuantity(@PathVariable Long cartItemId,
                                                          @RequestParam Integer quantity) {
        try {
            ItemCart updatedItem = itemCartService.updateCartItemQuantity(cartItemId, quantity);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/item/{userId}/{productId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long userId,
                                                  @PathVariable Long productId) {
        try {
            itemCartService.removeItemFromCart(userId, productId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        itemCartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total/{userId}")
    public ResponseEntity<BigDecimal> getCartTotal(@PathVariable Long userId) {
        BigDecimal total = itemCartService.calculateCartTotal(userId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> getCartItemCount(@PathVariable Long userId) {
        Integer count = itemCartService.getCartItemCount(userId);
        return ResponseEntity.ok(count);
    }
}
