package com.basicarch.module.menu.converter;

import com.basicarch.base.converter.TypeConverter;
import com.basicarch.module.menu.entity.Menu;
import com.basicarch.module.menu.model.MenuModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
        , uses = {TypeConverter.class}
)
public interface MenuConverter {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createId", target = "createId")
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "updateId", target = "updateId")
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "useYn", target = "useYn", qualifiedByName = "stringToYn")
    @Mapping(target = "children", ignore = true)
    MenuModel toModel(Menu menu);

    @Mapping(target = "rowNum", ignore = true)
    @Mapping(target = "isChild", ignore = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createId", target = "createId")
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "updateId", target = "updateId")
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "useYn", target = "useYn", qualifiedByName = "ynToString")
    Menu toEntity(MenuModel menuModel);

    List<MenuModel> toModelList(List<Menu> menuList);

    List<Menu> toEntityList(List<MenuModel> menuModelList);
}
