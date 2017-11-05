package com.excel.export.style;

import com.excel.entity.params.ExcelExportEntity;
import com.excel.entity.params.ExcelForEachParams;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:22:20
 */
public interface IExcelExportStyler {

    /**
     * 列表头样式
     */
    CellStyle getHeaderStyle(short headerColor);

    /**
     * 标题样式
     */
    CellStyle getTitleStyle(short color);

    /**
     * 获取样式方法
     */
    CellStyle getStyles(boolean parity, ExcelExportEntity entity);

    /**
     * 获取样式方法
     *
     * @param dataRow 数据行
     * @param obj     对象
     * @param data    数据
     */
    CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data);

    /**
     * 模板使用的样式设置
     */
    CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams);
}
