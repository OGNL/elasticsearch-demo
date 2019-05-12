package com.demo.util;


import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtil {


    /**
     * 读取后缀为.xlsx的Excel文件（只读单个sheet表）
     * @param inputStream 文件流 ---NOT NULL
     * @param startRowNum 开始读取行数 ---NOT NULL
     * @return
     */
    public static List<List<String>> readXlsxData(InputStream inputStream, int startRowNum){
        XSSFWorkbook workbook= null;
        XSSFSheet sheet = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        List<List<String>> dataList = null;
        try {
            workbook = new XSSFWorkbook(inputStream);
            //读取第一个sheet表
            sheet = workbook.getSheetAt(0);
            if(sheet != null){
                dataList = new ArrayList<>();
                //读取每一行数据
                for (int i = startRowNum; i <= sheet.getLastRowNum() ; i++) {
                    row = sheet.getRow(i);
                    if (row != null) {
                        List<String> rowCellList = new ArrayList<>();
                        //读取每个单元格数据
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            cell = row.getCell(j);
                            if (cell != null) {
                                switch(cell.getCellType()){
                                    case XSSFCell.CELL_TYPE_STRING:
                                        rowCellList.add(cell.getStringCellValue().trim());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            Date date = cell.getDateCellValue();
                                            if (date != null) {
                                                rowCellList.add(DateFormatUtils.format(cell.getDateCellValue(),"yyyy-MM-dd"));
                                            } else {
                                                rowCellList.add("");
                                            }
                                        } else {
                                            rowCellList.add(String.valueOf(cell.getNumericCellValue()));
                                        }
                                        break;
                                    default:
                                        rowCellList.add("");
                                        break;
                                }
                            } else {
                                rowCellList.add("");
                            }
                        }
                        dataList.add(rowCellList);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 读取后缀为.xls的Excel文件（只读单个sheet表）
     * @param inputStream 文件流 ---NOT NULL
     * @param startRowNum 开始读取行数 ---NOT NULL
     * @return
     */
    public static List<List<String>> readXlsData(InputStream inputStream, int startRowNum){
        HSSFWorkbook workbook= null;
        HSSFSheet sheet = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        List<List<String>> dataList = null;
        try {
            workbook = new HSSFWorkbook(inputStream);
            //读取第一个sheet表
            sheet = workbook.getSheetAt(0);
            if(sheet != null){
                dataList = new ArrayList<>();
                //读取每一行数据
                for (int i = startRowNum; i <= sheet.getLastRowNum() ; i++) {
                    row = sheet.getRow(i);
                    if (row != null) {
                        List<String> rowCellList = new ArrayList<>();
                        //读取每个单元格数据
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            cell = row.getCell(j);
                            if (cell != null) {
                                switch(cell.getCellType()){
                                    case HSSFCell.CELL_TYPE_STRING:
                                        rowCellList.add(cell.getStringCellValue().trim());
                                        break;
                                    case HSSFCell.CELL_TYPE_NUMERIC:
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            Date date = cell.getDateCellValue();
                                            if (date != null) {
                                                rowCellList.add(DateFormatUtils.format(cell.getDateCellValue(),"yyyy-MM-dd"));
                                            } else {
                                                rowCellList.add("");
                                            }
                                        } else {
                                            rowCellList.add(String.valueOf(cell.getNumericCellValue()));
                                        }
                                        break;
                                    default:
                                        rowCellList.add("");
                                        break;
                                }
                            } else {
                                rowCellList.add("");
                            }
                        }
                        dataList.add(rowCellList);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 创建字体
     * @param workbook excel对象
     * @param fontName 字体风格 例如:"宋体"
     * @param fontSize 字体大小
     * @param boldWeight 字体粗细程度
     * @param color 颜色
     * @return
     */
    public static Font createFont(XSSFWorkbook workbook, String fontName, Short fontSize,  Short boldWeight, Short color){
        Font font = workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        font.setBoldweight(boldWeight);
        font.setColor(color);
        return font;
    }

    /**
     * 创建单元格样式
     * @param workbook excel对象
     * @param font 字体
     * @param alignment 水平对齐模式
     * @param verticalAlignment 垂直对齐模式
     * @param isBorder 是否有边框
     * @param fillPattern 填充模式
     * @param fillForeGroundColor  填充背景色
     * @return
     */
    public static CellStyle createCellStyle(XSSFWorkbook workbook, Font font, Short alignment, Short verticalAlignment,
                                      Boolean isBorder, Short fillPattern, Short fillForeGroundColor){



        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(alignment);
        cellStyle.setVerticalAlignment(verticalAlignment);
        if(isBorder){
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        }
        if(fillPattern != null){
            cellStyle.setFillPattern(fillPattern);
        }
        if(fillForeGroundColor != null){
            cellStyle.setFillForegroundColor(fillForeGroundColor);
        }
        cellStyle.setLocked(true);
        cellStyle.setWrapText(false);// 自动换行
        return cellStyle;
    }
}
