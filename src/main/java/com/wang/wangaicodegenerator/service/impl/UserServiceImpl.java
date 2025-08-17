package com.wang.wangaicodegenerator.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;


import com.wang.wangaicodegenerator.exception.BusinessException;
import com.wang.wangaicodegenerator.exception.ErrorCode;
import com.wang.wangaicodegenerator.mapper.UserMapper;
import com.wang.wangaicodegenerator.model.dto.user.UserQueryRequest;
import com.wang.wangaicodegenerator.model.entity.User;
import com.wang.wangaicodegenerator.model.enums.UserRoleEnum;
import com.wang.wangaicodegenerator.model.vo.LoginUserVO;
import com.wang.wangaicodegenerator.model.vo.UserVO;
import com.wang.wangaicodegenerator.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wang.wangaicodegenerator.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户 服务层实现。
 *
 * @author Fugitive Mr.Wang
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2. 检查是否重复
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.mapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 3. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("用户昵称"+ RandomUtil.randomString(5));
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }


    /**
     * 用户登录功能
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request      HTTP请求对象，用于记录登录状态
     * @return LoginUserVO 脱敏后的用户信息对象
     * @throws BusinessException 当参数校验失败或用户认证失败时抛出异常
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        // 2. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 查询用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.mapper.selectOneByQuery(queryWrapper);
        // 用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }

        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);

        // 4. 获得脱敏后的用户信息
        return this.getLoginUserVO(user);
    }


    /**
     * 获取当前登录用户信息
     *
     * @param request HTTP请求对象，用于获取用户会话信息
     * @return User 当前登录的用户对象
     * @throws BusinessException 当用户未登录或用户不存在时抛出业务异常
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接返回上述结果）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }


    /**
     * 用户登出功能
     *
     * @param request HTTP请求对象，用于获取用户会话信息
     * @return boolean 登出成功返回true
     * @throws BusinessException 当用户未登录时抛出业务异常
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 检查用户登录状态
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 清除用户登录状态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }


    /**
     * 对用户密码进行加密处理
     *
     * @param userPassword 用户输入的原始密码
     * @return 加密后的密码字符串
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "Wang";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }


    /**
     * 将User对象转换为LoginUserVO对象
     *
     * @param user 用户对象，包含用户的基本信息
     * @return LoginUserVO 登录用户VO对象，如果传入的user为null则返回null
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        // 创建LoginUserVO对象并复制user属性
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }


    /**
     * 将User对象转换为UserVO对象
     *
     * @param user 用户实体对象
     * @return 转换后的用户VO对象，如果输入为null则返回null
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        // 复制用户属性到VO对象
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }


    /**
     * 将用户实体列表转换为用户VO列表
     *
     * @param userList 用户实体列表
     * @return 用户VO列表，如果输入列表为空则返回空列表
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        // 如果用户列表为空，返回空的ArrayList
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        // 使用Stream API将用户实体列表映射为用户VO列表
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }


    /**
     * 根据用户查询请求构建查询条件包装器
     *
     * @param userQueryRequest 用户查询请求参数对象，包含各种查询条件
     * @return QueryWrapper 查询条件包装器，用于构建数据库查询条件
     * @throws BusinessException 当请求参数为空时抛出业务异常
     */
    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        // 从请求参数中提取查询条件
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        // 构建查询条件包装器，包含等于查询和模糊查询条件
        return QueryWrapper.create()
                .eq("id", id)
                .eq("userRole", userRole)
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }


}
