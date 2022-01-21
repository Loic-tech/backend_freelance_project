package com.ecommerce.shoppingcart;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "shop")
public class ShoppingCartController {

    @Autowired
    shoppingCartRepository shoppingCartRepository;

    @PostMapping(value = "/adding")
    public ResponseEntity<?> saveBook( @RequestBody ShoppingCart shoppingCart){
        shoppingCartRepository.save(shoppingCart);

        return new ResponseEntity<>(shoppingCart, HttpStatus.CREATED);
    }
    @GetMapping(value = "/cart/{id}")
    public ResponseEntity<?> AllCart(@PathVariable String id){
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findShoppingCartByIde(id);

        return new ResponseEntity<>(shoppingCartList,HttpStatus.OK);
    }

    @GetMapping(value = "/count/{id}")
    public int getCountOfCart(@PathVariable String id) {
        int count = 0;
        List<ShoppingCart> shoppingCart = shoppingCartRepository.findShoppingCartByIde(id);
        for (ShoppingCart cart : shoppingCart) {
            count += cart.getQuantity();
        }
        return count;
    }

    @DeleteMapping(value = "/cart/{id}")
    public ResponseEntity<?> Delete(@PathVariable String id){
        shoppingCartRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value ="/modify/{id}" )
    public ResponseEntity<?> Update(@PathVariable String id, @RequestParam String idb, @RequestParam int quantity) throws ShoppingCartException {

        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByIde(id);
        if(shoppingCartList.isEmpty())
            throw new ShoppingCartException("404");
        else {
            for (ShoppingCart shoppingCart : shoppingCartList) {
                if (idb.equals(shoppingCart.getProduct().getId())) {
                    shoppingCart.setQuantity(shoppingCart.getQuantity() + quantity);
                    shoppingCartRepository.save(shoppingCart);

                    return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
                }
            }
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(value ="/modifyq/{id}" )
    public ResponseEntity<?> UpdateQuantity(@PathVariable String id, @RequestParam String idb, @RequestParam int quantity)
            throws ShoppingCartException {

        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByIde(id);
        if(shoppingCartList.isEmpty())
            throw new ShoppingCartException("404");
        else {
            for (ShoppingCart shoppingCart : shoppingCartList) {
                if (idb.equals(shoppingCart.getProduct().getId())) {
                    shoppingCart.setQuantity(quantity);
                    shoppingCartRepository.save(shoppingCart);

                    return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
                }
            }
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
