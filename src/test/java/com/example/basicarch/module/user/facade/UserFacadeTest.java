package com.example.basicarch.module.user.facade;

import com.example.basicarch.base.constants.YN;
import com.example.basicarch.base.exception.CustomException;
import com.example.basicarch.module.user.converter.UserConverter;
import com.example.basicarch.module.user.model.UserModel;
import com.example.basicarch.module.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * packageName    : com.example.basicarch.module.user.facade
 * fileName       : UserFacadeTest
 * author         : KIM JIMAN
 * date           : 26. 2. 20. 금요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 2. 20.     KIM JIMAN      First Commit
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserFacade 테스트")
class UserFacadeTest {
    @Mock
    private UserService userService;
    @Mock
    private UserConverter userConverter;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserFacade userFacade;

    @Test
    @DisplayName("User 생성, loginId없이 생성할경우 에러발생")
    void userCreateFail() {
        UserModel userModel = UserModel.builder()
                .password("pass1234")
                .name("testUser")
                .useYn(YN.Y)
                .roleList(List.of("USR"))
                .build();

        assertThrows(CustomException.class, () -> userFacade.createUser(userModel));
    }


    @Test
    @DisplayName("User 수정, loginId없이 수정할경우 에러발생")
    void userUpdateFail() {
        UserModel userModel = UserModel.builder()
                .password("pass1234")
                .name("testUser")
                .useYn(YN.N)
                .roleList(List.of("USR"))
                .build();

        assertThrows(CustomException.class, () -> userFacade.updateUser(userModel));
    }

    @Test
    @DisplayName("password인코딩 성공")
    void passwordUpdateSuc() {
        UserModel userModel = UserModel.builder()
                .loginId("testUser")
                .password("pass1234")
                .name("testUser")
                .useYn(YN.Y)
                .roleList(List.of("USR"))
                .build();

        given(passwordEncoder.encode(userModel.getPassword())).willReturn("encodedPassword");

        userFacade.createUser(userModel);

        verify(passwordEncoder).encode("pass1234");
        verify(userService).save(any());
    }

    @Test
    @DisplayName("User 생성, password 없이 생성할 경우 에러발생")
    void userCreateFailNoPassword() {
        UserModel userModel = UserModel.builder()
                .loginId("testUser")
                .name("testUser")
                .useYn(YN.Y)
                .roleList(List.of("USR"))
                .build();

        assertThrows(CustomException.class, () -> userFacade.createUser(userModel));
    }

    @Test
    @DisplayName("User 생성, name 없이 생성할 경우 에러발생")
    void userCreateFailNoName() {
        UserModel userModel = UserModel.builder()
                .loginId("testUser")
                .password("pass1234")
                .useYn(YN.Y)
                .roleList(List.of("USR"))
                .build();

        assertThrows(CustomException.class, () -> userFacade.createUser(userModel));
    }

    @Test
    @DisplayName("User 수정, name 없이 수정할 경우 에러발생")
    void userUpdateFailNoName() {
        UserModel userModel = UserModel.builder()
                .loginId("testUser")
                .password("pass1234")
                .useYn(YN.Y)
                .roleList(List.of("USR"))
                .build();

        assertThrows(CustomException.class, () -> userFacade.updateUser(userModel));
    }
}
