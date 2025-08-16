package com.wang.wangaicodegenerator.core.saver;

import com.wang.wangaicodegenerator.ai.model.HtmlCodeResult;
import com.wang.wangaicodegenerator.ai.model.MultiFileCodeResult;
import com.wang.wangaicodegenerator.ai.model.enums.CodeGenTypeEnum;
import com.wang.wangaicodegenerator.exception.BusinessException;
import com.wang.wangaicodegenerator.exception.ErrorCode;

import java.io.File;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/16---09:57
 * @description:
 */

public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate HTML_CODE_FILE_SAVER = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate MULTI_FILE_CODE_FILE_SAVER = new MultiFileCodeFileSaverTemplate();

    /**
     * 执行代码生成操作
     * @param codeResult 代码生成结果对象，包含具体的代码内容
     * @param codeGenType 代码生成类型枚举值，决定代码的生成方式
     * @return 生成的代码文件对象
     */
    public static File execute(Object codeResult, CodeGenTypeEnum codeGenType,Long appId) {
        // 根据代码生成类型执行相应的代码解析和保存操作
        return switch (codeGenType){
            case HTML -> HTML_CODE_FILE_SAVER.saveCode((HtmlCodeResult) codeResult,appId);
            case MULTI_FILE -> MULTI_FILE_CODE_FILE_SAVER.saveCode((MultiFileCodeResult) codeResult,appId);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的代码生成类型："+codeGenType);
        };
    }


}
