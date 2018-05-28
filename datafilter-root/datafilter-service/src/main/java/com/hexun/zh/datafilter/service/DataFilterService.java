package com.hexun.zh.datafilter.service;


import com.hexun.zh.datafilter.common.page.Page;
import com.hexun.zh.datafilter.common.utils.BaseResponse;
import com.hexun.zh.datafilter.entity.Feedback;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;

/**
 * Created by hexun on 2018/03/06.
 */
public interface DataFilterService {

    Page<Feedback> findDataByPage(String sqlId, Map<String, Object> param,
                                  int pageNo, int PageSize);

    /**
     * excel数据导入数据库
     * @param normalFile
     */
    void importExcel(File normalFile) throws Exception;
    
    
    /**
     * chaop_excel数据导入数据库
     * @param normalFile
     */
    void importChaoPiExcel(File normalFile) throws Exception;
    

    /**
     * 查询线形图数据
     * @param req
     * @return
     */
    BaseResponse loadLineChat(HttpServletRequest req);

    /**
     * 查询条形图数据
     * @param req
     * @return
     */
    BaseResponse loadBarChat(HttpServletRequest req);
    /**
     * 查询饼图数据
     * @param req
     * @return
     */
    BaseResponse loadPieChat(HttpServletRequest req);
}
