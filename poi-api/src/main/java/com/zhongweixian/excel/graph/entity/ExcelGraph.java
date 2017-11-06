package com.zhongweixian.excel.graph.entity;

import java.util.List;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:23:24
 */
public interface ExcelGraph {
     ExcelGraphElement getCategory();
     List<ExcelGraphElement> getValueList();
     Integer getGraphType();
     List<ExcelTitleCell> getTitleCell();
     List<String> getTitle();
}
