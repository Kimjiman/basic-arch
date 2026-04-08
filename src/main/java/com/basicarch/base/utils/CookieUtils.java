package com.basicarch.base.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class CookieUtils {
    public static void setCookie(HttpServletResponse response, String key, String value, int day, String path) {
        if(null == value) value = "";
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(day * 24 * 60 * 60);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static void setCookie(HttpServletResponse response, String key, String value) {
        setCookie(response, key, value, 365, "/");
    }

    public static void deleteCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, 0, "/");
    }

    public static Cookie getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(key))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request, String key) {
        Cookie cookie = getCookie(request, key);
        if (cookie == null || cookie.getValue() == null) {
            return null;
        }
        return cookie.getValue();
    }
}
