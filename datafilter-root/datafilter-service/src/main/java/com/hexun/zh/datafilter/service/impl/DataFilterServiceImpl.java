package com.hexun.zh.datafilter.service.impl;

import com.alibaba.fastjson.JSON;
import com.hexun.zh.datafilter.common.utils.CsvUtils;
import com.hexun.zh.datafilter.common.mybatis.BaseMybatisDao;
import com.hexun.zh.datafilter.common.page.Page;
import com.hexun.zh.datafilter.common.utils.BaseResponse;
import com.hexun.zh.datafilter.common.utils.CsvCPUtils;
import com.hexun.zh.datafilter.common.utils.DateUtils;
import com.hexun.zh.datafilter.common.utils.ExcelCPUtils;
import com.hexun.zh.datafilter.common.utils.ExcelUtils;
import com.hexun.zh.datafilter.common.utils.StringUtils;
import com.hexun.zh.datafilter.entity.Feedback;
import com.hexun.zh.datafilter.mapper.FeedbackMapper;
import com.hexun.zh.datafilter.service.DataFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.*;

@Service
public class DataFilterServiceImpl extends BaseMybatisDao<Feedback> implements DataFilterService {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private FeedbackMapper feedbackMapper;

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 分页获取数据
     * @return
     */
    @Override
    public Page<Feedback> findDataByPage(String sqlId, Map<String, Object> param,
                                         int pageNo, int PageSize) {
        return findByPageBySqlId(sqlId,param,pageNo,PageSize);
    }

    /**
     * excel数据导入数据库
     * @param normalFile
     */
    @Override
    public void importExcel(File normalFile) throws Exception {

        List<List<Object>> list = null;
        // 1、判断文件类型
        String fileName = normalFile.getPath();
        String fileType = fileName.substring(fileName.lastIndexOf("."));

        if(".csv".equalsIgnoreCase(fileType)){
            list = CsvUtils.getBankListByCSV(normalFile);
            log.info(" ** 解析CSV文件完成，共{}行。",list.size());
        } else {
            InputStream input = new FileInputStream(normalFile);
            list = ExcelUtils.getBankListByExcel(input,normalFile.getPath());
            log.info(" ** 解析Excel文件完成，共{}行。",list.size());
        }
        List<Object[]> params = new ArrayList<>();

        String sql = "INSERT INTO `feedback` (`ID`, `FEEDBACK_TIME`, `DEVICE_NAME`, `PLATFORM`, `CLIENT_VERSION`, `EMAIL`, `PHONE`, `FEEDBACK_TYPE`, `MEMO`, `ANSWER_TYPE`, `MESSAGE`, `IP`, `UA`, `PROVINCE`, `PHONE_OPERATOR`, `INSTALL_CANNEL`, `IS_HANDLE`, `OPERATOR`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for(List<Object> rows : list){

            Date date = DateUtils.toDate(rows.get(0).toString(),"yyyy/MM/dd");
            rows.remove(0);
            rows.add(0,date);
            rows.add(0,UUID.randomUUID().toString().replaceAll("-",""));

            Object[] os = rows.toArray(new Object[rows.size()]);
            params.add(os);

            /*params.add(new Object[]{
                    UUID.randomUUID().toString().replaceAll("-",""),
                    DateUtils.toDate(rows.get(0).toString(),"yyyy-MM-dd"),
                    String.valueOf(rows.get(1)), String.valueOf(rows.get(2)), String.valueOf(rows.get(3)),
                    String.valueOf(rows.get(4)), String.valueOf(rows.get(5)), String.valueOf(rows.get(6)),
                    String.valueOf(rows.get(7)), String.valueOf(rows.get(8)), String.valueOf(rows.get(9)),
                    String.valueOf(rows.get(10)), String.valueOf(rows.get(11)), String.valueOf(rows.get(12)),
                    String.valueOf(rows.get(13)), String.valueOf(rows.get(14)), String.valueOf(rows.get(15)),
                    String.valueOf(rows.get(16))
            });*/
        }
        long startTime = System.currentTimeMillis();
        try {
            jdbcTemplate.batchUpdate(sql, params);
        }catch (Exception e){ // 异常不做处理，继续执行。
            log.info(e.getMessage());
        }

