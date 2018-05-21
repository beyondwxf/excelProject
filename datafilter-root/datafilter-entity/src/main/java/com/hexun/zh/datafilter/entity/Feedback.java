package com.hexun.zh.datafilter.entity;

import java.util.Date;

public class Feedback {
    private String id;

    private Date feedbackTime;

    private String deviceName;

    private String platform;

    private String clientVersion;

    private String email;

    private String phone;

    private String feedbackType;

    private String memo;

    private String answerType;

    private String message;

    private String ip;

    private String ua;

    private String province;

    private String phoneOperator;

    private String installCannel;

    private String isHandle;

    private String operator;

    private Date createTime;

    private String extension;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion == null ? null : clientVersion.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType == null ? null : feedbackType.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType == null ? null : answerType.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua == null ? null : ua.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getPhoneOperator() {
        return phoneOperator;
    }

    public void setPhoneOperator(String phoneOperator) {
        this.phoneOperator = phoneOperator == null ? null : phoneOperator.trim();
    }

    public String getInstallCannel() {
        return installCannel;
    }

    public void setInstallCannel(String installCannel) {
        this.installCannel = installCannel == null ? null : installCannel.trim();
    }

    public String getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(String isHandle) {
        this.isHandle = isHandle == null ? null : isHandle.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension == null ? null : extension.trim();
    }
}