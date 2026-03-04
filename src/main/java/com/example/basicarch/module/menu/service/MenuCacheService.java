package com.example.basicarch.module.menu.service;

import com.example.basicarch.module.menu.entity.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuCacheService {
    private final MenuService menuService;

    @Cacheable(value = "menu", key = "'all'")
    public List<Menu> findAll() {
        return menuService.findAll();
    }

    @Cacheable(value = "menu", key = "'active'")
    public List<Menu> findActive() {
        return menuService.findByUseYn("Y");
    }

    @CacheEvict(value = "menu", allEntries = true)
    public void evict() {}
}
