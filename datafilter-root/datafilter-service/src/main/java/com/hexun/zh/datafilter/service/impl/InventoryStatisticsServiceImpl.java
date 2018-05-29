package com.hexun.zh.datafilter.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hexun.zh.datafilter.common.utils.CsvCPUtils;
import com.hexun.zh.datafilter.common.utils.ExcelCPUtils;
import com.hexun.zh.datafilter.mapper.Inventory_statisticsMapper;
import com.hexun.zh.datafilter.service.InventoryStatisticsService;

@Service
public class  InventoryStatisticsServiceImpl implements InventoryStatisticsService {
	
	private Logger log = LoggerFactory.getLogger(getClass());

//	    @Resource
//	    private FeedbackMapper feedbackMapper;

	
	    @Resource
	    private JdbcTemplate jdbcTemplate;
	
	    private Inventory_statisticsMapper inventory_statisticsMapper;
	
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

	@Override
	public List<Map<String, Object>> findInventoryStatisticsGroupBy(String startTime, String endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteAll() {
		// TODO Auto-generated method stub
		return 0;
	}
    
}
