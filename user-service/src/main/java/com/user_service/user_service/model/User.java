package com.user_service.user_service.model;

import com.user_service.user_service.enums.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String name;
    private String email;
    private String password;

    private Role role;

    // Only for RESTAURANT role
    private String restaurantId;
}
