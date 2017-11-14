package com.zhongweixian.excel.handler.impl;

import com.zhongweixian.excel.handler.inter.IExcelDataHandler;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;

import java.util.Map;

/**
 * @author : caoliang
 * @date : 2017/11/14  下午3:23
 */
public abstract class ExcelDataHandlerDefaultImpl<T> implements IExcelDataHandler<T> {
    /**
     * 需要处理的字段
     */
    private String[] needHandlerFields;

    @Override
    public Object exportHandler(T obj, String name, Object value) {
        return value;
    }

    @Override
    public String[] getNeedHandlerFields() {
        return needHandlerFields;
    }

    @Override
    public Object importHandler(T obj, String name, Object value) {
        return value;
    }

    @Override
    public void setNeedHandlerFields(String[] needHandlerFields) {
        this.needHandlerFields = needHandlerFields;
    }

    @Override
    public void setMapValue(Map<String, Object> map, String originKey, Object value) {
        map.put(originKey, value);
    }

    @Override
    public Hyperlink getHyperlink(CreationHelper creationHelper, T obj, String name, Object value) {
        return null;
    }

}
