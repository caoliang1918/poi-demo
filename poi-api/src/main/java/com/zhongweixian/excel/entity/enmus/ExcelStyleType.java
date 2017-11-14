package com.zhongweixian.excel.entity.enmus;

import com.zhongweixian.excel.export.style.ExcelExportStylerBorderImpl;
import com.zhongweixian.excel.export.style.ExcelExportStylerColorImpl;
import com.zhongweixian.excel.export.style.ExcelExportStylerDefaultImpl;

/**
 * 默认样式
 *
 * @author : caoliang
 * @date : 2017/11/14  下午2:42
 */
public enum ExcelStyleType {

    NONE("默认样式", ExcelExportStylerDefaultImpl.class),
    BORDER("边框样式", ExcelExportStylerBorderImpl.class),
    COLOR("间隔行样式", ExcelExportStylerColorImpl.class);

    private String name;
    private Class<?> clazz;

    ExcelStyleType(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

}
