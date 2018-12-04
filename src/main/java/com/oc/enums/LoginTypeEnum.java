package com.oc.enums;

/**
 * @author wyj
 * @date 2018/12/4
 */
public enum LoginTypeEnum {
    /**
     * 认证类型
     */
    SIMPLE(1,"simple"),
    CA(2,"certificationAuthority"),
    FINGERPRINT(3,"fingerprint"),
    FACE(4,"face");

    private int stateCode;

    private String msg;

    public int getStateCode() {
        return stateCode;
    }

    public String getMsg() {
        return msg;
    }

    LoginTypeEnum(int stateCode, String msg) {
        this.stateCode = stateCode;
        this.msg = msg;
    }
}
