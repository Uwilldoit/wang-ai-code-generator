package com.wang.wangaicodegenerator.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/15---00:42
 * @description:
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}

