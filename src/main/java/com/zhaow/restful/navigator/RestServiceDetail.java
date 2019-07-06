package com.zhaow.restful.navigator;

import com.google.gson.*;
import com.intellij.openapi.editor.colors.FontPreferences;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.zhaow.restful.common.PsiClassHelper;
import com.zhaow.restful.common.RequestHelper;
import com.zhaow.restful.common.RequestTestHelper;
import com.zhaow.restful.common.RespondTestEntity;
import com.zhaow.utils.JsonUtils;
import com.zhaow.utils.ToolkitUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

//import com.intellij.openapi.editor.colors.impl.AppEditorFontOptions;
//import com.intellij.ui.components.JBPanelWithEmptyText;

public class RestServiceDetail extends JBPanel/*WithEmptyText*/{
    private static RestServiceDetail restServiceDetail;
/*
    JBTextField methodField = new JBTextField("GET");
    JBTextField urlField = new JBTextField("url url ");*/

    /**
     * 用 awt 重新定义，后期再改吧。
     */
//    public JPanel this;// = new JBPanelWithEmptyText();
    public JTextField urlField;
    public JPanel urlPanel;
    public JTextField methodField;
    public JButton sendButton;
    public JTabbedPane requestTabbedPane;
    public JButton testButton;

    public JTextArea requestParamsTextArea;
    public JTextArea requestTestParamsTextArea;

    public JTextArea requestBodyTextArea;
    public JTextArea requestTestBodyTextArea;
    public JTextArea responseTextArea;

/*    public static RestServiceDetail getInstance() {
        if (restServiceDetail == null) {
            restServiceDetail =  new RestServiceDetail();
        }
        return restServiceDetail;
    }*/

    public static RestServiceDetail getInstance(Project p) {
        return p.getComponent(RestServiceDetail.class);
    }


    private RestServiceDetail() {
        super();
//        withEmptyText("JSON FORMAT");
        initComponent();
    }

    public void initComponent() {
        initUI();
        initActions();
        initTab();
    }

    private void initActions() {
//        bindMouseEvent(servicesTree);
        bindSendButtonActionListener();
        bindTestButtonActionListener();
        bindUrlTextActionListener();
        bindUrlTestTextActionListener();
    }

    public void initTab() {
//        jTextArea.setAutoscrolls(true);
        String jsonFormat = "Try press 'Ctrl(Cmd) Enter'";
        JTextArea textArea = createTextArea("{'key':'value'}");

        addRequestTabbedPane(jsonFormat, textArea);
    }

    @Override
    protected void printComponent(Graphics g) {
        super.printComponent(g);
    }

    private void initUI() {
//        this#urlPanel
//        urlField = new JBTextField();
//        urlField.setColumns(20);
        urlField.setAutoscrolls(true);


//        urlPanel.setLayout(new HorizontalLayout());

//        urlPanel = new JBPanelWithEmptyText();
        urlPanel = new JBPanel();

        GridLayoutManager mgr = new GridLayoutManager(1, 4);
//        GridLayoutManager mgr = new GridLayoutManager(1, 2);
        mgr.setHGap(1);
        mgr.setVGap(1);
        urlPanel.setLayout(mgr);
//        urlPanel.setLayout(new HorizontalLayout());

        urlPanel.add(methodField,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTHEAST, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                        null, null, null));
        urlPanel.add(urlField,
                new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_SOUTHEAST, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));
