package com.wang.wangaicodegenerator.ai.core;

import com.wang.wangaicodegenerator.ai.model.HtmlCodeResult;
import com.wang.wangaicodegenerator.ai.model.MultiFileCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/15---12:09
 * @description:
 */

public class CodeParser {

    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);
    private static final Pattern CSS_CODE_PATTERN = Pattern.compile("```css\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);
    private static final Pattern JS_CODE_PATTERN = Pattern.compile("```(?:js|javascript)\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    /**
     * 解析HTML代码内容，提取其中的HTML代码块或使用全部内容作为HTML代码
     *
     * @param codeContent 包含HTML代码的字符串内容
     * @return HtmlCodeResult对象，包含解析后的HTML代码
     */
    public static HtmlCodeResult parseHtmlCode(String codeContent) {
        HtmlCodeResult result = new HtmlCodeResult();
        //提取HTML代码
        String htmlCode = extractHtmlCode(codeContent);
        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            result.setHtmlCode(htmlCode.trim());
        } else {
            //如果没有找到代码块，将整个内容作为HTML
            result.setHtmlCode(codeContent.trim());
        }
        return result;
    }

    /**
     * 解析多文件代码（HTML + CSS + JS）
     */
    public static MultiFileCodeResult parseMultiFileCode(String codeContent) {
        MultiFileCodeResult result = new MultiFileCodeResult();
        //提取各类代码
        String htmlCode = extractCodeByPattern(codeContent,HTML_CODE_PATTERN);
        String cssCode = extractCodeByPattern(codeContent,CSS_CODE_PATTERN);
        String jsCode = extractCodeByPattern(codeContent,JS_CODE_PATTERN);

        //设置Html代码
        if (htmlCode != null && !htmlCode.trim().isEmpty()){
            result.setHtmlCode(htmlCode.trim());
        }

        //设置CSS代码
        if (cssCode != null && !cssCode.trim().isEmpty()){
            result.setCssCode(cssCode.trim());
        }

        //设置JS代码
        if (jsCode != null && !jsCode.trim().isEmpty()){
            result.setJsCode(jsCode.trim());
        }
        return result;
    }

    private static String extractCodeByPattern(String codeContent, Pattern jsCodePattern) {
        Matcher matcher = jsCodePattern.matcher(codeContent);
        if (matcher.find()){
            return matcher.group(1);
        }
        return null;
    }


    /**
     * 从给定内容中提取HTML代码
     *
     * @param content 要从中提取HTML代码的字符串内容
     * @return 提取出的HTML代码，如果未找到则返回null
     */
    private static String extractHtmlCode(String content) {
        // 使用预定义的HTML代码模式匹配器查找内容中的HTML代码
        Matcher matcher = HTML_CODE_PATTERN.matcher(content);
        // 如果找到匹配项，则返回第一个捕获组的内容
        if (matcher.find()) {
            return matcher.group(1);
        }
        // 未找到匹配项时返回null
        return null;
    }

}
