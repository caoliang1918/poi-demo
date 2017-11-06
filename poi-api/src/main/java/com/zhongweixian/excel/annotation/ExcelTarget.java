package com.zhongweixian.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:23:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface ExcelTarget {
    /**
     * 定义excel导出ID 来限定导出字段
     */
    public String value();

}
