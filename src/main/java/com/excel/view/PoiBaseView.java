package com.excel.view;

import com.excel.entity.vo.ExcelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:21:57
 */
public abstract class PoiBaseView extends AbstractView {
    private static final Logger logger = LoggerFactory.getLogger(PoiBaseView.class);

    protected static boolean isIE(HttpServletRequest request) {
        return (request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0
                || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0
                || request.getHeader("USER-AGENT").toLowerCase().indexOf("edge") > 0) ? true
                : false;
    }

    public static void render(Map<String, Object> model, HttpServletRequest request,
                              HttpServletResponse response, String viewName) {
        PoiBaseView view = null;
        if (ExcelConstants.EASYPOI_BIG_EXCEL_VIEW.equals(viewName)) {
            view = new PoiBigExcelExportView();
        } else if (ExcelConstants.EASYPOI_MAP_EXCEL_VIEW.equals(viewName)) {
            view = new PoiMapExcelView();
        } else if (ExcelConstants.EASYPOI_EXCEL_VIEW.equals(viewName)) {
            view = new PoiSingleExcelView();
        } else if (ExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW.equals(viewName)) {
            view = new PoiTemplateExcelView();
        } else if (ExcelConstants.MAP_GRAPH_EXCEL_VIEW.equals(viewName)) {
            view = new MapGraphExcelView();
        }
        try {
            view.renderMergedOutputModel(model, request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
