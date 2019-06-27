package com.apitest.action.utils;

import com.intellij.openapi.module.Module;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author joe
 * @data 2019/6/27 21:03
 * @email joeluo520@gmail.com
 */
public class ModuleHelper {
    Module module;

    // URL
    private static final String SCHEME = "http://"; //PROTOCOL
    private static final String HOST = "localhost";
    private static final String PORT = "8080"; // int
    public static String DEFAULT_URI = "http://localhost"+":"+PORT;
//    private static final String PATH = "http://localhost"+":"+PORT; // PATH or FILE

    public static String getAUTHORITY() {
        return null;
    }

    PropertiesHandler propertiesHandler;

    public ModuleHelper(Module module) {
        this.module = module;
        propertiesHandler = new PropertiesHandler(module);
    }


    public static ModuleHelper create(Module module) {
        return new ModuleHelper(module);
    }

    @NotNull
    public String getServiceHostPrefix() {
        if (module == null) {
            return DEFAULT_URI;
        }

        String port = propertiesHandler.getServerPort();
        if (StringUtils.isEmpty(port)) port = PORT;

        String contextPath = propertiesHandler.getContextPath();
        return new StringBuilder(SCHEME).append(HOST).append(":").append(port).append(contextPath).toString();
    }
}
