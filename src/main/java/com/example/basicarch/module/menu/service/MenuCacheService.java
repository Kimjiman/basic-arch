package com.example.basicarch.module.menu.service;

import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.service.BaseCacheService;
import com.example.basicarch.module.menu.entity.Menu;
import com.example.basicarch.module.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuCacheService implements BaseCacheService {
    private final MenuRepository menuRepository;

    @Override
    public CacheType getCacheType() {
        return CacheType.MENU;
    }

    @Override
    @CacheEvict(value = CacheType.Names.MENU, allEntries = true)
    public void evict() {}

    @Cacheable(value = CacheType.Names.MENU, key = "'all'", unless = "#result == null || #result.isEmpty()")
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }
}
