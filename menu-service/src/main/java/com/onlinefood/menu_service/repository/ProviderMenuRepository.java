package com.onlinefood.menu_service.repository;

import com.onlinefood.menu_service.model.ProviderMenu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderMenuRepository
        extends MongoRepository<ProviderMenu, String> {

    List<ProviderMenu> findByProviderId(String providerId);

    Optional<ProviderMenu> findByProviderIdAndMenuItemId(
            String providerId, String menuItemId);
}
