package com.basicarch.base.redis;

import com.basicarch.base.model.BaseObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "value", timeToLive = 60 * 60 * 24 * 3L)
@ToString
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class RedisObject extends BaseObject {
    @Id
    private String key;
    private String value;
    private Long expirationDay;

    public RedisObject(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    public RedisObject(final String key, final String value, final Long expirationDay) {
        this.key = key;
        this.value = value;
        this.expirationDay = expirationDay;
    }
}
