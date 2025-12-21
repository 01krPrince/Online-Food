package com.onlinefood.menu_service.repository;

import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import com.onlinefood.menu_service.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {

    List<MenuItem> findByProviderIdAndAvailableTrue(String providerId);

    List<MenuItem> findByMealTypeAndFoodTypeAndAvailableTrue(
            MealType mealType,
            FoodType foodType
    );

    List<MenuItem> findByMealTypeAndAvailableTrue(MealType mealType);

    List<MenuItem> findByFoodTypeAndAvailableTrue(FoodType foodType);

    List<MenuItem> findByMenuId(String menuId);
}
