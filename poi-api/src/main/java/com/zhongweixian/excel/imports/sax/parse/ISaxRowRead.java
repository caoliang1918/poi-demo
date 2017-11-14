package com.zhongweixian.excel.imports.sax.parse;

import com.zhongweixian.excel.imports.sax.SaxReadCellEntity;

import java.util.List;

/**
 * @author : caoliang
 * @date : 2017/11/14  下午2:36
 */
public interface ISaxRowRead {

    /**
     * 获取返回数据
     *
     * @param <T>
     * @return
     */
    <T> List<T> getList();

    /**
     * 解析数据
     *
     * @param index
     * @param datas
     */
    void parse(int index, List<SaxReadCellEntity> datas);

}
