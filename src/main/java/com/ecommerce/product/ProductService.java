package com.ecommerce.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void AddProduct(Product product){
        productRepository.save(product);
    }

    public Optional<Product> getOneProductById(String id){
        return productRepository.findById(id);
    }

    public Optional<Product> getOneProductByName(String name){
        return productRepository.findByName(name);
    }

    public Product updateProduct(Product product) throws ProductException{
        Optional<Product> optionalProduct = productRepository.findById(product.getId());

        if(optionalProduct.isEmpty()){
            throw new ProductException("EMPTY");
        }

        Product product1 = optionalProduct.get();

        product1.setName(product.getName());
        product1.setImage(product.getImage());
        product1.setPrice(product.getPrice());
        product1.setDescription(product.getDescription());

        productRepository.save(product1);

        return product1;
    }

    public void deleteById(String id){
        productRepository.deleteById(id);
    }

}
