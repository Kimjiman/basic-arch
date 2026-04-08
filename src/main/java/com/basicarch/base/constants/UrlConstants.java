package com.basicarch.base.constants;

/**
 * packageName    : com.basicarch.constants
 * fileName       : UrlConstants
 * author         : KIM JIMAN
 * date           : 24. 7. 15. 월요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 24. 7. 15.     KIM JIMAN      First Commit
 */
public class UrlConstants {
    public static String[] SWAGGER_URLS = {
            "/swagger-ui/**"
            , "/v3/api-docs/**"
    };

    public static final String[] ACTUATOR_URLS = {
            "/actuator/**"
    };

    public static String[] RESOURCE_URLS = {
            "/static/**"
    };

    public static String[] ALLOWED_URLS = {
            "/auth/login"
            , "/auth/issueAccessToken"
    };

    public static String[] NOT_ALLOWED_URLS = {

    };
}
