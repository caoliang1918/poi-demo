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
public class EasypoiBigExcelExportView extends MiniAbstractExcelView {

    @Override
    protected void renderMergedOutputModel(String fileName , Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        String codedFileName = "临时文件";
        Workbook workbook = ExcelExportUtil.exportBigExcel(
                (ExportParams) model.get(ExcelConstants.PARAMS),
                (Class<?>) model.get(ExcelConstants.CLASS), Collections.EMPTY_LIST);
        IExcelExportServer server = (IExcelExportServer) model.get(ExcelConstants.DATA_INTER);
        int page = 1;
        List<Object> list = server.selectListForExcelExport(model.get(ExcelConstants.DATA_PARAMS), page++);
        while (list != null && list.size() > 0) {
            workbook = ExcelExportUtil.exportBigExcel(
                    (ExportParams) model.get(ExcelConstants.PARAMS),
                    (Class<?>) model.get(ExcelConstants.CLASS), list);
            list = server.selectListForExcelExport(model.get(ExcelConstants.DATA_PARAMS),
                    page++);
        }
        ExcelExportUtil.closeExportBigExcel();
        if (model.containsKey(ExcelConstants.FILE_NAME)) {
            codedFileName = (String) model.get(ExcelConstants.FILE_NAME);
        }
        if (workbook instanceof HSSFWorkbook) {
            codedFileName += HSSF;
        } else {
            codedFileName += XSSF;
        }
        if (isIE(request)) {
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF8");
        } else {
            codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
        response.setContentType(CONTENT_TYPE);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }
}
