package com.zhongweixian.excel.view;

import com.zhongweixian.excel.ExcelExportUtil;
import com.zhongweixian.excel.entity.ExportParams;
import com.zhongweixian.excel.entity.vo.ExcelConstants;
import com.zhongweixian.excel.handler.inter.IExcelExportServer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author : caoliang
 * @date : 2017/11/6:下午2:02
 */
public class PoiBigExcelExportView extends MiniAbstractExcelView {

    @Override
    protected void renderMergedOutputModel(String fileName, Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        Workbook workbook = ExcelExportUtil.exportBigExcel(
                (ExportParams) model.get(ExcelConstants.PARAMS),
                (Class<?>) model.get(ExcelConstants.CLASS), Collections.EMPTY_LIST);
        IExcelExportServer server = (IExcelExportServer) model.get(ExcelConstants.DATA_INTER);
        int page = 2;
        List<Object> list = server.selectListForExcelExport(model.get(ExcelConstants.DATA_PARAMS), page++);
        while (list != null && list.size() > 0) {
            workbook = ExcelExportUtil.exportBigExcel(
                    (ExportParams) model.get(ExcelConstants.PARAMS),
                    (Class<?>) model.get(ExcelConstants.CLASS), list);
            list = server.selectListForExcelExport(model.get(ExcelConstants.DATA_PARAMS),
                    page++);
        }
        ExcelExportUtil.closeExportBigExcel();

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
