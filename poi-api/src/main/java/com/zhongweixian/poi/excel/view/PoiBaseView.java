package com.zhongweixian.poi.excel.view;

import com.zhongweixian.poi.excel.entity.vo.ExcelConstants;
import com.zhongweixian.poi.excel.entity.vo.MapExcelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author : caoliang
 * @date : 2017/11/6:下午2:00
 */
public abstract class PoiBaseView {

    private static final Logger logger = LoggerFactory.getLogger(PoiBaseView.class);

    protected static boolean isIE(HttpServletRequest request) {
        return (request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0
                || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0
                || request.getHeader("USER-AGENT").toLowerCase().indexOf("edge") > 0) ? true
                : false;
    }

    public void render(String fileName , Map<String, Object> model, HttpServletRequest request,
                       HttpServletResponse response, String viewName) {
        PoiBaseView view = null;
        if (ExcelConstants.EASYPOI_BIG_EXCEL_VIEW.equals(viewName)) {
            view = new EasypoiBigExcelExportView();
        } else if (MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW.equals(viewName)) {
            view = new EasypoiMapExcelView();
        } else if (ExcelConstants.EASYPOI_EXCEL_VIEW.equals(viewName)) {
            view = new EasypoiSingleExcelView();
        } else if (ExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW.equals(viewName)) {
            view = new EasypoiTemplateExcelView();
        } else if (ExcelConstants.MAP_GRAPH_EXCEL_VIEW.equals(viewName)) {
            view = new MapGraphExcelView();
        }
        try {
            view.renderMergedOutputModel(fileName , model, request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    abstract void renderMergedOutputModel(String fileName , Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)throws Exception;

}
