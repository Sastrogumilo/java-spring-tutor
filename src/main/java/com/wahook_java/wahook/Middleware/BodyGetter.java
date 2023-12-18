package com.wahook_java.wahook.Middleware;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Kelas ini untuk mendapatakan body dari @RequestBody atau @RequestParam
 */
public class BodyGetter {

    public static <T> T get(
            @RequestBody(required = false) Map<String, ?> body_map,
            @RequestParam(required = false) Map<String, ?> body_param,
            Class<T> returnType
    ) {
        if (body_map != null) {
            return returnType.cast(body_map);
        } else if (body_param != null) {
            return returnType.cast(body_param);
        } else {
            return null; // Or handle the case where both are null based on your requirements
        }
    }
}
