package com.zhongweixian.excel.entity;

import com.zhongweixian.excel.handler.inter.IExcelDataHandler;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:22:16
 */
public class ExcelBaseParams {

    /**
     * 数据处理接口,以此为主,replace,format都在这后面
     */
    private IExcelDataHandler dataHanlder;

    public IExcelDataHandler getDataHanlder() {
        return dataHanlder;
    }

    public void setDataHanlder(IExcelDataHandler dataHanlder) {
        this.dataHanlder = dataHanlder;
    }

}
