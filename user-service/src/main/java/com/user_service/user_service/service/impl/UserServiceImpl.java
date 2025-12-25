package com.user_service.user_service.service.impl;

import com.user_service.user_service.dto.LoginRequestDTO;
import com.user_service.user_service.dto.LoginResponseDTO;
import com.user_service.user_service.dto.RegisterRequestDTO;
import com.user_service.user_service.enums.Role;
import com.user_service.user_service.enums.UserStatus;
import com.user_service.user_service.model.User;
import com.user_service.user_service.repository.UserRepository;
import com.user_service.user_service.security.JwtUtil;
import com.user_service.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    private final JwtUtil jwtUtil;

    public UserServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // ---------------- REGISTER ----------------

    @Override
    public String register(RegisterRequestDTO dto) {

        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(Role.CUSTOMER);

        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        repository.save(user);

        return "User registered successfully";
    }

    // ---------------- LOGIN ----------------

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {

        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            throw new RuntimeException("User account is blocked");
        }

        if (user.getRole() == null) {
            throw new RuntimeException("User role not assigned");
        }

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getRole().name()
        );

        return new LoginResponseDTO(token, user.getRole().name());
    }


    // ---------------- PROFILE ----------------

    @Override
    public User getUserById(String userId) {

        return repository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    // ---------------- ADMIN ----------------

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void updateStatus(String id, String status) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        try {
            UserStatus newStatus =
                    UserStatus.valueOf(status.toUpperCase());

            user.setStatus(newStatus);
            user.setUpdatedAt(LocalDateTime.now());

            repository.save(user);

        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid user status");
        }
    }

    @Override
    public void updateRole(String userId, Role role) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(role);
        user.setUpdatedAt(LocalDateTime.now());
        repository.save(user);
    }

}
