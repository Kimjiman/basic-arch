package com.basicarch.config;

import com.basicarch.base.cache.redis.RedisCacheEventListener;
import com.basicarch.base.constants.CacheType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
public class RedisConfig {
    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;
    @Value("${spring.data.redis.port:6379}")
    private int redisPort;
    @Value("${spring.data.redis.password:}")
    private String redisPassword;
    @Value("${spring.data.redis.ssl.enabled:false}")
    private boolean redisSsl;


    /**
     * Redis 커넥터
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        if (redisPassword != null && !redisPassword.isBlank()) {
            config.setPassword(redisPassword);
        }

        // dev/prod SSL enable
        if (redisSsl) {
            LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                    .useSsl()
                    .build();
            return new LettuceConnectionFactory(config, clientConfig);
        }

        // local SSL disable
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }


    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        return new StringRedisTemplate(redisConnectionFactory());
    }

    /**
     * @Cacheable: 최초 조회 시 DB에서 읽어 Redis에 저장, 이후 Redis에서 반환
     * @CacheEvict: 데이터 변경 시 Redis에서 캐시를 삭제해 다음 조회 때 DB를 다시 읽게 한다
     * DefaultTyping.NON_FINAL: final 클래스 제외하고 타입 정보 추가
     * DefaultTyping.EVERYTHING: 모든 타입에 추가
     * DefaultTyping.NON_CONCRETE: 인터페이스, 추상클래스에만 추가
     */
    @Bean
    @ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer(
                                    JsonMapper.builder()
                                        .addModule(new JavaTimeModule()) // java 날짜타입
                                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 한 문자열로 구성
                                        .activateDefaultTyping(  // 레디스 저장시 클래스 타입정보 같이 저장
                                            BasicPolymorphicTypeValidator.builder()
                                                .allowIfSubType(Object.class) // Object 클래스 저장
                                                .build(),
                                            ObjectMapper.DefaultTyping.NON_FINAL
                                        )
                                        .build()
                                )
                        )
                );


        Map<String, RedisCacheConfiguration> cacheConfigs = Arrays.stream(CacheType.values())
                .collect(Collectors.toMap(it -> it.getCacheName(), it -> config.entryTtl(it.getTtl())));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }


    /**
     * Redis에서 메시지를 받을 경우, 실행되는 어댑터
     */
    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisCacheEventListener listener) {
        // CacheEventListener의 onMessage 메서드 실행
        return new MessageListenerAdapter(listener, "onMessage");
    }

    /**
     * 구동 시 Redis에 SUBSCRIBE cache:invalidate 커맨드를 실행 해당 채널을 구독한다.
     * 해당 채널에 메시지가 오면 adapter → CacheEventListener.onMessage()를 호출한다
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory factory,
            MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        // SUBSCRIBE cache:invalidate 커맨드를 실행
        container.addMessageListener(adapter, new ChannelTopic(CacheType.INVALIDATE_CHANNEL));
        return container;
    }
}
