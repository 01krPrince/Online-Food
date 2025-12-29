package com.tiffin_provider_service.tiffin_provider_service.service.serviceImpl;

import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderRequestDTO;
import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderResponseDTO;
import com.tiffin_provider_service.tiffin_provider_service.enums.ProviderStatus;
import com.tiffin_provider_service.tiffin_provider_service.exception.UnauthorizedException;
import com.tiffin_provider_service.tiffin_provider_service.model.TiffinProvider;
import com.tiffin_provider_service.tiffin_provider_service.repository.TiffinProviderRepository;
import com.tiffin_provider_service.tiffin_provider_service.service.TiffinProviderService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TiffinProviderServiceImpl implements TiffinProviderService {

    private final TiffinProviderRepository repository;

    public TiffinProviderServiceImpl(TiffinProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public TiffinProviderResponseDTO apply(
            String userId,
            String role,
            TiffinProviderRequestDTO dto) {

        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            throw new UnauthorizedException("Only CUSTOMER can apply");
        }

        if (repository.existsByUserId(userId)) {
            throw new BadRequestException("Provider profile already exists");
        }

        TiffinProvider provider = new TiffinProvider();
        provider.setUserId(userId);
        provider.setName(dto.getName());
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
                provider.getName(),
                provider.getStatus()
        );
    }


    @Override
    public void updateStatus(String providerId, String role, String status) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only ADMIN can approve provider");
        }

        TiffinProvider provider = repository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        ProviderStatus newStatus =
                ProviderStatus.valueOf(status.toUpperCase());

        provider.setStatus(newStatus);
        provider.setUpdatedAt(LocalDateTime.now());

        repository.save(provider);

        // 👉 YAHAN future me user-service ko role update call hoga
    }

    @Override
    public Object getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
    }

    @Override
    public List<?> getByStatus(String status) {
        return repository.findByStatus(
                ProviderStatus.valueOf(status.toUpperCase()));
    }
}
