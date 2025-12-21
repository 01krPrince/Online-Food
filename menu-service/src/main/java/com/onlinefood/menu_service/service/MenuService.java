package com.onlinefood.menu_service.service;

public interface MenuService {

    Object createMenu(String providerId, Object request);

    Object getMenusByProvider(String providerId);

    Object getActiveMenusByProvider(String providerId);
}
