package com.excel.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:21:56
 */
public class AbstractExcelView extends PoiBaseView {

    private static final String   CONTENT_TYPE = "text/html;application/vnd.ms-excel";

    protected static final String HSSF         = ".xls";
    protected static final String XSSF         = ".xlsx";

    public AbstractExcelView() {
        setContentType(CONTENT_TYPE);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

    }

}
