package com.user_service.user_service.model;

import com.user_service.user_service.enums.PublicRole;
import com.user_service.user_service.enums.UserStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String name;
    private String email;
    private String phone;
    private String password;

    private PublicRole role;              // CUSTOMER, PROVIDER, ADMIN
    private UserStatus status;       // ACTIVE, BLOCKED

    // Only if role == PROVIDER
    private String providerId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
