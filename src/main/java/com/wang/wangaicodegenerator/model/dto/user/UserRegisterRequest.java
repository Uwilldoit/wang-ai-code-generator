package com.wang.wangaicodegenerator.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Shajia Wang
 * @createTime: 2025/8/15---00:24
 * @description:
 */

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
}

