package com.user_service.user_service.service.impl;

import com.user_service.user_service.dto.VerifyOtpDTO;
import com.user_service.user_service.dto.provider.ProviderRegistrationDTO;
import com.user_service.user_service.dto.provider.TiffinProviderRequestDTO;
import com.user_service.user_service.dto.provider.TiffinProviderResponseDTO;
import com.user_service.user_service.enums.ProviderStatus;
import com.user_service.user_service.enums.Role;
import com.user_service.user_service.exception.ResourceNotFoundException;
import com.user_service.user_service.model.TiffinProvider;
import com.user_service.user_service.model.User;
import com.user_service.user_service.repository.TiffinProviderRepository;
import com.user_service.user_service.repository.UserRepository;
import com.user_service.user_service.service.TiffinProviderService;
import com.user_service.user_service.service.UserService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TiffinProviderServiceImpl implements TiffinProviderService {

    private final TiffinProviderRepository repository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public TiffinProviderServiceImpl(TiffinProviderRepository repository,
                                     UserRepository userRepository,
                                     UserService userService,
                                     PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public TiffinProviderResponseDTO registerNewProvider(ProviderRegistrationDTO dto) {
        VerifyOtpDTO verifyOtpDTO = new VerifyOtpDTO();
        verifyOtpDTO.setEmail(dto.getEmail());
        verifyOtpDTO.setOtp(dto.getOtp());

        try {
            userService.verifyOtp(verifyOtpDTO);
        } catch (RuntimeException e) {
            throw new BadRequestException(e.getMessage());
        }

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found. Please request OTP first."));

        if (repository.existsByUserId(user.getId())) {
            throw new BadRequestException("A Kitchen is already registered with this email.");
        }

        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.PROVIDER);
        user.setVerified(true);

        User savedUser = userRepository.save(user);

        TiffinProvider provider = new TiffinProvider();
        provider.setUserId(savedUser.getId());
        provider.setKitchenName(dto.getKitchenName());
        provider.setEmail(dto.getEmail());
        provider.setPhone(dto.getPhone());
        provider.setAddress(dto.getAddress());
        provider.setLegalAndFinancialDetails(dto.getLegalAndFinancialDetails());
        provider.setStatus(ProviderStatus.PENDING);
        provider.setCreatedAt(LocalDateTime.now());
        provider.setUpdatedAt(LocalDateTime.now());

        repository.save(provider);

        return new TiffinProviderResponseDTO(
                provider.getId(),
                provider.getKitchenName(),
                savedUser.getName(),
                provider.getStatus()
        );
    }

    @Override
    public TiffinProviderResponseDTO apply(String userId, TiffinProviderRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (repository.existsByUserId(userId)) {
            throw new BadRequestException("Provider profile already exists for this user");
        }

        TiffinProvider provider = new TiffinProvider();
        provider.setUserId(userId);
        provider.setKitchenName(dto.getKitchenName());
        provider.setEmail(dto.getEmail() != null ? dto.getEmail() : user.getEmail());
        provider.setPhone(dto.getPhone() != null ? dto.getPhone() : user.getPhone());
        provider.setAddress(dto.getAddress());
        provider.setLegalAndFinancialDetails(dto.getLegalAndFinancialDetails());
        provider.setStatus(ProviderStatus.PENDING);
        provider.setCreatedAt(LocalDateTime.now());
        provider.setUpdatedAt(LocalDateTime.now());

        repository.save(provider);

        if (user.getRole() == Role.CUSTOMER) {
            user.setRole(Role.PROVIDER);
            userRepository.save(user);
        }

        return new TiffinProviderResponseDTO(
                provider.getId(),
                provider.getKitchenName(),
                user.getName(),
                provider.getStatus()
        );
    }

    @Override
    public void updateStatus(String providerId, String status) {
        ProviderStatus newStatus;
        try {
            newStatus = ProviderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status provided: " + status);
        }

        TiffinProvider provider = repository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));

        provider.setStatus(newStatus);
        provider.setUpdatedAt(LocalDateTime.now());

        repository.save(provider);
    }

    @Override
    public TiffinProvider getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + id));
    }

    @Override
    public TiffinProviderResponseDTO getByUserId(String userId) {
        TiffinProvider provider = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider profile not found for User ID: " + userId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for ID: " + userId));

        return new TiffinProviderResponseDTO(
                provider.getId(),
                provider.getKitchenName(),
                user.getName(),
                provider.getStatus()
        );
    }

    @Override
    public List<TiffinProvider> getByStatus(String status) {
        try {
            return repository.findByStatus(ProviderStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status provided: " + status);
        }
    }
}