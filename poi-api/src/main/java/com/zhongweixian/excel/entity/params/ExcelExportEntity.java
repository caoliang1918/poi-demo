package com.zhongweixian.excel.entity.params;

import java.util.List;

/**
 * @author caoliang1918@aliyun.com
 *
 * @Date 2017/11/5:20:55
 */
public class ExcelExportEntity extends ExcelBaseEntity implements Comparable<ExcelExportEntity> {

    /**
     * 如果是MAP导出,这个是map的key
     */
    private Object key;

    private double width = 10;

    private double height = 10;

    /**
     * 图片的类型,1是文件,2是数据库
     */
    private int exportImageType = 0;

    /**
     * 排序顺序
     */
    private int orderNum = 0;

    /**
     * 是否支持换行
     */
    private boolean isWrap;

    /**
     * 是否需要合并
     */
    private boolean needMerge;
    /**
     * 单元格纵向合并
     */
    private boolean mergeVertical;
    /**
     * 合并依赖
     */
    private int[] mergeRely;
    /**
     * 后缀
     */
    private String suffix;
    /**
     * 统计
     */
    private boolean isStatistics;

    private String numFormat;

    private List<ExcelExportEntity> list;

    public ExcelExportEntity() {
    }
    public ExcelExportEntity(String name) {
        super.name = name;
    }

    public ExcelExportEntity(String name, Object key) {
        super.name = name;
        this.key = key;
    }
    public ExcelExportEntity(String name, Object key, int width) {
        super.name = name;
        this.width = width;
        this.key = key;
    }

    @Override
    public int compareTo(ExcelExportEntity prev) {
        return this.getOrderNum() - prev.getOrderNum();
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getExportImageType() {
        return exportImageType;
    }

    public void setExportImageType(int exportImageType) {
        this.exportImageType = exportImageType;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public boolean isWrap() {
        return isWrap;
    }

    public void setWrap(boolean wrap) {
        isWrap = wrap;
    }

    public boolean isNeedMerge() {
        return needMerge;
    }

    public void setNeedMerge(boolean needMerge) {
        this.needMerge = needMerge;
    }

    public boolean isMergeVertical() {
        return mergeVertical;
    }

    public void setMergeVertical(boolean mergeVertical) {
        this.mergeVertical = mergeVertical;
    }

    public int[] getMergeRely() {
        return mergeRely;
    }

    public void setMergeRely(int[] mergeRely) {
        this.mergeRely = mergeRely;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isStatistics() {
        return isStatistics;
    }

    public void setStatistics(boolean statistics) {
        isStatistics = statistics;
    }

    public String getNumFormat() {
        return numFormat;
    }

    public void setNumFormat(String numFormat) {
        this.numFormat = numFormat;
    }

    public List<ExcelExportEntity> getList() {
        return list;
    }

    public void setList(List<ExcelExportEntity> list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ExcelExportEntity that = (ExcelExportEntity) obj;
        if (key == null) {
            if (that.key != null) {
                return false;
            }
        } else if (!key.equals(that.key)) {
            return false;
        }
        return list != null ? list.equals(that.list) : that.list == null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }
}
