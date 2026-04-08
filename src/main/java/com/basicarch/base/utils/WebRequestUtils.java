package com.basicarch.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

@Slf4j
public class WebRequestUtils {

    private static final WebClient WEB_CLIENT = WebClient.create();

    private WebRequestUtils() {}

    // ===== GET =====

    public static Map<String, Object> get(String url) {
        return get(url, Collections.emptyMap(), Collections.emptyMap());
    }

    public static Map<String, Object> get(String url, Map<String, String> queryParams) {
        return get(url, queryParams, Collections.emptyMap());
    }

    public static Map<String, Object> get(String url, Map<String, String> queryParams, Map<String, String> headers) {
        return execute(HttpMethod.GET, url, queryParams, null, headers);
    }

    // ===== POST =====

    public static Map<String, Object> post(String url, Object body) {
        return post(url, body, Collections.emptyMap());
    }

    public static Map<String, Object> post(String url, Object body, Map<String, String> headers) {
        return execute(HttpMethod.POST, url, null, body, headers);
    }

    // ===== PUT =====

    public static Map<String, Object> put(String url, Object body) {
        return put(url, body, Collections.emptyMap());
    }

    public static Map<String, Object> put(String url, Object body, Map<String, String> headers) {
        return execute(HttpMethod.PUT, url, null, body, headers);
    }

    // ===== DELETE =====

    public static Map<String, Object> delete(String url) {
        return delete(url, Collections.emptyMap(), Collections.emptyMap());
    }

    public static Map<String, Object> delete(String url, Map<String, String> queryParams) {
        return delete(url, queryParams, Collections.emptyMap());
    }

    public static Map<String, Object> delete(String url, Map<String, String> queryParams, Map<String, String> headers) {
        return execute(HttpMethod.DELETE, url, queryParams, null, headers);
    }

    // ===== 공통 실행 =====

    private static Map<String, Object> execute(HttpMethod method, String url,
                                               Map<String, String> queryParams,
                                               Object body,
                                               Map<String, String> headers) {
        log.info("[WebRequestUtils] {} {}", method, url);

        WebClient.RequestBodySpec requestSpec = WEB_CLIENT.method(method)
                .uri(uriBuilder -> {
                    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
                    if (queryParams != null) {
                        queryParams.forEach(builder::queryParam);
                    }
                    return builder.build().toUri();
                })
                .headers(httpHeaders -> {
                    httpHeaders.set("Content-Type", "application/json");
                    if (headers != null) {
                        headers.forEach(httpHeaders::set);
                    }
                });

        WebClient.ResponseSpec responseSpec;
        if (body != null && (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH)) {
            responseSpec = requestSpec.bodyValue(body).retrieve();
        } else {
            responseSpec = requestSpec.retrieve();
        }

        String responseBody = responseSpec
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .map(errorBody -> {
                                    log.error("[WebRequestUtils] {} {} - status: {}, error: {}",
                                            method, url, clientResponse.statusCode().value(), errorBody);
                                    return new RuntimeException(errorBody);
                                })
                )
                .bodyToMono(String.class)
                .block();

        return JsonUtils.fromJsonToMap(responseBody);
    }
}
