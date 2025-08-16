package com.wang.wangaicodegenerator.core.parser;

import com.wang.wangaicodegenerator.ai.model.HtmlCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/15---17:00
 * @description:
 */

public class HtmlCodeParser implements CodeParser<HtmlCodeResult> {
    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    /**
     * 解析代码内容
     *
     * @param codeContent 原始代码内容
     * @return 解析后的结果对象
     */
    @Override
    public HtmlCodeResult parseCode(String codeContent) {
        HtmlCodeResult result = new HtmlCodeResult();
        //提取HTML代码
        String htmlCode = extractHtmlCode(codeContent);
        if (htmlCode != null && !htmlCode.trim().isEmpty()){
            result.setHtmlCode(htmlCode.trim());
        }else{
            //如果没有找到，将整个内容作为HTML
            result.setHtmlCode(codeContent.trim());
        }
        return result;
    }


        /**
     * 从给定的代码内容中提取HTML代码片段
     *
     * @param codeContent 包含HTML代码的字符串内容
     * @return 提取出的HTML代码片段，如果未找到匹配内容则返回null
     */
    private String extractHtmlCode(String codeContent) {
        // 使用预定义的HTML代码模式匹配器查找代码内容中的HTML片段
        Matcher matcher = HTML_CODE_PATTERN.matcher(codeContent);
        if (matcher.find()) {
            // 返回匹配到的第一个分组内容（HTML代码片段）
            return matcher.group(1);
        }
        return null;
    }

}