// 是否必要保留？
        urlPanel.add(sendButton,
                new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_SOUTHEAST, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                        null, null, null));

        urlPanel.add(testButton,
                new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_SOUTHEAST, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                        null, null, null));

        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new GridLayoutManager(3, 2));

        this.add(urlPanel,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED,
                        null, null, null));
        this.add(requestTabbedPane,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));
    }

    /**
     * 测试按钮绑定
     */
    private void bindTestButtonActionListener() {
        testButton.addActionListener(e -> {
            // PluginManagerMain
            ProgressManager.getInstance().run(new Task.Backgroundable(null,"Sending1111 Request") {
                @Override
                public void run(@NotNull ProgressIndicator indicator) {
                    final Runnable runnable = () -> {
                        String baseUrl = urlField.getText();
                        //如果不是以http开头的请求 无须理会
                        if(!baseUrl.startsWith("http")){
                            return;
                        }
                        List<String> urlArray = new ArrayList<>();
                        if (requestParamsTextArea != null) {
                            String requestParamsText = requestParamsTextArea.getText();
                            List<Map<String, String>> paramMapList = ToolkitUtil.textToParamMapList(requestParamsText);
                            for (Map<String, String> paramMap : paramMapList) {
                                String temp = baseUrl;
                                String params = ToolkitUtil.textToRequestParam(requestParamsText);
                                if (paramMap != null && paramMap.size() > 0) {
                                    for (String key : paramMap.keySet()) {
                                        temp = temp.replaceFirst("\\{("+key+"[\\s\\S]*?)\\}",paramMap.get(key));
                                    }
                                }
                                if (params.length() != 0) {
                                    // 包含了参数
                                    if (temp.contains("?")) {
                                        temp += "&" + params;
                                    } else {
                                        temp += "?" + params;
                                    }
                                }
                                urlArray.add(temp);
                            }
                        }
                        // 完整url
                        String method = methodField.getText();
                       List<RespondTestEntity> respondTestEntities = new ArrayList<>();
                        if (requestTestBodyTextArea != null && StringUtils.isNotBlank(requestTestBodyTextArea.getText())) {
                            String bodys = requestTestBodyTextArea.getText();
                            List<String> jsonList =  ToolkitUtil.textToParamJsonStringList(bodys);
                            for (int a =0;a<jsonList.size();a++) {
                                RespondTestEntity response = RequestTestHelper.postRequestBodyWithJson(urlArray.get(0), jsonList.get(a));
                                respondTestEntities.add(response);
                            }
                        }else{
                            for (int a =0;a<urlArray.size();a++) {
                                RespondTestEntity response =  RequestTestHelper.request(urlArray.get(a), method);
                                respondTestEntities.add(response);
                            }
                        }
                        Collections.sort(respondTestEntities);
                        addResponseTabPanel(respondTestEntities.toString());

                    };
                    runnable.run();
                }
            });

        });
    }
    private void bindSendButtonActionListener() {
        sendButton.addActionListener(e -> {
            // PluginManagerMain
            ProgressManager.getInstance().run(new Task.Backgroundable(null,"Sending Request") {
                @Override
                public void run(@NotNull ProgressIndicator indicator) {
                    final Runnable runnable = () -> {
                        String url = urlField.getText();
                        //如果不是以http开头的请求 无须理会
                        if(!url.startsWith("http")){
                            return;
                        }
                        if (requestParamsTextArea != null) {
                            String requestParamsText = requestParamsTextArea.getText();
                            Map<String, String> paramMap = ToolkitUtil.textToParamMap(requestParamsText);
                            if (paramMap != null && paramMap.size() > 0) {
                                // set PathVariable value to request URI
                                for (String key : paramMap.keySet()) {
                                    url = url.replaceFirst("\\{("+key+"[\\s\\S]*?)\\}",paramMap.get(key));
                                }
                            }

                            String params = ToolkitUtil.textToRequestParam(requestParamsText);
                            if (params.length() != 0) {
                                // 包含了参数
                                if (url.contains("?")) {
                                    url += "&" + params;
                                } else {
                                    url += "?" + params;
                                }
                            }
                        }

                        // 完整url
                        String method = methodField.getText();
                        String responseText = url;

                        String response;
                        if (requestBodyTextArea != null && StringUtils.isNotBlank(requestBodyTextArea.getText())) {
                            response = RequestHelper.postRequestBodyWithJson(url, requestBodyTextArea.getText());
                        }else{
                            response = RequestHelper.request(url, method);
                        }

                       /* else if (method.equalsIgnoreCase("post")) {
//                response = HttpClientHelper.post(url);
                            response = RequestHelper.post(url);
                        } else {
                            response = RequestHelper.get(url);
                        }*/

                        if (response != null) responseText = response;

                        addResponseTabPanel(responseText);

                    };
                    runnable.run();
                }
            });

        });
    }

    private void bindUrlTestTextActionListener() {
         requestTabbedPane.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 System.out.println(e.getClickCount());
                 super.mouseClicked(e);
//                urlField.moveCaretPosition(urlField.getDocument().getLength());
//                urlField.select(0,0);
             }

             @Override
             public void mousePressed(MouseEvent e) {
                 super.mousePressed(e);
                urlField.selectAll();
             }
             @Override
             public void mouseEntered(MouseEvent e) {
                 super.mousePressed(e);
                 urlField.selectAll();
             }
             @Override
             public void mouseMoved(MouseEvent e) {
                 super.mousePressed(e);
                 urlField.selectAll();
             }
         });


        /*urlField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getClickCount());
                super.mouseClicked(e);
//                urlField.moveCaretPosition(urlField.getDocument().getLength());
//                urlField.select(0,0);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
//                urlField.selectAll();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mousePressed(e);
                urlField.selectAll();
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mousePressed(e);
                urlField.selectAll();
            }
        });*/

        methodField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                methodField.selectAll();
            }
        });

    }
    private void bindUrlTextActionListener() {
         requestTabbedPane.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 System.out.println(e.getClickCount());
                 super.mouseClicked(e);
//                urlField.moveCaretPosition(urlField.getDocument().getLength());
//                urlField.select(0,0);
             }

             @Override
             public void mousePressed(MouseEvent e) {
                 super.mousePressed(e);
                urlField.selectAll();
             }
             @Override
             public void mouseEntered(MouseEvent e) {
                 super.mousePressed(e);
                 urlField.selectAll();
             }
             @Override
             public void mouseMoved(MouseEvent e) {
                 super.mousePressed(e);
                 urlField.selectAll();
             }
         });


        /*urlField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getClickCount());
                super.mouseClicked(e);
//                urlField.moveCaretPosition(urlField.getDocument().getLength());
//                urlField.select(0,0);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
//                urlField.selectAll();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mousePressed(e);
                urlField.selectAll();
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mousePressed(e);
                urlField.selectAll();
            }
        });*/

        methodField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                methodField.selectAll();
            }
        });

    }


    public void addRequestParamsTab(String requestParams) {

        StringBuilder paramBuilder = new StringBuilder();

        if (StringUtils.isNotBlank(requestParams)) {
            String[] paramArray = requestParams.split("&");
            for (String paramPairStr : paramArray) {
                String[] paramPair = paramPairStr.split("=");

                String param = paramPair[0];
                String value = paramPairStr.substring(param.length() + 1);
                paramBuilder.append(param).append(" : ").append(value).append("\n");
            }
        }

        if (requestParamsTextArea == null){
            requestParamsTextArea = createTextArea(paramBuilder.toString());
        }
        else {
            requestParamsTextArea.setText(paramBuilder.toString());
//            addRequestTabbedPane("RequestParams", requestParamsTextArea);
        }

        highlightTextAreaData(requestParamsTextArea);

        addRequestTabbedPane("RequestParams", requestParamsTextArea);

    }


    public void addRequestBodyTabPanel(String text) {
//        jTextArea.setAutoscrolls(true);
        String reqBodyTitle = "RequestBody";
        if (requestBodyTextArea == null){
            requestBodyTextArea = createTextArea(text);
        }
        else {
            requestBodyTextArea.setText(text);
        }

        highlightTextAreaData(requestBodyTextArea);

        addRequestTabbedPane(reqBodyTitle, this.requestBodyTextArea);
    }

    public void addRequestTestParamsTab(String requestParams) {

        StringBuilder paramBuilder = new StringBuilder();

        if (StringUtils.isNotBlank(requestParams)) {
            String[] paramArray = requestParams.split("&");
            for (String paramPairStr : paramArray) {
                String[] paramPair = paramPairStr.split("=");

                String param = paramPair[0];
                String value = paramPairStr.substring(param.length() + 1);
                paramBuilder.append(param).append(" : ").append(PsiClassHelper.getTestParamValue(param,value)).append("\n");
            }
        }

        if (requestTestParamsTextArea == null){
            requestTestParamsTextArea = createTextArea(paramBuilder.toString());
        }
        else {
            requestTestParamsTextArea.setText(paramBuilder.toString());
//            addRequestTabbedPane("RequestParams", requestParamsTextArea);
        }

        highlightTextAreaData(requestTestParamsTextArea);

        addRequestTabbedPane("RequestTestParams", requestTestParamsTextArea);

    }
    public void addTestRequestBodyTabPanel(String text) {
        StringBuilder paramBuilder = new StringBuilder();

        if (StringUtils.isNotBlank(text)) {
            JsonObject jsonObject = new Gson().fromJson(text,JsonObject.class);
            for (String paramPairStr : jsonObject.keySet()) {
                String param = paramPairStr;
                String value = jsonObject.get(paramPairStr).getAsString();
                paramBuilder.append(param).append(" : ").append(PsiClassHelper.getTestParamValue(param,value)).append("\n");
            }
        }

        if (requestTestBodyTextArea == null){
            requestTestBodyTextArea = createTextArea(paramBuilder.toString());
        }
        else {
            requestTestBodyTextArea.setText(paramBuilder.toString());
//            addRequestTabbedPane("RequestParams", requestParamsTextArea);
        }

        highlightTextAreaData(requestTestBodyTextArea);

        addRequestTabbedPane("TestRequestBody", requestTestBodyTextArea);


    }


    public void addRequestTabbedPane(String title, JTextArea jTextArea) {

        JScrollPane jbScrollPane = new JBScrollPane(jTextArea, JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jTextArea.addKeyListener(new TextAreaKeyAdapter(jTextArea));

        requestTabbedPane.addTab(title, jbScrollPane) ;

        requestTabbedPane.setSelectedComponent(jbScrollPane) ;//.setSelectedIndex(requestTabbedPane.getTabCount() - 1);
    }

    /*添加 Response Tab*/
    public void addResponseTabPanel(String text) {

        String responseTabTitle = "Response";
        if (responseTextArea == null) {
            responseTextArea = createTextArea(text);
            addRequestTabbedPane(responseTabTitle, responseTextArea);
        }
        else {
            Component componentAt = null;
            responseTextArea.setText(text);
            int tabCount = requestTabbedPane.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                if (requestTabbedPane.getTitleAt(i).equals(responseTabTitle)) {
                    componentAt = requestTabbedPane.getComponentAt(i);
                    requestTabbedPane.addTab(responseTabTitle,componentAt);
                    requestTabbedPane.setSelectedComponent(componentAt);
                    break;
//                    Component component = requestTabbedPane.getComponent(i);
                }
            }
            if (componentAt == null) {
                addRequestTabbedPane(responseTabTitle, responseTextArea);
            }

        }

    }

    @NotNull
    public JTextArea createTextArea(String text) {
        Font font = getEffectiveFont();

        // TODO : 适当时候替换，展现效果更好
//        JTextPane editor = new JTextPane();
        JTextArea jTextArea = new JTextArea(text);
        jTextArea.setFont(font);

        jTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String text = jTextArea.getText();
                getEffectiveFont(text);
                highlightTextAreaData(jTextArea);
            }
        });

        jTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    // copy text to parse
                    CopyPasteManager.getInstance().setContents(new StringSelection(jTextArea.getText()));
                }
            }
        });

        highlightTextAreaData(jTextArea);

        return jTextArea;
    }

    @NotNull  // editor.xml
    private Font getEffectiveFont(String text) {
         FontPreferences fontPreferences = this.getFontPreferences();

        List<String> effectiveFontFamilies = fontPreferences.getEffectiveFontFamilies();

        int size = fontPreferences.getSize(fontPreferences.getFontFamily());
//        Font font=new Font(FontPreferences.FALLBACK_FONT_FAMILY,Font.PLAIN,size);
        Font font=new Font(FontPreferences.DEFAULT_FONT_NAME,Font.PLAIN,size);
//        String fallbackFontFamily = getFallbackName(fontFamily, size, null);
        //有效字体
        for (String effectiveFontFamily : effectiveFontFamilies) {
            Font effectiveFont = new Font(effectiveFontFamily, Font.PLAIN, size);
            if (effectiveFont.canDisplayUpTo(text) == -1) {
                font = effectiveFont;
                break;
            }
        }
        return font;
    }

    @NotNull
    private final FontPreferences getFontPreferences() {
        //        FontPreferences fontPreferences = AppEditorFontOptions.getInstance().getFontPreferences();
        return new FontPreferences();
    }

    @NotNull
    private Font getEffectiveFont() {
        //        UIUtil.getEditorPaneBackground()
        //editor default font
        FontPreferences fontPreferences = this.getFontPreferences();
        String fontFamily = fontPreferences.getFontFamily();
        int size = fontPreferences.getSize(fontFamily);
        return new Font("Monospaced",Font.PLAIN,size);
    }


    public void resetRequestTabbedPane() {
        this.requestTabbedPane.removeAll();
        resetTextComponent(requestParamsTextArea);
        resetTextComponent(requestBodyTextArea);
        resetTextComponent(responseTextArea);
    }

    private void resetTextComponent(JTextArea textComponent) {
        if( textComponent != null && StringUtils.isNotBlank(textComponent.getText())) {
            textComponent.setText("");
        }
    }

    public void setMethodValue(String method) {
        methodField.setText(String.valueOf(method));
    }

    public void setUrlValue(String url) {
        urlField.setText(url);
    }

    private class TextAreaKeyAdapter extends KeyAdapter {
        private final JTextArea jTextArea;

        public TextAreaKeyAdapter(JTextArea jTextArea) {
            this.jTextArea = jTextArea;
        }

        @Override
        public void keyPressed(KeyEvent event) {
            super.keyPressed(event);

            // 组合键ctrl+enter自定义，当Ctrl (Command on Mac)+Enter组合键按下时响应
            if ((event.getKeyCode() == KeyEvent.VK_ENTER)
                    && (event.isControlDown() || event.isMetaDown()) ) {

                //解析，格式化json
                String oldValue = jTextArea.getText();
                if (!JsonUtils.isValidJson(oldValue)) {
                    return;
                }

                JsonParser parser = new JsonParser();
                JsonElement parse = parser.parse(oldValue);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(parse);
                jTextArea.setText(json);
            }
        }
    }


    /* 高亮 */
    public void highlightTextAreaData(JTextArea jTextArea) {
//        JTextAreaHighlight.highlightTextAreaData(jTextArea);
    }

}