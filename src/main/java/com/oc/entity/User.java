package com.oc.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author wyj
 * @date 2018/11/22
 */
@Data
public class User {
    private Integer id;

    private String accountId;

    private String account;

    private Integer flag;

    private String password;

    private String salt;

    private String client;

    private String name;

    private Integer sex;

    private String idCard;

    private String comments;

    private Date createdAt;

    private Date updatedAt;

    private String userSn;

    private String mailFrom;

    private String certStyle;

    private String department;
}
