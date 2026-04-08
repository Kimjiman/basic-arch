package com.basicarch.module.menu.controller;

import com.basicarch.base.constants.YN;
import com.basicarch.module.menu.facade.MenuFacade;
import com.basicarch.module.menu.model.MenuModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
@Slf4j
public class MenuController {
    private final MenuFacade menuFacade;

    @GetMapping
    public List<MenuModel> selectMenuTree(@RequestParam(required = false) YN useYn) {
        return menuFacade.findAllTree();
    }

    @GetMapping("/{id}")
    public MenuModel selectMenuById(@PathVariable Long id) {
        return menuFacade.findById(id);
    }

    @PostMapping
    public MenuModel createMenu(@RequestBody MenuModel menuModel) {
        return menuFacade.create(menuModel);
    }

    @PutMapping
    public MenuModel updateMenu(@RequestBody MenuModel menuModel) {
        return menuFacade.update(menuModel);
    }

    @DeleteMapping("/{id}")
    public void deleteMenu(@PathVariable Long id) {
        menuFacade.removeById(id);
    }
}
