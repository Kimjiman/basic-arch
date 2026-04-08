package com.basicarch.base.security;

import com.basicarch.base.utils.CommonUtils;
import com.basicarch.base.utils.SessionUtils;
import com.basicarch.module.user.entity.User;
import com.basicarch.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        User user = userRepository.findById(SessionUtils.getId()).orElse(null);
        if (user != null) {
            user.setPassword("[hidden]");
        }
        CommonUtils.responseSuccess(user, response);
    }
}
