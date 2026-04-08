package com.basicarch.module.menu.service;

import com.basicarch.base.service.BaseService;
import com.basicarch.module.menu.entity.Menu;
import com.basicarch.module.menu.model.MenuSearchParam;
import com.basicarch.module.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService implements BaseService<Menu, MenuSearchParam, Long> {
    private final MenuRepository menuRepository;

    @Override
    public boolean existsById(Long id) {
        return menuRepository.existsById(id);
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public List<Menu> findAllBy(MenuSearchParam param) {
        return menuRepository.findAll();
    }

    @Override
    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public Menu update(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) return;
        menuRepository.deleteById(id);
    }

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    public List<Menu> findByUseYn(String useYn) {
        return menuRepository.findByUseYn(useYn);
    }

    public List<Menu> findByParentId(Long parentId) {
        return menuRepository.findByParentId(parentId);
    }

    public void deleteByParentId(Long parentId) {
        menuRepository.deleteByParentId(parentId);
    }
}
