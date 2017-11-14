package com.zhongweixian.excel.handler.inter;

import com.zhongweixian.excel.entity.result.ExcelVerifyHanlderResult;

/**
 * @author : caoliang
 * @date : 2017/11/14  下午2:16
 */
public interface IExcelVerifyHandler<T> {

    ExcelVerifyHanlderResult verifyHandler(T obj);
}
