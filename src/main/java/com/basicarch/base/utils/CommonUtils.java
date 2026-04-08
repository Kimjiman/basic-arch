package com.basicarch.base.utils;

import com.basicarch.base.model.Response;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Map;

@Slf4j
public class CommonUtils {
    private static final Gson gson = new Gson();
    private static final Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

    public static <T> void responseSuccess(T obj, HttpServletResponse response) throws IOException {
        String json = gson.toJson(Response.success(obj));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
    }

    public static void responseFail(Integer status, String message, HttpServletResponse response) throws IOException {
        String json = gson.toJson(Response.fail(status, message));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
    }

    /**
     * jwtToken을 decode해서 Map으로 변환한다.
     *
     * @param token
     * @return
     */
    public static Map<String, Object> decodeJwtToken(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        return gson.fromJson(new String(decoder.decode(chunks[1])), mapType);
    }
}
