package com.hexun.zh.datafilter.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hexun.zh.datafilter.entity.Inventory_statistics;

public interface Inventory_statisticsMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(Inventory_statistics record);

    int insertSelective(Inventory_statistics record);

    Inventory_statistics selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Inventory_statistics record);

    int updateByPrimaryKey(Inventory_statistics record);
    
    /**
     * 查询符合条件数据
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> findInventoryStatisticsByTime(@Param("startTime") String startTime,@Param("endTime") String endTime);
    
    
    /**
     * 分组统计查询
     * @param startTime
     * @param endTime
     * @return
     */
    /**
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> findInventoryStatisticsGroupBy(@Param("startTime") String startTime,@Param("endTime") String endTime);
    
    

    /**
     * 根据时间段查询查询 不重样的文件名称
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> findInventoryStatisticsDistinctFileName(@Param("startTime") String startTime,@Param("endTime") String endTime);
    
    

    /**
     * 根据文件名查询，不重复sheet名称
     * @param startTime
     * @param endTime
     * @param fileName
     * @return
     */
    List<Map<String,Object>> findInventoryStatisticsDistinctSheetName(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("fileName") String fileName);
    
    

    /**
     * 根据文件名，sheet名称，名查询产品序列号名称
     * @param startTime
     * @param endTime
     * @param fileName
     * @param sheetName
     * @return
     */
    List<Map<String,Object>> findInventoryStatisticsDistinctSerialNumber(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("fileName") String fileName,@Param("sheetName") String sheetName);
    
    
    /**
     * 清除所有
     * @return
     */
    int deleteAll();
    
    
   
}