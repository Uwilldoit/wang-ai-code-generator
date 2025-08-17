package com.wang.wangaicodegenerator.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.core.paginate.Page;
import com.wang.wangaicodegenerator.model.dto.chatHistory.ChatHistoryQueryRequest;
import com.wang.wangaicodegenerator.model.entity.ChatHistory;
import com.wang.wangaicodegenerator.model.entity.User;
import com.wang.wangaicodegenerator.model.vo.ChatHistoryVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对话历史 服务层。
 *
 * @author Fugitive Mr.Wang
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 分页查询应用的聊天历史记录
     *
     * @param appId          应用ID，不能为空且必须大于0
     * @param pageSize       页面大小，必须在1-50之间
     * @param lastCreateTime 最后创建时间，用于分页查询
     * @param loginUser      当前登录用户，不能为空
     * @return 分页的聊天历史记录
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    /**
     * 根据应用ID删除记录
     *
     * @param appId 应用ID，不能为空且必须大于0
     * @return 删除成功返回true，否则返回false
     */
    boolean deleteByAppId(Long appId);

    /**
     * 添加聊天消息
     *
     * @param appId       应用ID
     * @param message     消息内容
     * @param messageType 消息类型
     * @param userId      用户ID
     * @return 是否添加成功
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);




    /**
     * 根据应用查询请求构建查询条件包装器
     *
     * @param chatHistoryQueryRequest 对话历史查询请求参数对象，包含各种查询条件
     * @return QueryWrapper 查询条件包装器，用于构建数据库查询条件
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

}