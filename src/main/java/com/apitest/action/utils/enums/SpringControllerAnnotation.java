package com.apitest.action.utils.enums;

import com.apitest.action.utils.PathMappingAnnotation;

/**
 * @author joe
 * @data 2019/6/27 20:57
 * @email joeluo520@gmail.com
 */
public enum SpringControllerAnnotation implements PathMappingAnnotation {
    CONTROLLER("Controller", "org.springframework.stereotype.Controller"),
    REST_CONTROLLER("RestController", "org.springframework.web.bind.annotation.RestController");

    SpringControllerAnnotation(String shortName, String qualifiedName) {
        this.shortName = shortName;
        this.qualifiedName = qualifiedName;
    }

    private String shortName;
    private String qualifiedName;
    @Override
    public String getQualifiedName() {
        return qualifiedName;
    }
    @Override
    public String getShortName() {
        return shortName;
    }

/*
    @Override
    public List<PathMappingAnnotation> getPathMappings() {
        return allPathMappingAnnotations;
    }
*/

/*    static {
        for (SpringControllerAnnotation springControllerAnnotation : SpringControllerAnnotation.values()) {
            allPathMappingAnnotations.add(springControllerAnnotation);
        }
    }*/
}
