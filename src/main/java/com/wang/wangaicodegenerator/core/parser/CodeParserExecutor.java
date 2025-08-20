package com.wang.wangaicodegenerator.core.parser;

import com.wang.wangaicodegenerator.model.enums.CodeGenTypeEnum;
import com.wang.wangaicodegenerator.exception.BusinessException;
import com.wang.wangaicodegenerator.exception.ErrorCode;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/15---17:00
 * @description:
 */

public class CodeParserExecutor {

    /**
     * html代码解析器
     */
    private static final HtmlCodeParser HTML_CODE_PARSER = new HtmlCodeParser();
    /**
     * 多文件代码解析器
     */
    private static final MultiFileCodeParser MULTI_FILE_CODE_PARSER  = new MultiFileCodeParser();

    public static Object execute(String codeContent, CodeGenTypeEnum codeGenType)
    {
        return switch (codeGenType) {
            case HTML -> HTML_CODE_PARSER.parseCode(codeContent);
            case MULTI_FILE -> MULTI_FILE_CODE_PARSER.parseCode(codeContent);
        default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型："+codeGenType);
        };
    }
}
