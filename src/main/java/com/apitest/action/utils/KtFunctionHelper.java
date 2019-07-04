package com.apitest.action.utils;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.asJava.LightClassUtilsKt;
import org.jetbrains.kotlin.psi.KtNamedFunction;

import java.util.List;
import java.util.Map;

/**
 * @author joe
 * @data 2019/6/27 20:59
 * @email joeluo520@gmail.com
 */
public class KtFunctionHelper extends PsiMethodHelper {
    KtNamedFunction ktNamedFunction;
    Project myProject;
    Module myModule;

    private String pathSeparator= "/";

    public static KtFunctionHelper create(@NotNull KtNamedFunction psiMethod) {
        return new KtFunctionHelper(psiMethod);
    }

    public KtFunctionHelper withModule(Module module) {
        this.myModule = module;
        return this;
    }

    protected KtFunctionHelper(@NotNull KtNamedFunction ktNamedFunction) {
        super(null);
        List<PsiMethod> psiMethods = LightClassUtilsKt.toLightMethods(ktNamedFunction);
        PsiMethod psiMethod = psiMethods.get(0);
        super.psiMethod = psiMethod;
        this.ktNamedFunction = ktNamedFunction;
    }

    @NotNull
    protected Project getProject() {
        myProject =  psiMethod.getProject();
        return myProject;
    }

    /**
     * 构建URL参数 key value
     * @return
     */
    public String buildParamString() {

//        boolean matchedGet = matchGetMethod();
        // 没指定method 标示支持所有method

        StringBuilder param = new StringBuilder("");
        Map<String, Object> baseTypeParamMap = getBaseTypeParameterMap();

        if (baseTypeParamMap != null && baseTypeParamMap.size() > 0) {
            baseTypeParamMap.forEach((s, o) -> param.append(s).append("=").append(o).append("&"));
        }

        return param.length() >0 ? param.deleteCharAt(param.length()-1).toString() : "";
    }
}
