package com.hexun.zh.datafilter.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel 工具类
 */
public class ExcelCPUtils {
	private final static String excel2003L = ".xls"; // 2003- 版本的excel
	private final static String excel2007U = ".xlsx"; // 2007+ 版本的excel

	// //显示的导出表的标题
	// private String title;
	// //导出表的列名
	// private String[] rowName ;
	//
	// private List<Object[]> dataList = new ArrayList<Object[]>();
	//
	// HttpServletResponse response;

	// //构造方法，传入要导出的数据
	// public ExcelCPUtils(String title,String[] rowName,List<Object[]> dataList){
	// this.dataList = dataList;
	// this.rowName = rowName;
	// this.title = title;
	// }

	/**
	 * 导入excel
	 * 
	 * @param in
	 * @param fileName
	 * @param productDescNum
	 *            产品描述列数（非业务类）
	 * @return
	 * @throws Exception
	 */
	public static List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception {
		List<List<Object>> list = null;

		// 创建Excel工作薄
		Workbook work = getWorkbook(in, fileName);
		if (null == work) {
			throw new Exception("创建Excel工作薄为空！");
		}
		Sheet sheet = null;
		// 每个sheet的名称
		String sheetName = "";
		Row row = null;
		Cell cell = null;
		list = new ArrayList<List<Object>>();
		// 遍历Excel中所有的sheet
		for (int i = 0; i < work.getNumberOfSheets(); i++) {
			sheet = work.getSheetAt(i);
			sheetName = work.getSheetAt(i).getSheetName();
			System.out.println("sheetName:" + sheetName);
			if (sheet == null) {
				continue;
			}

			// 遍历当前sheet中的所有行
			for (int j = sheet.getFirstRowNum() + 1; j < sheet.getLastRowNum()+1; j++) {
				row = sheet.getRow(j);
				if (row == null || row.getFirstCellNum() == j) {
					continue;
				}

				// 遍历所有的列
				List<Object> li = new ArrayList<Object>();

				List<Object> licp = null;
				// 每一行
				String serialNumber = "";
				String productCoding = "";
				String productName = "";
				for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {

					cell = row.getCell(y);
					if (0 == y) {
						licp = new ArrayList<Object>();
						serialNumber = (String) getCellValue(cell);
					} else if (1 == y) {
						productCoding = (String) getCellValue(cell);
					} else if (2 == y) {
						productName = (String) getCellValue(cell);
					} else if (y >= 3) {
						if (y % 2 == 1) {
							if (y >= 5) {
								licp = new ArrayList<Object>();
							}
							// 加入sheet名称
							licp.add(sheetName);
							licp.add(serialNumber);
							licp.add(productCoding);
							licp.add(productName);
							licp.add(getCellValue(cell));

						} else if (y % 2 == 0) {

							if (StringUtils.isBlank(getCellValue(cell))) {
								licp.add(null);
								licp.add(null);
							} else {
								System.out.println("getCellValue(cell).toString():" + getCellValue(cell).toString());
								licp.add(getCellValue(cell));
								try {
									if(StringUtils.isBlank(DateUtils.CPgetExpiryDate(getCellValue(cell).toString()))) {
										licp.add("其他原因");
									}else {
										licp.add(DateUtils.CPgetExpiryDate(getCellValue(cell).toString()));
									}
									
								} catch (Exception e) {
									licp.add("其他原因");
								}

								// licp.add(DateUtils.CPgetExpiryDate(getCellValue(cell).toString()));
							}
						}

					}
					if ((y >= 4) && (y % 2 == 0)) {
						System.out.println("licp：" + licp.size());
						System.out.println("licp.get(\"4\"):" + licp.get(4));
						System.out.println("licp.get(\"6\"):" + licp.get(6));
						if (!(StringUtils.isBlank(licp.get(4)) && StringUtils.isBlank(licp.get(6)))) {
							list.add(licp);
							System.out.println("由于两个以一个不为空所以加入数据");
						} else {
							System.out.println("由于都为空，不加入");
						}
					}

					// System.out.println(y+"_cell:"+cell);
					// li.add(getCellValue(cell));
				}
				// list.add(li);
			}
		}
		// work.close();
		return list;
	}

