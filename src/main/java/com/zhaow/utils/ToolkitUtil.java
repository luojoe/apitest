package com.zhaow.utils;


import com.google.gson.JsonObject;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.DisposeAwareRunnable;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.*;

public class ToolkitUtil {
    public static void runWhenInitialized(final Project project, final Runnable r) {

        if (project.isDisposed()) return;

        if (isNoBackgroundMode()) {
            r.run();
            return;
        }

        if (!project.isInitialized()) {
            StartupManager.getInstance(project).registerPostStartupActivity(DisposeAwareRunnable.create(r, project));
            return;
        }

/*        System.out.println((DumbService.getInstance(project).isDumb()));
        if (DumbService.getInstance(project).isDumb()) {
//            return;
            runWhenInitialized(project,r);
        }*/
//        runDumbAware(project, r);
        invokeLater(project, r);
//        ApplicationManager.getApplication().invokeAndWait(r);
    }


    public static void runWhenProjectIsReady(final Project project, final Runnable runnable) {

//        DumbService.getInstance(project).runWhenSmart(runnable);
        DumbService.getInstance(project).smartInvokeLater(runnable);
//        DumbService.getInstance(project).runReadActionInSmartMode(runnable);
    }


    public static boolean isNoBackgroundMode() {
        return (ApplicationManager.getApplication().isUnitTestMode()
                || ApplicationManager.getApplication().isHeadlessEnvironment());
    }


    public static void runDumbAware(final Project project, final Runnable r) {
        if (DumbService.isDumbAware(r)) {
            r.run();
        }
        else {
            DumbService.getInstance(project).runWhenSmart(DisposeAwareRunnable.create(r, project));
        }
    }


    public static void invokeLater(Runnable r) {
        ApplicationManager.getApplication().invokeLater(r);
    }

    public static void invokeLater(Project p, Runnable r) {
        invokeLater(p, ModalityState.defaultModalityState(), r);
    }

    public static void invokeLater(final Project p, final ModalityState state, final Runnable r) {
        if (isNoBackgroundMode()) {
            r.run();
        }
        else {
            ApplicationManager.getApplication().invokeLater(DisposeAwareRunnable.create(r, p), state);
        }
    }


    public static String formatHtmlImage(URL url) {
        return "<img src=\"" + url + "\"> ";
    }


    public static PsiClass findPsiClass(final String qualifiedName, final Module module, final Project project) {
        final GlobalSearchScope scope = module == null ? GlobalSearchScope.projectScope(project) : GlobalSearchScope.moduleWithDependenciesScope(module);
        return JavaPsiFacade.getInstance(project).findClass(qualifiedName, scope);
    }

    public static PsiPackage getContainingPackage(@NotNull PsiClass psiClass) {
        PsiDirectory directory = psiClass.getContainingFile().getContainingDirectory();
        return directory == null ? null : JavaDirectoryService.getInstance().getPackage(directory);
    }

    public static void runWriteAction(@NotNull Runnable action) {
        ApplicationManager.getApplication().runWriteAction(action);
    }



    @NotNull
    public static String removeRedundancyMarkup(String pattern) {

        String localhostRegex = "(http(s?)://)?(localhost)(:\\d+)?";
        String hostAndPortRegex = "(http(s?)://)?" +
                "( " +
                "([a-zA-Z0-9]([a-zA-Z0-9\\\\-]{0,61}[a-zA-Z0-9])?\\\\.)+[a-zA-Z]{2,6} |" +  // domain
                "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)" + // ip address
                ")" ;

        String localhost = "localhost";
        if (pattern.contains(localhost)) {
            pattern = pattern.replaceFirst(localhostRegex,"");
        }

        if (pattern.contains("http:") || pattern.contains("https:") ) { // quick test if reg exp should be used
            pattern  = pattern.replaceFirst(hostAndPortRegex, "");
        }

        //TODO : resolve RequestMapping(params="method=someMethod")
        // 包含参数
        if (pattern.contains("?")) {
            pattern = pattern.substring(0, pattern.indexOf("?"));
        }
        return pattern;
    }

    /* 将文本转为 Param */
    @NotNull
    public static String textToRequestParam(String text) {
        //拼装param参数
        StringBuilder param = new StringBuilder();

        Map<String, String> paramMap = textToParamMap(text);

        if (paramMap != null && paramMap.size() > 0) {
            paramMap.forEach((s, o) -> param.append(s).append("=").append(o).append("&"));
        }

        String params = param.length() == 0 ? "" : param.deleteCharAt(param.length() - 1).toString();

        return params;
    }


    /* 将文本转为 Param Map*/
    @NotNull
    public static Map<String, String> textToParamMap(String text) {
        Map<String, String> paramMap = new HashMap<>();
        //拼装param参数
        String paramText = text;//serviceInfo.getText();
        String[] lines = paramText.split("\n");

        for (String line : lines) {
            //合法参数键值对 （非//开头，且包含 : ）
            if (!line.startsWith("//") && line.contains(":")) {

                String[] prop = line.split(":");

                if (prop.length > 1) {
                    String key = prop[0].trim();
                    String value = prop[1].trim();
//                        value = line.substring()
                    paramMap.put(key, value);
                }
            }
        }
        return paramMap;
    }

    /* 将文本转为 Param Map*/
    @NotNull
    public static List<Map<String, String>> textToParamMapList(String text) {
        List<Map<String, String>> list = new ArrayList<>();
        //拼装param参数
        String paramText = text;//serviceInfo.getText();
        String[] lines = paramText.split("\n");
        //储存类似 id:1,2,3
        Map<String, String[]> params = new HashMap<>();
        //长度最长的那个文件
        int maxLength = 1;
        for (String line : lines) {
            //合法参数键值对 （非//开头，且包含 : ）
            if (!line.startsWith("//") && line.contains(":")) {
                String[] prop = line.split(":");
                if (prop.length > 1) {
                    String key = prop[0].trim();
                    String value = prop[1].trim();
                    String[] valueArray =  value.split(",");
                    maxLength = maxLength < valueArray.length ? valueArray.length : maxLength;
                    params.put(key,valueArray);
                }
            }
        }
        for (int a = 0; a < maxLength; a++) {
            Map<String, String> map = new HashMap<>();
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                map.put(key,getValue(values,a));
            }
            list.add(map);
        }
        return list;
    }
    /* 将文本转为 Param Map*/
    @NotNull
    public static List<String> textToParamJsonStringList(String text) {
        List<String> list = new ArrayList<>();
        //拼装param参数
        String paramText = text;//serviceInfo.getText();
        String[] lines = paramText.split("\n");
        //储存类似 id:1,2,3
        Map<String, String[]> params = new HashMap<>();
        //长度最长的那个文件
        int maxLength = 1;
        for (String line : lines) {
            //合法参数键值对 （非//开头，且包含 : ）
            if (!line.startsWith("//") && line.contains(":")) {
                String[] prop = line.split(":");
                if (prop.length < 2) {
                    continue;
                }
                String key = prop[0].trim();
                String value = prop[1].trim();
                String[] valueArray =  value.split(",");
                maxLength = maxLength < valueArray.length ? valueArray.length : maxLength;
                params.put(key,valueArray);
            }
        }
        JsonObject jsonObject = new JsonObject();
        for (int a = 0; a < maxLength; a++) {
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                jsonObject.addProperty(key,getValue(values,a));
            }
            list.add(jsonObject.toString());
        }
        return list;
    }
    public static String getValue(String[] array,int index){
        if (array.length > index) {
            return array[index];
        }else {
            return array[0];
        }
    }
}
