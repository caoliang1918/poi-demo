package com.zhongweixian.excel.view;

import com.zhongweixian.excel.ExcelExportUtil;
import com.zhongweixian.excel.entity.ExportParams;
import com.zhongweixian.excel.entity.params.ExcelExportEntity;
import com.zhongweixian.excel.entity.vo.ExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author : caoliang
 * @date : 2017/11/6:下午2:02
 */
public class PoiMapExcelView extends MiniAbstractExcelView {

    @Override
    protected void renderMergedOutputModel(String fileName, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Workbook workbook = ExcelExportUtil.exportExcel(
                (ExportParams) model.get(ExcelConstants.PARAMS),
                (List<ExcelExportEntity>) model.get(ExcelConstants.ENTITY_LIST),
                (Collection<? extends Map<?, ?>>) model.get(ExcelConstants.MAP_LIST));
        if (model.containsKey(ExcelConstants.FILE_NAME)) {
            fileName = (String) model.get(ExcelConstants.FILE_NAME);
        }
        if (workbook instanceof HSSFWorkbook) {
            fileName += HSSF;
        } else {
            fileName += XSSF;
        }
        if (isIE(request)) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + fileName);
        response.setContentType(CONTENT_TYPE);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }
}
