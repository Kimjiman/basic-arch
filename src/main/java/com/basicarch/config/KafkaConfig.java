package com.basicarch.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * KafkaListener > 예외 발생 > DefaultErrorHandler > 실패 > DeadLetterPublishingRecoverer > DLT 이동 > DltHandler
 * packageName    : com.basicarch.config
 * fileName       : CacheConfig
 * author         : KIM JIMAN
 * date           : 26. 3. 17. 화요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 17.     KIM JIMAN      First Commit
 */
@EnableKafka
@Configuration
public class KafkaConfig {
    public static final String INVALIDATE_TOPIC = "cache-invalidate";
    public static final String INVALIDATE_DLT_TOPIC = "cache-invalidate.DLT";

    // 캐시 무효화 토픽
    @Bean
    public NewTopic cacheInvalidateTopic() {
        return TopicBuilder.name(KafkaConfig.INVALIDATE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    // DLT 토픽
    @Bean
    public NewTopic cacheInvalidateDltTopic() {
        return TopicBuilder.name(KafkaConfig.INVALIDATE_DLT_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    // 컨슈머 에러 핸들러: 실패 시 DefaultErrorHandler(DLT로 전송하기 위함)
    @Bean
    public DefaultErrorHandler comsumerErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        DeadLetterPublishingRecoverer deadLetterPublishingRecoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        long second = 1;
        long maxAttempts = 3L;
        FixedBackOff fixedBackOff = new FixedBackOff(1000L * second, maxAttempts);

        return new DefaultErrorHandler(deadLetterPublishingRecoverer, fixedBackOff);
    }

    // 컨테이너 팩토리: ErrorHandler를 연결
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            DefaultErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}
