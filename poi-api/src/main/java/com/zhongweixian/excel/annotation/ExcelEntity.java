package com.zhongweixian.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/4:22:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelEntity {


    /**
     * 定义excel导出ID 来限定导出字段,处理一个类对应多个不同名称的情况
     */
    public String id() default "";

    /**
     * 导出时，对应数据库的字段 主要是用户区分每个字段， 不能有annocation重名的 导出时的列名
     * 导出排序跟定义了annotation的字段的顺序有关 可以使用a_id,b_id来确实是否使用
     */
    public String name() default "";

    /**
     * 如果等于true,name必须有值, Excel的表头会变成两行,同时改Excel内部数据不参与总排序,排序用下面这个来代替,内部再排序
     * 排序取当中最小值排序
     *
     * @return
     */
    public boolean show() default false;
}
