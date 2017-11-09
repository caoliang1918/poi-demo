package com.zhongweixian.excel.view;

import com.zhongweixian.excel.entity.vo.ExcelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author : caoliang1918@aliyun.com
 *
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

    public static void render(String fileName , Map<String, Object> model, HttpServletRequest request,
                       HttpServletResponse response, String viewName) {
        PoiBaseView view = null;

        switch (viewName){
            case ExcelConstants.POI_BIG_EXCEL_VIEW:
                view = new PoiBigExcelExportView();
                break;
            case ExcelConstants.POI_EXCEL_VIEW:
                view = new PoiSingleExcelView();
                break;
            case ExcelConstants.POI_TEMPLATE_EXCEL_VIEW:
                view = new PoiTemplateExcelView();
            case ExcelConstants.POI_MAP_EXCEL_VIEW:
                view = new PoiMapExcelView();
                break;
            case ExcelConstants.MAP_GRAPH_EXCEL_VIEW:
                view = new MapGraphExcelView();
                break;
            default :
                break;
        }
        try {
            view.renderMergedOutputModel(fileName , model, request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    abstract void renderMergedOutputModel(String fileName , Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)throws Exception;

}
