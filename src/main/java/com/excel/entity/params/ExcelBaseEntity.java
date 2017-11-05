package com.excel.entity.params;

import com.excel.entity.vo.BaseEntityTypeConstants;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:20:52
 */
public class ExcelBaseEntity {

    /**
     * 对应name
     */
    protected String name;
    /**
     * 对应groupName
     */
    protected String groupName;
    /**
     * 对应type
     */
    private int type = BaseEntityTypeConstants.STRING_TYPE;
    /**
     * 数据库格式
     */
    private String databaseFormat;
    /**
     * 导出日期格式
     */
    private String format;
    /**
     * 导出日期格式
     */
    private String[] replace;
    /**
     * set/get方法
     */
    private Method method;
    /**
     * 这个是不是超链接,如果是需要实现接口返回对象
     */
    private boolean hyperlink;

    private List<Method> methods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDatabaseFormat() {
        return databaseFormat;
    }

    public void setDatabaseFormat(String databaseFormat) {
        this.databaseFormat = databaseFormat;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String[] getReplace() {
        return replace;
    }

    public void setReplace(String[] replace) {
        this.replace = replace;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public boolean isHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(boolean hyperlink) {
        this.hyperlink = hyperlink;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }
}
