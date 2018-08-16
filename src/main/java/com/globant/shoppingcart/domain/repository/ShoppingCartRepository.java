package com.globant.shoppingcart.domain.repository;

import com.globant.shoppingcart.domain.ShoppingCart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ShoppingCartRepository extends ReactiveCrudRepository<ShoppingCart, String> {
}
