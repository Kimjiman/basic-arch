package com.example.basicarch.module.menu.facade;

import com.example.basicarch.base.annotation.CacheInvalidate;
import com.example.basicarch.base.annotation.Facade;
import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.exception.CustomException;
import com.example.basicarch.base.exception.SystemErrorCode;
import com.example.basicarch.base.exception.ToyAssert;
import com.example.basicarch.module.menu.converter.MenuConverter;
import com.example.basicarch.module.menu.entity.Menu;
import com.example.basicarch.module.menu.model.MenuModel;
import com.example.basicarch.module.menu.service.MenuCacheService;
import com.example.basicarch.module.menu.service.MenuService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Facade
@RequiredArgsConstructor
public class MenuFacade {
    private final MenuService menuService;
    private final MenuCacheService menuCacheService;
    private final MenuConverter menuConverter;

    @PostConstruct
    public void init() {
        try {
            refresh();
        } catch (Exception e) {
            log.error("[MenuFacade] cache refresh error: {}", e.getMessage());
        }
    }

    public void refresh() {
        menuCacheService.evict();
        menuCacheService.findAll();
        log.info("[MenuFacade] menu cache refreshed.");
    }

    public List<MenuModel> findAll() {
        return menuConverter.toModelList(menuCacheService.findAll());
    }

    public List<MenuModel> findAllTree() {
        return buildTree(findAll());
    }

    public MenuModel findById(Long id) {
        ToyAssert.notNull(id, SystemErrorCode.REQUIRED, "ID를 입력해주세요.");
        return menuCacheService.findAll().stream()
                .filter(it -> id.equals(it.getId()))
                .map(menuConverter::toModel)
                .findFirst()
                .orElseThrow(() -> new CustomException(SystemErrorCode.NOT_FOUND, "메뉴를 찾을 수 없습니다."));
    }

    @CacheInvalidate(CacheType.MENU)
    public MenuModel create(MenuModel menuModel) {
        Menu menu = menuService.save(menuConverter.toEntity(menuModel));
        return menuConverter.toModel(menu);
    }

    @CacheInvalidate(CacheType.MENU)
    public MenuModel update(MenuModel menuModel) {
        ToyAssert.notNull(menuModel.getId(), SystemErrorCode.REQUIRED, "ID를 입력해주세요.");
        Menu menu = menuService.save(menuConverter.toEntity(menuModel));
        return menuConverter.toModel(menu);
    }

    @CacheInvalidate(CacheType.MENU)
    @Transactional
    public void removeById(Long id) {
        ToyAssert.notNull(id, SystemErrorCode.REQUIRED, "ID를 입력해주세요.");
        deleteRecursive(id);
    }

    private void deleteRecursive(Long parentId) {
        List<Menu> children = menuService.findByParentId(parentId);
        for (Menu child : children) {
            deleteRecursive(child.getId());
        }
        menuService.deleteById(parentId);
    }

    private List<MenuModel> buildTree(List<MenuModel> flatList) {
        flatList.forEach(it -> it.setChildren(new ArrayList<>()));
        Map<Long, MenuModel> menuMap = flatList.stream()
                .collect(Collectors.toMap(MenuModel::getId, Function.identity()));

        List<MenuModel> roots = new ArrayList<>();
        for (MenuModel menu : menuMap.values()) {
            if (menu.getParentId() == null) {
                roots.add(menu);
            } else {
                MenuModel parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(menu);
                }
            }
        }

        Comparator<MenuModel> byOrder = Comparator.comparing(
                MenuModel::getOrder, Comparator.nullsLast(Comparator.naturalOrder()));
        sortRecursive(roots, byOrder);
        return roots;
    }

    private void sortRecursive(List<MenuModel> menus, Comparator<MenuModel> comparator) {
        menus.sort(comparator);
        for (MenuModel menu : menus) {
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                sortRecursive(menu.getChildren(), comparator);
            }
        }
    }
}
