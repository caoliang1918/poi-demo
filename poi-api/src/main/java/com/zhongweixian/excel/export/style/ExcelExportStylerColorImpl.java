package com.zhongweixian.excel.export.style;

import org.apache.poi.ss.usermodel.*;

/**
 * @author : caoliang
 * @date : 2017/11/14  下午2:50
 */
public class ExcelExportStylerColorImpl extends AbstractExcelExportStyler
        implements IExcelExportStyler {

    public ExcelExportStylerColorImpl(Workbook workbook) {
        super.createStyles(workbook);
    }

    @Override
    public CellStyle getHeaderStyle(short headerColor) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 24);
        titleStyle.setFont(font);
        titleStyle.setFillForegroundColor(headerColor);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return titleStyle;
    }

    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderLeft(BorderStyle.valueOf("1"));
        style.setBorderRight(BorderStyle.valueOf("1"));
        style.setBorderBottom(BorderStyle.valueOf("1"));
        style.setBorderTop(BorderStyle.valueOf("1"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }

    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(color); // 填充的背景颜色
        titleStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 填充图案
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setWrapText(true);
        return titleStyle;
    }

    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderLeft(BorderStyle.valueOf("1")); // 左边框
        style.setBorderRight(BorderStyle.valueOf("1")); // 右边框
        style.setBorderBottom(BorderStyle.valueOf("1"));
        style.setBorderTop(BorderStyle.valueOf("1"));
        style.setFillForegroundColor((short) 41); // 填充的背景颜色
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 填充图案
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }

}
