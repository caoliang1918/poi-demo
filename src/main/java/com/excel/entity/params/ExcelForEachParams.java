package com.excel.entity.params;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:22:21
 */
public class ExcelForEachParams {

    /**
     * key
     */
    private String            name;
    /**
     * 模板的cellStyle
     */
    private CellStyle cellStyle;
    /**
     * 行高
     */
    private short             height;
    /**
     * 常量值
     */
    private String            constValue;
    /**
     * 列合并
     */
    private int               colspan          = 1;
    /**
     * 行合并
     */
    private int               rowspan          = 1;

    private boolean           needSum;

    public ExcelForEachParams() {

    }

    public ExcelForEachParams(String name, CellStyle cellStyle, short height) {
        this.name = name;
        this.cellStyle = cellStyle;
        this.height = height;
    }

    public ExcelForEachParams(String name, CellStyle cellStyle, short height,boolean needSum) {
        this.name = name;
        this.cellStyle = cellStyle;
        this.height = height;
        this.needSum = needSum;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public String getConstValue() {
        return constValue;
    }

    public void setConstValue(String constValue) {
        this.constValue = constValue;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public boolean isNeedSum() {
        return needSum;
    }

    public void setNeedSum(boolean needSum) {
        this.needSum = needSum;
    }
}
