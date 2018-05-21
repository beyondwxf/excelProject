package com.hexun.zh.datafilter.common.enums;

/**
 * 平台类型
 * Created by zhoudong on 2017/10/16.
 */
public enum PlatformTypeEnum {
    COMMUNITY_PLATFORM("COMMUNITY_PLATFORM","社区平台");

    private String code;
    private String msg;

    private PlatformTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static PlatformTypeEnum getByCode(String code) {
        for (PlatformTypeEnum ls : PlatformTypeEnum.values()) {
            if (ls.code.equals(code)) {
                return ls;
            }
        }
        return null;
    }

    public static PlatformTypeEnum getByMsg(String msg) {
        for (PlatformTypeEnum ls : PlatformTypeEnum.values()) {
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
