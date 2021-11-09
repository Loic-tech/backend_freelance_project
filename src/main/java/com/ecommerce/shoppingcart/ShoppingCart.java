package com.ecommerce.shoppingcart;

import com.ecommerce.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "shoppingCart")
@Getter
@Setter
@Data
@AllArgsConstructor
public class ShoppingCart {
    @Id
    private String id;

    private String ide;
    private Product product;
    private Integer quantity;
}
