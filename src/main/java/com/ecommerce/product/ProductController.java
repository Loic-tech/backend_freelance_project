package com.ecommerce.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> productList = productService.getAllProducts();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> AddProduct(@RequestBody Product product){
        productService.AddProduct(product);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable String id){
        Optional<Product> productOptional = productService.getOneProductById(id);
        return new ResponseEntity<>(productOptional,HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Optional<Product>> getProductByName(@PathVariable String name){
        Optional<Product> productOptional = productService.getOneProductByName(name);
        return new ResponseEntity<>(productOptional,HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) throws ProductException {
        Product product1 = productService.updateProduct(product);
        return new ResponseEntity<>(product1,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id){
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }






}
