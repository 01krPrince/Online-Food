package com.user_service.user_service.service.impl;

import com.user_service.user_service.dto.RegisterRequestDTO;
import com.user_service.user_service.model.User;
import com.user_service.user_service.repository.UserRepository;
import com.user_service.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public String register(RegisterRequestDTO dto) {

        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // 🔐 will encrypt later
        user.setRole(dto.getRole());

        repository.save(user);

        return "User registered successfully";
    }
}
