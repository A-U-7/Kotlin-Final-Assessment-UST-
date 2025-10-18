package com.amittechie_code.ecommerceapp.service;

import com.amittechie_code.ecommerceapp.model.ItemCart;
import com.amittechie_code.ecommerceapp.model.Product;
import com.amittechie_code.ecommerceapp.model.User;
import com.amittechie_code.ecommerceapp.repository.ItemCartRepository;
import com.amittechie_code.ecommerceapp.repository.ProductRepository;
import com.amittechie_code.ecommerceapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemCartService {

    private final ItemCartRepository itemCartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemCartService(ItemCartRepository itemCartRepository,
                          ProductRepository productRepository,
                          UserRepository userRepository) {
        this.itemCartRepository = itemCartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<ItemCart> getCartItemsByUserId(Long userId) {
        return itemCartRepository.findByUserId(userId);
    }

    public ItemCart addItemToCart(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        // Check if item already exists in cart
        Optional<ItemCart> existingItem = itemCartRepository.findByUserAndProductId(user, productId)
                .stream().findFirst();

        if (existingItem.isPresent()) {
            // Update existing cart item quantity
            ItemCart cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return itemCartRepository.save(cartItem);
        } else {
            // Create new cart item
            ItemCart cartItem = new ItemCart();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            return itemCartRepository.save(cartItem);
        }
    }

    public ItemCart updateCartItemQuantity(Long cartItemId, Integer quantity) {
        ItemCart cartItem = itemCartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        if (cartItem.getProduct().getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + cartItem.getProduct().getName());
        }

        cartItem.setQuantity(quantity);
        return itemCartRepository.save(cartItem);
    }

    public void removeItemFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        itemCartRepository.deleteByUserAndProductId(user, productId);
    }

    public void clearCart(Long userId) {
        List<ItemCart> cartItems = itemCartRepository.findByUserId(userId);
        itemCartRepository.deleteAll(cartItems);
    }

    public BigDecimal calculateCartTotal(Long userId) {
        List<ItemCart> cartItems = itemCartRepository.findByUserId(userId);
        return cartItems.stream()
                .map(ItemCart::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getCartItemCount(Long userId) {
        List<ItemCart> cartItems = itemCartRepository.findByUserId(userId);
        return cartItems.size();
    }
}