	/**
	 * 描述：根据文件后缀，自适应上传文件的版本
	 * 
	 * @param inStr,fileName
	 * @return
	 * @throws Exception
	 */
	private static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
		Workbook wb = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (excel2003L.equals(fileType)) {
			wb = new HSSFWorkbook(inStr); // 2003-
		} else if (excel2007U.equals(fileType)) {
			wb = new XSSFWorkbook(inStr); // 2007+
		} else {
			throw new Exception("解析的文件格式有误！");
		}
		return wb;
	}

	/**
	 * 描述：对表格中数值进行格式化
	 * 
	 * @param cell
	 * @return
	 */
	public static Object getCellValue(Cell cell) {
		Object value = null;
		DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
		SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd"); // 日期格式化
		DecimalFormat df2 = new DecimalFormat("0.00"); // 格式化数字

		if (cell == null)
			return null;

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString().trim();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				value = sdf.format(date);

			} else {
				if ("General".equals(cell.getCellStyle().getDataFormatString())) {
					value = df.format(cell.getNumericCellValue());
				}
				// else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
				// value = sdf.format(cell.getDateCellValue());
				// }
				else {
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

	/*
	 * 列数据信息单元格样式
	 */
	private static HSSFCellStyle getStyle(HSSFWorkbook workbook) {
		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		// font.setFontHeightInPoints((short)10);
		// 字体加粗
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		return style;

	}

	/*
	 * 列头单元格样式
	 */
	private static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 11);
		// 字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		return style;

	}

	/*
	 * 导出数据
	 */
	/**
	 * 导出数据
	 * 
	 * @param out
	 *            输出流
	 * @param fileName
	 *            文件名
	 * @param sheetTitle
	 *            sheet名称
	 * @param rowName
	 *            excel抬头
	 * @param dataList
	 *            数据
	 * @return
	 * @throws Exception
	 */
	public static void export(HttpServletRequest request, HttpServletResponse response, String fileName,
			String sheetName, List titleName, List<List> dataList) throws Exception {
		// try{
		HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
		HSSFSheet sheet = workbook.createSheet(sheetName); // 创建工作表

		// 产生表格标题行
		// HSSFRow rowm = sheet.createRow(0);
		// HSSFCell cellTiltle = rowm.createCell(0);

		// sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
		HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);// 获取列头样式对象
		HSSFCellStyle style = getStyle(workbook); // 单元格样式对象

		// sheet.addMergedRegion(new CellRangeAddress(0, 1, 0,
		// (rowName.length-1)));//合并单元格
		// cellTiltle.setCellStyle(columnTopStyle);
		// cellTiltle.setCellValue(title);

		// 定义所需列数
		int columnNum = titleName.size();
		HSSFRow rowRowName = sheet.createRow(0); // 在索引2的位置创建行(最顶端的行开始的第二行)

		// 将列头设置到sheet的单元格中
		for (int n = 0; n < columnNum; n++) {
			HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
			cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
			HSSFRichTextString text = new HSSFRichTextString(titleName.get(n).toString());
			cellRowName.setCellValue(text); // 设置列头单元格的值
			cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
		}

		// 将查询出的数据设置到sheet对应的单元格中
		for (int i = 0; i < dataList.size(); i++) {

			List objList = dataList.get(i);// 遍历每个对象
			HSSFRow row = sheet.createRow(i + 1);// 创建所需的行数（从第二行开始写数据）

			for (int j = 0; j < objList.size(); j++) {
				HSSFCell cell = null; // 设置单元格的数据类型
//				if (j == 0) {
//					cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
//					cell.setCellValue(i + 1);
//				} else {
					cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
					System.out.println("objList:"+j+":"+objList.get(j));
					if (!"".equals(objList.get(j)) && objList.get(j) != null) {
						cell.setCellValue(objList.get(j).toString()); // 设置单元格的值
					}
//				}
				cell.setCellStyle(style); // 设置单元格样式
			}
		}
		// 让列宽随着导出的列长自动适应
		for (int colNum = 0; colNum < columnNum; colNum++) {
			int columnWidth = sheet.getColumnWidth(colNum) / 256;
			for (int rowNum = 0; rowNum < sheet.getLastRowNum()+1; rowNum++) {
				HSSFRow currentRow;
				// 当前行未被使用过
				if (sheet.getRow(rowNum) == null) {
					currentRow = sheet.createRow(rowNum);
				} else {
					currentRow = sheet.getRow(rowNum);
				}
				// if (currentRow.getCell(colNum) != null) {
				// HSSFCell currentCell = currentRow.getCell(colNum);
				// if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				// int length = currentCell.getStringCellValue().getBytes().length;
				// if (columnWidth < length) {
				// columnWidth = length;
				// }
				// }
				// }
				if (currentRow.getCell(colNum) != null) {
					HSSFCell currentCell = currentRow.getCell(colNum);
					if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						int length = 0;
						try {
							length = currentCell.getStringCellValue().getBytes().length;
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}

			}
//			if (colNum == 0) {
//				sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
//			} else {
				sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
//			}
		}
		if (workbook != null) {
			// try{
			// workbook.write(out);
			// 第六步，输出Excel文件
			// String fileName = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "_" +
			// excelExportEnum.getTypeName() +".xls";
			outWrite(request, response, workbook, fileName);
			// }catch (IOException e) {
			// e.printStackTrace();
			// }
		}

		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// finally{
		// out.close();
		// }

	}
	
	/**
	 * 导出数据
	 * 
	 * @param out
	 *            输出流
	 * @param fileName
	 *            文件名
	 * @param sheetTitle
	 *            sheet名称
	 * @param rowName
	 *            excel抬头
	 * @param dataList
	 *            数据
	 * @return
	 * @throws Exception
	 */
	/**
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 * @param datelist
	 * @throws Exception
	 */
	public static void export(HttpServletRequest request, HttpServletResponse response, String fileName, List<Map<String,Object>> exportDatalist) throws Exception {
		// try{
		HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
		for (int x = 0; x < exportDatalist.size(); x++) {
			String sheetName = exportDatalist.get(x).get("sheetName").toString();
			List titleName = (List) exportDatalist.get(x).get("titleName");
			List<List> dataList = (List) exportDatalist.get(x).get("dataList");
			
			
			
			HSSFSheet sheet = workbook.createSheet(sheetName); // 创建工作表

			// 产生表格标题行
			// HSSFRow rowm = sheet.createRow(0);
			// HSSFCell cellTiltle = rowm.createCell(0);

			// sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
			HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);// 获取列头样式对象
			HSSFCellStyle style = getStyle(workbook); // 单元格样式对象

			// sheet.addMergedRegion(new CellRangeAddress(0, 1, 0,
			// (rowName.length-1)));//合并单元格
			// cellTiltle.setCellStyle(columnTopStyle);
			// cellTiltle.setCellValue(title);

			// 定义所需列数
			int columnNum = titleName.size();
			HSSFRow rowRowName = sheet.createRow(0); // 在索引2的位置创建行(最顶端的行开始的第二行)

			// 将列头设置到sheet的单元格中
			for (int n = 0; n < columnNum; n++) {
				HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
				cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
				HSSFRichTextString text = new HSSFRichTextString(titleName.get(n).toString());
				cellRowName.setCellValue(text); // 设置列头单元格的值
				cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
			}

			// 将查询出的数据设置到sheet对应的单元格中
			for (int i = 0; i < dataList.size(); i++) {

				List objList = dataList.get(i);// 遍历每个对象
				HSSFRow row = sheet.createRow(i + 1);// 创建所需的行数（从第二行开始写数据）

				for (int j = 0; j < objList.size(); j++) {
					HSSFCell cell = null; // 设置单元格的数据类型
//					if (j == 0) {
//						cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
//						cell.setCellValue(i + 1);
//					} else {
						cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
						System.out.println("objList:"+j+":"+objList.get(j));
						if (!"".equals(objList.get(j)) && objList.get(j) != null) {
							cell.setCellValue(objList.get(j).toString()); // 设置单元格的值
						}
//					}
					cell.setCellStyle(style); // 设置单元格样式
				}
			}
			// 让列宽随着导出的列长自动适应
			for (int colNum = 0; colNum < columnNum; colNum++) {
				int columnWidth = sheet.getColumnWidth(colNum) / 256;
				for (int rowNum = 0; rowNum < sheet.getLastRowNum()+1; rowNum++) {
					HSSFRow currentRow;
					// 当前行未被使用过
					if (sheet.getRow(rowNum) == null) {
						currentRow = sheet.createRow(rowNum);
					} else {
						currentRow = sheet.getRow(rowNum);
					}
					// if (currentRow.getCell(colNum) != null) {
					// HSSFCell currentCell = currentRow.getCell(colNum);
					// if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					// int length = currentCell.getStringCellValue().getBytes().length;
					// if (columnWidth < length) {
					// columnWidth = length;
					// }
					// }
					// }
					if (currentRow.getCell(colNum) != null) {
						HSSFCell currentCell = currentRow.getCell(colNum);
						if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							int length = 0;
							try {
								length = currentCell.getStringCellValue().getBytes().length;
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (columnWidth < length) {
								columnWidth = length;
							}
						}
					}

				}
//				if (colNum == 0) {
//					sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
//				} else {
					sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
//				}
			}
		}
		
		for (int x = 0; x < exportDatalist.size(); x++) {
			String sheetName = exportDatalist.get(x).get("inconformitSheetName").toString();
			List titleName = (List) exportDatalist.get(x).get("inconformitTitle");
			List<List> dataList = (List) exportDatalist.get(x).get("inconformitDataList");
			
			HSSFSheet sheet = workbook.createSheet(sheetName); // 创建工作表

			// 产生表格标题行
			// HSSFRow rowm = sheet.createRow(0);
			// HSSFCell cellTiltle = rowm.createCell(0);

			// sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
			HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);// 获取列头样式对象
			HSSFCellStyle style = getStyle(workbook); // 单元格样式对象

			// sheet.addMergedRegion(new CellRangeAddress(0, 1, 0,
			// (rowName.length-1)));//合并单元格
			// cellTiltle.setCellStyle(columnTopStyle);
			// cellTiltle.setCellValue(title);

			// 定义所需列数
			int columnNum = titleName.size();
			HSSFRow rowRowName = sheet.createRow(0); // 在索引2的位置创建行(最顶端的行开始的第二行)

			// 将列头设置到sheet的单元格中
			for (int n = 0; n < columnNum; n++) {
				HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
				cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
				HSSFRichTextString text = new HSSFRichTextString(titleName.get(n).toString());
				cellRowName.setCellValue(text); // 设置列头单元格的值
				cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
			}

			// 将查询出的数据设置到sheet对应的单元格中
			for (int i = 0; i < dataList.size(); i++) {

				List objList = dataList.get(i);// 遍历每个对象
				HSSFRow row = sheet.createRow(i + 1);// 创建所需的行数（从第二行开始写数据）

				for (int j = 0; j < objList.size(); j++) {
					HSSFCell cell = null; // 设置单元格的数据类型
//					if (j == 0) {
//						cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
//						cell.setCellValue(i + 1);
//					} else {
						cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
						System.out.println("objList:"+j+":"+objList.get(j));
						if (!"".equals(objList.get(j)) && objList.get(j) != null) {
							cell.setCellValue(objList.get(j).toString()); // 设置单元格的值
						}
//					}
					cell.setCellStyle(style); // 设置单元格样式
				}
			}
			// 让列宽随着导出的列长自动适应
			for (int colNum = 0; colNum < columnNum; colNum++) {
				int columnWidth = sheet.getColumnWidth(colNum) / 256;
				for (int rowNum = 0; rowNum < sheet.getLastRowNum()+1; rowNum++) {
					HSSFRow currentRow;
					// 当前行未被使用过
					if (sheet.getRow(rowNum) == null) {
						currentRow = sheet.createRow(rowNum);
					} else {
						currentRow = sheet.getRow(rowNum);
					}
					// if (currentRow.getCell(colNum) != null) {
					// HSSFCell currentCell = currentRow.getCell(colNum);
					// if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					// int length = currentCell.getStringCellValue().getBytes().length;
					// if (columnWidth < length) {
					// columnWidth = length;
					// }
					// }
					// }
					if (currentRow.getCell(colNum) != null) {
						HSSFCell currentCell = currentRow.getCell(colNum);
						if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							int length = 0;
							try {
								length = currentCell.getStringCellValue().getBytes().length;
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (columnWidth < length) {
								columnWidth = length;
							}
						}
					}

				}
//				if (colNum == 0) {
//					sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
//				} else {
					sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
//				}
			}
		}
		
	
		if (workbook != null) {
			// try{
			// workbook.write(out);
			// 第六步，输出Excel文件
			// String fileName = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "_" +
			// excelExportEnum.getTypeName() +".xls";
			outWrite(request, response, workbook, fileName);
			// }catch (IOException e) {
			// e.printStackTrace();
			// }
		}

		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// finally{
		// out.close();
		// }

	}

	private static void outWrite(HttpServletRequest request, HttpServletResponse response, HSSFWorkbook wb,
			String fileName) throws IOException {
		
	    OutputStream output = response.getOutputStream();
        try {
        	setResponseHeader(request,response,fileName);  
            wb.write(output);  
            output.flush(); 
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(output != null){
                output.close();  
            }
        }

	}

	/**
	 * 
	 * 设置下载请求头和文件名
	 * 
	 * @param request
	 *            request
	 * @param response
	 *            response
	 * @param fileName
	 *            文件名称
	 */
	private static void setResponseHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
		try {
			try {
				if (request.getHeader("USER-AGENT").toLowerCase().contains("firefox")) {
					response.setCharacterEncoding("utf-8");
					response.setHeader("content-disposition",
							"attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
				} else {
					response.setCharacterEncoding("utf-8");
					response.setHeader("content-disposition",
							"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.setContentType("application/msexcel;charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
