package com.ecommerce.user;



import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(){

    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
