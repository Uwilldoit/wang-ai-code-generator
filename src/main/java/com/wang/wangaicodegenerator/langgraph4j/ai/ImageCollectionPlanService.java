package com.wang.wangaicodegenerator.langgraph4j.ai;

import com.wang.wangaicodegenerator.langgraph4j.model.ImageCollectionPlan;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/10/22---18:21
 * @description:
 */

public interface ImageCollectionPlanService {

    /**
     * 根据用户提示词分析需要收集的图片类型和参数
     */
    @SystemMessage(fromResource = "prompt/image-collection-plan-system-prompt.txt")
    ImageCollectionPlan planImageCollection(@UserMessage String userPrompt);
}

