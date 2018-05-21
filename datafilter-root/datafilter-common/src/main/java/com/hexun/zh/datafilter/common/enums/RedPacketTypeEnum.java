package com.hexun.zh.datafilter.common.enums;


/**
 * 红包类型
 * Created by zhoudong on 2017/10/16.
 */
public enum RedPacketTypeEnum {
    normal("normal","普通红包"),
    rondom("rondom","随机红包"),
    unique("unique","指定接收人红包");

    private String code;
    private String msg;

    private RedPacketTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static RedPacketTypeEnum getByCode(String code) {
        for (RedPacketTypeEnum ls : RedPacketTypeEnum.values()) {
            if (ls.code.equals(code)) {
                return ls;
            }
        }
        return null;
    }

    public static RedPacketTypeEnum getByMsg(String msg) {
        for (RedPacketTypeEnum ls : RedPacketTypeEnum.values()) {
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
