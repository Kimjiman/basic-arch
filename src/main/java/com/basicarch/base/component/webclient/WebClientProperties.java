package com.basicarch.base.component.webclient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "web-client")
public class WebClientProperties {

    private Map<String, DomainConfig> domains = new LinkedHashMap<>();

    @Getter
    @Setter
    public static class DomainConfig {
        private String baseUrl = "";
        private int connectTimeout = 5000;
        private int readTimeout = 10000;
        private int writeTimeout = 10000;
        private Map<String, String> headers = new LinkedHashMap<>();
    }
}
