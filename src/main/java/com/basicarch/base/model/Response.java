package com.basicarch.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Response<T> extends BaseObject {
    @Builder.Default
    private int status = 200;
    private String error;
    private String message;
    private T response;

    public static <T> Response<T> fail(int status, String error, String message) {
        return Response.<T>builder()
                .status(status)
                .error(error)
                .message(message)
                .build();
    }

    public static <T> Response<T> fail(int status, String message) {
        return Response.<T>builder()
                .status(status)
                .message(message)
                .build();
    }

    public static <T> Response<T> success(T response) {
        return Response.<T>builder()
                .status(200)
                .response(response)
                .build();
    }
}
