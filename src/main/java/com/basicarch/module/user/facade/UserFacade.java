package com.basicarch.module.user.facade;

import com.basicarch.base.annotation.Facade;
import com.basicarch.base.exception.SystemErrorCode;
import com.basicarch.base.exception.ToyAssert;
import com.basicarch.module.user.converter.UserConverter;
import com.basicarch.module.user.model.UserModel;
import com.basicarch.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Facade
@RequiredArgsConstructor
@Slf4j
public class UserFacade {
    private final UserService userService;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserModel userModel) {
        ToyAssert.notBlank(userModel.getLoginId(), SystemErrorCode.REQUIRED, "로그인 아이디를 입력해주세요.");
        if (userModel.getId() == null) {
            ToyAssert.notBlank(userModel.getPassword(), SystemErrorCode.REQUIRED, "패스워드를 입력해주세요.");
        }
        ToyAssert.notBlank(userModel.getName(), SystemErrorCode.REQUIRED, "이름을 입력해주세요.");

        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userService.save(userConverter.toEntity(userModel));
    }

    public void updateUser(UserModel userModel) {
        ToyAssert.notBlank(userModel.getLoginId(), SystemErrorCode.REQUIRED, "로그인 아이디를 입력해주세요.");
        ToyAssert.notBlank(userModel.getName(), SystemErrorCode.REQUIRED, "이름을 입력해주세요.");
        userService.update(userConverter.toEntity(userModel));
    }
}
