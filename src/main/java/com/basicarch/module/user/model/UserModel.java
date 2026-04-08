package com.basicarch.module.user.model;

import com.basicarch.base.constants.YN;
import com.basicarch.base.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel extends BaseModel<Long> {
    private String loginId;
    private String password;
    private String name;
    private YN useYn;
    private List<String> roleList;
}
