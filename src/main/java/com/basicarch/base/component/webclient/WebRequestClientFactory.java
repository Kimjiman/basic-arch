package com.basicarch.base.component.webclient;

import com.basicarch.base.exception.CustomException;
import com.basicarch.base.exception.SystemErrorCode;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class WebRequestClientFactory {

    private final Map<String, WebRequestClient> clients = new ConcurrentHashMap<>();

    public WebRequestClientFactory(WebClientProperties properties) {
        properties.getDomains().forEach((name, config) -> {
            WebClient webClient = buildWebClient(config);
            clients.put(name, new DefaultWebRequestClient(webClient, name));
            log.info("WebRequestClient registered: [{}] baseUrl={}", name, config.getBaseUrl());
        });
    }

    public WebRequestClient getClient(String domainName) {
        WebRequestClient client = clients.get(domainName);
        if (client == null) {
            throw new CustomException(SystemErrorCode.NOT_FOUND,
                    "등록되지 않은 WebClient 도메인입니다: " + domainName);
        }
        return client;
    }

    private WebClient buildWebClient(WebClientProperties.DomainConfig config) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout())
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(config.getReadTimeout(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(config.getWriteTimeout(), TimeUnit.MILLISECONDS)));

        WebClient.Builder builder = WebClient.builder()
                .baseUrl(config.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient));

        config.getHeaders().forEach(builder::defaultHeader);

        return builder.build();
    }
}
