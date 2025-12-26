package com.onlinefood.menu_service.service.serviceImpl;

import com.onlinefood.menu_service.dto.MenuCreateRequestDTO;
import com.onlinefood.menu_service.dto.MenuUpdateRequestDTO;
import com.onlinefood.menu_service.enums.MenuType;
import com.onlinefood.menu_service.exception.ResourceNotFoundException;
import com.onlinefood.menu_service.exception.UnauthorizedException;
import com.onlinefood.menu_service.exception.ValidationException;
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

    // ================= CREATE =================

    @Override
    public Object createMenu(String providerId, MenuCreateRequestDTO dto) {

        // 🔒 Business validation (TIFFIN rules)
        if (dto.getMenuType() == MenuType.TIFFIN &&
                (dto.getActiveDays() == null || dto.getActiveDays().isEmpty())) {
            throw new ValidationException(
                    "Active days are required for TIFFIN menu"
            );
        }

        Menu menu = new Menu();
        menu.setProviderId(providerId);
        menu.setName(dto.getName());
        menu.setDescription(dto.getDescription());
        menu.setMenuImage(dto.getMenuImage());

        menu.setMenuType(dto.getMenuType());
        menu.setMealType(dto.getMealType());
        menu.setFoodType(dto.getFoodType());

        menu.setSubscriptionEnabled(
                Boolean.TRUE.equals(dto.getSubscriptionEnabled())
        );
        menu.setActiveDays(dto.getActiveDays());
        menu.setDeliveryTimeSlot(dto.getDeliveryTimeSlot());
        menu.setPricePerDay(dto.getPricePerDay());
        menu.setMonthlyPrice(dto.getMonthlyPrice());

        menu.setActive(dto.isActive());
        menu.setAdminApproved(false); // future admin approval

        menu.setCreatedAt(LocalDateTime.now());
        menu.setUpdatedAt(LocalDateTime.now());

        return repository.save(menu);
    }

    // ================= READ =================

    @Override
    public Object getMenusByProvider(String providerId) {
        return repository.findByProviderId(providerId);
    }

    @Override
    public Object getActiveMenusByProvider(String providerId) {
        return repository.findByProviderIdAndActiveTrue(providerId);
    }

    // ================= UPDATE =================

    @Override
    public Object updateMenu(
            String menuId,
            String providerId,
            MenuUpdateRequestDTO dto) {

        Menu menu = repository.findById(menuId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Menu not found"));

        if (!menu.getProviderId().equals(providerId)) {
            throw new UnauthorizedException("Unauthorized menu access");
        }

        if (dto.getName() != null) menu.setName(dto.getName());
        if (dto.getDescription() != null) menu.setDescription(dto.getDescription());
        if (dto.getMenuImage() != null) menu.setMenuImage(dto.getMenuImage());
        if (dto.getActive() != null) menu.setActive(dto.getActive());
        if (dto.getActiveDays() != null) menu.setActiveDays(dto.getActiveDays());
        if (dto.getDeliveryTimeSlot() != null)
            menu.setDeliveryTimeSlot(dto.getDeliveryTimeSlot());
        if (dto.getPricePerDay() != null)
            menu.setPricePerDay(dto.getPricePerDay());
        if (dto.getMonthlyPrice() != null)
            menu.setMonthlyPrice(dto.getMonthlyPrice());

        menu.setUpdatedAt(LocalDateTime.now());
        return repository.save(menu);
    }

    // ================= STATUS =================

    @Override
    public Object updateMenuStatus(
            String menuId,
            String providerId,
            boolean active) {

        Menu menu = repository.findById(menuId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Menu not found"));

        if (!menu.getProviderId().equals(providerId)) {
            throw new UnauthorizedException("Unauthorized menu access");
        }

        menu.setActive(active);
        menu.setUpdatedAt(LocalDateTime.now());
        return repository.save(menu);
    }
}
