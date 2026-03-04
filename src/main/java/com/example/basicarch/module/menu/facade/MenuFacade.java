package com.example.basicarch.module.menu.facade;

import com.example.basicarch.base.annotation.Facade;
import com.example.basicarch.base.cache.RedisCacheEventService;
import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.exception.SystemErrorCode;
import com.example.basicarch.base.exception.ToyAssert;
import com.example.basicarch.module.menu.converter.MenuConverter;
import com.example.basicarch.module.menu.entity.Menu;
import com.example.basicarch.module.menu.model.MenuModel;
import com.example.basicarch.module.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class MenuFacade {
    private final MenuService menuService;
    private final MenuConverter menuConverter;
    private final RedisCacheEventService redisCacheEventService;

    public MenuModel create(MenuModel menuModel) {
        Menu saved = menuService.save(menuConverter.toEntity(menuModel));
        redisCacheEventService.publishEvent(CacheType.MENU);
        return menuConverter.toModel(saved);
    }

    public MenuModel update(MenuModel menuModel) {
        ToyAssert.notNull(menuModel.getId(), SystemErrorCode.REQUIRED, "ID를 입력해주세요.");
        Menu saved = menuService.save(menuConverter.toEntity(menuModel));
        redisCacheEventService.publishEvent(CacheType.MENU);
        return menuConverter.toModel(saved);
    }

    @Transactional
    public void removeById(Long id) {
        ToyAssert.notNull(id, SystemErrorCode.REQUIRED, "ID를 입력해주세요.");
        deleteRecursive(id);
        redisCacheEventService.publishEvent(CacheType.MENU);
    }

    private void deleteRecursive(Long parentId) {
        List<Menu> children = menuService.findByParentId(parentId);
        for (Menu child : children) {
            deleteRecursive(child.getId());
        }
        menuService.deleteById(parentId);
    }
}
