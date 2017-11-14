package com.zhongweixian.excel.handler.inter;

/**
 * @author : caoliang
 * @date : 2017/11/14  下午3:22
 */
public interface IExcelModel {

    /**
     * 获取错误数据
     *
     * @return
     */
    public String getErrorMsg();

    /**
     * 设置错误信息
     *
     * @param errorMsg
     */
    public void setErrorMsg(String errorMsg);

}
