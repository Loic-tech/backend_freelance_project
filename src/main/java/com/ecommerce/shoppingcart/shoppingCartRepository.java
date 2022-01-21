package com.ecommerce.shoppingcart;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface shoppingCartRepository extends MongoRepository<ShoppingCart,String> {
    List<ShoppingCart> findShoppingCartByIde(String i);
    List<ShoppingCart> findByIde(String id);

}
