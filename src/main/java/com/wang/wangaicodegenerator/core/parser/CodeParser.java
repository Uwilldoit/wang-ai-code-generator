package com.wang.wangaicodegenerator.core.parser;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/15---16:58
 * @description:
 */

public interface CodeParser<T> {
    /**
     * 解析代码内容
     *
     * @param codeContent 原始代码内容
     * @return 解析后的结果对象
     */
    T parseCode(String codeContent);

}
