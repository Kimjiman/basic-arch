package com.example.basicarch.module.menu.facade;

import com.example.basicarch.base.annotation.Facade;
import com.example.basicarch.base.cache.RedisCacheEventService;
import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.constants.YN;
import com.example.basicarch.base.exception.CustomException;
import com.example.basicarch.base.exception.SystemErrorCode;
import com.example.basicarch.base.exception.ToyAssert;
import com.example.basicarch.base.redis.CacheEventHandler;
import com.example.basicarch.base.utils.JsonUtils;
import com.example.basicarch.module.menu.converter.MenuConverter;
import com.example.basicarch.module.menu.entity.Menu;
import com.example.basicarch.module.menu.model.MenuModel;
import com.example.basicarch.module.menu.service.MenuService;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Facade
@Slf4j
@RequiredArgsConstructor
public class MenuCacheFacade implements CacheEventHandler {
    private static final String MENU_CACHE_KEY = "cache:menu";
    private static final String MENU_FIELD_ALL = "all";
    private static final String MENU_FIELD_USE_Y = "useYn:Y";

    private final MenuService menuService;
    private final MenuConverter menuConverter;
    private final RedisCacheEventService redisCacheEventService;

    @Override
    public CacheType getSupportedCacheType() {
        return CacheType.MENU;
    }

    @Override
    public void handle() {
        refresh();
    }

    @PostConstruct
    public void init() {
        try {
            refresh();
        } catch (Exception e) {
            log.warn("[MenuCacheFacade] refresh Error: {}", e.getMessage());
        }
    }

    public void refresh() {
        List<Menu> allMenus = menuService.findAll();
        List<Menu> activeMenus = menuService.findByUseYn("Y");

        redisCacheEventService.saveAll(MENU_CACHE_KEY, Map.of(
                MENU_FIELD_ALL, JsonUtils.toJson(allMenus),
                MENU_FIELD_USE_Y, JsonUtils.toJson(activeMenus)
        ));
        log.info("Menu cache refreshed. all={}, active={}", allMenus.size(), activeMenus.size());
    }

    private List<Menu> cachedAll() {
        return redisCacheEventService.get(MENU_CACHE_KEY, MENU_FIELD_ALL)
                .map(it -> JsonUtils.<List<Menu>>fromJson(it, new TypeToken<List<Menu>>() {}.getType()))
                .orElseGet(menuService::findAll);
    }

    private List<Menu> cachedActive() {
        return redisCacheEventService.get(MENU_CACHE_KEY, MENU_FIELD_USE_Y)
                .map(it -> JsonUtils.<List<Menu>>fromJson(it, new TypeToken<List<Menu>>() {}.getType()))
                .orElseGet(() -> menuService.findByUseYn("Y"));
    }

    public List<MenuModel> findAll() {
        return menuConverter.toModelList(cachedAll());
    }

    public List<MenuModel> findAllTree() {
        return buildTree(findAll());
    }

    public List<MenuModel> findByUseYn(YN useYn) {
        if (useYn == YN.Y) {
            return menuConverter.toModelList(cachedActive());
        }
        return menuConverter.toModelList(menuService.findByUseYn(useYn.getValue()));
    }

    public List<MenuModel> findTreeByUseYn(YN useYn) {
        return buildTree(findByUseYn(useYn));
    }

    public MenuModel findById(Long id) {
        ToyAssert.notNull(id, SystemErrorCode.REQUIRED, "ID를 입력해주세요.");
        return cachedAll().stream()
                .filter(m -> id.equals(m.getId()))
                .map(menuConverter::toModel)
                .findFirst()
                .orElseThrow(() -> new CustomException(SystemErrorCode.NOT_FOUND, "메뉴를 찾을 수 없습니다."));
    }

    private List<MenuModel> buildTree(List<MenuModel> flatList) {
        Map<Long, MenuModel> menuMap = flatList.stream()
                .peek(m -> m.setChildren(new ArrayList<>()))
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
