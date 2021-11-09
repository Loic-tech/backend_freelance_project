package com.ecommerce.user;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;






@Document(collection = "JwtResponse")
@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;
    private String password;


    public JwtResponse(String jwt, String id, String username, String mail, String password) {
        this.token = jwt;
        this.id = id;
        this.username = username;
        this.email = mail;
        this.password = password;
    }
}
