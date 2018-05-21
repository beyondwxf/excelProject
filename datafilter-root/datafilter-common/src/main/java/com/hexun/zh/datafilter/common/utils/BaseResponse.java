package com.hexun.zh.datafilter.common.utils;

import org.codehaus.jackson.map.annotate.JsonSerialize;


/**
 * 网关请求主返回体
 * @author zhoudong
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BaseResponse {
	private String respCode;   	//返回码
	private String respMgs;		//返回信息
	private String respData;	//返回信息
	private String errorMgs;	//错误信息
	private String errorData;	//错误信息
	private Object result;		//返回结果
	private String charset = "utf-8";		//编码
	
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMgs() {
		return respMgs;
	}
	public void setRespMgs(String respMgs) {
		this.respMgs = respMgs;
	}
	public String getErrorMgs() {
		return errorMgs;
	}
	public void setErrorMgs(String errorMgs) {
		this.errorMgs = errorMgs;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getRespData() {
		return respData;
	}
	public void setRespData(String respData) {
		this.respData = respData;
	}
	public String getErrorData() {
		return errorData;
	}
	public void setErrorData(String errorData) {
		this.errorData = errorData;
	}
	
	public static BaseResponse getSuccessByEnCode(String errMsg){
		BaseResponse resp = new BaseResponse();
		resp.setRespCode(String.valueOf(RespEnum.RESP_FAIL.getEnCode()));
		resp.setRespData(errMsg);
		
		return resp;
	}

	public static BaseResponse getSuccessByEnCode(Object obj){
		BaseResponse resp = new BaseResponse();
		resp.setRespCode(String.valueOf(RespEnum.RESP_SUCCESS.getEnCode()));
		resp.setRespData("");
		resp.setResult(obj);

		return resp;
	}
}

