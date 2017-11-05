package com.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/4:22:32
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {
    String databaseFormat() default "yyyyMMddHHmmss";

    String exportFormat() default "";

    String importFormat() default "";

    String format() default "";

    String numFormat() default "";

    /** @deprecated */
    @Deprecated
    double height() default 10.0D;

    int imageType() default 1;

    String suffix() default "";

    boolean isWrap() default true;

    int[] mergeRely() default {};

    boolean mergeVertical() default false;

    String name();

    String groupName() default "";

    boolean needMerge() default false;

    String orderNum() default "0";

    String[] replace() default {};

    String savePath() default "upload";

    int type() default 1;

    double width() default 10.0D;

    boolean isStatistics() default false;

    boolean isHyperlink() default false;

    String isImportField() default "false";


}
