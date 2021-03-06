package com.zhongweixian.excel.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:23:47
 */
public class PoiCellUtil {

    /**
     * 读取单元格的值
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getCellValue(Sheet sheet, int row, int column) {
        String value = null;
        if (isMergedRegion(sheet, row, column)) {
            value = getMergedRegionValue(sheet, row, column);
        } else {
            Row rowData = sheet.getRow(row);
            Cell cell = rowData.getCell(column);
            value = getCellValue(cell);
        }
        return value;
    }

    /**
     * 获取合并单元格的值
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);

                    return getCellValue(fCell);
                }
            }
        }

        return null;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        if (cell.getCellTypeEnum() == CellType.STRING) {

            return cell.getStringCellValue();

        } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {

            return String.valueOf(cell.getBooleanCellValue());

        } else if (cell.getCellTypeEnum() == CellType.FORMULA) {

            return cell.getCellFormula();

        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {

            return String.valueOf(cell.getNumericCellValue());

        } else {
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        }
    }
}
