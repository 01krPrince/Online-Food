package com.onlinefood.menu_service.service.serviceImpl;
import com.onlinefood.menu_service.dto.CategoryRequestDTO;
import com.onlinefood.menu_service.dto.MenuItemRequestDTO;
import com.onlinefood.menu_service.model.Category;
import com.onlinefood.menu_service.model.MenuItem;
import com.onlinefood.menu_service.repository.CategoryRepository;
import com.onlinefood.menu_service.repository.MenuItemRepository;
import com.onlinefood.menu_service.service.MenuCatalogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuCatalogServiceImpl implements MenuCatalogService {

    private final CategoryRepository categoryRepo;
    private final MenuItemRepository itemRepo;

    public MenuCatalogServiceImpl(CategoryRepository categoryRepo,
                                  MenuItemRepository itemRepo) {
        this.categoryRepo = categoryRepo;
        this.itemRepo = itemRepo;
    }

    @Override
    public Category createCategory(CategoryRequestDTO dto) {
        if (categoryRepo.existsByNameIgnoreCase(dto.getName())) {
            throw new RuntimeException("Category already exists");
        }

        Category c = new Category();
        c.setName(dto.getName().trim());
        return categoryRepo.save(c);
    }

    @Override
    public MenuItem createMenuItem(MenuItemRequestDTO dto) {
        MenuItem item = new MenuItem();
        item.setName(dto.getName().trim());
        item.setCategoryId(dto.getCategoryId());
        item.setVeg(dto.isVeg());
        return itemRepo.save(item);
    }

    @Override
    public List<MenuItem> getAllItems() {
        return itemRepo.findAll();
    }
}
