package com.bckefu.controller;

import com.bckefu.entity.ContactsEntity;
import com.zhongweixian.excel.ExcelExportUtil;
import com.zhongweixian.excel.entity.ExportParams;
import com.zhongweixian.excel.entity.enmus.ExcelType;
import com.zhongweixian.excel.entity.params.ExcelExportEntity;
import com.zhongweixian.excel.entity.vo.ExcelConstants;
import com.zhongweixian.excel.handler.inter.IExcelExportServer;
import com.zhongweixian.excel.view.AbstractExcelView;
import com.zhongweixian.excel.view.BaseView;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : caoliang
 * @date : 2017/11/6:下午3:51
 */

@RestController
@RequestMapping("export")
public class ExportController {

    @Autowired
    private IExcelExportServer excelExportServer;

    @ApiOperation(value = "导出bigData", tags = "1.2.1")
    @GetMapping("bigData")
    public void downloadByPoiBaseView(ModelMap map, HttpServletRequest request,
                                      HttpServletResponse response) {
        ExportParams params = new ExportParams("测试sheetName", ExcelType.XSSF);
        //params.setFreezeCol(0); //冻结列
        params.setFreezeRow(1); //冻结行
        map.put(ExcelConstants.CLASS, ContactsEntity.class);
        map.put(ExcelConstants.PARAMS, params);
        //就是我们的查询参数,会带到接口中,供接口查询使用
        map.put(ExcelConstants.DATA_PARAMS, new HashMap<String, String>());
        map.put(ExcelConstants.DATA_INTER, excelExportServer);
        BaseView.render("测试下载", map, request, response, ExcelConstants.POI_BIG_EXCEL_VIEW);
    }

    @ApiOperation(value = "视图导出mapData", tags = "1.2.2")
    @GetMapping("viewMapData")
    public void mapData(ModelMap modelMap, HttpServletRequest request,
                        HttpServletResponse response) {
        List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
        entity.add(new ExcelExportEntity("姓名", "name"));
        entity.add(new ExcelExportEntity("性别", "sex"));
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (int i = 0; i < 10; i++) {
            map = new HashMap<String, Object>();
            map.put("name", "1" + i);
            map.put("sex", "2" + i);
            list.add(map);
        }
        ExportParams params = new ExportParams("测试", ExcelType.XSSF);
        params.setFreezeRow(1);
        modelMap.put(ExcelConstants.MAP_LIST, list);
        modelMap.put(ExcelConstants.ENTITY_LIST, entity);
        modelMap.put(ExcelConstants.PARAMS, params);
        BaseView.render("map导出", modelMap, request, response, ExcelConstants.POI_MAP_EXCEL_VIEW);
    }

    @ApiOperation(value = "直接导出mapData", tags = "1.2.3")
    @GetMapping("simpleMapData")
    public void simpleMapData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
        entityList.add(new ExcelExportEntity("姓名", "name"));
        entityList.add(new ExcelExportEntity("性别", "sex"));
        entityList.add(new ExcelExportEntity("年龄", "age"));
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (int i = 0; i < 10; i++) {
            map = new HashMap<String, Object>();
            map.put("name", "1" + i);
            map.put("sex", "2" + i);
            map.put("age", i);
            list.add(map);
        }
        ExportParams exportParams = new ExportParams("测试", ExcelType.XSSF);
        exportParams.setFreezeRow(1);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, entityList, list);

        String fileName = RandomStringUtils.randomAlphanumeric(6);
        if (workbook instanceof HSSFWorkbook) {
            fileName += AbstractExcelView.HSSF;
        } else {
            fileName += AbstractExcelView.XSSF;
        }
        if (BaseView.isIE(request)) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + fileName);
        response.setContentType(AbstractExcelView.CONTENT_TYPE);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

}
