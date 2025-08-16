package com.wang.wangaicodegenerator.core.saver;

import cn.hutool.core.util.StrUtil;
import com.wang.wangaicodegenerator.ai.model.HtmlCodeResult;
import com.wang.wangaicodegenerator.ai.model.enums.CodeGenTypeEnum;
import com.wang.wangaicodegenerator.exception.BusinessException;
import com.wang.wangaicodegenerator.exception.ErrorCode;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/16---09:57
 * @description:
 */

public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult>{

    /**
     * 验证输入参数的有效性（可由子类覆盖）
     *
     * @param result 待验证的输入对象，不能为null
     * @throws BusinessException 当输入对象为null时抛出业务异常
     */
    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        //HTML代码不能为空
        if (StrUtil.isBlank(result.getHtmlCode())){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"HTML代码内容不能为空");
        }
    }

    /**
     * 保存文件的具体实现（由子类实现）
     *
     * @param result      要保存的代码结果对象
     * @param baseDirPath 保存代码的根目录路径
     */
    @Override
    protected void savesFiles(HtmlCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath,result.getHtmlCode(),"index.html");
    }

    /**
     * 获取代码类型（由子类实现）
     *
     * @return CodeGenTypeEnum 代码类型枚举对象
     */
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }
}
