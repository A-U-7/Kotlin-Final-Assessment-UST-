package com.amittechie_code.ecommerceapp.repository;

import com.amittechie_code.ecommerceapp.model.ItemCart;
import com.amittechie_code.ecommerceapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {

    List<ItemCart> findByUser(User user);

    List<ItemCart> findByUserId(Long userId);

    void deleteByUserAndProductId(User user, Long productId);

    boolean existsByUserAndProductId(User user, Long productId);

    Optional<ItemCart> findByUserAndProductId(User user, Long productId);
}
