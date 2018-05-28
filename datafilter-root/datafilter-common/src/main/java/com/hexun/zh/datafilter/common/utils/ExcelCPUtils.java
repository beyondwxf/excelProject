package com.hexun.zh.datafilter.common.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Excel 工具类
 */
public class ExcelCPUtils {
    private final static String excel2003L =".xls";    //2003- 版本的excel
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel
    /**
     * 
     * @param in
     * @param fileName
     * @param productDescNum 产品描述列数（非业务类）
     * @return
     * @throws Exception
     */
    public static  List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception{
        List<List<Object>> list = null;

        //创建Excel工作薄
        Workbook work = getWorkbook(in,fileName);
        if(null == work){
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        //每个sheet的名称
        String sheetName = "";
        Row row = null;
        Cell cell = null;
        list = new ArrayList<List<Object>>();
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
        	sheet = work.getSheetAt(i);
        	sheetName = work.getSheetAt(i).getSheetName();
        	System.out.println("sheetName:"+sheetName);
            if(sheet==null){continue;}

            //遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum() + 1; j < sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if(row==null||row.getFirstCellNum()==j){continue;}

                //遍历所有的列
                List<Object> li = new ArrayList<Object>();
                
                List<Object> licp = null;
                //每一行
                String serialNumber = "";
            	String productCoding = "";
            	String productName = "";
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                	
                	 cell = row.getCell(y);
                	if(0==y) {
                		 licp = new ArrayList<Object>();
                		serialNumber = (String) getCellValue(cell);
                	}else if(1==y) {
                		productCoding = (String) getCellValue(cell);
                	}else if(2==y) {
                		productName = (String)getCellValue(cell);
                	}else if(y>=3) {
                    	if(y%2==1) {
                    		if(y>=5) {
                   			 licp = new ArrayList<Object>();
                   		    }
                    		//加入sheet名称
                    		licp.add(sheetName);
                    		licp.add(serialNumber);
                    		licp.add(productCoding);
                    		licp.add(productName);
                    		licp.add(getCellValue(cell));
                    		
                    	}else if(y%2==0) {
                    		
                    		if(StringUtils.isBlank(getCellValue(cell))) {
                    			licp.add(null);
                    			licp.add(null);
                    		}else {
                    			System.out.println("getCellValue(cell).toString():"+getCellValue(cell).toString());
                    			licp.add(getCellValue(cell));
                    			try {
                    				licp.add(DateUtils.CPgetExpiryDate(getCellValue(cell).toString()));
								} catch (Exception e) {
									licp.add("其他原因");
								}
                    			
                    			
//                    			licp.add(DateUtils.CPgetExpiryDate(getCellValue(cell).toString()));
                    		}
                    	}
                    	
                    	
                    	
                    }
                	if((y>=4)&&(y%2==0)) {
                		System.out.println("licp："+licp.size());
                		System.out.println("licp.get(\"4\"):"+licp.get(4));
                    	System.out.println("licp.get(\"6\"):"+licp.get(6));
                    	if(!(StringUtils.isBlank(licp.get(4))&&StringUtils.isBlank(licp.get(6)))) {
                    		 list.add(licp);
                    		 System.out.println("由于两个以一个不为空所以加入数据");
                    	}else {
                    		System.out.println("由于都为空，不加入");
                    	}
                	}
                	
                   	
//                    System.out.println(y+"_cell:"+cell);
//                    li.add(getCellValue(cell));
                }
//                list.add(li);
            }
        }
//        work.close();
        return list;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    private static  Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    public static  Object getCellValue(Cell cell){
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字

        if(cell == null) return null;

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString().trim();
                break;
            case Cell.CELL_TYPE_NUMERIC:
            	if (HSSFDateUtil.isCellDateFormatted(cell)) {
            		Date date = cell.getDateCellValue();
            		value = sdf.format(date);

            	}else {
            		if("General".equals(cell.getCellStyle().getDataFormatString())){
                        value = df.format(cell.getNumericCellValue());
                    }
//            		else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
//                        value = sdf.format(cell.getDateCellValue());
//                    }
                    else{
                        value = df.format(cell.getNumericCellValue());
                    }
            	}
                
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

}
