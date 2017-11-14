package com.zhongweixian.excel.handler.inter;

/**
 * @author : caoliang
 * @date : 2017/11/14  下午3:13
 */
public interface IExcelReadRowHanlder<T> {
    /**
     * 处理解析对象
     * @param t
     */
    public void hanlder(T t);

}
