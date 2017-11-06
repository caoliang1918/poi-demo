package com.zhongweixian.controller;

import com.zhongweixian.entity.ContactsEntity;
import com.zhongweixian.excel.entity.ExportParams;
import com.zhongweixian.excel.entity.enmus.ExcelType;
import com.zhongweixian.excel.entity.vo.ExcelConstants;
import com.zhongweixian.excel.handler.inter.IExcelExportServer;
import com.zhongweixian.excel.view.PoiBaseView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author : caoliang
 * @date : 2017/11/6:下午3:51
 */

@RestController
@RequestMapping("download")
public class TestController {

    @Autowired
    IExcelExportServer excelExportServer;

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
        PoiBaseView.render("测试下载", map, request, response, ExcelConstants.POI_BIG_EXCEL_VIEW);

    }
}
