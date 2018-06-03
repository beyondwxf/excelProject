package com.hexun.zh.datafilter.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hexun.zh.datafilter.common.utils.BaseResponse;
import com.hexun.zh.datafilter.common.utils.CsvCPUtils;
import com.hexun.zh.datafilter.common.utils.ExcelCPUtils;
import com.hexun.zh.datafilter.common.utils.StringUtils;
import com.hexun.zh.datafilter.mapper.Inventory_statisticsMapper;
import com.hexun.zh.datafilter.service.InventoryStatisticsService;

@Service
public  class  InventoryStatisticsServiceImpl implements InventoryStatisticsService {
	
	private Logger log = LoggerFactory.getLogger(getClass());

//	    @Resource
//	    private FeedbackMapper feedbackMapper;

	
	    @Resource
	    private JdbcTemplate jdbcTemplate;
	    @Resource
	    private Inventory_statisticsMapper inventory_statisticsMapper;
	
	  /**
     * excel数据导入数据库
     * @param normalFile
     */
    @Override
    public void importChaoPiExcel(File normalFile,String realFileName) throws Exception {

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

        String sql = "INSERT INTO `inventory_statistics` (`id`, `fileName`,`sheetName`,`serialNumber`, `productCoding`, `productName`, `productInventory`, `productEffectiveTimeDec`, `effectiveTime`) VALUES (?, ?,?, ?, ?, ?, ?, ?, ?)";
        for(List<Object> rows : list){

//            Date date = DateUtils.toDate(rows.get(0).toString(),"yyyy/MM/dd");
//            rows.remove(0);
//            rows.add(0,date);
        	String s = UUID.randomUUID().toString();
        	System.out.println(s);
        	 rows.add(0,s.replaceAll("-",""));
        	 rows.add(1,realFileName);
            

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

//	@Override
//	public List<Map<String, Object>> findInventoryStatisticsGroupBy(String startTime, String endTime) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public int deleteAll() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BaseResponse findInventoryStatisticsDistinctFileName(HttpServletRequest req) {
		String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");

//        if(StringUtils.isBlank(startTime)){
//            log.info("开始时间为空");
//            return BaseResponse.getSuccessByEnCode("开始时间为空");
//        }
//        if(StringUtils.isBlank(endTime)){
//            log.info("结束时间为空");
//            return BaseResponse.getSuccessByEnCode("结束时间为空");
//        }

        List<Map<String,String>> list = inventory_statisticsMapper.findInventoryStatisticsDistinctFileName(startTime,endTime);
        log.info(" **  根据时间段查询查询 不重样的文件名称，查询到结果：{}", JSON.toJSONString(list));
        
//        Map<String,String> resultMap = null;
//        List<Map<String, String>> resultList = new ArrayList<>();
//        int i = 0;
//        for(Map<String,String> map : list){
//            i++;
//        }
		// TODO Auto-generated method stub
		return BaseResponse.getSuccessByEnCode(list);
	}

	@Override
	public BaseResponse findInventoryStatisticsDistinctSheetName(HttpServletRequest req) {
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String fileName = req.getParameter("fileName");

        if(StringUtils.isBlank(startTime)){
            log.info("开始时间为空");
            return BaseResponse.getSuccessByEnCode("开始时间为空");
        }
        if(StringUtils.isBlank(endTime)){
            log.info("结束时间为空");
            return BaseResponse.getSuccessByEnCode("结束时间为空");
        }
        if(StringUtils.isBlank(fileName)){
            log.info("文件名称不能为空");
            return BaseResponse.getSuccessByEnCode("文件名称不能为空");
        }
        List<Map<String,Object>> list = inventory_statisticsMapper.findInventoryStatisticsDistinctSheetName(startTime,endTime,fileName);
		return null;
	}

	@Override
	public BaseResponse findInventoryStatisticsDistinctSerialNumber(HttpServletRequest req) {
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String fileName = req.getParameter("fileName");
		String sheetName = req.getParameter("sheetName");

        if(StringUtils.isBlank(startTime)){
            log.info("开始时间为空");
            return BaseResponse.getSuccessByEnCode("开始时间为空");
        }
        if(StringUtils.isBlank(endTime)){
            log.info("结束时间为空");
            return BaseResponse.getSuccessByEnCode("结束时间为空");
        }
        if(StringUtils.isBlank(fileName)){
            log.info("文件名称不能为空");
            return BaseResponse.getSuccessByEnCode("文件名称不能为空");
        }
        if(StringUtils.isBlank(sheetName)){
            log.info("sheet名称不能为空");
            return BaseResponse.getSuccessByEnCode("sheet名称不能为空");
        }
        List<Map<String,Object>> list = inventory_statisticsMapper.findInventoryStatisticsDistinctSerialNumber(startTime,endTime,fileName,sheetName);
		return null;
	}

	@Override
	public BaseResponse findInventoryStatisticsBySeri(HttpServletRequest req) {
		
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String fileName = req.getParameter("fileName");
		String sheetName = req.getParameter("sheetName");
		String serialNumber = req.getParameter("serialNumber");

        if(StringUtils.isBlank(startTime)){
            log.info("开始时间为空");
            return BaseResponse.getSuccessByEnCode("开始时间为空");
        }
        if(StringUtils.isBlank(endTime)){
            log.info("结束时间为空");
            return BaseResponse.getSuccessByEnCode("结束时间为空");
        }
        if(StringUtils.isBlank(fileName)){
            log.info("文件名称不能为空");
            return BaseResponse.getSuccessByEnCode("文件名称不能为空");
        }
        if(StringUtils.isBlank(sheetName)){
            log.info("sheet名称不能为空");
            return BaseResponse.getSuccessByEnCode("sheet名称不能为空");
        }
        if(StringUtils.isBlank(serialNumber)){
            log.info("serialNumber名称不能为空");
            return BaseResponse.getSuccessByEnCode("serialNumber名称不能为空");
        }
        
        List<Map<String,Object>> list = inventory_statisticsMapper.findInventoryStatisticsBySeri(startTime,endTime,fileName,sheetName,serialNumber);
		return null;
	}

	@Override
	public List<Map<String,Object>> queryExportExcelData(HttpServletRequest req) {
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String fileName = req.getParameter("fileName");
		if(StringUtils.isBlank(startTime)){
	            log.info("开始时间为空");
	            return null;
	        }
		 if(StringUtils.isBlank(endTime)){
	            log.info("结束时间为空");
	            return null;
	        }
		 
		 if(StringUtils.isBlank(fileName)){
	            log.info("结束时间为空");
	            return null;
	        }
	        List<Map<String,Object>> excelExportSheetList = excelExportSheetList = new ArrayList<>();
	        List<Map<String,Object>> sheetNamelist = inventory_statisticsMapper.findInventoryStatisticsDistinctSheetName(startTime,endTime,fileName); 
	        for(Map<String,Object> mapSheetName : sheetNamelist){
	        	System.out.println(mapSheetName.get("sheetName"));
	        	String sheetName = mapSheetName.get("sheetName").toString();
	        	
	        	Map<String,Object> map = new HashMap<>();
	        	map.put("sheetName", sheetName);
	        	int titleNum = 0;
	        	
	        	List sheetDataList = new ArrayList<>();
	        	  List<Map<String,Object>> serialNumberlist = inventory_statisticsMapper.findInventoryStatisticsDistinctSerialNumber(startTime, endTime, fileName, sheetName);
	        	 if(null != serialNumberlist) {
		        	  for(Map<String,Object> mapSerialNumber : serialNumberlist){
		        		  
		  	        	String serialNumber = mapSerialNumber.get("serialNumber").toString();
		  	        	System.out.println("serialNumber:"+serialNumber);
		  	        	
		  	         	List<String> excelProduct = null;
		  	             List<Map<String,Object>> productList  = inventory_statisticsMapper.findInventoryStatisticsBySeri(startTime, endTime, fileName, sheetName, serialNumber);
		  	             		if(null != productList && productList.size()>0) {
		  	             			
		  	             			String productCoding = productList.get(0).get("productCoding").toString();
		  	             			String productName = productList.get(0).get("productName").toString();
		  	             			excelProduct = new ArrayList<>();
		  	             			excelProduct.add(serialNumber.toString());
		  	             			excelProduct.add(productCoding);
		  	             			excelProduct.add(productName);
		  	             		}
		  	                 for(Map<String,Object> mapProductInfo : productList){
		  	                	String productCoding = mapProductInfo.get("productCoding").toString();
		  	                	String productName = mapProductInfo.get("productName").toString();
		  	                	String productInventory = mapProductInfo.get("productInventory").toString();
		  	                	String productEffectiveTimeDec = mapProductInfo.get("productEffectiveTimeDec").toString();
		  	                	System.out.println("productCoding:"+productCoding);
		  	                	System.out.println("productName:"+productName);
		  	                	System.out.println("productInventory:"+productInventory);
		  	                	System.out.println("productEffectiveTimeDec:"+productEffectiveTimeDec);
		  	                	
		  	                	excelProduct.add(productInventory);
		  	                	excelProduct.add(productEffectiveTimeDec);
		  	                	
				  	      	 }
		  	                 if(excelProduct.size() > titleNum) {
		  	                	 titleNum = excelProduct.size();
		  	                 }
		  	              
		  	               sheetDataList.add(excelProduct); 
		        	  }
		        	   List titleNameList  = new ArrayList<>();
		        	   titleNameList.add("编码");
		        	   titleNameList.add("条码");
		        	   titleNameList.add("名称");
		        	   for(int i=3;i<titleNum;i++) {
		        		   if(i%2==1) {
		        			   titleNameList.add("数量");
		        		   }else {
		        			   titleNameList.add("效期");
		        		   }
		        		  
		        	   }
		        	    map.put("titleName",titleNameList);
		        	   
		        		map.put("dataList", sheetDataList);
		        		excelExportSheetList.add(map);
	        	 }
	        	 
	        }
		return excelExportSheetList;
	}
    
}
