package com.onlinefood.menu_service.repository;

import com.onlinefood.menu_service.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
    boolean existsByNameIgnoreCase(String name);
}
