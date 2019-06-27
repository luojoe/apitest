package com.apitest.action.utils;

/**
 * @author joe
 * @data 2019/6/27 21:03
 * @email joeluo520@gmail.com
 */
public enum JaxrsPathAnnotation implements PathMappingAnnotation  {
    PATH("Path", "javax.ws.rs.Path");

    JaxrsPathAnnotation(String shortName, String qualifiedName) {
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
