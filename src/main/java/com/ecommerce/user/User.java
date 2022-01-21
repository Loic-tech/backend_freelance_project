package com.ecommerce.user;

import com.ecommerce.shoppingcart.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "user")
@Getter
@Setter
@AllArgsConstructor
@Data
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    public User() {

    }
    public User(String username, String email, String encode) {
        this.username = username;
        this.email = email;
        this.password = encode;
    }
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
