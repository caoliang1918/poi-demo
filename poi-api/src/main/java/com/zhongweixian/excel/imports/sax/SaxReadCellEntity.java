package com.zhongweixian.excel.imports.sax;

import com.zhongweixian.excel.entity.enmus.CellValueType;

/**
 * @author : caoliang
 * @date : 2017/11/14  下午3:07
 */
public class SaxReadCellEntity {
    /**
     * 值类型
     */
    private CellValueType cellType;
    /**
     * 值
     */
    private Object value;

    public SaxReadCellEntity(CellValueType cellType, Object value) {
        this.cellType = cellType;
        this.value = value;
    }

    public CellValueType getCellType() {
        return cellType;
    }

    public void setCellType(CellValueType cellType) {
        this.cellType = cellType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "[type=" + cellType.toString() + ",value=" + value + "]";
    }

}
