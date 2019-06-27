package com.apitest.action.utils.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author joe
 * @data 2019/6/27 20:40
 * @email joeluo520@gmail.com
 */
public enum HttpMethod {
    GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, TRACE, CONNECT;

    private static final Map<String, HttpMethod> methodMap = new HashMap(8);


    public static HttpMethod getByRequestMethod(String method) {
        if (method == null || method.isEmpty()) {
            return null;
        }

        String[] split = method.split("\\.");

        if (split.length > 1) {
            method = split[split.length - 1].toUpperCase();
            return HttpMethod.valueOf(method);
        }

        return HttpMethod.valueOf(method.toUpperCase());
    }

    static {
        for (HttpMethod httpMethod : values()) {
            methodMap.put(httpMethod.name(), httpMethod);
        }
    }
}
