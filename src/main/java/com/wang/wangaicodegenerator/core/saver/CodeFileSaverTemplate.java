package com.wang.wangaicodegenerator.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.wang.wangaicodegenerator.ai.model.enums.CodeGenTypeEnum;
import com.wang.wangaicodegenerator.exception.BusinessException;
import com.wang.wangaicodegenerator.exception.ErrorCode;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/16---09:56
 * @description:
 */

public abstract class CodeFileSaverTemplate<T> {
    /**
     * 文件保存根目录
     */
    protected static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 模板方法：保存代码的标准流程
     * @param result 要保存的代码结果对象
     * @return 保存代码的目录文件对象
     */
    public final File saveCode(T result,Long appId) {
        // 1、验证输入参数的有效性
        validateInput(result);
        // 2、构建唯一的目录路径
        String baseDirPath = buildUniqueDir(appId);
        // 3、保存文件到指定目录
        savesFiles(result, baseDirPath);
        // 4、返回目录文件的对象
        return new File(baseDirPath);
    }


    /**
     * 构建唯一的目录路径
     * <p>
     * 该方法通过组合代码类型和雪花算法生成的唯一ID来创建一个唯一的目录名称，
     * 并在指定的根目录下创建该目录，最后返回完整的目录路径。
     * </p>
     *
     * @return String 返回创建的唯一目录的完整路径
     */
    protected final String buildUniqueDir(Long appId) {
        if (appId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"appId不能为空");
        }
        // 获取代码类型值
        String codeType = getCodeType().getValue();
        // 使用代码类型和雪花ID生成唯一目录名称
        String uniqueDirName = StrUtil.format("{}_{}", codeType, IdUtil.getSnowflakeNextIdStr());
        // 构建完整目录路径
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        // 创建目录
        FileUtil.mkdir(dirPath);
        return dirPath;
    }


    /**
     * 验证输入参数的有效性（可由子类覆盖）
     *
     * @param result 待验证的输入对象，不能为null
     * @throws BusinessException 当输入对象为null时抛出业务异常
     */
    protected void validateInput(T result) {
        // 验证输入对象是否为空
        if (result == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"代码结果对象不能为空");
        }
    }


    /**
     * 写入单个文件的工具方法
     *
     * @param dirPath 文件目录路径（未使用）
     * @param content 要写入的文件内容
     * @param fileName 文件名称
     */
    protected final void writeToFile(String dirPath, String content,String fileName){
        // 只有当内容不为空时才执行写入操作
        if (StrUtil.isNotBlank(content)){
            String filePath = dirPath + File.separator + fileName;
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }

    }


    /**
     * 保存文件的具体实现（由子类实现）
     * @param result 要保存的代码结果对象
     * @param baseDirPath 保存代码的根目录路径
     */
    protected abstract void savesFiles(T result, String baseDirPath);

    /**
     * 获取代码类型（由子类实现）
     * @return CodeGenTypeEnum 代码类型枚举对象
     */
    protected abstract CodeGenTypeEnum getCodeType();
}
