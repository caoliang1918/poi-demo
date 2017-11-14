package com.zhongweixian.excel.handler.inter;

import java.util.List;

/**
 * @author : caoliang
 * @date : 2017/11/6:下午2:49
 */
public interface IExcelExportServer {
    /**
     * 查询数据接口
     * @param obj    查询条件
     * @param page   当前页数
     * @return
     */
    List<Object> selectListForExcelExport(Object obj, int page);

}

