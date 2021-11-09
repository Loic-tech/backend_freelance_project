package com.ecommerce.user;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userRole")
public enum UserRole {
    ADMIN,USER
}
