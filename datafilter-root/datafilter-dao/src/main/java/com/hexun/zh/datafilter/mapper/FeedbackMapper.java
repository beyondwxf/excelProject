package com.hexun.zh.datafilter.mapper;

import com.hexun.zh.datafilter.entity.Feedback;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FeedbackMapper {
    int deleteByPrimaryKey(String id);

    int insert(Feedback record);

    int insertSelective(Feedback record);

    Feedback selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Feedback record);

    int updateByPrimaryKey(Feedback record);

    /**
     * 线形图数据查询
     * @param startTime
     * @param endTime
     * @param feedbackType
     * @return
     */
    List<Map<String,String>> loadLineChat(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("feedbackType") String feedbackType);
    /**
     * 条形图数据查询
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> loadBarChat(@Param("startTime") String startTime,@Param("endTime") String endTime);
    /**
     * 饼图数据查询
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> loadPieChat(@Param("startTime") String startTime,@Param("endTime")  String endTime);
}