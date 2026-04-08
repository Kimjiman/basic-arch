package com.basicarch.base.converter;

import com.basicarch.base.constants.YN;
import com.basicarch.base.utils.DateUtils;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TypeConverter {
    @Named("stringToLocalDateTime")
    public LocalDateTime stringToLocalDateTime(String time) {
        if (time == null || time.isEmpty()) {
            return null;
        }
        return DateUtils.stringToLocalDateTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    @Named("localDateTimeToString")
    public String localDateTimeToString(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return DateUtils.localDateTimeToString(time, "yyyy-MM-dd HH:mm:ss");
    }

    @Named("ynToString")
    public String ynToString(YN yn) {
        return yn != null ? yn.getValue() : null;
    }

    @Named("stringToYn")
    public YN stringToYn(String value) {
        if (value == null) return null;
        return YN.fromValue(value);
    }
}
