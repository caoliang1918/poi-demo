/**
 * Copyright 2013-2015 JueYue (qrb.jueyue@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excel.view;

import com.excel.ExcelExportUtil;
import com.excel.entity.ExportParams;
import com.excel.entity.vo.ExcelConstants;
import com.excel.export.base.ExcelExportServer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author JueYue on 14-3-8. Excel 生成解析器,减少用户操作
 */
@SuppressWarnings("unchecked")
@Controller(ExcelConstants.EXCEL_VIEW)
public class PoiSingleExcelView extends AbstractExcelView {

    public PoiSingleExcelView() {
        super();
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        String codedFileName = "临时文件";
        Workbook workbook = null;
        if (model.containsKey(ExcelConstants.MAP_LIST)) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) model
                    .get(ExcelConstants.MAP_LIST);
            if (list.size() == 0) {
                throw new RuntimeException("MAP_LIST IS NULL");
            }
            workbook = ExcelExportUtil.exportExcel(
                    (ExportParams) list.get(0).get(ExcelConstants.PARAMS), (Class<?>) list.get(0)
                            .get(ExcelConstants.CLASS),
                    (Collection<?>) list.get(0).get(ExcelConstants.DATA_LIST));
            for (int i = 1; i < list.size(); i++) {
                new ExcelExportServer().createSheet(workbook,
                        (ExportParams) list.get(i).get(ExcelConstants.PARAMS), (Class<?>) list
                                .get(i).get(ExcelConstants.CLASS),
                        (Collection<?>) list.get(i).get(ExcelConstants.DATA_LIST));
            }
        } else {
            workbook = ExcelExportUtil.exportExcel(
                    (ExportParams) model.get(ExcelConstants.PARAMS),
                    (Class<?>) model.get(ExcelConstants.CLASS),
                    (Collection<?>) model.get(ExcelConstants.DATA_LIST));
        }
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
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }
}
