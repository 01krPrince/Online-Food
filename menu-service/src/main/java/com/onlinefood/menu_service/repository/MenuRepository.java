package com.onlinefood.menu_service.repository;

import com.onlinefood.menu_service.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends MongoRepository<Menu, String> {

    List<Menu> findByProviderId(String providerId);

    List<Menu> findByProviderIdAndActiveTrue(String providerId);

    Optional<Menu> findByProviderIdAndNameIgnoreCase(String providerId, String name);
}
