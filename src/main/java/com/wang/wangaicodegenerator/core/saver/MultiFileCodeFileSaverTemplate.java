package com.wang.wangaicodegenerator.core.saver;

import cn.hutool.core.util.StrUtil;
import com.wang.wangaicodegenerator.ai.model.MultiFileCodeResult;
import com.wang.wangaicodegenerator.model.enums.CodeGenTypeEnum;
import com.wang.wangaicodegenerator.exception.BusinessException;
import com.wang.wangaicodegenerator.exception.ErrorCode;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/16---09:57
 * @description:
 */

public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult>{

    /**
     * 验证输入参数的有效性（可由子类覆盖）
     *
     * @param result 待验证的输入对象，不能为null
     * @throws BusinessException 当输入对象为null时抛出业务异常
     */
    @Override
    protected void validateInput(MultiFileCodeResult result) {
        super.validateInput(result);
        //至少要有HTML代码，CSS和JS可以为空
        if (StrUtil.isBlank(result.getHtmlCode())){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"HTML代码内容不能为空");
        }
    }

    /**
     * 获取代码类型（由子类实现）
     *
     * @return CodeGenTypeEnum 代码类型枚举对象
     */
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

        /**
     * 保存文件的具体实现（由子类实现）
     *
     * @param result      要保存的代码结果对象
     * @param baseDirPath 保存代码的根目录路径
     */
    @Override
    protected void savesFiles(MultiFileCodeResult result, String baseDirPath) {
        // 保存HTML文件
        writeToFile(baseDirPath, result.getHtmlCode(), "index.html");
        // 保存CSS文件
        writeToFile(baseDirPath, result.getCssCode(), "style.css");
        // 保存JavaScript文件
        writeToFile(baseDirPath, result.getJsCode(), "script.js");
    }

}
