package com.zhongweixian.excel.export.style;

import org.apache.poi.ss.usermodel.*;

/**
 * excel 边框样式
 *
 * @author : caoliang
 * @date : 2017/11/14  下午2:50
 */
public class ExcelExportStylerBorderImpl extends AbstractExcelExportStyler
        implements IExcelExportStyler {

    public ExcelExportStylerBorderImpl(Workbook workbook) {
        super.createStyles(workbook);
    }

    @Override
    public CellStyle getHeaderStyle(short color) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints((short) 12);
        titleStyle.setFont(font);
        titleStyle.setBorderLeft(BorderStyle.valueOf("1")); // 左边框
        titleStyle.setBorderRight(BorderStyle.valueOf("1")); // 右边框
        titleStyle.setBorderBottom(BorderStyle.valueOf("1"));
        titleStyle.setBorderTop(BorderStyle.valueOf("1"));
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return titleStyle;
    }

    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setBorderLeft(BorderStyle.valueOf("1")); // 左边框
        style.setBorderRight(BorderStyle.valueOf("1")); // 右边框
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
        titleStyle.setBorderLeft(BorderStyle.valueOf("1")); // 左边框
        titleStyle.setBorderRight(BorderStyle.valueOf("1")); // 右边框
        titleStyle.setBorderBottom(BorderStyle.valueOf("1"));
        titleStyle.setBorderTop(BorderStyle.valueOf("1"));
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setWrapText(true);
        return titleStyle;
    }

    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        return isWarp ? stringNoneWrapStyle : stringNoneStyle;
    }

}
