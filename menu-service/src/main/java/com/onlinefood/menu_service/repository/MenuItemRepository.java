package com.onlinefood.menu_service.repository;

import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import com.onlinefood.menu_service.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {

    List<MenuItem> findByProviderIdAndAvailableTrue(String providerId);

    List<MenuItem> findByAvailableTrue();

    List<MenuItem> findByAvailableTrueAndMealType(MealType mealType);

    List<MenuItem> findByAvailableTrueAndFoodType(FoodType foodType);

    List<MenuItem> findByAvailableTrueAndMealTypeAndFoodType(
            MealType mealType,
            FoodType foodType
    );
}
