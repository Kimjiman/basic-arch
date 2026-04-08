package com.basicarch.module.user.controller;

import com.basicarch.base.security.jwt.JwtTokenInfo;
import com.basicarch.base.utils.SessionUtils;
import com.basicarch.module.user.facade.AuthFacade;
import com.basicarch.module.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthFacade authFacade;

    @PostMapping("/login")
    public JwtTokenInfo login(@RequestBody UserModel userModel) {
        return authFacade.login(userModel);
    }

    @PostMapping("/logout")
    public void logout() {
        authFacade.logout();
    }

    @PostMapping("/issueAccessToken")
    public JwtTokenInfo issueAccessToken(@RequestBody JwtTokenInfo jwtTokenInfo) {
        return authFacade.issueAccessToken(jwtTokenInfo);
    }

    @PostMapping("/test")
    public void test() {
        log.info("loginId: {}", SessionUtils.getLoginId());
        log.info("id: {}", SessionUtils.getId());
        log.info("roleList: {}", SessionUtils.getRoleList());
        log.info("password: {}", SessionUtils.getUser().getPassword());
    }
}
