package com.hexun.zh.datafilter.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface InventoryStatisticsService {
	
	  /**
     * chaop_excel数据导入数据库
     * @param normalFile
     */
    void importChaoPiExcel(File normalFile) throws Exception;
    /**
     * 分组统计查询
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> findInventoryStatisticsGroupBy(@Param("startTime") String startTime,@Param("endTime") String endTime);
    
    /**
     * 清除所有
     * @return
     */
    int deleteAll();
}
