package com.basicarch.base.component.webclient;

import com.basicarch.base.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Slf4j
public class DefaultWebRequestClient implements WebRequestClient {

    private final WebClient webClient;
    private final String domainName;

    public DefaultWebRequestClient(WebClient webClient, String domainName) {
        this.webClient = webClient;
        this.domainName = domainName;
    }

    @Override
    public <T> Mono<T> get(String path, Map<String, String> queryParams, Class<T> responseType) {
        return get(path, queryParams, Collections.emptyMap(), responseType);
    }

    @Override
    public <T> Mono<T> get(String path, Map<String, String> queryParams, Map<String, String> headers, Class<T> responseType) {
        return executeRequest(HttpMethod.GET, path, queryParams, null, headers)
                .map(body -> JsonUtils.fromJson(body, responseType));
    }

    @Override
    public <T> Mono<T> post(String path, Object body, Class<T> responseType) {
        return post(path, body, Collections.emptyMap(), responseType);
    }

    @Override
    public <T> Mono<T> post(String path, Object body, Map<String, String> headers, Class<T> responseType) {
        return executeRequest(HttpMethod.POST, path, null, body, headers)
                .map(responseBody -> JsonUtils.fromJson(responseBody, responseType));
    }

    @Override
    public <T> Mono<T> put(String path, Object body, Class<T> responseType) {
        return put(path, body, Collections.emptyMap(), responseType);
    }

    @Override
    public <T> Mono<T> put(String path, Object body, Map<String, String> headers, Class<T> responseType) {
        return executeRequest(HttpMethod.PUT, path, null, body, headers)
                .map(responseBody -> JsonUtils.fromJson(responseBody, responseType));
    }

    @Override
    public <T> Mono<T> delete(String path, Map<String, String> queryParams, Class<T> responseType) {
        return delete(path, queryParams, Collections.emptyMap(), responseType);
    }

    @Override
    public <T> Mono<T> delete(String path, Map<String, String> queryParams, Map<String, String> headers, Class<T> responseType) {
        return executeRequest(HttpMethod.DELETE, path, queryParams, null, headers)
                .map(body -> JsonUtils.fromJson(body, responseType));
    }

    private Mono<String> executeRequest(HttpMethod method, String path,
                                        Map<String, String> queryParams,
                                        Object body,
                                        Map<String, String> headers) {
        log.info("[{}] {} {}", domainName, method, path);

        WebClient.RequestBodySpec requestSpec = webClient.method(method)
                .uri(uriBuilder -> {
                    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriBuilder.build().toString() + path);
                    if (queryParams != null) {
                        queryParams.forEach(builder::queryParam);
                    }
                    return builder.build().toUri();
                })
                .headers(httpHeaders -> {
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

        return responseSpec
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("[{}] {} {} - status: {}, error: {}",
                                            domainName, method, path, clientResponse.statusCode().value(), errorBody);
                                    return Mono.error(new WebClientRequestException(
                                            clientResponse.statusCode().value(), errorBody));
                                })
                )
                .bodyToMono(String.class);
    }
}
