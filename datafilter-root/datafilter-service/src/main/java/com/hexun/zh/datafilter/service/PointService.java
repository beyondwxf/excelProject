package com.hexun.zh.datafilter.service;

import com.hexun.zh.datafilter.common.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Administrator
 *
 */
public interface PointService {

   

    /**
     * 积分常用操作
     * @param req
     * @return
     */
    BaseResponse commonOperation(HttpServletRequest req);

}
