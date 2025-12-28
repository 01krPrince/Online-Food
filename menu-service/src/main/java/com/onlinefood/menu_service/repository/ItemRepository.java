package com.onlinefood.menu_service.repository;

import com.onlinefood.menu_service.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

    List<Item> findByMenuIdsContaining(String menuId);

    List<Item> findByIdInAndProviderId(List<String> ids, String providerId);
}
