package com.oc.entity;

import lombok.Data;

/**
 * @author SxL
 * @since 2.0.0
 * 2019-03-06 23:03
 */
@Data
public class DatabaseProperties {
    private String driver;

    private String url;

    private String username;

    private String password;
}
