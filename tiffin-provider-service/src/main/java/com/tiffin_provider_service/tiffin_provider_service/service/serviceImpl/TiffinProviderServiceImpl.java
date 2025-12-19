package com.tiffin_provider_service.tiffin_provider_service.service.serviceImpl;

import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderRequestDTO;
import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderResponseDTO;
import com.tiffin_provider_service.tiffin_provider_service.enums.ProviderStatus;
import com.tiffin_provider_service.tiffin_provider_service.exception.TiffinProviderException;
import com.tiffin_provider_service.tiffin_provider_service.model.TiffinProvider;
import com.tiffin_provider_service.tiffin_provider_service.repository.TiffinProviderRepository;
import com.tiffin_provider_service.tiffin_provider_service.service.TiffinProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TiffinProviderServiceImpl implements TiffinProviderService {

    @Autowired
    private TiffinProviderRepository repository;

    // 1️⃣ Register new tiffin provider
    @Override
    public TiffinProviderResponseDTO registerProvider(TiffinProviderRequestDTO dto) {

        // 🔒 Duplicate email check
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new TiffinProviderException("Email already registered");
        }

        // 🔒 Duplicate phone check
        if (repository.findByPhone(dto.getPhone()).isPresent()) {
            throw new TiffinProviderException("Phone number already registered");
        }

        TiffinProvider provider = new TiffinProvider();
        provider.setName(dto.getName().trim());
        provider.setEmail(dto.getEmail().toLowerCase());
        provider.setPhone(dto.getPhone());
        provider.setAddress(dto.getAddress().trim());
        provider.setStatus(ProviderStatus.PENDING);

        repository.save(provider);

        return mapToResponse(provider);
    }

    // 2️⃣ Get all providers
    @Override
    public List<TiffinProviderResponseDTO> getAllProviders() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 3️⃣ Get provider by ID
    @Override
    public TiffinProviderResponseDTO getProviderById(String id) {

        if (id == null || id.isBlank()) {
            throw new TiffinProviderException("Provider ID cannot be empty");
        }

        TiffinProvider provider = repository.findById(id)
                .orElseThrow(() ->
                        new TiffinProviderException("Tiffin Provider not found"));

        return mapToResponse(provider);
    }

    // 4️⃣ Update provider status (ADMIN use case)
    @Override
    public void updateStatus(String id, String status) {

        TiffinProvider provider = repository.findById(id)
                .orElseThrow(() ->
                        new TiffinProviderException("Tiffin Provider not found"));

        ProviderStatus newStatus;
        try {
            newStatus = ProviderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new TiffinProviderException("Invalid provider status");
        }

        // 🚦 Prevent redundant update
        if (provider.getStatus() == newStatus) {
            throw new TiffinProviderException(
                    "Provider is already in status: " + newStatus);
        }

        provider.setStatus(newStatus);
        repository.save(provider);
    }

    // 5️⃣ Get all providers by status
    @Override
    public List<TiffinProviderResponseDTO> getAllByStatus(String status) {

        ProviderStatus providerStatus;
        try {
            providerStatus = ProviderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new TiffinProviderException("Invalid status value");
        }

        return repository.findAll()
                .stream()
                .filter(p -> p.getStatus() == providerStatus)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 6️⃣ Update provider profile
    @Override
    public TiffinProviderResponseDTO updateProvider(
            String id,
            TiffinProviderRequestDTO dto) {

        TiffinProvider provider = repository.findById(id)
                .orElseThrow(() ->
                        new TiffinProviderException("Tiffin Provider not found"));

        // 🧠 Name
        if (dto.getName() != null && !dto.getName().isBlank()) {
            provider.setName(dto.getName().trim());
        }

        // 📧 Email (with duplicate check)
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {

            repository.findByEmail(dto.getEmail())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        throw new TiffinProviderException("Email already in use");
                    });

            provider.setEmail(dto.getEmail().toLowerCase());
        }

        // 📱 Phone (with duplicate check)
        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {

            repository.findByPhone(dto.getPhone())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        throw new TiffinProviderException("Phone already in use");
                    });

            provider.setPhone(dto.getPhone());
        }

        // 🏠 Address
        if (dto.getAddress() != null && !dto.getAddress().isBlank()) {
            provider.setAddress(dto.getAddress().trim());
        }

        repository.save(provider);
        return mapToResponse(provider);
    }

    // 🔁 Common mapper method
    private TiffinProviderResponseDTO mapToResponse(TiffinProvider provider) {

        TiffinProviderResponseDTO dto = new TiffinProviderResponseDTO();
        dto.setId(provider.getId());
        dto.setName(provider.getName());
        dto.setStatus(provider.getStatus());
        return dto;
    }
}
