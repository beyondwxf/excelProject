package com.hexun.zh.datafilter.common.enums;


/**
 * 平台类型
 * Created by zhoudong on 2017/10/16.
 */
public enum UserTypeEnum {
    MEMBER_ID("member_id","会员ID"),
    UID("uid","用户ID"),
    MOBILE("mobil","手机号");

    private String code;
    private String msg;

    private UserTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static UserTypeEnum getByCode(String code) {
        for (UserTypeEnum ls : UserTypeEnum.values()) {
            if (ls.code.equals(code)) {
                return ls;
            }
        }
        return null;
    }

    public static UserTypeEnum getByMsg(String msg) {
        for (UserTypeEnum ls : UserTypeEnum.values()) {
            if (ls.msg.equalsIgnoreCase(msg)) {
                return ls;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean equals(String code) {
        return getCode().equals(code);
    }

}
