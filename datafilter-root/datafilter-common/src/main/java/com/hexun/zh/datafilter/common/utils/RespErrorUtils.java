package com.hexun.zh.datafilter.common.utils;

import org.springframework.web.servlet.ModelAndView;

/**
 * 设置resp 错误信息
 * Created by zhoudong on 2017/7/19.
 */
public class RespErrorUtils {
    /**
     * 设置错误信息（接口）
     * @param resp
     * @param msg
     * @return
     */
    public static BaseResponse errorMgs(BaseResponse resp, String msg){
        resp.setCharset("UTF-8");
        resp.setRespCode(String.valueOf(RespEnum.RESP_FAIL.getEnCode()));
        resp.setErrorData(msg);
        return resp;
    }

    /**
     * 设置错误信息（页面）
     * @param mav
     * @param msg
     * @return
     */
    public static ModelAndView errorMgs(ModelAndView mav, String msg){
        mav.addObject("errorMsg",msg);
        mav.setViewName("error");
        return mav;
    }
}
