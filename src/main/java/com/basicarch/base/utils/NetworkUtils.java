package com.basicarch.base.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class NetworkUtils {
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest();
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getBrowser(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        String browser = "unknown";

        if (userAgent != null) {
            if (userAgent.contains("Edg")) { // Microsoft Edge (Chromium)
                browser = "Microsoft Edge";
            } else if (userAgent.contains("Chrome")) { // Chrome
                browser = "Google Chrome";
            } else if (userAgent.contains("Firefox")) { // Firefox
                browser = "Mozilla Firefox";
            } else if (userAgent.contains("Safari")) { // Safari
                browser = "Apple Safari";
            } else if (userAgent.contains("OPR")) { // Opera
                browser = "Opera";
            } else if (userAgent.contains("Trident/7.0")) { // Internet Explorer 11
                browser = "Internet Explorer";
            } else if (userAgent.contains("MSIE")) { // Internet Explorer 10 이하
                browser = "Internet Explorer Old";
            }
        }
        return browser.toLowerCase();
    }

    public static String getIpAddress(HttpServletRequest request) {
        final List<String> IP_HEADERS = Arrays.asList(
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "X-Real-IP"
        );

        for (String header : IP_HEADERS) {
            String ipAddress = request.getHeader(header);
            if (ipAddress != null && !ipAddress.isEmpty() && !"unknown".equalsIgnoreCase(ipAddress)) {
                int commaIndex = ipAddress.indexOf(',');
                if (commaIndex != -1) {
                    return ipAddress.substring(0, commaIndex).trim();
                }
                return ipAddress;
            }
        }

        String remoteAddr = request.getRemoteAddr();
        int percentIndex = remoteAddr.indexOf('%');
        if (percentIndex != -1) {
            return remoteAddr.substring(0, percentIndex);
        }
        return remoteAddr;
    }

    public static String getOs(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        String os = "unknown";

        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            if (userAgent.contains("windows")) {
                os = "Windows";
            } else if (userAgent.contains("macintosh") || userAgent.contains("mac os")) {
                os = "Mac OS";
            } else if (userAgent.contains("linux")) {
                os = "Linux";
            } else if (userAgent.contains("android")) {
                os = "Android";
            } else if (userAgent.contains("iphone") || userAgent.contains("ipad") || userAgent.contains("ipod")) {
                os = "IOS";
            }
        }
        return os;
    }

    public static String getDevice(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        String device = "unknown";

        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            if (userAgent.contains("mobile")) {
                device = "Mobile";
            } else if (userAgent.contains("tablet")) {
                device = "Tablet";
            } else {
                device = "Desktop";
            }
        }
        return device;
    }

    public static String getFullDomain() {
        return getScheme() + "://" + getServerName() + ":" + getServerPort();
    }

    public static String getScheme() {
        return getRequest().getScheme();
    }

    public static String getServerName() {
        return getRequest().getServerName();
    }

    public static int getServerPort() {
        return getRequest().getServerPort();
    }

    public static String getDomain(HttpServletRequest request) {
        return request.getRequestURL().toString().replaceAll(request.getRequestURI(), "");
    }

    public static String getReferer(HttpServletRequest request) {
        String refererHeader = request.getHeader("Referer");
        return refererHeader != null ? refererHeader.split("\\?")[0] : null;
    }

    public static boolean passReferer(HttpServletRequest request, String... refererArr) {
        if (request == null || refererArr == null || refererArr.length == 0) {
            return false;
        }

        String referer = getReferer(request);
        if (referer == null) {
            return false;
        }

        String domain = getDomain(request);

        return Stream.of(refererArr)
                .map(path -> domain + path)
                .anyMatch(referer::equals);
    }
}
