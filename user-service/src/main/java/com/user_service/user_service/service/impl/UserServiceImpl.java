package com.user_service.user_service.service.impl;

import com.user_service.user_service.dto.customer.LoginRequestDTO;
import com.user_service.user_service.dto.customer.LoginResponseDTO;
import com.user_service.user_service.dto.customer.RegisterRequestDTO;
import com.user_service.user_service.dto.VerifyOtpDTO;
import com.user_service.user_service.enums.Role;
import com.user_service.user_service.enums.UserStatus;
import com.user_service.user_service.exception.BadRequestException;
import com.user_service.user_service.exception.ResourceNotFoundException;
import com.user_service.user_service.model.User;
import com.user_service.user_service.repository.UserRepository;
import com.user_service.user_service.security.JwtUtil;
import com.user_service.user_service.service.EmailService;
import com.user_service.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Import this
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository,
                           EmailService emailService,
                           JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------- SEND OTP ----------------
    @Override
    public void sendOtp(String email) {
        User user = repository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setRole(Role.CUSTOMER);
            user.setVerified(false);
            user.setStatus(UserStatus.ACTIVE);
            user.setCreatedAt(LocalDateTime.now());
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpExpiration(LocalDateTime.now().plusMinutes(10)); // 10 mins expiry
        user.setUpdatedAt(LocalDateTime.now());

        repository.save(user);

        System.out.println("DEBUG OTP for " + email + ": " + otp);
        emailService.sendOtpEmail(email, otp);
    }

    // ---------------- REGISTER (CUSTOMER) ----------------
    @Override
    public String register(RegisterRequestDTO dto) {

        User existingUser = repository.findByEmail(dto.getEmail()).orElse(null);
        if (existingUser != null && existingUser.isVerified()) {
            throw new BadRequestException("Email already exists and is verified. Please login.");
        }

        User user = (existingUser != null) ? existingUser : new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setRole(Role.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        user.setVerified(false);

        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpExpiration(LocalDateTime.now().plusMinutes(10));

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        repository.save(user);

        emailService.sendOtpEmail(dto.getEmail(), otp);

        return "OTP sent to email. Please verify to complete registration.";
    }

    // ---------------- VERIFY OTP ----------------
    @Override
    public void verifyOtp(VerifyOtpDTO dto) {
        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(dto.getOtp())) {
            throw new BadRequestException("Invalid OTP");
        }

        if (user.getOtpExpiration().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP has expired");
        }

        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiration(null);
        repository.save(user);
    }

    // ---------------- LOGIN ----------------
    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {

        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!user.isVerified()) {
            throw new BadRequestException("Account not verified. Please verify your email first.");
        }

        if (UserStatus.BLOCKED.equals(user.getStatus()) || !UserStatus.ACTIVE.equals(user.getStatus())) {
            throw new BadRequestException("User account is blocked or inactive");
        }

        if (user.getRole() == null) {
            throw new BadRequestException("User role not assigned");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getRole().name()
        );

        return new LoginResponseDTO(token, user.getRole().name());
    }

    // ---------------- PROFILE & ADMIN ----------------

    @Override
    public User getUserById(String userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void updateStatus(String id, String status) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        try {
            UserStatus newStatus = UserStatus.valueOf(status.toUpperCase());
            user.setStatus(newStatus);
            user.setUpdatedAt(LocalDateTime.now());
            repository.save(user);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid user status");
        }
    }

    @Override
    public void updateRole(String userId, Role role) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(role);
        user.setUpdatedAt(LocalDateTime.now());
        repository.save(user);
    }
}