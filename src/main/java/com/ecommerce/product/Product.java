package com.ecommerce.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product")
@Data
@AllArgsConstructor
public class Product {
    @Id
    private String id;
    private String name;
    private String image;
    private int price;
    private String description;
}
