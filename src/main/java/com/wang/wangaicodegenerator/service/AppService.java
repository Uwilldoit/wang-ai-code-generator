package com.wang.wangaicodegenerator.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.wang.wangaicodegenerator.model.dto.app.AppQueryRequest;
import com.wang.wangaicodegenerator.model.entity.App;
import com.wang.wangaicodegenerator.model.entity.User;
import com.wang.wangaicodegenerator.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author Fugitive Mr.Wang
 */
public interface AppService extends IService<App> {

    /**
     * 根据应用查询请求构建查询条件包装器
     *
     * @param appQueryRequest 应用查询请求参数对象，包含各种查询条件
     * @return QueryWrapper 查询条件包装器，用于构建数据库查询条件
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    AppVO getAppVO(App app);

    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 聊天生成代码
     *
     * @param message     用户输入的提示信息
     * @param appId       应用ID
     * @param loginUser   登录用户
     * @return 生成的代码
     */
    Flux<String>chatToGenCode(String message, Long appId, User loginUser);
}
