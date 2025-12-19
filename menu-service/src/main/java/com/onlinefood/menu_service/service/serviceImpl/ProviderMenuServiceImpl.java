package com.onlinefood.menu_service.service.serviceImpl;

import com.onlinefood.menu_service.dto.ProviderMenuRequestDTO;
import com.onlinefood.menu_service.model.ProviderMenu;
import com.onlinefood.menu_service.repository.ProviderMenuRepository;
import com.onlinefood.menu_service.service.ProviderMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderMenuServiceImpl implements ProviderMenuService {

    private final ProviderMenuRepository repo;

    public ProviderMenuServiceImpl(ProviderMenuRepository repo) {
        this.repo = repo;
    }

    @Override
    public ProviderMenu addOrUpdateMenu(
            String providerId,
            ProviderMenuRequestDTO dto) {

        ProviderMenu pm = repo
                .findByProviderIdAndMenuItemId(providerId, dto.getMenuItemId())
                .orElse(new ProviderMenu());

        pm.setProviderId(providerId);
        pm.setMenuItemId(dto.getMenuItemId());
        pm.setPrice(dto.getPrice());
        pm.setAvailable(dto.isAvailable());

        return repo.save(pm);
    }

    @Override
    public List<ProviderMenu> getProviderMenu(String providerId) {
        return repo.findByProviderId(providerId);
    }
}
