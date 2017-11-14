package com.zhongweixian.excel.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @author : caoliang
 * @date : 2017/11/14  下午3:25
 */
public class PoiValidationUtil {

    private final static Validator VALIDATOR;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        VALIDATOR = factory.getValidator();
    }

    public static String validation(Object obj, Class[] verfiyGroup) {
        Set<ConstraintViolation<Object>> set = null;
        if(verfiyGroup != null){
            set = VALIDATOR.validate(obj,verfiyGroup);
        }else{
            set = VALIDATOR.validate(obj);
        }
        if (set!= null && set.size() > 0) {
            return getValidateErrMsg(set);
        }
        return null;
    }

    private static String getValidateErrMsg(Set<ConstraintViolation<Object>> set) {
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<Object> constraintViolation : set) {
            builder.append(constraintViolation.getMessage()).append(",");
        }
        return builder.substring(0, builder.length() - 1).toString();
    }

}