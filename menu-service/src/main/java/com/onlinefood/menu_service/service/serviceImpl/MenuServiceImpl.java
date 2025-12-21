package com.onlinefood.menu_service.service.serviceImpl;

import com.onlinefood.menu_service.exception.MenuException;
import com.onlinefood.menu_service.model.Menu;
import com.onlinefood.menu_service.repository.MenuRepository;
import com.onlinefood.menu_service.service.MenuService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository repository;

    public MenuServiceImpl(MenuRepository repository) {
        this.repository = repository;
    }

    @Override
    public Object createMenu(String providerId, Object request) {

        if (!(request instanceof Menu menu)) {
            throw new MenuException("Invalid menu request");
        }

        repository.findByProviderIdAndNameIgnoreCase(providerId, menu.getName())
                .ifPresent(m -> {
                    throw new MenuException("Menu with same name already exists");
                });

        menu.setId(null);
        menu.setProviderId(providerId);
        menu.setActive(true);
        menu.setCreatedAt(LocalDateTime.now());
        menu.setUpdatedAt(LocalDateTime.now());

        return repository.save(menu);
    }

    @Override
    public Object getMenusByProvider(String providerId) {
        return repository.findByProviderId(providerId);
    }

    @Override
    public Object getActiveMenusByProvider(String providerId) {
        return repository.findByProviderIdAndActiveTrue(providerId);
    }
}