        log.info(" ** 批量插入完成,耗时：{}秒",(System.currentTimeMillis()-startTime)/1000);
    }
    
    /**
     * excel数据导入数据库
     * @param normalFile
     */
    @Override
    public void importChaoPiExcel(File normalFile) throws Exception {

        List<List<Object>> list = null;
        // 1、判断文件类型
        String fileName = normalFile.getPath();
        String fileType = fileName.substring(fileName.lastIndexOf("."));

        if(".csv".equalsIgnoreCase(fileType)){
            list = CsvCPUtils.getBankListByCSV(normalFile);
            log.info(" ** 解析CSV文件完成，共{}行。",list.size());
        } else {
            InputStream input = new FileInputStream(normalFile);
//            list = ExcelCPUtils.getBankListByExcel(input, normalFile, productDescNum)
            list = ExcelCPUtils.getBankListByExcel(input,normalFile.getPath());
            log.info(" ** 解析Excel文件完成，共{}行。",list.size());
        }
        List<Object[]> params = new ArrayList<>();

        String sql = "INSERT INTO `inventory_statistics` (`id`, `sheetName`,`serialNumber`, `productCoding`, `productName`, `productInventory`, `productEffectiveTimeDec`, `effectiveTime`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        for(List<Object> rows : list){

//            Date date = DateUtils.toDate(rows.get(0).toString(),"yyyy/MM/dd");
//            rows.remove(0);
//            rows.add(0,date);
        	String s = UUID.randomUUID().toString();
        	System.out.println(s);
            rows.add(0,s.replaceAll("-",""));

            Object[] os = rows.toArray(new Object[rows.size()]);
            params.add(os);

            /*params.add(new Object[]{
                    UUID.randomUUID().toString().replaceAll("-",""),
                    DateUtils.toDate(rows.get(0).toString(),"yyyy-MM-dd"),
                    String.valueOf(rows.get(1)), String.valueOf(rows.get(2)), String.valueOf(rows.get(3)),
                    String.valueOf(rows.get(4)), String.valueOf(rows.get(5)), String.valueOf(rows.get(6)),
                    String.valueOf(rows.get(7)), String.valueOf(rows.get(8)), String.valueOf(rows.get(9)),
                    String.valueOf(rows.get(10)), String.valueOf(rows.get(11)), String.valueOf(rows.get(12)),
                    String.valueOf(rows.get(13)), String.valueOf(rows.get(14)), String.valueOf(rows.get(15)),
                    String.valueOf(rows.get(16))
            });*/
        }
        long startTime = System.currentTimeMillis();
        try {
            jdbcTemplate.batchUpdate(sql, params);
        }catch (Exception e){ // 异常不做处理，继续执行。
            log.info(e.getMessage());
        }

        log.info(" ** 批量插入完成,耗时：{}秒",(System.currentTimeMillis()-startTime)/1000);
    }
    
    
    /**
     * 查询线形图数据
     * @param req
     * @return
     */
    @Override
    public BaseResponse loadLineChat(HttpServletRequest req) {
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");
        String feedbackType = req.getParameter("feedbackType");

        if(StringUtils.isBlank(startTime)){
            log.info("开始时间为空");
            return BaseResponse.getSuccessByEnCode("开始时间为空");
        }
        if(StringUtils.isBlank(endTime)){
            log.info("结束时间为空");
            return BaseResponse.getSuccessByEnCode("结束时间为空");
        }

        List<Map<String, String>> list = feedbackMapper.loadLineChat(startTime,endTime,feedbackType);

        log.info(" ** 线形图，查询到结果：{}", JSON.toJSONString(list));

        return BaseResponse.getSuccessByEnCode(list);
    }
    /**
     * 查询条形图数据
     * @param req
     * @return
     */
    @Override
    public BaseResponse loadBarChat(HttpServletRequest req) {
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");

        if(StringUtils.isBlank(startTime)){
            log.info("开始时间为空");
            return BaseResponse.getSuccessByEnCode("开始时间为空");
        }
        if(StringUtils.isBlank(endTime)){
            log.info("结束时间为空");
            return BaseResponse.getSuccessByEnCode("结束时间为空");
        }

        List<Map<String, Object>> list = feedbackMapper.loadBarChat(startTime,endTime);
        log.info(" ** 条形图，查询到结果：{}", JSON.toJSONString(list));


        String timeKey = "";
        Map<String,String> barChatMap = null;
        List<Map<String, String>> resultList = new ArrayList<>();
        int i = 0;
        for(Map<String,Object> map : list){
            i++;
            String feedbackTime = map.get("feedbackTime").toString();
            if(StringUtils.isBlank(timeKey)){ // 第一次循环
                timeKey = feedbackTime;
                barChatMap = new HashMap<>();
                barChatMap.put("feedbackTime",feedbackTime);
                barChatMap.put(map.get("feedbackType").toString(),map.get("count").toString());
            }else{
                if(timeKey.equals(feedbackTime)){ // 时间和上一次循环相同
                    barChatMap.put(map.get("feedbackType").toString(),map.get("count").toString());
                }else{ // 时间和上一次循环不相同，开始一个新的map数据
                    timeKey = feedbackTime;
                    resultList.add(barChatMap);
                    barChatMap = new HashMap<>();
                    barChatMap.put("feedbackTime",feedbackTime);
                    barChatMap.put(map.get("feedbackType").toString(),map.get("count").toString());
                }
            }

            if(i == list.size()){
                resultList.add(barChatMap);
            }
        }

        return BaseResponse.getSuccessByEnCode(resultList);
    }

    /**
     * 查询饼图数据
     * @param req
     * @return
     */
    @Override
    public BaseResponse loadPieChat(HttpServletRequest req) {
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");

        if(StringUtils.isBlank(startTime)){
            log.info("开始时间为空");
            return BaseResponse.getSuccessByEnCode("开始时间为空");
        }
        if(StringUtils.isBlank(endTime)){
            log.info("结束时间为空");
            return BaseResponse.getSuccessByEnCode("结束时间为空");
        }

        List<Map<String, Object>> list = feedbackMapper.loadPieChat(startTime,endTime);

        log.info(" ** 饼图，查询到结果：{}", JSON.toJSONString(list));

        long count = 0;
        for(Map<String,Object> map : list){
            Object v = map.get("value");
            long value = Long.parseLong(StringUtils.isBlank(v) ? "0" : String.valueOf(v));
            count += value;
        }
        log.info(" ** 总数：{}",count);

        // 计算百分比
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        for(Map<String,Object> map : list){
            Object v = map.get("value");
            long value = Long.parseLong(StringUtils.isBlank(v) ? "0" : String.valueOf(v));
            map.put("value",numberFormat.format(((float)value / (float)count) * 100));
        }

        return BaseResponse.getSuccessByEnCode(list);
    }


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            list.add(i);
        }
        System.out.println("arrayList 所用时间： " + (System.currentTimeMillis() - startTime));

        long startTime1 = System.currentTimeMillis();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 100000; i++) {
            set.add(i);
        }
        System.out.println("set 所用时间： " + (System.currentTimeMillis() - startTime1));
    }

}
