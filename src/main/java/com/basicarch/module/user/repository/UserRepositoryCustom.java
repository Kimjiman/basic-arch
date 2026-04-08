package com.basicarch.module.user.repository;

import com.basicarch.module.user.entity.User;
import com.basicarch.module.user.model.UserSearchParam;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> findAllBy(UserSearchParam param);
}
