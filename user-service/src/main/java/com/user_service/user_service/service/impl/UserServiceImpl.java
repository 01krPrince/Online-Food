package com.user_service.user_service.service.impl;

import com.user_service.user_service.dto.LoginRequestDTO;
import com.user_service.user_service.dto.LoginResponseDTO;
import com.user_service.user_service.dto.RegisterRequestDTO;
import com.user_service.user_service.dto.VerifyOtpDTO;
import com.user_service.user_service.enums.Role;
import com.user_service.user_service.enums.UserStatus;
import com.user_service.user_service.model.User;
import com.user_service.user_service.repository.UserRepository;
import com.user_service.user_service.security.JwtUtil;
import com.user_service.user_service.service.EmailService;
import com.user_service.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EmailService emailService; // Inject Email Service

    private final JwtUtil jwtUtil;

    public UserServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // ---------------- REGISTER (UPDATED) ----------------

    @Override
    public String register(RegisterRequestDTO dto) {

        // Check if user exists
        User existingUser = repository.findByEmail(dto.getEmail()).orElse(null);

        // 1. If user exists AND is already verified -> Error
        if (existingUser != null && existingUser.isVerified()) {
            throw new RuntimeException("Email already exists and is verified.");
        }

        // 2. If user exists but NOT verified -> We overwrite/resend OTP
        //    Otherwise -> Create new User
        User user = (existingUser != null) ? existingUser : new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(Role.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);

        // IMPORTANT: Set verified to false initially
        user.setVerified(false);

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpExpiration(LocalDateTime.now().plusMinutes(10)); // Expires in 10 mins

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        repository.save(user);

        // Send Email
        emailService.sendOtpEmail(dto.getEmail(), otp);

        return "OTP sent to email. Please verify to complete registration.";
    }

    // ---------------- VERIFY OTP (NEW) ----------------

    @Override
    public String verifyOtp(VerifyOtpDTO dto) {
        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isVerified()) {
            return "User already verified";
        }

        // Check if OTP matches
        if (user.getOtp() == null || !user.getOtp().equals(dto.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        // Check expiration
        if (user.getOtpExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        // Verification Successful
        user.setVerified(true);
        user.setOtp(null);           // Clear OTP
        user.setOtpExpiration(null); // Clear Expiration
        repository.save(user);

        return "Account verified successfully!";
    }

    // ---------------- LOGIN (UPDATED) ----------------

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {

        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        // 1. Check Verification Status
//        if (!user.isVerified()) {
//            throw new RuntimeException("Account not verified. Please verify your email first.");
//        }

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