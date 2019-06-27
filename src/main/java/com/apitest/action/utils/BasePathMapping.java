package com.apitest.action.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joe
 * @data 2019/6/27 20:44
 * @email joeluo520@gmail.com
 */
public abstract class BasePathMapping {
    static List<PathMappingAnnotation> pathMappings = new ArrayList<>();

    public List<PathMappingAnnotation> getPathMappings() {
        return pathMappings;
    }

    /*need override*/
    public void addToPathList(PathMappingAnnotation mapping) {
        pathMappings.add(mapping);
    }
}
