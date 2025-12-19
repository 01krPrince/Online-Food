package com.onlinefood.menu_service.service;

import com.onlinefood.menu_service.dto.ProviderMenuRequestDTO;
import com.onlinefood.menu_service.model.ProviderMenu;

import java.util.List;

public interface ProviderMenuService {

    ProviderMenu addOrUpdateMenu(
            String providerId,
            ProviderMenuRequestDTO dto);

    List<ProviderMenu> getProviderMenu(String providerId);
}
