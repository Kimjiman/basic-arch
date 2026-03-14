package com.example.basicarch.base.utils;

import com.example.basicarch.base.security.AuthUserDetails;
import com.example.basicarch.module.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionUtils {
    public static String getPrincipal() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return "GUS";
        }
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (obj instanceof org.springframework.security.core.userdetails.UserDetails) {
            return ((org.springframework.security.core.userdetails.UserDetails) obj).getUsername();
        } else {
            return "GUS";
        }
    }

    public static AuthUserDetails getAuthUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object obj = authentication.getPrincipal();
        if (obj == null || obj.equals("anonymousUser")) {
            return null;
        }
        return (AuthUserDetails) obj;
    }

    public static User getUser() {
        return getAuthUserDetails() != null && getAuthUserDetails().user() != null ? getAuthUserDetails().user() : null;
    }

    public static Long getId() {
        return getUser() != null ? getUser().getId() : null;
    }

    public static String getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public static List<String> getRoleList() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public static boolean hasRole(String roleName) {
        return getRoleList().stream()
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                .anyMatch(role -> role.equals(roleName));
    }

//    /**
//     * 중복 로그인이라면 기존 유저의 세션을 만료시키고, 새 유저의 정보를 세션레지스트리에 등록한다.
//     * @param principal 인증객체
//     */
//    public void destroyDuplicateSession(Object principal) {
//        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attr.getRequest().getSession(true);
//
//        List<SessionInformation> sessionInfoList = new ArrayList<>();
//        for (Object obj : sessionRegistry.getAllPrincipals()) {
//            sessionInfoList.addAll(sessionRegistry.getAllSessions(obj, false));
//        }
//
//        for (SessionInformation sessionInformation : sessionInfoList) {
//            LoginUser newUser = (LoginUser)principal;
//            LoginUser originUser = (LoginUser)sessionInformation.getPrincipal();
//            if(originUser.getLoginId().equals(newUser.getLoginId())) {
//                log.info("registerNewSession userId: {}", newUser.getLoginId());
//                log.info("destroyDuplicationSession userId: {}", originUser.getLoginId());
//                sessionInformation.expireNow();
//            }
//        }
//        log.info("registerNewSession start sessionId : {}", session.getId());
//        sessionRegistry.registerNewSession(session.getId(), principal);
//    }
}
