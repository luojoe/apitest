package com.apitest.action.utils.enums;

/**
 * @author joe
 * @data 2019/6/27 22:27
 * @email joeluo520@gmail.com
 */
public enum SpringRequestParamAnnotations {
    REQUEST_PARAM("RequestParam", "org.springframework.web.bind.annotation.RequestParam"),
    REQUEST_BODY("RequestBody", "org.springframework.web.bind.annotation.RequestBody"),
    PATH_VARIABLE("PathVariable", "org.springframework.web.bind.annotation.PathVariable");

    SpringRequestParamAnnotations(String shortName, String qualifiedName) {
        this.shortName = shortName;
        this.qualifiedName = qualifiedName;
    }

    private String shortName;
    private String qualifiedName;

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getShortName() {
        return shortName;
    }

}
