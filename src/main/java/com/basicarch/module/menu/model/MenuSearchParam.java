package com.basicarch.module.menu.model;

import com.basicarch.base.model.BaseSearchParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MenuSearchParam extends BaseSearchParam<Long> {
    private String name;
    private String useYn;
    private Long parentId;
}
