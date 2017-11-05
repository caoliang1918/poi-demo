package com.excel.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:20:39
 */
public abstract class ExcelBaseView extends AbstractView {
    static Logger logger = LoggerFactory.getLogger(ExcelBaseView.class);

    private static final String CONTENT_TYPE = "application/vnd.ms-excel";
    protected static final String HSSF = ".xls";
    protected static final String XSSF = ".xlsx";

    public ExcelBaseView() {
        setContentType(CONTENT_TYPE);
    }

    protected boolean isIE(HttpServletRequest request) {
        return (request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request
                .getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0) ? true : false;
    }

    public static void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response, String viewName) {
        Object view = null;
        if ("poiBigExcelView".equals(viewName)) {
            view = new PoiBigExcelExportView();
        } else if ("easypoiMapExcelView".equals(viewName)) {
            view = new PoiMapExcelView();
        } else if ("easypoiExcelView".equals(viewName)) {
            view = new PoiSingleExcelView();
        } else if ("easypoiTemplateExcelView".equals(viewName)) {
            view = new PoiTemplateExcelView();
        } else if ("MapGraphExcelView".equals(viewName)) {
            view = new MapGraphExcelView();
        }

        try {
            ((ExcelBaseView) view).renderMergedOutputModel(model, request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}
