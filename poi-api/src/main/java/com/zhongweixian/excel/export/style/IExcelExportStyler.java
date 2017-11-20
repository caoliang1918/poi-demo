package com.zhongweixian.excel.export.style;

import com.zhongweixian.excel.entity.params.ExcelForEachParams;
import com.zhongweixian.excel.entity.params.ExcelExportEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:22:20
 */
public interface IExcelExportStyler {

    String fontName = "微软雅黑";

    /**
     * 列表头样式
     *
     * @param headerColor
     * @return
     */
    CellStyle getHeaderStyle(short headerColor);

    /**
     * 标题样式
     *
     * @param color
     * @return
     */
    CellStyle getTitleStyle(short color);

    /**
     * 获取样式方法
     *
     * @param parity
     * @param entity
     * @return
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
     *
     * @param isSingle
     * @param excelForEachParams
     * @return
     */
    CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams);
}
