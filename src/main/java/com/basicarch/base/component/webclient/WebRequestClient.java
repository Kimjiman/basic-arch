package com.basicarch.base.component.webclient;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface WebRequestClient {

    <T> Mono<T> get(String path, Map<String, String> queryParams, Class<T> responseType);

    <T> Mono<T> get(String path, Map<String, String> queryParams, Map<String, String> headers, Class<T> responseType);

    <T> Mono<T> post(String path, Object body, Class<T> responseType);

    <T> Mono<T> post(String path, Object body, Map<String, String> headers, Class<T> responseType);

    <T> Mono<T> put(String path, Object body, Class<T> responseType);

    <T> Mono<T> put(String path, Object body, Map<String, String> headers, Class<T> responseType);

    <T> Mono<T> delete(String path, Map<String, String> queryParams, Class<T> responseType);

    <T> Mono<T> delete(String path, Map<String, String> queryParams, Map<String, String> headers, Class<T> responseType);
}
