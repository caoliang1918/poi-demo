package com.zhongweixian.controller;

import com.zhongweixian.entity.ContactsEntity;
import com.zhongweixian.excel.entity.ExportParams;
import com.zhongweixian.excel.entity.enmus.ExcelType;
import com.zhongweixian.excel.entity.params.ExcelExportEntity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("mapData")
    public void mapData(ModelMap modelMap, HttpServletRequest request , HttpServletResponse response) {
        List<ExcelExportEntity> header = new ArrayList<ExcelExportEntity>();
        ExcelExportEntity excelEntity = new ExcelExportEntity("姓名", "name");
        excelEntity.setNeedMerge(true);
        header.add(excelEntity);
        header.add(new ExcelExportEntity("性别", "sex"));
        excelEntity = new ExcelExportEntity("", "students");
        List<ExcelExportEntity> temp = new ArrayList<ExcelExportEntity>();
        temp.add(new ExcelExportEntity("姓名", "name"));
        temp.add(new ExcelExportEntity("性别", "sex"));
        excelEntity.setList(temp);
        header.add(excelEntity);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map;
        for (int i = 0; i < 10; i++) {
            map = new HashMap<String, Object>();
            map.put("name", "1" + i);
            map.put("sex", "2" + i);

            List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
            tempList.add(map);
            tempList.add(map);
            map.put("students", tempList);
            list.add(map);
        }

        ExportParams params = new ExportParams("测试map导出", ExcelType.XSSF);
        params.setFreezeRow(1);
        modelMap.put(ExcelConstants.MAP_LIST, list);
        modelMap.put(ExcelConstants.HEADER, header);
        modelMap.put(ExcelConstants.PARAMS, params);
        PoiBaseView.render("map导出",modelMap, request, response, ExcelConstants.POI_MAP_EXCEL_VIEW);

    }
}
