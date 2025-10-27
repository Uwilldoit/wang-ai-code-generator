package com.wang.wangaicodegenerator.langgraph4j.node;

import com.wang.wangaicodegenerator.langgraph4j.ai.ImageCollectionPlanService;
import com.wang.wangaicodegenerator.langgraph4j.ai.ImageCollectionService;
import com.wang.wangaicodegenerator.langgraph4j.model.ImageCollectionPlan;
import com.wang.wangaicodegenerator.langgraph4j.model.ImageResource;
import com.wang.wangaicodegenerator.langgraph4j.state.WorkflowContext;
import com.wang.wangaicodegenerator.langgraph4j.tools.ImageSearchTool;
import com.wang.wangaicodegenerator.langgraph4j.tools.LogoGeneratorTool;
import com.wang.wangaicodegenerator.langgraph4j.tools.MermaidDiagramTool;
import com.wang.wangaicodegenerator.langgraph4j.tools.UndrawIllustrationTool;
import com.wang.wangaicodegenerator.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/22---17:30
 * @description: 图片收集节点
 */

@Slf4j
public class ImageCollectorNode {

    public static AsyncNodeAction<MessagesState<String>> create() {
        return node_async(state -> {
            WorkflowContext context = WorkflowContext.getContext(state);
            String originalPrompt = context.getOriginalPrompt();
            List<ImageResource> imageList = new ArrayList<>();
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            try {
                //第一步：获取图片收集计划
                ImageCollectionPlanService planService = SpringContextUtil.getBean(ImageCollectionPlanService.class);
                ImageCollectionPlan plan = planService.planImageCollection(originalPrompt);
                log.info("获取图片收集计划,开始并发执行");
                //第二步：并发执行图片收集任务
                List<CompletableFuture<List<ImageResource>>> futures = new ArrayList<>();
                //并发执行内容图片搜索
                if (plan.getContentImageTasks() != null){
                    ImageSearchTool imageSearchTool = SpringContextUtil.getBean(ImageSearchTool.class);
                    for (ImageCollectionPlan.ImageSearchTask task : plan.getContentImageTasks()) {
                        futures.add(CompletableFuture.supplyAsync(() ->
                                        imageSearchTool.searchContentImages(task.query())));
                    }
                }
                //并发执行插画图片搜索
                if (plan.getIllustrationTasks() != null){
                    UndrawIllustrationTool illustrationTool = SpringContextUtil.getBean(UndrawIllustrationTool.class);
                    for (ImageCollectionPlan.IllustrationTask task : plan.getIllustrationTasks()) {
                        futures.add(CompletableFuture.supplyAsync(() ->
                                illustrationTool.searchIllustrations(task.query())));
                    }
                }
                //并发执行架构图片搜索
                if (plan.getDiagramTasks() != null){
                    MermaidDiagramTool mermaidDiagramTool = SpringContextUtil.getBean(MermaidDiagramTool.class);
                    for (ImageCollectionPlan.DiagramTask task : plan.getDiagramTasks()) {
                        futures.add(CompletableFuture.supplyAsync(() ->
                                mermaidDiagramTool.generateMermaidDiagram(task.mermaidCode(), task.description())));
                    }
                }
                //并发执行LOGO图片生成
                if (plan.getLogoTasks() != null){
                    LogoGeneratorTool logoGeneratorTool = SpringContextUtil.getBean(LogoGeneratorTool.class);
                    for (ImageCollectionPlan.LogoTask task : plan.getLogoTasks()) {
                        futures.add(CompletableFuture.supplyAsync(() ->
                                logoGeneratorTool.generateLogos(task.description())));
                    }
                }
                //等待所有任务完成并收集结果
                CompletableFuture<Void> allTasks = CompletableFuture
                        .allOf(futures.toArray(new CompletableFuture[0]));
                allTasks.join();
                //收集所有结果
                for (CompletableFuture<List<ImageResource>> future : futures) {
                    List<ImageResource> images = future.get();
                    if (images != null){
                        imageList.addAll(images);
                    }
                }
                stopWatch.stop();
                log.info("并发图片收集完成，共收集到{}张图片,共耗时{}ms",
                        imageList.size(),stopWatch.getTotalTimeMillis());
            } catch (Exception e) {
                log.error("图片收集失败: {}", e.getMessage(), e);
            }
            // 更新状态
            context.setCurrentStep("图片收集");
            context.setImageList(imageList);
            return WorkflowContext.saveContext(context);
        });
    }
}


