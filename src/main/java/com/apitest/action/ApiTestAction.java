package com.apitest.action;

import com.apitest.action.utils.RestServiceItem;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joe
 * @data 2019/6/27 21:39
 * @email joeluo520@gmail.com
 * https://yq.aliyun.com/articles/691224
 * https://www.jianshu.com/p/0117d4b1eb00
 * http://www.jetbrains.org/intellij/sdk/docs/welcome.html //官网
 */
public class ApiTestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        List<RestServiceItem> itemList = new ArrayList<>();

//        GlobalSearchScope globalSearchScope = GlobalSearchScope.moduleScope(itemList);




        //获取当前在操作的工程上下文
        Project project = e.getData(PlatformDataKeys.PROJECT);

        //获取当前操作的类文件
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        //获取当前类文件的路径
        String classPath = psiFile.getVirtualFile().getPath();
        String title = "Hello World!";

        //显示对话框
        Messages.showMessageDialog(project, classPath, title, Messages.getInformationIcon());
    }
}
